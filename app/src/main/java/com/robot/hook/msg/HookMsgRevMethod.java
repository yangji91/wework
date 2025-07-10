package com.robot.hook.msg;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.robot.entity.ConvTypeEnum;
import com.robot.nettywss.WssProtocalManager;
import com.robot.util.StrUtils;
import com.robot.robothook.RobotMethodParam;
import com.robot.common.Global;
import com.robot.controller.ConvController;
import com.robot.entity.MsgEntity;
import com.robot.entity.ResEntity;
import com.robot.hook.KeyConst;
import com.robot.hook.base.HookBaseMethod;
import com.robot.hook.base.IHookCallBack;
import com.robot.hook.msghandle.MsgHandleEnum;
import com.robot.hook.msghandle.base.BaseHandleMsg;
import com.robot.com.BuildConfig;
import com.robot.com.database.service.WeWorkMessageService;
import com.robot.util.MsgParseUtil;
import com.robot.util.MyLog;

import com.robot.robothook.RobotMethodHook;

import com.robot.robothook.RobotHelpers;
import com.robot.robothook.LoadPackageParam;

//接收消息监控 消息入口
public class HookMsgRevMethod extends HookBaseMethod {

    public HookMsgRevMethod() {
    }

    @Override
    public void onHookInfo(final Class clazz, final LoadPackageParam loadPackageParam, IHookCallBack callBack) {
        Class clazzJiI = RobotHelpers.findClassIfExists(KeyConst.C_ConversationEngine_IConversationObserver, loadPackageParam.classLoader);
        RobotHelpers.hookAllMethods(clazzJiI, KeyConst.M_ConversationEngine_IConversationObserver_onAddMessages, new RobotMethodHook() {
            @Override
            protected void afterHookedMethod(RobotMethodParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object[] params = param.args;
                if (params != null && params.length >= 2) {
                    Object[] msgs = (Object[]) params[1];
                    if (Global.SystemStartReceiveTime == 0) {
                        Global.SystemStartReceiveTime = System.currentTimeMillis();
                    }
                    onAddMessage(msgs, loadPackageParam);
                }
            }
        });

    }

    public void onAddMessage(Object[] msgs, LoadPackageParam loadPackageParam) {
        for (Object msg : msgs) {
            WeWorkMessageService.getInstance().save(msg);
            Global.postRunnableDownLoadImg(new Runnable() {
                @Override
                public void run() {
                    MsgEntity entity = MsgParseUtil.parseMsgEntity(msg);
                    if (WeWorkMessageService.getInstance().hasSameMsgById(entity.msgId)) {
                        return;
                    }
                    WeWorkMessageService.getInstance().mHasAddMessageIdList.add(entity.msgId);
                    entity.originMsg = msg;
                    MyLog.debug(TAG, "[onHookInfo]" + "  MsgEntity ->" + StrUtils.objectToJson(entity));
//                    MyLog.debug(TAG, "[onHookInfo]" + " 原始msg " + JSON.toJSONString(msg));
//                    ConvController.getInstance().addMsg2Cache(entity.id, msg);
                    /*if (entity.contentType==112||entity.contentType==205){
                        MassHelperController.performMassSend(entity.content,entity.contentType, new ProxyUtil.ProxyStringResultCallBack() {
                            @Override
                            public void onResult(int i, String str) {
                                MyLog.debug(TAG, "群发消息 "+i+str);
                            }
                        });
                    }*/
                    handleMsg(entity, loadPackageParam);
                }
            });

        }
    }

    @Override
    public boolean onInvokeMethod(Class classDb, LoadPackageParam loadPackageParam, IHookCallBack paramCall) {
        return false;
    }

    public void handleMsg(MsgEntity entity, LoadPackageParam loadPackageParam) {
        if (entity.convType == 1) {
            return;
        }
        //然后处理：rList列表中包含所有消息 此处处理的消息包含 需要下载的和不需要下载的
        //todo 循环赋值 entity.realRemark
        MsgHandleEnum msgEnum = MsgHandleEnum.getHandleMsgByType(entity.contentType);
        MyLog.debug(TAG, "[onHookInfo]" + " contentType:" + entity.contentType + " content:" + entity.content + " imageEntity:" + entity.fileMsgEntity + " convId:" + entity.conversationId + " remoteId:" + entity.remoteId);
        if (msgEnum != null) {
            BaseHandleMsg baseHandleMsg = msgEnum.getHandleMsg();
            if (baseHandleMsg != null) {      //二次处理消息
                MyLog.debug(TAG, "[onHookInfo]" + "处理消息类型:" + msgEnum.getType() + " tips:" + msgEnum.getTips() + " entity " + StrUtils.objectToJson(entity));
                baseHandleMsg.onHandleMsg(loadPackageParam, entity);
            }
        } else {
//            WssProtocalManager.sendMsgEntity(entity, "otherMsg");
            MyLog.debug(TAG, "[onHookInfo]" + "未查找到该消息类型... e:" + msgEnum);
        }
        if (BuildConfig.customConfigLog) {
            if (!TextUtils.isEmpty(entity.content)) {
//                HookTestEnum hookTestEnum = HookTestEnum.getEnumByKey(entity.content);
//                if (hookTestEnum != null) {
//                    MyLog.debug(TAG, "[handleMsg]" + " handle " + hookTestEnum.getContent());
//                    HookBaseMethod hookBaseMethod = hookTestEnum.getMethod();
//                    hookBaseMethod.onInvokeMethod(null, loadPackageParam, new IHookCallBack<String>() {
//                        @Override
//                        public void onCall(ResEntity resEntity) {
//                        }
//
//                        @Override
//                        public String getParams() {
//                            return entity.content;
//                        }
//                    });
//                }
            }
        }

    }
}
