package com.gome.work.core.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Create by liupeiquan on 2018/9/19
 * 开屏广告
 */
public class UsersRspInfo {

    @Expose
    public int pn;

    @Expose
    public int ps;

    @Expose
    public int total;
    
    @Expose
    @SerializedName("data")
    public List<UserInfo> items;
}
