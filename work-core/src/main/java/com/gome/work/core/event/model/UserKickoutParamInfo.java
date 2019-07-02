
package com.gome.work.core.event.model;

import android.app.Activity;

import java.io.Serializable;

/**
 * 强制用户退出
 */
public class UserKickoutParamInfo implements Serializable {

    /**
     * 退出的原因类型
     */
    public String type;

}
