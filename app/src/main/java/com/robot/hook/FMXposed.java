//package com.robot.hook;
//
//import com.dzone.Entry;
//import com.robot.util.MyLog;
//import com.robot.robothook.LoadPackageParam;
//import com.robot.robothook.RobotFMHook;
//import com.robot.robothook.RobotHelpers;
//
///**
// * 阿里云手机环境
// */
//@Entry
//public class FMXposed {
//
//    private Main main =new Main();
//    @Entry
//    public void main(com.dzone.LoadPackageParam lpparam) throws Throwable {
//        MyLog.debug("FMXposed", lpparam.packageName);
//        RobotHelpers.setHookEnvironment(new RobotFMHook());
//        MyLog.debug("FMXposed", lpparam.processName);
//        main.hook(new LoadPackageParam(lpparam.packageName,lpparam.processName,lpparam.classLoader,lpparam.appInfo,lpparam.isFirstApplication));
//    }
//}
