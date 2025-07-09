package com.robot.entity;

public enum  RobotStatusEnum {
    STATUS_NORMAL(1,"在线"),
    STATUS_OFFLINE(2,"离线");


    private int status;
    private String desc;

    RobotStatusEnum(int status,String desc){
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
