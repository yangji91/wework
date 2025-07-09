package com.robot.entity;


public class WelcomMsgDataInfoEntity{
    public int createts;
    public WelcomeMsgDataV2List data;
    public long id;
    public int isDelete;
    public long operator;
    public long updateVid;
    public int updatets;
    public String title;
    public Object obj;

    public static class WelcomeMsgDataV2List{
        public WelcomeMsgDataV2[] items;
        public String title;
    }


    public static class WelcomeMsgDataV2 {
        public byte[] content;
        public int contentType;
        public MessageItemEntity itemEntity;
    }
}
