package com.robot.hook.system;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Process;

import com.robot.hook.FramworkConst;
import com.robot.robothook.LoadPackageParam;
import com.robot.robothook.RobotHelpers;
import com.robot.robothook.RobotMethodHook;
import com.robot.robothook.RobotMethodParam;
import com.robot.util.MyLog;

public class HookRenamePkgInfoMethod {
    protected final String XPOSED_PKGNAME = "de.robv.android.xposed.installer";
    protected final String XPOSED_PKGNAME_START = "de.robv.android.xposed";
    protected final String X_INSTALLER = "com.pyler.xinstaller";

    /**
     * PackageManager packageManager = context.getPackageManager();
     * * PackageInfo packageInfo = packageManager.getPackageInfo(
     * * 	            packageName, PackageManager.GET_ACTIVITIES);
     * * 	            for (ActivityInfo activity : packageInfo.activities) {
     * * 		String acn = activity.name; //activity名称
     * * 	    Class clazz= Class.forName(acn);
     * * }
     * * ---------------------
     * * 作者：无风之翼
     * * 来源：CSDN
     * * 原文：https://blog.csdn.net/peachs885090/article/details/86137890
     * * 版权声明：本文为博主原创文章，转载请附上博文链接！
     *
     * @param loadPackageParam
     * @param
     */

    public void onHookInfo(LoadPackageParam loadPackageParam, Context context) {
        //开始伪装
        RobotHelpers.findAndHookMethod(context.getPackageManager().getClass(), FramworkConst.M_PackageManager_getPackageInfo, String.class, int.class, new RobotMethodHook() {
            @Override
            protected void afterHookedMethod(RobotMethodParam param) throws Throwable {
                StringBuilder builder = new StringBuilder();
                Object[] objs = param.args;
                for (Object obj : objs) {
                    builder.append("arg:").append(obj).append("\t");
                }
                if (builder.toString().contains(XPOSED_PKGNAME) || builder.toString().contains(X_INSTALLER) || builder.toString().contains("com.robot.com") || builder.toString().contains("posed") || builder.toString().contains("com.tsng.hidemyapplist")) {
                    param.setResult(null);
                }
                MyLog.debug("HookRenamePkgInfoMethod", "[getPackageInfo]" + " builder:" + builder + " myPid:" + Process.myPid() + " myUid :" + Process.myUid());
            }
        });
    }

    public void Test(Context context) {
        PackageManager packageManager = context.getPackageManager();
        StringBuilder builder = new StringBuilder();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(XPOSED_PKGNAME, PackageManager.GET_RECEIVERS);
            if (packageInfo != null && packageInfo.receivers != null) {
                for (ActivityInfo acInfo : packageInfo.receivers) {
                    builder.append("xactivityName:" + acInfo.name + "\t");
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        MyLog.debug("HookRenamePkgInfoMethod", "[onHookInfo]" + " builder->" + builder);
    }
}
