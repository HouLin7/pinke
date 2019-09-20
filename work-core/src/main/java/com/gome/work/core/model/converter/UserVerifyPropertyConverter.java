package com.gome.work.core.model.converter;


import android.text.TextUtils;

import com.gome.utils.GsonUtil;
import com.gome.work.core.model.UserVerifyPropertyItem;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by liupeiquan on 2018/11/3
 * 应用截图
 */
public class UserVerifyPropertyConverter implements PropertyConverter<UserVerifyPropertyItem, String> {
    @Override
    public UserVerifyPropertyItem convertToEntityProperty(String databaseValue) {
        if (TextUtils.isEmpty(databaseValue)) {
            return new UserVerifyPropertyItem();
        } else {

            return GsonUtil.jsonToObject(UserVerifyPropertyItem.class, databaseValue);
        }
    }

    @Override
    public String convertToDatabaseValue(UserVerifyPropertyItem propertyItem) {
        if (propertyItem == null) {
            return "";
        } else {
            return GsonUtil.objectToJson(propertyItem);
        }
    }
}
