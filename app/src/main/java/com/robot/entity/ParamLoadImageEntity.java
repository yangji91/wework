package com.robot.entity;

public class ParamLoadImageEntity {
    public long converId = 0l;  ////not used ,default 0
    public int contentType;
    public String url;
    public String aesKey;
    public String authKey;
    public long fileSize;
    public String filePath;
    public String md5;

    public int fileType;
    public String fieldId;

    @Override
    public String toString() {
        return "ParamLoadImageEntity{" +
                "converId=" + converId +
                ", i=" + contentType +
                ", url='" + url + '\'' +
                ", aesKey='" + aesKey + '\'' +
                ", authKey='" + authKey + '\'' +
                ", fileSize=" + fileSize +
                ", filePath='" + filePath + '\'' +
                ", md5='" + md5 + '\'' +
                '}';
    }
}
