package com.gome.work.common.webview.jsbridge;

/**
 * js传过来的待执行的任务
 */

public class JsTask {

    public String action;

    public String param;

    public String callbackId;

    public String result;

    public JsTask(String action, String param, String callbackId) {
        this.action = action;
        this.param = param;
        this.callbackId = callbackId;
    }

}
