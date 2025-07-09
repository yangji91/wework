package com.robot.entity;

public enum FileTypeEnum {
    TYPE_IMG(2,"图片类型"),
    TYPE_VIDEO(3,"视频"),
    TYPE_FILE(4,"文件类型类型(pdf,doc..)");

    private int type;
    private String desc;

    FileTypeEnum(int type,String desc){
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
