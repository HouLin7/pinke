package com.gome.work.core.model.converter;


import android.text.TextUtils;

import com.gome.utils.GsonUtil;
import com.gome.work.core.model.UserInfo;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by liupeiquan on 2018/11/3
 */
public class GroupInfoUserListConverter implements PropertyConverter<List<UserInfo>, String> {
    @Override
    public List<UserInfo> convertToEntityProperty(String databaseValue) {
        if (TextUtils.isEmpty(databaseValue)) {
            return new ArrayList<>();
        } else {
            TypeToken<List<UserInfo>> typeToken = new TypeToken<List<UserInfo>>() {
            };
            return GsonUtil.jsonToList(typeToken.getType(), databaseValue);
        }
    }

    @Override
    public String convertToDatabaseValue(List<UserInfo> entityProperty) {
        if (entityProperty == null) {
            return null;
        } else {
            return GsonUtil.objectToJson(entityProperty);
        }
    }
}
