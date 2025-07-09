package com.robot.netty.proto.req;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.robot.entity.ActionResultEnum;
import com.robot.entity.ActionRet;
import com.robot.entity.ActionStatusEnum;
import com.robot.com.BuildConfig;
import com.robot.util.MyLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReqMsgCallBackEntity implements Serializable {
    private static final String TAG = ReqMsgCallBackEntity.class.getSimpleName();
    public List<MsgCallBackEntity> callbacks;

    public static class MsgCallBackEntity implements Serializable {
        public String uid;  //需要回调的执行计划id
        public Integer actionStatus;
        public String actionInfo;
        public String actionResult;
        public Long robotUin;
        public ActionRet actionRet;
    }

    public static final ReqMsgCallBackEntity buildReqCallBackEntity(Long robotUin, String uid, ActionStatusEnum actionStatusEnum, String actionInfo, Collection<ActionResultEnum.PActionResultItem> errList) {
        ReqMsgCallBackEntity reqEntity = new ReqMsgCallBackEntity();
        List<MsgCallBackEntity> sList = new ArrayList<>();
        MsgCallBackEntity entity = new MsgCallBackEntity();
        entity.uid = uid;
        entity.actionStatus = actionStatusEnum.getStatus();
        entity.actionInfo = actionInfo;
        if (TextUtils.isEmpty(entity.actionInfo)) {
            entity.actionInfo = actionStatusEnum.getDesc();
        }
        entity.robotUin = robotUin;
        entity.actionRet = new ActionRet();
        List<ActionRet.FailExpectMatters> failList = new ArrayList<>();
        if (errList != null && errList.size() > 0) {
            for (ActionResultEnum.PActionResultItem item : errList) {
                ActionRet.FailExpectMatters failItemEntity = new ActionRet.FailExpectMatters();
                failItemEntity.type = item.code;
                failItemEntity.info = item.msg;
                failItemEntity.errorMessage = item.desc;
                failList.add(failItemEntity);
            }
            entity.actionRet.failExpectMatters = failList;
        }

        sList.add(entity);
        reqEntity.callbacks = sList;
        if (BuildConfig.customConfigLog) {
            Gson gson = new Gson();
            String str = gson.toJson(reqEntity);
            MyLog.debug(TAG, "[buildReqCallBackEntity]" + str);
        }
        return reqEntity;
    }

    public static final ReqMsgCallBackEntity buildReqCallBackEntity(Long robotUin, String uid, ActionStatusEnum actionStatusEnum, String actionInfo, Object actionResult) {
        ReqMsgCallBackEntity reqEntity = new ReqMsgCallBackEntity();
        List<MsgCallBackEntity> sList = new ArrayList<>();
        MsgCallBackEntity entity = new MsgCallBackEntity();
        entity.uid = uid;
        entity.actionStatus = actionStatusEnum.getStatus();
        entity.actionInfo = actionInfo;
        if (TextUtils.isEmpty(entity.actionInfo)) {
            entity.actionInfo = actionStatusEnum.getDesc();
        }
        entity.robotUin = robotUin;
        if (actionResult != null) {
            Gson gson = new Gson();
            entity.actionResult = gson.toJson(actionResult);
        }
        sList.add(entity);
        reqEntity.callbacks = sList;
        return reqEntity;
    }

    public static final ReqMsgCallBackEntity buildReqCallBackEntity(Long robotUin, String uid, ActionStatusEnum actionStatusEnum, String actionInfo, Object actionResult, ActionRet tempAR) {
        ReqMsgCallBackEntity reqEntity = new ReqMsgCallBackEntity();
        List<MsgCallBackEntity> sList = new ArrayList<>();
        MsgCallBackEntity entity = new MsgCallBackEntity();
        entity.uid = uid;
        entity.actionStatus = actionStatusEnum.getStatus();
        entity.actionInfo = actionInfo;
        entity.robotUin = robotUin;
        if (actionResult != null) {
            Gson gson = new Gson();
            entity.actionResult = gson.toJson(actionResult);
            MyLog.debug("SKFLY_DEBUG  entity.actionResult=", entity.actionResult);
        } else {
            MyLog.debug("SKFLY_DEBUG", "entity.actionResult=null");
        }
        entity.actionRet = tempAR;
        if (tempAR == null) {
            if (TextUtils.isEmpty(entity.actionInfo)) {
                entity.actionInfo = actionStatusEnum.getDesc();
            }
            MyLog.debug("SKFLY_DEBUG", "tempAR==null");
        } else {
            Gson gson = new Gson();
            MyLog.debug("SKFLY_DEBUG   tempAR=", gson.toJson(tempAR));
        }

        sList.add(entity);
        reqEntity.callbacks = sList;
        return reqEntity;
    }
}
