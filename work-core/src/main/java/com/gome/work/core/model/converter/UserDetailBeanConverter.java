package com.gome.work.core.model.converter;

import com.gome.work.core.model.UserInfo;
import com.gome.work.core.utils.GsonUtil;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Create by liupeiquan on 2018/11/3
 */
public class UserDetailBeanConverter implements PropertyConverter<UserInfo, String> {
    @Override
    public UserInfo convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        } else {
            return GsonUtil.jsonToBean(databaseValue, UserInfo.class);
        }
    }

    @Override
    public String convertToDatabaseValue(UserInfo entityProperty) {
        if (entityProperty == null) {
            return null;
        } else {
            return GsonUtil.GsonString(entityProperty);
        }
    }
}
