package com.robot.common

import com.robot.controller.LoginController
import com.robot.com.BuildConfig
import com.robot.util.MyLog
import java.util.Arrays

/**
 * 打点中间层
 *
 * 例：
 *
 * StatsHelper.event(
 *  module = "1",
 *  op = "1",
 *  isRealTime = true,
 *  params = *mapOf("1", "2")
 *
 */
object StatsHelper {

    val ETC_S = arrayOf("s0", "s1", "s2", "s3", "s4")
    var mIsInit = false
    const val LOG_DB_NAME = "vox_logs" //LOG数据库名称

    const val LOG_DB_TABLE_NAME = "normal_wecomrobot" //LOG日志表名称

    const val YR_UP_LOAD_LOG_URL = ""

    /**
     * kibana打点
     */

    @JvmStatic
    fun event(
        module: String, option: String, isRealTime: Boolean = true, map: MutableMap<String, Any>?
    ) {
        runCatching {
            if (!mIsInit) {
                mIsInit = true
//                LogHandlerManager.init(
//                    LOG_DB_NAME, LOG_DB_TABLE_NAME, YR_UP_LOAD_LOG_URL
//                )
            }
            val params = map ?: mutableMapOf()
//            addCommonParams(params)
//            if (isRealTime) {
//                LogHandlerManager.onEventRealTime(module, option, params)
//            } else {
//                LogHandlerManager.onEvent(module, option, params)
//            }
            MyLog.debug(
                "StatsHelper",
                "[event] module " + module + " option " + option + " params " + map.toString() + " time " + Thread.currentThread().name
            )
        }.onFailure {
            it.printStackTrace()
            MyLog.debug(
                "StatsHelper", "error $it", true
            )
        }
    }

    /**
     * 简化打点
     */
    @JvmStatic
    fun event(
        module: String, option: String, vararg params: Any?
    ) {
        MyLog.debug(
            "StatsHelper", "params ${Arrays.toString(params)}", true
        )
//        kotlin.runCatching {
////            val map = hashMapOf<String, Any>()
////            params.forEachIndexed { index, param ->
////                if (param != null) {
////                    map[ETC_S[index]] = param
////                }
////            }
////            event(module, option, true, map)
//        }.onFailure {
//            it.printStackTrace()
//            MyLog.debug(
//                "StatsHelper", "error $it", true
//            )
//        }
    }

    /**
     * 大数据平台 打点的 常规参数
     *
     * @param params
     */
    private fun addCommonParams(params: MutableMap<String, Any>?) {
        var params: MutableMap<String, Any>? = params
        if (params == null) {
            params = HashMap()
        }
        try {
            params["robot_user_id"] = LoginController.getInstance().loginUserId.toString()
            params["robot_phone_num"] = LoginController.getInstance().loginMobile ?: ""
            params["robot_phoneLoc_enum"] = MConfiger.phoneLocEnum ?: ""
            params["robot_device_sn"] = Global.getDeviceSn() ?: ""
            params["robot_app_version"] = "${BuildConfig.VERSION_NAME}.${BuildConfig.VERSION_CODE}"
            params["robot_share_wecom_version"] = Global.getWecomVersion()
//            params["y_wecom_version"] =
//                "${Utils.getVersionName(Global.getContext())}.${Utils.getVersionCode(Global.getContext())}"
            params["y_env"] = BuildConfig.build_env
            params["robot_unified_id"] = Global.getDeviceSn()
        } catch (var8: Exception) {
            var8.printStackTrace()
        }
    }
}