package com.robot.hook;


import com.robot.hook.base.HookBaseMethod;
import com.robot.hook.conv.HookConvListMethod;
import com.robot.hook.conv.HookConvMethod;
import com.robot.hook.crash.HookCrashReportMethod;
import com.robot.hook.enterprise.HookEnterpriseMethod;
import com.robot.hook.imageloader.HookFileDownloadMethod;
import com.robot.hook.imageloader.HookImageLoaderSelfMethod;
import com.robot.hook.imageloader.HookVoiceLoaderMethod;
import com.robot.hook.login.HookLoginMethod;
import com.robot.hook.login.HookLogoutMethod;
import com.robot.hook.msg.HookMsgRevMethod;
import com.robot.hook.msg.HookReCallMsgMethod;
import com.robot.hook.system.HookUnLockWxMethod;
import com.robot.hook.unreject.HookLogMethod;
import com.robot.netty.handle.imple.contact.HookDeleteOuterFriendMethod;

public enum HookMethodEnum {
    //反注入
    HOOK_UNREJECT_WXWORK("unreject_wxwork", "反注入", new HookBlankEmptyMethod()),//HookUnRejectWeWorkMethod
    HOOKUNREFJECT_SYS("unreject_sys", "系统反注入", null),    //HookUnRejectMethod
    MSG_REV("MSG_REV", "消息接收", new HookMsgRevMethod()),                       //HookMsgRevMethod
    CONV_LIST("ConvList", "会话列表", new HookConvListMethod()),                  //HookConvListMethod
    ENTERPRISE_INFO("enterprise", "企业信息", new HookEnterpriseMethod()),        //HookEnterpriseMethod
    File_LOADER("ImageLoader", "文件加载", new HookFileDownloadMethod()),         //HookFileDownloadMethod
    IMG_LOADER_SELF("ImageLoaderSelf", "图片加载SELF", new HookImageLoaderSelfMethod()),    //HookImageLoaderSelfMethod
    VOICE_LOADER("voice_loader", "语音加载", new HookVoiceLoaderMethod()),        //HookVoiceLoaderMethod
    CONVERSATION_ADD("conversation_add", "会话列表", new HookConvMethod()),      //HookConvMethod
    DELETE_OUTER_FRIEND("delete_outer_friend", "删除外部联系人", new HookDeleteOuterFriendMethod()),                               //HookGroupMemChangedMethod

    HOOK_CRASH_REPORT("HOOK_CRASH_REPORT", "崩溃转储", new HookCrashReportMethod()),                       //HookMsgRevMethod

    LOG("log", "日志系统", new HookLogMethod()),                                   //HookLogMethod
    UNLOCK("HookUnLockWxMethod", "禁止屏幕锁屏", new HookUnLockWxMethod()),        //HookUnLockWxMethod
    LOGOUT("LoginOut", "退登录", new HookLogoutMethod()),                           //HookLogoutMethod
    LOGIN_INFO("LoginUserInfo", "用户登录信息", new HookLoginMethod()),            //HookLoginMethod
    HOOK_MESSAGE_RECALL("message_recall", "消息主动撤回", HookReCallMsgMethod.getInstance());


    private String methodName;
    private String tips;
    private HookBaseMethod method;

    HookMethodEnum(String methodName, String tips, HookBaseMethod hookDBContactMethod) {
        this.methodName = methodName;
        this.method = hookDBContactMethod;
        this.tips = tips;
    }

    public HookBaseMethod getMethod() {
        return method;
    }

    public String getName() {
        return this.methodName;
    }

    public String getTips() {
        return this.tips;
    }
}
