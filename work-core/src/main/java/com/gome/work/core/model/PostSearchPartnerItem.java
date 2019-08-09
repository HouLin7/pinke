package com.gome.work.core.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 发布找伴读信息
 */
public class PostSearchPartnerItem {
    /*
     * 科目
     */
    @Expose
    public String discipline;

    @Expose
    public String grade;

    @Expose
    public int gender;

    @Expose
    @SerializedName("school")
    public String school;

    @Expose
    public int score;

    @Expose
    public int type;

    @Expose
    public String note;

    /**
     * 意图
     */
    @Expose
    public int purpose;

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
        public long longitude;

        @Expose
        public long latitude;
    }

    public static class scheduleCard {

        @Expose
        public long startTime;

        @Expose
        public long endTime;

        @Expose
        public int type;

    }

}
