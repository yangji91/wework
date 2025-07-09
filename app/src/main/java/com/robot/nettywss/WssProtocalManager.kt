package com.robot.nettywss

import com.google.gson.Gson
import com.robot.com.database.service.WeWorkMessageService
import com.robot.controller.LoginController
import com.robot.entity.ConvEntity
import com.robot.entity.MsgEntity
import com.robot.entity.OriginMsgEntity
import com.robot.entity.ReqAddContactResultEntity
import com.robot.entity.ReqCircleInfoEntity
import com.robot.entity.ReqCircleInfoEntity.CircleInfoEntity
import com.robot.entity.ReqInviteWxToGroupResultEntity
import com.robot.entity.ReqMsgResultEntity
import com.robot.entity.UserEntity
import com.robot.net.NanoHttpClient
import com.robot.netty.NettyHandleProtoEnum
import com.robot.netty.entity.ReqBaseEntity
import com.robot.netty.handle.ActionEnum
import com.robot.netty.proto.WssSendMessageImpl
import com.robot.netty.proto.req.ReqConvEntity
import com.robot.netty.proto.req.ReqHeartEntity
import com.robot.netty.proto.req.ReqMiniAppEntity
import com.robot.netty.proto.req.ReqMsgEntity
import com.robot.netty.proto.req.ReqMsgLinkEntity
import com.robot.netty.proto.req.ReqUserEntity
import com.robot.nettywss.proto.WssSendHeartBeatImpl
import com.robot.util.MyLog
import com.robot.util.StrUtils
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

object WssProtocalManager {
    private val TAG: String = WssProtocalManager::class.java.simpleName

    /**
     * 发送心跳
     */
    @JvmStatic
    fun sendHeartBeat() {
        val reqHeartEntity = ReqHeartEntity()
        reqHeartEntity.self_headurl = LoginController.getInstance().loginUser.avatorUrl ?: ""
        reqHeartEntity.self_nickname = LoginController.getInstance().loginUserName ?: ""
        reqHeartEntity.bbsName = LoginController.getInstance().enterpriseInfo.bbsName ?: ""
        reqHeartEntity.phoneNum = LoginController.getInstance().getLoginMobile() ?: ""
        val req = ReqBaseEntity<ReqHeartEntity>().build(
            UUID.randomUUID().toString(),
            NettyHandleProtoEnum.HEAT_BEAT.actionType,
            "",
            "",
            reqHeartEntity
        )
        MyLog.debug("sendHeartBeat ", req)
        val heartBeatImpl = WssSendHeartBeatImpl(TextWebSocketFrame(req))
        WssNettyEngine.getInstance().sendMsg(heartBeatImpl)
    }

    /**
     * 发送心跳
     */
    @JvmStatic
    fun sendHeartBeatWithoutContent() {
        val reqHeartEntity = ReqHeartEntity()
        val req = ReqBaseEntity<ReqHeartEntity>().build(
            UUID.randomUUID().toString(),
            NettyHandleProtoEnum.HEAT_BEAT.actionType,
            "",
            "",
            reqHeartEntity
        )
        MyLog.debug("sendHeartBeat ", req)
        val heartBeatImpl = WssSendHeartBeatImpl(TextWebSocketFrame(req))
        WssNettyEngine.getInstance().sendMsg(heartBeatImpl)
    }

    /**
     * 接收消息上报
     *
     * @param msgEntity
     */
    @JvmStatic
    fun sendMsgEntity(msgEntity: MsgEntity, msgType: String) {
        // long userId = LoginController.getInstance().getLoginUserId();
        // ReqMsgRecvEntity reqEntity = new ReqMsgRecvEntity();
        // List<ReqMsgRecvEntity.PMsgItemEntity> rList = ReqMsgRecvEntity.convertByMsgEntity(msgEntity);
        // reqEntity.messages = rList;
        // reqEntity.robotUin = userId;
        val reqMsgEntity = ReqMsgEntity()
        reqMsgEntity.senderRemoteId = msgEntity.sender.toString()
        if (msgEntity.sender.toString() == LoginController.getInstance().loginUserId.toString()) {
            reqMsgEntity.receiverRemoteId = msgEntity.conversationId.toString()
        } else {
            reqMsgEntity.receiverRemoteId = LoginController.getInstance().loginUserId.toString()
        }
        reqMsgEntity.contentType = msgEntity.contentType
        reqMsgEntity.msgId = msgEntity.msgId
        if (msgType == "txt") {
            reqMsgEntity.content = msgEntity.content
        } else if (msgType == "img") {
            reqMsgEntity.imgUrl = msgEntity.content
        } else if (msgType == "link") {
            val linkMsg: ReqMsgLinkEntity =
                Gson().fromJson<ReqMsgLinkEntity>(msgEntity.content, ReqMsgLinkEntity::class.java)
            reqMsgEntity.linkTitle = linkMsg.title ?: ""
            reqMsgEntity.linkDesc = linkMsg.description ?: ""
            reqMsgEntity.linkImgUrl = linkMsg.imageUrl ?: ""
            reqMsgEntity.linkUrl = linkMsg.linkUrl ?: ""
        } else if (msgType == "miniProgram") {
            val miniProgramMsg: ReqMiniAppEntity =
                Gson().fromJson<ReqMiniAppEntity>(msgEntity.content, ReqMiniAppEntity::class.java)
            reqMsgEntity.miniProgramThumbUrl = miniProgramMsg.imgUrl ?: ""
            reqMsgEntity.miniProgramTitle = miniProgramMsg.title ?: ""
            reqMsgEntity.miniProgramUsername = miniProgramMsg.username ?: ""
            reqMsgEntity.miniProgramAppId = miniProgramMsg.appid ?: ""
            reqMsgEntity.miniProgramLogoUrl = miniProgramMsg.logo ?: ""
            reqMsgEntity.miniProgramAppName = miniProgramMsg.appName ?: ""
            reqMsgEntity.miniProgramPagePath = miniProgramMsg.pagePath ?: ""
        } else if (msgType == "personCard") {
            reqMsgEntity.content = Gson().toJson(msgEntity.personalCardMsgEntity)
        } else if (msgType == "diyEmoji") {
            val originMsg: OriginMsgEntity = Gson().fromJson<OriginMsgEntity>(
                StrUtils.objectToJson(msgEntity.originMsg), OriginMsgEntity::class.java
            )
            reqMsgEntity.content = originMsg.info?.content ?: ""
        } else if (msgType == "tip") {
            //todo
            val originMsg: OriginMsgEntity = Gson().fromJson<OriginMsgEntity>(
                StrUtils.objectToJson(msgEntity.originMsg), OriginMsgEntity::class.java
            )
            reqMsgEntity.content = originMsg.info?.content ?: ""
        } else {
            reqMsgEntity.content = msgEntity.content ?: ""
        }
//        reqMsgEntity.nickname = msgEntity.sendername ?: ""
        val req = ReqBaseEntity<ReqMsgEntity>().build(
            UUID.randomUUID().toString(),
            NettyHandleProtoEnum.UPLOAD_MSG.actionType,
            "msg",
            msgType,
            reqMsgEntity
        )
        // MyLog.debug(TAG, "updateMessageCompete  messageId=" + msgEntity.msgId + " upContent=" + msgEntity.content + " msgEntity " + StrUtils.objectToJson(msgEntity));
        WeWorkMessageService.getInstance()
            .updateMessageComplete(msgEntity.msgId, msgEntity.content, msgType)
        MyLog.debug("sendMsgEntity ", req, true)
        val sssSendMessageImpl = WssSendMessageImpl(TextWebSocketFrame(req))
        WssNettyEngine.getInstance().sendMsg(sssSendMessageImpl)
    }

    /**
     * 接收消息上报
     *
     * @param msgEntity
     */
    @JvmStatic
    fun sendWxMsgEntity(
        type: Int, msgType: String, msgSvrId: Long, talker: String, content: String
    ) {
        val reqMsgEntity = ReqMsgEntity()
        reqMsgEntity.contentType = type
        reqMsgEntity.msgId = msgSvrId
        reqMsgEntity.content = content
        reqMsgEntity.senderRemoteId = talker
        val req = ReqBaseEntity<ReqMsgEntity>().build(
            UUID.randomUUID().toString(),
            NettyHandleProtoEnum.UPLOAD_MSG.actionType,
            "msg",
            msgType,
            reqMsgEntity
        )
        MyLog.debug("sendWxMsgEntity ", req, true)
        val sssSendMessageImpl = WssSendMessageImpl(TextWebSocketFrame(req))
        WssNettyEngine.getInstance().sendMsg(sssSendMessageImpl)
    }

    /**
     * 发送消息的回调
     */
    @JvmStatic
    fun sendMsgResultEntity(reqid: String?, msgType: String?, code: Int, msg: String?) {
        // long userId = LoginController.getInstance().getLoginUserId();
        val reqEntity = ReqMsgResultEntity(code, msg)
        // List<ReqMsgRecvEntity.PMsgItemEntity> rList = ReqMsgRecvEntity.convertByMsgEntity(msgEntity);
        // reqEntity.messages = rList;
        // reqEntity.robotUin = userId;
        val req = ReqBaseEntity<ReqMsgResultEntity>().build(
            reqid ?: "",
            NettyHandleProtoEnum.UPLOAD_MSG.actionType,
            ActionEnum.PUSH_USER_MEDIA_MSG.taskType,
            msgType ?: "",
            reqEntity
        )
        // MyLog.debug(TAG, "updateMessageCompete  messageId=" + msgEntity.msgId + " upContent=" + msgEntity.content + " msgEntity " + StrUtils.objectToJson(msgEntity));
        MyLog.debug("sendMsgResultEntity ", req, true)
        val sssSendMessageImpl = WssSendMessageImpl(TextWebSocketFrame(req))
        WssNettyEngine.getInstance().sendMsg(sssSendMessageImpl)
    }

    /**
     * 添加联系人的回调
     */
    @JvmStatic
    fun sendAddContactResultEntity(
        reqid: String?, phonenum: String?, remoteId: String, code: Int, msg: String?
    ) {
        // long userId = LoginController.getInstance().getLoginUserId();
        val reqEntity = ReqAddContactResultEntity(ReqAddContactResultEntity.Result().apply {
            this.phonenum = phonenum
            this.remoteId = remoteId
            this.msg = msg
            this.code = code
        })
        // List<ReqMsgRecvEntity.PMsgItemEntity> rList = ReqMsgRecvEntity.convertByMsgEntity(msgEntity);
        // reqEntity.messages = rList;
        // reqEntity.robotUin = userId;
        val req = ReqBaseEntity<ReqAddContactResultEntity>().build(
            reqid ?: "",
            NettyHandleProtoEnum.UPLOAD_MSG.actionType,
            "contact",
            "searchAndAddContact",
            reqEntity
        )
        // MyLog.debug(TAG, "updateMessageCompete  messageId=" + msgEntity.msgId + " upContent=" + msgEntity.content + " msgEntity " + StrUtils.objectToJson(msgEntity));
        MyLog.debug("sendAddContactResultEntity ", req, true)
        val sssSendMessageImpl = WssSendMessageImpl(TextWebSocketFrame(req))
        WssNettyEngine.getInstance().sendMsg(sssSendMessageImpl)
    }

    /**
     * 微信通过 微信好友的 申请添加好友的申请后添加的回调
     */
    @JvmStatic
    fun sendWechatAddContactResultEntity(
        reqid: String?, msgType: String, wxId: String, code: Int, msg: String?
    ) {
        // long userId = LoginController.getInstance().getLoginUserId();
        val reqEntity = ReqAddContactResultEntity(ReqAddContactResultEntity.Result().apply {
            this.wxId = wxId
            this.msg = msg
            this.code = code
        })
        // List<ReqMsgRecvEntity.PMsgItemEntity> rList = ReqMsgRecvEntity.convertByMsgEntity(msgEntity);
        // reqEntity.messages = rList;
        // reqEntity.robotUin = userId;
        val req = ReqBaseEntity<ReqAddContactResultEntity>().build(
            reqid ?: "", NettyHandleProtoEnum.UPLOAD_MSG.actionType, "contact", msgType, reqEntity
        )
        // MyLog.debug(TAG, "updateMessageCompete  messageId=" + msgEntity.msgId + " upContent=" + msgEntity.content + " msgEntity " + StrUtils.objectToJson(msgEntity));
        MyLog.debug("msgType ", req, true)
        val sssSendMessageImpl = WssSendMessageImpl(TextWebSocketFrame(req))
        WssNettyEngine.getInstance().sendMsg(sssSendMessageImpl)
    }

    /**
     * 企微通过微信好友的 申请添加好友的申请后添加的回调
     */
    @JvmStatic
    fun sendWeComAdoptedWechatFriendInfoEntity(
        reqid: String?,
        msgType: String,
        wxProfileCode: String,
        wxId: String,
        code: Int,
        msg: String?
    ) {
        // long userId = LoginController.getInstance().getLoginUserId();
        val reqEntity = ReqAddContactResultEntity(ReqAddContactResultEntity.Result().apply {
            this.wxProfileCode = wxProfileCode
            this.wxId = wxId
            this.msg = msg
            this.code = code
        })
        // List<ReqMsgRecvEntity.PMsgItemEntity> rList = ReqMsgRecvEntity.convertByMsgEntity(msgEntity);
        // reqEntity.messages = rList;
        // reqEntity.robotUin = userId;
        val req = ReqBaseEntity<ReqAddContactResultEntity>().build(
            reqid ?: "", NettyHandleProtoEnum.UPLOAD_MSG.actionType, "contact", msgType, reqEntity
        )
        // MyLog.debug(TAG, "updateMessageCompete  messageId=" + msgEntity.msgId + " upContent=" + msgEntity.content + " msgEntity " + StrUtils.objectToJson(msgEntity));
        MyLog.debug("msgType ", req, true)
        val sssSendMessageImpl = WssSendMessageImpl(TextWebSocketFrame(req))
        WssNettyEngine.getInstance().sendMsg(sssSendMessageImpl)
    }

    /**
     * 企微通过微信好友的 申请添加好友的申请后添加的回调
     */
    @JvmStatic
    fun sendWecomAddContactResultEntity(
        reqid: String?,
        msgType: String,
        wxProfileCode: String,
        remoteId: String,
        code: Int,
        msg: String?
    ) {
        // long userId = LoginController.getInstance().getLoginUserId();
        val reqEntity = ReqAddContactResultEntity(ReqAddContactResultEntity.Result().apply {
            this.wxProfileCode = wxProfileCode
            this.remoteId = remoteId
            this.msg = msg
            this.code = code
        })
        // List<ReqMsgRecvEntity.PMsgItemEntity> rList = ReqMsgRecvEntity.convertByMsgEntity(msgEntity);
        // reqEntity.messages = rList;
        // reqEntity.robotUin = userId;
        val req = ReqBaseEntity<ReqAddContactResultEntity>().build(
            reqid ?: "", NettyHandleProtoEnum.UPLOAD_MSG.actionType, "contact", msgType, reqEntity
        )
        // MyLog.debug(TAG, "updateMessageCompete  messageId=" + msgEntity.msgId + " upContent=" + msgEntity.content + " msgEntity " + StrUtils.objectToJson(msgEntity));
        MyLog.debug("msgType ", req, true)
        val sssSendMessageImpl = WssSendMessageImpl(TextWebSocketFrame(req))
        WssNettyEngine.getInstance().sendMsg(sssSendMessageImpl)
    }


    /**
     * 发送我的外部联系人
     */
    @JvmStatic
    fun sendAddContactInfo(userEntity: UserEntity) {
//        List<ReqUserConvEntity> list = ReqUserConvEntity.convertByUList();
//        NanoHttpClient.cacheAddUserConv(userEntity);
//        long userId = LoginController.getInstance().getLoginUserId();
        MyLog.debug(TAG, " 上报新增联系人 $userEntity", true)
        val reqUserEntity = ReqUserEntity()
        reqUserEntity.headUrl = userEntity.avatorUrl.toString()
        reqUserEntity.remoteId = userEntity.remoteId.toString()
        reqUserEntity.content = "我通过了你的联系人验证请求，现在我们可以开始聊天了"
        reqUserEntity.nickname = userEntity.name ?: ""
        val req = ReqBaseEntity<ReqUserEntity>().build(
            UUID.randomUUID().toString(),
            NettyHandleProtoEnum.UPLOAD_MSG.actionType,
            "contact",
            "useraddnotify",
            reqUserEntity
        )

        //        TransmitData<RobotMsg> msg = TransmitData.buildDefault(NettyHandleProtoEnum.USER_CONV_INFO.getCode(), NettyHandleProtoEnum.PING.getType(), reqEntity);
//        SendUserConvImple sendUserConvImple = new SendUserConvImple(msg);
//        NettyEngine.getInstance().sendMsg(sendUserConvImple);
        MyLog.debug("sendAddContactInfo ", req, true)
        val sssSendMessageImpl = WssSendMessageImpl(TextWebSocketFrame(req))
        WssNettyEngine.getInstance().sendMsg(sssSendMessageImpl)
        NanoHttpClient.cacheUser(userEntity)
    }

    /**
     * 发送我的外部联系人
     */
    @JvmStatic
    fun sendContactInfos(userEntitys: List<UserEntity>) {
        MyLog.debug(TAG, " 上报联系人s ${userEntitys.size}", true)
        MainScope().launch {
            withContext(Dispatchers.IO) {
                userEntitys.chunked(30).forEach {
                    val reqUsers = ArrayList<ReqUserEntity>()
                    it.forEach { userEntity ->
                        val reqUserEntity = ReqUserEntity()
                        reqUserEntity.headUrl = userEntity.avatorUrl.toString()
                        reqUserEntity.remoteId = userEntity.remoteId.toString()
                        //            reqUserEntity.content = "我通过了你的联系人验证请求，现在我们可以开始聊天了";
                        reqUserEntity.nickname = userEntity.name
                        reqUsers.add(reqUserEntity)
                    }
                    val req = ReqBaseEntity<List<ReqUserEntity>>().build(
                        UUID.randomUUID().toString(),
                        NettyHandleProtoEnum.UPLOAD_MSG.actionType,
                        "contact",
                        "contactList",
                        reqUsers
                    )

                    //        TransmitData<RobotMsg> msg = TransmitData.buildDefault(NettyHandleProtoEnum.USER_CONV_INFO.getCode(), NettyHandleProtoEnum.PING.getType(), reqEntity);
//        SendUserConvImple sendUserConvImple = new SendUserConvImple(msg);
//        NettyEngine.getInstance().sendMsg(sendUserConvImple);
                    MyLog.debug("sendContactInfos ", req, true)
                    val sssSendMessageImpl = WssSendMessageImpl(TextWebSocketFrame(req))
                    WssNettyEngine.getInstance().sendMsg(sssSendMessageImpl)
                    NanoHttpClient.cacheUsers(userEntitys)
                }
                cancel()
            }
        }
//        List<ReqUserConvEntity> list = ReqUserConvEntity.convertByUList();
//        NanoHttpClient.cacheAddUserConv(userEntity);
//        long userId = LoginController.getInstance().getLoginUserId();
    }

    /**
     * 发送会话列表
     *
     * @param convEntities
     */
    @JvmStatic
    fun sendConvListInfo(convEntities: List<ConvEntity?>) {
        MainScope().launch {
            withContext(Dispatchers.IO) {
                convEntities.chunked(30).forEach {
                    val reqConvs = ArrayList<ReqConvEntity>()
                    it.forEach { convEntity ->
                        val reqConvEntity = ReqConvEntity()
                        reqConvEntity.convId = convEntity?.id ?: 0
                        reqConvEntity.convName = convEntity?.name ?: ""
                        reqConvEntity.memberCount = convEntity?.memberCount ?: 0
                        reqConvs.add(reqConvEntity)
                    }
                    val req = ReqBaseEntity<List<ReqConvEntity>>().build(
                        UUID.randomUUID().toString(),
                        NettyHandleProtoEnum.UPLOAD_MSG.actionType,
                        "groupConv",
                        "groupConvList",
                        reqConvs
                    )
                    MyLog.debug("sendGroupConvList ", req, false)
                    val sssSendMessageImpl = WssSendMessageImpl(TextWebSocketFrame(req))
                    WssNettyEngine.getInstance().sendMsg(sssSendMessageImpl)
                    NanoHttpClient.cacheConvs(convEntities)
                }
                cancel()
            }
        }
    }

    /**
     * 发送我的外部联系人
     */
    @JvmStatic
    fun sendAddConvInfo(convEntity: ConvEntity) {
//        List<ReqUserConvEntity> list = ReqUserConvEntity.convertByUList();
//        NanoHttpClient.cacheAddUserConv(userEntity);
//        long userId = LoginController.getInstance().getLoginUserId();
        MyLog.debug(TAG, " 上报新增群会话 $convEntity", true)
        val reqConvEntity = ReqConvEntity()
        reqConvEntity.convId = convEntity.id
        reqConvEntity.convName = convEntity.name
        reqConvEntity.memberCount = convEntity.memberCount
        val req = ReqBaseEntity<ReqConvEntity>().build(
            UUID.randomUUID().toString(),
            NettyHandleProtoEnum.UPLOAD_MSG.actionType,
            "groupConv",
            "groupConvAddnotify",
            reqConvEntity
        )

        //        TransmitData<RobotMsg> msg = TransmitData.buildDefault(NettyHandleProtoEnum.USER_CONV_INFO.getCode(), NettyHandleProtoEnum.PING.getType(), reqEntity);
//        SendUserConvImple sendUserConvImple = new SendUserConvImple(msg);
//        NettyEngine.getInstance().sendMsg(sendUserConvImple);
        MyLog.debug("sendAddConvInfo ", req, true)
        val sssSendMessageImpl = WssSendMessageImpl(TextWebSocketFrame(req))
        WssNettyEngine.getInstance().sendMsg(sssSendMessageImpl)
        NanoHttpClient.cacheConv(convEntity)
    }

    /**
     * 企微通过微信好友的 申请添加好友的申请后添加的回调
     */
    @JvmStatic
    fun sendInviteWxToGroupResultEntity(
        reqid: String?,
        taskType: String,
        msgType: String,
        remoteId: Long?,
        convId: Long?,
        code: Int,
        msg: String?
    ) {
        // long userId = LoginController.getInstance().getLoginUserId();
        val reqEntity = ReqInviteWxToGroupResultEntity(code, msg).apply {
            this.remoteId = remoteId
            this.convId = convId
        }
        // List<ReqMsgRecvEntity.PMsgItemEntity> rList = ReqMsgRecvEntity.convertByMsgEntity(msgEntity);
        // reqEntity.messages = rList;
        // reqEntity.robotUin = userId;
        val req = ReqBaseEntity<ReqInviteWxToGroupResultEntity>().build(
            reqid ?: "", NettyHandleProtoEnum.UPLOAD_MSG.actionType, taskType, msgType, reqEntity
        )
        // MyLog.debug(TAG, "updateMessageCompete  messageId=" + msgEntity.msgId + " upContent=" + msgEntity.content + " msgEntity " + StrUtils.objectToJson(msgEntity));
        MyLog.debug("msgType ", req, true)
        val sssSendMessageImpl = WssSendMessageImpl(TextWebSocketFrame(req))
        WssNettyEngine.getInstance().sendMsg(sssSendMessageImpl)
    }

}
