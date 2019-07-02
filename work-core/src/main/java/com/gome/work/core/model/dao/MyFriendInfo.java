package com.gome.work.core.model.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.io.Serializable;

/**
 * Created by chaergongzi on 2018/8/14.
 */

@Entity(indexes = {@Index(value = "userId, dataUseId ", unique = true)})

public class MyFriendInfo implements Serializable {

    private static final long serialVersionUID = -5075840996376191949L;
    @Id(autoincrement = true)
    private Long id;

    private String userId;

    private String dataUseId;

    @Generated(hash = 1083791406)
    public MyFriendInfo(Long id, String userId, String dataUseId) {
        this.id = id;
        this.userId = userId;
        this.dataUseId = dataUseId;
    }

    @Generated(hash = 18687857)
    public MyFriendInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDataUseId() {
        return this.dataUseId;
    }

    public void setDataUseId(String dataUseId) {
        this.dataUseId = dataUseId;
    }


}
