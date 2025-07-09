package com.robot.hook.msghandle;

import android.text.TextUtils;

import com.robot.entity.MsgEntity;
import com.robot.hook.msghandle.base.BaseHandleMsg;
import com.robot.netty.ProtocalManager;
import com.robot.nettywss.WssProtocalManager;
import com.robot.util.MyLog;
import com.robot.util.StrUtils;
import com.robot.robothook.LoadPackageParam;

/**
 * 自定义表情 content 为表情的连接url
 */
public class HandleDIYMemeImageMsg implements BaseHandleMsg {

    @Override
    public void onHandleMsg(LoadPackageParam loadPackageParam, MsgEntity msgEntity) {

        if (msgEntity.fileMsgEntity != null) {
            msgEntity.content = getString(StrUtils.byteToUTFStr(msgEntity.fileMsgEntity.fileId),
                    StrUtils.byteToUTFStr(msgEntity.fileMsgEntity.url));
        }
        WssProtocalManager.sendMsgEntity(msgEntity, "diyEmoji");
    }

    private String getString(String... strings) {
        for (int i = 0; i < strings.length; i++) {
            if (!TextUtils.isEmpty(strings[i]) && strings[i].startsWith("http")) {
                MyLog.debug("getString:", strings[i]);
                return strings[i];
            }
        }
        return "";
    }
}
