package com.robot.entity;

import java.io.Serializable;

public class BConvItemEntity implements Serializable {
    public int convType;        //cuD()
    public long convLocalId;    //getLocalId()
    public long convRemoteId;   //getRemoteId()
    public long convServiceId;  //cuE()
    public String name;     //djz.j(getName(), 5);
    public boolean whole;   //Boolean.valueOf(cuL());
    public int flag;        //TextUtils.concat(new CharSequence[]{"0x", Integer.toHexString(this.jkT)});
    public int corps;       //Integer.valueOf(this.jkU.size());
    public int members;     //Integer.valueOf(getMemberCount());
    public boolean exConv;
    public boolean exMem;
    public int exitType;
    public boolean visible;
    public int exMemCount;
    public int unread;
    public int modify;      //Long.valueOf(this.jkr);
    public long create;     //Long.valueOf(this.mCreateTime);
    public boolean top;     //Boolean.valueOf(this.jks);
    public long messageLocalId;
    public long messageRemoteId;
    public long LatestID;   //fln.getMessageID(this.jkx);
    public Object objConv;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("convType:" + convType + " localId:" + convLocalId + " remoteId:" + convRemoteId + " serviceId:" + convServiceId
                + " name:" + name  + " whole:" + whole + " flag:" + flag + " members:" + members);
        return builder.toString();
    }
}
