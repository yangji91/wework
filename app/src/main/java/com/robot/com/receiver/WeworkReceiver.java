package com.robot.com.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.robot.hook.Main;
import com.robot.com.SDCardCleanUtil;
import com.robot.com.database.service.WeWorkMessageService;
import com.robot.util.ACache;
import com.robot.util.MyLog;

public class WeworkReceiver extends BroadcastReceiver {
    private final String TAG = "WeworkReceiver";
    public static final String ACTION_CHECK_SDCARD = "com.tencent.wework.intent.action.checksdcard";
    private long count = 0L;
    private final int deleteTime = 61;
//    private final int messageCheckTime = 5;//五分钟检测遗漏的消息
//    private long lastStartTime = System.currentTimeMillis() - ACache.TIME_HOUR * 1000;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
            MyLog.debug(TAG, "每分钟广播 count =" + (count++));
            if (count >= deleteTime && count % deleteTime == 0) {
                SDCardCleanUtil.checkWeiXinWorkFileSize();
            }
//            if (count >= messageCheckTime && count % messageCheckTime == 0) {
//                //检查没有截获到的消息
//                WeWorkMessageService.getInstance().checkMessageFromWeWorkDB();
//            }
//            //检查没有上报成功的消息
//            long startTime = lastStartTime / 1000;
//            MyLog.debug(TAG, " startTime =" + startTime);
//            long endTime = System.currentTimeMillis() / 1000 - 2;
//            //todo 这里有bug，乱传消息
////            WeWorkMessageService.getInstance().checkLocalMessage(startTime, endTime);
//            lastStartTime = endTime;
            Main.launchMainActivity();
            //检查没有截获到的消息
        }
    }
}
