package com.gome.update;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class BaseRspInfo<T> implements Serializable {

    @Expose
    public String code;

    @Expose
    public String message;

    @Expose
    public T data;


    public boolean isSuccess() {
        return "0".equals(code);
    }


}
