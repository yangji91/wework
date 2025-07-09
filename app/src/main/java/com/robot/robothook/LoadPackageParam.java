package com.robot.robothook;

import android.content.pm.ApplicationInfo;

public class LoadPackageParam {

    public String packageName;

    public volatile String processName;

    public ClassLoader classLoader;

    public ApplicationInfo appInfo;

    public boolean isFirstApplication;

    public LoadPackageParam(Object loadPackageParam) {
        packageName = (String) RobotHelpers.getObjectField(loadPackageParam, "packageName");
        processName = (String) RobotHelpers.getObjectField(loadPackageParam, "processName");
        classLoader = (ClassLoader) RobotHelpers.getObjectField(loadPackageParam, "classLoader");
        appInfo = (ApplicationInfo) RobotHelpers.getObjectField(loadPackageParam, "appInfo");
        isFirstApplication = RobotHelpers.getBooleanField(loadPackageParam, "isFirstApplication");

    }

    public LoadPackageParam(String packageName, String processName, ClassLoader classLoader, ApplicationInfo appInfo, boolean isFirstApplication) {
        this.packageName = packageName;
        this.processName = processName;
        this.classLoader = classLoader;
        this.appInfo = appInfo;
        this.isFirstApplication = isFirstApplication;

    }
}
