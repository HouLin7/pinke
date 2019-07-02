package com.gome.work.core.event.model;

import android.app.Activity;

/**
 * 通用事件模型
 */

public class PhotoChooseParamInfo extends BaseChooseParamInfo {

    private static final long serialVersionUID = -7544803677563586845L;
    /**
     * 0 包含视频和图片
     * 1 仅有图片
     * 2 仅有视频
     */
    public int typeScope = 0;


    public PhotoChooseParamInfo(Activity activity) {
        super(activity);
    }
}
