package com.robot.common;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.robot.util.StrUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * 自定义全局异常捕获类
 *
 * @author Alex
 */
public class CrashHandler implements UncaughtExceptionHandler {

    private Context mContext;
    private static volatile CrashHandler crashHandler;
    private UncaughtExceptionHandler mDefaultHandler;

    // 获取CrashHandler实例 单例模式 - 双重校验锁
    public static CrashHandler getInstance() {
        if (crashHandler == null) {
            synchronized (CrashHandler.class) {
                if (crashHandler == null) {
                    crashHandler = new CrashHandler();
                }
            }
        }
        return crashHandler;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        if (context != null) {
            new Handler(context.getMainLooper()).post(() -> {
                while (true) {
                    try {
                        Looper.loop();//try-catch主线程的所有异常；Looper.loop()内部是一个死循环，出现异常时才会退出，所以这里使用while(true)。
                    } catch (Throwable e) {
                        e.printStackTrace();
                        StatsHelper.event("CrashHandler", "handleExceptionLoop", "throwable " + e + " stackTrace " + getStackTrace(e), "time " + StrUtils.getTimeDetailStr());
                    }
                }
            });
            mContext = context;
            // 静态方法，获取系统默认的UncaughtException处理器，对所有线程都有效
            mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            // 设置该CrashHandler为程序的默认处理器
            Thread.setDefaultUncaughtExceptionHandler(this);
        }
    }

    public static String getStackTrace(Throwable e) {
        try {
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);
            String stackTraceStr = result.toString();
            return stackTraceStr;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return "";
        }
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable e) {
        StatsHelper.event("CrashHandler", "handleException", "throwable " + e, "time " + StrUtils.getTimeDetailStr());
    }
}