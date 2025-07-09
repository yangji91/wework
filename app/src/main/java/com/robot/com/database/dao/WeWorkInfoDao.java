package com.robot.com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.robot.com.database.entity.WeWorkInfo;

/***
 *@author 
 *@date 2021/8/5
 *@description
 ****/
@Dao
public interface WeWorkInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WeWorkInfo... workInfos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void update(WeWorkInfo... workInfos);

    @Query("SELECT * FROM weworkinfo WHERE  remoteId== :remoteId")
    WeWorkInfo selectByRemoteId(long  remoteId);

}
