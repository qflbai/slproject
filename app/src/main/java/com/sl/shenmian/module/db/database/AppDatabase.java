package com.sl.shenmian.module.db.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;

import com.sl.shenmian.App;
import com.sl.shenmian.module.db.dao.DBDao;
import com.sl.shenmian.module.db.entity.SealInfoEntity;

/**
 * @author WenXian Bai
 * @Date: 2018/9/21.
 * @Description:
 */
@Database(entities = {SealInfoEntity.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DBDao dbDao();

    private static final String DATABASE_NAME = "sl.db";
    private static AppDatabase mAppDatabase;

    public static AppDatabase getInstance() {
        if (mAppDatabase == null) {
            synchronized (AppDatabase.class) {
                if (mAppDatabase == null) {
                    mAppDatabase = Room.databaseBuilder(App.getAPPContext(), AppDatabase.class, DATABASE_NAME)
                            // .addMigrations(MIGRATION_1_2)
                            // 清空数据
                            .fallbackToDestructiveMigration()
                            // .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return mAppDatabase;
    }


    /**
     * 数据库版本 1->2 user表格新增了age列
     */
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // database.execSQL("ALTER TABLE User ADD COLUMN age integer");

            database.execSQL("drop table Acency");
            database.execSQL("drop table OverdueAcency");

        }
    };

    /**
     * 数据库版本 2->3 新增book表格
     */
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // database.execSQL("CREATE TABLE IF NOT EXISTS `book` (`uid` INTEGER PRIMARY KEY autoincrement, `name` TEXT , `userId` INTEGER, 'time' INTEGER)");
        }
    };


}
