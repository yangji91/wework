package com.robot.util;

import com.robot.common.Global;
import com.robot.entity.WeAppMsgEntity;
import com.robot.hook.KeyConst;

import com.robot.robothook.RobotHelpers;

import static com.robot.hook.KeyConst.C_WwRichmessageBase_LinkMessage;

public class WeAppParseUtil
{
    private static final String TAG = WeAppParseUtil.class.getSimpleName();

    public static final WeAppMsgEntity parseData(byte[] bytes)
    {
        WeAppMsgEntity fEntity = new WeAppMsgEntity();
        Class[] clazzArray = {byte[].class};
        Object[] objArray = {bytes};
        Class clazzLinkMsg = RobotHelpers.findClassIfExists(C_WwRichmessageBase_LinkMessage, Global.loadPackageParam.classLoader);
        Object linkMsgObj = RobotHelpers.callStaticMethod(clazzLinkMsg, KeyConst.M_parseFrom, clazzArray, objArray);
        Class clazzISO = RobotHelpers.findClassIfExists(KeyConst.C_LinkMessageUtil,Global.loadPackageParam.classLoader);
        if(linkMsgObj != null){
            Class[] clazzArray1 = {clazzLinkMsg};
            Object[] objArray1 = {linkMsgObj};
            Object weAppMsgObj = RobotHelpers.callStaticMethod(clazzISO,KeyConst.M_LinkMessageUtil_getWeAppMessage,clazzArray1,objArray1);
            if(weAppMsgObj != null){
                fEntity.appMediaUrl = (String) RobotHelpers.getObjectField(weAppMsgObj, KeyConst.F_LinkMessageUtil_getWeAppMessage_appMediaUrl);
                fEntity.appName = (String) RobotHelpers.getObjectField(weAppMsgObj, KeyConst.F_LinkMessageUtil_getWeAppMessage_appName);
                fEntity.appid = (String) RobotHelpers.getObjectField(weAppMsgObj, KeyConst.F_LinkMessageUtil_getWeAppMessage_appid);
                fEntity.appservicetype = RobotHelpers.getIntField(weAppMsgObj, KeyConst.F_LinkMessageUtil_getWeAppMessage_appservicetype);
                fEntity.desc = (String) RobotHelpers.getObjectField(weAppMsgObj, KeyConst.F_LinkMessageUtil_getWeAppMessage_desc);

                fEntity.pagepath = (String) RobotHelpers.getObjectField(weAppMsgObj, KeyConst.F_LinkMessageUtil_getWeAppMessage_pagepath);
                fEntity.pkginfoType = RobotHelpers.getIntField(weAppMsgObj, KeyConst.F_LinkMessageUtil_getWeAppMessage_pkginfoType);
                fEntity.shareId = (String) RobotHelpers.getObjectField(weAppMsgObj, KeyConst.F_LinkMessageUtil_getWeAppMessage_shareId);
                fEntity.shareKey = (String) RobotHelpers.getObjectField(weAppMsgObj, KeyConst.F_LinkMessageUtil_getWeAppMessage_shareKey);
                fEntity.shareName = (String) RobotHelpers.getObjectField(weAppMsgObj, KeyConst.F_LinkMessageUtil_getWeAppMessage_shareName);

                fEntity.thumbAESKey = (String) RobotHelpers.getObjectField(weAppMsgObj, KeyConst.F_LinkMessageUtil_getWeAppMessage_thumbAESKey);
                fEntity.thumbFileId = (String) RobotHelpers.getObjectField(weAppMsgObj, KeyConst.F_LinkMessageUtil_getWeAppMessage_thumbFileId);
                fEntity.thumbHeight = RobotHelpers.getIntField(weAppMsgObj, KeyConst.F_LinkMessageUtil_getWeAppMessage_thumbHeight);
                fEntity.thumbMD5 = (String) RobotHelpers.getObjectField(weAppMsgObj, KeyConst.F_LinkMessageUtil_getWeAppMessage_thumbMD5);
                fEntity.thumbSize = RobotHelpers.getLongField(weAppMsgObj, KeyConst.F_LinkMessageUtil_getWeAppMessage_thumbSize);

                fEntity.thumbUrl = (String) RobotHelpers.getObjectField(weAppMsgObj, KeyConst.F_LinkMessageUtil_getWeAppMessage_thumbUrl);
                fEntity.thumbWidth = RobotHelpers.getIntField(weAppMsgObj, KeyConst.F_LinkMessageUtil_getWeAppMessage_thumbWidth);
                fEntity.title = (String) RobotHelpers.getObjectField(weAppMsgObj, KeyConst.F_LinkMessageUtil_getWeAppMessage_title);
                fEntity.type = RobotHelpers.getIntField(weAppMsgObj, KeyConst.F_LinkMessageUtil_getWeAppMessage_type);
                fEntity.username = (String) RobotHelpers.getObjectField(weAppMsgObj, KeyConst.F_LinkMessageUtil_getWeAppMessage_username);

                fEntity.version = RobotHelpers.getIntField(weAppMsgObj, KeyConst.F_LinkMessageUtil_getWeAppMessage_version);
                fEntity.weappIconUrl = (String) RobotHelpers.getObjectField(weAppMsgObj, KeyConst.F_LinkMessageUtil_getWeAppMessage_weappIconUrl);

                WeAppMsgEntity.OpenImCdnImg openImCdnImg = null;
                Object obj = RobotHelpers.getObjectField(weAppMsgObj, KeyConst.F_LinkMessageUtil_getWeAppMessage_wechatThumb);
                MyLog.debug(TAG,"[parseData]" + " obj->" + obj);
                if (obj != null){
                    openImCdnImg = new WeAppMsgEntity.OpenImCdnImg();
                    fEntity.wechatThumb = openImCdnImg;
                    openImCdnImg.height = RobotHelpers.getIntField(obj, KeyConst.F_LinkMessageUtil_getWeAppMessage_wechatThumb_height);
                    openImCdnImg.width = RobotHelpers.getIntField(obj, KeyConst.F_LinkMessageUtil_getWeAppMessage_wechatThumb_width);
                    WeAppMsgEntity.OpenImCdnUri openImCdnUri;
                    Object objImgCndUri = RobotHelpers.getObjectField(obj, KeyConst.F_LinkMessageUtil_getWeAppMessage_wechatThumb_imgUri);
                    if (objImgCndUri != null) {
                        openImCdnUri = new WeAppMsgEntity.OpenImCdnUri();
                        openImCdnUri.aeskey = (byte[]) RobotHelpers.getObjectField(objImgCndUri, KeyConst.F_LinkMessageUtil_getWeAppMessage_wechatThumb_imgUri_aeskey);
                        openImCdnUri.authkey = (byte[]) RobotHelpers.getObjectField(objImgCndUri, KeyConst.F_LinkMessageUtil_getWeAppMessage_wechatThumb_imgUri_authkey);
                        openImCdnUri.md5 = (byte[]) RobotHelpers.getObjectField(objImgCndUri, KeyConst.F_LinkMessageUtil_getWeAppMessage_wechatThumb_imgUri_md5);
                        openImCdnUri.size = RobotHelpers.getIntField(objImgCndUri, KeyConst.F_LinkMessageUtil_getWeAppMessage_wechatThumb_imgUri_size);
                        openImCdnUri.url = (byte[]) RobotHelpers.getObjectField(objImgCndUri, KeyConst.F_LinkMessageUtil_getWeAppMessage_wechatThumb_imgUri_url);
                        openImCdnImg.imgUri = openImCdnUri;
                    }
                }
            }
        }
        return fEntity;
    }

}
