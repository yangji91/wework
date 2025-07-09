package com.robot.com.receiver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.robot.util.MyLog;

public class ReciverManager {
    private final String TAG = ReciverManager.class.getSimpleName();

    private static ReciverManager instance;
    private WeworkReceiver changeAccReceiver;

    private ReciverManager(){
        this.changeAccReceiver = new WeworkReceiver();
    }

    public static    ReciverManager getInstance(){
        if(instance == null){
            instance = new ReciverManager();
        }
        return instance;
    }

    public void regReceiver(Activity mContext){
        MyLog.debug(TAG,"[regReceiver]" +" 注册广播...");

        IntentFilter intentFilter = new IntentFilter( );
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        mContext.registerReceiver(changeAccReceiver, intentFilter);
    }


    /***
     *
     * @param context
     */
    public static final void broadCastCheckSDCard(Context context){
        Intent intent = new Intent(WeworkReceiver.ACTION_CHECK_SDCARD);
        context.sendBroadcast(intent);
    }
}
