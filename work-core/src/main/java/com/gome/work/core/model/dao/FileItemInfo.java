package com.gome.work.core.model.dao;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.Serializable;

/**
 * Created by chaergongzi on 2018/8/14.
 */

@Entity
public class FileItemInfo implements Serializable {

    private static final long serialVersionUID = 532612269323561077L;

    /**
     * 默认文件类型
     */
    public static final String TYPE_DEFAULT = "default";

    public static final String TYPE_PPT = "ppt";

    public static final String TYPE_PDF = "pdf";

    public static final String TYPE_EXCEL = "xls";

    public static final String TYPE_TXT = "txt";

    public static final String TYPE_IMAGE = "img";

    public static final String TYPE_VIDEO = "video";

    public static final String TYPE_DOC = "doc";

    public static final String TYPE_MP3 = "mp3";

    public static final String TYPE_RAR = "rar";

    @Id(autoincrement = true)
    private Long id;

    /**
     * 文件名
     */
    @NotNull
    private String name;

    /**
     * 可识别的自有文件类型
     */
    @NotNull
    private String type;


    /**
     * 文件创建时间
     */
    @NotNull
    private long createDate;


    /**
     * 文件修改时间，一般记录最近一次使用时间
     */
    @NotNull
    private long updateDate;

    /**
     * 文件来源标识
     */
    @NotNull
    private String fromSourceCode;

    /**
     * 文件来源描述
     */
    private String fromSourceName;

    /**
     * 文件大小
     */
    private long contentLength;

    /**
     * 文件路径
     */
    private String path;


    @Generated(hash = 764941776)
    public FileItemInfo(Long id, @NotNull String name, @NotNull String type,
            long createDate, long updateDate, @NotNull String fromSourceCode,
            String fromSourceName, long contentLength, String path) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.fromSourceCode = fromSourceCode;
        this.fromSourceName = fromSourceName;
        this.contentLength = contentLength;
        this.path = path;
    }


    @Generated(hash = 1195954210)
    public FileItemInfo() {
    }


    public static String getRecognizeFileType(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return FileItemInfo.TYPE_DEFAULT;
        }
        int index = fileName.indexOf(".");
        if (index < 0) {
            return FileItemInfo.TYPE_DEFAULT;
        }
        String suffix = fileName.substring(index, fileName.length());
        suffix = suffix.toLowerCase();
        if (suffix.contains("xls")) {
            return FileItemInfo.TYPE_EXCEL;
        } else if (suffix.contains("doc")) {
            return FileItemInfo.TYPE_DOC;
        } else if (suffix.contains("ppt")) {
            return FileItemInfo.TYPE_PPT;
        } else if (suffix.contains("pdf")) {
            return FileItemInfo.TYPE_PDF;
        } else if (suffix.contains("jpg") || suffix.contains("png")) {
            return FileItemInfo.TYPE_IMAGE;
        } else if (suffix.contains("mp4") || suffix.contains("mov")) {
            return FileItemInfo.TYPE_VIDEO;
        } else if (suffix.contains("mp3") || suffix.contains("aac")) {
            return FileItemInfo.TYPE_MP3;
        } else if (suffix.contains("txt")) {
            return FileItemInfo.TYPE_TXT;
        }else if (suffix.contains("zip")|| suffix.contains("rar")) {
            return FileItemInfo.TYPE_RAR;
        }

        return FileItemInfo.TYPE_DEFAULT;
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getType() {
        return this.type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public long getCreateDate() {
        return this.createDate;
    }


    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }


    public long getUpdateDate() {
        return this.updateDate;
    }


    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }


    public String getFromSourceCode() {
        return this.fromSourceCode;
    }


    public void setFromSourceCode(String fromSourceCode) {
        this.fromSourceCode = fromSourceCode;
    }


    public String getFromSourceName() {
        return this.fromSourceName;
    }


    public void setFromSourceName(String fromSourceName) {
        this.fromSourceName = fromSourceName;
    }


    public long getContentLength() {
        return this.contentLength;
    }


    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }


    public String getPath() {
        return this.path;
    }


    public void setPath(String path) {
        this.path = path;
    }
}
