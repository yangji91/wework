package com.robot.netty.entity;

public enum PRspContentTypeEnum {
    TEXT(1,"文本"),
    IMAGE(3,"图片"),
    VOICE(34,"语音"),
    VIDEO(43,"视频"),
    EMOJI(47,"表情"),
    LOC(48,"定位"),
    FILE(49,"文件");

    private int type;
    private String desc;

    PRspContentTypeEnum(int type,String desc){
        this.type = type;
        this.desc = desc;
    }

    public int getType(){
        return this.type;
    }

    public String getDesc(){
        return this.desc;
    }

    public static PRspContentTypeEnum getEnumByType(Integer type){
        PRspContentTypeEnum rEnum = null;
        for(PRspContentTypeEnum itemEnum : PRspContentTypeEnum.values()){
            if(type != null && itemEnum != null && itemEnum.type == type){
                rEnum = itemEnum;
                break;
            }
        }
        return rEnum;
    }
}
