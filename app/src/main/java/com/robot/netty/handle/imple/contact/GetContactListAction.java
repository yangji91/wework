package com.robot.netty.handle.imple.contact;

import com.robot.controller.ConvController;
import com.robot.entity.ActionStatusEnum;
import com.robot.entity.UserEntity;
import com.robot.netty.ProtocalManager;
import com.robot.netty.entity.rsp.PRspGroupManagerEntity;
import com.robot.netty.handle.imple.group.BaseHandleGroup;
import com.robot.nettywss.WssProtocalManager;
import com.robot.util.MyLog;
import com.robot.util.SpliteUtil;

import java.util.List;

/**
 * 同步联系人
 */
public class GetContactListAction extends BaseHandleGroup {

    @Override
    public void handleAction(String data) {
        super.handleAction(data);
        ConvController.getInstance().getOuterContact(new ConvController.GetUserCallback() {
            @Override
            public void onResult(int code, List<UserEntity> list) {
                MyLog.debug(TAG, "[synchrOuterContact]" + " 同步外部联系人:" + list.size());
                if (!list.isEmpty()) {
                    WssProtocalManager.sendContactInfos(list);
                } else {
                    MyLog.debug(TAG, "[synchrOuterContact]" + " 未找到外部联系人:");
                }
            }
        });
    }

    @Override
    public void handleAction(PRspGroupManagerEntity.ActionItem actionItem) {
        ConvController.getInstance().getOuterContact(new ConvController.GetUserCallback() {
            @Override
            public void onResult(int code, List<UserEntity> list) {
                MyLog.debug(TAG, "[synchrOuterContact]" + " 同步外部联系人:" + list.size());
                if (list.size() > 0) {
                    if (list.size() > 100) {
                        List<List> mList = SpliteUtil.splistList(list, 50);
                        if (mList != null && mList.size() > 0) {
                            for (List<UserEntity> ll : mList) {
                                ProtocalManager.getInstance().sendContactInfo(ll);
                                MyLog.debug(TAG, "[startConnect]" + " splite userList->" + ll.size());
                            }
                        }
                    } else {
                        ProtocalManager.getInstance().sendContactInfo(list);
                    }
                }
                ProtocalManager.getInstance().sendMsgReportCallback(actionItem.executorUin, actionItem.uid, ActionStatusEnum.SUCC, "执行成功", actionItem.actionType, actionItem.actionSubType);
            }
        });
    }
}
