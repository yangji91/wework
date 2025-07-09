package com.robot.netty.handle.imple.entity

data class CircleEntity(
    var actionType: String?, // task
    var taskType: String?, // sendMsg
    var msgType: String?, // txt
    var reqid: String?, // 123
    var result: Result?

) {
    data class Result(
        var text: String?,// 哈喽 加个好友啊
        var imgUrlList: List<String>?,
        var videoNumberId: String?,
        var sndId: String?,
        var contactNickName: String?,
        var coverPath: String?
    )
}