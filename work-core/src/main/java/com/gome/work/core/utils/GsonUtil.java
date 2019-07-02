package com.gome.work.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * Create by liupeiquan on 2018/9/27
 */
public class GsonUtil {

    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }


    private GsonUtil() {
    }


    /**
     * 将object对象转成json字符串
     *
     * @param object
     * @return
     */
    public static String GsonString(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }


    /**
     * 将gsonString转成泛型bean
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T GsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

    /**
     * 将gsonString转成泛型bean,null则创建新对象返回
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T GsonToNewBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        if (t==null){
            try {
                t = cls.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return t;
    }


    /**
     * 转成list
     * 解决泛型问题
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }


    /**
     * 转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> GsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }


    /**
     * 转成map的
     *
     * @param gsonString
     * @return
     */
    public static <K, V> Map<K, V> GsonToMaps(String gsonString) {
        Map<K, V> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<K, V>>() {
            }.getType());
        }
        return map;
    }

}
