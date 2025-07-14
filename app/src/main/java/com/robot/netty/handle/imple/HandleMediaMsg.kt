package com.robot.netty.handle.imple

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.robot.common.Global
import com.robot.controller.message.MessageController.SendMessageCallBack
import com.robot.controller.message.WeWorkMessageUtil
import com.robot.controller.resource.ResourceController
import com.robot.entity.SendHelperSendMsgEntity
import com.robot.entity.SendHelperSendMsgEntity.SendHelperSendImgEntity
import com.robot.exception.MessageException
import com.robot.netty.handle.BaseHandle
import com.robot.netty.handle.imple.entity.MessageEntity
import com.robot.nettywss.WssProtocalManager
import com.robot.util.MyLog


class HandleMediaMsg : BaseHandle() {
    override fun onHandle(data: String) {
        super.onHandle(data)

        MyLog.debug(
            TAG, " [HandleMediaMsg] " + "发送消息..." + data
        )

        val root: JsonObject = JsonParser().parse(data).getAsJsonObject()
        val resultJsonStr = root.get("result").getAsString()
        val resultObject = JsonParser().parse(resultJsonStr).getAsJsonObject()
        root.add("result", resultObject) // 替换掉原来的字符串字段


        val res: MessageEntity = Gson().fromJson<MessageEntity>(root, MessageEntity::class.java)
        val msgType: String = res.msgType ?: ""

        MyLog.debug(
            TAG, "msgType" + msgType + " [HandleMediaMsg]" + "发送消息..." + data
        )
        when (msgType) {
            "txt" -> {
                if ((res.result?.remoteId ?: return) > 0) {
                    if (res.result?.content?.isNotEmpty() == true) {
                        val message = WeWorkMessageUtil.buildTexMessage(
                            Global.loadPackageParam.classLoader, res.result?.content ?: ""
                        )
                        MyLog.debug(
                            TAG,
                            "msgType" + msgType + " [onHandle]" + message + "发送txt消息..." + data
                        )
                        sendPrivateMessage(
                            res.reqid, res.msgType, res.result?.remoteId!!, message
                        )
                    }
                }
            }

            "img" -> {
                if ((res.result?.remoteId ?: return) > 0) {
                    if (res.result?.content?.isNotEmpty() == true) {
                        res.result?.imgLocalPath =
                            SendHelperSendImgEntity(res.result?.content).localPath
                        val message = WeWorkMessageUtil.buildImageMessage(
                            Global.loadPackageParam.classLoader, res.result?.imgLocalPath ?: ""
                        )
                        MyLog.debug(
                            TAG,
                            "msgType" + msgType + " [onHandle]" + message + "发送img消息..." + data
                        )

                        sendPrivateMessage(
                            res.reqid, res.msgType, res.result?.remoteId!!, message
                        )
                    }
                }
            }

            "video" -> {
                if ((res.result?.remoteId ?: return) > 0) {
                    if (res.result?.content?.isNotEmpty() == true) {
                        res.result?.videoLocalPath =
                            SendHelperSendMsgEntity.SendHelperSendVideoEntity(res.result?.content).localPath
                        val message = WeWorkMessageUtil.buildVideoMessage(
                            Global.loadPackageParam.classLoader, res.result?.videoLocalPath ?: ""
                        )
                        MyLog.debug(
                            TAG,
                            "msgType" + msgType + " [onHandle]" + message + "发送 video 消息..." + data
                        )

                        sendPrivateMessage(
                            res.reqid, res.msgType, res.result?.remoteId!!, message
                        )
                    }
                }
            }

            "voice" -> {
                if ((res.result?.remoteId ?: return) > 0) {
                    if (res.result?.content?.isNotEmpty() == true) {

                         val voiceFilePath = ResourceController.saveIntNetFile(
                             res.result?.content,
                            ResourceController.MResource.VOICE
                        )

//                        res.result?.voiceLocalPath =
//                            SendHelperSendMsgEntity.SendHelperSendVideoEntity(res.result?.content).localPath
                        val message = WeWorkMessageUtil.buildVoiceMessage(
                            Global.loadPackageParam.classLoader, voiceFilePath
                        , 1)
                        MyLog.debug(
                            TAG,
                            "msgType" + msgType + " [onHandle]" + message + "发送 voice 消息..." + data
                        )

                        sendPrivateMessage(
                            res.reqid, res.msgType, res.result?.remoteId!!, message
                        )
                    }
                }
            }

            "link" -> {
                if ((res.result?.remoteId ?: return) > 0) {
                    val message = WeWorkMessageUtil.buildLinkMessage(
                        Global.loadPackageParam.classLoader,
                        res.result?.linkUrl ?: "",
                        res.result?.linkTitle ?: "",
                        res.result?.linkDesc ?: "",
                        res.result?.linkImgUrl ?: ""
                    )
                    MyLog.debug(
                        TAG,
                        "msgType" + msgType + " [onHandle]" + message + "发送link消息..." + data
                    )
                    sendPrivateMessage(
                        res.reqid, res.msgType, res.result?.remoteId!!, message
                    )
                }
            }

            "miniProgram" -> {
                if ((res.result?.remoteId ?: return) > 0) {
                    if (!TextUtils.isEmpty(res.result?.miniProgramThumbUrl) && res.result?.miniProgramThumbUrl?.startsWith(
                            "http"
                        ) == true
                    ) {
                        try {
                            res.result?.miniProgramLocalThumbUrl =
                                ResourceController.saveIntNetFile(
                                    res.result?.miniProgramThumbUrl,
                                    ResourceController.MResource.IMAGE
                                )
                            MyLog.debug(
                                SendHelperSendMsgEntity.TAG,
                                "[SendHelperSendLittleProgramEntity]" + " fileFullPathName:" + res.result?.miniProgramLocalThumbUrl + " 下载 小程序 图片 imgHttpPath:" + res.result?.miniProgramThumbUrl,
                                true
                            )
                        } catch (e: MessageException) {
                            res.result?.miniProgramLocalThumbUrl = ""
                            e.printStackTrace()
                        }
                    } else {
                        res.result?.miniProgramLocalThumbUrl = ""
                    }
                    val message = WeWorkMessageUtil.buildMiniProgremMessage(
                        Global.loadPackageParam.classLoader,
                        res.result?.miniProgramAppId ?: "",
                        res.result?.miniProgramUsername ?: "",
                        res.result?.linkUrl ?: "",
                        res.result?.miniProgramLogoUrl ?: "",
                        res.result?.miniProgramTitle ?: "",
                        res.result?.miniProgramLocalThumbUrl,
                        res.result?.miniProgramAppName ?: "",
                        ""
                    )
                    MyLog.debug(
                        TAG,
                        "msgType" + msgType + " [onHandle]" + message + "发送link消息..." + data
                    )
                    sendPrivateMessage(
                        res.reqid, res.msgType, res.result?.remoteId!!, message
                    )
                }
            }
        }
    }

    fun sendPrivateMessage(
        reqid: String?, msgType: String?, userRemoteId: Long, message: Any?
    ) {
        MyLog.debug(
            TAG,
            "sendPrivateMessage" + " [onHandle]" + message + "发送txt消息..userRemoteId." + userRemoteId
        )
        Global.postRunnable2UI {
            WeWorkMessageUtil.sendMessagePrivate(Global.loadPackageParam.classLoader,
                0,
                userRemoteId,
                message,
                object : SendMessageCallBack() {
                    override fun onResult(code: Int, conversation: Any, message: Any) {
                        if (code == 0) {
                            WssProtocalManager.sendMsgResultEntity(reqid, msgType, code, "发送成功")
                            MyLog.debug(
                                TAG,
                                "[sendMessageCallback]: onResult{code=$code, conversation=$conversation message=$message}"
                            )
                        } else {
                            WssProtocalManager.sendMsgResultEntity(reqid, msgType, code, "发送失败")
                            MyLog.debug(
                                TAG,
                                "[sendMessageCallback]: onResult{code=$code, conversation=$conversation message=$message}"
                            )
                        }
                    }

                    override fun onProgress(message: Any, j: Long, j2: Long) {
                    }
                })
        }
    }

}
