package com.robot.hook.msghandle

import android.text.TextUtils
import com.robot.common.Global
import com.robot.controller.ConvController
import com.robot.controller.LoginController
import com.robot.entity.MsgEntity
import com.robot.entity.UserEntity
import com.robot.hook.KeyConst
import com.robot.hook.msghandle.base.BaseHandleMsg
import com.robot.netty.ProtocalManager
import com.robot.util.MyLog
import com.robot.util.ProxyUtil.ProxyUserResultCallBack
import com.robot.util.StrUtils
import com.robot.robothook.LoadPackageParam
import com.robot.robothook.RobotHelpers

/***
 * @author 
 * @date 2022/1/17
 * @description
 */
class GroupGreyMsg : BaseHandleMsg {
    override fun onHandleMsg(loadPackageParam: LoadPackageParam?, msgEntity: MsgEntity?) {
        if (loadPackageParam == null || msgEntity == null) return
        val appInfo = msgEntity.appinfo
        val list = ArrayList<Long>()
        list.add(msgEntity.sender)
        if (msgEntity.contentType == 1022) {
            val tipslist = msgEntity.roomTipsList.tipslist
            if (tipslist != null && tipslist.size > 0) {
                if (tipslist[0].vid != null && tipslist[0].vid.size > 0) {
                    list.clear()
                    list.add(tipslist[0].vid[0])
                }
                msgEntity.content = msgEntity.roomTipsList.tipslist[0].tips
            }
        } else if (msgEntity.contentType == 1002 || msgEntity.contentType == 1003) {
            val memeberIds = getMemeberIds(msgEntity.content)
            list.addAll(memeberIds)
        }
        ConvController.getUserObjectFromDBByIds(list) { code, userList ->
            if (code == 0 && list.size > 0) {
                var senderUserName = if (LoginController.getInstance().loginUserId == msgEntity.sender) "你" else getUserName(userList, msgEntity.sender)
                if (msgEntity.contentType == 1001) {
                    msgEntity.content = senderUserName + " 将群名称修改为: " + msgEntity.content
                    ProtocalManager.getInstance().sendMsgEntity(msgEntity, "sendGroupGrey1001")
                } else if (msgEntity.contentType == 1022) {
                    senderUserName = if (LoginController.getInstance().loginUserId == list[list.size - 1]) "你" else getUserName(userList, list[list.size - 1])
                    msgEntity.content = senderUserName + msgEntity.content
                    ProtocalManager.getInstance().sendMsgEntity(msgEntity, "sendGroupGrey1022")
                } else if (msgEntity.contentType == 1002) {
                    msgEntity.memberIds = getMemeberIds(msgEntity.content)
                    val memberNames = getUserNames(userList, msgEntity.memberIds)
                    if (memberNames.isEmpty()) {
                        updateUserFromIntnet(senderUserName, msgEntity, appInfo)
                    } else {
                        commitAddMember(senderUserName, memberNames, msgEntity.memberIds, appInfo, msgEntity)
                    }
                } else if (msgEntity.contentType == 1003) {
                    msgEntity.memberIds = getMemeberIds(msgEntity.content)
                    msgEntity.memberNames = getUserNames(userList, msgEntity.memberIds)
                    msgEntity.inviterName = senderUserName
                    msgEntity.content = senderUserName + "将" + msgEntity.memberNames.joinToString("、") + " 移出了群聊"
                    ProtocalManager.getInstance().sendMsgEntity(msgEntity, "sendGroupGrey1003")
                }
            }
        }
    }

    private fun getMemeberIds(content: String): ArrayList<Long> {
        val list = ArrayList<Long>()
        if (!TextUtils.isEmpty(content)) {
            if (content.contains(";")) {
                val split = content.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (s in split) {
                    list.add(s.toLong())
                }
            } else {
                try {
                    list.add(content.toLong())
                } catch (e: Throwable) {
                    MyLog.debug("getMemeberIds", e.toString(), true)
                }
            }
        }
        return list
    }

    private fun updateUserFromIntnet(
            senderUserName: String,
            msgEntity: MsgEntity,
            appInfo: String
    ) {
        Global.postRunnable2UI {
            val memberIds = getMemeberIds(msgEntity.content)
            ConvController.getInstance().refreshUserObjectInMainThread(
                    Global.loadPackageParam.classLoader,
                    memberIds.toLongArray(),
                    msgEntity.conversationId,
                    object : ProxyUserResultCallBack() {
                        override fun onResult(code: Int, userObj: Array<Any>?) {
                            if (code == 0) {
                                MyLog.debug("GroupGreyMsg", " 更新用户信息成功  " + StrUtils.objectToJson(userObj))
                                if (!userObj.isNullOrEmpty()) {
                                    val memberNames = ArrayList<String>()
                                    userObj.forEach {
                                        val info = RobotHelpers.callMethod(it, KeyConst.M_User_getInfo)
                                        val memberName = RobotHelpers.getObjectField(info, KeyConst.F_User_getInfo_name) as String
                                        memberNames.add(memberName)
                                    }
                                    commitAddMember(senderUserName, memberNames, memberIds, appInfo, msgEntity)
                                }
                            } else {
                                commitAddMember(senderUserName, memberIds.map { it.toString() }, memberIds, appInfo, msgEntity)
                                //更新失败
                                MyLog.debug("GroupGreyMsg", "网络更新  用户信息失败了 $memberIds", true)
                            }
                        }
                    })
        }
    }

    private fun commitAddMember(
            senderUserName: String,
            memberNames: List<String>,
            memberIds: List<Long>,
            appInfo: String,
            msgEntity: MsgEntity
    ) {
        if (appInfo.startsWith("AddOpenIMChatRoomByQRCode")) {
            msgEntity.content = memberNames.joinToString("、") + "通过扫描 " + senderUserName + " 分享的二维码加入群聊"
        } else if (appInfo.startsWith("AddAssociateChatRoomMember")) {
            msgEntity.content = senderUserName + " 邀请 " + memberNames.joinToString("、") + " 加入群聊"
        } else {
            msgEntity.content = senderUserName + " 邀请 " + memberNames.joinToString("、") + " 加入了群聊"
        }
        msgEntity.inviterName = senderUserName
        msgEntity.memberIds = memberIds
        msgEntity.memberNames = memberNames
        ProtocalManager.getInstance().sendMsgEntity(msgEntity, "commitAddMember")
    }

    /**
     * 获取用户名称
     * @param list
     * @param sender
     * @return
     */
    private fun getUserNames(list: List<UserEntity>, ids: List<Long>): ArrayList<String> {
        val names = ArrayList<String>()
        ids.forEach {
            val userName = getUserName(list, it)
            names.add(userName)
        }
        return names
    }

    /**
     * 获取用户名称
     * @param list
     * @param sender
     * @return
     */
    private fun getUserName(list: List<UserEntity>, sender: Long): String {
        for (item in list) {
            if (item.remoteId == sender) {
                return item.name
            }
        }
        return ""
    }
}