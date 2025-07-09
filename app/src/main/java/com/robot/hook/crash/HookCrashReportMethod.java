package com.robot.hook.crash;

import com.robot.common.StatsHelper;
import com.robot.hook.KeyConst;
import com.robot.util.StrUtils;
import com.robot.robothook.RobotMethodParam;
import com.robot.entity.UserEntity;
import com.robot.hook.base.HookBaseMethod;
import com.robot.hook.base.IHookCallBack;
import com.robot.util.MyLog;

import com.robot.robothook.RobotMethodHook;

import com.robot.robothook.RobotHelpers;
import com.robot.robothook.LoadPackageParam;


public class HookCrashReportMethod extends HookBaseMethod {
    private UserEntity userEntity = null;

    public HookCrashReportMethod() {
    }

    public String getStackInfo(Throwable ex) {
        StackTraceElement[] stackElements = ex.getStackTrace();
        return getStackInfo(stackElements);
    }

    public String getStackInfo(StackTraceElement[] stackElements) {
        String retStr = "";
        if (stackElements != null) {
            for (int i = 0; i < stackElements.length; i++) {
                retStr += "[" + i + "]: 类名: " + stackElements[i].getClassName() + "----函数名:" + stackElements[i].getMethodName() + "---- 文件名: " + stackElements[i].getFileName() + "(" + stackElements[i].getLineNumber() + ")\n";
            }
        }
        return retStr;
    }

    @Override
    public void onHookInfo(final Class clazz, final LoadPackageParam loadPackageParam, IHookCallBack callBack) {

        //    public void uncaughtException(Thread thread, Throwable th) {
        /*RobotHelpers.hookAllMethods(RobotHelpers.findClassIfExists("com.tencent.mm.sdk.platformtools.MMUncaughtExceptionHandler", loadPackageParam.classLoader), "uncaughtException", new MethodHook() {
            @Override
            protected void afterHookedMethod(MethodParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object[] params = param.args;
                uncaughtException(params);
            }
        });*/
        RobotHelpers.hookAllMethods(RobotHelpers.findClassIfExists(KeyConst.C_bugly_e, loadPackageParam.classLoader), KeyConst.uncaughtException, new RobotMethodHook() {
            @Override
            protected void afterHookedMethod(RobotMethodParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object[] params = param.args;
                uncaughtException(params);
            }
        });
        MyLog.debug(TAG, " crash.e");
        RobotHelpers.hookAllMethods(RobotHelpers.findClassIfExists(KeyConst.C_i2e, loadPackageParam.classLoader), KeyConst.uncaughtException, new RobotMethodHook() {
            @Override
            protected void afterHookedMethod(RobotMethodParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object[] params = param.args;
                uncaughtException(params);
            }
        });
        RobotHelpers.hookAllMethods(RobotHelpers.findClassIfExists(KeyConst.C_WxaJvmCrashHandler, loadPackageParam.classLoader), KeyConst.uncaughtException, new RobotMethodHook() {
            @Override
            protected void afterHookedMethod(RobotMethodParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object[] params = param.args;
                uncaughtException(params);
            }
        });
        MyLog.debug(TAG, " WxaJvmCrashHandler");
    }

    private void uncaughtException(Object[] params) {
        if (params != null && params.length >= 2) {
            Thread tempThread = (Thread) params[0];
            Throwable tempThrowable = (Throwable) params[1];
            String carshInfo = "=========================================================\r\n";
            if (null == tempThread) {
                carshInfo += "线程信息: null\r\n";
            } else {
                carshInfo += "线程id: " + tempThread.getId() + "\r\n";
                carshInfo += "线程名字: " + tempThread.getName() + "\r\n";
                carshInfo += "线程调用堆栈: " + getStackInfo(tempThread.getStackTrace()) + "\r\n";
            }

            if (null == tempThrowable) {
                carshInfo += "异常信息: null" + "\r\n";
            } else {
                carshInfo += "异常信息: " + tempThrowable.getMessage() + "\r\n";
                carshInfo += "异常调用堆栈信息: " + getStackInfo(tempThrowable) + "\r\n";
            }
            carshInfo += "=========================================================\r\n";
            MyLog.debug(TAG, carshInfo, true);
            StatsHelper.event("CrashHandler", "uncaughtException", "carshInfo " + carshInfo, "time " + StrUtils.getTimeDetailStr());
        }
    }

    @Override
    public boolean onInvokeMethod(Class classDb, LoadPackageParam loadPackageParam, IHookCallBack paramCall) {
        return false;
    }

}
