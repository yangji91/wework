package com.robot.util;

import com.robot.common.Global;
import com.robot.entity.LinkMsgEntity;

import com.robot.hook.KeyConst;
import com.robot.robothook.RobotHelpers;

import static com.robot.hook.KeyConst.C_WwRichmessageBase_LinkMessage;

public class LinkMsgParseUtil {
    private static final String TAG = LinkMsgParseUtil.class.getSimpleName();

    public static final LinkMsgEntity parseData(byte[] bytes){
        LinkMsgEntity linkEntity = new LinkMsgEntity();
        Class[] clazzArray = {byte[].class};
        Object[] objArray = {bytes};
        Class clazz = RobotHelpers.findClassIfExists(C_WwRichmessageBase_LinkMessage, Global.loadPackageParam.classLoader);
        if(clazz != null){
            Object resultObj = RobotHelpers.callStaticMethod(clazz, KeyConst.M_WwRichmessageBase_LinkMessage_parseFrom,clazzArray,objArray);
            linkEntity.cmd = RobotHelpers.getIntField(resultObj,KeyConst.F_WwRichmessageBase_LinkMessage_parseFrom_cmd);
            linkEntity.description = FileImgParseUtil.StrUtilsDjz_cJ((byte[]) RobotHelpers.getObjectField(resultObj,KeyConst.F_WwRichmessageBase_LinkMessage_parseFrom_description));
            linkEntity.imageData = FileImgParseUtil.StrUtilsDjz_cJ((byte[]) RobotHelpers.getObjectField(resultObj,KeyConst.F_WwRichmessageBase_LinkMessage_parseFrom_imageData));
            linkEntity.imageUrl = FileImgParseUtil.StrUtilsDjz_cJ((byte[]) RobotHelpers.getObjectField(resultObj,KeyConst.F_WwRichmessageBase_LinkMessage_parseFrom_imageUrl));
            linkEntity.linkUrl = FileImgParseUtil.StrUtilsDjz_cJ((byte[]) RobotHelpers.getObjectField(resultObj,KeyConst.F_WwRichmessageBase_LinkMessage_parseFrom_linkUrl));
            linkEntity.title = FileImgParseUtil.StrUtilsDjz_cJ((byte[]) RobotHelpers.getObjectField(resultObj,KeyConst.F_WwRichmessageBase_LinkMessage_parseFrom_title));
            LinkMsgEntity.OpenImCdnUri openImCdnUri = linkEntity.openImageUri;
            Object obj = RobotHelpers.getObjectField(resultObj,KeyConst.F_WwRichmessageBase_LinkMessage_parseFrom_openImageUri);
            if(obj != null){
                openImCdnUri.aeskey = (byte[]) RobotHelpers.getObjectField(obj,KeyConst.F_WwRichmessageBase_LinkMessage_parseFrom_aeskey);
                openImCdnUri.authkey = (byte[]) RobotHelpers.getObjectField(obj,KeyConst.F_WwRichmessageBase_LinkMessage_parseFrom_authkey);
                openImCdnUri.md5 = (byte[]) RobotHelpers.getObjectField(obj,KeyConst.F_WwRichmessageBase_LinkMessage_parseFrom_md5);
                openImCdnUri.size = RobotHelpers.getIntField(obj,KeyConst.F_WwRichmessageBase_LinkMessage_parseFrom_size);
                openImCdnUri.url = (byte[]) RobotHelpers.getObjectField(obj,KeyConst.F_WwRichmessageBase_LinkMessage_parseFrom_url);
            }
        }
        return linkEntity;
    }
}
