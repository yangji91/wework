package com.robot.com;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.widget.Toast;

import com.robot.util.CommonUtil;
import com.robot.util.Log;

import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LifecycleService;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ComWorkService extends LifecycleService {
    private static final String TAG = "WeBotService";
    private static final int CHECK_WOWORK_REFRESH_TIME = 10;
    private static final long CHECK_WOWORK_RUNNING_THRESHOLD = 1000 * 60;
    private static final int COM_WORK_SERVICE_NOTIFICATION_ID = 666;
    private static final String COM_WORK_SERVICE_CHANNEL_ID = "com.robot.com.service";
    private static final String COM_WORK_SERVICE_CHANNEL_NAME = "WeBot";

    public static final int WOWORK_RUNNING_UNKNOWN = -1;
    public static final int WOWORK_RUNNING_NO = 0;
    public static final int WOWORK_RUNNING_YES = 1;

    private final IBinder mBinder = new RobotWorkServiceBinder();
    private PowerManager.WakeLock mWakeLock;
    private String mNotificationContent;

    //doCheckWoWork
    private Disposable mDoCheckWoWorkDisposable;
    private volatile Boolean mIsWoWorkRunning;
    private long mStartWoWorkCounter = 0;


    @SuppressLint("AutoDispose")
    private void doCheckWoWork(int refreshInterval) {
        if (mDoCheckWoWorkDisposable != null && !mDoCheckWoWorkDisposable.isDisposed()) {
            mDoCheckWoWorkDisposable.dispose();
        }

        mDoCheckWoWorkDisposable = Single
                .timer(refreshInterval, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .map(
                        num -> {
                            long heartBeatTimeStamp = CommonUtil.getHeartBeatTime();

                            if ((System.currentTimeMillis() - heartBeatTimeStamp) > CHECK_WOWORK_RUNNING_THRESHOLD) {
                                mIsWoWorkRunning = false;
                            } else {
                                mIsWoWorkRunning = true;
                            }

                            return 1;
                        }
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (num, throwable) -> {
                            if (throwable != null) {
                                Log.e(TAG, "CheckWoWork error: " + throwable.getMessage(), throwable);
                            } else {
                                if (mIsWoWorkRunning != null && !mIsWoWorkRunning && CommonUtil.checkWoWorkIsInstall(getApplicationContext())) {
                                    if (++mStartWoWorkCounter >= 25) {
                                        mStartWoWorkCounter = 0;
                                        CommonUtil.startWoWork(getApplicationContext());
                                        Toast.makeText(getApplicationContext(), "WoWork: 长链接断开", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    mStartWoWorkCounter = 0;
                                }
                            }

                            doCheckWoWork(CHECK_WOWORK_REFRESH_TIME);
                        }
                );
    }

    public void updateNotification(String content) {
        if (mNotificationContent == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(COM_WORK_SERVICE_CHANNEL_ID, COM_WORK_SERVICE_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        if (content.equals(mNotificationContent)) {
            return;
        } else {
            mNotificationContent = content;
        }

        Notification.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(getApplicationContext(), COM_WORK_SERVICE_CHANNEL_ID);
        } else {
            builder = new Notification.Builder(getApplicationContext());
        }

        builder.setOngoing(true)
                .setContentTitle(content)
                .setPriority(Notification.PRIORITY_MAX);

        startForeground(COM_WORK_SERVICE_NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        requireLock();
        updateNotification("WeBot 正在运行");

        doCheckWoWork(0);
    }

    @Override
    public IBinder onBind(Intent intent) {

        super.onBind(intent);
        return mBinder;
    }

    @Override
    public void onDestroy() {
        releaseLock();
        super.onDestroy();
    }

    private synchronized void requireLock() {
        if (mWakeLock == null) {
            PowerManager mgr = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
            mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, ":" + TAG);
            mWakeLock.setReferenceCounted(false);
            mWakeLock.acquire();
        }
    }

    private synchronized void releaseLock() {
        if (mWakeLock != null) {
            if (mWakeLock.isHeld()) {
                mWakeLock.release();
            }

            mWakeLock = null;
        }
    }

    public class RobotWorkServiceBinder extends IRobotWorkService.Stub {
        @Override
        public int isWoWorkRunning() throws RemoteException {
            if (mIsWoWorkRunning == null) {
                return WOWORK_RUNNING_UNKNOWN;
            }

            return mIsWoWorkRunning ? WOWORK_RUNNING_YES : WOWORK_RUNNING_NO;
        }
    }
}
