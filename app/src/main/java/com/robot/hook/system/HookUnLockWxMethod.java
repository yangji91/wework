package com.robot.hook.system;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.robot.hook.FramworkConst;
import com.robot.hook.KeyConst;
import com.robot.robothook.RobotMethodParam;
import com.robot.hook.base.HookBaseMethod;
import com.robot.hook.base.IHookCallBack;
import com.robot.util.MyLog;

import com.robot.robothook.RobotMethodHook;
import com.robot.robothook.RobotHelpers;
import com.robot.robothook.LoadPackageParam;

public class HookUnLockWxMethod extends HookBaseMethod {

    @Override
    public void onHookInfo(Class classDb, LoadPackageParam loadPackageParam, IHookCallBack callBack) {
        Class<?> clazz = RobotHelpers.findClassIfExists(KeyConst.C_WwMainActivity,loadPackageParam.classLoader);
        if(clazz != null){
            RobotHelpers.findAndHookMethod(clazz, FramworkConst.M_Activity_onCreate, Bundle.class, new RobotMethodHook() {
                @Override
                protected void beforeHookedMethod(RobotMethodParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    //开启
                    if(param != null && param.thisObject instanceof  Activity){
                        Activity mAc = (Activity) param.thisObject;
                        if(mAc.getClass().getName().contains(KeyConst.C_WwMainActivity)){
                            mAc.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                            MyLog.debug(TAG,"[onHookInfo]" + " unLock生效...");
                        }
                    }
                }
            });
        }
    }

    @Override
    public boolean onInvokeMethod(Class classDb, LoadPackageParam loadPackageParam, IHookCallBack paramCall) {
        return false;
    }
}
