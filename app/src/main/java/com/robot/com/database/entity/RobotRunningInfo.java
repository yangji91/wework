package com.robot.com.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/***
 *@author 
 *@date 2021/10/13
 *@description 运行日志
 ****/
@Entity
public class RobotRunningInfo {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id" )
    @NonNull
    public int id;
    @ColumnInfo(name = "event")
    @NonNull
    public String event;

    @ColumnInfo(name ="create_time")
    public long createTime ;
    @ColumnInfo(name ="update_time")

    public long updateTime ;
    @ColumnInfo(name ="user_id")
    public long userId;

    @ColumnInfo(name = "user_name")
    public String content;

    @ColumnInfo(name = "message")
    public String message;

    @ColumnInfo(name = "code")
    public int code;

    @ColumnInfo(name = "up_content")
    public String upContent;

    @ColumnInfo(name = "is_upload")
    public int isUpload;

    @ColumnInfo(name = "remark")
    public String remark;
}
