package com.robot.entity;

public enum WelcomeMediaTypeEnum {
    TYPE_TEXT(0,"文本"),
    TYPE_IMG(7,"图片"),
    TYPE_LINK(13,"链接类型");

    private int type;
    private String desc;

    WelcomeMediaTypeEnum(int type,String desc){
        this.type = type;
        this.desc = desc;
    }

    public int getType(){
        return this.type;
    }

    public String getDesc(){
        return this.desc;
    }
}
