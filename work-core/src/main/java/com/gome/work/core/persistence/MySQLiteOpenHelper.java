package com.gome.work.core.persistence;

import android.content.Context;

import com.gome.core.greendao.DaoMaster;
import com.gome.core.greendao.FileItemInfoDao;
import com.gome.core.greendao.FileTransferTaskInfoDao;
import com.gome.core.greendao.FileUploadRecordInfoDao;
import com.gome.core.greendao.MyAppInfoDao;
import com.gome.core.greendao.MyFavoriteAppInfoDao;
import com.gome.core.greendao.MyFriendInfoDao;
import com.gome.core.greendao.MyGroupInfoDao;
import com.gome.core.greendao.NoticeAssociationTabDao;
import com.gome.core.greendao.PushDataExtraInfoDao;
import com.gome.core.greendao.TagDataInfoDao;

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
                NoticeAssociationTabDao.class,
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
