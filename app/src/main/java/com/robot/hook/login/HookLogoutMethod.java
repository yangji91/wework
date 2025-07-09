package com.robot.hook.login;

import com.robot.common.StatsHelper;
import com.robot.common.Global;
import com.robot.controller.LoginController;
import com.robot.entity.ParamLogoutEntity;
import com.robot.entity.ResEntity;
import com.robot.entity.UserEntity;
import com.robot.hook.KeyConst;
import com.robot.hook.base.HookBaseMethod;
import com.robot.hook.base.IHookCallBack;
import com.robot.nettywss.WssNettyEngine;
import com.robot.robothook.LoadPackageParam;
import com.robot.robothook.RobotHelpers;
import com.robot.robothook.RobotMethodHook;
import com.robot.robothook.RobotMethodParam;
import com.robot.util.MyLog;

/***
 * 退登录
 */
public class HookLogoutMethod extends HookBaseMethod<ParamLogoutEntity> {

    public long loginRemoteId = 0;

    @Override
    public void onHookInfo(Class clazz, LoadPackageParam loadPackageParam, IHookCallBack<ParamLogoutEntity> callBack) {


        //logout
        Class clazzAccHelper = RobotHelpers.findClassIfExists(KeyConst.C_AttendanceApiImpl, loadPackageParam.classLoader);
        MyLog.debug(TAG, "[onHookInfo]" + "...clazzAccHelper:" + clazzAccHelper);
        RobotHelpers.hookAllMethods(clazzAccHelper, KeyConst.M_AttendanceApiImpl_onLogout, new RobotMethodHook() {
            @Override
            protected void afterHookedMethod(RobotMethodParam param) throws Throwable {
                MyLog.debug(TAG, "[onHookInfo]" + "退出登录事件...");
                StringBuilder builder = buildStrBuilder(param);
                MyLog.debug(TAG, "[afterHookedMethod]" + "logout... builder:" + builder);
                onLogout("账号被退出登录", null, false);
            }
        });
        RobotHelpers.hookAllMethods(RobotHelpers.findClass(KeyConst.C_mbi, loadPackageParam.classLoader), KeyConst.M_mbi_onTPFEvent, new RobotMethodHook() {
            @Override
            protected void afterHookedMethod(RobotMethodParam param) throws Throwable {
                if (param.args.length == 5) {
                    String event = param.args[0] + "";
                    int msgCode = (int) param.args[1];
                    if (KeyConst.P_mbi_onTPFEvent_event.equals(event)) {
                        MyLog.debug(TAG, "[onHookInfo]" + " 登录账户..." + msgCode);
                        switch (msgCode) {
                            case 4:
                            case 1:
                                if (loginRemoteId == 0) {
                                    loginRemoteId = LoginController.getInstance().getLoginUserId();
                                } else if (loginRemoteId != LoginController.getInstance().getLoginUserId()) {
                                    MyLog.debug(TAG, "[onHookInfo]" + "切换登录账户...", true);
                                    onLogout("切换登录账户", null, false);
                                }
                                MyLog.debug(TAG, "[onHookInfo]" + "账户登录成功...", true);
                                break;
                            case 3:
                                MyLog.debug(TAG, "[onHookInfo]" + "账户在其他设备登录...", true);
                                onLogout("账户在其他设备登录", null, false);
                                break;
                            case 2:
                                MyLog.debug(TAG, "[onHookInfo]" + "账户退出登录...", true);
                                onLogout("账户退出登录", null, false);
                                break;
                            /* case 15:

                                break;*/
                        }


                    }
                }


            }
        });
    }

    public void onLogout(String tips, IHookCallBack callBack, boolean isBan) {
        MyLog.debug(TAG, "[onLogout]" + "...");
        UserEntity userEntity = LoginController.getInstance().getLoginUserEntity();
//        if (userEntity != null) {
//            if (!isBan) {
//                ProtocalManager.getInstance().deviceLogout(userEntity);
//            } else {
//                ProtocalManager.getInstance().deviceAccountBan(userEntity);
//            }
//        }
        Global.postNettyRunnableDelay(() -> {
            LoginController.getInstance().setLoginUserInfo(null);
            MyLog.debug(TAG, "[onHookInfo]" + "断开链接...");
            WssNettyEngine.getInstance().closeConnect();
            LoginController.getInstance().logout(tips);
            if (callBack != null) {
                callBack.onCall(ResEntity.genSucc(null));
            }
        }, 5000);
        StatsHelper.event("msgReport", "onLogout", "isBan " + isBan, "userEntity " + userEntity);
    }

    @Override
    public boolean onInvokeMethod(Class classDb, LoadPackageParam loadPackageParam, IHookCallBack<ParamLogoutEntity> paramCall) {
        if (paramCall != null) {
            ParamLogoutEntity paramEntity = paramCall.getParams();
            String tips = paramEntity.msg;
            boolean isBan = paramEntity.isBan;
            onLogout(tips, paramCall, isBan);
        }
        return false;
    }
}
