
package com.gome.work.core.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 基础数据字典
 */
public class CfgDicItem implements Serializable {

    private static final long serialVersionUID = -6507871928142789829L;

//    @Expose
//    public String id;

    @Expose
    public String name;

    @Expose
    public String code;


    @Expose
    @SerializedName("childs")
    public List<CfgDicItem> children;

    public boolean isSelected;

    public CfgDicItem(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public boolean isHasChild() {
        return children != null && !children.isEmpty();
    }

}
