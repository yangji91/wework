package com.robot.entity;

import androidx.annotation.NonNull;

import com.robot.util.StrUtils;

/***
 *@author 
 *@date 2021/9/18
 *@description
 ****/
public class GroupData {
    public long createts;
    public int flag;
    public long id;
    public int memberCount;
    public long ownerVid;
    public long roomid;
    public String roomname;
    public String roomurl;
    public long updatets;
    public long createId;
    @NonNull
    @Override
    public String toString() {
        return StrUtils.objectToJson(this);
    }
}
