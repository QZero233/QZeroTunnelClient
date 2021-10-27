package com.qzero.tunnel.client.socks;

public class Socks5Server {

    private int port;

    private Socks5ServerAcceptThread acceptThread=null;

    private ServerStatusListener serverStatusListener;
    private NewClientConnectedCallback newClientConnectedCallback;

    private boolean running=false;

    public Socks5Server(int port,NewClientConnectedCallback newClientConnectedCallback) throws Exception {
        this.port = port;
        this.newClientConnectedCallback=newClientConnectedCallback;

        if(newClientConnectedCallback==null)
            throw new Exception("Callback can not be null, otherwise the server won't relay any data");
    }

    public void setServerStatusListener(ServerStatusListener serverStatusListener) {
        this.serverStatusListener = serverStatusListener;
        if(acceptThread!=null)
            acceptThread.setServerStatusListener(serverStatusListener);
    }

    public void startServer() throws Exception{
        if(running)
            throw new Exception("Server is running, can not start it");

        acceptThread=new Socks5ServerAcceptThread(port, new ServerStatusListener() {
            @Override
            public void onStarted() {
                running=true;
                if(serverStatusListener!=null)
                    serverStatusListener.onStarted();
            }

            @Override
            public void onStopped() {
                running=false;
                if(serverStatusListener!=null)
                    serverStatusListener.onStopped();
            }
        }, newClientConnectedCallback);
        acceptThread.start();
    }

    public void stopServer() throws Exception{
        if(!running)
            throw new Exception("Server is stopped, can not stop it");

        acceptThread.closeServer();
        acceptThread=null;
    }

}
