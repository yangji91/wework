package com.robot.hook.base;

import android.os.Handler;
import android.os.Message;


public abstract class BaseMainXposed   {
    protected final String TAG = getClass().getSimpleName();

    private Handler mHandler = null;

    abstract protected void handleMsg(Message msg);


    protected void sendMsg(Message msg){
        if(mHandler == null){
            initHander();
        }
        mHandler.removeMessages(msg.what);
        mHandler.sendMessage(msg);
    }

    protected void removeMsg(int what){
        if(mHandler != null){
            mHandler.removeMessages(what);
        }
    }

    protected void sendMsgDelay(Message msg,long milli){
        if(mHandler == null){
            initHander();
        }
        mHandler.removeMessages(msg.what);
        mHandler.sendMessageDelayed(msg,milli);
    }

    protected void initHander(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                handleMsg(msg);
            }
        };
    }
}
