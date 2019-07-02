package com.gome.work.common.webview.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsDownloadInfo {

    @Expose
    public String url;

    @Expose
    public long contentLength;

    @Expose
    public String mimeType;


    @Expose
    public String fileName;


}
