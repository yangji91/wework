package com.robot.com.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/***
 *@author 
 *@date 2021/8/23
 *@description 文件相关
 ****/
@Entity
public class WeWorkFileInfo {
    @PrimaryKey(autoGenerate =true)
    @ColumnInfo(name = "id")
    @NonNull
    public long id;
    @ColumnInfo(name = "md5")
    @NonNull
    public String md5;

    @ColumnInfo(name ="create_time")
    public long createTime ;

    @ColumnInfo(name ="update_time")
    public long updateTime;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "code")
    public int code;

    @ColumnInfo(name = "path")
    public String path;
    @ColumnInfo(name = "file_name")
    public String fileName;

    @ColumnInfo(name = "message_id")
    public long messageId;

    @ColumnInfo(name = "oss_url")
    public String OSSUrl;

    @ColumnInfo(name = " url")
    public String url;

    @ColumnInfo(name = "aes_key")
    public String aesKey;

    @ColumnInfo(name = "auth_key")
    public String authKey;

    @ColumnInfo(name = "file_id")
    public String fileId;

    @ColumnInfo(name = "size")
    public long size;

    @ColumnInfo(name ="complete_time")
    public long completeTime ;

    @ColumnInfo(name = "duration")
    public int duration;//时长
}
