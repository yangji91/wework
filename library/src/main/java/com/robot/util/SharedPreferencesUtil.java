package com.robot.util;

import android.content.Context;

public class SharedPreferencesUtil {
    public static final String NAME = "device_info";
    public static final String KEY_DEVICE_ID = "device_id";

    public static boolean hasKey(Context context, String name, String key) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).contains(key);
    }

    public static boolean getBoolean(Context context, String name, String key, boolean defValue) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    public static String getString(Context context, String name, String key, String defValue) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).getString(key, defValue);
    }

    public static int getInt(Context context, String name, String key, int defValue) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).getInt(key, defValue);
    }

    public static long getLong(Context context, String name, String key, long defValue) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).getLong(key, defValue);
    }

    public static boolean putBoolean(Context context, String name, String key, boolean value) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }

    public static boolean putString(Context context, String name, String key, String value) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    public static boolean putInt(Context context, String name, String key, int value) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putInt(key, value).commit();
    }

    public static boolean putLong(Context context, String name, String key, long value) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putLong(key, value).commit();
    }
}
