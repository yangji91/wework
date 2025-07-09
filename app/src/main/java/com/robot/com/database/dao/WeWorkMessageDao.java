package com.robot.com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.robot.com.database.entity.WeWorkMessage;

import java.util.List;

/***
 *@author 
 *@date 2021/8/5
 *@description
 ****/

@Dao
public interface WeWorkMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WeWorkMessage... weWorkMessages);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<WeWorkMessage> weWorkMessages );

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(WeWorkMessage... weWorkMessages);


    @Query("SELECT * FROM weworkmessage WHERE LID == :id")
    WeWorkMessage selectByLID(long id);

    @Query("SELECT * FROM weworkmessage WHERE is_upload != 1")
    List<WeWorkMessage>  selectByUnUpload(  );

    @Query("SELECT * FROM weworkmessage  ")
    List<WeWorkMessage> selectALL( );

    @Query("SELECT * FROM weworkmessage WHERE LID IN(:ids)")
    List<WeWorkMessage>  selectByIds( long...ids );

    @Query("UPDATE weworkmessage set uid =:uid where LID IN(:ids)")
    int updateUid(String uid,List<Long> ids );

    @Query("SELECT * FROM weworkmessage WHERE send_time >=:startTime and send_time <:endTime and is_upload=0 ")
    List<WeWorkMessage>  selectByTimeBetween(long startTime,long endTime );
}
