package com.robot.common;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gzuliyujiang.oaid.DeviceIdentifier;
import com.robot.com.file.SharePreWecomVersion;
import com.robot.entity.DeviceModelEnum;
import com.robot.entity.MsgEntity;
import com.robot.entity.PhoneLocEnum;
import com.robot.hook.KeyConst;
import com.robot.com.BuildConfig;
import com.robot.com.file.ShareDetectSafeStatus;
import com.robot.util.CheckUtil;
import com.robot.util.DeviceUtil;
import com.robot.util.MyLog;
import com.robot.robothook.LoadPackageParam;
import com.robot.robothook.RobotHelpers;

import java.util.HashMap;
import java.util.Map;


public class Global {
    private static final String TAG = Global.class.getSimpleName();
    private static Context mContext;
    public static LoadPackageParam loadPackageParam;
    //    public static ClassLoader mLoader;
    private static Handler uiHandler;
    private static Toast mToast;

    private static HandlerThread mHandlerThread;
    private static Looper mLooper;
    private static Handler mHandler;

    private static HandlerThread mNettyThread;
    private static Looper mNettyLooper;
    private static Handler mNettyHandler;

    private static HandlerThread mDownLoadImgThread;
    private static Looper mLooperDownloadImg;
    private static Handler mHandlerDownLoadImg;

    private static HandlerThread mThreadLog;
    private static Looper mLooperLog;
    private static Handler mHandlerLog;

    public static Long bbsId = 0L;
    public static String bbsName = "";
    private static String DeviceSn = "";
    private static String oaid = "";
    private static String wecomVersion = "";
    private static String detectSafeStatus = "";

    //skfly add begin
    public static Long corpId = 0L;
    public static String corpName = "";
    public static Long vId = 0L;
    //skfly add end

    public static long SystemStartReceiveTime;
    private static Map<Long, Object> mUserMap = new HashMap<>();

    static {
        initHandlerThread();
        initNettyHandlerThread();
        initDownloadImgHandlerThread();
        initLogThread();
    }

    public static long getRemoteId() {
        Class clazzCC = RobotHelpers.findClassIfExists(KeyConst.C_IAccount_CC, loadPackageParam.classLoader);
        Object objImple = RobotHelpers.callStaticMethod(clazzCC, KeyConst.C_IAccount_CC_GET);
        return (long) RobotHelpers.callMethod(objImple, KeyConst.M_IACCOUNT_GETLOGINUSERID);
    }

    private static void initLogThread() {
        mThreadLog = new HandlerThread("Log");
        mThreadLog.start();
        mLooperLog = mThreadLog.getLooper();
        mHandlerLog = new Handler(mLooperLog);
    }

    private static void initDownloadImgHandlerThread() {
        mDownLoadImgThread = new HandlerThread("DownloadImg");
        mDownLoadImgThread.start();
        mLooperDownloadImg = mDownLoadImgThread.getLooper();
        mHandlerDownLoadImg = new Handler(mLooperDownloadImg);
    }

    private static void initNettyHandlerThread() {
        mNettyThread = new HandlerThread("NettySocket");
        mNettyThread.start();
        mNettyLooper = mNettyThread.getLooper();
        mNettyHandler = new Handler(mNettyLooper);
    }

    public static void postNettyRunnableDelay(Runnable r, long milli) {
        if (mNettyThread != null && !mNettyThread.isAlive()) {
            initNettyHandlerThread();
        }
        mNettyHandler.postDelayed(r, milli);
    }

    public static void init( Context context) {
        try {
            Global.mContext = context;
//            lpparam.classLoader = context.getClassLoader();
            Global.loadPackageParam.classLoader = context.getClassLoader();
            if (context instanceof Application) {
                DeviceIdentifier.register((Application) context);
            } else {
                DeviceIdentifier.register((Application) context.getApplicationContext());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static Context getContext() {
        return Global.mContext;
    }

    public static void initLoadPackageParam(LoadPackageParam lpparam) {
        Global.loadPackageParam = lpparam;
    }

//    public static void initClassloader(ClassLoader loader) {
//        Global.mLoader = loader;
//    }


    public static Looper getLooper() {
        return mLooper;
    }

    public static void postRunnable2UI(Runnable r) {
        if (uiHandler == null) {
            uiHandler = new Handler(Looper.getMainLooper());
        }
        uiHandler.post(r);
    }

    public static void postRunnable2UIDelay(Runnable r, long milli) {
        if (uiHandler == null) {
            uiHandler = new Handler(Looper.getMainLooper());
        }
        uiHandler.postDelayed(r, milli);
    }


    public static void runTaskCycle(Runnable r) {

        uiHandler.post(r);
    }


    public static void removeRunnable(Runnable r) {
        if (uiHandler != null) {
            uiHandler.removeCallbacks(r);
        }
    }

    public static void postRunnableDownLoadImg(Runnable r) {
        if (mDownLoadImgThread == null || !mDownLoadImgThread.isAlive()) {
            initDownloadImgHandlerThread();
        }
        mHandlerDownLoadImg.post(r);
    }

    public static void postRunnableLog(Runnable r) {
        if (mHandlerLog != null) {
            mHandlerLog.post(r);
        }
    }

    public static void showToast(String tips) {
        showToast(tips, true, false);
    }

    public static void showToast(String tips, boolean isSucc) {
        showToast(tips, isSucc, false);
    }

    public static void showToast(String tips, boolean isSucc, boolean isTop) {
        DeviceModelEnum deviceModelEnum = DeviceUtil.getDeviceModelType();
        if (deviceModelEnum != DeviceModelEnum.MEIZU || MConfiger.phoneLocEnum != PhoneLocEnum.LOCAL) {
            Global.postRunnable2UI(() -> {
                hideToast();
//                mToast = Toast.makeText(mContext,tips+" [版本号" + MConfiger.VERSION + "]",Toast.LENGTH_LONG);
//                mToast.show();

                /**
                 * TextView view = new TextView(context);
                 * view.setBackgroundResource(android.R.color.holo_green_light);
                 * view.setTextColor(Color.RED);
                 * view.setText("item"+item.getItemId());
                 * view.setPadding(10, 10, 10, 10);
                 * toast.setGravity(Gravity.CENTER, 0, 40);
                 * toast.setView(view);
                 */
                float r = 18;
                mToast = new Toast(mContext);
                TextView view = new TextView(mContext);
                GradientDrawable gradientDrawable;
                if (isSucc) {
                    gradientDrawable = getShapColor(Color.parseColor("#646464"), r);
                } else {
                    gradientDrawable = getShapColor(Color.parseColor("#FF3366"), r);
                }
                view.setBackground(gradientDrawable);
                view.setTextColor(Color.WHITE);
                view.setText(tips + "\r\n" + "版本号:" + BuildConfig.VERSION_NAME + " " + BuildConfig.VERSION_CODE + "\r\n" + "企微版本号:" + Global.getPackageWecomVersion() + "\r\n" + "\r\n" + "IMEI:" + DeviceUtil.getIMEI());
                view.setPadding(60, 20, 60, 20);
                if (isTop) {
                    mToast.setGravity(Gravity.BOTTOM, 0, 380);
                } else {
                    mToast.setGravity(Gravity.BOTTOM, 0, 120);
                }
                mToast.setView(view);
                mToast.setDuration(Toast.LENGTH_LONG);
                mToast.show();
            });
        }
    }

    public static void wakeUpToast() {
        Global.postRunnable2UI(() -> {
            hideToast();
            mToast = new Toast(mContext);
            View view = new View(mContext);
            view.setBackgroundColor(Color.TRANSPARENT);
            mToast.setView(view);
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.show();
        });
    }

    /**
     * 设置圆角背景
     *
     * @param
     * @return
     */
    @SuppressLint("WrongConstant")
    public static GradientDrawable getShapColor(int color, float r) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setGradientType(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(r);
        drawable.setColor(color);
        return drawable;
    }

    private static void hideToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

    public static final void postRunnable(Runnable r) {
        if (!mHandlerThread.isAlive()) {
            initHandlerThread();
        }
        mHandler.postDelayed(r, 0);
    }

    private static void initHandlerThread() {
        mHandlerThread = new HandlerThread("Global");
        mHandlerThread.start();
        mLooper = mHandlerThread.getLooper();
        mHandler = new Handler(mLooper);
    }

    public static final void postRunnableDelay(Runnable r, long milli) {
        if (!mHandlerThread.isAlive()) {
            initHandlerThread();
        }
        MyLog.debug(TAG, "[postRunnableDelay]" + " milli:" + milli);
        mHandler.postDelayed(r, milli);
    }


    public static final int getUserMapSize() {
        return mUserMap.size();
    }

    public static void setUserObject(Long remoteId, Object obj) {
        mUserMap.put(remoteId, obj);
    }

    public static Object getUserObject(Long remoteId) {
        Object obj = mUserMap.get(remoteId);
        if (BuildConfig.customConfigLog) {
            MyLog.debug(TAG, "[getUserObject]" + " key:" + remoteId + " val:" + obj);
        }
        return obj;
    }

    public static Integer getUserSize() {
        int size = mUserMap.size();
        return size;
    }

    public static String getDeviceSn() {
        if (TextUtils.isEmpty(DeviceSn)) {
            DeviceSn = DeviceUtil.getSn();
        }
        if (DeviceSn == null) {
            DeviceSn = "";
        }
        return DeviceSn;
    }

    public static String getDetectSafeStatus() {
        if (TextUtils.isEmpty(detectSafeStatus)) {
            ShareDetectSafeStatus shareDetectSafeStatus = new ShareDetectSafeStatus();
            detectSafeStatus = shareDetectSafeStatus.loadData();
        }
        if (TextUtils.isEmpty(detectSafeStatus)) {
            detectSafeStatus = "0";
        }
        return detectSafeStatus;
    }

//    public static String getOAID() {
//        try {
//            if (TextUtils.isEmpty(oaid)) {
//                if (Global.getContext() != null) {
//                    oaid = DeviceIdentifier.getOAID(Global.getContext());
//                }
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//        return oaid;
//    }

    public static String getWecomVersion() {
//        if (TextUtils.isEmpty(wecomVersion)) {
//            SharePreWecomVersion sharePreWecomVersion = new SharePreWecomVersion();
//            wecomVersion = sharePreWecomVersion.loadData();
//        }
//        if (TextUtils.isEmpty(wecomVersion)) {
//            wecomVersion = getPackageWecomVersion();
//        }
        //todo
        return MConfiger.WEWORK_VERSION_31145;
    }

    public static String getPackageWecomVersion() {
        try {
            String version = "";
            if (mContext != null) {
                if (TextUtils.isEmpty(wecomVersion) || wecomVersion.equals(".")) {
                    PackageInfo packageInfo = CheckUtil.getPackageInfo(mContext, MConfiger.WX_ENTERPISE_PKGNAME);
                    if (packageInfo != null) {
                        version = packageInfo.versionName + "." + packageInfo.versionCode;
                    }
                }
            }
            return version;
        } catch (Throwable e) {
            return "";
        }
    }


    //存储接收到的语音信息
    private static Map<String, MsgEntity> msgEntityMap = new HashMap<>();

    public static void puttMsgEntity2Map(String fileMd5, MsgEntity entity) {
        msgEntityMap.put(fileMd5, entity);
    }

    public static MsgEntity getMsgEntityFromMap(String fileMd5) {
        return msgEntityMap.get(fileMd5);
    }

    public static void deleteMsgEntityFromMap(String fileMd5) {
        msgEntityMap.remove(fileMd5);
    }

    public static long getSystemStartReceiveTime() {
        if (SystemStartReceiveTime == 0) {
            SystemStartReceiveTime = System.currentTimeMillis();
        }
        return SystemStartReceiveTime;
    }

}
