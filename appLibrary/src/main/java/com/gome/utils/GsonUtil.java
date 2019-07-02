package com.gome.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.List;

public class GsonUtil {

    private final static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static String objectToJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T jsonToObject(Class<T> cls, String json) {
        return gson.fromJson(json, cls);
    }

    public static <T> List<T> jsonToList(Type type, String json) {
        return gson.fromJson(json, type);
    }

    public static Gson getGson() {
        return gson;
    }

}
