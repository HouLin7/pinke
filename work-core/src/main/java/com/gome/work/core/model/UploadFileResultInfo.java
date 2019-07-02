package com.gome.work.core.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Create by liupeiquan on 2018/8/21
 */
public class UploadFileResultInfo {

    @Expose
    public long size;

    @Expose
    @SerializedName("gfsFileName")
    public String fileName;

    @Expose
    public String url;

}
