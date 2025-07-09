package com.robot.hook.msghandle;

import android.os.SystemClock;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.robot.common.MD5;
import com.google.gson.Gson;
import com.robot.common.Global;
import com.robot.common.MConfiger;
import com.robot.controller.LoginController;
import com.robot.controller.OssController;
import com.robot.controller.message.FileMessageService;
import com.robot.controller.resource.ResourceController;
import com.robot.entity.FileTypeEnum;
import com.robot.entity.MsgEntity;
import com.robot.entity.ParamLoadImageEntity;
import com.robot.entity.ResEntity;
import com.robot.entity.WeAppMsgEntity;
import com.robot.hook.HookMethodEnum;
import com.robot.hook.base.HookBaseMethod;
import com.robot.hook.base.IHookCallBack;
import com.robot.hook.msghandle.base.BaseHandleMsg;
import com.robot.netty.proto.req.ReqMiniAppEntity;
import com.robot.nettywss.WssProtocalManager;
import com.robot.util.FileImgParseUtil;
import com.robot.util.FileUtil;
import com.robot.util.MyLog;
import com.robot.util.ProxyUtil;
import com.robot.util.StrUtils;

import java.io.File;

import com.robot.robothook.LoadPackageParam;

public class HandleMiniAppMsg implements BaseHandleMsg {
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onHandleMsg(LoadPackageParam loadPackageParam, MsgEntity msgEntity) {
        loadMiniApp(msgEntity);
    }

    private void loadMiniApp(MsgEntity msgEntity) {
        if (!TextUtils.isEmpty(msgEntity.url) && FileUtil.isLocalPath(msgEntity.url)) {
            uploadMiniProgram(msgEntity.url, msgEntity);
            MyLog.debug(TAG, "[onInvokeMethod]" + "  文件不需要下载：" + " url =" + msgEntity.url);
        } else if (msgEntity.weAppMsgEntity.wechatThumb != null && msgEntity.weAppMsgEntity.wechatThumb.imgUri != null && msgEntity.weAppMsgEntity.wechatThumb.imgUri.url != null) {
            WeAppMsgEntity.OpenImCdnImg openImCdnImg = msgEntity.weAppMsgEntity.wechatThumb;
            new FileMessageService().downLoadMessageFile(101, "", StrUtils.byteToUTFStr(openImCdnImg.imgUri.url),
                    StrUtils.byteToUTFStr(openImCdnImg.imgUri.aeskey), StrUtils.byteToUTFStr(openImCdnImg.imgUri.authkey), openImCdnImg.imgUri.size, StrUtils.byteToUTFStr(openImCdnImg.imgUri.md5), new ProxyUtil.ProxyStringResultCallBack() {
                        @Override
                        public void onResult(int i, String str) {
                            if (i == 0) {
                                uploadMiniProgram(str, msgEntity);
                            } else {
                                MyLog.debug(TAG, "小程序 文件下载失败 messageId" + msgEntity.msgId + "   path= " + str, true);
                                //下载失败也发条消息上去
                                uploadMiniProgram(str, msgEntity);
                            }
                        }
                    });
        } else if (FileUtil.isLocalPath(msgEntity.weAppMsgEntity.thumbUrl)) {
            uploadMiniProgram(msgEntity.weAppMsgEntity.thumbUrl, msgEntity);
        } else if (!TextUtils.isEmpty(msgEntity.weAppMsgEntity.thumbFileId)) {
            new FileMessageService().downLoadLocalFile(101, msgEntity.weAppMsgEntity.thumbUrl, msgEntity.weAppMsgEntity.thumbFileId, msgEntity.weAppMsgEntity.thumbAESKey,
                    msgEntity.weAppMsgEntity.thumbMD5, "", msgEntity.weAppMsgEntity.thumbSize,
                    new ProxyUtil.ProxyStringResultCallBack() {
                        @Override
                        public void onResult(int i, String str) {
                            if (i == 0) {
                                uploadMiniProgram(str, msgEntity);
                            } else {
                                MyLog.debug(TAG, "小程序 文件下载失败 messageId" + msgEntity.msgId + "   path= " + str, true);
                                //下载失败也发条消息上去
                                uploadMiniProgram(str, msgEntity);
                            }
                        }
                    });
            MyLog.debug(TAG, "[onInvokeMethod]" + "  小程序文件 下载：" + "   =" + JSON.toJSONString(msgEntity));
        }
    }

    private void loadMiniApp(LoadPackageParam loadPackageParam, MsgEntity msgEntity) {
        //图片
        WeAppMsgEntity.OpenImCdnImg openImCdnImg = msgEntity.weAppMsgEntity.wechatThumb;
        MyLog.debug(TAG, "[onInvokeMethod]" + " WeAppMsgEntity.OpenImCdnImg:" + openImCdnImg);
        ParamLoadImageEntity pEntity = new ParamLoadImageEntity();
        //收到的小程序 下载图片
        if (openImCdnImg != null && openImCdnImg.imgUri != null && openImCdnImg.imgUri.url != null) {
            WeAppMsgEntity.OpenImCdnUri openImCdnUri = openImCdnImg.imgUri;
            pEntity.contentType = 101;
            pEntity.url = FileImgParseUtil.StrUtilsDjz_cJ(openImCdnUri.url);
            pEntity.aesKey = FileImgParseUtil.StrUtilsDjz_cJ(openImCdnUri.aeskey);
            pEntity.authKey = FileImgParseUtil.StrUtilsDjz_cJ(openImCdnUri.authkey);
            pEntity.fileSize = openImCdnUri.size;
            if (openImCdnUri.md5 != null && openImCdnUri.md5.length > 0) {
                pEntity.md5 = FileImgParseUtil.StrUtilsDjz_cJ(openImCdnUri.md5);
            }
            MyLog.debug(TAG, "[onInvokeMethod]" + "    WeAppMsgEntity.OpenImCdnUri ：" + openImCdnUri.toString());
            //发出去的小程序 下载图片
        } else {
            pEntity.contentType = 101;
            pEntity.url = msgEntity.weAppMsgEntity.thumbUrl;
            pEntity.aesKey = msgEntity.weAppMsgEntity.thumbAESKey;
            pEntity.authKey = msgEntity.weAppMsgEntity.thumbFileId;
            pEntity.fileSize = msgEntity.weAppMsgEntity.thumbSize;
            if (!TextUtils.isEmpty(msgEntity.weAppMsgEntity.thumbMD5)) {
                pEntity.md5 = msgEntity.weAppMsgEntity.thumbMD5;
            }
            MyLog.debug(TAG, "[onInvokeMethod]" + "  收到 pEntity ：" + msgEntity.weAppMsgEntity);
        }
        //小程序 图片 自己发送的情况  文件存在不需要下载
        if (!TextUtils.isEmpty(pEntity.url) && pEntity.url.startsWith(FileUtil.getSdcardPath())) {
            uploadMiniProgram(pEntity.url, msgEntity);
            MyLog.debug(TAG, "[onInvokeMethod]" + "  文件不需要下载：" + pEntity.url + " url =" + pEntity.url);
        } else {
            if (TextUtils.isEmpty(pEntity.url)) {
                pEntity.md5 = TextUtils.isEmpty(msgEntity.weAppMsgEntity.thumbMD5) ? SystemClock.currentThreadTimeMillis() + "" : msgEntity.weAppMsgEntity.thumbMD5;
                String localPath = FileUtil.getSaveImageFilePath(pEntity.md5);
                HookBaseMethod<ParamLoadImageEntity> baseMethod = HookMethodEnum.IMG_LOADER_SELF.getMethod();
                boolean succ = baseMethod.onInvokeMethod(null, Global.loadPackageParam, new IHookCallBack<ParamLoadImageEntity>() {
                    @Override
                    public void onCall(ResEntity<ParamLoadImageEntity> resEntity) {
                        if (resEntity.isSucc()) {
                            uploadMiniProgram(resEntity.getData().filePath, msgEntity);
                            MyLog.debug(TAG, "[onInvokeMethod]" + "  自己发的图片下载成功 ：" + resEntity.getData().filePath + " url =" + pEntity.url);
                        } else {
                            uploadMiniProgram(resEntity.getMsg(), msgEntity);
                            MyLog.debug(TAG, "[onInvokeMethod]" + "  自己发的图片下载失败 ：" + resEntity.getMsg() + " url =" + pEntity.url);
                        }
                    }

                    @Override
                    public ParamLoadImageEntity getParams() {
                        ParamLoadImageEntity paramLoadImageEntity = new ParamLoadImageEntity();
                        paramLoadImageEntity.aesKey = msgEntity.weAppMsgEntity.thumbAESKey;
                        paramLoadImageEntity.md5 = msgEntity.weAppMsgEntity.thumbMD5;
                        paramLoadImageEntity.fileSize = msgEntity.weAppMsgEntity.thumbSize;
                        paramLoadImageEntity.filePath = localPath;
                        paramLoadImageEntity.fileType = FileTypeEnum.TYPE_IMG.getType();
                        paramLoadImageEntity.fieldId = msgEntity.weAppMsgEntity.thumbFileId;
                        return paramLoadImageEntity;
                    }
                });
                MyLog.debug(TAG, "[downloadImg]" + " succ:" + succ);
            } else {
                //小程序 图片  收到小程序情况   文件需要下载
                HookBaseMethod<ParamLoadImageEntity> hookBaseMethod = HookMethodEnum.File_LOADER.getMethod();
                hookBaseMethod.onInvokeMethod(null, loadPackageParam, new IHookCallBack<ParamLoadImageEntity>() {
                    @Override
                    public void onCall(ResEntity<ParamLoadImageEntity> resEntity) {

                        if (resEntity.isSucc()) {
                            uploadMiniProgram(resEntity.getData().filePath, msgEntity);
                            MyLog.debug(TAG, "[onInvokeMethod]" + "  文件下载成功 ：" + resEntity.getData().filePath + " url =" + pEntity.url);
                        } else {
                            uploadMiniProgram(resEntity.getMsg(), msgEntity);
                            MyLog.debug(TAG, "[onInvokeMethod]" + "  文件下载失败 ：" + resEntity.getMsg() + " url =" + pEntity.url);
                        }
                    }

                    @Override
                    public ParamLoadImageEntity getParams() {
                        return pEntity;
                    }
                });
            }
        }

    }

    private String getObjectName(String fileName) {
        Long userId = LoginController.getInstance().getLoginUserId();
        String prefix = "png";
        String objectName = String.format("wecom/robot/%d/%s/%s", userId, prefix, fileName);//文件;
        if (objectName.endsWith(".jpg") || objectName.endsWith(".png")) {
            return objectName;
        }
        return objectName + ".jpg";
    }

    private void uploadMiniProgram(String thumbPath, MsgEntity msgEntity) {
        String fullUrl = thumbPath;
        boolean needUpload = false;
        String objectName = null;
        if (!TextUtils.isEmpty(fullUrl) && (fullUrl.startsWith("/sdcard") || fullUrl.startsWith("/storage"))) {
            fullUrl = ResourceController.getUploadUrl(thumbPath);
            if (TextUtils.isEmpty(fullUrl)) {
                File file = new File(thumbPath);
                if (file.exists()) {
                    needUpload = true;
                }
                String fileName = MD5.getFileMD5(thumbPath) + ".jpg";
                objectName = getObjectName(fileName);
                fullUrl = MConfiger.getFullImgUrl(objectName);
                MyLog.debug(TAG, "[onInvokeMethod]" + " img 上传 succ :" + file.exists() + " fullUrl:" + fullUrl, true);
            } else {
                MyLog.debug(TAG, "[onInvokeMethod]" + " img  不需要上传  " + fullUrl, true);
            }
        }
        //数据组合上传到服务器
        ReqMiniAppEntity reqLinkEntity = WeAppMsgEntity.parseContent(msgEntity.weAppMsgEntity, fullUrl);
        Gson gson = new Gson();
        String content = gson.toJson(reqLinkEntity);
        msgEntity.content = content;
        WssProtocalManager.sendMsgEntity(msgEntity, "miniProgram");
        if (needUpload && objectName != null) {
            OssController.getInstance().uploadFile(thumbPath, objectName, msgEntity);
        }
    }
}
