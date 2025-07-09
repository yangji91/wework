package com.robot.netty.proto.res

data class ResSearchAddContactEntity(
    var content: String?, // 哈喽 加个好友啊
    var msgtype: String?, // getphonenum_more
    var phonenum: List<String?>?
)