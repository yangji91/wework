package com.robot.hook.msghandle;

import com.robot.entity.MsgEntity;
import com.robot.hook.msghandle.base.BaseHandleMsg;
import com.robot.nettywss.WssProtocalManager;
import com.robot.robothook.LoadPackageParam;

public class HandleRedPacketMsg implements BaseHandleMsg {

    @Override
    public void onHandleMsg(LoadPackageParam loadPackageParam, MsgEntity msgEntity) {
        WssProtocalManager.sendMsgEntity(msgEntity, "redPacket");
    }
}
