package com.gome.work.common.webview.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JsImageWrapper implements Serializable {

    @Expose
    public int index;

    @Expose
    @SerializedName("images")
    public ArrayList<ImageItem> images;

    public static class ImageItem {

        @Expose
        public String url;


    }

}
