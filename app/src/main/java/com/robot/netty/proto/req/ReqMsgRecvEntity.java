package com.robot.netty.proto.req;

import androidx.annotation.NonNull;

import com.robot.entity.MsgEntity;
import com.robot.util.MyLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReqMsgRecvEntity implements Serializable {
    public long robotUin;
    public List<PMsgItemEntity> messages;

    public static class PMsgItemEntity implements Serializable{
        public int contentType;
        public long sendTime;
        public String appinfo;
        public long asId;
        public String content;
        public long id;
        public Long messageId;
        public int convType;
        public String conversationId;
        public int flag;
        public long referid;
        public long remoteId;
        public String sender;
        public String sendername;
        public String inviterName;
        public List<String> memberNames;
        public List<Long> memberIds;
        public int state;
        public long receiver;
        public long devinfo;
        public MsgEntity.MessageExtras extras;
        public List<MsgEntity.AtUserEntity> atUsers;
        public long sttStatus;//语音识别状态
        public String sttText;//语音识别内容
        public int voiceTime;//语音时长
        //自加字段
        public String callToPhoneNum;//打给谁
        //自加 0.主叫，1.被叫
        public String callType = "";
        public long callStartTime = 0L;
        public long callEndTime = 0L;
        @NonNull
        @Override
        public String toString() {
             return "PMsgItemEntity { conversationId:"+conversationId+" messageId:"+messageId+" sender:"+sender+" \n contentType :"+contentType+" content:"+content+" appinfo:"+appinfo+"}";
        }
    }



    private static final PMsgItemEntity convertByMsg(MsgEntity msgEntity){
        PMsgItemEntity rEntity = new PMsgItemEntity();
        rEntity.contentType = msgEntity.contentType;
        rEntity.sendTime = msgEntity.senderTime * 1000;
        rEntity.appinfo = msgEntity.appinfo;
        rEntity.asId = msgEntity.asId;
        rEntity.content = msgEntity.content;
        rEntity.id = msgEntity.id;
        rEntity.messageId = msgEntity.msgId;
        rEntity.convType = msgEntity.convType;
        rEntity.conversationId = msgEntity.conversationId+"";
        rEntity.flag = msgEntity.flag;
        rEntity.devinfo = msgEntity.devinfo;
        rEntity.referid = msgEntity.referid;
        rEntity.remoteId = msgEntity.remoteId;
        rEntity.sender = msgEntity.sender + "";
        rEntity.sendername = msgEntity.sendername;
        rEntity.inviterName = msgEntity.inviterName;
        rEntity.memberNames = msgEntity.memberNames;
        rEntity.memberIds = msgEntity.memberIds;
        rEntity.state = msgEntity.state;
        rEntity.receiver = msgEntity.receiver;
        rEntity.extras = msgEntity.extras;
        rEntity.atUsers = msgEntity.atUsers;
        rEntity.sttStatus=msgEntity.sttStatus;//语音识别状态
        rEntity.sttText=msgEntity.sttText;//语音识别内容
        rEntity.callToPhoneNum=msgEntity.callToPhoneNum;
        rEntity.callType=msgEntity.callType;
        rEntity.callStartTime=msgEntity.callStartTime;
        rEntity.callEndTime=msgEntity.callEndTime;
        if(msgEntity.fileMsgEntity!=null){
            rEntity.voiceTime=msgEntity.fileMsgEntity.voiceTime;//语音识别内容
        }
        return rEntity;
    }

    public static final List<PMsgItemEntity> convertByMsgList(List<MsgEntity> sList){
        List<PMsgItemEntity> rList = new ArrayList<>();
        if(sList != null && sList.size() > 0){
            for(MsgEntity msgEntity : sList){
                PMsgItemEntity itemEntity = convertByMsg(msgEntity);
                MyLog.debug("Entity",  "上报消息 " +  itemEntity ,true);
              rList.add(itemEntity);
            }
        }
        return rList;
    }

    public static final List<PMsgItemEntity> convertByMsgEntity(MsgEntity msgEntity){
        List<PMsgItemEntity> rList = new ArrayList<>();
        PMsgItemEntity itemEntity = convertByMsg(msgEntity);
        MyLog.debug("Entity",  "上报消息 " +  itemEntity);
        rList.add(itemEntity);
        return rList;
    }
}
