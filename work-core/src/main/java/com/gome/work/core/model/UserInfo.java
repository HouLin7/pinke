package com.gome.work.core.model;

import com.gome.work.core.model.converter.UserVerifyPropertyConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * 用户信息详情
 */
@Entity
public class UserInfo implements Serializable, ISelectableItem {
    private static final long serialVersionUID = 510041171037518010L;

    @Expose
    @Id
    @SerializedName("id")
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

    @Expose
    private String grade;

    @Expose
    private String school;

    @Expose
    private String identity;

    @Expose
    private String partnerRelation;

    @Expose
    private String followRelation;

    @Expose
    private String teachAge;

    @Expose
    private String teachCourse;

    @Expose
    private String doorVisit;
    
    @Expose
    private String distance;
    
    @Convert(columnType = String.class, converter = UserVerifyPropertyConverter.class)
    private UserVerifyPropertyItem verifyProperty;

    @Generated(hash = 1142766567)
    public UserInfo(String id, String imId, String avatar, String email, String username,
            String nickname, String sex, String address, String phone, String firstLetter,
            String grade, String school, String identity, String partnerRelation,
            String followRelation, String teachAge, String teachCourse, String doorVisit,
            String distance, UserVerifyPropertyItem verifyProperty) {
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
        this.grade = grade;
        this.school = school;
        this.identity = identity;
        this.partnerRelation = partnerRelation;
        this.followRelation = followRelation;
        this.teachAge = teachAge;
        this.teachCourse = teachCourse;
        this.doorVisit = doorVisit;
        this.distance = distance;
        this.verifyProperty = verifyProperty;
    }

    @Generated(hash = 1279772520)
    public UserInfo() {
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

    public String getGrade() {
        return this.grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSchool() {
        return this.school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getIdentity() {
        return this.identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }


    public String getSexName() {
        if ("1".equals(sex)) {
            return "男";
        } else if ("2".equals(sex)) {
            return "女";
        } else {
            return "未知";
        }
    }

    public String getPartnerRelation() {
        return this.partnerRelation;
    }

    public void setPartnerRelation(String partnerRelation) {
        this.partnerRelation = partnerRelation;
    }

    public String getFollowRelation() {
        return this.followRelation;
    }

    public void setFollowRelation(String followRelation) {
        this.followRelation = followRelation;
    }

    public UserVerifyPropertyItem getVerifyProperty() {
        return this.verifyProperty;
    }

    public void setVerifyProperty(UserVerifyPropertyItem verifyProperty) {
        this.verifyProperty = verifyProperty;
    }

    public String getTeachAge() {
        return this.teachAge;
    }

    public void setTeachAge(String teachAge) {
        this.teachAge = teachAge;
    }

    public String getDoorVisit() {
        return this.doorVisit;
    }

    public void setDoorVisit(String doorVisit) {
        this.doorVisit = doorVisit;
    }

    public String getTeachCourse() {
        return this.teachCourse;
    }

    public void setTeachCourse(String teachCourse) {
        this.teachCourse = teachCourse;
    }

    public String getDistance() {
        return this.distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

}
