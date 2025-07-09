package com.robot.entity;

public enum ActionStatusEnum {
    SUCC(0,"成功执行"),
    FAILURE(1,"执行失败"),
    //TIMEOUT(2,"执行超时"),
    REPEAT(5,"其他问题"),
    SUCC_PART(101,"部分成功");

    private int status;
    private String desc;

    ActionStatusEnum(int status,String desc){
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
