
package com.gome.work.core.model;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by chenhang01 on 2016/6/6.
 * Modify by Marlon on 2018/5/22
 * 修改内容：根据新OA登录接口加 @SerializedName()适配新数据接口
 */
public class AccessTokenBean {

    @Expose
    @SerializedName("user_info")
    public UserInfo userInfo;

    @Expose
    @SerializedName("access_token")
    public String token;

    @Expose
    @SerializedName("im_token")
    public String imToken;

    @Expose
    @SerializedName("expire_date")
    public long expireDate;

    @Expose
    public boolean isImLogin;

    /**
     * 存取令牌是否有效
     *
     * @return
     */
    public boolean isValid() {
        return userInfo != null && !TextUtils.isEmpty(token) && !TextUtils.isEmpty(userInfo.getId());
    }

}
