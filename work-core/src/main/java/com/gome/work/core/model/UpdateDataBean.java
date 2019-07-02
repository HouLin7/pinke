package com.gome.work.core.model;

/**
 * Created by songzhiyang on 2016/7/19.
 * data: {
 isNeedUpdate: true,
 updateType: 1,
 title:"提示标题",
 content: "提示内容",
 downloadPath: "www.gomeplus.com/download/v1.2.3",
 updateVersion: "即将下载的apk版本编号 如1.0.0版本 为1.0.0",
 description: "具体升级细节描述"
 }
 */
public class UpdateDataBean {
    public boolean isNeedUpdate;
    public int updateType;
    public String title = "";
    public String content = "";
    public String downloadPath = "";
    public String updateVersion = "";
    public String description = "";

    public boolean isNeedUpdate() {
        return isNeedUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        isNeedUpdate = needUpdate;
    }

    public int getUpdateType() {
        return updateType;
    }

    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getUpdateVersion() {
        return updateVersion;
    }

    public void setUpdateVersion(String updateVersion) {
        this.updateVersion = updateVersion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "UpdateDataBean{" +
                "isNeedUpdate=" + isNeedUpdate +
                ", updateType=" + updateType +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", downloadPath='" + downloadPath + '\'' +
                ", updateVersion='" + updateVersion + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
