package com.robot.com.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/***
 *@author 
 *@date 2021/8/5
 *@description
 ****/
@Entity
public class WeWorkInfo {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "nickname")
    public String nickname;
    @ColumnInfo(name = "remoteId")
    public long remoteId;
    @ColumnInfo(name = "mobile")
    public String mobile;
    @ColumnInfo(name = "avatorUrl")
    public String avatorUrl;
    @ColumnInfo(name = "bbsId")
    public long bbsId;
    @ColumnInfo(name = "bbsName")
    public String bbsName;
    @ColumnInfo(name = "acctid")
    public String acctid;



}
