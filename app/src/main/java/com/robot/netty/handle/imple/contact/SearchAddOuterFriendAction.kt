package com.robot.netty.handle.imple.contact

import com.google.gson.Gson
import com.robot.common.Global
import com.robot.common.MConfiger
import com.robot.entity.SearchUserEntity
import com.robot.hook.KeyConst
import com.robot.netty.handle.imple.entity.ContactEntity
import com.robot.netty.handle.imple.group.BaseHandleGroup
import com.robot.nettywss.WssProtocalManager
import com.robot.robothook.RobotHelpers
import com.robot.util.MyLog
import com.robot.util.ProxyUtil
import com.robot.util.ProxyUtil.ProxyStringResultCallBack
import com.robot.util.StrUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

/**
 * 搜索添加联系人
 */
class SearchAddOuterFriendAction : BaseHandleGroup() {

    public fun handlerAction(reqid: String?, action: ContactEntity.Result?) {
        action?.let { searchUser(reqid, action) }
    }

    fun searchUser(reqid: String?, action: ContactEntity.Result) {
        try {
            MyLog.debug(TAG, "SearchAddOuterFriendAction [onInvokeMethod]" + " start... call:")
            if (Global.getWecomVersion().equals(MConfiger.WEWORK_VERSION_19717)) {
                val clazzArray = arrayOf(
                    String::class.java,
                    Boolean::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType,
                    RobotHelpers.findClassIfExists(
                        KeyConst.I_IGetUserByIdCallback2, Global.loadPackageParam.classLoader
                    )
                )
                val proxyInstance =
                    ProxyUtil.GetProxyInstance(KeyConst.I_IGetUserByIdCallback2) { o, method, objects ->
                        MyLog.debug(TAG, "[onHookInfo]" + " method   " + method.name)
                        if ("onResult" == method.name && objects.size == 3) {
                            val code = objects[0] as Int
                            val str = objects[1] as String?
                            val users = objects[2] as Array<Any>?
                            MyLog.debug(
                                TAG,
                                "SearchAddOuterFriendAction [onInvokeMethod] code $code str $str ${
                                    StrUtils.objectToJson(users)
                                }",
                                false
                            )
                            MyLog.debug(
                                TAG,
                                "SearchAddOuterFriendAction [onInvokeMethod] code $code str $str",
                                true
                            )
//                        val message = WeWorkMessageUtil.buildTexMessage(
//                            Global.loadPackageParam.classLoader, StrUtils.objectToJson(users)
//                        )
//                        HandleMediaMsg().sendPrivateMessage(
//                            7881301092905731, message
//                        )
                            if (code == 0) {
                                users?.forEach {
                                    addUser(
                                        reqid ?: "",
                                        action.phonenum ?: "",
                                        action.verify ?: "",
                                        it
                                    )
                                }
                            } else {
                                WssProtocalManager.sendAddContactResultEntity(
                                    reqid, action.phonenum, "", code, "执行失败" + str
                                )
                            }
                        }
                        null
                    }
                val objArray = arrayOf(action.phonenum, true, 3, proxyInstance)
                Global.postRunnable2UI {
                    RobotHelpers.callStaticMethod(
                        RobotHelpers.findClass(
                            KeyConst.C_ContactManager, Global.loadPackageParam.classLoader
                        ), KeyConst.M_ContactManager_SearchContact, clazzArray, *objArray
                    )
                    MyLog.debug(
                        TAG,
                        "SearchAddOuterFriendAction [onInvokeMethod]" + " end... call:"
                    )
                }
            } else if (Global.getWecomVersion().equals(MConfiger.WEWORK_VERSION_31145)) {
                val clazzArray = arrayOf(
                    String::class.java,
                    Boolean::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType,
                    Boolean::class.javaPrimitiveType,
                    RobotHelpers.findClassIfExists(
                        KeyConst.I_IGetUserByIdCallback2, Global.loadPackageParam.classLoader
                    )
                )
                val proxyInstance =
                    ProxyUtil.GetProxyInstance(KeyConst.I_IGetUserByIdCallback2) { o, method, objects ->
                        MyLog.debug(TAG, "[onHookInfo]" + " method   " + method.name)
                        if ("onResult" == method.name && objects.size == 3) {
                            val code = objects[0] as Int
                            val str = objects[1] as String?
                            val users = objects[2] as Array<Any>?
                            MyLog.debug(
                                TAG,
                                "SearchAddOuterFriendAction [onInvokeMethod] code $code str $str ${
                                    StrUtils.objectToJson(users)
                                }",
                                false
                            )
                            MyLog.debug(
                                TAG,
                                "SearchAddOuterFriendAction [onInvokeMethod] code $code str $str",
                                true
                            )
//                        val message = WeWorkMessageUtil.buildTexMessage(
//                            Global.loadPackageParam.classLoader, StrUtils.objectToJson(users)
//                        )
//                        HandleMediaMsg().sendPrivateMessage(
//                            7881301092905731, message
//                        )
                            if (code == 0) {
                                users?.forEach {
                                    addUser(
                                        reqid ?: "",
                                        action.phonenum ?: "",
                                        action.verify ?: "",
                                        it
                                    )
                                }
                            } else {
                                WssProtocalManager.sendAddContactResultEntity(
                                    reqid, action.phonenum, "", code, "执行失败" + str
                                )
                            }
                        }
                        null
                    }
                val objArray = arrayOf(action.phonenum, true, 3, true, proxyInstance)
                Global.postRunnable2UI {
                    RobotHelpers.callStaticMethod(
                        RobotHelpers.findClass(
                            KeyConst.C_ContactManager, Global.loadPackageParam.classLoader
                        ), KeyConst.M_ContactManager_SearchContact, clazzArray, *objArray
                    )
                    MyLog.debug(
                        TAG,
                        "SearchAddOuterFriendAction [onInvokeMethod]" + " end... call:"
                    )
                }
            }
        } catch (e: Throwable) {
            MyLog.error(TAG, e)
        }
    }


    fun addUser(reqid: String, phonenum: String, content: String, user: Any) {
        val clazzContactService = RobotHelpers.findClassIfExists(
            KeyConst.C_ContactService, Global.loadPackageParam.classLoader
        )
        val objContactService =
            RobotHelpers.callStaticMethod(clazzContactService, KeyConst.M_ContactService_getService)
        MainScope().launch {
            withContext(Dispatchers.IO) {
                delay(Random.nextLong(500, 600))
                val proxyInstance = ProxyUtil.GetProxyInstance(
                    KeyConst.C_ICommonStringCallback,
                    object : ProxyStringResultCallBack() {
                        override fun onResult(i: Int, str: String) {
                            MyLog.debug(TAG, "[onResult] addUser code =$i result =$str")
                            var searchUserData: SearchUserEntity? = null
                            runCatching {
                                searchUserData = Gson().fromJson<SearchUserEntity>(
                                    StrUtils.objectToJson(user), SearchUserEntity::class.java
                                )
                                MyLog.debug(
                                    TAG, "[onResult] addUser toJson(searchUserData) =${
                                        Gson().toJson(
                                            searchUserData
                                        )
                                    }"
                                )
                            }.onFailure {
                                it.printStackTrace()
                            }
                            if (i == 0) {
                                MyLog.debug(TAG, "[onResult]addUser 执行成功")
                                WssProtocalManager.run {
                                    sendAddContactResultEntity(
                                        reqid,
                                        phonenum,
                                        searchUserData?.extraWechatRemoteId ?: "",
                                        i,
                                        "执行成功"
                                    )
                                }
                            } else if (i == 202) {
                                WssProtocalManager.sendAddContactResultEntity(
                                    reqid,
                                    phonenum,
                                    searchUserData?.extraWechatRemoteId ?: "",
                                    i,
                                    "执行失败" + str
                                )
                            } else if (i == 208) {   //被禁止搜索
                                MyLog.debug(
                                    TAG, "[onInvokeMethod] addUser" + " 用户禁止添加..."
                                )
                                WssProtocalManager.sendAddContactResultEntity(
                                    reqid,
                                    phonenum,
                                    searchUserData?.extraWechatRemoteId?.toString() ?: "",
                                    i,
                                    "执行失败" + "用户禁止添加" + str
                                )
                            } else if (i == 207) {   //被禁止搜索
                                MyLog.debug(TAG, "[onInvokeMethod]" + " 未实名认证" + str)
                                WssProtocalManager.sendAddContactResultEntity(
                                    reqid,
                                    phonenum,
                                    searchUserData?.extraWechatRemoteId?.toString() ?: "",
                                    i,
                                    "执行失败" + "未实名认证" + str
                                )
                            } else {
                                WssProtocalManager.sendAddContactResultEntity(
                                    reqid,
                                    phonenum,
                                    searchUserData?.extraWechatRemoteId?.toString() ?: "",
                                    i,
                                    "执行失败" + str
                                )
                                MyLog.debug(
                                    TAG,
                                    "[uid = ]" + "添加好友失败 code =" + i + " message = " + str,
                                    true
                                )
                            }
                        }
                    })
                withContext(Dispatchers.Main) {
                    val clazzArray = arrayOf(
                        Int::class.javaPrimitiveType,
                        String::class.javaPrimitiveType,
                        user::class.javaPrimitiveType,
                        Int::class.javaPrimitiveType,
                        Boolean::class.javaPrimitiveType,
                        Boolean::class.javaPrimitiveType,
                        Int::class.javaPrimitiveType,
                        RobotHelpers.findClassIfExists(
                            KeyConst.C_ICommonStringCallback, Global.loadPackageParam.classLoader
                        )
                    )
                    val tempArray = arrayOf(14, content, user, 4, true, true, 0, proxyInstance)
                    RobotHelpers.callMethod(
                        objContactService,
                        KeyConst.M_ContactService_OperateContact,
                        clazzArray,
                        *tempArray
                    )
                    MyLog.debug(TAG, "[onResult]" + "addUser 调用完成")
                }
                MyLog.debug(TAG, "[onResult]" + "addUsers 调用完成")
            }
            cancel()
        }
    }
}

