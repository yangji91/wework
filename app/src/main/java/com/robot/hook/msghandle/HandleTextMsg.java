package com.robot.hook.msghandle;

import com.robot.entity.MsgEntity;
import com.robot.hook.msghandle.base.BaseHandleMsg;

import com.robot.nettywss.WssProtocalManager;
import com.robot.robothook.LoadPackageParam;

public class HandleTextMsg implements BaseHandleMsg {
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onHandleMsg(LoadPackageParam loadPackageParam, MsgEntity msgEntity) {
        WssProtocalManager.sendMsgEntity(msgEntity,"txt");
    }
}
