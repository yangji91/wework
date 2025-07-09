package com.robot.hook.msg;

/***
 *@author 
 *@date 2021/9/24
 *@description
 ****/
public class ContentType {



    public static int VOICE=16;

    public  static int LINK =13;

    public static  int MINI_APP=78;

    public static  int TEXT_SEND=0;

    public static  int TEXT_RECEIVE=2;

    public static int FILE_SEND=8;
    public static int FILE_MASSHELPER_SEND=20;
    public static int FILE_RECEIVE=102;
    public static int  FILE_MASSHELPER=15;


    public static int IMG_SEND=14;
    public static int IMG_RECEIVE=101;
    public static int  IMG_MASSHELPER=7;

    public static int VIDEO_SEND=5;
    public static int VIDEO_RECEIVE=103;
    public static int  VIDEO_MASSHELPER_SEND=22;
    public static int VIDEO_MASSHELPER_RECEIVE =23;

    public static int CUSTOM_IMAGE=29;
    public static int CUSTOM_WX_IMAGE=104;


    public static boolean isFileMessage(int contentType){
        return isImageMessage(contentType)|| isVoiceMessage(contentType)||contentType==FILE_SEND||contentType==FILE_RECEIVE||contentType==CUSTOM_IMAGE||
                contentType==CUSTOM_WX_IMAGE||contentType==FILE_MASSHELPER_SEND;
    }
    public static boolean isImageMessage(int contentType){
        return contentType==IMG_SEND||contentType==IMG_RECEIVE||contentType==IMG_MASSHELPER;
    }

    public static boolean isVideoMessage(int contentType){
        return contentType==VIDEO_SEND||contentType==VIDEO_RECEIVE||contentType==VIDEO_MASSHELPER_SEND||contentType==VIDEO_MASSHELPER_RECEIVE;
    }
    public static boolean isVoiceMessage(int contentType){
        return contentType==VOICE ;
    }

    public static boolean isLinkMessage(int contentType) {
        return contentType==LINK ;
    }
    public static boolean isMiniAppMessage(int contentType) {
        return contentType==MINI_APP;
    }
}
