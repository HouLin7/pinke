package com.gome.work.core.model.converter;


import android.text.TextUtils;

import com.gome.utils.GsonUtil;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by liupeiquan on 2018/11/3
 * 应用截图
 */
public class AppDeatilScrConverter implements PropertyConverter<List<String>, String> {
    @Override
    public List<String> convertToEntityProperty(String databaseValue) {
        if (TextUtils.isEmpty(databaseValue)) {
            return new ArrayList<>();
        } else {
            TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {
            };
            return GsonUtil.jsonToList(typeToken.getType(), databaseValue);
        }
    }

    @Override
    public String convertToDatabaseValue(List<String> screenshotUrls) {
        if (screenshotUrls == null) {
            return null;
        } else {
            return GsonUtil.objectToJson(screenshotUrls);
        }
    }
}
