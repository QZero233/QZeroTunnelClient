package com.qzero.tunnel.client.exception;

public class IllegalPortException extends IllegalArgumentException{

    public IllegalPortException(String portInString) {
        super("Illegal port: "+portInString);
    }
}
