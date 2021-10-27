package com.qzero.tunnel.client.socks;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Socks5HandshakeUtil {

    public static Socks5HandshakeInfo doHandshake(Socket socket) throws Exception{
        Socks5HandshakeInfo handshakeInfo=new Socks5HandshakeInfo();

        InputStream is=socket.getInputStream();
        OutputStream os=socket.getOutputStream();

        //Get version code
        int verCode=is.read();
        if(verCode!=5){
            throw new Exception("Ver code is not 5, but "+verCode);
        }

        //Get method
        int methodCount=is.read();
        List<Integer> methods=new ArrayList<>();
        for(int i=0;i<methodCount;i++){
            methods.add(is.read());
        }

        if(!methods.contains(0)){
            throw new Exception("Can not find method 0");
        }

        //Choose method 0
        os.write(5);
        os.write(0);

        //Read version code again
        verCode=is.read();

        //Read type, only accept tcp:1
        if(is.read()!=1){
            throw new Exception("Only accept tcp");
        }

        //Reserve
        is.read();

        int type=is.read();
        switch (type){
            case 1://IPV4
                handshakeInfo.setType(Socks5HandshakeInfo.ConnectType.IPV4);

                //Read ipv4 address
                int[] ip=new int[4];
                for(int i=0;i<4;i++){
                    int ipPart=is.read();
                    if(ipPart<0)
                        ipPart+=256;

                    ip[i]=ipPart;
                }

                String ipv4=ip[0]+"."+ip[1]+"."+ip[2]+"."+ip[3];
                handshakeInfo.setIpv4Address(ipv4);
                break;
            case 3://Domain
                handshakeInfo.setType(Socks5HandshakeInfo.ConnectType.DOMAIN);

                //Read domainName
                int length=is.read();
                String domainName=new String(StreamUtils.readSpecifiedLengthDataFromInputStream(is,length));

                handshakeInfo.setDomainName(domainName);
                break;
            default:
                throw new Exception("Unknown type "+type);
        }

        //Read port
        int highPort=is.read();
        if(highPort<0)
            highPort+=256;

        int lowPort=is.read();
        if(lowPort<0)
            lowPort+=256;

        int port=256*highPort+lowPort;
        handshakeInfo.setPort(port);

        //Write response, handshake over
        os.write(new byte[]{5,0,0,1,0,0,0,0,0,0});

        return handshakeInfo;
    }

}
