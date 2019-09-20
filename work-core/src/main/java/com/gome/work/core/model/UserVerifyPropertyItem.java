package com.gome.work.core.model;


import android.text.TextUtils;

import com.gome.utils.GsonUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户认证标记
 */
public class UserVerifyPropertyItem {

    /**
     * 证书认证
     */
    @Expose
    public String certificate;
    /**
     * 学历认证
     */
    @Expose
    public String education;
    /**
     * 实名身份份证
     */
    @Expose
    public String idcard;
}
