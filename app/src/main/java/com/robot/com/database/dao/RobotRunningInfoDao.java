package com.robot.com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.robot.com.database.entity.RobotRunningInfo;

/***
 *@author 
 *@date 2021/10/13
 *@description
 ****/
@Dao
public interface RobotRunningInfoDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RobotRunningInfo info);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(RobotRunningInfo info);

    @Query("SELECT * FROM RobotRunningInfo where event =:event  ORDER BY update_time desc limit 1  ")
    RobotRunningInfo getRobotMessageCheckEvent(String event);
}
