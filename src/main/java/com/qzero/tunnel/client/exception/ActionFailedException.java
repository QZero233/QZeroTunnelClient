package com.qzero.tunnel.client.exception;

public class ActionFailedException extends Exception{

    private String actionMethodName;
    private String reason;

    public ActionFailedException(String actionMethodName, String reason) {
        super(String.format("Action %s failed\nReason: %s", actionMethodName,reason));
        this.actionMethodName = actionMethodName;
        this.reason = reason;
    }

    public String getActionMethodName() {
        return actionMethodName;
    }

    public void setActionMethodName(String actionMethodName) {
        this.actionMethodName = actionMethodName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
