package com.gome.work.core.upload;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadResult {
    @Expose
    public String result;

    @Expose
    public String errorCode;

    @Expose
    public String msg;

    @Expose
    @SerializedName("gfsFileName")
    public String fileToken;

    @Expose
    public String url;

    public boolean isSuccess() {
        return "Y".equals(result);
    }


    public String getValidUrl() {
        if (url != null) {
            if (url.startsWith("http")) {
                return url;
            } else {
                return "http:" + url;
            }
        }
        return "";
    }
}
