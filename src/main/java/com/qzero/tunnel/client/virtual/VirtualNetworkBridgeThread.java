package com.qzero.tunnel.client.virtual;

import com.qzero.tunnel.client.config.GlobalConfigStorage;
import com.qzero.tunnel.client.SpringUtil;
import com.qzero.tunnel.client.data.ServerProfile;
import com.qzero.tunnel.client.data.UserToken;
import com.qzero.tunnel.client.socks.NewClientConnectedCallback;
import com.qzero.tunnel.client.socks.Socks5HandshakeInfo;
import com.qzero.tunnel.client.socks.Socks5Server;
import com.qzero.tunnel.client.socks.StreamUtils;
import com.qzero.tunnel.crypto.CryptoModule;
import com.qzero.tunnel.crypto.CryptoModuleFactory;
import com.qzero.tunnel.crypto.DataWithLength;
import com.qzero.tunnel.crypto.modules.PlainModule;
import com.qzero.tunnel.relay.RelaySession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class VirtualNetworkBridgeThread extends Thread {

    private int localPort;
    private int remotePort;

    private Logger log= LoggerFactory.getLogger(getClass());

    private Socks5Server socks5Server;

    private ServerProfile serverProfile;
    private UserToken userToken;

    private NewClientConnectedCallback callback=(clientId, connection) -> {
        log.info("Received socks5 connection "+connection.getHandshakeInfo());
        Socket remote;
        try {
            String ip= serverProfile.getServerIp();
            remote=new Socket(ip,remotePort);
        }catch (Exception e){
            log.error("Failed to connect to tunnel server");

            try {
                connection.getLocalSocket().close();
            }catch (Exception e1){}

            return;
        }

        //Read crypto module name first, then initialize crypto module
        CryptoModule cryptoModule;
        try {
            int length= StreamUtils.readIntWith4Bytes(remote.getInputStream());
            byte[] buf=StreamUtils.readSpecifiedLengthDataFromInputStream(remote.getInputStream(),length);
            String name=new String(buf, StandardCharsets.UTF_8);

            cryptoModule= CryptoModuleFactory.getModule(name);
            if(cryptoModule==null)
                throw new Exception(String.format("Crypto module with name %s does not exist", name));
        }catch (Exception e){
            log.error("Failed to read crypto module name or initialize crypto module",e);

            try {
                remote.close();
                connection.getLocalSocket().close();
            }catch (Exception e1){}

            return;
        }

        //Do crypto module handshake
        try {
            cryptoModule.doHandshakeAsClient(remote.getInputStream(),remote.getOutputStream());
        }catch (Exception e){
            log.error("Failed to do crypto module handshake",e);

            try {
                remote.close();
                connection.getLocalSocket().close();
            }catch (Exception e1){}

            return;
        }

        //Construct dst info and encrypt it and send

        try {
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            /**
             * 4 bytes length of dstIdentity
             * n bytes dstIdentity
             * 4 bytes port
             */
            Socks5HandshakeInfo handshakeInfo=connection.getHandshakeInfo();

            String dstIdentity=handshakeInfo.getDomainName();
            if(dstIdentity==null)
                dstIdentity=handshakeInfo.getIpv4Address();

            StreamUtils.writeIntWith4Bytes(byteArrayOutputStream,dstIdentity.getBytes(StandardCharsets.UTF_8).length);
            byteArrayOutputStream.write(dstIdentity.getBytes(StandardCharsets.UTF_8));
            StreamUtils.writeIntWith4Bytes(byteArrayOutputStream,handshakeInfo.getPort());

            DataWithLength data=cryptoModule.encrypt(new DataWithLength(byteArrayOutputStream.toByteArray(),byteArrayOutputStream.size()));
            byte[] buf=data.getData();

            OutputStream os=remote.getOutputStream();
            StreamUtils.writeIntWith4Bytes(os,buf.length);
            os.write(buf);
        }catch (Exception e){
            log.error("Failed to send dst info",e);

            try {
                remote.close();
                connection.getLocalSocket().close();
            }catch (Exception e1){}

            return;
        }

        //Start relay session
        RelaySession relaySession=new RelaySession();

        relaySession.setDirectClient(connection.getLocalSocket());
        relaySession.setTunnelClient(remote);
        relaySession.initializeCryptoModule(cryptoModule,new PlainModule());
        relaySession.startRelay();

        log.debug("Started virtual network relay session");
    };

    public VirtualNetworkBridgeThread(int localPort, int remotePort) {
        this.localPort = localPort;
        this.remotePort = remotePort;

        GlobalConfigStorage configStorage= SpringUtil.getBean(GlobalConfigStorage.class);
        serverProfile=configStorage.getCurrentServerProfile();
        userToken=configStorage.getUserToken();
    }

    @Override
    public void run() {
        super.run();

        try {
            socks5Server=new Socks5Server(localPort,callback);
            socks5Server.startServer();
        }catch (Exception e){
            log.error("Failed to start local socks5 server",e);
        }
    }

    public void closeBridge() throws Exception{
        socks5Server.stopServer();
    }


}
