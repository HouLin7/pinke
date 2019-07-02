package com.gome.work.core.model.schedule;

import java.io.Serializable;

/**
 * Create by liupeiquan on 2018/12/10
 */
public class ScheduleInfo implements Serializable {

    private static final long serialVersionUID = 1689068972357050365L;

    /**
     * createTime : 1544695090000
     * creator : 521534
     * executionDate : 2018-12-13
     * executionDateStr : 2018-12-13 17:57:54
     * executionTime : 17:57:54
     * hadRemidTimes : 0
     * id : 419
     * lastModifier : 521534
     * modifyTime : 1544695090000
     * objId : 0
     * objType : 0
     * remindValue : 10
     * remindValueStr : 10分钟前
     * scheduleTime : 1544695074000
     * scheduleTimeStr : 2018-12-13 17:57:54
     * title : 兔子
     * type : 3
     * userId : 521534
     * validType : 2
     * validValue : 2018-12-23 17:58:03
     * week : 星期四
     */

    private long createTime;
    private int creator;
    private String executionDate;
    private String executionDateStr;
    private String executionTime;
    private int hadRemidTimes;
    private int id;
    private int lastModifier;
    private long modifyTime;
    private int objId;
    private int objType;
    private int remindValue;
    private String remindValueStr;
    private long scheduleTime;
    private String scheduleTimeStr;
    private String title;
    private int type;
    private int userId;
    private int validType;
    private String validValue;
    private String week;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public String getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(String executionDate) {
        this.executionDate = executionDate;
    }

    public String getExecutionDateStr() {
        return executionDateStr;
    }

    public void setExecutionDateStr(String executionDateStr) {
        this.executionDateStr = executionDateStr;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public int getHadRemidTimes() {
        return hadRemidTimes;
    }

    public void setHadRemidTimes(int hadRemidTimes) {
        this.hadRemidTimes = hadRemidTimes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLastModifier() {
        return lastModifier;
    }

    public void setLastModifier(int lastModifier) {
        this.lastModifier = lastModifier;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getObjId() {
        return objId;
    }

    public void setObjId(int objId) {
        this.objId = objId;
    }

    public int getObjType() {
        return objType;
    }

    public void setObjType(int objType) {
        this.objType = objType;
    }

    public int getRemindValue() {
        return remindValue;
    }

    public void setRemindValue(int remindValue) {
        this.remindValue = remindValue;
    }

    public String getRemindValueStr() {
        return remindValueStr;
    }

    public void setRemindValueStr(String remindValueStr) {
        this.remindValueStr = remindValueStr;
    }

    public long getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(long scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getScheduleTimeStr() {
        return scheduleTimeStr;
    }

    public void setScheduleTimeStr(String scheduleTimeStr) {
        this.scheduleTimeStr = scheduleTimeStr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getValidType() {
        return validType;
    }

    public void setValidType(int validType) {
        this.validType = validType;
    }

    public String getValidValue() {
        return validValue;
    }

    public void setValidValue(String validValue) {
        this.validValue = validValue;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }
}
