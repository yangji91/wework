package com.robot.netty.handle.imple.contact;

import com.robot.common.Global;
import com.robot.entity.ActionResultEnum;
import com.robot.entity.ActionStatusEnum;
import com.robot.entity.ResEntity;
import com.robot.entity.UserEntity;
import com.robot.hook.HookMethodEnum;
import com.robot.hook.base.HookBaseMethod;
import com.robot.hook.base.IHookCallBack;
import com.robot.netty.ProtocalManager;
import com.robot.netty.entity.rsp.PRspActionTextMsgEntity;
import com.robot.netty.entity.rsp.PRspGroupManagerEntity;
import com.robot.netty.handle.imple.group.BaseHandleGroup;
import com.robot.util.MyLog;

import java.util.List;

/**
 * 删除联系人
 */
public class DeleteOuterFriendAction extends BaseHandleGroup {

    @Override
    public void handleAction(PRspGroupManagerEntity.ActionItem actionItem) {
        MyLog.debug(TAG, "[deleteOuterContact]" + "删除外部联系人");
        List<PRspActionTextMsgEntity.PMatcherItemEntity> matchers = actionItem.matchers;
        MyLog.debug(TAG, "[handleItem]" + " 删除外部联系人...constActionSubType:" + actionItem.actionSubType);
        if (matchers != null && matchers.size() > 0) {
            PRspActionTextMsgEntity.PMatcherItemEntity matcher = actionItem.matchers.get(0);
            if (matcher.remoteId > 0) {
                handleDeleteOuterFriend(actionItem.executorUin, actionItem.uid, matcher.remoteId, actionItem.actionType, actionItem.actionSubType);
            } else {
                ProtocalManager.getInstance().sendMsgReportCallback(actionItem.executorUin, actionItem.uid, ActionStatusEnum.FAILURE, ActionResultEnum.ACTION_PARAM_FAIL.getMsg(), actionItem.actionType, actionItem.actionSubType);
            }
        } else {
            ProtocalManager.getInstance().sendMsgReportCallback(actionItem.executorUin, actionItem.uid, ActionStatusEnum.FAILURE, ActionResultEnum.ACTION_PARAM_FAIL.getMsg(), actionItem.actionType, actionItem.actionSubType);
        }
    }

    private void handleDeleteOuterFriend(long executorUin, String uid, long remoteId, long actionType, long actionSubType) {
        HookBaseMethod<UserEntity> hookBaseMethod = HookMethodEnum.DELETE_OUTER_FRIEND.getMethod();
        hookBaseMethod.onInvokeMethod(null, Global.loadPackageParam, new IHookCallBack<UserEntity>() {
            @Override
            public void onCall(ResEntity<UserEntity> resEntity) {
                if (resEntity.isSucc()) {
                    ProtocalManager.getInstance().sendMsgReportCallback(executorUin, uid, ActionStatusEnum.SUCC, ActionResultEnum.ACTION_SUCC.getMsg(), actionType, actionSubType);
                    MyLog.debug(TAG, "[onInvokeMethod]" + "删除成功...");
                } else {
                    ProtocalManager.getInstance().sendMsgReportCallback(executorUin, uid, ActionStatusEnum.FAILURE, ActionResultEnum.ACTION_PARAM_FAIL.getMsg(resEntity.getMsg()), actionType, actionSubType);
                    MyLog.debug(TAG, "[onInvokeMethod]" + "删除失败...");
                }
            }

            @Override
            public UserEntity getParams() {
                UserEntity userEntity = new UserEntity();
                userEntity.remoteId = remoteId;
                return userEntity;
            }
        });
    }
}

