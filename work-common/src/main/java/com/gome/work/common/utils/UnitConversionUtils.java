package com.gome.work.common.utils;

import android.content.Context;

import com.gome.work.core.SystemFramework;

/**
 * Created by liuletao on 2017/1/3.
 */
public class UnitConversionUtils {

    public static int dp2Px(float dp) {
        Context context = SystemFramework.getInstance().getGlobalContext();
        if (context == null) return -1;
//        float dp = context.getResources().getDimension(resId);
        return (int)(dp * density(context));
    }

    public static float density(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int parseInt(float dp) {
        return (int)dp;
    }
}
