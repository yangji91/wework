package com.robot.controller.message;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

import com.alibaba.fastjson.JSON;
import com.robot.common.MConfiger;
import com.tencent.smtt.utils.Md5Utils;
import com.robot.common.Global;
import com.robot.controller.ConvController;
import com.robot.hook.KeyConst;
import com.robot.hook.util.ConvParseUtil;
import com.robot.netty.entity.rsp.PRspActionTextMsgEntity;
import com.robot.util.CodedOutputByteBufferNanoUtil;
import com.robot.util.FileUtil;
import com.robot.util.MyLog;
import com.robot.util.ProxyUtil;
import com.robot.util.StrUtils;
import com.robot.robothook.RobotHelpers;

import java.io.File;
import java.lang.reflect.Array;
import java.util.List;

/***
 *@author
 *@date 2021/7/12
 *@description 企微消息构建 工具类
 ****/
public class WeWorkMessageUtil {

    private static final String TAG = "WeWorkMessageUtil";

    /**
     * @param classLoader 类加载器
     * @param contentType 消息类型
     * @param MessageNano MessageNano 最内层消息体
     * @return 直接发送的Message
     */
    public static Object buildWeWorkMessage(ClassLoader classLoader, int contentType, Object MessageNano) {
        Class<?> clazz_WwMessage$Message = RobotHelpers.findClass(KeyConst.C_WwMessage_Message, classLoader);
        //com.google.protobuf.nano.MessageNano
        Class<?> clazz_MessageNano = RobotHelpers.findClass(KeyConst.C_MESSAGE_NANO, classLoader);
        Object obj_WwMessage$Message = RobotHelpers.newInstance(clazz_WwMessage$Message);
        RobotHelpers.setObjectField(obj_WwMessage$Message, KeyConst.F_MSGITEM_MessageNano_messages_contentType, contentType);
        Object obj_tempObj = RobotHelpers.callStaticMethod(clazz_MessageNano, KeyConst.F_MSGITEM_MessageNano_messages_toByteArray, MessageNano);
        RobotHelpers.setObjectField(obj_WwMessage$Message, KeyConst.F_MSGITEM_MessageNano_messages_content, obj_tempObj);
        Class<?> clazz_Message = RobotHelpers.findClass(KeyConst.C_MESSAGE, classLoader);
        Object obj_Message = RobotHelpers.callStaticMethod(clazz_Message, KeyConst.M_MESSAGE_NewMessage);
        RobotHelpers.callMethod(obj_Message, KeyConst.M_MESSAGE_NewMessage_setInfo, obj_WwMessage$Message);
        return obj_Message;
    }

    /**
     * @param classLoader
     * @param content     文本内容
     * @return MessageNano 文本消息
     */
    public static Object buildTextualMessage(ClassLoader classLoader, String content) {
        Object obj_IMsg = RobotHelpers.callStaticMethod(RobotHelpers.findClass(KeyConst.C_MSG_CC, classLoader), KeyConst.M_MSG_CC_GET);
        Object tempObj = RobotHelpers.callMethod(obj_IMsg, KeyConst.M_MSG_CC_formatExpressionText, content);
        return RobotHelpers.callMethod(obj_IMsg, KeyConst.M_MSG_CC_buildTextualMessage, tempObj);
    }

    /**
     * 创建 文本类型消息
     *
     * @param classLoader
     * @param content
     * @return 可以直接发送的Message
     */
    public static Object buildTexMessage(ClassLoader classLoader, String content) {
        Object textualMessage = buildTextualMessage(classLoader, content);
        return buildWeWorkMessage(classLoader, 0, textualMessage);
    }


    /**
     * 创建 连接类型消息
     *
     * @param classLoader 类加载器
     * @param url         连接
     * @param title       标题
     * @param desc        描述
     * @param imageUrl    图片地址(本地)
     * @return 可以直接发送的Message
     */
    public static Object buildLinkMessage(ClassLoader classLoader, String url, String title, String desc, String imageUrl) {
        return buildLinkMessage(classLoader, url, title, desc, imageUrl, null);
    }

    private static Object buildLinkMessage(ClassLoader classLoader, String url, String title, String desc, String imageUrl, byte[] imageData) {
        Object obj_IMsg = RobotHelpers.callStaticMethod(RobotHelpers.findClass(KeyConst.C_MSG_CC, classLoader), KeyConst.M_MSG_CC_GET);
        Object linkMessage = RobotHelpers.callMethod(obj_IMsg, KeyConst.C_MSG_CC_buildLinkMessage, url, title, desc, imageUrl, imageData);
        return WeWorkMessageUtil.buildWeWorkMessage(classLoader, 13, linkMessage);
    }


    public static Object buildVideoMessage(ClassLoader classLoader, String localPath) {
        return buildVideoMessage(classLoader, localPath, 5);
    }


    private static Object getImageFileMessage(ClassLoader classLoader, String localPath) {
        Class<?> clazz_FileMessage = RobotHelpers.findClass(KeyConst.C_WwRickMsg_FileMsg, classLoader);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(localPath, options);
        Object obj_fileMessage = RobotHelpers.newInstance(clazz_FileMessage);
        RobotHelpers.setObjectField(obj_fileMessage, KeyConst.F_WwRickMsg_FileMsg_url, StrUtils.strToUTFByte(localPath));
        RobotHelpers.setObjectField(obj_fileMessage, KeyConst.F_WwRickMsg_FileMsg_fileName, FileUtil.getFileName(localPath).getBytes());
        RobotHelpers.setObjectField(obj_fileMessage, KeyConst.F_WwRickMsg_FileMsg_size, FileUtil.getFileSize(localPath));
        RobotHelpers.setObjectField(obj_fileMessage, KeyConst.F_WwRickMsg_FileMsg_width, options.outWidth);
        RobotHelpers.setObjectField(obj_fileMessage, KeyConst.F_WwRickMsg_FileMsg_height, options.outHeight);
        //RobotHelpers.setObjectField(obj_fileMessage, "aesKey", RobotHelpers.callStaticMethod(clazz_cch, "utf8Bytes", null));
        RobotHelpers.setObjectField(obj_fileMessage, KeyConst.F_WwRickMsg_FileMsg_aesKey, new byte[0]);
        RobotHelpers.setObjectField(obj_fileMessage, KeyConst.F_WwRickMsg_FileMsg_fileId, new byte[0]);
        RobotHelpers.setObjectField(obj_fileMessage, KeyConst.F_WwRickMsg_FileMsg_md5, Md5Utils.getMD5(new File(localPath)).getBytes());
        return obj_fileMessage;
    }

    private static Object getFileMessage(ClassLoader classLoader, String localPath, String fileName, String fileId) {
        Class<?> clazz_FileMessage = RobotHelpers.findClass(KeyConst.C_WwRickMsg_FileMsg, classLoader);
        Object obj_fileMessage = RobotHelpers.newInstance(clazz_FileMessage);
        RobotHelpers.setObjectField(obj_fileMessage, KeyConst.F_WwRickMsg_FileMsg_fileName, StrUtils.strToUTFByte(fileName));
        RobotHelpers.setObjectField(obj_fileMessage, KeyConst.F_WwRickMsg_FileMsg_size, FileUtil.getFileSize(localPath));
//        RobotHelpers.setObjectField(obj_fileMessage, KeyConst.F_WwRickMsg_FileMsg_aesKey, "".getBytes(StandardCharsets.UTF_8));
        RobotHelpers.setObjectField(obj_fileMessage, KeyConst.F_WwRickMsg_FileMsg_fileId, StrUtils.strToUTFByte(fileId));
//        RobotHelpers.setObjectField(obj_fileMessage, KeyConst.F_WwRickMsg_FileMsg_md5, Md5Utils.getMD5(new File(localPath)).getBytes());
        return obj_fileMessage;
    }

    /**
     * 创建图片消息
     *
     * @param classLoader
     * @param localPath
     * @return
     */
    public static Object buildImageMessage(ClassLoader classLoader, String localPath) {
        Object obj_fileMessage = getImageFileMessage(classLoader, localPath);
        return WeWorkMessageUtil.buildWeWorkMessage(classLoader, 7, obj_fileMessage);
    }

    /**
     * @param classLoader
     * @param localPath   视频地址(本地)
     * @return 可以直接发送的Message
     */
    public static Object buildVideoMessage(ClassLoader classLoader, String localPath, int contentType) {
        String thumb = FileUtil.getVideoThumbnail(localPath);
        Object videoMessage;
        if (Global.getWecomVersion().equals(MConfiger.WEWORK_VERSION_17857) || Global.getWecomVersion().equals(MConfiger.WEWORK_VERSION_19717)) {
            videoMessage = RobotHelpers.callStaticMethod(RobotHelpers.findClass(KeyConst.C_MessageManager, classLoader), KeyConst.M_MessageManager_buildVideoMessage, localPath, thumb);
        } else {
            //todo
            videoMessage = RobotHelpers.callStaticMethod(RobotHelpers.findClass(KeyConst.C_MessageManager, classLoader), KeyConst.M_MessageManager_buildVideoMessage, localPath, thumb, false);
        }        /*Class<?> clazz_videoMessage = RobotHelpers.findClass("com.tencent.wework.foundation.model.pb.WwRichmessage$VideoMessage",  classLoader);
        JSONObject object = SightVideoUtil.getSimpleMp4InfoVFS(classLoader,localPath);
        Object videoMessage = RobotHelpers.newInstance(clazz_videoMessage);
        RobotHelpers.setObjectField(videoMessage, "url",  localPath.getBytes());
        //RobotHelpers.setObjectField(videoMessage, "fileName", FileUtil.getFileName( localPath).getBytes());
        RobotHelpers.setObjectField(videoMessage, "size", FileUtil.getFileSize(localPath));
        RobotHelpers.setObjectField(videoMessage, "videoWidth", object.optInt("videoWidth") );
        RobotHelpers.setObjectField(videoMessage, "videoWidth", object.optInt("videoHeight"));
        //RobotHelpers.setObjectField(obj_fileMessage, "aesKey", RobotHelpers.callStaticMethod(clazz_cch, "utf8Bytes", null));
        RobotHelpers.setObjectField(videoMessage, "aesKey", new byte[0]);
        RobotHelpers.setObjectField(videoMessage, "videoDuration", object.optInt("videoDuration"));
        RobotHelpers.setObjectField(videoMessage, "previewImgUrl", FileUtil.getVideoThumbnail(localPath).getBytes());
        RobotHelpers.setObjectField(videoMessage, "videoId", "".getBytes());
        RobotHelpers.setObjectField(videoMessage, "md5", Md5Utils.getMD5(new File(localPath)).getBytes());*/
        return WeWorkMessageUtil.buildWeWorkMessage(classLoader, contentType, videoMessage);
    }

    /**
     * 构建小程序 消息
     *
     * @param classLoader 类加载器
     * @param appid       gh_5232ef019802@app
     * @param username    wx
     * @param pagePath    打开的页面路径
     * @param iconUrl     图标 url
     * @param title       标题
     * @param thumbUrl    图片的path (本地)
     * @param appName     小程序名称
     * @param desc        描述
     * @return 可以直接发送的Message
     */
    public static Object buildMiniProgremMessage(ClassLoader classLoader, String appid, String username, String pagePath, String iconUrl, String title
            , String thumbUrl, String appName, String desc) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(thumbUrl, options);
        //构造com.tencent.wework.foundation.model.pb.WwRichmessage$WeAppMessage
        Class<?> clazz_WwRichmessage$WeAppMessage = RobotHelpers.findClass(KeyConst.C_WwRichmessage_WeAppMessage, classLoader);
        Object weAppMessage = RobotHelpers.newInstance(clazz_WwRichmessage$WeAppMessage);
        RobotHelpers.setObjectField(weAppMessage, KeyConst.F_WwRichmessage_WeAppMessage_username, username);
        RobotHelpers.setObjectField(weAppMessage, KeyConst.F_WwRichmessage_WeAppMessage_appid, appid == null ? "" : appid);
        RobotHelpers.setObjectField(weAppMessage, KeyConst.F_WwRichmessage_WeAppMessage_type, 2);
        // RobotHelpers.setObjectField(obj_WwRichmessage$WeAppMessage,"type",2);
        RobotHelpers.setObjectField(weAppMessage, KeyConst.F_WwRichmessage_WeAppMessage_pagepath, pagePath == null ? "" : pagePath);
        RobotHelpers.setObjectField(weAppMessage, KeyConst.F_WwRichmessage_WeAppMessage_pkginfoType, 0);
        RobotHelpers.setObjectField(weAppMessage, KeyConst.F_WwRichmessage_WeAppMessage_version, 0);
        RobotHelpers.setObjectField(weAppMessage, KeyConst.F_WwRichmessage_WeAppMessage_weappIconUrl, iconUrl);
        RobotHelpers.setObjectField(weAppMessage, KeyConst.F_WwRichmessage_WeAppMessage_title, title == null ? "" : title);
        RobotHelpers.setObjectField(weAppMessage, KeyConst.F_WwRichmessage_WeAppMessage_desc, desc == null ? "" : desc);
        RobotHelpers.setObjectField(weAppMessage, KeyConst.F_WwRichmessage_WeAppMessage_appName, appName == null ? title : appName);
        RobotHelpers.setObjectField(weAppMessage, KeyConst.F_WwRichmessage_WeAppMessage_thumbUrl, thumbUrl == null ? "" : thumbUrl);
        RobotHelpers.setObjectField(weAppMessage, KeyConst.F_WwRichmessage_WeAppMessage_thumbWidth, options.outWidth);
        RobotHelpers.setObjectField(weAppMessage, KeyConst.F_WwRichmessage_WeAppMessage_thumbHeight, options.outHeight);
        Class<?> clazz_IMsg$CC = RobotHelpers.findClass(KeyConst.C_MSG_CC, Global.loadPackageParam.classLoader);
        Object obj_IMsg = RobotHelpers.callStaticMethod(clazz_IMsg$CC, KeyConst.M_MSG_CC_GET);
        Object obj_buildLinkMessage = RobotHelpers.newInstance(RobotHelpers.findClass(KeyConst.C_WwRichmessageBase_LinkMessage, classLoader));
        RobotHelpers.callMethod(obj_buildLinkMessage, KeyConst.M_WwRichmessageBase_LinkMessage_setExtension,
                RobotHelpers.getStaticObjectField(RobotHelpers.findClass(KeyConst.C_LinkMessage, classLoader), KeyConst.F_LinkMessage_wEAPPMESSAGE), weAppMessage);
        Object obj_Message = RobotHelpers.callMethod(obj_IMsg, KeyConst.M_WwRichmessage_buildMessage, 78, obj_buildLinkMessage);
        return obj_Message;
    }

    public static Object buildVoiceMessage(ClassLoader classLoader, String voicePath, int secondDuration) {
        Object obj_IMsg = RobotHelpers.callStaticMethod(RobotHelpers.findClass(KeyConst.C_MSG_CC, classLoader), KeyConst.M_MSG_CC_GET);
        Object fileMessage = RobotHelpers.callMethod(obj_IMsg, KeyConst.C_MSG_CC_buildFileMessage, voicePath, 0, 0, secondDuration);
        MyLog.debug(TAG, " voiceMessage =" + JSON.toJSONString(fileMessage));
        return WeWorkMessageUtil.buildWeWorkMessage(classLoader, 9, fileMessage);
    }

    /**
     * 发送消息
     *
     * @param classLoader
     * @param objConversation
     * @param objMessage
     * @param objCallback
     */
    public static void sendMessage(ClassLoader classLoader, Object objConversation, Object objMessage, Object objCallback) {
        MyLog.debug(TAG, "sendMessage " + objMessage);
        Class clazzConversation = RobotHelpers.findClassIfExists(KeyConst.C_Conversation_NativeHandleHolder, classLoader);
        Class clazzNewMessage = RobotHelpers.findClassIfExists(KeyConst.C_MESSAGE, classLoader);
        Class clazzExtraInfo = RobotHelpers.findClassIfExists(KeyConst.C_SendExtraInfo, classLoader);
        Class clzAppStoreUtil_ISendMessageCallback = RobotHelpers.findClassIfExists(KeyConst.I_ISendMessageCallback, Global.loadPackageParam.classLoader);
        Class[] clazzArray = {Context.class, clazzConversation, clazzNewMessage, clazzExtraInfo, clzAppStoreUtil_ISendMessageCallback};
        Object[] objArray = {Global.getContext(), objConversation, objMessage, null, objCallback};
        Class clazzMessageManager = RobotHelpers.findClassIfExists(KeyConst.C_MessageManager, classLoader);
        RobotHelpers.callStaticMethod(clazzMessageManager, KeyConst.M_sendMessage, clazzArray, objArray);
    }

    /**
     * 发送消息
     *
     * @param classLoader
     * @param objConversation
     * @param objMessage
     * @param
     */
    public static void sendMessage(ClassLoader classLoader, Object objConversation, Object objMessage, MessageController.SendMessageCallBack sendMessageCallBack) {
        sendMessage(classLoader, objConversation, objMessage, ProxyUtil.GetProxyInstance(KeyConst.I_ISendMessageCallback, sendMessageCallBack));

    }


    /**
     * 私聊消息
     *
     * @param classLoader
     * @param conversationID
     * @param objMessage
     * @param sendMessageCallback
     */
    public static void sendMessagePrivate(ClassLoader classLoader, long conversationID, long userRemoteId, Object objMessage, @NonNull MessageController.SendMessageCallBack sendMessageCallback) {
        Object proxyInstance = ProxyUtil.GetProxyInstance(KeyConst.I_ISendMessageCallback, sendMessageCallback);
        ConvController.getInstance().getConversationObjectByRemoteIDAndConversationId(classLoader, conversationID, userRemoteId, false, new ConvController.GetOrCreateConversationCallBack() {
            @Override
            public void onResult(int code, Object conversationObject, String message) {
                MyLog.debug(
                        TAG,
                        "sendMessagePrivate" + " [code]" + code + "conversationObject.." + conversationObject
                );
                sendMessage(classLoader, conversationObject, objMessage, proxyInstance);
            }
        });

    }


    public static Object getSendExtraInfo(ClassLoader classLoader, long conversationID) {
        Object instance = RobotHelpers.newInstance(RobotHelpers.findClass(KeyConst.C_SendExtraInfo, classLoader));
        Object ConversationID = RobotHelpers.newInstance(RobotHelpers.findClass(KeyConst.C_ConversationID, classLoader), conversationID);
        RobotHelpers.setObjectField(instance, KeyConst.F_SendExtraInfo_ConversationID, ConversationID);
        return instance;
    }

    public static void sendFileMessage(ClassLoader classLoader, Object conversationObject, String absolutePath) {
        Object get = RobotHelpers.callStaticMethod(RobotHelpers.findClass(KeyConst.C_MSG_CC, classLoader), KeyConst.M_MSG_CC_GET);
        Class[] sendClass = new Class[]{Context.class, long.class, String.class, boolean.class,
                RobotHelpers.findClass(KeyConst.C_SendExtraInfo, classLoader)};
        long id = ConvParseUtil.getID(conversationObject);
        RobotHelpers.callMethod(get, KeyConst.M_MSG_CC_sendFileMessage, sendClass, Global.getContext(), id, absolutePath, false, null);

    }
}
