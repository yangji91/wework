package com.robot.netty.entity

import java.io.Serializable

class ResBaseEntity<T> : Serializable {
    val result: T? = null
    val reqid: String? = null
    val actionType: String? = null
    val taskType: String? = null
    val msgType: String? = null
}
