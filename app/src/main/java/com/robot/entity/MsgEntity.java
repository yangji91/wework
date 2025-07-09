package com.robot.entity;

import java.io.Serializable;
import java.util.List;

public class MsgEntity {
    /**
     * Object msg = RobotHelpers.callMethod(v,"requestInfo");
     * int ackSendState = RobotHelpers.getIntField(msg,"ackSendState");
     * long asId = RobotHelpers.getLongField(msg,"asId");
     * byte[] bs = (byte[]) RobotHelpers.getObjectField(msg,"content");
     * String content = null;
     * if(bs != null && bs.length > 0){
     * content = StrUtils.ByteToString(bs);
     * }
     * long id = RobotHelpers.getLongField(msg,"id");
     * int contentType = RobotHelpers.getIntField(msg,"contentType");
     * int convType = RobotHelpers.getIntField(msg,"convType");
     * long conversationId = RobotHelpers.getLongField(msg,"conversationId");
     * int flag = RobotHelpers.getIntField(msg,"flag");
     * long referid = RobotHelpers.getLongField(msg,"referid");
     * long remoteId = RobotHelpers.getLongField(msg,"remoteId");
     * long sender = RobotHelpers.getLongField(msg,"sender");
     * int state = RobotHelpers.getIntField(msg,"state");
     * String url = (String) RobotHelpers.getObjectField(msg,"url");
     * Object msgExtras = RobotHelpers.getObjectField(msg,"extras");
     * long receiver = RobotHelpers.getLongField(msgExtras,"receiver");
     * String sendername = (String) RobotHelpers.getObjectField(msgExtras,"sendername");
     */

    public String appinfo;
    public int ackSendState;
    public long asId;
    public String content;
    public long id;
    public long msgId;
    public long mNativeHandle;
    public int contentType;
    public int convType;
    public long devinfo;
    public long conversationId;
    public int flag;
    public long referid;
    public long remoteId;
    public long sender;
    public int state;
    public String url;
    public long receiver;
    public String sendername;
    //自加
    public String realRemark;
    //自加
    public String callToPhoneNum;
    //自加 0.主叫，1.被叫
    public String callType = "";
    public long callStartTime = 0L;
    public long callEndTime = 0L;

    public String inviterName;
    public List<String> memberNames;
    public List<Long> memberIds;
    public long senderTime;
    public FileMsgEntity fileMsgEntity;
    public PersonalCardMsgEntity personalCardMsgEntity;
    public WeAppMsgEntity weAppMsgEntity;
    public LinkMsgEntity linkMsgEntity;
    public VideoMessage videoMsgEntity;
    public int sendScene;
    public int outContact;
    public long seq;
    public MessageExtras extras;
    public RoomTipsList roomTipsList;
    public List<AtUserEntity> atUsers;
    /**
     * NOT_DONE(0, "未知别"),
     * DOING(1, "正在识别"),
     * SUCCESS(2, "识别成功"),
     * ASR_ERROR(3, "识别失败"),
     * GET_FILE_ERROR(4, "下载失败");
     */
    public long sttStatus;//语音识别状态
    public String sttText;//语音识别内容

    public Object originMsg;
    public static class AtUserEntity implements Serializable {
        public String nickName;
        public Long remoteId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ackSendState:" + ackSendState + " asId:" + asId + " content:" + content + " id:" + id);
        builder.append(" contentType:" + contentType + " convType:" + convType + " converId:" + conversationId);
        builder.append(" flag:" + flag + " referId:" + referid + " remoteId:" + remoteId + " sender:" + sender);
        builder.append(" state:" + state + " url:" + url + " receiver:" + receiver + " senderName:" + sendername);
        builder.append(" callToPhoneNum:" + callToPhoneNum + " memberIds:" + memberIds + " memberNames:" + memberNames + " inviterName:" + inviterName);
        builder.append(" callType:" + callType + " callStartTime:" + callStartTime + " callEndTime:" + callEndTime);
        builder.append(" remoteId:" + remoteId + " appinfo:" + this.appinfo);
        return builder.toString();
    }

    public static class MessageExtras implements Serializable {
        public boolean canNotBeLastmessage;
        public int decryptRet;
        public boolean disableDataDetector;
        public boolean firstSentMessageThatDay;
        public boolean isAlertReachedReaded;
        public boolean isSvrFail;
        public byte[] openapiAssociateKey;
        public long openapiAssociateType;
        public long orderTime;
        public long[] readuins;
        public boolean receiptModeEntry;
        public long receiver;
        public long revokeTime;
        public long sendErrorCode;
        public String sendername;
        public boolean showTranslation;
        public long staffVid;
        public long topMsgCreatorId;
        public byte[] translation;
        public byte[] translationProvider;
        public long[] unreaduins;
    }


}
