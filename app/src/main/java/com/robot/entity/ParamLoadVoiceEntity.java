package com.robot.entity;

public class ParamLoadVoiceEntity {
    public long converId = 0l;  ////not used ,default 0
    public String fileId;
    public int fileType;  //4
    public long fileSize;
    public String filePath;
    public String aeskey;
    public String md5;

    public int voiceTime;

    public String rFilePath;
    public ParamLoadVoiceEntity() {
        converId = 0l;
        fileId = "";
        fileType = 0;
        fileSize = 0l;
        filePath = "";
        aeskey = "";
        md5 = "";
        voiceTime = 0;
    }

    @Override
    public String toString() {
        return "ParamLoadVoiceEntity{" +
                "converId=" + converId +
                ", fileId='" + fileId + '\'' +
                ", fileType=" + fileType +
                ", fileSize=" + fileSize +
                ", filePath='" + filePath + '\'' +
                ", aeskey='" + aeskey + '\'' +
                ", md5='" + md5 + '\'' +
                ", voiceTime=" + voiceTime +
                '}';
    }
}
