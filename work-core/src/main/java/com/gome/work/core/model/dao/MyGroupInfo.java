package com.gome.work.core.model.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.io.Serializable;

/**
 * Created by chaergongzi on 2018/8/14.
 */

@Entity(indexes = {@Index(value = "groupId, dataUseId ", unique = true)})

public class MyGroupInfo implements Serializable {

    private static final long serialVersionUID = 532612269323561077L;

    @Id(autoincrement = true)
    private Long id;

    private String groupId;

    private String dataUseId;

    @Generated(hash = 2070914225)
    public MyGroupInfo(Long id, String groupId, String dataUseId) {
        this.id = id;
        this.groupId = groupId;
        this.dataUseId = dataUseId;
    }

    @Generated(hash = 1039593014)
    public MyGroupInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDataUseId() {
        return this.dataUseId;
    }

    public void setDataUseId(String dataUseId) {
        this.dataUseId = dataUseId;
    }


}
