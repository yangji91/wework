package com.robot.controller.message

import com.robot.common.Global
import com.robot.controller.message.VoiceMessageService.IPickMessageCallback
import com.robot.hook.KeyConst
import com.robot.util.MyLog
import com.robot.util.ProxyUtil
import com.robot.util.StrUtils
import com.robot.robothook.RobotHelpers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object VoiceMessageUtils {

    @JvmStatic
    fun autoTranslateSelfVoiceText(
        classLoader: ClassLoader?,
        obj_Message: Any?,
        iPickMessageCallback: IPickMessageCallback?
    ) {
        Global.postRunnable2UI {
            val clazzConvService =
                RobotHelpers.findClassIfExists(KeyConst.C_ConversationService, classLoader)
            val convService =
                RobotHelpers.callStaticMethod(
                    clazzConvService,
                    KeyConst.M_ConversationService_getService
                )
            // 第二位是否优先本地转化
            RobotHelpers.callMethod(convService,
                KeyConst.M_MSG_TranslateVoiceText,
                obj_Message,
                true,
                ProxyUtil.GetProxyInstance(
                    KeyConst.I_IPickMessageCallback
                ) { o, method, objects ->
//                    MyLog.debug(
//                        VoiceMessageService.TAG,
//                        "[onHookInfo]" + " method   " + method.name
//                    )
//                    if (method.name == KeyConst.M_IApplyVoiceResultCallback_onResult) {
//                        if (iPickMessageCallback != null) {
//                            MyLog.debug(
//                                VoiceMessageService.TAG,
//                                "[onHookInfo]" + " 语音转换   " + StrUtils.objectToJson(
//                                    objects[1]
//                                )
//                            )
//                            val code = objects[0] as Int
//                            var message: String? = null
//                            if (code == 0) {
//                                val msg =
//                                    RobotHelpers.callMethod(objects[1], KeyConst.M_Message_requestInfo)
//                                val extras = RobotHelpers.getObjectField(
//                                    msg,
//                                    KeyConst.F_MSG_TranslateVoice_requestInfo_extras
//                                )
//                                val voiceTextInfo = RobotHelpers.getObjectField(
//                                    extras,
//                                    KeyConst.F_MSG_TranslateVoice_requestInfo_extras_voiceTextInfo
//                                )
//                                val transText = RobotHelpers.getObjectField(
//                                    voiceTextInfo,
//                                    KeyConst.F_MSG_TranslateVoice_requestInfo_extras_voiceTextInfo_transText
//                                ) as ByteArray
//                                message = StrUtils.byteToUTFStr(transText)
//                                MyLog.debug(
//                                    VoiceMessageService.TAG,
//                                    "[onHookInfo] 语音转换成功  $message", true
//                                )
//                            } else {
//                                MyLog.debug(
//                                    VoiceMessageService.TAG,
//                                    "[onHookInfo]" + " 语音转换失败  ",
//                                    true
//                                )
//                                message = "转化失败"
//                            }
//                            iPickMessageCallback.onResult(code, message)
//                        }
//                    }
//                    null
                })

            MainScope().launch(Dispatchers.Main) {
                var code: Int = 1
                var message: String = ""
                for (i in 1..5) {
                    delay(800L)
                    MyLog.debug(
                        VoiceMessageService.TAG,
                        "[onHookInfo]" + " method TranslateVoiceText 调用  " + i
                    )
                    val msg = RobotHelpers.callMethod(obj_Message, KeyConst.M_Message_requestInfo)
                    if (msg != null) {
                        val extras =
                            RobotHelpers.getObjectField(
                                msg,
                                KeyConst.F_MSG_TranslateVoice_requestInfo_extras
                            )
                        if (extras != null) {
                            val voiceTextInfo = RobotHelpers.getObjectField(
                                extras,
                                KeyConst.F_MSG_TranslateVoice_requestInfo_extras_voiceTextInfo
                            )
                            if (voiceTextInfo != null) {
                                val transText = RobotHelpers.getObjectField(
                                    voiceTextInfo,
                                    KeyConst.F_MSG_TranslateVoice_requestInfo_extras_voiceTextInfo_transText
                                ) as ByteArray?
                                if (transText != null) {
                                    message = StrUtils.byteToUTFStr(transText)
                                    if (message.isNotEmpty()) {
                                        MyLog.debug(
                                            VoiceMessageService.TAG,
                                            "[onHookInfo]" + " 语音转换成功  " + message,
                                            true
                                        )
                                        code = 0
                                        break
                                    }
                                }
                            }
                        }
                    }
                }
                iPickMessageCallback?.onResult(code, message)
                MyLog.debug(
                    VoiceMessageService.TAG,
                    "[onHookInfo]" + " method TranslateVoiceText 调用结束  "
                )
            }
        }
    }
}