
package com.gome.work.core.model;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * 日程时间
 */
@Entity
public class ScheduleTimeItem implements Serializable {

    private static final long serialVersionUID = 510041171037518010L;

    @Id
    @Expose
    public int id;

    @Expose
    public long startTime;

    @Expose
    public long endTime;

    @Expose
    public String title;

    @Expose
    public String colorValue;



    @Generated(hash = 1023389777)
    public ScheduleTimeItem(int id, long startTime, long endTime, String title,
            String colorValue) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.colorValue = colorValue;
    }

    @Generated(hash = 422902005)
    public ScheduleTimeItem() {
    }


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }


    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColorValue() {
        return this.colorValue;
    }

    public void setColorValue(String colorValue) {
        this.colorValue = colorValue;
    }

}
