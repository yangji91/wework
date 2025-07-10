package com.robot.netty;

import com.robot.com.database.service.WeWorkMessageService;
import com.robot.common.StatsHelper;
import com.robot.controller.LoginController;
import com.robot.entity.ActionResultEnum;
import com.robot.entity.ActionRet;
import com.robot.entity.ActionStatusEnum;
import com.robot.entity.ConvEntity;
import com.robot.entity.EnterpriseEntity;
import com.robot.entity.MsgEntity;
import com.robot.entity.UserData;
import com.robot.entity.UserEntity;
import com.robot.netty.entity.RobotMsg;
import com.robot.netty.entity.TransmitData;
import com.robot.netty.listener.ISendListener;
import com.robot.netty.proto.SendCommonImple;
import com.robot.netty.proto.req.ReqMsgRecvEntity;
import com.robot.netty.proto.req.ReqUserConvEntity;
import com.robot.util.MyLog;

import java.util.Collection;
import java.util.List;


public class ProtocalManager {
    private final String TAG = ProtocalManager.class.getSimpleName();
    private static ProtocalManager instance;

    private ProtocalManager() {
    }

    public static final ProtocalManager getInstance() {
        if (instance == null) {
            instance = new ProtocalManager();
        }
        return instance;
    }

    public void sendAuth(ISendListener mListener) {
//        long userId = LoginController.getInstance().getLoginUserId();
//        TransmitData<RobotMsg> req = TransmitData.buildDefault(NettyHandleProtoEnum.AUTH.getCode(), NettyHandleProtoEnum.PING.getActionType(), new JsonObject());
//        SendAuthImple sendAuthImple = new SendAuthImple(req, mListener);
//        NettyEngine.getInstance().sendMsg(sendAuthImple, true, true);
    }

    /**
     * 发送心跳
     */
    public void sendHeartBeat() {
//        ReqHeartEntity reqHeartEntity = new ReqHeartEntity();
//        reqHeartEntity.self_headurl = LoginController.getInstance().getLoginUser().avatorUrl;
//        reqHeartEntity.self_nickname = LoginController.getInstance().getLoginUserName();
//        TransmitData<RobotMsg> msg = TransmitData.buildDefault(NettyHandleProtoEnum.PING.getCode(), NettyHandleProtoEnum.PING.getActionType(), reqHeartEntity);
//        SendHeartBeatImple heartBeatImple = new SendHeartBeatImple(msg);
//        NettyEngine.getInstance().sendMsg(heartBeatImple);
    }

    /**
     * 确认设备配置信息
     */
    public void sendAckDeviceCfg() {
//        long userId = LoginController.getInstance().getLoginUserId();
//        TransmitData<RobotMsg> msg = TransmitData.buildDefault(NettyHandleProtoEnum.ROBOT_CFG.getCode(), NettyHandleProtoEnum.PING.getActionType(), new JsonObject());
//        SendAckDeviceCfgImple sendTask = new SendAckDeviceCfgImple(msg);
//        NettyEngine.getInstance().sendMsg(sendTask);
    }

    /**
     * 发送设备信息
     *
     * @param userEntity
     */
    public void sendDeviceInfo(UserEntity userEntity, EnterpriseEntity enterpriseEntity) {
//        MyLog.debug(TAG, "[sendDeviceInfo]" + "...", true);
//        ReqDeviceInfoEntity reqEntity = new ReqDeviceInfoEntity();
//        reqEntity.robotInfos = new ArrayList<>();
//        ReqDeviceInfoEntity.PRobotEntity robotEntity = new ReqDeviceInfoEntity.PRobotEntity();
//        reqEntity.deviceId = DeviceUtil.getAndroidID();
//        reqEntity.agentVersion = BuildConfig.VERSION_NAME;
//        reqEntity.serialNo = Global.getDeviceSn();
//        robotEntity.nickname = userEntity.name;
//        robotEntity.wxStatus = WxStatusEnum.WX_STATUS_ONLINE.getStatus();
//        robotEntity.robotUin = userEntity.remoteId;
//        robotEntity.tel = userEntity.mobile;
//        robotEntity.icon = userEntity.avatorUrl;
//        robotEntity.bbsId = enterpriseEntity.bbsId;
//        robotEntity.bbsName = enterpriseEntity.bbsName;
//        robotEntity.acctid = userEntity.acctid;
//        robotEntity.status = RobotStatusEnum.STATUS_NORMAL.getStatus();   //设备状态为0 正常;
//        reqEntity.baseInfo = DeviceUtil.getBaseInfo();
//        reqEntity.cpuInfo = CPUUtils.getCpuInfo();
//        reqEntity.osInfo = OSUtils.getOSInfoEntity();
//        reqEntity.netInfo = NetInfoUtil.getNetInfoEntity();
//        reqEntity.robotInfos.add(robotEntity);
//        reqEntity.platform = MConfiger.phoneLocEnum.getType();
//        MyLog.debug(TAG, "[sendDeviceInfo]" + " reqEntity:" + reqEntity);
//        long userId = LoginController.getInstance().getLoginUserId();
//        TransmitData<RobotMsg> msg = TransmitData.buildDefault(NettyHandleProtoEnum.DEVICE.getCode(), NettyHandleProtoEnum.PING.getActionType(), reqEntity);
//        SendDeviceImple sendDeviceImple = new SendDeviceImple(msg);
//        NettyEngine.getInstance().sendMsg(sendDeviceImple, false, false);
    }

    /**
     * 设备退登
     *
     * @param userEntity
     */
    public void deviceLogout(UserEntity userEntity) {
//        MyLog.debug(TAG, "[deviceLogout]" + "...", true);
//        ReqDeviceInfoEntity reqEntity = new ReqDeviceInfoEntity();
//        reqEntity.robotInfos = new ArrayList<>();
//        ReqDeviceInfoEntity.PRobotEntity robotEntity = new ReqDeviceInfoEntity.PRobotEntity();
//        reqEntity.deviceId = DeviceUtil.getAndroidID();
//        reqEntity.agentVersion = BuildConfig.VERSION_NAME;
//        reqEntity.serialNo = Global.getDeviceSn();
//        robotEntity.nickname = userEntity.name;
//        robotEntity.wxStatus = WxStatusEnum.WX_STATUS_LOGOUT.getStatus();
//        robotEntity.robotUin = userEntity.remoteId;
//        robotEntity.tel = userEntity.mobile;
//        robotEntity.icon = userEntity.avatorUrl;
//        robotEntity.bbsId = null;
//        robotEntity.bbsName = null;
//        robotEntity.status = RobotStatusEnum.STATUS_OFFLINE.getStatus();
//        reqEntity.platform = MConfiger.phoneLocEnum.getType();
//        reqEntity.robotInfos.add(robotEntity);
//        long userId = LoginController.getInstance().getLoginUserId();
//        TransmitData<RobotMsg> msg = TransmitData.buildDefault(NettyHandleProtoEnum.DEVICE.getCode(), NettyHandleProtoEnum.PING.getActionType(), reqEntity);
//        SendDeviceImple sendDeviceImple = new SendDeviceImple(msg);
//        NettyEngine.getInstance().sendMsg(sendDeviceImple, false, true);
//        StatsHelper.event("msgReport", "reportlog", "deviceLogout", "reqEntity " + reqEntity);
    }

    public void deviceAccountBan(UserEntity userEntity) {
//        MyLog.debug(TAG, "[deviceAccountBan]" + "...", true);
//        ReqDeviceInfoEntity reqEntity = new ReqDeviceInfoEntity();
//        reqEntity.robotInfos = new ArrayList<>();
//        ReqDeviceInfoEntity.PRobotEntity robotEntity = new ReqDeviceInfoEntity.PRobotEntity();
//        reqEntity.deviceId = DeviceUtil.getAndroidID();
//        reqEntity.agentVersion = BuildConfig.VERSION_NAME;
//        reqEntity.serialNo = Global.getDeviceSn();
//        robotEntity.nickname = userEntity.name;
//        robotEntity.wxStatus = WxStatusEnum.WX_STATUS_BAN.getStatus();
//        robotEntity.robotUin = userEntity.remoteId;
//        robotEntity.tel = userEntity.mobile;
//        robotEntity.icon = userEntity.avatorUrl;
//        robotEntity.bbsId = null;
//        robotEntity.bbsName = null;
//        robotEntity.status = RobotStatusEnum.STATUS_OFFLINE.getStatus();
//        reqEntity.platform = MConfiger.phoneLocEnum.getType();
//        reqEntity.robotInfos.add(robotEntity);
//        long userId = LoginController.getInstance().getLoginUserId();
//        TransmitData<RobotMsg> msg = TransmitData.buildDefault(NettyHandleProtoEnum.DEVICE.getCode(), NettyHandleProtoEnum.PING.getActionType(), reqEntity);
//        SendDeviceImple sendDeviceImple = new SendDeviceImple(msg);
//        NettyEngine.getInstance().sendMsg(sendDeviceImple, false, true);
//        StatsHelper.event("msgReport", "reportlog", "deviceAccountBan", "reqEntity " + reqEntity);
    }

    public void deviceAccountUpdate(UserEntity userEntity) {
//        MyLog.debug(TAG, "[deviceAccountUpdate]" + "...", true);
//        ReqDeviceInfoEntity reqEntity = new ReqDeviceInfoEntity();
//        reqEntity.robotInfos = new ArrayList<>();
//        ReqDeviceInfoEntity.PRobotEntity robotEntity = new ReqDeviceInfoEntity.PRobotEntity();
//        reqEntity.deviceId = DeviceUtil.getAndroidID();
//        reqEntity.agentVersion = BuildConfig.VERSION_NAME;
//        reqEntity.serialNo = Global.getDeviceSn();
//        robotEntity.nickname = userEntity.name;
//        robotEntity.wxStatus = WxStatusEnum.WX_STATUS_UPDATE.getStatus();
//        robotEntity.robotUin = userEntity.remoteId;
//        robotEntity.tel = userEntity.mobile;
//        robotEntity.icon = userEntity.avatorUrl;
//        robotEntity.bbsId = null;
//        robotEntity.bbsName = null;
//        robotEntity.status = RobotStatusEnum.STATUS_NORMAL.getStatus();
//        reqEntity.platform = MConfiger.phoneLocEnum.getType();
//        reqEntity.robotInfos.add(robotEntity);
//        long userId = LoginController.getInstance().getLoginUserId();
//        TransmitData<RobotMsg> msg = TransmitData.buildDefault(NettyHandleProtoEnum.DEVICE.getCode(), NettyHandleProtoEnum.PING.getActionType(), reqEntity);
//        SendDeviceImple sendDeviceImple = new SendDeviceImple(msg);
//        NettyEngine.getInstance().sendMsg(sendDeviceImple, false, true);
//        StatsHelper.event("msgReport", "reportlog", "deviceAccountUpdate", "reqEntity " + reqEntity);
    }

    /**
     * 发送我的外部联系人
     *
     * @param mList
     */
    public void sendContactInfo(List<UserEntity> mList) {
//        List<ReqUserConvEntity> list = ReqUserConvEntity.convertByUList(mList);
//        NanoHttpClient.cacheUserConv(list);
//        long userId = LoginController.getInstance().getLoginUserId();
//        MyLog.debug(TAG, " 上报联系人列表 " + " 总包量 " + list.size(), true);
//        StatsHelper.event("msgReport", "reportlog", "sendContactInfo reqEntity size" + list.size());
//        for (int i = 0; i < list.size(); i++) {
//            ReqUserConvEntity reqEntity = list.get(i);
//            reqEntity.robotUin = userId;
//            TransmitData<RobotMsg> msg = TransmitData.buildDefault(NettyHandleProtoEnum.USER_CONV_INFO.getCode(), NettyHandleProtoEnum.PING.getActionType(), reqEntity);
//            SendUserConvImple sendUserConvImple = new SendUserConvImple(msg);
//            NettyEngine.getInstance().sendMsg(sendUserConvImple);
//        }
    }

    /**
     * 发送我的外部联系人
     *
     * @param userData
     */
    public void sendContactRemarkInfo(UserData userData) {
//        MyLog.debug(TAG, " 上报联系人 " + " userData " + StrUtils.objectToJson(userData), true);
//        long userId = LoginController.getInstance().getLoginUserId();
//        ReqUserConvEntity reqEntity = new ReqUserConvEntity();
//        reqEntity.contactInfos = new ArrayList<>();
//        ReqUserConvEntity.PContractEntity pContractEntity = new ReqUserConvEntity.PContractEntity();
//        pContractEntity.remoteId = userData.getRemoteId();
//        pContractEntity.realRemark = userData.getRealRemark();
//        if (userData.getInfo() != null && userData.getInfo().getAvatorUrl() != null && !userData.getInfo().getAvatorUrl().isEmpty()) {
//            pContractEntity.avatorUrl = userData.getInfo().getAvatorUrl();
//        }
//        reqEntity.contactInfos.add(pContractEntity);
//        reqEntity.robotUin = userId;
//        StatsHelper.event("msgReport", "reportlog", "sendContactInfo", "reqEntity " + StrUtils.objectToJson(reqEntity));
//        TransmitData<RobotMsg> msg = TransmitData.buildDefault(NettyHandleProtoEnum.USER_CONV_INFO.getCode(), NettyHandleProtoEnum.PING.getActionType(), reqEntity);
//        SendUserConvImple sendUserConvImple = new SendUserConvImple(msg);
//        NettyEngine.getInstance().sendMsg(sendUserConvImple);
    }

    /**
     * 发送我的外部联系人
     */
    public void sendContactBlackListInfo(long remoteId, int isInBlackList) {
//        MyLog.debug(TAG, " 上报联系人 " + " userData " + remoteId + " isInBlackList " + isInBlackList, true);
//        long userId = LoginController.getInstance().getLoginUserId();
//        ReqUserConvEntity reqEntity = new ReqUserConvEntity();
//        reqEntity.contactInfos = new ArrayList<>();
//        ReqUserConvEntity.PContractEntity pContractEntity = new ReqUserConvEntity.PContractEntity();
//        pContractEntity.remoteId = remoteId;
//        pContractEntity.isInBlackList = isInBlackList;
//        reqEntity.contactInfos.add(pContractEntity);
//        reqEntity.robotUin = userId;
//        StatsHelper.event("msgReport", "reportlog", "sendContactInfo", "reqEntity " + StrUtils.objectToJson(reqEntity));
//        TransmitData<RobotMsg> msg = TransmitData.buildDefault(NettyHandleProtoEnum.USER_CONV_INFO.getCode(), NettyHandleProtoEnum.PING.getActionType(), reqEntity);
//        SendUserConvImple sendUserConvImple = new SendUserConvImple(msg);
//        NettyEngine.getInstance().sendMsg(sendUserConvImple);
    }


    /**
     * 发送会话列表
     *
     * @param mList
     */
    public void sendConvListInfo(List<ConvEntity> mList) {
//        List<ReqUserConvEntity> reqEntitys = ReqUserConvEntity.convertByConvList(mList);
//        MyLog.debug(TAG, "[sendConvListInfo]" + " size:" + reqEntitys.size(), true);
////        StatsHelper.event("msgReport", "callback", "sendConvListInfo", "reqEntity1 " + reqEntitys.size());
//        for (ReqUserConvEntity reqUserConvEntity : reqEntitys) {
//            sendConv(reqUserConvEntity);
//        }
    }

    private void sendConv(ReqUserConvEntity reqEntity) {
//        long userId = LoginController.getInstance().getLoginUserId();
//        reqEntity.robotUin = userId;
//        TransmitData<RobotMsg> msg = TransmitData.buildDefault(NettyHandleProtoEnum.USER_CONV_INFO.getCode(), NettyHandleProtoEnum.PING.getActionType(), reqEntity);
//        SendUserConvImple sendUserConvImple = new SendUserConvImple(msg);
//        NettyEngine.getInstance().sendMsg(sendUserConvImple, false, false);
    }

    /**
     * 发送会话列表
     *
     * @param item
     */
    public void sendConvListInfo(ConvEntity item) {
//        ReqUserConvEntity reqEntity = ReqUserConvEntity.convertByConv(item);
//        MyLog.debug(TAG, " 上报会话数据包name  = " + item.name + " size =" + JSON.toJSONString(reqEntity), false);
//        sendConv(reqEntity);
    }

    /**
     * 收到消息上报到服务端
     *
     * @param mList
     */
//    public void sendMsgListInfo(List<MsgEntity> mList) {
//        long userId = LoginController.getInstance().getLoginUserId();
//        ReqMsgRecvEntity reqEntity = new ReqMsgRecvEntity();
//        List<ReqMsgRecvEntity.PMsgItemEntity> rList = ReqMsgRecvEntity.convertByMsgList(mList);
//        reqEntity.messages = rList;
//        reqEntity.robotUin = userId;
//        TransmitData<RobotMsg> msg = TransmitData.buildDefault(NettyHandleProtoEnum.MSG_RECV.getCode(), userId, reqEntity);
//        SendCommonImple sendCommonImple = new SendCommonImple(msg);
//        NettyEngine.getInstance().sendMsg(sendCommonImple, false, true);
//    }

    /**
     * 接收消息上报
     *
     * @param msgEntity
     */
    public void sendMsgEntity(MsgEntity msgEntity, String from) {
//        long userId = LoginController.getInstance().getLoginUserId();
//        ReqMsgRecvEntity reqEntity = new ReqMsgRecvEntity();
//        List<ReqMsgRecvEntity.PMsgItemEntity> rList = ReqMsgRecvEntity.convertByMsgEntity(msgEntity);
//        reqEntity.messages = rList;
//        reqEntity.robotUin = userId;
//        TransmitData<RobotMsg> msg = TransmitData.buildDefault(NettyHandleProtoEnum.MSG_RECV.getCode(), NettyHandleProtoEnum.PING.getActionType(), reqEntity);
//        SendCommonImple sendCommonImple = new SendCommonImple(msg);
//        NettyEngine.getInstance().sendMsg(sendCommonImple);
//        MyLog.debug(TAG, "updateMessageCompete  messageId=" + msgEntity.msgId + " upContent=" + msgEntity.content + " msgEntity " + StrUtils.objectToJson(msgEntity));
//        StatsHelper.event("msgReport", "updateMessage", "from " + from + " messageId " + msgEntity.msgId + " upContent=" + msgEntity.content, StrUtils.objectToJson(msgEntity));
//        WeWorkMessageService.getInstance().updateMessageComplete(msgEntity.msgId, msgEntity.content, from);
    }

    /**
     * oss上报回调通知
     *
     * @param msgEntity
     * @param isSucc
     * @param failMsg
     */
    public void sendOssMsgNotify(MsgEntity msgEntity, boolean isSucc, String failMsg) {
//        Long userId = LoginController.getInstance().getLoginUserId();
//        ReqOssNotityEntity reqEntity = new ReqOssNotityEntity();
//        reqEntity.robotUin = userId;
//        reqEntity.noticeType = 100;
//        reqEntity.noticeBody = ReqOssNotityEntity.buildNoticeBody(msgEntity, isSucc, failMsg);
//        TransmitData<RobotMsg> msg = TransmitData.buildDefault(NettyHandleProtoEnum.OSS_NOTIFY.getCode(), NettyHandleProtoEnum.PING.getActionType(), reqEntity);
//        SendCommonImple sendCommonImple = new SendCommonImple(msg);
//        NettyEngine.getInstance().sendMsg(sendCommonImple);
//        StatsHelper.event("msgReport", "callback", "sendOssMsgNotify", "reqEntity " + reqEntity);
    }

    public void sendCompensateNotice(String uid, Integer type, String content) {
//        Long userId = LoginController.getInstance().getLoginUserId();
//        ReqOssNotityEntity reqEntity = new ReqOssNotityEntity();
//        reqEntity.robotUin = userId;
//        reqEntity.noticeType = 110;
//        ActionRet actionRet = new ActionRet();
//        actionRet.finishMatters = new ArrayList<>();
//        ActionRet.FinishMatters fMatter = new ActionRet.FinishMatters();
//        fMatter.info = content;
//        fMatter.type = type;
//        actionRet.finishMatters.add(fMatter);
//        reqEntity.noticeBody = ReqOssNotityEntity.buildNoticeBody(uid, actionRet);
//        TransmitData<RobotMsg> msg = TransmitData.buildDefault(NettyHandleProtoEnum.OSS_NOTIFY.getCode(), NettyHandleProtoEnum.PING.getActionType(), reqEntity);
//        SendCommonImple sendCommonImple = new SendCommonImple(msg);
//        NettyEngine.getInstance().sendMsg(sendCommonImple);
//        StatsHelper.event("msgReport", "reportlog", "sendCompensateNotice", "reqEntity " + reqEntity);
    }

//    public void sendFailCompensateNotice(String uid, int type, String content, String errorMessage) {
//        Long userId = LoginController.getInstance().getLoginUserId();
//        ReqOssNotityEntity reqEntity = new ReqOssNotityEntity();
//        reqEntity.robotUin = userId;
//        reqEntity.noticeType = 110;
//        ActionRet actionRet = new ActionRet();
//        actionRet.failExpectMatters = new ArrayList<>();
//        ActionRet.FailExpectMatters fMatter = new ActionRet.FailExpectMatters();
//        fMatter.info = content;
//        fMatter.type = type;
//        fMatter.errorMessage = errorMessage;
//        actionRet.failExpectMatters.add(fMatter);
//        reqEntity.noticeBody = ReqOssNotityEntity.buildNoticeBody(uid, actionRet);
//        TransmitData<RobotMsg> msg = TransmitData.buildDefault(NettyHandleProtoEnum.OSS_NOTIFY.getCode(), userId, reqEntity);
//        SendCommonImple sendCommonImple = new SendCommonImple(msg);
//        NettyEngine.getInstance().sendMsg(sendCommonImple);
//    }

    /**
     * 同步扩容状态
     *
     * @param isShow
     */
    public void sendExpansionStatus(boolean isShow) {
//        ReqExpansionStatusEntity reqEntity = new ReqExpansionStatusEntity();
//        Long userId = LoginController.getInstance().getLoginUserId();
//        reqEntity.robotUin = userId;
//        reqEntity.noticeType = 120;
//        reqEntity.noticeBody = new ReqExpansionStatusEntity.PExpansionStatus();
//        ////1没有 2展示有
//        if (isShow) {
//            reqEntity.noticeBody.status = 2;
//        } else {
//            reqEntity.noticeBody.status = 1;
//        }
//        TransmitData<RobotMsg> msg = TransmitData.buildDefault(NettyHandleProtoEnum.OSS_NOTIFY.getCode(), NettyHandleProtoEnum.PING.getActionType(), reqEntity);
//        SendCommonImple sendCommonImple = new SendCommonImple(msg);
//        NettyEngine.getInstance().sendMsg(sendCommonImple);
    }

    /**
     * 消息执行callback
     *
     * @param uid
     */
    public void sendMsgReportCallbackSuccess(long robotUin, String uid, long actionType, long actionSubType) {
//        sendMsgReportCallback(robotUin, uid, ActionStatusEnum.SUCC, "执行成功", actionType, actionSubType);
    }

    /**
     * 消息执行callback
     *
     * @param uid
     * @param actionStatusEnum
     * @param actionInfo
     */
    public void sendMsgReportCallback(long robotUin, String uid, ActionStatusEnum actionStatusEnum, String actionInfo, long actionType, long actionSubType) {
      /*  recordActionHistory(uid, actionStatusEnum, actionInfo, null, null);
        long userId = LoginController.getInstance().getLoginUserId();   //(Long robotUin,String uid, ActionStatusEnum actionStatusEnum,String actionInfo){
        ReqMsgCallBackEntity reqEntity = ReqMsgCallBackEntity.buildReqCallBackEntity(robotUin, uid, actionStatusEnum, actionInfo, "");
        TransmitData<RobotMsg> msg = TransmitData.buildDefault(NettyHandleProtoEnum.MSG_CALLBACK.getCode(), userId, reqEntity);
        SendCommonImple sendCommonImple = new SendCommonImple(msg);
        NettyEngine.getInstance().sendMsg(sendCommonImple);*/
//        sendMsgReportCallback(robotUin, uid, actionStatusEnum, actionInfo, null, actionType, actionSubType);
    }

    public void sendMsgReportCallbackErr(long robotUin, String uid, ActionStatusEnum actionStatusEnum, String actionInfo, Collection<ActionResultEnum.PActionResultItem> errList, long actionType, long actionSubType) {
     /*   recordActionHistory(uid, actionStatusEnum, null, null, null);
        long userId = LoginController.getInstance().getLoginUserId();   //(Long robotUin,String uid, ActionStatusEnum actionStatusEnum,String actionInfo){
        ReqMsgCallBackEntity reqEntity = ReqMsgCallBackEntity.buildReqCallBackEntity(robotUin, uid, actionStatusEnum, null, errList);
        TransmitData<RobotMsg> msg = TransmitData.buildDefault(NettyHandleProtoEnum.MSG_CALLBACK.getCode(), userId, reqEntity);
        SendCommonImple sendCommonImple = new SendCommonImple(msg);
        NettyEngine.getInstance().sendMsg(sendCommonImple);*/
//        sendMsgReportCallback(robotUin, uid, actionStatusEnum, actionInfo, errList, actionType, actionSubType);
    }

    public void sendMsgReportCallback(long robotUin, String uid, ActionStatusEnum actionStatusEnum, String actionInfo, Object actionResult, long actionType, long actionSubType) {
       /* recordActionHistory(uid, actionStatusEnum, actionInfo, actionResult, null);
        long userId = LoginController.getInstance().getLoginUserId();   //(Long robotUin,String uid, ActionStatusEnum actionStatusEnum,String actionInfo){
        ReqMsgCallBackEntity reqEntity = ReqMsgCallBackEntity.buildReqCallBackEntity(robotUin, uid, actionStatusEnum, actionInfo, actionResult);
        TransmitData<RobotMsg> msg = TransmitData.buildDefault(NettyHandleProtoEnum.MSG_CALLBACK.getCode(), userId, reqEntity);
        SendCommonImple sendCommonImple = new SendCommonImple(msg);
        NettyEngine.getInstance().sendMsg(sendCommonImple);*/
//        sendMsgReportCallback(robotUin, uid, actionStatusEnum, actionInfo, actionResult, null, actionType, actionSubType);
    }

    public void sendMsgReportCallback(long robotUin, String uid, ActionStatusEnum actionStatusEnum, String actionInfo, Object actionResult, ActionRet tempAR, long actionType, long actionSubType) {
//        recordActionHistory(uid, actionStatusEnum, actionInfo, actionResult, tempAR);
//        long userId = LoginController.getInstance().getLoginUserId();
//        ReqMsgCallBackEntity reqEntity;
//        if (!TextUtils.isEmpty(actionInfo) && actionStatusEnum != ActionStatusEnum.SUCC && !actionInfo.startsWith("{")) {
//            actionInfo = ActionResultEnum.ACTION_ROBOT_RESULT.getMsg(actionInfo);
//        }
//        if (tempAR == null) {
//            reqEntity = ReqMsgCallBackEntity.buildReqCallBackEntity(robotUin, uid, actionStatusEnum, actionInfo, actionResult);
//        } else {
//            reqEntity = ReqMsgCallBackEntity.buildReqCallBackEntity(robotUin, uid, actionStatusEnum, actionInfo, actionResult, tempAR);
//        }
//        TransmitData<RobotMsg> msg = TransmitData.buildDefault(NettyHandleProtoEnum.MSG_CALLBACK.getCode(), NettyHandleProtoEnum.PING.getActionType(), reqEntity);
//        SendCommonImple sendCommonImple = new SendCommonImple(msg);
//        NettyEngine.getInstance().sendMsg(sendCommonImple);
//        TaskService.getInstance().completeTask(uid, JSON.toJSONString(reqEntity), actionStatusEnum.getStatus(), actionInfo == null ? JSON.toJSONString(tempAR) : actionInfo);
//        StatsHelper.event("msgReport", "callback", "reqEntity " + JSON.toJSONString(reqEntity), "actionInfo " + actionInfo, uid, actionType, actionSubType);
    }

    //
    private void recordActionHistory(String uid, ActionStatusEnum actionStatusEnum, String actionInfo, Object actionResult, ActionRet tempAR) {
        //输出日志

        //todo 队列移除已执行
        //根据uid查找并更新相关任务
//        if (actionStatusEnum == ActionStatusEnum.FAILURE || actionStatusEnum == ActionStatusEnum.REPEAT) {
//            synchronized (HandleActionNew.g_ActionHistoryEntityList) {
//                HandleActionNew.g_ActionHistoryEntityList.remove(uid);
//            }
//        } else {
//            synchronized (HandleActionNew.g_ActionHistoryEntityList) {
//                ActionHistoryEntity actionHistoryEntity = HandleActionNew.g_ActionHistoryEntityList.get(uid);
//                if (actionHistoryEntity != null) {
//                    actionHistoryEntity.action_exec_finished_time = new Date();
//                }
//            }
//        }
//        MyLog.debug(TAG, "任务完成后回上报服务端. uid=" + uid + "   actionStatusEnum=" + actionStatusEnum.getDesc() + "    actionInfo=" + actionInfo + "    actionResult=" + new Gson().toJson(actionResult) + "    tempAR=" + new Gson().toJson(tempAR), true);
    }
}
