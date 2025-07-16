package com.robot.hook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.android.server.dexguard.IDexGuardClient;
import com.robot.common.CrashHandler;
import com.robot.common.FloatHelper;
import com.robot.common.StatsHelper;
import com.robot.com.database.AppDatabase;
import com.robot.com.database.service.RobotRunningInfoService;
import com.robot.com.receiver.ReciverManager;
import com.robot.common.Global;
import com.robot.common.MConfiger;
import com.robot.controller.ConvController;
import com.robot.controller.LoginController;
import com.robot.controller.resource.ResourceController;
import com.robot.entity.EnterpriseEntity;
import com.robot.entity.PhoneLocEnum;
import com.robot.entity.UserEntity;
import com.robot.hook.base.BaseMainXposed;
import com.robot.hook.base.HookBaseMethod;
import com.robot.hook.system.HookRenamePkgInfoMethod;
import com.robot.hook.util.WeworkHelper;
import com.robot.net.NanoHttpClient;
import com.robot.nettywss.WssNettyEngine;
import com.robot.nettywss.WssProtocalManager;
import com.robot.nettywss.listener.IWssNettyListener;
import com.robot.robothook.LoadPackageParam;
import com.robot.robothook.RobotHelpers;
import com.robot.robothook.RobotMethodHook;
import com.robot.robothook.RobotMethodParam;
import com.robot.util.FileUtil;
import com.robot.util.MyLog;
import com.robot.util.RootUtil;
import com.robot.util.StrUtils;

import java.io.IOException;
import java.util.List;

public class Main extends BaseMainXposed {

    private final int FLAG_GET_USER = 1;
    private final int FLAG_LOGOUT = 2;
    private final int FLAG_TOAST = 100;

    private boolean isUpload = false;


    @SuppressLint("StaticFieldLeak")
    public static Activity mainActivity;

    public void hook(LoadPackageParam lpparam) {
        if (lpparam == null) {
            return;
        }
        hookWeCom(lpparam);
    }


    private void hookWeCom(LoadPackageParam lpparam) {
        if (!lpparam.packageName.equals(MConfiger.WX_ENTERPISE_PKGNAME)) {
            return;
        }
        if (!lpparam.processName.equals(MConfiger.WX_ENTERPISE_PKGNAME)) {
            return;
        }
        MyLog.init();
        MyLog.debug(TAG, "[handleLoadPackage]" + " 进入到企业微信进程... proc:" + lpparam.processName);
//         new HookUnRejectWeWorkMethod().onHookInfo(lpparam );
        RobotHelpers.findAndHookMethod(Application.class, "attach", Context.class, new RobotMethodHook() {
            @Override
            protected void afterHookedMethod(RobotMethodParam param) throws Throwable {
                Global.initLoadPackageParam(lpparam);
                Context context = (Context) param.args[0];
                MyLog.debug(TAG, "[handleLoadPackage]" + " WwApplicationLike... attachBaseContext:" + lpparam.processName);
                new HookRenamePkgInfoMethod().onHookInfo(lpparam, context);
                if (MConfiger.WX_ENTERPISE_PKGNAME.equals(lpparam.processName)) {
                    initHander();
                    Global.init(context);
//                    CrashHandler.getInstance().init(context);
                    ResourceController.initCache(context);
                    hookWeWorkInfo(lpparam, true);
                    getLoginUserInfoAndConnect();
                    try {
                        new NanoHttpClient().start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    WeworkHelper.openWework(context);
                }
            }
        });
        RobotHelpers.findAndHookMethod(Activity.class, FramworkConst.M_Activity_onCreate, Bundle.class, new RobotMethodHook() {
            @Override
            protected void afterHookedMethod(RobotMethodParam RobotMethodParam) throws Throwable {
                if (MConfiger.WX_ENTERPISE_PKGNAME.equals(lpparam.processName)) {
                    try {
                        Activity activity = (Activity) RobotMethodParam.thisObject;
                        if (activity.getClass().toString().contains(KeyConst.C_LoginWxAuthActivity)) {
                            FloatHelper.init(activity);
                        }
                        if (activity.getClass().toString().contains(KeyConst.C_WwMainActivity)) {
                            ReciverManager.getInstance().regReceiver(activity);
                            mainActivity = activity;
                            if (Global.getWecomVersion().equals(MConfiger.WEWORK_VERSION_17857)) {
                                Handler handler = new Handler();
                                handler.postDelayed(() -> {//跳到我的页面
                                    RobotHelpers.callMethod(activity, KeyConst.M_WwMainActivity_mine, RobotHelpers.callMethod(activity, KeyConst.M_WwMainActivity_mine_findIndex, KeyConst.F_WwMainActivity_mine_index));
                                }, 2000);
                            }
                            FloatHelper.init(activity);
                            //保活
                            IDexGuardClient.deviceConfig().update("am.persistentPkgs", MConfiger.WX_ENTERPISE_PKGNAME + "").update("system.suDenyApps", MConfiger.WX_ENTERPISE_PKGNAME).commit();  /*注意只有commit后修改才会生效！ */
                            StatsHelper.event("msgReport", "onCreate", "activity " + activity.getClass(), "time " + StrUtils.getTimeDetailStr());
                        }
                        MyLog.debug(TAG, "[handleLoadPackage]" + " 打开了... " + activity.getClass());
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    static public Activity getMainActivity() {
        return mainActivity;
    }

    private void getLoginUserInfoAndConnect() {

        MyLog.debug(TAG, "[getLoginUserInfoAndConnect]" + Thread.currentThread().getName());

        Global.postRunnable2UI(() -> {
            MyLog.debug(TAG, "[getLoginUserInfoAndConnect]" + Thread.currentThread().getName());
            UserEntity loginUser = LoginController.getInstance().getLoginUser();
            if (loginUser != null && !TextUtils.isEmpty(loginUser.name)) {
                MyLog.debug(TAG, "[getLoginUserInfoAndConnect]" + " remoteId:" + loginUser.remoteId + " mobile:" + loginUser.mobile + " name:" + loginUser.name + " accId:" + loginUser.acctid, true);
                StatsHelper.event("msgReport", "getLoginUserInfoAndConnect", "loginUser " + loginUser, "time " + StrUtils.getTimeDetailStr());
                LoginController.getInstance().setLoginUserInfo(loginUser);
                checkSnDetail(loginUser);
                AppDatabase.getInstance().init(Global.getContext(), loginUser.remoteId);
                //启动成功
                RobotRunningInfoService.getInstance().addRebootEvent(loginUser.remoteId);
            } else {
                Message msg = Message.obtain();
                msg.what = FLAG_GET_USER;
                sendMsgDelay(msg, 1000 * 5);
            }
        });
    }

    private void checkSnDetail(final UserEntity userEntity) {
        MyLog.debug(TAG, "[checkSnDetail]" + "...");
        startWecomConnectAndDelayTask(userEntity);
    }

    private void startWecomConnectAndDelayTask(final UserEntity userEntity) {
        MyLog.debug(TAG, "[startConnectAndDelayTask]" + "...");

        // 获取企业信息
        EnterpriseEntity enterpriseEntity = LoginController.getInstance().getEnterpriseInfo();
        StatsHelper.event("msgReport", "startConnectAndDelayTask", "enterpriseEntity " + enterpriseEntity, "time " + StrUtils.getTimeDetailStr());
        Global.postNettyRunnableDelay(() -> {
            FileUtil.saveUserLoginInfo(userEntity);
            MyLog.debug(TAG, "[startConnectAndDelayTask]" + " start connnect... sn:" + Global.getDeviceSn());
            WssNettyEngine.getInstance().setINettyListener(new IWssNettyListener() {
                @Override
                public void onReady() {
                    MyLog.debug(TAG, "[startConnectAndDelayTask]" + " 鉴权成功... isUpload:" + isUpload + " userEntity:" + userEntity);
                    // 发送心跳
                    WssProtocalManager.sendHeartBeat();
                    // 发送设备信息
                    // WssProtocalManager.sendDeviceInfo(userEntity, enterpriseEntity);
                    StatsHelper.event("msgReport", "sendHeartBeat", "鉴权成功 mainActivity " + mainActivity, "time " + StrUtils.getTimeDetailStr());
                    launchMainActivity();
                    if (!isUpload) {
                        delayTask();
                        isUpload = true;
                    }
                }
            });
            WssNettyEngine.getInstance().startConnect();
        }, 100L);

    }

    private void startWechatConnectAndDelayTask() {
        MyLog.debug(TAG, "[startWechatConnectAndDelayTask]" + "...");
        Global.postNettyRunnableDelay(() -> {
            MyLog.debug(TAG, "[startWechatConnectAndDelayTask]" + " start connnect... ");
            WssNettyEngine.getInstance().setINettyListener(new IWssNettyListener() {
                @Override
                public void onReady() {
                    MyLog.debug(TAG, "[startWechatConnectAndDelayTask]");
                    WssProtocalManager.sendHeartBeat();
                }
            });
            WssNettyEngine.getInstance().startConnect();
        }, 100L);
    }

    public static void launchMainActivity() {
        if (mainActivity == null) {
            if (MConfiger.phoneLocEnum == PhoneLocEnum.BAIDU) {
                MyLog.debug("mainActivity", "百度云 启动 mainActivity =");
                RootUtil.shellExec(" dg am start " + MConfiger.WX_ENTERPISE_PKGNAME);
            } else if (MConfiger.phoneLocEnum == PhoneLocEnum.Ali) {
                MyLog.debug("mainActivity", "阿里云 启动 mainActivity =");
                RootUtil.shellExec(" dzsu am start -n 'com.tencent.wework/.launch.LaunchSplashActivity'  ");
            } else {
                MyLog.debug("mainActivity", "  启动 mainActivity =");
                RootUtil.shellExec(" am start -n 'com.tencent.wework/.launch.LaunchSplashActivity'    ");
            }
        }
    }

    private void syncOuterContact() {
        ConvController.getInstance().getOuterContact(new ConvController.GetUserCallback() {
            @Override
            public void onResult(int code, List<UserEntity> list) {
                MyLog.debug("mainActivity", "  syncOuterContact " + list.size());
//                for (UserEntity userEntity : list) {
//                    user +=" "+userEntity.toString();
//                }
//                MyLog.debug(TAG,"[outerContact] " + user,true);
//                DBController.getInstance().execSql(Global.loadPackageParam.classLoader, "select * from USER", DBController.getInstance().getSessionDBPath(Global.loadPackageParam.classLoader), new DBController.IExecSqlCallback() {
//                    @Override
//                    public void onResult(String errMsg, long execTime, List<Map<String, String>> resultMap) {
//                        String user1="";
//                        for (Map<String, String> stringStringMap : resultMap) {
//                            user1 +=" "+stringStringMap.toString();
//                        }
//                        MyLog.debug(TAG,"[outerContact1] " + user1,true);
//                    }
//                });
//                DBController.getInstance().execSql(Global.loadPackageParam.classLoader, "select * from MYHEAD", DBController.getInstance().getSessionDBPath(Global.loadPackageParam.classLoader), new DBController.IExecSqlCallback() {
//                    @Override
//                    public void onResult(String errMsg, long execTime, List<Map<String, String>> resultMap) {
//                        String user2="";
//                        for (Map<String, String> stringStringMap : resultMap) {
//                            user2 +=" "+stringStringMap.toString();
//                        }
//                        MyLog.debug(TAG,"[outerContact2] " + user2,true);
//                    }
//                });
//                NanoHttpClient.resetUserConv();
//                for (UserEntity userEntity : list) {
                WssProtocalManager.sendContactInfos(list);
//                }
//                if (list != null && list.size() > 0) {
//                    List<List> mList = SpliteUtil.splistList(list, 100);
//                    if (mList != null) {
//                        for (List ll : mList) {
//                            ProtocalManager.getInstance().sendContactInfo(ll);
//                            MyLog.debug(TAG, "[startConnect]" + " splite userList->" + ll.size());
//                        }
//                    }
//                }
            }
        });
    }

    private void synchrConversationList() {
        ConvController.getInstance().getAndSendConvInfo();
    }

    private void delayTask() {
        //15秒后上传联系人和会话信息
        MyLog.debug(TAG, "[delayTask]...10秒后同步...");
        Global.postRunnable2UIDelay(new Runnable() {
            @Override
            public void run() {
                MyLog.debug(TAG, "[delayTask]" + " run...");
                //同步外部联系人信息
                syncOuterContact();
                //同步会话列表
                synchrConversationList();
            }
        }, 1000 * 10);
    }

    private void hookWeWorkInfo(LoadPackageParam lpparam, boolean isInit) {
        if (isInit) {
            HookBaseMethod[] baseMethods = {
                    HookMethodEnum.MSG_REV.getMethod(),
//                    HookMethodEnum.LOG.getMethod(),
//                    HookMethodEnum.LOGOUT.getMethod(),
//                    HookMethodEnum.HOOK_CRASH_REPORT.getMethod(),
//                    HookMethodEnum.HOOK_MESSAGE_RECALL.getMethod(),
//                    HookMethodEnum.CONVERSATION_ADD.getMethod(),
            };
            MyLog.debug(TAG, "[handleLoadPackage]" + " 启动 注册hook函数 注册函数列表 " + baseMethods.length);


            for (HookBaseMethod m : baseMethods) {
                MyLog.debug(TAG, "[handleLoadPackage]" + " 启动 注册hook函数 " + m.getClass(), true);
                m.onHookInfo(null, lpparam, null);
            }
            MyLog.debug(TAG, "[handleLoadPackage]" + " 启动 注册hook函数 完成>>" + lpparam.processName, true);
            LoginController.getInstance().registerListener(new LoginController.ILoginListener() {
                @Override
                public void onLogout(String tips) {
                    removeMsg(FLAG_LOGOUT);
                    Message msg = Message.obtain();
                    msg.what = FLAG_LOGOUT;
                    msg.obj = tips;
                    sendMsgDelay(msg, 1000 * 8);
                }
            });
        }
    }


    @Override
    protected void handleMsg(Message msg) {
        int what = msg.what;
        switch (what) {
            case FLAG_GET_USER:
                String envStr = MConfiger.env.getDesc();
                MConfiger.mRobotStatus = 0;
                MConfiger.mRobotTips = "获取登录用户信息...\n" + envStr + "\n" + "设备标识:" + Global.getDeviceSn() + "\n" + "\n";
                FloatHelper.notifyData();
                getLoginUserInfoAndConnect();
                break;
            case FLAG_LOGOUT:
                String str = (String) msg.obj;
                MConfiger.mRobotStatus = -1;
                MConfiger.mRobotTips = "运行失败，失败原因【" + str + "】" + "\n" + "设备标识:" + Global.getDeviceSn() + "\n" + "\n";
                FloatHelper.notifyData();
                removeMsg(what);
                msg = Message.obtain();
                msg.what = FLAG_LOGOUT;
                msg.obj = str;
                sendMsgDelay(msg, 1000 * 8);
                break;
            case FLAG_TOAST:
                str = (String) msg.obj;
                MConfiger.mRobotStatus = -1;
                MConfiger.mRobotTips = str + "\n" + StrUtils.buildCommonToast(false) + "\n" + "设备标识:" + Global.getDeviceSn() + "\n" + "\n";
                FloatHelper.notifyData();
                msg = Message.obtain();
                msg.what = FLAG_TOAST;
                msg.obj = str;
                sendMsgDelay(msg, 1000 * 5);
                break;
        }
    }
}
