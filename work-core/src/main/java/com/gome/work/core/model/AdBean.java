package com.gome.work.core.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Create by liupeiquan on 2018/9/19
 * 开屏广告
 */
public class AdBean {
    @Expose
    public long endDate;

    @Expose
    @SerializedName("url")
    public String linkUrl;

    @Expose
    public String image;

    @Expose
    public int stayDuration = 3;

    @Expose
    public String mediaType;

    @Expose
    public String name;


    /**
     * 是否广告已失效
     *
     * @return
     */
    public boolean isExpired() {
        long time = System.currentTimeMillis();
        return endDate < time;
    }


    @Override
    public String toString() {
        return "AdBean{" +
                ", endDate=" + endDate +
                ", linkUrl='" + linkUrl + '\'' +
                ", stayDuration=" + stayDuration +
                ", mediaType='" + mediaType + '\'' +
                '}';
    }
}
