package com.gome.work.common.utils;

import java.lang.reflect.Method;

/**
 * Created by liuletao on 2017/1/5.
 */
public class PropertyUtils {
    private static volatile Method set = null;
    private static volatile Method get = null;

    public static void set(String prop, String value) {

        try {
            if (null == set) {
                synchronized (PropertyUtils.class) {
                    if (null == set) {
                        Class<?> cls = Class.forName("android.os.SystemProperties");
                        set = cls.getDeclaredMethod("set", String.class, String.class);
                    }
                }
            }
            set.invoke(null, prop, value);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public static String get(String prop, String defaultvalue) {
        String value = defaultvalue;
        try {
            if (null == get) {
                synchronized (PropertyUtils.class) {
                    if (null == get) {
                        Class<?> cls = Class.forName("android.os.SystemProperties");
                        get = cls.getDeclaredMethod("get", String.class, String.class);
                    }
                }
            }
            value = (String) (get.invoke(null, prop, defaultvalue));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return value;
    }
}
