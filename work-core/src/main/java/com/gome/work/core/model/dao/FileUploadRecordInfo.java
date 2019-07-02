package com.gome.work.core.model.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.Serializable;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by chaergongzi on 2018/8/14.
 */

@Entity
public class FileUploadRecordInfo implements Serializable {

    private static final long serialVersionUID = 8132577083740318044L;

    @Id(autoincrement = true)
    private Long id;

    /**
     * 保留字段
     */
    private String state;

    /**
     * 文件更新时间
     */
    @NotNull
    private long updateTime;

    /**
     * 文件绝对路径
     */
    @Unique
    private String filePath;

    @Generated(hash = 171612996)
    public FileUploadRecordInfo(Long id, String state, long updateTime,
            String filePath) {
        this.id = id;
        this.state = state;
        this.updateTime = updateTime;
        this.filePath = filePath;
    }

    @Generated(hash = 1012420990)
    public FileUploadRecordInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


}
