package com.gome.work.common.utils;

import android.content.Context;

import java.lang.ref.Reference;

/**
 * Created by liubomin on 2017/3/27.
 */

public class CustomActivityManager {

    public Reference<Context> currentActivity;

    private static CustomActivityManager customActivityManager;

    public static CustomActivityManager getInstance() {

        if (null == customActivityManager) {

            synchronized (CustomActivityManager.class) {
                if (null == customActivityManager) {
                    customActivityManager = new CustomActivityManager();
                }
            }
        }

        return customActivityManager;
    }


}
