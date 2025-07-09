package com.robot.hook.msghandle.base;

import com.robot.entity.MsgEntity;

import com.robot.robothook.LoadPackageParam;

public interface BaseHandleMsg {
    void onHandleMsg(LoadPackageParam loadPackageParam, MsgEntity msgEntity);
}
