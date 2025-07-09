package com.robot.com.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/***
 *@author 
 *@date 2021/8/4
 *@description
 ****/
@Entity
public class WeWorkConversation {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "LID")
   public long LID;
    @ColumnInfo(name = "RID")
   public long RID;
    @ColumnInfo(name = "name")
   public String name;
    @ColumnInfo(name = "creater_id")
   public long createrId;
    @ColumnInfo(name = "modify_time")
   public long modifyTime;
    @ColumnInfo(name = "create_time")
   public long createTime;
    @ColumnInfo(name = "is_sticky")
   public int isSticky;
    @ColumnInfo(name = "is_inactive")
   public long isInactive;
    @ColumnInfo(name = "last_message_content")
   public String lastMessageContent;
    @ColumnInfo(name = "msg_sects")
   public String msgSects;

    @ColumnInfo(name = "conversationtype")
   public int conversationType;

    @ColumnInfo(name = "exited")
   public int exited;
    @ColumnInfo(name = "hidden")
   public int hidden;
    @ColumnInfo(name = "extras")
   public String extras;
    @ColumnInfo(name = "search_time")
   public long searchTime;
    @ColumnInfo(name = "fw_id")
   public long fwId;
   @ColumnInfo(name = "member_bin")
   public String memberBin;
   @ColumnInfo(name = "upload_time")
   public long uploadTime;


}
