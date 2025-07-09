package com.robot.netty.proto.req

import java.io.Serializable

class ReqConvEntity : Serializable {
    @JvmField
    var convId: Long = 0

    @JvmField
    var convName: String = ""

    @JvmField
    var memberCount: Int = 0
}
