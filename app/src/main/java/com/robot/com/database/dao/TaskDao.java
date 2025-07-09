package com.robot.com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.robot.com.database.entity.Task;

import java.util.List;

/***
 *@author 
 *@date 2021/8/5
 *@description
 ****/
@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Task task);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Task task);

    @Query("SELECT * FROM Task WHERE  uid== :id")
    Task selectByUid(String id);

    @Query("SELECT * FROM Task WHERE  uid== :id and  create_time >=:time and create_time <:endTime")
    Task selectByUidAndTime (String id,long time,long endTime);

    @Query("SELECT * FROM Task   ")
    List<Task> selectAll( );
}
