package com.gome.work.core.model;

import com.google.gson.annotations.Expose;

/**
 * Create by liupeiquan on 2018/9/19
 * 开屏广告
 */
public class AdBean {
    @Expose
    public String downloadUrl;
    @Expose
    public long endDate;
    @Expose
    public String linkUrl;
    @Expose
    public int stayDuration;

    @Expose
    public String type;


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
                "downloadUrl='" + downloadUrl + '\'' +
                ", endDate=" + endDate +
                ", linkUrl='" + linkUrl + '\'' +
                ", stayDuration=" + stayDuration +
                ", type='" + type + '\'' +
                '}';
    }
}
