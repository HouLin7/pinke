
package com.gome.work.core.net;

public class JsonCodeErrorException extends Throwable {

    private String serviceMsg;

    public JsonCodeErrorException() {
        super();
    }

    public JsonCodeErrorException(String msg, String serviceMsg) {
        super(msg);
        this.serviceMsg = serviceMsg;
    }

    public JsonCodeErrorException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JsonCodeErrorException(Throwable cause) {
        super(cause);
    }

    public String getServiceMsg() {
        return serviceMsg;
    }

    public void setServiceMsg(String serviceMsg) {
        this.serviceMsg = serviceMsg;
    }

    public static int getJsonErrorCode(Throwable e) {
        if (e instanceof JsonCodeErrorException) {
            return Integer.parseInt(e.getMessage());
        } else {
            return -1;
        }
    }
}
