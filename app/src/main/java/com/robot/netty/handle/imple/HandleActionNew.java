package com.robot.netty.handle.imple;

import com.robot.common.FloatHelper;
import com.google.gson.Gson;
import com.robot.common.Global;
import com.robot.common.MConfiger;
import com.robot.controller.ConvController;
import com.robot.controller.LoginController;
import com.robot.netty.entity.ResBaseEntity;
import com.robot.netty.handle.ActionEnum;
import com.robot.netty.handle.BaseHandle;
import com.robot.nettywss.WssNettyEngine;
import com.robot.util.MyLog;
import com.robot.util.StrUtils;

public class HandleActionNew extends BaseHandle {
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onHandle(String data) {
        super.onHandle(data);
        ResBaseEntity res = new Gson().fromJson(data, ResBaseEntity.class);
        String taskType = res.getTaskType();
//        MyLog.debug(TAG, "taskType " + taskType + " [收到服务端新下发的任务:]: " + data, true);

        ActionEnum handleEnum = ActionEnum.getHandleActionByTaskType(taskType);

        MyLog.debug(TAG, "taskType " + taskType + " [收到服务端新下发的任务:]: " + data + "enum: " + handleEnum, true);

        if (handleEnum != null) {
            MyLog.debug(TAG, "[onHandle]" + handleEnum.getDesc());
            BaseHandle baseHandle = handleEnum.getHandleAction();
            if (baseHandle != null) {
                String envStr = MConfiger.env.getDesc();
                long startTime = WssNettyEngine.getInstance().getStartTime();
                String strStartTime = StrUtils.getRunStrTime(startTime);
                long connectTime = WssNettyEngine.getInstance().getConnectTime();
                String strConnectTime = StrUtils.getRunStrTime(connectTime);
                int mConnectCnt = WssNettyEngine.getInstance().getConnectCnt();
                int userSize = Global.getUserSize();
                int convSize = ConvController.getInstance().getConvSize();
                String loginMobile = LoginController.getInstance().getLoginMobile();

                if (MConfiger.mRobotStatus != 2) {
                    MConfiger.mRobotStatus = 2;
                    MConfiger.mRobotTips = "执行任务中-" + handleEnum.getDesc() +
                            "\n" + "正常运行 " + envStr +
                            "\n" + "设备标识:" + Global.getDeviceSn() +
                            "\n" + "手机号:" + loginMobile +
                            "\n" + "运行时长:" + strStartTime +
                            "\n" + "连接时长:" + strConnectTime + " 尝试连接:" + mConnectCnt + "次" +
                            "\n" + "联系人:" + userSize  + "\n";
                    FloatHelper.notifyData();
                } else {
                    MConfiger.mRobotTips = "执行任务中-" + handleEnum.getDesc() +
                            "\n" + "正常运行 " + envStr +
                            "\n" + "设备标识:" + Global.getDeviceSn() +
                            "\n" + "手机号:" + loginMobile +
                            "\n" + "运行时长:" + strStartTime +
                            "\n" + "连接时长:" + strConnectTime + " 尝试连接:" + mConnectCnt + "次" +
                            "\n" + "联系人:" + userSize  + "\n";
                }

                baseHandle.onHandle(data);
            } else {
                MyLog.debug(TAG, "[onHandle]" + "该消息未找到实现类 type:" + handleEnum.getDesc());
            }
        } else {
            MyLog.debug(TAG, "[onHandle]" + "该消息类型未找到实现...");
        }
    }

    boolean mIsTest = false;

    public void onHandleTest(String data) {
        mIsTest = true;
        onHandle(data);
        mIsTest = false;
    }
}
