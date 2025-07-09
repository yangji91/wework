package com.robot.netty.proto.req

import java.io.Serializable

class ReqUserEntity : Serializable {

    @JvmField
    var headUrl: String? = ""

    @JvmField
    var remoteId: String? = ""

//    //暂时没有
//    @JvmField
//    var uid: String = ""

    @JvmField
    var nickname: String? = ""

    @JvmField
    var content: String? = ""
}
