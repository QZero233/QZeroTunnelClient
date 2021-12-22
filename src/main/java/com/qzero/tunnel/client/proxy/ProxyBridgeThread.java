package com.qzero.tunnel.client.proxy;

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
import com.qzero.tunnel.relay.RelaySession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ProxyBridgeThread extends Thread {

    private int localPort;
    private int remotePort;

    private Logger log= LoggerFactory.getLogger(getClass());

    private Socks5Server socks5Server;

    private ServerProfile serverProfile;
    private UserToken userToken;

    private NewClientConnectedCallback callback=(clientId,connection) -> {
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

        //Send proxy dst info
        try {
            /*
            4 bytes length of encrypted data
            n bytes encrypted data package

            within data package:
            4 bytes length of token
            n bytes token
            4 bytes length of host
            n bytes host
            4 bytes port
            */
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

            String token=userToken.getToken();

            StreamUtils.writeIntWith4Bytes(byteArrayOutputStream,token.getBytes(StandardCharsets.UTF_8).length);
            byteArrayOutputStream.write(token.getBytes(StandardCharsets.UTF_8));

            Socks5HandshakeInfo handshakeInfo=connection.getHandshakeInfo();
            String host=handshakeInfo.getIpv4Address();
            if(host==null)
                host=handshakeInfo.getDomainName();

            StreamUtils.writeIntWith4Bytes(byteArrayOutputStream,host.getBytes(StandardCharsets.UTF_8).length);
            byteArrayOutputStream.write(host.getBytes(StandardCharsets.UTF_8));

            StreamUtils.writeIntWith4Bytes(byteArrayOutputStream,handshakeInfo.getPort());

            byte[] buf=byteArrayOutputStream.toByteArray();
            DataWithLength data=new DataWithLength(buf,buf.length);
            data=cryptoModule.encrypt(data);

            if(data==null){
                //Normally it won't return null, it will throw exception
                throw new Exception("Crypto error without throwing exception");
            }

            buf=data.getData();

            OutputStream os=remote.getOutputStream();
            StreamUtils.writeIntWith4Bytes(os,buf.length);
            os.write(buf,0,data.getLength());
        }catch (Exception e){
            log.error("Failed to send proxy dst info",e);

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
        relaySession.initializeCryptoModule(cryptoModule,null);
        relaySession.startRelay();
    };

    public ProxyBridgeThread(int localPort, int remotePort) {
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
