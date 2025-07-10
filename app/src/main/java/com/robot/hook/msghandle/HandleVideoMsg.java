package com.robot.hook.msghandle;

import android.text.TextUtils;

import com.robot.common.MConfiger;
import com.robot.controller.LoginController;
import com.robot.controller.OssController;
import com.robot.controller.message.FileMessageService;
import com.robot.controller.resource.ResourceController;
import com.robot.entity.MsgEntity;
import com.robot.entity.VideoMessage;
import com.robot.hook.msghandle.base.BaseHandleMsg;
import com.robot.netty.ProtocalManager;
import com.robot.nettywss.WssProtocalManager;
import com.robot.util.MyLog;
import com.robot.util.ProxyUtil;

import java.io.File;
import com.robot.robothook.LoadPackageParam;

/**
 * 他人发送的视频消息
 */
public class HandleVideoMsg implements BaseHandleMsg {
    protected final String TAG = getClass().getSimpleName();

    public HandleVideoMsg(){

    }

    @Override
    public void onHandleMsg(LoadPackageParam loadPackageParam, MsgEntity msgEntity) {
        MyLog.debug(TAG,"[onHandleMsg]" + "Video: " + msgEntity);
        if (msgEntity.contentType==22){
            msgEntity.contentType =23;
        }
        downloadImg(msgEntity);
    }

    /***
     * 视频下载
     *
     */
    private void downloadImg(MsgEntity msgEntity){
        VideoMessage videoMessage =msgEntity.videoMsgEntity;
        int contentType=msgEntity.contentType;
        if ( videoMessage.videoId.length()==0&& videoMessage.url.length()>0 && videoMessage.url.contains("http")) {//OSS文件
            new FileMessageService().downLoadMessageFile(contentType,  videoMessage.md5, videoMessage.url , videoMessage.aesKey, videoMessage.wechatAuthKey
                    , videoMessage.size,videoMessage.md5, new ProxyUtil.ProxyStringResultCallBack() {
                        @Override
                        public void onResult(int i, String str) {
                            if (i == 0) {
                                uploadVideoFile(str,  msgEntity);
                            } else {
                                MyLog.debug(TAG, "文件下载失败 messageId" + msgEntity.msgId + "   path= " + str, true);
                                //下载失败也发条消息上去
                                uploadVideoFile(str,  msgEntity);
                            }
                        }
                    });
        }else {//本地文件
            MyLog.debug(TAG, "开始下载文件");
            new FileMessageService().downLoadLocalFile(contentType,  videoMessage.rawUrl,  videoMessage.videoId,  videoMessage.aesKey,  videoMessage.md5, videoMessage.md5
                    ,  videoMessage.size, new ProxyUtil.ProxyStringResultCallBack() {
                        @Override
                        public void onResult(int i, String str) {
                            if (i == 0) {
                                uploadVideoFile(str,  msgEntity);
                            } else {
                                MyLog.debug(TAG, "本地文件下载失败 messageId" + msgEntity.msgId + "   path= " + str, true);
                                //下载失败也发条消息上去
                                uploadVideoFile(str,  msgEntity);
                            }
                        }
                    });
        }

    }
    private String getObjectName(File  file){
        Long userId = LoginController.getInstance().getLoginUserId();
        String prefix="video";
        String objectName = String.format("wecom/robot/%d/%s/%s", userId, prefix, file.getName());//文件;
        if (objectName.endsWith(".mp4") ){
            return objectName;
        }
        return objectName+".mp4";
    }
    private void uploadVideoFile(String filePath, MsgEntity msgEntity) {

        String fullImgUrl  = ResourceController.getUploadUrl(filePath);
        if (TextUtils.isEmpty(fullImgUrl)){
            File file = new File(filePath);
            String objectName = getObjectName( file);
            fullImgUrl = MConfiger.getFullImgUrl(objectName);
            msgEntity.content = fullImgUrl;
            if (file.exists()){
                OssController.getInstance().uploadFile(filePath,objectName,msgEntity);
                MyLog.debug(TAG,"[downloadVideo]" + " 上传视频地址:" + fullImgUrl);
            }
        }else {
            MyLog.debug(TAG,"[downloadVideo]" + " localPath:" + filePath+" 已存在视频地址 "+fullImgUrl,true);
        }
        msgEntity.content = fullImgUrl;
//        ProtocalManager.getInstance().sendMsgEntity(msgEntity,"sendVideoMsg");
        WssProtocalManager.sendMsgEntity(msgEntity, "video");
    }
}
