package com.gome.work.core.model;

import com.google.gson.annotations.Expose;

/**
 * Create by liupeiquan on 2018/8/28
 */
public class BackLogItem {
    @Expose
    public String sourceId;

    @Expose
    public String sourceName;

    @Expose
    public int total;

    @Expose
    public String type;

    @Expose
    public int drawableId;

    @Expose
    public String name;

    @Expose
    public String url;

    @Override
    public String toString() {
        return "BackLogItem{" +
                "sourceId='" + sourceId + '\'' +
                ", sourceName='" + sourceName + '\'' +
                ", total=" + total +
                ", type='" + type + '\'' +
                ", drawableId=" + drawableId +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
