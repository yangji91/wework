package com.robot.netty.handle.imple.runnable;

import android.text.TextUtils;

import com.robot.common.Global;
import com.robot.controller.ConvController;
import com.robot.entity.ActionResultEnum;
import com.robot.entity.ActionStatusEnum;
import com.robot.entity.UserEntity;
import com.robot.netty.ProtocalManager;
import com.robot.netty.entity.rsp.PRspActionTextMsgEntity;
import com.robot.netty.handle.ActionEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 旧的私聊消息处理中间层
 */
public class MessageTask {

    private static ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    public static void addTask(PRspActionTextMsgEntity.PContactMsgActionItem data) {
        //**新增remote id 为空的情况
        if (data.matchers.get(0).remoteId == 0) {
            if (data.actionType == ActionEnum.PUSH_USER_MEDIA_MSG.getActionType()) {//私聊
                delPrivateChat(data);
            }
        } else {
            //通过会话id和remoteId创建会话
            searchConversation(data);
        }
    }

    /**
     * 私聊匹配
     *
     * @param data
     */
    private static void delPrivateChat(PRspActionTextMsgEntity.PContactMsgActionItem data) {
        PRspActionTextMsgEntity.PMatcherItemEntity pMatcherItemEntity = data.matchers.get(0);
        if (TextUtils.isEmpty(pMatcherItemEntity.secondMatch)) {
            ProtocalManager.getInstance().sendMsgReportCallback(data.executorUin, data.uid, ActionStatusEnum.REPEAT, ActionResultEnum.ACTION_PARAM_FAIL.getMsg("匹配参数为空 " + pMatcherItemEntity.secondMatch), data.actionType, data.actionSubType);

        } else {
            ConvController.getInstance().getUserByNameAndHead(pMatcherItemEntity.match, pMatcherItemEntity.secondMatch, new ConvController.GetUserCallback() {
                @Override
                public void onResult(int code, List<UserEntity> list) {
                    if (code == 0 && list.size() > 0) {
                        data.matchers.get(0).remoteId = list.get(0).remoteId;
                        searchConversation(data);
                    } else {
                        List<ActionResultEnum.PActionResultItem> errList = new ArrayList<>();
                        errList.add(ActionResultEnum.ACTION_SEARCH_CONV_FAIL.getActionResultItemEntity());
                        ProtocalManager.getInstance().sendMsgReportCallbackErr(data.executorUin, data.uid, ActionStatusEnum.FAILURE, ActionResultEnum.ACTION_SEARCH_CONV_FAIL.getMsg(), errList, data.actionType, data.actionSubType);
                    }
                }
            });
        }
    }


    /**
     * 线程池提交任务
     *
     * @param runnable
     */
    public static void submit(Runnable runnable) {
        singleThreadExecutor.submit(runnable);
    }

    private static void searchConversation(PRspActionTextMsgEntity.PContactMsgActionItem data) {
        ConvController.getInstance().getConversationObjectByRemoteIDAndConversationId(Global.loadPackageParam.classLoader, data.matchers.get(0).conversationId, data.matchers.get(0).remoteId, false, new ConvController.GetOrCreateConversationCallBack() {
            @Override
            public void onResult(int code, Object conversationObject, String messageStr) {
                if (code == 0) {
                    singleThreadExecutor.submit(getMessageRunnable(data, conversationObject));
                } else {
                    List<ActionResultEnum.PActionResultItem> errList = new ArrayList<>();
                    errList.add(ActionResultEnum.ACTION_CREATE_CONV_FAILRE.getActionResultItemEntity());
                    ProtocalManager.getInstance().sendMsgReportCallbackErr(data.executorUin, data.uid, ActionStatusEnum.FAILURE, ActionResultEnum.ACTION_CREATE_CONV_FAILRE.getMsg(), errList, data.actionType, data.actionSubType);
                }
            }
        });
    }

    private static Runnable getMessageRunnable(PRspActionTextMsgEntity.PContactMsgActionItem data, Object conversationObject) {
        return new MessageRunnable(data, conversationObject);
    }
}





