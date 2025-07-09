package com.robot.com;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.robot.util.Log;

public class ComWorkServiceManager {
    private static final String TAG = "ComWorkServiceManager";
    private static final ComWorkServiceManager INSTANCE = new ComWorkServiceManager();
    private IRobotWorkService mRobotWorkService;

    private final ServiceConnection mRobotWorkServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRobotWorkService = IRobotWorkService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRobotWorkService = null;
        }
    };

    private ComWorkServiceManager() {

    }

    public static ComWorkServiceManager getInstance() {
        return INSTANCE;
    }

    public int isWoWorkRunning() {
        if (mRobotWorkService == null) {
            return ComWorkService.WOWORK_RUNNING_UNKNOWN;
        }

        try {
            return mRobotWorkService.isWoWorkRunning();
        } catch (RemoteException e) {
            Log.e(TAG, "ComWorkServiceManager isWoWorkRunning error: " + e.getMessage(), e);
            return ComWorkService.WOWORK_RUNNING_UNKNOWN;
        }
    }

    public void registerService(Context context) {
        if (mRobotWorkService != null) {
            return;
        }

        Intent intent = new Intent(context.getApplicationContext(), ComWorkService.class);
        context.getApplicationContext().bindService(intent, mRobotWorkServiceConnection, Context.BIND_ABOVE_CLIENT);
    }

    public void unregisterService(Context context) {
        if (mRobotWorkService == null) {
            return;
        }

        context.getApplicationContext().unbindService(mRobotWorkServiceConnection);
        mRobotWorkService = null;
    }
}
