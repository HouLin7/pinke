package com.gome.core.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.gome.work.core.model.dao.FileTransferTaskInfo;
import com.gome.work.core.model.dao.MyAppInfo;
import com.gome.work.core.model.dao.TagDataInfo;
import com.gome.work.core.model.dao.FileUploadRecordInfo;
import com.gome.work.core.model.dao.MyGroupInfo;
import com.gome.work.core.model.dao.FileItemInfo;
import com.gome.work.core.model.dao.MyFriendInfo;
import com.gome.work.core.model.appmarket.AppItemBean;
import com.gome.work.core.model.UserInfo;
import com.gome.work.core.model.im.ConversationInfo;
import com.gome.work.core.model.im.PushDataExtraInfo;
import com.gome.work.core.model.im.GroupInfo;

import com.gome.core.greendao.FileTransferTaskInfoDao;
import com.gome.core.greendao.MyAppInfoDao;
import com.gome.core.greendao.TagDataInfoDao;
import com.gome.core.greendao.FileUploadRecordInfoDao;
import com.gome.core.greendao.MyGroupInfoDao;
import com.gome.core.greendao.FileItemInfoDao;
import com.gome.core.greendao.MyFriendInfoDao;
import com.gome.core.greendao.AppItemBeanDao;
import com.gome.core.greendao.UserInfoDao;
import com.gome.core.greendao.ConversationInfoDao;
import com.gome.core.greendao.PushDataExtraInfoDao;
import com.gome.core.greendao.GroupInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig fileTransferTaskInfoDaoConfig;
    private final DaoConfig myAppInfoDaoConfig;
    private final DaoConfig tagDataInfoDaoConfig;
    private final DaoConfig fileUploadRecordInfoDaoConfig;
    private final DaoConfig myGroupInfoDaoConfig;
    private final DaoConfig fileItemInfoDaoConfig;
    private final DaoConfig myFriendInfoDaoConfig;
    private final DaoConfig appItemBeanDaoConfig;
    private final DaoConfig userInfoDaoConfig;
    private final DaoConfig conversationInfoDaoConfig;
    private final DaoConfig pushDataExtraInfoDaoConfig;
    private final DaoConfig groupInfoDaoConfig;

    private final FileTransferTaskInfoDao fileTransferTaskInfoDao;
    private final MyAppInfoDao myAppInfoDao;
    private final TagDataInfoDao tagDataInfoDao;
    private final FileUploadRecordInfoDao fileUploadRecordInfoDao;
    private final MyGroupInfoDao myGroupInfoDao;
    private final FileItemInfoDao fileItemInfoDao;
    private final MyFriendInfoDao myFriendInfoDao;
    private final AppItemBeanDao appItemBeanDao;
    private final UserInfoDao userInfoDao;
    private final ConversationInfoDao conversationInfoDao;
    private final PushDataExtraInfoDao pushDataExtraInfoDao;
    private final GroupInfoDao groupInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        fileTransferTaskInfoDaoConfig = daoConfigMap.get(FileTransferTaskInfoDao.class).clone();
        fileTransferTaskInfoDaoConfig.initIdentityScope(type);

        myAppInfoDaoConfig = daoConfigMap.get(MyAppInfoDao.class).clone();
        myAppInfoDaoConfig.initIdentityScope(type);

        tagDataInfoDaoConfig = daoConfigMap.get(TagDataInfoDao.class).clone();
        tagDataInfoDaoConfig.initIdentityScope(type);

        fileUploadRecordInfoDaoConfig = daoConfigMap.get(FileUploadRecordInfoDao.class).clone();
        fileUploadRecordInfoDaoConfig.initIdentityScope(type);

        myGroupInfoDaoConfig = daoConfigMap.get(MyGroupInfoDao.class).clone();
        myGroupInfoDaoConfig.initIdentityScope(type);

        fileItemInfoDaoConfig = daoConfigMap.get(FileItemInfoDao.class).clone();
        fileItemInfoDaoConfig.initIdentityScope(type);

        myFriendInfoDaoConfig = daoConfigMap.get(MyFriendInfoDao.class).clone();
        myFriendInfoDaoConfig.initIdentityScope(type);

        appItemBeanDaoConfig = daoConfigMap.get(AppItemBeanDao.class).clone();
        appItemBeanDaoConfig.initIdentityScope(type);

        userInfoDaoConfig = daoConfigMap.get(UserInfoDao.class).clone();
        userInfoDaoConfig.initIdentityScope(type);

        conversationInfoDaoConfig = daoConfigMap.get(ConversationInfoDao.class).clone();
        conversationInfoDaoConfig.initIdentityScope(type);

        pushDataExtraInfoDaoConfig = daoConfigMap.get(PushDataExtraInfoDao.class).clone();
        pushDataExtraInfoDaoConfig.initIdentityScope(type);

        groupInfoDaoConfig = daoConfigMap.get(GroupInfoDao.class).clone();
        groupInfoDaoConfig.initIdentityScope(type);

        fileTransferTaskInfoDao = new FileTransferTaskInfoDao(fileTransferTaskInfoDaoConfig, this);
        myAppInfoDao = new MyAppInfoDao(myAppInfoDaoConfig, this);
        tagDataInfoDao = new TagDataInfoDao(tagDataInfoDaoConfig, this);
        fileUploadRecordInfoDao = new FileUploadRecordInfoDao(fileUploadRecordInfoDaoConfig, this);
        myGroupInfoDao = new MyGroupInfoDao(myGroupInfoDaoConfig, this);
        fileItemInfoDao = new FileItemInfoDao(fileItemInfoDaoConfig, this);
        myFriendInfoDao = new MyFriendInfoDao(myFriendInfoDaoConfig, this);
        appItemBeanDao = new AppItemBeanDao(appItemBeanDaoConfig, this);
        userInfoDao = new UserInfoDao(userInfoDaoConfig, this);
        conversationInfoDao = new ConversationInfoDao(conversationInfoDaoConfig, this);
        pushDataExtraInfoDao = new PushDataExtraInfoDao(pushDataExtraInfoDaoConfig, this);
        groupInfoDao = new GroupInfoDao(groupInfoDaoConfig, this);

        registerDao(FileTransferTaskInfo.class, fileTransferTaskInfoDao);
        registerDao(MyAppInfo.class, myAppInfoDao);
        registerDao(TagDataInfo.class, tagDataInfoDao);
        registerDao(FileUploadRecordInfo.class, fileUploadRecordInfoDao);
        registerDao(MyGroupInfo.class, myGroupInfoDao);
        registerDao(FileItemInfo.class, fileItemInfoDao);
        registerDao(MyFriendInfo.class, myFriendInfoDao);
        registerDao(AppItemBean.class, appItemBeanDao);
        registerDao(UserInfo.class, userInfoDao);
        registerDao(ConversationInfo.class, conversationInfoDao);
        registerDao(PushDataExtraInfo.class, pushDataExtraInfoDao);
        registerDao(GroupInfo.class, groupInfoDao);
    }
    
    public void clear() {
        fileTransferTaskInfoDaoConfig.clearIdentityScope();
        myAppInfoDaoConfig.clearIdentityScope();
        tagDataInfoDaoConfig.clearIdentityScope();
        fileUploadRecordInfoDaoConfig.clearIdentityScope();
        myGroupInfoDaoConfig.clearIdentityScope();
        fileItemInfoDaoConfig.clearIdentityScope();
        myFriendInfoDaoConfig.clearIdentityScope();
        appItemBeanDaoConfig.clearIdentityScope();
        userInfoDaoConfig.clearIdentityScope();
        conversationInfoDaoConfig.clearIdentityScope();
        pushDataExtraInfoDaoConfig.clearIdentityScope();
        groupInfoDaoConfig.clearIdentityScope();
    }

    public FileTransferTaskInfoDao getFileTransferTaskInfoDao() {
        return fileTransferTaskInfoDao;
    }

    public MyAppInfoDao getMyAppInfoDao() {
        return myAppInfoDao;
    }

    public TagDataInfoDao getTagDataInfoDao() {
        return tagDataInfoDao;
    }

    public FileUploadRecordInfoDao getFileUploadRecordInfoDao() {
        return fileUploadRecordInfoDao;
    }

    public MyGroupInfoDao getMyGroupInfoDao() {
        return myGroupInfoDao;
    }

    public FileItemInfoDao getFileItemInfoDao() {
        return fileItemInfoDao;
    }

    public MyFriendInfoDao getMyFriendInfoDao() {
        return myFriendInfoDao;
    }

    public AppItemBeanDao getAppItemBeanDao() {
        return appItemBeanDao;
    }

    public UserInfoDao getUserInfoDao() {
        return userInfoDao;
    }

    public ConversationInfoDao getConversationInfoDao() {
        return conversationInfoDao;
    }

    public PushDataExtraInfoDao getPushDataExtraInfoDao() {
        return pushDataExtraInfoDao;
    }

    public GroupInfoDao getGroupInfoDao() {
        return groupInfoDao;
    }

}
