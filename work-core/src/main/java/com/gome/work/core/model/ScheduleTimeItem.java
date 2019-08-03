
package com.gome.work.core.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 日程时间
 */
public class ScheduleTimeItem implements Serializable {

    @Expose
    public long startTime;

    @Expose
    public long endTime;

    @Expose
    public String note;


}
