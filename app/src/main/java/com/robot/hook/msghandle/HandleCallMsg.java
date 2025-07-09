package com.robot.hook.msghandle;

import com.robot.common.StatsHelper;
import com.robot.common.MConfiger;
import com.robot.controller.LoginController;
import com.robot.controller.OssController;
import com.robot.entity.FileMsgEntity;
import com.robot.entity.MsgEntity;
import com.robot.hook.group.HookCallRecordMethod;
import com.robot.hook.msghandle.base.BaseHandleMsg;
import com.robot.netty.ProtocalManager;
import com.robot.util.MyLog;
import com.robot.util.StrUtils;
import com.robot.robothook.LoadPackageParam;
import com.robot.robothook.utils.StringUtils;

import java.io.File;

public class HandleCallMsg implements BaseHandleMsg {
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onHandleMsg(LoadPackageParam loadPackageParam, MsgEntity msgEntity) {
//        MyLog.debug(TAG, "[onHookInfo]" + "onVoipEvent1 " + "HandleCallMsg getMIsRecording" + HookCallRecordMethod.getMIsRecordEnd());
//        StatsHelper.event("msgReport", "reportlog", "isNeedUpload " + HookCallRecordMethod.getMIsRecordEnd(), "time " + StrUtils.getTimeDetailStr());
        String path = HookCallRecordMethod.getOutputFile();
        if (StringUtils.isNotEmpty(path) && HookCallRecordMethod.getMIsRecordEnd()) {
            if (msgEntity.fileMsgEntity == null) {
                msgEntity.fileMsgEntity = new FileMsgEntity();
            }
            msgEntity.fileMsgEntity.voiceTime = (int) HookCallRecordMethod.getMRecordTime();
            sendVoice(path, msgEntity);
            HookCallRecordMethod.setOutputFile("");
        }
    }

    /**
     * @param voicePath 语音地址 语音没有缓存
     * @param msgEntity
     */
    private void sendVoice(String voicePath, MsgEntity msgEntity) {
//        mMap.remove(msgEntity);
        String objectName = getObjectName(new File(voicePath).getName());
        String fullImgUrl = MConfiger.getFullImgUrl(objectName);
        msgEntity.content = fullImgUrl;
        ProtocalManager.getInstance().sendMsgEntity(msgEntity, "sendCallMsg");
        //上传文件到oss
        File amrFile = new File(voicePath);
        boolean exists = amrFile.exists();
        if (exists) {
            OssController.getInstance().uploadFile(voicePath, objectName, msgEntity);
        }
        StatsHelper.event("msgReport", "reportlog", "HandleCallMsg exists " + exists + "path " + voicePath + " voiceTime" + msgEntity.fileMsgEntity.voiceTime, "time " + StrUtils.getTimeDetailStr());
        MyLog.debug(TAG, "[sendVoice]" + " oss 音频文件:" + voicePath + " url:" + fullImgUrl + " type:" + msgEntity.contentType + " voiceTime" + msgEntity.fileMsgEntity.voiceTime, true);
    }

    private String getObjectName(String fileName) {
        Long userId = LoginController.getInstance().getLoginUserId();
        String prefix = "record";
        String objectName = String.format("wecom/robot/%d/%s/%s", userId, prefix, fileName);//文件;
        if (objectName.endsWith(".mp3")) {
            return objectName;
        }
        return objectName + ".mp3";
    }
}
