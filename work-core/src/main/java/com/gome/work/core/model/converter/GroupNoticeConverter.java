package com.gome.work.core.model.converter;

import com.gome.work.core.model.im.GroupNoticeBean;
import com.gome.work.core.utils.GsonUtil;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Create by liupeiquan on 2018/11/3
 */
public class GroupNoticeConverter implements PropertyConverter<GroupNoticeBean, String> {
    @Override
    public GroupNoticeBean convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        } else {
            return GsonUtil.GsonToBean(databaseValue, GroupNoticeBean.class);
        }
    }

    @Override
    public String convertToDatabaseValue(GroupNoticeBean entityProperty) {
        if (entityProperty == null) {
            return null;
        } else {
            return GsonUtil.GsonString(entityProperty);
        }
    }
}
