package com.robot.hook.msghandle;

import android.text.TextUtils;

import com.robot.common.MConfiger;
import com.robot.controller.LoginController;
import com.robot.controller.OssController;
import com.robot.controller.message.FileMessageService;
import com.robot.controller.resource.ResourceController;
import com.robot.entity.FileMsgEntity;
import com.robot.entity.MsgEntity;
import com.robot.netty.ProtocalManager;
import com.robot.util.FileUtil;
import com.robot.util.MyLog;
import com.robot.util.ProxyUtil;
import com.robot.util.StrUtils;

import java.io.File;

import com.robot.robothook.LoadPackageParam;

public class HandleFileSelfMsgV2 extends HandleFileMsg {
    protected final String TAG = getClass().getSimpleName();
    ///storage/emulated/0/robot/com/e61e3b72564fb436.txt

    @Override
    public void onHandleMsg(LoadPackageParam loadPackageParam, MsgEntity msgEntity) {
        MyLog.debug(TAG, "[onHandleMsg]" + "... msgEntity:" + msgEntity.fileMsgEntity);
        FileMsgEntity fileMsgEntity = msgEntity.fileMsgEntity;
        String fileName = StrUtils.byteToUTFStr(fileMsgEntity.fileName);
        String localPath = StrUtils.byteToUTFStr(fileMsgEntity.url);
        if (FileUtil.isLocalPath(localPath)) {
            uploadFile(localPath, msgEntity);
        } else {
            //"fileMsgEntity": {
            //        "aesKey": "NjEzMTMwNjMzMTYxMzk2NjMwMzczNTM1MzEzNzY0NjY=",
            //        "encryptSize": 9479,
            //        "fileId": "MzA2ODAyMDEwMjA0NjEzMDVmMDIwMTAwMDIwNDIxNWU4MTUyMDIwMzBmNTVjYTAyMDQyMjg0Mjc3ZDAyMDQ2MTI3NTViOTA0MjQzMjYyMzYzMjM4NjUzODMxMmQ2NTM3MzA2NDJkMzQzMTM2NjMyZDM4MzgzNjMwMmQzODM3MzczNDYyMzAzNzM1MzQzNzM3MzIwMjAxMDAwMjAyMjUxMDA0MTBhYTlhY2RmZWI0ZmJkOTE3OTRiOWYyZmI3YTFkZmNiODAyMDEwNTAyMDEwMDA0MDA=",
            //        "fileName": "MGExYzM4NTQ2ZDQ3NmJlOS54bWw=",
            //        "md5": "YWE5YWNkZmViNGZiZDkxNzk0YjlmMmZiN2ExZGZjYjg=",
            //        "url": "","size": 9479,

            //    },

            new FileMessageService().downLoadLocalFile(msgEntity.contentType, localPath, StrUtils.byteToUTFStr(fileMsgEntity.fileId), StrUtils.byteToUTFStr(fileMsgEntity.aesKey), StrUtils.byteToUTFStr(fileMsgEntity.md5)
                    , fileName, fileMsgEntity.size, new ProxyUtil.ProxyStringResultCallBack() {
                        @Override
                        public void onResult(int code, String str) {
                            uploadFile(str, msgEntity);
                            MyLog.debug(TAG, "下载文件 " + msgEntity.contentType + "类型文件 code" + code + " str " + str);
                        }
                    });
        }

    }

    private void uploadFile(String localPath, MsgEntity msgEntity) {
        String fullUrl = ResourceController.getUploadUrl(localPath);
        File file = new File(localPath);
        String fileName = StrUtils.byteToUTFStr(msgEntity.fileMsgEntity.fileName);
        if (TextUtils.isEmpty(fullUrl) && file.exists()) {
            String objectName = getObjectName(file, LoginController.getInstance().getLoginUserId(), "file");
            fullUrl = MConfiger.getFullImgUrl(objectName);
            fullUrl = fullUrl + HandleFileMsg.buildParams(fileName, file.length(), FileUtil.getFileTail(fileName));
            msgEntity.content = fullUrl;
            OssController.getInstance().uploadFile(localPath, objectName, msgEntity);
        } else if (!fullUrl.contains("?fileTitle=")) {
            fullUrl = fullUrl + HandleFileMsg.buildParams(fileName, file.length(), FileUtil.getFileTail(fileName));
        }
        msgEntity.content = fullUrl;
        ProtocalManager.getInstance().sendMsgEntity(msgEntity, "sendSelfFileMsg");
    }
}
