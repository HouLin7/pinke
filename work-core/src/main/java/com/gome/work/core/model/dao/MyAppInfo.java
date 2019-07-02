package com.gome.work.core.model.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chaergongzi on 2018/8/14.
 */

@Entity(indexes = {@Index(value = "appId, dataUseId ", unique = true)})

/**
 * 我当前所有可用的app
 */
public class MyAppInfo implements Serializable {
    
    private static final long serialVersionUID = 961174612243895261L;
    @Id(autoincrement = true)
    private Long id;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 使用者id
     */
    private String dataUseId;

    @Generated(hash = 1991905616)
    public MyAppInfo(Long id, String appId, String dataUseId) {
        this.id = id;
        this.appId = appId;
        this.dataUseId = dataUseId;
    }

    @Generated(hash = 479941346)
    public MyAppInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDataUseId() {
        return this.dataUseId;
    }

    public void setDataUseId(String dataUseId) {
        this.dataUseId = dataUseId;
    }


}
