
package com.gome.work.core.model;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 *
 */
public class AccessTokenInfo {

    @Expose
    public UserInfo userInfo;

    @Expose
    @SerializedName("token")
    public String token;

    @Expose
    @SerializedName("im_token")
    public String imToken;

    @Expose
    @SerializedName("expire_date")
    public long expireDate;



    /**
     * 存取令牌是否有效
     *
     * @return
     */
    public boolean isValid() {
        return userInfo != null && !TextUtils.isEmpty(token);
    }

}
