package com.gome.work.core.persistence;

import android.content.Context;

import com.gome.core.greendao.*;
import com.gome.work.core.model.dao.FileTransferTaskInfo;

import java.util.concurrent.Callable;

/**
 * Created by chaergongzi on 2018/8/15.
 */

public class DaoUtil {

    private DaoManager mManager;

    public DaoUtil(Context context) {
        mManager = DaoManager.getInstance(context);
    }

    public void update(FileTransferTaskInfo dao) {
        mManager.getDaoSession().getFileTransferTaskInfoDao().update(dao);
    }

    public FileItemInfoDao getFileItemInfoDao() {
        return mManager.getDaoSession().getFileItemInfoDao();
    }

    public FileTransferTaskInfoDao getFileTransferTaskInfoDao() {
        return mManager.getDaoSession().getFileTransferTaskInfoDao();
    }


    public FileUploadRecordInfoDao getFileUploadRecordInfoDao() {
        return mManager.getDaoSession().getFileUploadRecordInfoDao();
    }

    public UserInfoDao getUserInfoDao() {
        return mManager.getDaoSession().getUserInfoDao();
    }

    public GroupInfoDao getGroupInfoDao() {
        return mManager.getDaoSession().getGroupInfoDao();
    }

    public NoticeAssociationTabDao getNoticeAssociationTabDao() {
        return mManager.getDaoSession().getNoticeAssociationTabDao();
    }

    public PushDataExtraInfoDao getPushDataExtraInfoDao() {
        return mManager.getDaoSession().getPushDataExtraInfoDao();
    }

    public AppItemBeanDao getAppItemBeanDao() {
        return mManager.getDaoSession().getAppItemBeanDao();
    }


    public MyGroupInfoDao getMyGroupInfoDao() {
        return mManager.getDaoSession().getMyGroupInfoDao();
    }

    public MyFriendInfoDao getMyFriendInfoDao() {
        return mManager.getDaoSession().getMyFriendInfoDao();
    }

    public MyAppInfoDao getMyAppInfoDao() {
        return mManager.getDaoSession().getMyAppInfoDao();
    }

    public MyFavoriteAppInfoDao getMyFavoriteAppInfoDao() {
        return mManager.getDaoSession().getMyFavoriteAppInfoDao();
    }


    public TagDataInfoDao getTagDataInfo() {
        return mManager.getDaoSession().getTagDataInfoDao();
    }


    /**
     * 通过事物处理数据库操作
     *
     * @param callable
     * @param <T>
     * @return
     */
    public <T> long commitByTransaction(Callable<Long> callable) {
        long result = 0;
        try {
            result = mManager.getDaoSession().callInTx(callable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
