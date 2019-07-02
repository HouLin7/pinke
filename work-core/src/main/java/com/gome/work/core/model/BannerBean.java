package com.gome.work.core.model;

import com.google.gson.annotations.Expose;

/**
 * Create by liupeiquan on 2018/9/13
 */
public class BannerBean {
    @Expose
    public String small_image;
    @Expose
    public String large_image;
    @Expose
    public String linkUrl;
    @Expose
    public String title;

    @Override
    public String toString() {
        return "BannerBean{" +
                "small_image='" + small_image + '\'' +
                ", large_image='" + large_image + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                '}';
    }
}
