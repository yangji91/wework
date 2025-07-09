package com.robot.entity;

public class PVoiceEntity {
    public long id;
    public byte[] voiceArray;
    public long conversationId;
    public long sender;
    public String senderName;
    public long receiver;
    public long remoteId;
    public long sendTime;
    public int convType;
    public int contentType;
    public String ext = "";
}
