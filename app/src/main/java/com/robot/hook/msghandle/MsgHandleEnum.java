package com.robot.hook.msghandle;

import com.robot.hook.msghandle.base.BaseHandleMsg;

public enum MsgHandleEnum {
    GREY(1011, "grey消息", new HandleGreyMsg()),
    //    GROUP_ADD(1002, "添加群成员群成员", new GroupGreyMsg()),
//    GROUP_REMOVE(1003, "群成员退出", new GroupGreyMsg()),
//    GROUP_RENAME(1001, "群修改名称", new GroupGreyMsg()),
//    GROUP_OWER_CHANGE(1022, "群内tips", new GroupGreyMsg()),
    IMG(101, "图片消息处理", new HandleMsgImage()),
    IMG_WEWORK(14, "图片消息处理(企信用户)", new HandleMsgImage()),
    //    FILE(102, "文件消息处理", new HandleFileMsg()),
//    FILE_SELF(8, "文件消息SELF发送", new HandleFileSelfMsgV2()),
//    FILE_MESSAGE_SELF(15, "文件消息SELF发送", new HandleFileSelfMsgV2()),
    IMG_SELF(7, "图片消息处理SELF空消息处理", new HandleMsgImage()),
    VIDEO_SELF(5, "视频消息SELF", new HandleVideoMsg()),
    //    VIDEO_MASSHELPER(23, "视频消息群发助手", new HandleVideoMsg()),
//    VIDEO_MASSHELPER__22(22, "视频消息群发助手", new HandleVideoMsg()),
//    FILE_MASSHELPER(20, "文件消息群发助手", new HandleFileSelfMsgV2()),
//    LOCATION(6, "位置信息", new HandleBlankMsg()),
//    MERGE_FORWARD(4, "合并转发", new HandleBlankMsg()),
    VIDEO(103, "其他人发送视频消息", new HandleVideoMsg()),
    VOICE(16, "语音消息处理", new HandleMsgVoice()),
    VOICE_SELF(9, "自己发送的语音消息处理", new HandleMsgVoice()),
    LINK(13, "卡片链接消息处理", new HandleMsgLink()),
    LINK_SELF(13, "自己发送的卡片链接消息处理", new HandleMsgLink()),
    //    GROUP_INVITE(1006, "你邀请微信的xxx加入外部群", new HandleBlankMsg()),
//    GROUP_INPUT_TXT(1054, "请填写群名称,方便维护和管理", new HandleBlankMsg()),
    TEXTMSG_SELF(0, "文本消息自己", new HandleTextMsg()),
    MINI_APP(78, "小程序", new HandleMiniAppMsg()),
    TextMSG(2, "文本消息", new HandleTextMsg()),
    //    CallMSG(40, "语音视频通话", new HandleCallMsg()),
//    CUSTOM_IMAGE(29, "企微自定义表情", new HandleDIYMemeImageMsg()),
//    GROUP_SEND_NOTIFY_MSG(205, "群发群消息给群确认提醒", new HandleBlankMsg()),
//    PRIVATE_SEND_NOTIFY_MSG(112, "群发消息给客户确认提醒", new HandleBlankMsg()),
    CUSTOM_WX_IMAGE(104, "个微自定义表情", new HandleDIYMemeImageMsg());

    private int type;
    private String tips;
    private BaseHandleMsg handleMsg;

    MsgHandleEnum(int type, String tips, BaseHandleMsg handleMsg) {
        this.type = type;
        this.tips = tips;
        this.handleMsg = handleMsg;
    }

    public int getType() {
        return this.type;
    }

    public String getTips() {
        return this.tips;
    }

    public BaseHandleMsg getHandleMsg() {
        return this.handleMsg;
    }

//    public static final boolean needParseFile(int contentType) {
//        return contentType == IMG.getType() || contentType == IMG_WEWORK.getType() || contentType == FILE.getType() || contentType == IMG_SELF.getType()
//                || contentType == FILE_SELF.getType() || contentType == FILE_MASSHELPER.getType() || contentType == FILE_MESSAGE_SELF.getType() || contentType == CUSTOM_IMAGE.getType() || contentType == CUSTOM_WX_IMAGE.getType();
//    }

    public static final boolean needParseFile(int contentType) {
        return contentType == IMG.getType() || contentType == IMG_WEWORK.getType() || contentType == IMG_SELF.getType();
//                || contentType == CUSTOM_WX_IMAGE.getType();
    }

    public static final MsgHandleEnum getHandleMsgByType(int type) {
        MsgHandleEnum e = null;
        for (MsgHandleEnum en : values()) {
            if (en != null && en.getType() == type) {
                e = en;
                break;
            }
        }
        return e;
    }

    public static final String getDescByTypeId(int type) {
        String e = "未找到";
        for (MsgHandleEnum en : values()) {
            if (en != null && en.getType() == type) {
                e = en.tips;
                break;
            }
        }
        return e;
    }

    public static final boolean isImgType(int contentType) {
        return contentType == MsgHandleEnum.IMG.getType() || contentType == MsgHandleEnum.IMG_SELF.getType() || contentType == MsgHandleEnum.IMG_WEWORK.getType();
    }

    //==================================================
//    public static final boolean isVideoType(int contentType) {
//        return contentType == MsgHandleEnum.VIDEO.getType() || contentType == MsgHandleEnum.VIDEO_SELF.getType()
//                || contentType == MsgHandleEnum.VIDEO_MASSHELPER.getType() || contentType == MsgHandleEnum.VIDEO_MASSHELPER__22.getType();
//    }
    public static final boolean isVideoType(int contentType) {
        return false;
    }

    public static final boolean isP2PImage(int contentType) {
        return 63 == contentType;
    }

    public static final boolean isFileIdModePic(int contentType) {
        return contentType == MsgHandleEnum.IMG_WEWORK.getType() || isFtnPic(contentType);
    }

    private static final boolean isFtnPic(int contentType) {
        return contentType == 19 || contentType == 48;
    }
}
