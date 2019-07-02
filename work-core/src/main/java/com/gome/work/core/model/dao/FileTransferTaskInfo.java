package com.gome.work.core.model.dao;

import com.gome.core.greendao.DaoSession;
import com.gome.core.greendao.FileItemInfoDao;
import com.gome.core.greendao.FileTransferTaskInfoDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;

/**
 * Created by chaergongzi on 2018/8/14.
 */

/**
 * 文件下载表
 */
@Entity
public class FileTransferTaskInfo implements Serializable {

    private static final long serialVersionUID = 8132577083740318044L;

    /**
     * 初始状态
     */
    public static final String STATE_IDLE = "idle";

    /**
     * 文件下载失败
     */
    public static final String STATE_FAIL = "fail";

    /**
     * 文件下载成功
     */
    public static final String STATE_OK = "success";

    /**
     * 文件下载中
     */
    public static final String STATE_DOING = "doing";

    /**
     * 用户取消
     */
    public static final String STATE_CANCEL = "cancel";


    @Id(autoincrement = true)
    private Long id;

    /**
     * 保存的文件ID
     */
    private long fileId = 0;

    /**
     * 文件下载状态
     */
    @NotNull
    private String state = STATE_IDLE;

    /**
     * 任务创建时间
     */
    @NotNull
    private long createDate;

    /**
     * 文件总容量
     */
    private long contentLength;

    @NotNull
    private String downloadUrl;

    @NotNull
    private String fileName;

    /**
     * 保留要上传或下载的文件本地路径
     */
    private String filePath;

    /**
     * 0：下载，1：上传
     */
    private int direct = 0;

    /**
     * 已经传输的字节数
     */
    private long transferLength;

    @ToOne(joinProperty = "fileId")
    private FileItemInfo fileItemInfo;

    /**
     * 文件来源标识
     */
    private String fromSourceCode;

    /**
     * 文件来源名称
     */
    private String fromSourceName;


    /**
     * MIME 类型
     */
    private String mimeType;


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getFileId() {
        return this.fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getDownloadUrl() {
        return this.downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    @Generated(hash = 1560049759)
    private transient Long fileItemInfo__resolvedKey;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 561962328)
    private transient FileTransferTaskInfoDao myDao;


    @Generated(hash = 1845522761)
    public FileTransferTaskInfo(Long id, long fileId, @NotNull String state, long createDate,
            long contentLength, @NotNull String downloadUrl, @NotNull String fileName, String filePath,
            int direct, long transferLength, String fromSourceCode, String fromSourceName,
            String mimeType) {
        this.id = id;
        this.fileId = fileId;
        this.state = state;
        this.createDate = createDate;
        this.contentLength = contentLength;
        this.downloadUrl = downloadUrl;
        this.fileName = fileName;
        this.filePath = filePath;
        this.direct = direct;
        this.transferLength = transferLength;
        this.fromSourceCode = fromSourceCode;
        this.fromSourceName = fromSourceName;
        this.mimeType = mimeType;
    }

    @Generated(hash = 623660426)
    public FileTransferTaskInfo() {
    }


    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1451855236)
    public FileItemInfo getFileItemInfo() {
        long __key = this.fileId;
        if (fileItemInfo__resolvedKey == null || !fileItemInfo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FileItemInfoDao targetDao = daoSession.getFileItemInfoDao();
            FileItemInfo fileItemInfoNew = targetDao.load(__key);
            synchronized (this) {
                fileItemInfo = fileItemInfoNew;
                fileItemInfo__resolvedKey = __key;
            }
        }
        return fileItemInfo;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1796672862)
    public void setFileItemInfo(@NotNull FileItemInfo fileItemInfo) {
        if (fileItemInfo == null) {
            throw new DaoException(
                    "To-one property 'fileId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.fileItemInfo = fileItemInfo;
            fileId = fileItemInfo.getId();
            fileItemInfo__resolvedKey = fileId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 974081358)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFileTransferTaskInfoDao() : null;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getDirect() {
        return this.direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
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

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public long getContentLength() {
        return this.contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public long getTransferLength() {
        return this.transferLength;
    }

    public void setTransferLength(long transferLength) {
        this.transferLength = transferLength;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }


}
