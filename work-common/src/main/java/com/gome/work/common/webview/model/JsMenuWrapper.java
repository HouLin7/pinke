package com.gome.work.common.webview.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsMenuWrapper {

    @Expose
    public String onMenuClick;

    @Expose
    @SerializedName("menus")
    public List<JsMenuInfo> menuList;

}
