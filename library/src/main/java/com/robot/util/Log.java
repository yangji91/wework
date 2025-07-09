package com.robot.util;

import java.util.Map;

public abstract class Log {

    public static void d(String tag, String msg) {
        android.util.Log.d(tag, msg);
    }

    public static void d(String tag, String msg, boolean isNeedUpload, Map<String, String> paramMap) {
        android.util.Log.d(tag, msg);


    }

    public static void i(String tag, String msg) {
        i(tag, msg, false, true);
    }

    public static void i(String tag, String msg, Throwable tr) {
        i(tag, msg, tr, false, true);
    }

    public static void i(String tag, String msg, boolean isNeedUpload, boolean isNeedPrint) {
        if (isNeedPrint) {
            android.util.Log.i(tag, msg);
        }


    }

    public static void i(String tag, String msg, boolean isNeedUpload, boolean isNeedPrint, Map<String, String> paramMap) {
        if (isNeedPrint) {
            android.util.Log.i(tag, msg);
        }


    }

    public static void i(String tag, String subTag, String msg, boolean isNeedUpload, boolean isNeedPrint) {
        if (isNeedPrint) {
            android.util.Log.i(tag, msg);
        }


    }

    public static void i(String tag, String subTag, String msg, boolean isNeedUpload, boolean isNeedPrint, Map<String, String> paramMap) {
        if (isNeedPrint) {
            android.util.Log.i(tag, msg);
        }


    }

    public static void i(String tag, String msg, Throwable tr, boolean isNeedUpload, boolean isNeedPrint) {
        if (isNeedPrint) {
            android.util.Log.i(tag, msg, tr);
        }

    }

    public static void i(String tag, String msg, Throwable tr, boolean isNeedUpload, boolean isNeedPrint, Map<String, String> paramMap) {
        if (isNeedPrint) {
            android.util.Log.i(tag, msg, tr);
        }


    }

    public static void w(String tag, String msg) {
        w(tag, msg, true);
    }

    public static void w(String tag, String msg, Map<String, String> paramMap) {
        w(tag, msg, true, paramMap);
    }

    public static void w(String tag, String msg, boolean isNeedUpload) {
        android.util.Log.w(tag, msg);


    }

    public static void w(String tag, String msg, boolean isNeedUpload, Map<String, String> paramMap) {
        android.util.Log.w(tag, msg);


    }

    public static void e(String tag, String msg) {
        e(tag, msg, true);
    }

    public static void e(String tag, String msg, Map<String, String> paramMap) {
        e(tag, msg, true, paramMap);
    }

    public static void e(String tag, String msg, Throwable tr) {
        e(tag, msg, tr, true);
    }

    public static void e(String tag, String msg, Throwable tr, Map<String, String> paramMap) {
        e(tag, msg, tr, true, paramMap);
    }

    public static void e(String tag, String subTag, String msg, Throwable tr) {
        e(tag, subTag, msg, tr, true);
    }

    public static void e(String tag, String subTag, String msg, Throwable tr, Map<String, String> paramMap) {
        e(tag, subTag, msg, tr, true, paramMap);
    }

    public static void e(String tag, String msg, boolean isNeedUpload) {
        android.util.Log.e(tag, msg);


    }

    public static void e(String tag, String msg, boolean isNeedUpload, Map<String, String> paramMap) {
        android.util.Log.e(tag, msg);


    }

    public static void e(String tag, String msg, Throwable tr, boolean isNeedUpload) {
        android.util.Log.e(tag, msg, tr);


    }

    public static void e(String tag, String msg, Throwable tr, boolean isNeedUpload, Map<String, String> paramMap) {
        android.util.Log.e(tag, msg, tr);


    }

    public static void e(String tag, String subTag, String msg, Throwable tr, boolean isNeedUpload) {
        android.util.Log.e(tag, msg, tr);

    }

    public static void e(String tag, String subTag, String msg, Throwable tr, boolean isNeedUpload, Map<String, String> paramMap) {
        android.util.Log.e(tag, msg, tr);


    }
//
//    public static void uploadLog(String tag, String level, String log) {
//        uploadLog(tag, "", level, log, null);
//    }
//
//    public static void uploadLog(String tag, String level, String log, Map<String, String> paramMap) {
//        uploadLog(tag, "", level, log, paramMap);
//    }
//
//    public static void uploadLog(String tag, String sub_tag, String level, String log, Map<String, String> paramMap) {
//        try {
//            JSONObject json = new JSONObject();
//            json.put("device_id", DEVICE_ID);
//            json.put("tag", tag);
//            json.put("sub_tag", sub_tag);
//            json.put("log", log);
//            json.put("log_level", level);
//            json.put("log_timestamp", System.currentTimeMillis());
//            json.put("debug", BuildConfig.DEBUG ? 1 : 0);
//
//            if (paramMap != null) {
//                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
//                    json.put(entry.getKey(), entry.getValue());
//                }
//            }
//
//            LogHandlerManager.onEventRealTimeByJs(json.toString());
//        } catch (Exception e) {
//            android.util.Log.e("RobotUploadLog", e.getMessage(), e);
//        }
//    }
}
