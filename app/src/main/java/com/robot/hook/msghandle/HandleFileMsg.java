package com.robot.hook.msghandle;

import android.text.TextUtils;

import com.robot.common.MConfiger;
import com.robot.controller.LoginController;
import com.robot.controller.OssController;
import com.robot.controller.resource.ResourceController;
import com.robot.entity.FileMsgEntity;
import com.robot.entity.MsgEntity;
import com.robot.netty.ProtocalManager;
import com.robot.util.FileUtil;
import com.robot.util.MyLog;
import com.robot.util.StrUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class HandleFileMsg extends HandleMsgImage {
    private String TAG ="HandleFileMsg";



    public static String buildParams(String fileRemoteName,Long fileSize,String fileType){
        String encodeResult = "";
        if(!TextUtils.isEmpty(fileRemoteName)){
            try {
                encodeResult = URLEncoder.encode(fileRemoteName,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return "?fileTitle=" + encodeResult + "&" + "fileSize=" + fileSize + "&fileType=" + fileType;
    }
    public   String getObjectName(File file,long userId,String prefix){
        String objectName = String.format("wecom/robot/%d/%s/%s", userId, prefix, file.getName());//文件;
        return objectName ;
    }
    protected   void sendMsg(String localPath, int contentType, FileMsgEntity entity, MsgEntity msgEntity){
        MyLog.debug(TAG,"[sendMsg]" + "======ParamLoadImageEntity=001");
        //图片消息类型
        String fileName = StrUtils.byteToUTFStr(entity.fileName);
        long fileSize = entity.size;
        File file = new File(localPath);
        String fullUrl = ResourceController.getUploadUrl(localPath);
        MyLog.debug(TAG,"[upload]" +  " fullUrl:" + fullUrl   );
        if (TextUtils.isEmpty(fullUrl)&& file.exists()){
            String objectName =getObjectName(file, LoginController.getInstance().getLoginUserId(),"file");
            fullUrl = MConfiger.getFullImgUrl(objectName);
            fullUrl = fullUrl +  buildParams(fileName,fileSize, FileUtil.getFileTail(fileName));
            msgEntity.content=fullUrl;
            OssController.getInstance().uploadFile(localPath,objectName,msgEntity);
        }else {
            fullUrl = fullUrl +  buildParams(fileName,fileSize, FileUtil.getFileTail(fileName));
            msgEntity.content=fullUrl;
        }

        ProtocalManager.getInstance().sendMsgEntity(msgEntity,"sendFileMsg");
        MyLog.debug(TAG,"[upload]" +  " fullUrl:" + fullUrl + " contentType:" + msgEntity.contentType + " fileName:" + fileName + " size:" + fileSize,true);
    }


}
