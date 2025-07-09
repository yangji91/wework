package com.robot.hook;


import com.robot.robothook.LoadPackageParam;
import com.robot.robothook.RobotHelpers;
import com.robot.robothook.RobotXposedHook;


import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class MainXposed implements de.robv.android.xposed.IXposedHookLoadPackage, IXposedHookZygoteInit {

    private Main main = new Main();

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        RobotHelpers.setHookEnvironment(new RobotXposedHook());
        main.hook(new LoadPackageParam(lpparam.packageName, lpparam.processName, lpparam.classLoader, lpparam.appInfo, lpparam.isFirstApplication));
    }

    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
    }
}
