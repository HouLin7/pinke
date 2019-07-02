package com.gome.work.core.event.model;

import android.app.Activity;

import java.io.Serializable;

/**
 * 通用事件模型
 */

public class BaseParamInfo implements Serializable {

    private static final long serialVersionUID = -7080606661933089285L;

    public Activity activity;

    public int requestCode = -1;

    public Serializable extraData;

    public BaseParamInfo(Activity activity) {
        this.activity = activity;
    }
}
