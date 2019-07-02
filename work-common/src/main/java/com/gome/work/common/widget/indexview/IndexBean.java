package com.gome.work.common.widget.indexview;

import com.gome.work.common.widget.indexview.indexbar.bean.BaseIndexPinyinBean;

/**
 * Create by liupeiquan on 2018/11/29
 */
public class IndexBean extends BaseIndexPinyinBean {
    private String name;
    @Override
    public String getTarget() {
        return name;
    }

    public IndexBean(String name) {
        this.name = name;
    }
}
