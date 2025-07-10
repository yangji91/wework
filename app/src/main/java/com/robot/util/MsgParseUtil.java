package com.robot.util;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.robot.entity.FileMsgEntity;
import com.robot.entity.LinkMsgEntity;
import com.robot.entity.MsgEntity;
import com.robot.entity.SimpleMsgEntity;
import com.robot.entity.VideoMessage;
import com.robot.entity.WeAppMsgEntity;
import com.robot.entity.WelcomFileMsgEntity;
import com.robot.entity.WelcomeLinkeMsgEntity;
import com.robot.hook.KeyConst;
import com.robot.hook.msghandle.MsgHandleEnum;
import com.robot.com.BuildConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.robot.robothook.RobotHelpers;

import static com.robot.hook.msghandle.MsgHandleEnum.*;
//import static com.robot.hook.msghandle.MsgHandleEnum.MINI_APP;
//import static com.robot.hook.msghandle.MsgHandleEnum.PRIVATE_SEND_NOTIFY_MSG;
//import static com.robot.hook.msghandle.MsgHandleEnum.VIDEO;
//import static com.robot.hook.msghandle.MsgHandleEnum.VIDEO_MASSHELPER;
//import static com.robot.hook.msghandle.MsgHandleEnum.VIDEO_MASSHELPER__22;
//import static com.robot.hook.msghandle.MsgHandleEnum.VIDEO_SELF;

public class MsgParseUtil {
    private static final String TAG = MsgParseUtil.class.getSimpleName();
    private static Class clazzObserver;

   /* public static final MsgEntity parseMsgEntity(Object obj,final IObserverCallBack callBack){
        MsgEntity msgEntity = parseMsgEntity(obj);
        //add observer
        if(callBack != null){
            if(MsgParseUtil.clazzObserver == null){
                MsgParseUtil.clazzObserver = RobotHelpers.findClassIfExists(KeyConst.C_MESSAGE_IMessageObserver, Global.loadPackageParam.classLoader);
            }
            Class[] clazzArray = {MsgParseUtil.clazzObserver};
            Object objCallBack = Proxy.newProxyInstance(Global.loadPackageParam.classLoader, clazzArray, new InvocationHandler() {
                @Override
                public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                    String name = method.getName();
                    if(KeyConst.M_onMsgUpdate.equals(name)){
                        if(args != null && args.length >= 1){
                            Object objMsg = args[0];
                            if(callBack != null){
                                callBack.onMsgUpdate(objMsg);
                            }
                        }
                    }
                    return null;
                }
            });
            Object[] objArray = {objCallBack};
            RobotHelpers.callMethod(obj,KeyConst.M_AddObserver,clazzArray,objArray);
        }
        return msgEntity;
    }*/

    public static final MsgEntity parseMsgEntity(Object obj) {
        MsgEntity msgEntity = new MsgEntity();
        msgEntity.mNativeHandle = RobotHelpers.getLongField(obj, "mNativeHandle");
        Object msg = RobotHelpers.callMethod(obj, KeyConst.M_Message_requestInfo);
        int ackSendState = RobotHelpers.getIntField(msg, "ackSendState");
        long asId = RobotHelpers.getLongField(msg, "asId");
        byte[] bs = (byte[]) RobotHelpers.getObjectField(msg, "content");
        int contentType = RobotHelpers.getIntField(msg, "contentType");
        byte[] appinfo = (byte[]) RobotHelpers.getObjectField(msg, "appinfo");
        String content = null;
        if (BuildConfig.customConfigLog) {
            MyLog.debug(TAG, "[parseMsgEntity]" + "mNativeHandle " + msgEntity.mNativeHandle + " contentType:" + contentType + " bs:" + bs);
        }
        if (bs != null && bs.length > 0) {
            //101:image download; 102:file download
            if (MsgHandleEnum.needParseFile(contentType)) {
                FileMsgEntity fMsgEntity = FileImgParseUtil.parseData(bs, null);
                msgEntity.fileMsgEntity = fMsgEntity;
                MyLog.debug(TAG, "[parseMsgEntity]" + " fileMsgEntity->" + JSON.toJSONString(fMsgEntity));
            } else if (contentType == VIDEO.getType() || contentType == VIDEO_SELF.getType() || contentType == VIDEO_MASSHELPER.getType() || contentType == VIDEO_MASSHELPER__22.getType()) {//群发助手的视频信息
                VideoMessage videoMsg = VideoParseUtil.parseVideMessage(bs);
                msgEntity.videoMsgEntity = videoMsg;
                MyLog.debug(TAG, "[parseVideMessage]" + " videoMsg->" + videoMsg + "bs: " + bs.toString());
            } else if (contentType == MINI_APP.getType()) {      //小程序
                WeAppMsgEntity weAppEntity = WeAppParseUtil.parseData(bs);
                msgEntity.weAppMsgEntity = weAppEntity;
                content = "";
                MyLog.debug(TAG, "[parseWebAppMsgEntity]" + " weAppEntity->" + weAppEntity);
            } else if (contentType == 16) { //voice
                FileMsgEntity fMsgEntity = FileImgParseUtil.parseData(bs, null);
                msgEntity.fileMsgEntity = fMsgEntity;
                MyLog.debug(TAG, "[parseMsgEntity]" + " fileMsgEntity->" + fMsgEntity);
            } else if (contentType == LINK.getType()) { //卡片连接
                LinkMsgEntity linkMsgEntity = LinkMsgParseUtil.parseData(bs);
                msgEntity.linkMsgEntity = linkMsgEntity;
                MyLog.debug(TAG, "[parseMsgEntity]" + " linkMsgEntity->" + linkMsgEntity);
//            } else if (contentType > 1000 && contentType <= MsgHandleEnum.GREY.getType()) {
//                String str = StrUtils.byteToUTFStr(bs);
//                content = str;
//                MyLog.debug(TAG, "[parseMsgEntity]" + " 系统消息->" + str);
//            } else if (contentType == MsgHandleEnum.GROUP_OWER_CHANGE.getType()) {
//                MyLog.debug(TAG, "[parseMsgEntity]" + " bs " + Arrays.toString(bs));
//                msgEntity.roomTipsList = RoomTipsParseUtil.parseData(bs);
//                MyLog.debug(TAG, "[parseMsgEntity]" + " RoomTips->" + JSON.toJSONString(msgEntity.roomTipsList));
//            } else if (contentType == MsgHandleEnum.CUSTOM_WX_IMAGE.getType()) {
//                MyLog.debug(TAG, "[parseMsgEntity]" + " 动图不解析...");
//
//            } else if (contentType == MsgHandleEnum.LOCATION.getType()) {
//                MyLog.debug(TAG, "[parseMsgEntity]" + "位置信息不解析");
//                content = "[定位消息，暂不支持查看！]";
//            } else if (contentType == MsgHandleEnum.MERGE_FORWARD.getType()) {
//                MyLog.debug(TAG, "[parseMsgEntity]" + "合并转发...");
//                content = "[合并转发消息，暂不支持查看！]";
            } else {
                if (contentType == 0 || contentType == 2) {
                    List<SimpleMsgEntity> sList = StrUtils.ByteToString(bs, contentType);
                    content = StrUtils.getContentTextInfo(sList);
                    List<MsgEntity.AtUserEntity> atUsers = null;
                    if (sList != null && sList.size() > 0) {
                        MyLog.debug(TAG, "[parseMsgEntity]" + " atUserSize:" + sList.size());
                        for (SimpleMsgEntity simpleMsgEntity : sList) {
                            if (simpleMsgEntity.atMsg != null) {
                                SimpleMsgEntity.SimpleAtMsgEntity simpleAtMsgEntity = simpleMsgEntity.atMsg;
                                if (atUsers == null) {
                                    atUsers = new ArrayList<>();
                                }
                                MsgEntity.AtUserEntity atUserEntity = new MsgEntity.AtUserEntity();
                                atUserEntity.nickName = StrUtils.byteToUTFStr(simpleAtMsgEntity.name);
                                atUserEntity.remoteId = simpleAtMsgEntity.uin;
                                atUsers.add(atUserEntity);
                                MyLog.debug(TAG, "[parseMsgEntity]" + " nickName:" + atUserEntity.nickName + " remoteId:" + atUserEntity.remoteId);
                            }
                        }
                    }
                    msgEntity.atUsers = atUsers;
                }
            }
            //
            if (content == null) {
                content = "";
            }
//            Class clazzStrUtil = RobotHelpers.findClassIfExists("djz", Global.loadPackageParam.classLoader);
//            Class[] clazzArray = {byte[].class,boolean.class};
//            Object[] objsDesc = {bs,false};
//            String strInfo = (String) RobotHelpers.callStaticMethod(clazzStrUtil,"e",clazzArray,objsDesc);
            MyLog.debug(TAG, "[parseMsgEntity]" + " rContent:" + content);
        }
        if (appinfo != null && appinfo.length > 0) {
            String strAppInfo = StrUtils.byteToUTFStr(appinfo);
            msgEntity.appinfo = strAppInfo;
        }
        String json = JSON.toJSONString(msg);
        JSONObject jsonObject = JSON.parseObject(json);

        int sendScene = jsonObject.getIntValue("sendScene");
        int outContact = jsonObject.getIntValue("outContact");
        long seq = jsonObject.getLongValue("seq");
        long devinfo = jsonObject.getLongValue("devinfo");
        long sendTime = jsonObject.getLongValue("sendTime");
        long id = jsonObject.getLongValue("id");
        int convType = jsonObject.getIntValue("convType");
        long conversationId = jsonObject.getLongValue("conversationId");
        int flag = jsonObject.getIntValue("flag");
        long referid = jsonObject.getLongValue("referid");
        long remoteId = jsonObject.getLongValue("remoteId");
        long sender = jsonObject.getLongValue("sender");
        int state = jsonObject.getIntValue("state");
        String url = jsonObject.getString("url");
        JSONObject objExtra = jsonObject.getJSONObject("extras");
        if (objExtra != null) {
            MsgEntity.MessageExtras msgExtras = new MsgEntity.MessageExtras();
            long receiver = objExtra.getLongValue("receiver");
            String sendername = objExtra.getString("sendername");
            boolean canNotBeLastmessage = objExtra.getBooleanValue("canNotBeLastmessage");
            int decryptRet = objExtra.getIntValue("decryptRet");
            boolean disableDataDetector = objExtra.getBooleanValue("disableDataDetector");
            boolean firstSentMessageThatDay = objExtra.getBooleanValue("firstSentMessageThatDay");
            boolean isAlertReachedReaded = objExtra.getBooleanValue("isAlertReachedReaded");
            boolean isSvrFail = objExtra.getBooleanValue("isSvrFail");
            int orderTime = objExtra.getIntValue("orderTime");
            //  byte[] openapiAssociateKey = (byte[]) RobotHelpers.getObjectField(objExtra,"openapiAssociateKey");
            int revokeTime = objExtra.getIntValue("revokeTime");
            //    long[] readuins = (long[]) RobotHelpers.getObjectField(objExtra,"readuins");
            boolean receiptModeEntry = objExtra.getBooleanValue("receiptModeEntry");
            boolean showTranslation = objExtra.getBooleanValue("showTranslation");
            long staffVid = objExtra.getLongValue("staffVid");
            long topMsgCreatorId = objExtra.getLongValue("topMsgCreatorId");
            //  byte[] translation = (byte[]) RobotHelpers.getObjectField(objExtra,"translation");
            // byte[] translationProvider = (byte[]) RobotHelpers.getObjectField(objExtra,"translationProvider");
            //  long[] unreaduins = (long[]) RobotHelpers.getObjectField(objExtra,"unreaduins");

            msgExtras.receiver = receiver;
            msgExtras.sendername = sendername;
            msgExtras.canNotBeLastmessage = canNotBeLastmessage;
            msgExtras.decryptRet = decryptRet;
            msgExtras.disableDataDetector = disableDataDetector;
            msgExtras.firstSentMessageThatDay = firstSentMessageThatDay;
            msgExtras.isAlertReachedReaded = isAlertReachedReaded;
            msgExtras.isSvrFail = isSvrFail;
            msgExtras.orderTime = orderTime * 1000;
            // msgExtras.openapiAssociateKey = openapiAssociateKey;
            msgExtras.revokeTime = revokeTime * 1000;
            // msgExtras.readuins = readuins;
            msgExtras.receiptModeEntry = receiptModeEntry;
            msgExtras.showTranslation = showTranslation;
            msgExtras.staffVid = staffVid;
            msgExtras.topMsgCreatorId = topMsgCreatorId;
            // msgExtras.translation = translation;
            //  msgExtras.translationProvider = translationProvider;
            // msgExtras.unreaduins = unreaduins;

            msgEntity.extras = msgExtras;
        }

        msgEntity.sendScene = sendScene;
        msgEntity.outContact = outContact;
        msgEntity.seq = seq;
        msgEntity.devinfo = devinfo;
        msgEntity.ackSendState = ackSendState;
        msgEntity.asId = asId;
        msgEntity.content = content;
        msgEntity.id = id;
        msgEntity.msgId = id;
        msgEntity.contentType = contentType;
        msgEntity.convType = convType;
        msgEntity.conversationId = conversationId;
        msgEntity.flag = flag;
        msgEntity.referid = referid;
        msgEntity.remoteId = remoteId;
        msgEntity.sender = sender;
        msgEntity.state = state;
        msgEntity.url = url;
        msgEntity.senderTime = sendTime;
        return msgEntity;
    }

//    public static String parseMsgEntityContent(int contentType, byte[] bs) {
//
//        if (bs != null && bs.length > 0) {
//            //101:image download; 102:file download
//            if (MsgHandleEnum.needParseFile(contentType)) {
//                FileMsgEntity fMsgEntity = FileImgParseUtil.parseData(bs, null);
//                return StrUtils.objectToJson(fMsgEntity);
//
//            } else if (contentType == VIDEO.getType() || contentType == VIDEO_SELF.getType() || contentType == VIDEO_MASSHELPER.getType() || contentType == VIDEO_MASSHELPER__22.getType()) {//群发助手的视频信息
//                VideoMessage videoMsg = VideoParseUtil.parseVideMessage(bs);
//                return StrUtils.objectToJson(videoMsg);
//            } else if (contentType == MINI_APP.getType()) {      //小程序
//                WeAppMsgEntity weAppEntity = WeAppParseUtil.parseData(bs);
//                return StrUtils.objectToJson(weAppEntity);
//
//            } else if (contentType == 16) { //voice
//                FileMsgEntity fMsgEntity = FileImgParseUtil.parseData(bs, null);
//                return StrUtils.objectToJson(fMsgEntity);
//            } else if (contentType == LINK.getType()) { //卡片连接
//                LinkMsgEntity linkMsgEntity = LinkMsgParseUtil.parseData(bs);
//                return StrUtils.objectToJson(linkMsgEntity);
//            } else if (contentType == MsgHandleEnum.GREY.getType()) {
//                String str = StrUtils.byteToUTFStr(bs);
//                return str;
//            }
//        }
//        //
//        return "";
//    }
    /**
     *         public boolean canNotBeLastmessage;
     *         public int decryptRet;
     *         public boolean disableDataDetector;
     *         public boolean firstSentMessageThatDay;
     *         public boolean isAlertReachedReaded;
     *         public boolean isSvrFail;
     *         public byte[] openapiAssociateKey;
     *         public long openapiAssociateType;
     *         public int orderTime;
     *         public long[] readuins;
     *         public boolean receiptModeEntry;
     *         public long receiver;
     *         public int revokeTime;
     *         public long sendErrorCode;
     *         public String sendername;
     *         public boolean showTranslation;
     *         public long staffVid;
     *         public long topMsgCreatorId;
     *         public byte[] translation;
     *         public byte[] translationProvider;
     *         public long[] unreaduins;
     */


    /**
     * 解析string
     *
     * @param bs
     * @return
     */
    public static final String parseTxtString(byte[] bs) {
        String str = "";
        str = StrUtils.byteToUTFStr(bs);
        return str;
    }

    //
    public static final String trimTxtString(String strInfo) {
        String result = null;
        if (strInfo != null && !TextUtils.isEmpty(strInfo)) {
            result = strInfo.replaceAll("\\f", "");
            result = result.replaceAll("\\n", "");
            if (result.length() > 1) {
                int c = result.charAt(0);
                if (c < 31) {
                    StringBuilder builder = new StringBuilder(result);
                    builder = builder.deleteCharAt(0);
                    result = builder.toString();
                }
            }
        }
        return result;
    }


    public static final WelcomeLinkeMsgEntity parseLinkMsgEntity(Object obj) {
        WelcomeLinkeMsgEntity rEntity = null;
        if (obj != null) {
            MyLog.debug(TAG, "[parseLinkMsgEntity]" + " linkMsg:" + obj);
            rEntity = new WelcomeLinkeMsgEntity();
            rEntity.cmd = RobotHelpers.getIntField(obj, KeyConst.F_LinkMessage_cmd);
            byte[] bDesc = (byte[]) RobotHelpers.getObjectField(obj, KeyConst.F_LinkMessage_description);
            if (bDesc != null && bDesc.length > 0) {
                rEntity.desc = StrUtils.byteToUTFStr(bDesc);
            }
            byte[] bImgUrl = (byte[]) RobotHelpers.getObjectField(obj, KeyConst.F_LinkMessage_imageUrl);
            if (bImgUrl != null && bImgUrl.length > 0) {
                rEntity.imgUrl = StrUtils.byteToUTFStr(bImgUrl);
            }
            byte[] bLinkUrl = (byte[]) RobotHelpers.getObjectField(obj, KeyConst.F_LinkMessage_linkUrl);
            if (bLinkUrl != null && bLinkUrl.length > 0) {
                rEntity.linkUrl = StrUtils.byteToUTFStr(bLinkUrl);
            }
            byte[] bTitle = (byte[]) RobotHelpers.getObjectField(obj, KeyConst.F_LinkMessage_title);
            if (bTitle != null && bTitle.length > 0) {
                rEntity.title = StrUtils.byteToUTFStr(bTitle);
            }
            MyLog.debug(TAG, "[parseLinkMsgEntity]" + " linkInfo:" + rEntity);
        }
        return rEntity;
    }
}
