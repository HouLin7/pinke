package com.gome.work.core.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 用户信息详情
 */
@Entity
public class UserInfo implements Serializable, ISelectableItem {
    private static final long serialVersionUID = 510041171037518010L;

    @Expose
    @Unique
    @Id
    @SerializedName("uid")
    private String id;

    @Expose
    @SerializedName("im_id")
    private String imId;

    @Expose
    private String avatar;

    @Expose
    private String email;

    @Expose
    private String username;

    @Expose
    private String nickname;

    @Expose
    @SerializedName("gender")
    private String sex;

    @Expose
    private String address;

    @Expose
    private String phone;

    private String firstLetter;//示数据拼音的首字母

    @Transient
    private boolean isSelect;



    @Generated(hash = 1529530149)
    public UserInfo(String id, String imId, String avatar, String email,
            String username, String nickname, String sex, String address,
            String phone, String firstLetter) {
        this.id = id;
        this.imId = imId;
        this.avatar = avatar;
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.sex = sex;
        this.address = address;
        this.phone = phone;
        this.firstLetter = firstLetter;
    }

    @Generated(hash = 1279772520)
    public UserInfo() {
    }
    
    

    @Override
    public String toString() {
        return "UserInfo{" +
                "id='" + id + '\'' +
                ", imId='" + imId + '\'' +
                ", avatar='" + avatar + '\'' +
                ", email='" + email + '\'' +
                ", name='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", firstLetter='" + firstLetter + '\'' +
                '}';
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImId() {
        return this.imId;
    }

    public void setImId(String imId) {
        this.imId = imId;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstLetter() {
        return this.firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }


    @Override
    public String getItemId() {
        return null;
    }

    @Override
    public String getItemAvatar() {
        return null;
    }

    @Override
    public String getItemName() {
        return null;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


}
