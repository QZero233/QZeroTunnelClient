package com.qzero.tunnel.client.exception;

public class IllegalPortException extends ActionFailedException{

    public IllegalPortException(String portInString) {
        super("","Illegal port: "+portInString);
    }
}
