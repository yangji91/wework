package com.robot.util;

import android.content.Context;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.UUID;

public class DeviceIdUtil {
    private static final String TAG = "DeviceIdUtil";
    private static final String SETTING_NAME = "android_robot_setting";
    private static final File mDeviceIdFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "robot/com/files/device_id");

    private static void saveToFile(String deviceId) {
        try {
            if (!mDeviceIdFile.getParentFile().exists()) {
                mDeviceIdFile.getParentFile().mkdirs();
            }

            FileUtils.write(mDeviceIdFile, deviceId, "UTF-8");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private static String getFromFile() {
        try {
            return FileUtils.readFileToString(mDeviceIdFile, "UTF-8");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return "";
    }

    private static void saveToSharedPreferences(Context context, String deviceId) {
        SharedPreferencesUtil.putString(context, SharedPreferencesUtil.NAME, SharedPreferencesUtil.KEY_DEVICE_ID, deviceId);
    }

    private static String getFromSharedPreferences(Context context) {
        return SharedPreferencesUtil.getString(context, SharedPreferencesUtil.NAME, SharedPreferencesUtil.KEY_DEVICE_ID, "");
    }

    private static void saveToGlobal(Context context, String deviceId) {
        try {
            Settings.System.putString(context.getContentResolver(), SETTING_NAME, deviceId);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private static String getFromGlobal(Context context) {
        try {
            return Settings.System.getString(context.getContentResolver(), SETTING_NAME);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return "";
    }

    private static void saveToAll(Context context, String deviceId) {
        saveToGlobal(context, deviceId);
        saveToFile(deviceId);
        saveToSharedPreferences(context, deviceId);
    }

    private static String getDeviceIdInner(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId = tm.getDeviceId();

            if (!TextUtils.isEmpty(deviceId) && !deviceId.substring(0, 3).equals("000")) {
                return deviceId;
            }
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return "";
    }

    public static String getDeviceId(Context context) {
        String deviceId = getFromGlobal(context);

        if (!TextUtils.isEmpty(deviceId)) {
            saveToFile(deviceId);
            saveToSharedPreferences(context, deviceId);
            return deviceId;
        }

        deviceId = getFromFile();

        if (!TextUtils.isEmpty(deviceId)) {
            saveToGlobal(context, deviceId);
            saveToSharedPreferences(context, deviceId);
            return deviceId;
        }

        deviceId = getFromSharedPreferences(context);

        if (!TextUtils.isEmpty(deviceId)) {
            saveToGlobal(context, deviceId);
            saveToFile(deviceId);
            return deviceId;
        }

        deviceId = getDeviceIdInner(context);

        if (!TextUtils.isEmpty(deviceId)) {
            saveToAll(context, deviceId);
            return deviceId;
        }

        deviceId = UUID.randomUUID().toString();
        saveToAll(context, deviceId);
        return deviceId;
    }
}
