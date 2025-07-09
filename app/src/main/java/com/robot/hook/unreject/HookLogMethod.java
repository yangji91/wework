package com.robot.hook.unreject;

import com.robot.robothook.RobotMethodParam;
import com.robot.hook.KeyConst;
import com.robot.hook.base.HookBaseMethod;
import com.robot.hook.base.IHookCallBack;
import com.robot.com.BuildConfig;
import com.robot.util.MyLog;
import com.robot.robothook.RobotMethodHook;

import com.robot.robothook.RobotHelpers;
import com.robot.robothook.LoadPackageParam;

/**
 * 日志系统
 */
public class HookLogMethod extends HookBaseMethod<String> {

    @Override
    public void onHookInfo(Class clazz, LoadPackageParam loadPackageParam, IHookCallBack<String> callBack) {
        Class clazzLog = RobotHelpers.findClassIfExists(KeyConst.C_Log,loadPackageParam.classLoader);
        MyLog.debug(TAG,"[onHookInfo]" + " clazzLog:" + clazzLog);
        if(clazzLog != null && BuildConfig.customConfigLog){
            RobotHelpers.hookAllMethods(clazzLog, KeyConst.M_LOG_D, new RobotMethodHook() {
                @Override
                protected void afterHookedMethod(RobotMethodParam param) throws Throwable {
                    StringBuilder builder = buildStrBuilder(param);
                   // MyLog.debug(TAG,"[onHookInfo]" + builder);
                }
            });

            RobotHelpers.hookAllMethods(clazzLog, KeyConst.M_LOG_W, new RobotMethodHook() {
                @Override
                protected void afterHookedMethod(RobotMethodParam param) throws Throwable {
                    StringBuilder builder = buildStrBuilder(param);
                   // MyLog.debug(TAG,"[onHookInfo]w" + builder);
                }
            });
        }
    }

    @Override
    public boolean onInvokeMethod(Class classDb, LoadPackageParam loadPackageParam, IHookCallBack<String> paramCall) {
        return false;
    }
}
