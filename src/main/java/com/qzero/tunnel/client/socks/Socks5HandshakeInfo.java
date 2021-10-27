package com.qzero.tunnel.client.socks;

public class Socks5HandshakeInfo {

    public enum ConnectType{
        IPV4,
        DOMAIN
    }

    private ConnectType type;
    private String domainName;
    private String ipv4Address;
    private int port;

    public Socks5HandshakeInfo(ConnectType type, String domainName, String ipv4Address, int port) {
        this.type = type;
        this.domainName = domainName;
        this.ipv4Address = ipv4Address;
        this.port = port;
    }

    public Socks5HandshakeInfo() {
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getIpv4Address() {
        return ipv4Address;
    }

    public void setIpv4Address(String ipv4Address) {
        this.ipv4Address = ipv4Address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ConnectType getType() {
        return type;
    }

    public void setType(ConnectType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Socks5HandshakeInfo{" +
                "type=" + type +
                ", domainName='" + domainName + '\'' +
                ", ipv4Address='" + ipv4Address + '\'' +
                ", port=" + port +
                '}';
    }
}
