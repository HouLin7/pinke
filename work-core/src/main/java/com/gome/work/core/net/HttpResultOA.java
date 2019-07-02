
package com.gome.work.core.net;

import com.google.gson.annotations.SerializedName;

public class HttpResultOA<T> {

    private String code;
    @SerializedName("message")
    private String msg;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
