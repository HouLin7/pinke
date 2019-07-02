package com.gome.work.common.webview.model;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;

public class JsShareInfo {

    @Expose
    public String linkUrl;

    @Expose
    public String imageUrl;

    @Expose
    public String title;

    @Expose
    public String content;

    public boolean isValid() {
        return !TextUtils.isEmpty(title) || !TextUtils.isEmpty(content);
    }


}
