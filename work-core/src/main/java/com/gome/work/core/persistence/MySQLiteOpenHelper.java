package com.gome.work.core.persistence;

import android.content.Context;

import com.gome.core.greendao.*;

import org.greenrobot.greendao.database.Database;

/**
 * Created by chaergongzi on 2018/8/15.
 */

class MySQLiteOpenHelper extends DaoMaster.OpenHelper {

    public MySQLiteOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        MigrationHelper2.migrate(db,
                FileItemInfoDao.class,
                FileTransferTaskInfoDao.class,
                FileUploadRecordInfoDao.class,
                ConversationInfoDao.class,
                PushDataExtraInfoDao.class,
                MyGroupInfoDao.class,
                MyFriendInfoDao.class,
                MyAppInfoDao.class,
                MyFavoriteAppInfoDao.class,
                TagDataInfoDao.class,
                FileTransferTaskInfoDao.class,
                FileUploadRecordInfoDao.class

        );

    }
}
