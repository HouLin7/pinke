package com.gome.work.core.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 搜寻伴读信息
 */
public class SearchPartnerItem implements Serializable {


    public static class ResponseWrapper {

        @Expose
        @SerializedName("pn")
        public int pageIndex;

        @Expose
        @SerializedName("total")
        public int totalPage;

        @Expose
        @SerializedName("datas")
        public List<SearchPartnerItem> dataItems;

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
    @SerializedName("intro")
    public String note;

    @Expose
    public Position position;

    @Expose
    public String price;

    @Expose
    @SerializedName("userObj")
    public UserInfo userInfo;

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
