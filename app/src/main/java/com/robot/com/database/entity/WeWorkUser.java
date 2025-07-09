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
public class WeWorkUser {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public  long id ;

    @ColumnInfo(name = "RID")
    public long RID;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "gender")
    public int gender;
    @ColumnInfo(name = "email")
    public String email;
    @ColumnInfo(name = "birthday")
    public String birthday;
    @ColumnInfo(name = "mobile")
    public String  mobile;
    @ColumnInfo(name = "phone")
    public String phone;
    @ColumnInfo(name = "job")
    public String job;
    @ColumnInfo(name = "number")
    public String number;
    @ColumnInfo(name = "level")
    public int level;
    @ColumnInfo(name = "avatarurl")
    public String avatarurl;
    @ColumnInfo(name = "fts")
    public String fts;
    @ColumnInfo(name = "full")
    public String full;
    @ColumnInfo(name = "head")
    public String head;
    @ColumnInfo(name = "eng")
    public String eng;
    @ColumnInfo(name = "hot")
    public int  hot;
    @ColumnInfo(name = "disp_order")
    public int  dispOrder;
    @ColumnInfo(name = "attr")
    public int  attr;
    @ColumnInfo(name = "alias")
    public String  alias;
    @ColumnInfo(name = "fullpath")
    public String  fullpath;
    @ColumnInfo(name = "nameverified")
    public String  nameverified;
    @ColumnInfo(name = "corpid")
    public long  corpid;
    @ColumnInfo(name = "source")
    public int  source;
    @ColumnInfo(name = "extras")
    public String  extras;
    @ColumnInfo(name = "internation_code")
    public String  internationCode;
    @ColumnInfo(name = "acctid")
    public String  acctid;
    @ColumnInfo(name = "unionid")
    public String  unionid;



}
