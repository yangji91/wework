package com.robot.entity

import java.io.Serializable

data class ReqAddContactResultEntity(
    var gw: Result?
) : Serializable {

    class Result : Serializable {
        var phonenum: String? = ""
        var remoteId: String? = ""
        var wxId: String? = ""
        var wxProfileCode: String? = ""
        var code: Int? = -1
        var msg: String? = ""
    }
}