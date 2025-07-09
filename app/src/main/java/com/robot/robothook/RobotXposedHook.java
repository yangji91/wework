package com.robot.robothook;

import java.lang.reflect.Member;

public class RobotXposedHook implements RobotHook {


    public void hookMethod(Member m, RobotMethodHook callback) {
        de.robv.android.xposed.XposedBridge.hookMethod(m, new  de.robv.android.xposed.XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam methodParam) throws Throwable {
                super.beforeHookedMethod(methodParam);
                if (callback!=null) callback.beforeHookedMethod(new RobotMethodParam(methodParam,methodParam.args,methodParam.thisObject,methodParam.method));
            }
            @Override
            protected void afterHookedMethod(MethodHookParam methodParam) throws Throwable {
                super.afterHookedMethod(methodParam);
                if (callback!=null) callback.afterHookedMethod(new RobotMethodParam(methodParam,methodParam.args,methodParam.thisObject,methodParam.method));
            }
        });
    }



    public void hookAllConstructors(Class<?> hookClass, RobotMethodHook callback) {
        de.robv.android.xposed.XposedBridge.hookAllConstructors(hookClass, new de.robv.android.xposed.XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam methodParam) throws Throwable {
                super.beforeHookedMethod(methodParam);
                if (callback!=null) callback.beforeHookedMethod(new RobotMethodParam(methodParam,methodParam.args,methodParam.thisObject,methodParam.method));
            }

            @Override
            protected void afterHookedMethod(MethodHookParam methodParam) throws Throwable {
                super.afterHookedMethod(methodParam);
                if (callback!=null) callback.beforeHookedMethod(new RobotMethodParam(methodParam,methodParam.args,methodParam.thisObject,methodParam.method));
            }
        });
    }
}
