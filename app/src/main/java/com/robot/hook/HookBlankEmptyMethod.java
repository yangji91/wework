package com.robot.hook;

import com.robot.hook.base.HookBaseMethod;
import com.robot.hook.base.IHookCallBack;
import com.robot.robothook.LoadPackageParam;

class HookBlankEmptyMethod extends HookBaseMethod {

    @Override
    public void onHookInfo(Class clazz, LoadPackageParam loadPackageParam, IHookCallBack callBack) {

    }

    @Override
    public boolean onInvokeMethod(Class classDb, LoadPackageParam loadPackageParam, IHookCallBack paramCall) {
        return false;
    }
}
