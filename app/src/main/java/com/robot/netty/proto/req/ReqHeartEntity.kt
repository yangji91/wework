package com.robot.netty.proto.req

import java.io.Serializable

class ReqHeartEntity : Serializable {
    @JvmField
    var self_headurl: String? = ""

    @JvmField
    var self_nickname: String? = ""

    @JvmField
    var bbsName: String? = ""

    @JvmField
    var phoneNum: String? = ""

    @JvmField
    var juhe: String? = ""
}
