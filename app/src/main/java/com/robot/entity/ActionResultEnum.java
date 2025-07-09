package com.robot.entity;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.Serializable;

//错误信息. 任务执行失败的时候才返回.
public enum ActionResultEnum {
    ACTION_PARAM_FAIL(80000,"参数异常","参数异常"),
    ACTION_SUCC(90000,"执行成功","执行成功"),           //这个用不上
    ACTION_INIT_FAILURE(90001,"任务初始化失败","下发任务matcher和msg中有一个为空"),
    ACTION_ACTION_REMOTE_ID_EMPTY(90002,"任务remoteId为空","remoteId==null无法查找到发送对象"),
    ACTION_CREATE_CONV_FAILRE(90003,"创建会话失败","创建会话失败"),
    ACTION_SEARCH_CONV_FAIL_MORE_THEN_ONE(90104,"匹配到多个会话结果","匹配到多个会话结果"),
    ACTION_SEARCH_CONV_FAIL(90004,"没有找指定的会话","没有找指定的会话"),
    ACTION_SEND_TXT_METHOD(90005,"发送文本接口失败","hook接口调用失败"),
    ACTION_SEND_IMG_METHOD(90006,"发送图片接口失败","hook接口调用失败"),
    ACTION_SEND_FILE_METHOD(90007,"发送文件接口失败","hook接口调用失败"),
    ACTION_SEND_VIDEO_METHOD(90008,"发送视频接口失败","hook接口调用失败"),
    ACTION_BITMAP_DECODE_FAIL(90009,"图片发送,bitmap decode失败","图片发送,bitmap decode失败"),
    ACTION_DOWNLOAD_FILE_FAIL(90010,"文件下载失败","文件发送，文件下载失败"),
    ACTION_FILE_PATH_EMPTY(90011,"文件路径为空","文件路径为空"),
    ACTION_FILE_NOT_EXIST(90012,"文件不存在","文件不存在"),
    ACTION_FILE_LEN_ZERO(90013,"文件下载失败","文件下载失败"),
    ACTION_TIME_OUT(90014,"企微接口回调超时", "企微接口回调超时"),
    ACTION_CLIENT_ERR(90015,"客户端异常","客户端初始化失败&其他异常"),
    ACTION_GROUP_NOTIFY_FAIL(90016,"群公告失败","群公告设置失败"),
    ACTION_CREATE_GROUP_ALONE_PARAM_ERR(90017,"独立创建群后端参数异常","独立创建群后端参数异常"),
    ACTION_CONTENT_TYPE_NOT_FOUND(90018,"未查找到该多媒体类型","客户端不识别该contentType类型消息"),
    ACTION_SEARCH_MSG_FAILRE(90019,"查找缓存消息失败","客户端查找Msg Cache失败"),
    ACTION_REVOKE_MSG_FAILURE(90020,"调用撤回消息失败","撤回消息失败"),
    ACTION_SEND_LINK_METHOD(90021,"发送卡片链接接口失败","hook接口调用失败"),
    ACTION_LINKED_ERR(90022,"网络链接异常","http链接异常"),
    ACTION_SEARTCH_USER_FAIL(90023,"查找外部联系人异常","查找外部联系人异常"),
    ACTION_GROUP_REMOVE_FAIL(90024,"移除群成员失败","移除群成员失败"),

    ACTION_URL_2_H5_CARD_URL_ACCESS_FAILED(90037,"url转h5卡片_指定的url不可访问","url转h5卡片_指定的url不可访问"),

    ACTION_VOICE_DOWNLOAD_FAIL(90040,"语音文件下载失败","接口调用下载语音失败"),
    DEPARTMENT_SEARCH_ALREADYINGROUP(90047,"被邀请人已在群中","被邀请人已在群中"),
    DEPARTMENT_SEARCH_FAIL(90050,"搜索接口调用失败","搜索接口调用失败"),
    DEPARTMENT_SEARCH_REPEAT(90051,"搜索结果有重复姓名","搜索结果有重复姓名"),
    DEPARTMENT_SEARCH_EMPTY(90053,"通过姓名找不到联系人","通过姓名找不到联系人"),

    ACTION_GROUP_ADD_EXTERNAL_FAIL(90061,"邀请外部成员接口调用失败","邀请外部成员接口调用失败"),
    ACTION_GROUP_VERIFY_EXTERNAL_FAIL(90063,"邀请校验用户失败","邀请校验用户失败"),

    ACTION_REPEAT(90070,"任务重复","任务重复"),
    ACTION_AUTO_JOIN_GROUP(90071,"已经接受过此邀请","自动进群"),
    ACTION_FORBIDRENAME_NOTADMIN(90072,"不是管理员设置失败"   ,"不是管理员设置失败"),
    ACTION_GROUP_SPAMRULE_SOUCH_ENPTY(90073,"没有找到指定的防骚扰规则","没有找到指定的防骚扰规则"),

    ACTION_MSG_SEARCH_FAIL(90080,"消息查找失败","转发消息查找失败"),
    ACTION_MSG_FORWARD_FAIL(90081,"调用消息转发接口失败","调用接口失败"),
    ACTION_SEND_MIMI_PROGREM(90082,"发送小程序接口失败","hook接口调用失败"),
    ACTION_RUN_ERROR(90100,"运行错误","运行错误"),
    ACTION_ROBOT_RESULT(90101,"其他原因","其他原因"),
    ACTION_ROBOT_REPEAT(90119,"操作不需要执行","操作不需要执行"),
    ACTION_WEWORK_RESULT(90110,"接口调用返回非成功状态","接口调用返回非成功状态");

    private int code;
    private String msg;
    private String desc;

    ActionResultEnum(int code, String msg, String desc){
        this.code=code;
        this.msg = msg;
        this.desc=desc;
    }

    public int getCode(){
        return this.code;
    }

    public String getMsg(){
        return getMsg(null);
    }

    public String getMsg(String tips){
        PActionResultItem pEntity = new PActionResultItem();
        pEntity.code = this.code;
        if(!TextUtils.isEmpty(tips)){
            pEntity.msg = this.msg + "[" + tips + "]";
        }else{
            pEntity.msg = this.msg;
        }
        Gson gson = new Gson();
        return gson.toJson(pEntity);
    }

    public PActionResultItem getActionResultItemEntity(){
        PActionResultItem rEntity = new PActionResultItem();
        rEntity.code = this.code;
        rEntity.msg = this.msg;
        rEntity.desc = this.desc;
        return rEntity;
    }

    public static class PActionResultItem implements Serializable {
        public int code;
        public String msg;
        public String desc;
    }
/*
    public String getDesc(){
        return this.desc;
    }
*/
}
