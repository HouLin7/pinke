package com.gome.work.core.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 发布找伴读信息
 */
public class PostSearchPartnerItem {

    public enum PurposeType {
        SEEK_PARTNER, SEEK_TEACHER, SEEK_ALL
    }

    /*
     * 科目
     */
    @Expose
    public String discipline;

    @Expose
    public String grade;

    @Expose
    @SerializedName("school")
    public String school;
    
    @Expose
    public String score;

    @Expose
    public String classType;

    @Expose
    @SerializedName("gender")
    public String sex;

    @Expose
    public long openDate;

    @Expose
    public String note;

    /**
     * 意图
     */
    @Expose
    public String purpose;

    @Expose
    public Position position;

    @Expose
    public List<scheduleCard> scheduleCards;

    public static class Position {

        @Expose
        public String district;

        @Expose
        public String city;

        @Expose
        public String province;

        @Expose
        public String address;

        @Expose
        public double longitude;

        @Expose
        public double latitude;

        public void assign(AddressItem item) {
            address = item.address;
            district = item.county.name;
            city = item.city.name;
            province = item.province.name;

            longitude = item.longitude;
            latitude = item.latitude;
        }

    }

    public static class scheduleCard {

        @Expose
        public long startTime;

        @Expose
        public long endTime;

        /**
         * 0日期，1周循环2日循环
         */
        @Expose
        public int type;

    }

}
