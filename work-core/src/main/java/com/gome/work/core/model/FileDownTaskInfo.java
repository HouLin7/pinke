
package com.gome.work.core.model;

import java.io.Serializable;

/**
 * 文件下载描述信息
 */
public class FileDownTaskInfo implements Serializable {

    /**
     * 来源名称
     */
    public String fromSource;

    /**
     * 来源标识
     */
    public String fromSourceType;

    /**
     *
     */
    public String url;

    /**
     * 文件名
     */
    public String fileName;

    /**
     * 文件长度
     */
    public long contentLength;

    public String mimeType;

}
