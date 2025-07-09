package com.robot.com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.robot.com.database.entity.WeWorkFileInfo;

/***
 *@author 
 *@date 2021/8/27
 *@description
 ****/
@Dao
public interface WeWorkFileInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WeWorkFileInfo... weWorkFileInfos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void update(WeWorkFileInfo... weWorkFileInfos);

    @Query("SELECT * FROM weworkfileinfo WHERE  md5== :md5")
    WeWorkFileInfo selectByMD5(String md5);

    @Query("SELECT * FROM weworkfileinfo WHERE  message_id== :messageId")
    WeWorkFileInfo selectByMessageId(long messageId);
}
