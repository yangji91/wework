package com.robot.com.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/***
 *@author 
 *@date 2021/8/4
 *@description
 ****/
@Entity
public class Task {

    @PrimaryKey()
    @ColumnInfo(name = "uid")
    @NonNull
    public String uid;
    @ColumnInfo(name = "uin")
    @NonNull
    public long uin;

    @ColumnInfo(name ="create_time")
    public long createTime ;

    @ColumnInfo(name ="end_time")
    public long endTime;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "is_upload")
    public int isUpload;

    @ColumnInfo(name = "code")
    public int code;

    @ColumnInfo(name = "up_content")
    public String upContent;

    @ColumnInfo(name = "message")
    public String message;

    @ColumnInfo(name = "business_id")
    public String businessId;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "action_type")
    public int actionType;

    @ColumnInfo(name ="complete_time")
    public long completeTime ;
}
