package com.gome.work.core.model.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class TagDataInfo  {

    @Id(autoincrement = true)
    private Long id;

    private String data;

    private long timestamp;

    @Generated(hash = 2046439511)
    public TagDataInfo(Long id, String data, long timestamp) {
        this.id = id;
        this.data = data;
        this.timestamp = timestamp;
    }

    @Generated(hash = 544971500)
    public TagDataInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
