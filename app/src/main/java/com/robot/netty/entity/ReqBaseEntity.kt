package com.robot.netty.entity

import com.google.gson.Gson
import java.io.Serializable

class ReqBaseEntity<T> : Serializable {
    var reqid: String = ""
    var actionType: String = ""
    var taskType: String = ""
    var msgType: String = ""
    var data: T? = null

    fun build(
        reqid: String,
        actionType: String = "",
        taskType: String = "",
        msgType: String = "",
        data: T
    ): String {
        this.reqid = reqid
        this.actionType = actionType
        this.taskType = taskType
        this.msgType = msgType
        this.data = data
        val gson = Gson()
        val str = gson.toJson(this)
        return str
    }
}
