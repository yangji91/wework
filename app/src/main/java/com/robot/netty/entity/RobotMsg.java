package com.robot.netty.entity;

import com.google.gson.JsonObject;

import java.io.Serializable;

public class RobotMsg implements Serializable {
    public long requestId;
    public CommonHeader header;
    public JsonObject body;

    public long getRequestId(){
        return this.requestId;
    }

    public void setRequestId(long requestId){
        this.requestId = requestId;
    }

    public void setHeader(CommonHeader header){
        this.header = header;
    }

    public void setBody(JsonObject jsonObject){
        this.body = jsonObject;
    }


    public static final RobotMsg buildRobotMsg(Long remoteId,JsonObject jsonObject){
        RobotMsg robotMsg = new RobotMsg();
//        robotMsg.setRequestId(MConfiger.getSeqNo());
//        robotMsg.setHeader(CommonHeader.buildCommonHeader(remoteId));
        robotMsg.setBody(jsonObject);
        return robotMsg;
    }

    public JsonObject getBody(){
        return this.body;
    }
}
