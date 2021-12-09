package com.qzero.tunnel.client.data;

public class VirtualNetworkMapping {

    //Such as domain or ip address
    private String dstIdentity;
    private String dstUserName;

    public VirtualNetworkMapping() {
    }

    public VirtualNetworkMapping(String dstIdentity, String dstUserName) {
        this.dstIdentity = dstIdentity;
        this.dstUserName = dstUserName;
    }

    public String getDstIdentity() {
        return dstIdentity;
    }

    public void setDstIdentity(String dstIdentity) {
        this.dstIdentity = dstIdentity;
    }

    public String getDstUserName() {
        return dstUserName;
    }

    public void setDstUserName(String dstUserName) {
        this.dstUserName = dstUserName;
    }

    @Override
    public String toString() {
        return "VirtualNetworkMapping{" +
                "dstIdentity='" + dstIdentity + '\'' +
                ", dstUserName='" + dstUserName + '\'' +
                '}';
    }
}
