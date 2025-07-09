package com.robot.netty.proto.req;

import com.robot.entity.ActionRet;
import com.robot.entity.MsgEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReqOssNotityEntity implements Serializable {
    public long  robotUin;
    public int noticeType;
    public PNoticeBody noticeBody;


    public static class PNoticeBody implements Serializable{
        public List<PNoticeMsgItem> messages;
        public List<PNoticeActionItem> actions;
    }

    public static class PNoticeMsgItem implements  Serializable{
        public Integer contentType;
        public Integer convType;
        public String appinfo;
        public Integer code;
        public String codeDesc;
    }

    public static class PNoticeActionItem implements  Serializable{
        public String uid;  //任务唯一标识
        public Integer actionStatus;    //任务状态
        public ActionRet actionRet;     //
    }

    public static final PNoticeBody buildNoticeBody(MsgEntity msgEntity,boolean isSucc,String failMsg){
        PNoticeBody rEntity = new PNoticeBody();
        rEntity.messages = new ArrayList<>();
        PNoticeMsgItem pMsgItem = new PNoticeMsgItem();
        pMsgItem.contentType = msgEntity.contentType;
        pMsgItem.convType = msgEntity.convType;
        pMsgItem.appinfo = msgEntity.appinfo;
        if(isSucc){
            pMsgItem.code = 0;
        }else{
            pMsgItem.code = 1;
            pMsgItem.codeDesc = failMsg;
        }
        rEntity.messages.add(pMsgItem);
        return rEntity;
    }

    public static final PNoticeBody buildNoticeBody(String uid,ActionRet actionRet){
        PNoticeBody rEntity = new PNoticeBody();
        rEntity.actions = new ArrayList<>();
        PNoticeActionItem pNoticeActionItem = new PNoticeActionItem();
        pNoticeActionItem.uid = uid;
        pNoticeActionItem.actionStatus = 0;
        pNoticeActionItem.actionRet = actionRet;
        rEntity.actions.add(pNoticeActionItem);
        return rEntity;
    }
}
