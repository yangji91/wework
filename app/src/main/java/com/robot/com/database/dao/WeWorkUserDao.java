package com.robot.com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.robot.com.database.entity.WeWorkUser;

import java.util.List;

/***
 *@author 
 *@date 2021/8/5
 *@description
 ****/
@Dao
public interface WeWorkUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WeWorkUser... weWorkUsers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void update(WeWorkUser... weWorkUsers);

    @Query("SELECT * FROM weworkuser WHERE  RID== :id")
    WeWorkUser selectByRID(long id);

    @Query("SELECT * FROM weworkuser  ")
    List<WeWorkUser> selectAll( );

    @Query("SELECT * FROM weworkuser WHERE RID IN(:ids) ")
    List<WeWorkUser> selectInRID( long...ids);

}
