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
public class WeWorkMessage {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "LID")
    public long LID;

    @ColumnInfo(name = "message_type")
    public int messageType;

    @ColumnInfo(name = "conv_id")
    public long convId;

    @ColumnInfo(name = "sender_id")
    public long senderId;


    @ColumnInfo(name = "seq")
    public int seq;

    @ColumnInfo(name = "at_user")
    public String atUser;

    @ColumnInfo(name = "flag")
    public int flag;

    @ColumnInfo(name = "content_type")
    public int contentType;

    @ColumnInfo(name = "send_time")
    public long sendTime;

    @ColumnInfo(name = "upload_time")
    public long uploadTime;


    @ColumnInfo(name = "url")
    public String url;


    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "extras")
    public String extras;


    @ColumnInfo(name = "devinfo")
    public long devinfo;

    @ColumnInfo(name = "refer")
    public int refer;

    @ColumnInfo(name = "uid")
    public String uid;

    @ColumnInfo(name = "extracontent")
    public String extraContent;

    @ColumnInfo(name = "is_upload")
    public int isUpload;

    @ColumnInfo(name = "up_content")
    public String upContent;
    @ColumnInfo(name = "app_info")
    public String appInfo;

    @NonNull
    @Override
    public String toString() {
        return "WeWorkMessage{" +
                "id=" + id +
                ", LID=" + LID +
                ", messageType='" + messageType + '\'' +
                ", convId='" + convId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", seq='" + seq + '\'' +
                ", atUser='" + atUser + '\'' +
                ", flag='" + flag + '\'' +
                ", contentType='" + contentType + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", uploadTime='" + uploadTime + '\'' +
                ", url='" + url + '\'' +
                ", content='" + content + '\'' +
                ", extras='" + extras + '\'' +
                ", devinfo='" + devinfo + '\'' +
                ", refer='" + refer + '\'' +
                ", uid='" + uid + '\'' +
                ", extraContent='" + extraContent + '\'' +
                ", isUpload='" + isUpload + '\'' +
                ", upContent='" + upContent + '\'' +
                ", appInfo='" + appInfo + '\'' +
                '}';
    }
}
