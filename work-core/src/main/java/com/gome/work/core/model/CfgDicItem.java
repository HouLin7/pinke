
package com.gome.work.core.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 基础数据字典结构
 */
public class CfgDicItem implements Serializable {

    /**
     * 文化课
     */
    public static final int ID_CULTURE_COURSE = 1;

    /**
     * 素质教育
     */
    public static final int TYPE_QUALITY_EDUCATION = 2;

    /**
     * P
     * 素质教育
     */
    public static final int TYPE_PROFESSIONAL_EDUCATION = 3;


    private static final long serialVersionUID = -6507871928142789829L;

    @Expose
    public String id;

    @Expose
    public String name;

    @Expose
    @SerializedName("childs")
    public List<CfgDicItem> children;

    public boolean isSelected;

    public boolean isHasChild() {
        return children != null && !children.isEmpty();
    }

}
