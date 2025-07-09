package com.robot.com;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;

import com.robot.common.Global;
import com.robot.common.MConfiger;
import com.robot.entity.PhoneLocEnum;
import com.robot.util.FileUtil;
import com.robot.util.MyLog;

import java.util.Iterator;

public class MApp extends Application {
    private static MApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
//        BaseLibrary.init(this, Config.APP_NAME, false, "");
//        try {
//            if (BuildConfig.build_env == 1) {
//                BaseAppInfoConfig.init(Config.APP_KEY, Config.APP_NAME_CHINESE, Config.HOST_TEST, "test");
//                AppBaseLayoutConfig.initAppParams(Config.UPGRADE_HOST_TEST, Config.APP_PRODUCT_ID, Config.APP_PRODUCT_NAME, null, null, getString(R.string.app_name));
//            } else {
//                BaseAppInfoConfig.init(Config.APP_KEY, Config.APP_NAME_CHINESE, Config.HOST_PROD, "prod");
//                AppBaseLayoutConfig.initAppParams(Config.UPGRADE_HOST_PROD, Config.APP_PRODUCT_ID, Config.APP_PRODUCT_NAME, null, null, getString(R.string.app_name));
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//        YrLogger.registerLogger(new BehaviourLogger(YrConfig.getBehaviourLogPath(), null));
        Global.init( getApplicationContext());
//        String baiduCode = FileUtil.getBaiduPhoneCode();
//        String huaweiCode = FileUtil.getHuaWeiYunCode();
//        if (!TextUtils.isEmpty(baiduCode)) {
//            MConfiger.phoneLocEnum = PhoneLocEnum.BAIDU;
//        } else if (!TextUtils.isEmpty(huaweiCode)) {
//            MConfiger.phoneLocEnum = PhoneLocEnum.HUAWEI;
//        } else {
        MConfiger.phoneLocEnum = PhoneLocEnum.LOCAL;
//        }
//        initMainProcess();
        MyLog.debug("MApp", "onCreate  time" + Thread.currentThread().getName(), true);
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onTerminate() {
//        PhoneUtils.unRegOutCallReceiver(this);
        super.onTerminate();
    }

    /**
     * 在应用主进程才初始化的操作。
     */
//    private void initMainProcess() {
//        try {
//            String procName = Utils.getCurrentProcessName(this);
//            if (Utils.isStringEquals(this.getPackageName(), procName)) {
//                //启动优化 初始化sdk
////                PhoneUtils.regOutCallReceiver(this);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public static String getCurrentProcessName(Context context) {
        int pid = Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService("activity");
        Iterator var3 = mActivityManager.getRunningAppProcesses().iterator();

        ActivityManager.RunningAppProcessInfo appProcess;
        do {
            if (!var3.hasNext()) {
                return null;
            }

            appProcess = (ActivityManager.RunningAppProcessInfo) var3.next();
        } while (appProcess.pid != pid);

        return appProcess.processName;
    }
}
