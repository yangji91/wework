package com.robot.netty.handle.imple.runnable;

import android.os.SystemClock;

import com.alibaba.fastjson.JSON;
import com.robot.com.database.service.WeWorkMessageService;
import com.robot.common.Global;
import com.robot.controller.message.MessageController;
import com.robot.controller.message.WeWorkMessageUtil;
import com.robot.controller.resource.ResourceController;
import com.robot.entity.ActionResultEnum;
import com.robot.entity.ActionStatusEnum;
import com.robot.entity.MsgEntity;
import com.robot.entity.SendHelperSendMsgEntity;
import com.robot.hook.KeyConst;
import com.robot.netty.ProtocalManager;
import com.robot.netty.entity.rsp.PRspActionTextMsgEntity;
import com.robot.util.MsgParseUtil;
import com.robot.util.MyLog;
import com.robot.util.ProxyUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 旧的私聊消息处理中间层
 */
public class MessageRunnable implements Runnable {

    public String TAG = "MessageRunnable";
    private PRspActionTextMsgEntity.PContactMsgActionItem data;
    private List<Long> sendingMessageId = new LinkedList<>();
    private List<Object> msgList = new LinkedList<>();
    private Map<Long, ActionResultEnum.PActionResultItem> sendingMessage = new HashMap<>();
    private long remoteId;
    private long startTime, endTime;
    private Object conversationObject;

    public MessageRunnable(PRspActionTextMsgEntity.PContactMsgActionItem datas, Object conversationObject) {
        this.data = datas;
        this.conversationObject = conversationObject;
        this.remoteId = datas.matchers.get(0).remoteId;
    }

    @Override
    public void run() {
        msgList = downloadResouceAndTransfor(data.messageDetails);
        if (msgList.size() == 0) {
            //todo 转化出错的情况
            ProtocalManager.getInstance().sendMsgReportCallbackErr(data.executorUin, data.uid, ActionStatusEnum.FAILURE, "消息转化失败", sendingMessage.values(), data.actionType, data.actionSubType);
            return;
        }
        startTime = System.currentTimeMillis() / 1000;
        MyLog.debug(TAG, "[onHandle]" + " 消息 发送 任务: uid=" + data.uid + " 消息个数 " + msgList.size(), true);
        for (int i = 0; i < msgList.size(); i++) {
            Object item = msgList.get(i);
            sendMessage(item);

            if (item instanceof File) {
                SystemClock.sleep(2000);
            }
            SystemClock.sleep(1500);
        }
        endTime = System.currentTimeMillis() / 1000 + 1;
        refreshMessage();
        int count = 0;
        SystemClock.sleep(200);
        while (count <= 10) {
            count++;
            if (checkMessageStatus()) {//全部成功
                uploadTaskResult();
                return;
            } else {
                refreshMessage();
            }
            SystemClock.sleep(2000);
        }
        uploadTaskResult();

    }


    //发送消息
    public void sendMessage(Object item) {
        Global.postRunnable2UI(() -> {
            if (item instanceof File) {
                WeWorkMessageUtil.sendFileMessage(Global.loadPackageParam.classLoader, conversationObject, ((File) item).getAbsolutePath());
            } else
                //发送消息
                WeWorkMessageUtil.sendMessage(Global.loadPackageParam.classLoader, conversationObject, item, ProxyUtil.GetProxyInstance(KeyConst.I_ISendMessageCallback, sendMessageCallback));
        });
    }

    private void uploadTaskResult() {
        ActionStatusEnum statusEnum = null;
        String uid = data.uid;
        long uin = data.executorUin;
        String actionInfo = "执行成功";
        if (sendingMessage.size() == 0) {
            statusEnum = ActionStatusEnum.SUCC;
            MyLog.debug(TAG, " uid:" + uid + "全部成功...IDs" + JSON.toJSONString(sendingMessageId), true);
        } else if (sendingMessage.size() == sendingMessageId.size()) {
            statusEnum = ActionStatusEnum.FAILURE;
            actionInfo = ActionResultEnum.ACTION_TIME_OUT.getMsg("消息发送全部失败");
            MyLog.debug(TAG, " uid:" + uid + "全部失败...IDs" + JSON.toJSONString(sendingMessage.keySet()), true);
        } else {
            statusEnum = ActionStatusEnum.SUCC_PART;
            actionInfo = ActionResultEnum.ACTION_TIME_OUT.getMsg("消息发送部分成功");
            MyLog.debug(TAG, "[handleItemByDg]" + "失败 uin:" + uin + " uid:" + uid + "共" + sendingMessageId.size() + "   失败 个数 " + sendingMessage.size() + " 失败的IDs" + JSON.toJSONString(sendingMessage.keySet()) + " 全部的IDs" + JSON.toJSONString(sendingMessageId), true);
        }
        ProtocalManager.getInstance().sendMsgReportCallbackErr(uin, uid, statusEnum, actionInfo, sendingMessage.values(), data.actionType, data.actionSubType);
        WeWorkMessageService.getInstance().updateMessageUid(sendingMessageId, uid);

    }

    /**
     * 刷新消息状态
     */
    public void refreshMessage() {
        Global.postRunnable2UI(new Runnable() {
            @Override
            public void run() {
                MessageController.getMessageFromDBByIdBetweenTime(remoteId, startTime, endTime, new MessageController.GetHistoryMessageCallback() {
                    @Override
                    public void onResult(int code, MsgEntity[] message) {
                        if (code == 0) {
                            if (message.length > 0) {
                                for (MsgEntity msgEntity : message) {
                                    if (!sendingMessageId.contains(msgEntity.msgId)) {
                                        sendingMessageId.add(msgEntity.msgId);
                                    }
                                    if (msgEntity.flag == 16777216 || msgEntity.flag == 83886080) {
                                        sendingMessage.remove(msgEntity.msgId);
                                        MyLog.debug(TAG, "消息 发送成功 id=" + msgEntity.id + " type = " + msgEntity.contentType + "content =" + msgEntity.asId);
                                    } else if (msgEntity.flag == 20971520 || msgEntity.flag == 36) {//被拉黑 被删除
                                        sendingMessage.remove(msgEntity.msgId);
                                        MyLog.debug(TAG, "消息 发送成功 但是被删除或者拉黑了 id=" + msgEntity.id + " type = " + msgEntity.contentType + "content =" + msgEntity.asId);
                                    } else if (msgEntity.flag == 83951616 || msgEntity.flag == 16842752) {//群公告成功
                                        sendingMessage.remove(msgEntity.msgId);
                                        MyLog.debug(TAG, "消息 发送成功 群公告 id=" + msgEntity.id + " type = " + msgEntity.contentType + "content =" + msgEntity.asId);
                                    } else if (msgEntity.flag == 4) {
                                        if (!sendingMessage.containsKey(msgEntity.msgId))
                                            sendingMessage.put(msgEntity.msgId, ActionResultEnum.ACTION_TIME_OUT.getActionResultItemEntity());
                                        MyLog.debug(TAG, "消息 发送中 id= " + msgEntity.id + " type = " + msgEntity.contentType + "content =" + msgEntity.asId);
                                    } else {
                                        ActionResultEnum.PActionResultItem rEntity = getError(msgEntity.contentType);
                                        rEntity.desc = "发送消息失败 id=" + msgEntity.id + " flag=" + msgEntity.flag;
                                        sendingMessage.put(msgEntity.msgId, rEntity);
                                        MyLog.debug(TAG, "消息 失败 id=" + msgEntity.id + " flag=" + msgEntity.flag + " type = " + msgEntity.contentType + "content =" + msgEntity.asId, true);
                                    }
                                }
                                MyLog.debug(TAG, "消息 共 " + sendingMessageId.size() + " 个 ，发送中 " + sendingMessage.size() + "个 ");
                            }
                            if (message.length < msgList.size()) {
                                endTime += 1;//消息数量不对 增大时间
                            }
                        }
                    }
                });
            }
        });

    }

    private boolean checkMessageStatus() {
        if (sendingMessage.size() == 0) {
            return true;
        }

        return false;

    }

    private ActionResultEnum.PActionResultItem getError(int contentType) {
        switch (contentType) {
            case 0:
                return ActionResultEnum.ACTION_SEND_TXT_METHOD.getActionResultItemEntity();
            case 7:
            case 14:
                return ActionResultEnum.ACTION_SEND_IMG_METHOD.getActionResultItemEntity();
            case 5:
            case 23:
            case 22:
                return ActionResultEnum.ACTION_SEND_VIDEO_METHOD.getActionResultItemEntity();
            case 13:
                return ActionResultEnum.ACTION_SEND_LINK_METHOD.getActionResultItemEntity();
            case 15:
            case 8:
                return ActionResultEnum.ACTION_SEND_FILE_METHOD.getActionResultItemEntity();
            case 78:
                return ActionResultEnum.ACTION_SEND_MIMI_PROGREM.getActionResultItemEntity();
        }
        return new ActionResultEnum.PActionResultItem();
    }

    /**
     * 下载资源
     *
     * @param datas
     */
    private List<Object> downloadResouceAndTransfor(List<PRspActionTextMsgEntity.PMsgDetailEntity> datas) {
        List<Object> list = new ArrayList<>();

        for (int i = 0; i < datas.size(); i++) {

            try {

                PRspActionTextMsgEntity.PMsgDetailEntity pMsgDetailEntity = datas.get(i);
                switch (pMsgDetailEntity.contentType) {
                    case 0://TextMSG
                        String content = "";
                        content = pMsgDetailEntity.content;
                        list.add(WeWorkMessageUtil.buildTexMessage(Global.loadPackageParam.classLoader, content));
                        break;
                    case 7://图片
                        content = new SendHelperSendMsgEntity.SendHelperSendImgEntity(pMsgDetailEntity.content).localPath;
                        list.add(WeWorkMessageUtil.buildImageMessage(Global.loadPackageParam.classLoader, content));
                        break;
                    case 5://视频
                        content = new SendHelperSendMsgEntity.SendHelperSendVideoEntity(pMsgDetailEntity.content).localPath;
                        list.add(WeWorkMessageUtil.buildVideoMessage(Global.loadPackageParam.classLoader, content, 5));
                        break;
                    case 13://H5
                        //  content=new SendHelperSendMsgEntity.SendHelperSendPageEntity(pMsgDetailEntity.h5Msg).imageUrl;
                        list.add(WeWorkMessageUtil.buildLinkMessage(Global.loadPackageParam.classLoader,
                                pMsgDetailEntity.h5Msg.linkUrl, pMsgDetailEntity.h5Msg.title, pMsgDetailEntity.h5Msg.description, pMsgDetailEntity.h5Msg.imageUrl));
                        break;
                    case 8://文件
                        String name = pMsgDetailEntity.fileTitle + "." + pMsgDetailEntity.fileType;
                        content = ResourceController.saveIntNetFile(pMsgDetailEntity.content, name, ResourceController.MResource.FILE);
                        list.add(new File(content));
//                        list.add(WeWorkMessageUtil.buildSilkMessage(Global.loadPackageParam.classLoader,new File(content))) ;
                        break;
                    case 9://语音
                        content = ResourceController.saveIntNetFile(pMsgDetailEntity.voice.voiceUrl, ResourceController.MResource.VOICE);
                        list.add(WeWorkMessageUtil.buildVoiceMessage(Global.loadPackageParam.classLoader, content, pMsgDetailEntity.voice.seconds != 0 ? pMsgDetailEntity.voice.seconds : 1));
                        break;
                    case 78://小程序
                        content = new SendHelperSendMsgEntity.SendHelperSendLittleProgramEntity(pMsgDetailEntity.miniProgram).thumbUrl;
                        list.add(WeWorkMessageUtil.buildMiniProgremMessage(Global.loadPackageParam.classLoader, pMsgDetailEntity.miniProgram.appId, pMsgDetailEntity.miniProgram.username,
                                pMsgDetailEntity.miniProgram.pagePath, pMsgDetailEntity.miniProgram.iconUrl, pMsgDetailEntity.miniProgram.title, content, pMsgDetailEntity.miniProgram.appName, ""));
                        break;

                }

            } catch (Exception e) {
                MyLog.debug("MessageTesk", "转化信息出错 " + e.getMessage(), true);
                // throw e;
                PRspActionTextMsgEntity.PMsgDetailEntity pMsgDetailEntity = datas.get(i);
                ActionResultEnum.PActionResultItem rEntity = new ActionResultEnum.PActionResultItem();
                rEntity.code = 90013;
                rEntity.msg = "文件下载失败[" + pMsgDetailEntity.contentType + "," + pMsgDetailEntity.content + "]";
                rEntity.desc = e.getMessage();
                sendingMessage.put(Long.valueOf(i), rEntity);

            }


        }
        return list;
    }

    private MessageController.SendMessageCallBack sendMessageCallback = new MessageController.SendMessageCallBack() {
        @Override
        public void onResult(int code, Object conversation, Object message) {
            MsgEntity msgEntity = MsgParseUtil.parseMsgEntity(message);
            if (code == 0) {
                MyLog.debug(TAG, "回调的消息" + msgEntity);
                if (sendingMessage.containsKey(msgEntity.msgId)) {
                    sendingMessage.remove(msgEntity.msgId);
                }
            }
        }

        @Override
        public void onProgress(Object message, long j, long j2) {
            if (j > 0) {
                MsgEntity msgEntity = MsgParseUtil.parseMsgEntity(message);
                if (sendingMessage.containsKey(msgEntity.msgId)) {
                    sendingMessage.remove(msgEntity.msgId);
                }
            }
        }
    };
}