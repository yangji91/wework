//package com.tx.hook.msghandle
//
//import com.tx.common.Global
//import com.tx.common.MConfiger
//import com.tx.controller.LoginController
//import com.tx.controller.OssController
//import com.tx.controller.message.MessageController.SendMessageCallBack
//import com.tx.controller.message.VoiceMessageService
//import com.tx.controller.message.WeWorkMessageUtil
//import com.tx.entity.MsgEntity
//import com.tx.hook.msghandle.base.BaseHandleMsg
//import com.tx.netty.ProtocalManager
//import com.tx.util.MyLog
//import com.tx.util.ProxyUtil.ProxyStringResultCallBack
//import com.tx.util.StrUtils
//import com.robot.robothook.RobotMethodParam
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.MainScope
//import kotlinx.coroutines.cancel
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import java.io.File
//
///**
// * 处理语音消息
// */
//class HandleMsgVoice : BaseHandleMsg {
//    private val TAG = javaClass.simpleName
//
//    //    private Map<MsgEntity, Integer> mMap = new HashMap<>();
//    override fun onHandleMsg(loadPackageParam: LoadPackageParam, msgEntity: MsgEntity) {
//        MyLog.debug(TAG, "[onHandleMsg]" + " voiceMsgEntity -> " + msgEntity.fileMsgEntity)
//        loadVoiceFile(loadPackageParam, msgEntity)
//    }
//
//    private fun loadVoiceFile(loadPackageParam: LoadPackageParam, msgEntity: MsgEntity) {
//        //他人语音amr
//        if (msgEntity.contentType == MsgHandleEnum.VOICE.type) {
//            Global.postRunnable2UIDelay({
//                val voiceMessageService = VoiceMessageService()
//                val filePath = VoiceMessageService.getVoiceFilePath(
//                    msgEntity.extras.receiver.toString(),
//                    StrUtils.byteToUTFStr(msgEntity.fileMsgEntity.fileId)
//                )
//                val fileId = StrUtils.byteToUTFStr(msgEntity.fileMsgEntity.fileId)
//                val aesKey = StrUtils.byteToUTFStr(msgEntity.fileMsgEntity.aesKey)
//                val md5 = StrUtils.byteToUTFStr(msgEntity.fileMsgEntity.md5)
//                val callBack: ProxyStringResultCallBack = object : ProxyStringResultCallBack() {
//                    override fun onResult(i: Int, str: String) {
//                        if (i == 0) {
//                            voiceMessageService.autoTranslateVoiceText(
//                                loadPackageParam.classLoader, fileId, aesKey
//                            ) { code, transText ->
//                                MyLog.debug(TAG, "语音翻译结束 code $code  message:$transText")
//                                val amrFile = File(str)
//                                if (code == 0) {
//                                    msgEntity.sttStatus = 2L
//                                    msgEntity.sttText = transText
//                                } else {
//                                    msgEntity.sttStatus = 3L
//                                    msgEntity.sttText = "$transText[$code]"
//                                }
//                                if (amrFile.exists()) {
//                                    sendVoice(str, msgEntity)
//                                }
//                            }
//                        } else {
//                            sendVoice(str, msgEntity)
//                        }
//                    }
//                }
//                MainScope().launch {
//                    withContext(Dispatchers.IO) {
//                        var count = 0
//                        while (true) {
//                            if (count >= 10) {
////                                val newFilePath = VoiceMessageService.getNewVoiceFilePath(
////                                    msgEntity.extras.receiver.toString(),
////                                    StrUtils.byteToUTFStr(msgEntity.fileMsgEntity.fileId)
////                                )
////                                voiceMessageService.downVoiceFile(
////                                    fileId,
////                                    aesKey,
////                                    md5,
////                                    msgEntity.fileMsgEntity.size,
////                                    newFilePath,
////                                    callBack
////                                )
//                                withContext(Dispatchers.Main) {
//                                    callBack.onResult(1, filePath)
//                                }
//                                return@withContext
//                            }
//                            withContext(Dispatchers.IO) {
//                                if (File(filePath).canRead()) {
//                                    MyLog.debug(TAG, "语音文件已经存在 无需下载 $filePath", true)
//                                    withContext(Dispatchers.Main) {
//                                        callBack.onResult(0, filePath)
//                                    }
//                                    cancel()
//                                } else {
//                                    MyLog.debug(
//                                        TAG,
//                                        "语音文件需要下载 filePath " + filePath + " fileId " + fileId + " aesKey " + aesKey + " md5 " + md5 + " size " + msgEntity.fileMsgEntity.size,
//                                        true
//                                    )
//                                    count++
//                                    delay(600)
//                                }
//                            }
//                        }
//                    }
//                }
//            }, 4000)
//        } else {
//            //自己语音silk
//            Global.postRunnable2UIDelay({
//                val filePath = VoiceMessageService.getVoiceSelfFilePath(msgEntity.sender.toString())
//                if (File(filePath).canRead()) {
//                    MyLog.debug(TAG, "语音文件存在 $filePath", true)
//                    sendSelfVoice(filePath, msgEntity)
//                } else {
//                    MyLog.debug(TAG, "语音文件不存在$filePath", true)
//                }
//            }, 4000)
//        }
//    }
//
//    private fun getObjectName(fileName: String): String {
//        val userId = LoginController.getInstance().loginUserId
//        val prefix = "voice"
//        val objectName = String.format("wecom/robot/%d/%s/%s", userId, prefix, fileName) //文件;
//        return if (objectName.endsWith(".amr")) {
//            objectName
//        } else "$objectName.amr"
//    }
//
//    private fun getSelfVoiceName(fileName: String): String {
//        val userId = LoginController.getInstance().loginUserId
//        val prefix = "voice"
//        val objectName = String.format("wecom/robot/%d/%s/%s", userId, prefix, fileName) //文件;
//        return if (objectName.endsWith(".silk")) {
//            objectName
//        } else "$objectName.silk"
//    }
//
//    /**
//     * @param voicePath 语音地址 语音没有缓存
//     * @param msgEntity
//     */
//    private fun sendVoice(voicePath: String, msgEntity: MsgEntity) {
////        mMap.remove(msgEntity);
//        val objectName = getObjectName(File(voicePath).name)
//        val fullImgUrl = MConfiger.getFullImgUrl(objectName)
//        msgEntity.content = fullImgUrl
//        ProtocalManager.getInstance().sendMsgEntity(msgEntity, "sendVoice")
//        //上传文件到oss
//        OssController.getInstance().uploadFile(voicePath, objectName, msgEntity)
//        MyLog.debug(
//            TAG,
//            "[sendVoice]" + "语音翻译 " + msgEntity.sttText + "语音时长 " + (if (msgEntity.fileMsgEntity != null) msgEntity.fileMsgEntity.voiceTime else 0) + " oss 音频文件:" + voicePath + " url:" + fullImgUrl + " type:" + msgEntity.contentType,
//            true
//        )
//    }
//
//    /**
//     * @param voicePath 语音地址 语音没有缓存
//     * @param msgEntity
//     */
//    private fun sendSelfVoice(voicePath: String, msgEntity: MsgEntity) {
//        val objectName = getSelfVoiceName(File(voicePath).name)
//        val fullImgUrl = MConfiger.getFullImgUrl(objectName)
//        msgEntity.content = fullImgUrl
//        ProtocalManager.getInstance().sendMsgEntity(msgEntity, "sendSelfVoice")
//        //上传文件到oss
//        OssController.getInstance().uploadFile(voicePath, objectName, msgEntity)
//        MyLog.debug(
//            TAG,
//            "[sendVoice]" + "silk " + msgEntity.sttText + " oss 音频文件:" + voicePath + " url:" + fullImgUrl + " type:" + msgEntity.contentType,
//            true
//        )
//        if (msgEntity.conversationId != 0L && msgEntity.conversationId == LoginController.getInstance().loginUserEntity.remoteId) {
//            sendVoiceCallbackMessage(fullImgUrl, msgEntity.conversationId)
//        }
//    }
//
//    private fun sendVoiceCallbackMessage(content: String, converId: Long) {
//        val message =
//            WeWorkMessageUtil.buildTexMessage(Global.loadPackageParam.classLoader, content)
//        WeWorkMessageUtil.sendMessagePrivate(Global.loadPackageParam.classLoader,
//            converId,
//            converId,
//            message,
//            object : SendMessageCallBack() {
//                override fun onResult(code: Int, conversation: Any, message: Any) {
//                    if (code == 0) {
//                        MyLog.debug(TAG, "[onCall] sendVoiceCallbackMessage" + "发送成功...")
//                    } else {
//                        MyLog.debug(TAG, "[onCall] sendVoiceCallbackMessage" + " 发送失败...")
//                    }
//                }
//
//                override fun onProgress(message: Any, j: Long, j2: Long) {}
//            })
//    }
//}