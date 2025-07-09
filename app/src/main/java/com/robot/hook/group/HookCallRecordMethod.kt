package com.robot.hook.group

import android.media.MediaRecorder
import com.robot.common.StatsHelper
import com.robot.hook.KeyConst
import com.robot.hook.base.HookBaseMethod
import com.robot.hook.base.IHookCallBack
import com.robot.util.FileUtil
import com.robot.util.MyLog
import com.robot.util.StrUtils
import com.robot.robothook.LoadPackageParam
import com.robot.robothook.RobotHelpers
import com.robot.robothook.RobotMethodHook
import com.robot.robothook.RobotMethodParam
import java.io.IOException
import java.util.Random


/**
 * 监听语音/视频通话开启与结束
 */
class HookCallRecordMethod : HookBaseMethod<Any?>() {

    companion object {
        @JvmStatic
        var mIsRecordEnd = false

        @JvmStatic
        var mRecordTime = 0L
        private var mediaRecorder: MediaRecorder? = null

        @JvmStatic
        var outputFile: String = ""

        @JvmStatic
        fun startRecording() {
            mIsRecordEnd = false
            mRecordTime = System.currentTimeMillis()
            // 设置输出文件的路径
//        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).absolutePath
            outputFile = FileUtil.getRecordFilePath(
                System.currentTimeMillis().toString() + "_" + Random().nextInt(999)
            )
            // 创建MediaRecorder实例并进行配置
            mediaRecorder = MediaRecorder()
            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC) // 设置音频源为麦克风
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4) // 设置输出格式为MP3
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC) // 设置音频编码器为AAC
            mediaRecorder?.setOutputFile(outputFile) // 设置输出文件的路径
            try {
                mediaRecorder?.prepare() // 准备录制
                mediaRecorder?.start() // 开始录制
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        @JvmStatic
        fun stopRecording() {
            if (mediaRecorder != null) {
                mediaRecorder?.stop() // 停止录制
                mediaRecorder?.release() // 释放资源
                mediaRecorder = null
                mIsRecordEnd = true
                mRecordTime = Math.round((System.currentTimeMillis() - mRecordTime) / 1000.0)
            }
        }
    }

    override fun onHookInfo(
        clazz: Class<*>?, loadPackageParam: LoadPackageParam?, callBack: IHookCallBack<Any?>?
    ) {
//        RobotHelpers.hookAllMethods(RobotHelpers.findClassIfExists(
//            "com.android.server.am.ActivityManagerService\$PermissionController",
//            loadPackageParam?.classLoader
//        ), "checkPermission", object : XC_MethodHook() {
//            @Throws(Throwable::class)
//            override fun afterHookedMethod(param: RobotMethodParam) {
//                MyLog.debug(
//                    TAG,
//                    "[onHookInfo]" + "onVoipEvent1CAPTURE_AUDIO_OUTPUT " + "CAPTURE_AUDIO_OUTPUT"
//                )
////                    val packages = XposedHelpers.callMethod(
////                        param.thisObject, "getPackagesForUid",
////                        param.args[2]
////                    ) as Array<String>
////                    if (Arrays.asList(*packages).contains(MConfiger.WX_ENTERPISE_PKGNAME)) {
////                        if ("android.permission.CAPTURE_AUDIO_OUTPUT" == param.args[0]) {
////                            param.result = true
////                        }
////                    }
//            }
//        })
        val voipCallActivity = RobotHelpers.findClassIfExists(
            KeyConst.C_CallEvent, loadPackageParam?.classLoader
        )
        RobotHelpers.hookAllMethods(voipCallActivity,
            KeyConst.M_CallEvent_onVoipEvent,
            object : RobotMethodHook() {
                @Throws(Throwable::class)
                override fun afterHookedMethod(param: RobotMethodParam) {
                    super.afterHookedMethod(param)
                    val params = param.args
                    if (params?.isNotEmpty() == true) {
                        val voipEvent = params[0] as Any
                        val voipEventStr = voipEvent.toString()
                        when (voipEventStr) {
                            KeyConst.F_CallEvent_onVoipEvent_STATE_TALK_READY -> {
//                                if (mIsRecording) {
//                                    return
//                                }
                                MyLog.debug(
                                    TAG,
                                    "[onHookInfo]" + "onVoipEvent1 " + params.size + " voipEvent  " + voipEventStr,
                                    true
                                )
                                StatsHelper.event(
                                    "msgReport",
                                    "reportlog",
                                    "HookCallRecordMethod STATE_TALK_READY",
                                    "time " + StrUtils.getTimeDetailStr()
                                )
                                startRecording()
//                            mRecordResource.startRecord(
//                                object : GetResourcesObserver {
//                                    override fun onResourcesCompleted(
//                                        path: String?, resource: CompletedResource?
//                                    ) {
//                                        MyLog.debug(
//                                            TAG,
//                                            "[onHookInfo]" + "onVoipEvent startRecord onResourcesCompleted" + path + " resource " + resource?.completeFile?.path
//                                        )
//                                    }
//
//                                    override fun onResourcesError(p0: String?, p1: StatusMessage?) {
//                                        MyLog.debug(
//                                            TAG,
//                                            "[onHookInfo]" + "onVoipEvent startRecord onResourcesError" + p0 + " p1 " + StrUtils.objectToJson(
//                                                p1
//                                            )
//                                        )
//                                    }
//
//                                    override fun onProgress(p0: Int, p1: String?) {
//                                    }
//                                }, getRecordFilePath()
//                            )
                            }

                            KeyConst.F_CallEvent_onVoipEvent_STATE_EXIT_ROOM -> {
                                stopRecording()
                                MyLog.debug(
                                    TAG,
                                    "[onHookInfo]" + "onVoipEvent1 " + params.size + " voipEvent  " + voipEventStr,
                                    true
                                )
                                StatsHelper.event(
                                    "msgReport",
                                    "reportlog",
                                    "HookCallRecordMethod STATE_EXIT_ROOM",
                                    "time " + StrUtils.getTimeDetailStr()
                                )
//                            mRecordResource.stopRecord()
                            }
                        }
                    }
                }
            })
    }

    override fun onInvokeMethod(
        classDb: Class<*>?, loadPackageParam: LoadPackageParam?, paramCall: IHookCallBack<Any?>?
    ): Boolean {
        return false
    }
}