package com.robot.entity;

public enum ConvTypeEnum {
    TYPE_PERSON(0,"私聊");

    private int type;
    private String desc;

    ConvTypeEnum(int type,String desc){
        this.type = type;
        this.desc = desc;
    }

    public int getType(){
        return this.type;
    }

    public String getDesc(){
        return this.desc;
    }

    public static final ConvTypeEnum getEnumByType(int type){
        ConvTypeEnum rEnum = null;
        for(ConvTypeEnum convTypeEnum : ConvTypeEnum.values()){
            if(convTypeEnum != null && convTypeEnum.getType() == type){
                rEnum = convTypeEnum;
                break;
            }
        }
        return rEnum;
    }
}
