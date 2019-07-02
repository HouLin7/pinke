package com.gome.work.core.event.model;

import android.app.Activity;

/**
 * 通用事件模型
 */

public class BaseChooseParamInfo extends BaseParamInfo {

    /**
     * 最大选择数量
     */
    public int maxCount = 0;

    /**
     * {@link com.gome.work.core.Constants.MODEL_PICK_MULTI} {@link com.gome.work.core.Constants.MODEL_PICK_SINGLE}
     */
    public String chooseModel;

    public BaseChooseParamInfo(Activity activity) {
        super(activity);
    }
}
