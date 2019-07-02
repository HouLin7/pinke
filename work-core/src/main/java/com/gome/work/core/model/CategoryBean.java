package com.gome.work.core.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Create by liupeiquan on 2018/9/4
 */
public class CategoryBean implements Serializable{
    @Expose
    public String code;

    @Expose
    public String name;

    public int position;//用于应用管理tab滑动
}
