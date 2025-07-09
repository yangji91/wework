package com.robot.entity;

import android.text.TextUtils;

import com.robot.common.Global;
import com.robot.controller.resource.ResourceController;
import com.robot.exception.MessageException;
import com.robot.hook.KeyConst;
import com.robot.hook.util.ConvParseUtil;
import com.robot.netty.entity.rsp.PRspActionTextMsgEntity;
import com.robot.util.MyLog;
import com.robot.robothook.RobotHelpers;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SendHelperSendMsgEntity implements Serializable {
    public static final String TAG = "SendHelperSendMsgEntity";
    public long[] targetUserRemoteIdList = null;
    public String content = "";
    public List<Object> multiMediaContentList = new LinkedList<>();

    //图片
    public static class SendHelperSendImgEntity implements Serializable {
        public String localPath;

        public SendHelperSendImgEntity(String content) throws MessageException {
            if (content.startsWith("http")) {
                localPath = ResourceController.saveIntNetFile(content, ResourceController.MResource.IMAGE);
                MyLog.debug("SendHelperSendMsgEntity", "[sendImgMsg]" + " fileFullPathName:" + localPath + " 下载 图片 imgHttpPath:" + content, true);
            } else {
                localPath = content;
            }
        }
    }

    //视频
    public static class SendHelperSendVideoEntity implements Serializable {
        public String localPath;

        public SendHelperSendVideoEntity() {
        }

        public SendHelperSendVideoEntity(String content) throws MessageException {
            if (!TextUtils.isEmpty(content) && content.startsWith("http")) {
                localPath = ResourceController.saveIntNetFile(content, ResourceController.MResource.VIDEO);
                MyLog.debug(TAG, "[SendHelperSendVideoEntity]" + " fileFullPathName:" + localPath + " 下载 视频 imgHttpPath:" + content, true);
            } else {
                localPath = content;
            }
        }
    }

    //文件
    public static class SendHelperSendFileEntity implements Serializable {
        public String localPath;
        public String fileName;
        public String fileId;
//        public String aesKey;

        public SendHelperSendFileEntity() {
        }

        public SendHelperSendFileEntity(String content, String fileTitle, String fileType) throws MessageException {
            fileName = fileTitle + "." + fileType;
            if (!TextUtils.isEmpty(content) && content.startsWith("http")) {
                localPath = ResourceController.saveIntNetFile(content, fileName, ResourceController.MResource.FILE);
                MyLog.debug(TAG, "[SendHelperSendVideoEntity]" + " fileFullPathName:" + localPath + " 下载 文件 fileHttpPath:" + content, true);
                Class clazzFtnUploadCallback = RobotHelpers.findClassIfExists(KeyConst.C_IFtnUploadCallback_CALLBACK, Global.loadPackageParam.classLoader);
                Class clazzFtnProgress = RobotHelpers.findClassIfExists(KeyConst.C_FTNPROGRO_CALLBACK, Global.loadPackageParam.classLoader);
                Class[] clazzArray = {boolean.class, String.class, clazzFtnUploadCallback, clazzFtnProgress};
                Object[] objArray = {true, localPath, Proxy.newProxyInstance(Global.loadPackageParam.classLoader, new Class[]{clazzFtnUploadCallback}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        String name = method.getName();
                        if (KeyConst.M_FTNPROGRO_CALLBACK_onResult.equals(name)) {
                            MyLog.debug(TAG, "[SendHelperSendFileEntity]" + " params " + Arrays.toString(args));
                            int ret = (int) args[0];
                            fileId = (String) args[1];
//                            String md5 = (String) args[2];
                        }
                        return null;
                    }
                }), null};
                Global.postRunnable2UI(() -> {
                    Object objService = ConvParseUtil.getFileService();
                    try {
                        RobotHelpers.callMethod(objService, KeyConst.M_Application_GetProfileManager_GetCurrentProfile_getServiceManager_getFileService_FtnUploadFileOnPath, clazzArray, objArray);
                        MyLog.debug(TAG, "[SendHelperSendFileEntity]" + " call method succ...");
                    } catch (Exception ee) {
                        MyLog.error(TAG, ee);
                    }
                });
            } else {
                localPath = content;
            }
        }
    }

    //网页
    public static class SendHelperSendPageEntity implements Serializable {
        public String url = null;
        public String title = null;
        public String desc = null;
        public String imageUrl = null;
        public byte[] imageDate = null;

        public SendHelperSendPageEntity(PRspActionTextMsgEntity.H5Msg h5Msg) {
            url = h5Msg.linkUrl;
            title = h5Msg.title;
            desc = h5Msg.description;
            /*if (!TextUtils.isEmpty(h5Msg.imageUrl)&&h5Msg.imageUrl.startsWith("http")) {
                String fileName = MD5.toString(h5Msg.imageUrl);
                String fileFullPathName = FileUtil.getSaveImageFilePath(fileName);
                MyLog.debug(TAG, "[SendHelperSendLinkEntity]" + " fileName:" + fileName + " fileFullPathName:" + fileFullPathName + " 下载 h5Msg imageUrl:" + h5Msg.imageUrl,true);
                try {
                    imageUrl   = HttpUtil.saveIntNetFile(h5Msg.imageUrl, new File(fileFullPathName));
                } catch (MessageException e) {
                    imageUrl="";
                }
            }else {
                imageUrl  =h5Msg.imageUrl;
            }*/
            imageUrl = h5Msg.imageUrl;
        }

        public SendHelperSendPageEntity() {

        }
    }

    //小程序 貌似需要自己生成一个链接, 不是通用链接
    public static class SendHelperSendLittleProgramEntity implements Serializable {
        /*
          public String title;

          public String imageUrl;
          public byte[] imageDate;*/
        public String url;
        public String appName;
        public String thumbUrl;
        public String appId;
        public String username;
        public String pagePath;
        public String title;
        public String iconUrl;
        public String desc;

        public SendHelperSendLittleProgramEntity(PRspActionTextMsgEntity.MiniProgram miniProgram) {
            username = miniProgram.username;
            pagePath = miniProgram.pagePath;
            appId = miniProgram.appId;
            if (!TextUtils.isEmpty(miniProgram.thumbUrl) && miniProgram.thumbUrl.startsWith("http")) {
                try {
                    thumbUrl = ResourceController.saveIntNetFile(miniProgram.thumbUrl, ResourceController.MResource.IMAGE);
                    MyLog.debug(TAG, "[SendHelperSendLittleProgramEntity]" + " fileFullPathName:" + thumbUrl + " 下载 小程序 图片 imgHttpPath:" + miniProgram.thumbUrl, true);
                } catch (MessageException e) {
                    thumbUrl = "";
                }
            } else {
                thumbUrl = miniProgram.thumbUrl;
            }
            iconUrl = miniProgram.iconUrl;
            title = miniProgram.title;
            desc = "";
            appName = miniProgram.appName;
        }

        public SendHelperSendLittleProgramEntity() {

        }
    }

    public ActionRet sendHelperSendGroupMsgResponseEntity;

}
