package com.robot.util;

import android.util.Log;

import androidx.annotation.Nullable;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.robot.common.Global;
import com.robot.com.BuildConfig;


public class MyLog {

    public static void init() {
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return isDebug();
            }
        });
    }

    public static void debug(String TAG, String info) {
        if (!BuildConfig.customConfigLog) {
            return;
        }
        Logger.t("HOOK-" +  "WeCom" + ":" + TAG);
        Logger.d(info);
    }

    public static final boolean isDebug() {
        return BuildConfig.customConfigLog;
    }


    public static final void error(String TAG, Throwable throwable) {
        if (throwable != null) {
            Log.e(TAG, throwable.getMessage());
        }
    }

    public static final void showCurrentThread(String TAG) {
        Thread thread = Thread.currentThread();
        MyLog.debug(TAG, "[showCurrentThread]" + " thread name:" + thread.getName() + " thread id:" + thread.getId());
    }

    public static void debug(String TAG, String info, boolean isSaveFile) {
        debug(TAG, info);
        /*if (new File("/sdcard/__SKFLY").exists())
        {
            try
            {
                Class<?> tempClz = Class.forName("com.skfly.utility.android.SLog");
                Method tempM = tempClz.getDeclaredMethod("d", String.class, int.class);
                tempM.invoke(tempClz, info, 2);
                return;
            }
            catch (Exception e)
            {
                //e.printStackTrace();
            }
            return;
        }*/
        info = "时间:" + StrUtils.getTimeDetailStr() + "   " + info;
        if (isSaveFile) {
           // todo
//            String finalInfo = info;
//            Global.postRunnableLog(() ->
//            {
//                //文件操作
//                FileUtil.logInfo(TAG, finalInfo);
//            });
        }
    }

    /**
     * 打印堆栈信息
     *
     * @param throwable
     * @return
     */
    public static String getThrowableTask(Throwable throwable) {
        StackTraceElement[] stackElements = throwable.getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append(" 错误堆栈 : cause " + throwable.getCause()).append(" +++ message " + throwable.getMessage() + "  ------\n");
        if (stackElements != null) {
            for (int i = 0; i < stackElements.length; i++) {
                sb.append(stackElements[i].getClassName() + "\t");
                sb.append(stackElements[i].getFileName() + "\t");
                sb.append(stackElements[i].getLineNumber() + "\t");
                sb.append(stackElements[i].getMethodName());
                sb.append("--------\n");
            }
        }
        return sb.toString();
    }
}


