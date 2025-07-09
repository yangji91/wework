package com.robot.netty.handle.imple

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.robot.netty.entity.rsp.PRspGroupManagerEntity
import com.robot.netty.handle.BaseHandle
import com.robot.netty.handle.imple.contact.GetContactListAction
import com.robot.netty.handle.imple.contact.SearchAddOuterFriendAction
import com.robot.netty.handle.imple.entity.ContactEntity
import com.robot.util.MyLog

/**
 * 联系人管理
 */
class HandleContactManager : BaseHandle() {
    override fun onHandle(data: String) {
        super.onHandle(data)
        MyLog.debug(TAG, "[onHandle]" + "外部联系人管理处理..." + data)
        val res: ContactEntity = Gson().fromJson<ContactEntity>(data, ContactEntity::class.java)
        val msgType: String = res.msgType ?: ""

        when (msgType) {
            "searchAndAddContact" -> {
                if (res.result?.phonenum?.isNotEmpty() == true) {
                    SearchAddOuterFriendAction().handlerAction(res.reqid, res.result)
                }
            }

            "contactList" -> {
                GetContactListAction().handleAction("")
            }
        }
    }

    override fun onHandle(data: JsonObject) {
    }
}
