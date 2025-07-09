package com.robot.entity;

import com.robot.netty.entity.rsp.PRspActionTextMsgEntity;

import java.util.Date;

//消息发送实体
//记录所有 根据服务端任务发送的消息, 不包含客户端手动操作发送的消息, 以及接收到的消息
public class ActionHistoryEntity
{
    public String send_target=null;
    public String content=null;
    public Date action_recv_time=new Date();
    public Date action_start_exec_time=null;
    public Date action_exec_finished_time=null;
    public PRspActionTextMsgEntity.PContactMsgActionItem        actionItem=null;
}
