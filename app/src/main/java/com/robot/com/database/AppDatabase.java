package com.robot.com.database;

import android.content.Context;
import android.os.Environment;


import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.robot.com.database.dao.RobotRunningInfoDao;
import com.robot.com.database.dao.TaskDao;
import com.robot.com.database.dao.WeWorkConversationDao;
import com.robot.com.database.dao.WeWorkFileInfoDao;
import com.robot.com.database.dao.WeWorkInfoDao;
import com.robot.com.database.dao.WeWorkMessageDao;
import com.robot.com.database.dao.WeWorkUserDao;
import com.robot.com.database.entity.RobotRunningInfo;
import com.robot.com.database.entity.Task;
import com.robot.com.database.entity.WeWorkConversation;
import com.robot.com.database.entity.WeWorkFileInfo;
import com.robot.com.database.entity.WeWorkInfo;
import com.robot.com.database.entity.WeWorkMessage;
import com.robot.com.database.entity.WeWorkUser;
import com.robot.com.database.service.BaseService;

import java.io.File;


public class AppDatabase {
    private static final AppDatabase INSTANCE = new AppDatabase();
    private String mWeixinDbPassword = "17admin";
    private WeWorkMessageAppDatabase weWorkMessageAppDatabase;
    public String path ="";
    public static final String DB_MESSANE_NAME="message.db";


    private WeWorkMessageAppDatabase buildWeworkMessageAppDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), WeWorkMessageAppDatabase.class, path+"/"+DB_MESSANE_NAME)
                .fallbackToDestructiveMigration()
                //.addMigrations(AppDatabaseMigration12)
                .allowMainThreadQueries()
                .build();
    }


    public synchronized void release() {
        if (weWorkMessageAppDatabase != null) {
            weWorkMessageAppDatabase.close();
            weWorkMessageAppDatabase = null;
        }


    }

    public synchronized void init(Context context,long count) {
        if (BaseService.enable) {
            File dbFile = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                    String.format("robot/com/db/%d/", count)
            );

            if (!dbFile.exists()) {
                dbFile.mkdirs();
            }
            path = dbFile.getAbsolutePath();
            weWorkMessageAppDatabase = buildWeworkMessageAppDatabase(context);
        }
    }



    public static AppDatabase getInstance() {
        return INSTANCE;
    }

    private AppDatabase() {

    }
    private final Migration  AppDatabaseMigration12 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE WeWorkMessage  ADD `app_info ` TEXT ; "  );
            database.execSQL("CREATE UNIQUE INDEX    Lid   ON  WeWorkMessage  ( LID  );");
        }
    };
    @Database(entities = {WeWorkMessage.class,Task.class,WeWorkConversation.class, WeWorkUser.class, WeWorkInfo.class, WeWorkFileInfo.class, RobotRunningInfo.class}, version = 2, exportSchema = false)
    public static abstract class WeWorkMessageAppDatabase extends RoomDatabase {
        public abstract WeWorkMessageDao getWeWorkMessageDao();
        public abstract WeWorkConversationDao getWeWorkConversationDao();
        public abstract WeWorkUserDao getWeWorkUserDao();
        public abstract WeWorkInfoDao getWeWorkInfoDao();
        public abstract WeWorkFileInfoDao getWeWorkFileInfoDao();
        public abstract TaskDao getTaskDao();
        public abstract RobotRunningInfoDao getRobotRunningInfoDao();
    }


    public static WeWorkMessageAppDatabase getWeWorkMessageAppDatabase() {
        return getInstance(). weWorkMessageAppDatabase;
    }

}
