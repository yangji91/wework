package com.robot.util;

import com.robot.common.Global;
import com.robot.entity.FileMsgEntity;
import com.robot.hook.KeyConst;

import com.robot.robothook.RobotHelpers;

public class FileImgParseUtil {
    private static final String TAG = FileImgParseUtil.class.getSimpleName();

    public static final FileMsgEntity parseData(byte[] bytes,Object srcObj){
        FileMsgEntity fEntity = new FileMsgEntity();
        Object resultObj = null;
        if(bytes != null){
            Class[] clazzArray = {byte[].class};
            Object[] objArray = {bytes};
            Class clazz = RobotHelpers.findClassIfExists(KeyConst.C_WwRickMsg_FileMsg,Global.loadPackageParam.classLoader);
            resultObj = RobotHelpers.callStaticMethod(clazz,KeyConst.M_WwRickMsg_FileMsg_parseFrom,clazzArray,objArray);
        }else{
            resultObj = srcObj;
        }
        byte[] bs = (byte[]) RobotHelpers.getObjectField(resultObj,"url");
        String url = StrUtilsDjz_cJ(bs);

        fEntity.aesKey = (byte[]) RobotHelpers.getObjectField(resultObj,"aesKey");
        fEntity.decryptRet = RobotHelpers.getIntField(resultObj,"decryptRet");
        fEntity.encryptKey = (byte[]) RobotHelpers.getObjectField(resultObj,"encryptKey");
        fEntity.encryptSize = RobotHelpers.getLongField(resultObj,"encryptSize");
        fEntity.extra = (byte[]) RobotHelpers.getObjectField(resultObj,"extra");
        fEntity.fileId = (byte[]) RobotHelpers.getObjectField(resultObj,"fileId");
        fEntity.fileName = (byte[]) RobotHelpers.getObjectField(resultObj,"fileName");
        fEntity.flags = RobotHelpers.getIntField(resultObj,"flags");
        fEntity.height = RobotHelpers.getIntField(resultObj,"height");
        fEntity.isHd = RobotHelpers.getBooleanField(resultObj,"isHd");
        fEntity.iscomplex = RobotHelpers.getBooleanField(resultObj,"iscomplex");
        fEntity.md5 = (byte[]) RobotHelpers.getObjectField(resultObj,"md5");
        fEntity.midImgSize = RobotHelpers.getLongField(resultObj,"midImgSize");
        fEntity.midThumbnailFileId = (byte[]) RobotHelpers.getObjectField(resultObj,"midThumbnailFileId");
        fEntity.midThumbnailPath = (byte[]) RobotHelpers.getObjectField(resultObj,"midThumbnailPath");
        fEntity.randomKey = (byte[]) RobotHelpers.getObjectField(resultObj,"randomKey");
        fEntity.receiverDeviceid = (byte[]) RobotHelpers.getObjectField(resultObj,"receiverDeviceid");
        fEntity.senderDeviceid = (byte[]) RobotHelpers.getObjectField(resultObj,"senderDeviceid");
        fEntity.sessionId = (byte[]) RobotHelpers.getObjectField(resultObj,"sessionId");
        fEntity.size = RobotHelpers.getLongField(resultObj,"size");
        fEntity.thumbImgSize = RobotHelpers.getLongField(resultObj,"thumbImgSize");
        fEntity.thumbnailFileId = (byte[]) RobotHelpers.getObjectField(resultObj,"thumbnailFileId");
        fEntity.thumbnailPath = (byte[]) RobotHelpers.getObjectField(resultObj,"thumbnailPath");
        fEntity.url = (byte[]) RobotHelpers.getObjectField(resultObj,"url");
        fEntity.voiceTime = RobotHelpers.getIntField(resultObj,"voiceTime");
        fEntity.wechatAuthKey = (byte[]) RobotHelpers.getObjectField(resultObj,"wechatAuthKey");
        fEntity.wechatCdnLdAeskey = (byte[]) RobotHelpers.getObjectField(resultObj,"wechatCdnLdAeskey");
        fEntity.wechatCdnLdHeight = RobotHelpers.getIntField(resultObj,"wechatCdnLdHeight");
        fEntity.wechatCdnLdMd5 = (byte[]) RobotHelpers.getObjectField(resultObj,"wechatCdnLdMd5");
        fEntity.wechatCdnLdSize = RobotHelpers.getIntField(resultObj,"wechatCdnLdSize");
        fEntity.wechatCdnLdUrl = (byte[]) RobotHelpers.getObjectField(resultObj,"wechatCdnLdUrl");
        fEntity.wechatCdnLdWidth = RobotHelpers.getIntField(resultObj,"wechatCdnLdWidth");
        fEntity.width = RobotHelpers.getIntField(resultObj,"width");
        FileMsgEntity.OpenImCdnImg openImCdnImg = null;
        Object obj = RobotHelpers.getObjectField(resultObj,"wechatMidImage");
        MyLog.debug(TAG,"[ByteToString]" + " resultObj file:" + fEntity.fileName + " url:" + fEntity.url + " md5:" + fEntity.md5);
        if(obj != null){
            openImCdnImg = new FileMsgEntity.OpenImCdnImg();
            fEntity.wechatMidImage = openImCdnImg;
            openImCdnImg.height = RobotHelpers.getIntField(obj,"height");
            openImCdnImg.width = RobotHelpers.getIntField(obj,"width");
            FileMsgEntity.OpenImCdnUri openImCdnUri;
            Object objImgCndUri = RobotHelpers.getObjectField(obj,"imgUri");
            if(objImgCndUri != null){
                openImCdnUri = new FileMsgEntity.OpenImCdnUri();
                openImCdnUri.aeskey = (byte[]) RobotHelpers.getObjectField(objImgCndUri,"aeskey");
                openImCdnUri.authkey = (byte[]) RobotHelpers.getObjectField(objImgCndUri,"authkey");
                openImCdnUri.md5 = (byte[]) RobotHelpers.getObjectField(objImgCndUri,"md5");
                openImCdnUri.size = RobotHelpers.getIntField(objImgCndUri,"size");
                openImCdnUri.url = (byte[]) RobotHelpers.getObjectField(objImgCndUri,"url");
                openImCdnImg.imgUri = openImCdnUri;
            }
        }
        return fEntity;
    }

    public static   String StrUtilsDjz_cJ(byte[] bs){
        if (bs == null) {
            return "";
        }
        try {
            return m35196S(new String(bs, "UTF-8"), true);
        } catch (Exception e) {
           MyLog.debug("log", e.getMessage());
            return "";
        }

    }
    private static String m35196S(String str, boolean z) {
        if (str.length() < 1 || !str.contains("\r")) {
            return str;
        }
        char[] charArray = str.toCharArray();
        int i = 0;
        int i2 = 0;
        while (i < charArray.length - 1) {
            char c = charArray[i];
            int i3 = i + 1;
            char c2 = charArray[i3];
            if (charArray[i] == '\r' && (c2 == '\n' || (z && c2 == '\r'))) {
                charArray[i3] = '\n';
                charArray[i] = '\n';
                while (i < charArray.length - 1) {
                    int i4 = i + 1;
                    charArray[i] = charArray[i4];
                    i = i4;
                }
                i2++;
            } else if (c == '\r') {
                charArray[i] = '\n';
            }
            i = i3;
        }
        if ('\r' == charArray[charArray.length - 1]) {
            charArray[charArray.length - 1] = '\n';
        }
        return new String(charArray, 0, charArray.length - i2);
    }

}
