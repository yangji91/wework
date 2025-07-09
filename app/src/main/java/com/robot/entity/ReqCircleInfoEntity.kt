package com.robot.entity

import java.io.Serializable

data class ReqCircleInfoEntity(
    var code: Int?,
    var msg: String?,
    var circleInfoEntity: CircleInfoEntity?
) : Serializable{

    data class CircleInfoEntity(
        var text: String?,// 哈喽 加个好友啊
        var videoNumberId: String?,
        var contactNickName: String?,
        var coverPath: String?
    ) : Serializable
}