package com.robot.util;

import com.robot.common.Global;
import com.robot.entity.VideoMessage;
import com.robot.hook.KeyConst;

import com.robot.robothook.RobotHelpers;

/***
 *@author 
 *@date 2021/7/26
 *@description
 ****/
public class VoiceParseUtil {

    public static final VideoMessage parseVideMessage(byte[] bytes){
        VideoMessage videoMessage = null;
        Class[] clazzArray = {byte[].class};
        Object[] objArray = {bytes};
        Class clazz = RobotHelpers.findClassIfExists(KeyConst.C_MESSAGE_VIDEO, Global.loadPackageParam.classLoader);
        Object obj = RobotHelpers.callStaticMethod(clazz,KeyConst.M_MESSAGE_VIDEO_parseFrom,clazzArray,objArray);
        videoMessage = new VideoMessage();
        videoMessage.aesKey = StrUtils.byteToUTFStr((byte[]) RobotHelpers.getObjectField(obj,"aesKey"));
        videoMessage.decryptRet = RobotHelpers.getIntField(obj,"decryptRet");
        videoMessage.encryptSize = RobotHelpers.getLongField(obj,"encryptSize");
        videoMessage.encryptKey = StrUtils.byteToUTFStr((byte[]) RobotHelpers.getObjectField(obj,"encryptKey"));
        videoMessage.flags = RobotHelpers.getIntField(obj,"flags");
        videoMessage.md5 = StrUtils.byteToUTFStr((byte[]) RobotHelpers.getObjectField(obj,"md5"));
        videoMessage.previewImgAesKey = StrUtils.byteToUTFStr((byte[]) RobotHelpers.getObjectField(obj,"previewImgAesKey"));
        videoMessage.previewImgMd5 = StrUtils.byteToUTFStr((byte[]) RobotHelpers.getObjectField(obj,"previewImgMd5"));
        videoMessage.previewImgSize = RobotHelpers.getIntField(obj,"previewImgSize");
        videoMessage.previewImgUrl = StrUtils.byteToUTFStr((byte[]) RobotHelpers.getObjectField(obj,"previewImgUrl"));
        videoMessage.randomKey = StrUtils.byteToUTFStr((byte[]) RobotHelpers.getObjectField(obj,"randomKey"));
        videoMessage.rawUploadDir = StrUtils.byteToUTFStr((byte[]) RobotHelpers.getObjectField(obj,"rawUploadDir"));
        videoMessage.rawUrl = StrUtils.byteToUTFStr((byte[]) RobotHelpers.getObjectField(obj,"rawUrl"));
        videoMessage.sessionId =  StrUtils.byteToUTFStr((byte[]) RobotHelpers.getObjectField(obj,"sessionId"));
        videoMessage.size = RobotHelpers.getLongField(obj,"size");
        videoMessage.thumbnailFileId = StrUtils.byteToUTFStr((byte[]) RobotHelpers.getObjectField(obj,"thumbnailFileId"));
        videoMessage.url = StrUtils.byteToUTFStr((byte[]) RobotHelpers.getObjectField(obj,"url"));
        videoMessage.videoDuration = RobotHelpers.getIntField(obj,"videoDuration");
        videoMessage.videoHeight = RobotHelpers.getIntField(obj,"videoHeight");
        videoMessage.videoId = StrUtils.byteToUTFStr((byte[]) RobotHelpers.getObjectField(obj,"videoId"));
        videoMessage.wechatAuthKey = StrUtils.byteToUTFStr((byte[]) RobotHelpers.getObjectField(obj,"wechatAuthKey"));
        return videoMessage;
    }
}
