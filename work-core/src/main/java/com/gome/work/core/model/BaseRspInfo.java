package com.gome.work.core.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by chaergongzi on 2018/6/19.
 */

public class BaseRspInfo<T> {

    @Expose
    @SerializedName("code")
    public String code;

    @Expose
    @SerializedName("message")
    public String message;

    @Expose
    @SerializedName("data")
    public T data;

    public boolean isSuccess() {
        return "0".equals(code);
    }

}
