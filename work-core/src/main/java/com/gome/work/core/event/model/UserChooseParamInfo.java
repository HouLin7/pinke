package com.gome.work.core.event.model;

import android.app.Activity;

import com.gome.work.core.model.UserInfo;

import java.util.List;

/**
 * 通用事件模型
 */

public class UserChooseParamInfo extends BaseChooseParamInfo {

    public List<UserInfo> filterList;

    public UserChooseParamInfo(Activity activity) {
        super(activity);
    }
}
