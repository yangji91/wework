package com.robot.netty.proto.req;

import java.io.Serializable;

public class ReqExpansionStatusEntity implements Serializable {
    public long  robotUin;
    public int noticeType;
    public ReqExpansionStatusEntity.PExpansionStatus noticeBody;


    public static class PExpansionStatus implements Serializable{
        public int status; //1没有 2展示有
    }
}
