package com.gome.core.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.gome.work.core.model.UserVerifyPropertyItem;
import com.gome.work.core.model.converter.UserVerifyPropertyConverter;

import com.gome.work.core.model.UserInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER_INFO".
*/
public class UserInfoDao extends AbstractDao<UserInfo, String> {

    public static final String TABLENAME = "USER_INFO";

    /**
     * Properties of entity UserInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property ImId = new Property(1, String.class, "imId", false, "IM_ID");
        public final static Property Avatar = new Property(2, String.class, "avatar", false, "AVATAR");
        public final static Property Email = new Property(3, String.class, "email", false, "EMAIL");
        public final static Property Username = new Property(4, String.class, "username", false, "USERNAME");
        public final static Property Nickname = new Property(5, String.class, "nickname", false, "NICKNAME");
        public final static Property Sex = new Property(6, String.class, "sex", false, "SEX");
        public final static Property Address = new Property(7, String.class, "address", false, "ADDRESS");
        public final static Property Phone = new Property(8, String.class, "phone", false, "PHONE");
        public final static Property FirstLetter = new Property(9, String.class, "firstLetter", false, "FIRST_LETTER");
        public final static Property Grade = new Property(10, String.class, "grade", false, "GRADE");
        public final static Property School = new Property(11, String.class, "school", false, "SCHOOL");
        public final static Property Identity = new Property(12, String.class, "identity", false, "IDENTITY");
        public final static Property PartnerRelation = new Property(13, String.class, "partnerRelation", false, "PARTNER_RELATION");
        public final static Property FollowRelation = new Property(14, String.class, "followRelation", false, "FOLLOW_RELATION");
        public final static Property TeachAge = new Property(15, String.class, "teachAge", false, "TEACH_AGE");
        public final static Property TeachCourse = new Property(16, String.class, "teachCourse", false, "TEACH_COURSE");
        public final static Property DoorVisit = new Property(17, String.class, "doorVisit", false, "DOOR_VISIT");
        public final static Property Distance = new Property(18, String.class, "distance", false, "DISTANCE");
        public final static Property VerifyProperty = new Property(19, String.class, "verifyProperty", false, "VERIFY_PROPERTY");
    }

    private final UserVerifyPropertyConverter verifyPropertyConverter = new UserVerifyPropertyConverter();

    public UserInfoDao(DaoConfig config) {
        super(config);
    }
    
    public UserInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER_INFO\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "\"IM_ID\" TEXT," + // 1: imId
                "\"AVATAR\" TEXT," + // 2: avatar
                "\"EMAIL\" TEXT," + // 3: email
                "\"USERNAME\" TEXT," + // 4: username
                "\"NICKNAME\" TEXT," + // 5: nickname
                "\"SEX\" TEXT," + // 6: sex
                "\"ADDRESS\" TEXT," + // 7: address
                "\"PHONE\" TEXT," + // 8: phone
                "\"FIRST_LETTER\" TEXT," + // 9: firstLetter
                "\"GRADE\" TEXT," + // 10: grade
                "\"SCHOOL\" TEXT," + // 11: school
                "\"IDENTITY\" TEXT," + // 12: identity
                "\"PARTNER_RELATION\" TEXT," + // 13: partnerRelation
                "\"FOLLOW_RELATION\" TEXT," + // 14: followRelation
                "\"TEACH_AGE\" TEXT," + // 15: teachAge
                "\"TEACH_COURSE\" TEXT," + // 16: teachCourse
                "\"DOOR_VISIT\" TEXT," + // 17: doorVisit
                "\"DISTANCE\" TEXT," + // 18: distance
                "\"VERIFY_PROPERTY\" TEXT);"); // 19: verifyProperty
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, UserInfo entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String imId = entity.getImId();
        if (imId != null) {
            stmt.bindString(2, imId);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(3, avatar);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(4, email);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(5, username);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(6, nickname);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(7, sex);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(8, address);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(9, phone);
        }
 
        String firstLetter = entity.getFirstLetter();
        if (firstLetter != null) {
            stmt.bindString(10, firstLetter);
        }
 
        String grade = entity.getGrade();
        if (grade != null) {
            stmt.bindString(11, grade);
        }
 
        String school = entity.getSchool();
        if (school != null) {
            stmt.bindString(12, school);
        }
 
        String identity = entity.getIdentity();
        if (identity != null) {
            stmt.bindString(13, identity);
        }
 
        String partnerRelation = entity.getPartnerRelation();
        if (partnerRelation != null) {
            stmt.bindString(14, partnerRelation);
        }
 
        String followRelation = entity.getFollowRelation();
        if (followRelation != null) {
            stmt.bindString(15, followRelation);
        }
 
        String teachAge = entity.getTeachAge();
        if (teachAge != null) {
            stmt.bindString(16, teachAge);
        }
 
        String teachCourse = entity.getTeachCourse();
        if (teachCourse != null) {
            stmt.bindString(17, teachCourse);
        }
 
        String doorVisit = entity.getDoorVisit();
        if (doorVisit != null) {
            stmt.bindString(18, doorVisit);
        }
 
        String distance = entity.getDistance();
        if (distance != null) {
            stmt.bindString(19, distance);
        }
 
        UserVerifyPropertyItem verifyProperty = entity.getVerifyProperty();
        if (verifyProperty != null) {
            stmt.bindString(20, verifyPropertyConverter.convertToDatabaseValue(verifyProperty));
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, UserInfo entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String imId = entity.getImId();
        if (imId != null) {
            stmt.bindString(2, imId);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(3, avatar);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(4, email);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(5, username);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(6, nickname);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(7, sex);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(8, address);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(9, phone);
        }
 
        String firstLetter = entity.getFirstLetter();
        if (firstLetter != null) {
            stmt.bindString(10, firstLetter);
        }
 
        String grade = entity.getGrade();
        if (grade != null) {
            stmt.bindString(11, grade);
        }
 
        String school = entity.getSchool();
        if (school != null) {
            stmt.bindString(12, school);
        }
 
        String identity = entity.getIdentity();
        if (identity != null) {
            stmt.bindString(13, identity);
        }
 
        String partnerRelation = entity.getPartnerRelation();
        if (partnerRelation != null) {
            stmt.bindString(14, partnerRelation);
        }
 
        String followRelation = entity.getFollowRelation();
        if (followRelation != null) {
            stmt.bindString(15, followRelation);
        }
 
        String teachAge = entity.getTeachAge();
        if (teachAge != null) {
            stmt.bindString(16, teachAge);
        }
 
        String teachCourse = entity.getTeachCourse();
        if (teachCourse != null) {
            stmt.bindString(17, teachCourse);
        }
 
        String doorVisit = entity.getDoorVisit();
        if (doorVisit != null) {
            stmt.bindString(18, doorVisit);
        }
 
        String distance = entity.getDistance();
        if (distance != null) {
            stmt.bindString(19, distance);
        }
 
        UserVerifyPropertyItem verifyProperty = entity.getVerifyProperty();
        if (verifyProperty != null) {
            stmt.bindString(20, verifyPropertyConverter.convertToDatabaseValue(verifyProperty));
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public UserInfo readEntity(Cursor cursor, int offset) {
        UserInfo entity = new UserInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // imId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // avatar
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // email
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // username
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // nickname
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // sex
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // address
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // phone
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // firstLetter
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // grade
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // school
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // identity
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // partnerRelation
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // followRelation
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // teachAge
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // teachCourse
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // doorVisit
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // distance
            cursor.isNull(offset + 19) ? null : verifyPropertyConverter.convertToEntityProperty(cursor.getString(offset + 19)) // verifyProperty
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, UserInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setImId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAvatar(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setEmail(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUsername(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setNickname(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setSex(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setAddress(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setPhone(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setFirstLetter(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setGrade(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setSchool(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setIdentity(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setPartnerRelation(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setFollowRelation(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setTeachAge(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setTeachCourse(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setDoorVisit(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setDistance(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setVerifyProperty(cursor.isNull(offset + 19) ? null : verifyPropertyConverter.convertToEntityProperty(cursor.getString(offset + 19)));
     }
    
    @Override
    protected final String updateKeyAfterInsert(UserInfo entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(UserInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(UserInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
