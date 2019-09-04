
package com.gome.work.core.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 基础数据字典
 */
public class SysCfgData implements Serializable {

    @Expose
    @SerializedName("price")
    public List<CfgDicItem> price;

    @Expose
    @SerializedName("subject")
    public List<CfgDicItem> course;

    @Expose
    @SerializedName("grade")
    public List<CfgDicItem> grade;

    /**
     * 学生分数
     */
    @Expose
    @SerializedName("student_score")
    public List<CfgDicItem> score;

    /**
     * 教龄
     */
    @Expose
    @SerializedName("teach_age")
    public List<CfgDicItem> teachAge;

    /**
     * 班级类型
     */
    @Expose
    @SerializedName("teach_type")
    public List<CfgDicItem> classType;

    /**
     * 教龄
     */
    @Expose
    @SerializedName("teacher_auth_type")
    public List<CfgDicItem> teacherAuthType;


    public static ArrayList<CfgDicItem> getSexCfgItems() {
        ArrayList<CfgDicItem> result = new ArrayList<>();
        result.add(new CfgDicItem("男", "1"));
        result.add(new CfgDicItem("女", "2"));
        return result;
    }


}
