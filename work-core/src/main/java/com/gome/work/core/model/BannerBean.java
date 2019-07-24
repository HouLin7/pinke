package com.gome.work.core.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class BannerBean {
    @Expose
    public String image;
    @Expose
    public String linkUrl;
    @Expose
    @SerializedName("name")
    public String title;

}
