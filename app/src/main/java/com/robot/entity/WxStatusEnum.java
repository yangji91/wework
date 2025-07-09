package com.robot.entity;

public enum WxStatusEnum {
    WX_STATUS_ONLINE(0,"正常上线"),
    WX_STATUS_LOGOUT(2,"退登被顶"),
    WX_STATUS_BAN(3,"账号被封"),
    WX_STATUS_UPDATE(4,"账号更新");


    private int status;
    private String desc;

    WxStatusEnum(int status,String desc){
        this.status = status;
        this.desc = desc;
    }

    public int getStatus(){
        return this.status;
    }

    public String getDesc(){
        return this.desc;
    }
}
