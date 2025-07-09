package com.robot.robothook;

import com.dzone.Fm;
import com.dzone.MethodHook;
import com.dzone.MethodParam;

import java.lang.reflect.Member;

public class RobotFMHook implements RobotHook {
    @Override
    public void hookMethod(Member m, RobotMethodHook callback) {
        Fm.hookMethod(m, new MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodParam methodParam) throws Throwable {
                super.beforeHookedMethod(methodParam);
                if (callback!=null) callback.beforeHookedMethod(new RobotMethodParam(methodParam,methodParam.args,methodParam.thisObject,methodParam.method));
                // if (callback!=null) callback.beforeHookedMethod(new RobotMethodParam(methodParam ));
            }

            @Override
            protected void afterHookedMethod(MethodParam methodParam) throws Throwable {
                super.afterHookedMethod(methodParam);
                // if (callback!=null) callback.afterHookedMethod(new RobotMethodParam(methodParam));
                if (callback!=null) callback.afterHookedMethod(new RobotMethodParam(methodParam,methodParam.args,methodParam.thisObject,methodParam.method));
            }
        });
    }

    @Override
    public void hookAllConstructors(Class<?> hookClass, RobotMethodHook callback) {
        Fm.hookAllConstructors(hookClass, new MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodParam methodParam) throws Throwable {
                super.beforeHookedMethod(methodParam);
                if (callback!=null) callback.beforeHookedMethod(new RobotMethodParam(methodParam,methodParam.args,methodParam.thisObject,methodParam.method));
            }

            @Override
            protected void afterHookedMethod(MethodParam methodParam) throws Throwable {
                super.afterHookedMethod(methodParam);
                if (callback!=null) callback.afterHookedMethod(new RobotMethodParam(methodParam,methodParam.args,methodParam.thisObject,methodParam.method));
            }
        });
    }
}
