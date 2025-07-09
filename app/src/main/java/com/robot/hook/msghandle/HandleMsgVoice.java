package com.robot.hook.msghandle;

import com.robot.common.Global;
import com.robot.common.MConfiger;
import com.robot.controller.LoginController;
import com.robot.controller.OssController;
import com.robot.controller.message.MessageController;
import com.robot.controller.message.VoiceMessageService;
import com.robot.controller.message.VoiceMessageUtils;
import com.robot.controller.message.WeWorkMessageUtil;
import com.robot.entity.MsgEntity;
import com.robot.hook.msghandle.base.BaseHandleMsg;
import com.robot.netty.ProtocalManager;
import com.robot.util.MyLog;
import com.robot.util.ProxyUtil;
import com.robot.util.StrUtils;
import com.robot.robothook.LoadPackageParam;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理语音消息
 */
public class HandleMsgVoice implements BaseHandleMsg {
    private final String TAG = getClass().getSimpleName();

    private Map<MsgEntity, Integer> mMap = new HashMap<>();

    @Override
    public void onHandleMsg(LoadPackageParam loadPackageParam, MsgEntity msgEntity) {
        MyLog.debug(TAG, "[onHandleMsg]" + " voiceMsgEntity -> " + msgEntity.fileMsgEntity);
        loadVoiceFile(loadPackageParam, msgEntity);
    }

    private void loadVoiceFile(LoadPackageParam loadPackageParam, MsgEntity msgEntity) {
        if (msgEntity.contentType == MsgHandleEnum.VOICE.getType()) {
            Global.postRunnable2UIDelay(new Runnable() {
                @Override
                public void run() {
                    final VoiceMessageService voiceMessageService = new VoiceMessageService();
                    String filePath = VoiceMessageService.getNewVoiceFilePath(String.valueOf(msgEntity.extras.receiver), StrUtils.byteToUTFStr(msgEntity.fileMsgEntity.fileId));
                    String fileId = StrUtils.byteToUTFStr(msgEntity.fileMsgEntity.fileId);
                    String aesKey = StrUtils.byteToUTFStr(msgEntity.fileMsgEntity.aesKey);
                    String md5 = StrUtils.byteToUTFStr(msgEntity.fileMsgEntity.md5);
                    ProxyUtil.ProxyStringResultCallBack callBack = new ProxyUtil.ProxyStringResultCallBack() {
                        @Override
                        public void onResult(int i, String str) {
                            if (i == 0) {
                                voiceMessageService.autoTranslateVoiceText(loadPackageParam.classLoader, fileId, aesKey, new VoiceMessageService.IPickMessageCallback() {
                                    @Override
                                    public void onResult(int code, String transText) {
                                        MyLog.debug(TAG, "语音翻译结束 code " + code + "  message:" + transText);
                                        File amrFile = new File(str);
                                        if (code == 0) {
                                            msgEntity.sttStatus = 2L;
                                            msgEntity.sttText = transText;
                                        } else {
                                            msgEntity.sttStatus = 3L;
                                            msgEntity.sttText = transText + "[" + code + "]";
                                        }
                                        if (amrFile.exists()) {
                                            sendVoice(str, msgEntity);
                                        }
                                    }
                                });
                            } else {
                                if (new File(str).exists()) {
                                    sendVoice(str, msgEntity);
                                }
                            }
                        }
                    };
                    if (new File(filePath).canRead()) {
                        MyLog.debug(TAG, "文件已经存在 无需下载 " + filePath, true);
                        callBack.onResult(0, filePath);
                    } else {
                        MyLog.debug(TAG, "语音文件需要下载 filePath" + filePath + " fileId " + fileId + " aesKey " + aesKey + " md5 " + md5 + " size " + msgEntity.fileMsgEntity.size, true);
                        voiceMessageService.downVoiceFile(fileId, aesKey, md5, msgEntity.fileMsgEntity.size, filePath, callBack);
                    }
                }
            }, 4000);
        } else {
            Global.postRunnable2UIDelay(new Runnable() {
                @Override
                public void run() {
                    String filePath = VoiceMessageService.getVoiceSelfFilePath(String.valueOf(msgEntity.sender));
                    if (new File(filePath).exists()) {
                        MyLog.debug(TAG, "语音文件存在 " + filePath, true);
                        final VoiceMessageService voiceMessageService = new VoiceMessageService();
                        if (msgEntity.originMsg != null) {
                            VoiceMessageUtils.autoTranslateSelfVoiceText(loadPackageParam.classLoader, msgEntity.originMsg, new VoiceMessageService.IPickMessageCallback() {
                                @Override
                                public void onResult(int code, String transText) {
                                    MyLog.debug(TAG, "语音翻译结束 code " + code + "  message:" + transText);
                                    if (code == 0) {
                                        msgEntity.sttStatus = 2L;
                                        msgEntity.sttText = transText;
                                    } else {
                                        msgEntity.sttStatus = 3L;
                                        msgEntity.sttText = transText + "[" + code + "]";
                                    }
                                    sendSelfVoice(filePath, msgEntity);
                                }
                            });
                        } else {
                            sendSelfVoice(filePath, msgEntity);
                        }
                    } else {
                        MyLog.debug(TAG, "语音文件不存在" + filePath, true);
                    }
                }
            }, 4000);
        }
    }

    private String getObjectName(String fileName) {
        Long userId = LoginController.getInstance().getLoginUserId();
        String prefix = "voice";
        String objectName = String.format("wecom/robot/%d/%s/%s", userId, prefix, fileName);//文件;
        if (objectName.endsWith(".amr")) {
            return objectName;
        }
        return objectName + ".amr";
    }

    private String getSelfVoiceName(String fileName) {
        Long userId = LoginController.getInstance().getLoginUserId();
        String prefix = "voice";
        String objectName = String.format("wecom/robot/%d/%s/%s", userId, prefix, fileName);//文件;
        if (objectName.endsWith(".silk")) {
            return objectName;
        }
        return objectName + ".silk";
    }

    /**
     * @param voicePath 语音地址 语音没有缓存
     * @param msgEntity
     */
    private void sendVoice(String voicePath, MsgEntity msgEntity) {
        mMap.remove(msgEntity);
        String objectName = getObjectName(new File(voicePath).getName());
        String fullImgUrl = MConfiger.getFullImgUrl(objectName);
        msgEntity.content = fullImgUrl;
        ProtocalManager.getInstance().sendMsgEntity(msgEntity, "sendVoiceMsg");
        //上传文件到oss
        OssController.getInstance().uploadFile(voicePath, objectName, msgEntity);
        MyLog.debug(TAG, "[sendVoice]" + "语音翻译 " + msgEntity.sttText + "语音时长 " + (msgEntity.fileMsgEntity != null ? msgEntity.fileMsgEntity.voiceTime : 0) + " oss 音频文件:" + voicePath + " url:" + fullImgUrl + " type:" + msgEntity.contentType, true);
    }

    /**
     * @param voicePath 语音地址 语音没有缓存
     * @param msgEntity
     */
    private void sendSelfVoice(String voicePath, MsgEntity msgEntity) {
        mMap.remove(msgEntity);
        String objectName = getSelfVoiceName(new File(voicePath).getName());
        String fullImgUrl = MConfiger.getFullImgUrl(objectName);
        msgEntity.content = fullImgUrl;
        ProtocalManager.getInstance().sendMsgEntity(msgEntity, "sendSelfVoiceMsg");
        //上传文件到oss
        OssController.getInstance().uploadFile(voicePath, objectName, msgEntity);
        MyLog.debug(TAG, "[sendVoice]" + "silk " + msgEntity.sttText + " oss 音频文件:" + voicePath + " url:" + fullImgUrl + " type:" + msgEntity.contentType, true);
        if (msgEntity.conversationId != 0 && msgEntity.conversationId == LoginController.getInstance().getLoginUserEntity().remoteId) {
            sendVoiceCallbackMessage(fullImgUrl, msgEntity.conversationId);
        }
    }

    private void sendVoiceCallbackMessage(String content, long converId) {
        Object message = WeWorkMessageUtil.buildTexMessage(Global.loadPackageParam.classLoader, content);
        WeWorkMessageUtil.sendMessagePrivate(Global.loadPackageParam.classLoader, converId, converId, message, new MessageController.SendMessageCallBack() {
            @Override
            public void onResult(int code, Object conversation, Object message) {
                if (code == 0) {
                    MyLog.debug(TAG, "[onCall] sendVoiceCallbackMessage" + "发送成功...");
                } else {
                    MyLog.debug(TAG, "[onCall] sendVoiceCallbackMessage" + " 发送失败...");
                }
            }

            @Override
            public void onProgress(Object message, long j, long j2) {
            }
        });
    }
}
