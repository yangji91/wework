package com.robot.hook.conv;

import com.alibaba.fastjson.JSON;
import com.robot.nettywss.WssProtocalManager;
import com.robot.robothook.RobotMethodParam;
import com.robot.common.Global;
import com.robot.controller.ConvController;
import com.robot.entity.ConvEntity;
import com.robot.entity.ConvTypeEnum;
import com.robot.entity.UserEntity;
import com.robot.hook.KeyConst;
import com.robot.hook.base.HookBaseMethod;
import com.robot.hook.base.IHookCallBack;
import com.robot.hook.util.ConvParseUtil;
import com.robot.com.BuildConfig;
import com.robot.util.MyLog;
import com.robot.util.ProxyUtil;
import com.robot.util.StrUtils;
import com.robot.util.UserParseUtil;

import java.util.ArrayList;
import java.util.List;

import com.robot.robothook.RobotMethodHook;

import com.robot.robothook.RobotHelpers;
import com.robot.robothook.LoadPackageParam;

/***
 * 获取会话列表
 */
public class HookConvMethod extends HookBaseMethod<List<ConvEntity>> {

    //key:6721591726792879964 item:convType:0 localId:6721591726792879964 remoteId:7881301586909984 serviceId:0 name:billywen whole:false flag:0 members:1
    //entity:autoMarkRead:false createTime:1564992493 creatorId:1688853040217346 exited:false forwardLeaveMsg:[B@82958f7 hidden:false id:6721591576469024589 isStickied:false modifyTime:0 createTime:1564992493 name:闻怀腾 notified:false remoteId:1688853040217346 searchTime:0 type:0 updateMember:false
    //entity:autoMarkRead:false createTime:1565164562 creatorId:1688853040217346 exited:false forwardLeaveMsg:[B@82958f7 hidden:false id:6721591726792879964 isStickied:false modifyTime:1565164562 createTime:1565164562 name:billywen notified:false remoteId:7881301586909984 searchTime:1565148488 type:0 updateMember:false
    //entity:autoMarkRead:false createTime:1568106223 creatorId:1688853040217346 exited:false forwardLeaveMsg: hidden:false id:6738691362845999611 isStickied:false modifyTime:1568106223 createTime:1568106223 name:闻怀腾
    //Id:1688853040217346 exited:false forwardLeaveMsg: hidden:false id:6738691362845999611 isStickied:false modifyTime:1568106223 createTime:1568106223 name:闻怀腾 notified:false remoteId:1688853040217346 searchTime:1568106223 type:0 updateMember:false
    //onvItemEntity:convType:0 localId:6738691362845999617 remoteId:7881301586909984 serviceId:0 name:billywen whole:false flag:0 members:1

    private final int FLAG_SEND = 0x10;
    private IHookCallBack<List<ConvEntity>> callBack;
    private static long curTime = 0;
    private static long startTime = 0l;

    public HookConvMethod() {
        startTime = System.currentTimeMillis();
    }

    public static final void reSet() {
        curTime = System.currentTimeMillis();
    }

    private void handleCallback(RobotMethodParam param, IHookCallBack<List<ConvEntity>> callBack) {
        Object[] objs = param.args;
        List<ConvEntity> rList = new ArrayList<>();
        if (objs != null && objs.length > 0) {
            Object[] array = (Object[]) objs[0];
            int size = array.length;
            for (int i = 0; i < size; i++) {
                Object cItem = array[i];
                ConvEntity rEntity = ConvParseUtil.parseConvEntity(cItem);
                rList.add(rEntity);
                ConvTypeEnum convTypeEnum = ConvTypeEnum.getEnumByType(rEntity.type);
                if (convTypeEnum == ConvTypeEnum.TYPE_PERSON) {
                    //判断是否是增量用户
                    long remoteId = rEntity.remoteId;
                    int userSize = Global.getUserSize();
                    long t = System.currentTimeMillis();
                    long data = Math.abs(t - startTime);

                    MyLog.debug(TAG, "[beforeHookedMethod]" + " remoteId:" + remoteId + " userSize:" + userSize + " time:" + data);

                    if (userSize > 0 || data >= StrUtils.nm * 10) { //10分钟后自动
                        Object userObj = Global.getUserObject(remoteId);
                        if (BuildConfig.customConfigLog) {
                            MyLog.debug(TAG, "[beforeHookedMethod]" + " 已查找到该user:" + userObj);
                        }
                        if (userObj == null) {
                            //开始同步增量user信息到服务端
                            Global.postRunnable2UIDelay(() -> sychrContactInfo(rEntity), 1000 * 3);
                        }
                    }
                }
//                else if (convTypeEnum == ConvTypeEnum.TYPE_GROUP) {
//                    ConvController.getInstance().put(rEntity.remoteId, rEntity.id, rEntity.id);
//                    //开始同步增量群会话信息到服务端
//                    Global.postRunnable2UIDelay(() -> WssProtocalManager.sendAddConvInfo(rEntity), 1000 * 3);
//                }
                MyLog.debug(TAG, "[beforeHookedMethod]" + " entity:" + rEntity);
            }
            MyLog.debug(TAG, "[beforeHookedMethod] onAddConversations" + " size:" + size);
            MyLog.debug(TAG, "[hookInfo]" + " conversation list.size->" + rList.size());
//            ConvController.getInstance().sendConvInfoList(rList);
        }
    }

    @Override
    public void onHookInfo(Class clazz, LoadPackageParam loadPackageParam, IHookCallBack<List<ConvEntity>> callBack) {
        this.callBack = callBack;
        MyLog.debug(TAG, "[onHookInfo]" + " C_ConversationEngine_IConversationListObserver start...");
        Class clazzfja$30 = RobotHelpers.findClass(KeyConst.C_ConversationEngine_IConversationListObserver, loadPackageParam.classLoader);
        RobotHelpers.hookAllMethods(clazzfja$30, KeyConst.M_ConversationEngine_IConversationListObserver_onAddConversations, new RobotMethodHook() {
            @Override
            protected void beforeHookedMethod(RobotMethodParam param) throws Throwable {
                super.beforeHookedMethod(param);
                handleCallback(param, callBack);
            }
        });

    }


//    private Handler mHandler = new Handler(Global.getLooper()) {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            int what = msg.what;
//            switch (what) {
//                case FLAG_SEND:
//                    List<Object> list = (List<Object>) msg.obj;
//                    List<ConvEntity> rList = new ArrayList<>();
//                    for (Object obj : list) {
//                        Object objConversation = RobotHelpers.getObjectField(obj, KeyConst.F_ConversationItem_mConversation);
//                        if (objConversation != null) {
//                            ConvEntity rEntity = ConvParseUtil.parseConvEntity(objConversation);
//                                MyLog.debug(TAG, "[afterHookedMethod]" + " rEntity -> " + rEntity);
//                            rList.add(rEntity);
//                        }
//                    }
//                    if (callBack != null) {
//                        ResEntity resEntity = ResEntity.genSucc(rList);
//                        callBack.onCall(resEntity);
//                    }
//                    break;
//            }
//        }
//    };

    @Override
    public boolean onInvokeMethod(Class classDb, LoadPackageParam loadPackageParam, IHookCallBack paramCall) {
        return false;
    }


    /**
     * 同步增量外部联系人信息
     *
     * @param convEntity
     */
    private void sychrContactInfo(ConvEntity convEntity) {
        MyLog.debug(TAG, "[sychrContactInfo]" + " convEntity id:" + convEntity.id + "convEntity.remoteId" + convEntity.remoteId);
        ConvController.getInstance().getUserByIdWithConversation(Global.loadPackageParam.classLoader, convEntity.id, convEntity.remoteId, new ProxyUtil.ProxyUserResultCallBack() {
            @Override
            public void onResult(int i, Object[] userObj) {
                MyLog.debug(TAG, "[sychrContactInfo]" + " code:" + i + "user =" + JSON.toJSONString(userObj));
                if (i == 0) {
                    UserEntity userEntity = UserParseUtil.parseUserModel(userObj[0]);
//                    List<UserEntity> userList = new ArrayList<>();
                    MyLog.debug(TAG, "[sychrContactInfo]" + " userEntity:" + userEntity);
//                    userList.add(userEntity);
                    Global.setUserObject(userEntity.remoteId, userObj[0]);
                    WssProtocalManager.sendAddContactInfo(userEntity);
//                    ProtocalManager.getInstance().sendContactInfo(userList);
                } else {
                    Global.postRunnable2UI(new Runnable() {
                        @Override
                        public void run() {
                            ConvController.getInstance().refreshUserObjectInMainThread(Global.loadPackageParam.classLoader, new long[]{convEntity.remoteId}, convEntity.id, new ProxyUtil.ProxyUserResultCallBack() {
                                @Override
                                public void onResult(int i, Object[] userObj) {
                                    if (i == 0) {
                                        UserEntity userEntity = UserParseUtil.parseUserModel(userObj[0]);
//                                        List<UserEntity> userList = new ArrayList<>();
                                        MyLog.debug(TAG, "[sychrContactInfo]" + "refreshUserObjectInMainThread userEntity:" + userEntity);
//                                        userList.add(userEntity);
                                        WssProtocalManager.sendAddContactInfo(userEntity);
//                                        ProtocalManager.getInstance().sendContactInfo(userList);
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}
