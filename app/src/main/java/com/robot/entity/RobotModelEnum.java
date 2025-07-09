package com.robot.entity;

public enum  RobotModelEnum {
    MODEL_ROBOT(1,"机器人模式"),
    MODEL_PULL(4,"待机Pull模式");

    private int type;
    private String desc;

    RobotModelEnum(int type,String desc){
        this.type = type;
        this.desc = desc;
    }

    public int getType(){
        return this.type;
    }

    public String getDesc(){
        return this.desc;
    }

    public static final RobotModelEnum getEnumByType(int type){
        RobotModelEnum result = null;
        for(RobotModelEnum en : RobotModelEnum.values()){
            if(en.getType() == type){
                result = en;
                break;
            }
        }
        return result;
    }
}
