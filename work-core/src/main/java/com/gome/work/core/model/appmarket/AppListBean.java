package com.gome.work.core.model.appmarket;

import com.gome.work.core.model.CategoryBean;

import java.io.Serializable;
import java.util.List;

public class AppListBean implements Serializable{

    public CategoryBean category;
    public List<AppItemBean> items;
}
