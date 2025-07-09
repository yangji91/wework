package com.robot.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Pair;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

public class CommonUtil {
    private static final String COM_ROBOT_PATH = "robot/com";
    private static final String PATH_FILE_HEART = "heart.txt";
    public static String PATH_FILE_WECOM_HEART = "wecom_heart.txt";
    public static String PATH_FILE_WECHAT_HEART = "wechat_heart.txt";
    private static final String PACKAGE_NAME_WOWORK = "com.tencent.wowork";

    public static int getVersionCode(Context context) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        return packageInfo.versionCode;
    }

    public static String getNativePhoneNumber(Context context) throws SecurityException {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String nativePhoneNumber = tm.getLine1Number();
        return nativePhoneNumber;
    }

    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context) throws SecurityException {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (tm == null) {
            return null;
        }

        return tm.getDeviceId();
    }

    @SuppressLint("MissingPermission")
    public static String getMeid(Context context) throws SecurityException {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (tm == null) {
            return null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return tm.getMeid();
        } else {
            return null;
        }
    }

    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context) throws SecurityException {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (tm == null) {
            return null;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return tm.getDeviceId();
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            try {
                Class<?> clazz = Class.forName("android.os.SystemProperties");
                Method method = clazz.getMethod("get", String.class, String.class);
                method.setAccessible(true);
                String gsm = (String) method.invoke(null, "ril.gsm.imei", "");

                if (!TextUtils.isEmpty(gsm)) {
                    String[] imeiArray = gsm.split(",");

                    if (imeiArray != null && imeiArray.length > 0) {
                        return imeiArray[0];
                    } else {
                        return tm.getDeviceId(0);
                    }
                } else {
                    return tm.getDeviceId(0);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return tm.getImei(0);
        }
    }

    public static void startWoWork(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(PACKAGE_NAME_WOWORK);
        context.getApplicationContext().startActivity(intent);
    }

    public static Pair<Boolean, String> getWoWorkInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> info = packageManager.getInstalledPackages(0);

        if (info == null || info.isEmpty()) {
            return new Pair<>(false, null);
        }

        for (int i = 0; i < info.size(); i++) {
            if (PACKAGE_NAME_WOWORK.equals(info.get(i).packageName)) {
                return new Pair<>(true, info.get(i).versionName);
            }
        }

        return new Pair<>(false, null);
    }

    public static boolean checkWoWorkIsInstall(Context context) {
        return checkApkIsInstall(context, PACKAGE_NAME_WOWORK);
    }

    private static boolean checkApkIsInstall(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }

        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> info = packageManager.getInstalledPackages(0);

        if (info == null || info.isEmpty()) {
            return false;
        }

        for (int i = 0; i < info.size(); i++) {
            if (packageName.equals(info.get(i).packageName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取心跳时间
     */
    public static long getHeartBeatTime() throws IOException {
        File robotDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), COM_ROBOT_PATH);
        File heartBeatFile = new File(robotDir, PATH_FILE_HEART);

        if (!heartBeatFile.exists()) {
            return 0;
        }

        String fileContent = FileUtils.readFileToString(heartBeatFile, "UTF-8");

        if (TextUtils.isEmpty(fileContent)) {
           // throw new RuntimeException("heartBeat file is empty");
            return 0;
        }

        fileContent = fileContent.trim();

        if (!TextUtils.isDigitsOnly(fileContent)) {
            throw new RuntimeException("heartBeat file format incorrect");
        }

        return Long.parseLong(fileContent);
    }
}
