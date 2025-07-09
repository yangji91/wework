package com.robot.entity

import java.io.Serializable

data class ReqInviteWxToGroupResultEntity(
    var code: Int?, var msg: String?
) : Serializable {
    var remoteId: Long? = 0
    var convId: Long? = 0
}