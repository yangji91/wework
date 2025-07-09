package com.robot.hook.util;

import android.content.Context;
import android.content.Intent;

import com.robot.common.MConfiger;

public class WeworkHelper {

    /**
     * 打开企业微信主界面
     */
    public static void openWework(Context context) {
        try {
//            Intent intent = new Intent();
//            ComponentName cmp = new ComponentName("com.tencent.wework", "com.tencent.wework.launch.LaunchSplashActivity");
//            intent.setAction(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setComponent(cmp);
//            context.startActivity(intent);

            //A应用直接拉起B应用
            if (context != null) {
                Intent intent = context.getPackageManager().getLaunchIntentForPackage(MConfiger.WX_ENTERPISE_PKGNAME);
                if (intent != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
//拉起B应用的某个界面，我们可以传一个type值；当然如果知道你要跳转的Activity的类名
//            Intent intent = new Intent();
//            intent.setClassName("B应用包名", "B应用包名.Activity");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void restartWework(Context context) {
        try {
            if (context != null) {
                // 使用 PackageManager 获取目标应用的启动 Intent
                Intent intent = context.getPackageManager().getLaunchIntentForPackage(MConfiger.WX_ENTERPISE_PKGNAME);
                if (intent != null) {
                    // 设置 Intent 标志以清除当前任务堆栈并重新创建 Activity
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
