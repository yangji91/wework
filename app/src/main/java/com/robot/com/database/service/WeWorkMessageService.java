package com.robot.com.database.service;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.robot.common.Global;
import com.robot.controller.ConvController;
import com.robot.controller.message.MessageController;
import com.robot.entity.FileMsgEntity;
import com.robot.entity.LinkMsgEntity;
import com.robot.entity.MsgEntity;
import com.robot.entity.VideoMessage;
import com.robot.entity.WeAppMsgEntity;
import com.robot.hook.msg.ContentType;
import com.robot.hook.msg.HookMsgRevMethod;
import com.robot.com.database.AppDatabase;
import com.robot.com.database.dao.WeWorkMessageDao;
import com.robot.com.database.entity.RobotRunningInfo;
import com.robot.com.database.entity.WeWorkFileInfo;
import com.robot.com.database.entity.WeWorkMessage;
import com.robot.util.MsgParseUtil;
import com.robot.util.MyLog;
import com.robot.util.StrUtils;

import java.util.ArrayList;
import java.util.List;


/***
 *@author 
 *@date 2021/8/27
 *@description
 ****/
public class WeWorkMessageService extends BaseService<Object> {

    private WeWorkMessageDao dao;

    private static WeWorkMessageService instance;
    private String TAG = "WeWorkMessageService";
    public ArrayList<Long> mHasAddMessageIdList = new ArrayList<>();

    public static WeWorkMessageService getInstance() {
        if (instance == null) {
            instance = new WeWorkMessageService();
        }
        return instance;
    }

    private WeWorkMessageService() {
        AppDatabase.WeWorkMessageAppDatabase weWorkMessageAppDatabase = AppDatabase.getWeWorkMessageAppDatabase();
        if (weWorkMessageAppDatabase != null) {
            this.dao = weWorkMessageAppDatabase.getWeWorkMessageDao();
        }
    }


    @Override
    public void saveOnce(Object msgObject) {
        MsgEntity msgEntity = MsgParseUtil.parseMsgEntity(msgObject);
        if (msgEntity != null) {
            WeWorkMessage weWorkMessage = new WeWorkMessage();
            weWorkMessage.LID = msgEntity.msgId;
            weWorkMessage.contentType = msgEntity.contentType;
            weWorkMessage.convId = msgEntity.conversationId;
            weWorkMessage.senderId = msgEntity.sender;
            weWorkMessage.sendTime = msgEntity.senderTime;
            weWorkMessage.flag = msgEntity.flag;
            weWorkMessage.isUpload = 0;
            weWorkMessage.atUser = StrUtils.objectToJson(msgEntity.atUsers);
            weWorkMessage.devinfo = msgEntity.mNativeHandle;
            weWorkMessage.extras = StrUtils.objectToJson(msgEntity.extras);
            weWorkMessage.content = msgEntity.content;
            weWorkMessage.url = msgEntity.appinfo;
            weWorkMessage.appInfo = msgEntity.appinfo;
            if (msgEntity.fileMsgEntity != null) {
                weWorkMessage.extraContent = StrUtils.objectToJson(msgEntity.fileMsgEntity);
                saveFileMessage(weWorkMessage.LID, StrUtils.byteToUTFStr(msgEntity.fileMsgEntity.url),
                        StrUtils.byteToUTFStr(msgEntity.fileMsgEntity.md5),
                        StrUtils.byteToUTFStr(msgEntity.fileMsgEntity.fileId),
                        StrUtils.byteToUTFStr(msgEntity.fileMsgEntity.aesKey),
                        StrUtils.byteToUTFStr(msgEntity.fileMsgEntity.wechatAuthKey),
                        StrUtils.byteToUTFStr(msgEntity.fileMsgEntity.fileName),
                        msgEntity.fileMsgEntity.size, 0
                );
            }
            if (msgEntity.videoMsgEntity != null) {
                weWorkMessage.extraContent = StrUtils.objectToJson(msgEntity.videoMsgEntity);
                saveFileMessage(weWorkMessage.LID, msgEntity.videoMsgEntity.url, msgEntity.videoMsgEntity.md5,
                        msgEntity.videoMsgEntity.videoId, msgEntity.videoMsgEntity.aesKey,
                        msgEntity.videoMsgEntity.wechatAuthKey, "视频.mp4", msgEntity.videoMsgEntity.size, msgEntity.videoMsgEntity.videoDuration);
            }
            if (msgEntity.weAppMsgEntity != null) {
                weWorkMessage.extraContent = StrUtils.objectToJson(msgEntity.weAppMsgEntity);
                if (msgEntity.weAppMsgEntity.wechatThumb.imgUri != null && msgEntity.weAppMsgEntity.wechatThumb.imgUri.url.length > 0) {
                    saveFileMessage(weWorkMessage.LID,
                            StrUtils.byteToUTFStr(msgEntity.weAppMsgEntity.wechatThumb.imgUri.url),
                            StrUtils.byteToUTFStr(msgEntity.weAppMsgEntity.wechatThumb.imgUri.md5),
                            msgEntity.weAppMsgEntity.thumbFileId,
                            StrUtils.byteToUTFStr(msgEntity.weAppMsgEntity.wechatThumb.imgUri.aeskey),
                            StrUtils.byteToUTFStr(msgEntity.weAppMsgEntity.wechatThumb.imgUri.authkey),
                            "小程序图片.jpg",
                            msgEntity.weAppMsgEntity.wechatThumb.imgUri.size, 0
                    );
                } else {
                    saveFileMessage(weWorkMessage.LID,
                            msgEntity.weAppMsgEntity.thumbUrl,
                            msgEntity.weAppMsgEntity.thumbMD5,
                            msgEntity.weAppMsgEntity.thumbFileId,
                            msgEntity.weAppMsgEntity.thumbAESKey,
                            "",
                            "小程序图片.jpg",
                            msgEntity.weAppMsgEntity.thumbSize, 0
                    );
                }
            }
            if (msgEntity.linkMsgEntity != null) {
                weWorkMessage.extraContent = StrUtils.objectToJson(msgEntity.linkMsgEntity);
            }
            MyLog.debug(TAG, "saveOnce  messageId=" + weWorkMessage.LID + " content=" + weWorkMessage.content + " weWorkMessage " + weWorkMessage);
            insertOrUpdate(weWorkMessage);
        }
    }
    public boolean hasSameMsgById(Long msgId) {
        if (mHasAddMessageIdList.contains(msgId)) {
            return true;
        } else {
//            WeWorkMessage localMessage = WeWorkMessageService.getInstance().getLocalMessage(msgEntity.msgId);
//            if (localMessage != null && localMessage.isUpload == 1) {
//                return true;
//            }
        }
        return false;
    }

    private void insertOrUpdate(WeWorkMessage item) {
        WeWorkMessage weWorkMessage = dao.selectByLID(item.LID);

        if (weWorkMessage == null)
            dao.insert(item);
        else {
            item.isUpload = weWorkMessage.isUpload;
            item.uploadTime = weWorkMessage.uploadTime;
            item.upContent = weWorkMessage.upContent;
            dao.update(item);
        }
    }

    public void updateMessageComplete(long messageId, String upContent, String from) {
        runOnThread(new Runnable() {
            @Override
            public void run() {
                boolean insert = false;
                WeWorkMessage weWorkMessage = dao.selectByLID(messageId);
                if (weWorkMessage == null) {
                    weWorkMessage = new WeWorkMessage();
                    insert = true;
                }
                weWorkMessage.LID = messageId;
                weWorkMessage.isUpload = 1;
                weWorkMessage.upContent = upContent;
                weWorkMessage.uploadTime = System.currentTimeMillis();
                if (insert) {
                    dao.insert(weWorkMessage);
                } else {
                    dao.update(weWorkMessage);
                }
            }
        });
    }

    public void updateMessageUid(List<Long> messageIdList, String uid) {
        runOnThread(new Runnable() {
            @Override
            public void run() {
                dao.updateUid(uid, messageIdList);
            }
        });


    }

    /**
     * 保存文件信息
     *
     * @param messageId
     * @param url
     * @param md5
     * @param fileId
     * @param aesKey
     * @param authKey
     * @param fileName
     * @param size
     */
    private void saveFileMessage(long messageId, String url, String md5, String fileId, String aesKey, String authKey, String fileName, long size, int duration) {
        WeWorkFileInfo fileInfo = new WeWorkFileInfo();
        fileInfo.md5 = md5;
        fileInfo.messageId = messageId;
        fileInfo.aesKey = aesKey;
        fileInfo.authKey = authKey;
        fileInfo.createTime = System.currentTimeMillis();
        fileInfo.fileId = fileId;
        fileInfo.size = size;
        fileInfo.fileName = fileName;
        fileInfo.url = url;
        fileInfo.duration = duration;
        MyLog.debug(TAG, "saveFileMessage  md5=" + md5 + " url=" + url + " fileId " + fileId);
        WeWorkFileInfoService.getInstance().saveOnce(fileInfo);
    }


    public void checkLocalMessage(long startTime, long endTime) {
        runOnThread(new Runnable() {
            @Override
            public void run() {
                List<WeWorkMessage> weWorkMessages = dao.selectByTimeBetween(startTime, endTime);
                MyLog.debug(TAG, " weWorkMessages.size = " + weWorkMessages.size());
                for (WeWorkMessage message : weWorkMessages) {
                    // long devinfo = message.devinfo;
                    try {
                        //   Object object = MessageController.getMessageObjectByNativeHandle(Global.loadPackageParam.classLoader, devinfo);
                        MsgEntity msgEntity = parseMsgEntity(message);
                        new HookMsgRevMethod().handleMsg(msgEntity, Global.loadPackageParam);
                        MyLog.debug(TAG, "msgEntity = " + JSON.toJSONString(msgEntity));
                    } catch (Exception e) {
                    }
                }
            }
        });

    }

    private MsgEntity parseMsgEntity(WeWorkMessage weWorkMessage) {
        MsgEntity msgEntity = new MsgEntity();
        msgEntity.msgId = weWorkMessage.LID;
        msgEntity.id = msgEntity.msgId;
        msgEntity.contentType = weWorkMessage.contentType;
        msgEntity.conversationId = weWorkMessage.convId;
        msgEntity.sender = weWorkMessage.senderId;
        msgEntity.senderTime = weWorkMessage.sendTime;
        msgEntity.flag = weWorkMessage.flag;
        if (TextUtils.isEmpty(weWorkMessage.appInfo)) {
            msgEntity.appinfo = "from_msgid_" + (weWorkMessage.LID + weWorkMessage.convId);
        } else {
            msgEntity.appinfo = weWorkMessage.appInfo;
        }

        msgEntity.convType = (weWorkMessage.convId + "").startsWith("106") ? 1 : 0;
        if (!TextUtils.isEmpty(weWorkMessage.atUser)) {
            msgEntity.atUsers = JSON.parseObject(weWorkMessage.atUser, new TypeReference<ArrayList<MsgEntity.AtUserEntity>>() {
            });
        }
        msgEntity.mNativeHandle = weWorkMessage.devinfo;
        if (!TextUtils.isEmpty(weWorkMessage.extras)) {
            msgEntity.extras = JSON.parseObject(weWorkMessage.extras, MsgEntity.MessageExtras.class);
            msgEntity.sendername = msgEntity.extras.sendername;
            msgEntity.receiver = msgEntity.extras.receiver;
        }
        msgEntity.content = weWorkMessage.content;
        if (!TextUtils.isEmpty(weWorkMessage.extraContent)) {
            if (ContentType.isFileMessage(weWorkMessage.contentType)) {
                msgEntity.fileMsgEntity = JSON.parseObject(weWorkMessage.extraContent, FileMsgEntity.class);
            } else if (ContentType.isVideoMessage(weWorkMessage.contentType)) {
                msgEntity.videoMsgEntity = JSON.parseObject(weWorkMessage.extraContent, VideoMessage.class);
            } else if (ContentType.isLinkMessage(weWorkMessage.contentType)) {
                msgEntity.linkMsgEntity = JSON.parseObject(weWorkMessage.extraContent, LinkMsgEntity.class);
            } else if (ContentType.isMiniAppMessage(weWorkMessage.contentType)) {
                msgEntity.weAppMsgEntity = JSON.parseObject(weWorkMessage.extraContent, WeAppMsgEntity.class);
            }
        }
        return msgEntity;

    }

    public void checkMessageFromWeWorkDB() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RobotRunningInfo info = RobotRunningInfoService.getInstance().getRobotMessageCheckEvent();
                MessageController.getMessageFromDBByBetweenTime(info.updateTime, System.currentTimeMillis() - 50 * 1000, new MessageController.GetHistoryMessageCallback() {
                    @Override
                    public void onResult(int code, MsgEntity[] message) {
                        ArrayList<String> missList = new ArrayList();

                        RobotRunningInfoService.getInstance().addCheckMessageEvent(info);
                        for (MsgEntity msgEntity : message) {
                            WeWorkMessage localMessage = getLocalMessage(msgEntity.msgId);
                            if ((localMessage == null || localMessage.isUpload == 0) && !TextUtils.isEmpty(msgEntity.appinfo)) {
                                missList.add(msgEntity.appinfo);
                            }
                        }
                        if (missList.size() > 0) {
                            MyLog.debug(TAG, " 查询到遗漏上报消息有" + missList.size() + " 个 ");
                            HookMsgRevMethod hookMsgRevMethod = new HookMsgRevMethod();
                            for (int i = 0; i < missList.size(); i++) {
                                String appInfo = missList.get(i);
                                MyLog.debug(TAG, " 查询appinfo   = " + appInfo);
                                ConvController.getInstance().getMessageByAppId(Global.loadPackageParam.classLoader, appInfo, 0, 0, new ConvController.MessageCallback() {
                                    @Override
                                    public void onResult(int code, Object message) {
                                        MyLog.debug(TAG, " 获取消息成功！code =" + code + " appInfo" + appInfo);
                                        if (code == 0 && message != null) {
                                            hookMsgRevMethod.onAddMessage(new Object[]{message}, Global.loadPackageParam);
                                        }
                                    }
                                });
                            }

                        } else {
                            MyLog.debug(TAG, " 没有查询到遗漏上报消息 ");
                        }

                    }
                });

            }
        }).start();
    }


    public WeWorkMessage getLocalMessage(long msgId) {
        WeWorkMessage weWorkMessage = dao.selectByLID(msgId);
        return weWorkMessage;
    }
}
