package com.robot.com.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.robot.com.database.entity.WeWorkConversation;

import java.util.List;

/***
 *@author 
 *@date 2021/8/5
 *@description
 ****/
@Dao
public interface WeWorkConversationDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WeWorkConversation... weWorkConversations);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void update(WeWorkConversation... weWorkConversations);

    @Query("SELECT * FROM weworkconversation WHERE  RID = :remoteId")
    WeWorkConversation selectByRemoteId(long remoteId);

    @Query("SELECT * FROM weworkconversation WHERE  LID = :id")
    WeWorkConversation selectByLID(long id);


    @Query("SELECT * FROM weworkconversation  ")
    List<WeWorkConversation> selectAll( );

    @Query("SELECT * FROM weworkconversation WHERE RID IN(:remoteIds) ")
    List<WeWorkConversation> selectInRemoteId( long...remoteIds);

    @Query(value = "SELECT * FROM weworkconversation  where  name =:name    and   creater_id =:createId and  create_time =:createTime ")
    WeWorkConversation  selectByNameAndCreateIDAndCreateTime(String name, long createId, long createTime);

    @Query(value = "SELECT * FROM weworkconversation  where  name =:name       and  create_time =:createTime ")
    WeWorkConversation  selectByNameAndCreateTime(String name,  long createTime);

    @Query(value = "SELECT * FROM weworkconversation  where  name =:name       and  creater_id =:createId ")
    WeWorkConversation  selectByNameAndCreateID(String name,  long createId);

    @Query(value = "SELECT * FROM weworkconversation  where  name =:name  ")
    WeWorkConversation  selectByName(String name  );
}
