package com.robot.entity;


import com.robot.netty.entity.rsp.PRspActionTextMsgEntity;

public class ParamSendLinkEntity {
    public long converId;
    public String description="";
    public String linkUrl="";
    public String imageUrl="";
    public String title="";
    public Long msgId;
    public PRspActionTextMsgEntity.PMsgAtDetailEntity atItemEntity;


    @Override
    public String toString() {
        return "title:" + title + " convertid:" + converId + " desc:" + description + " imgUrl:" + imageUrl;
    }
}
