package com.robot.netty.handle.imple.entity

data class AvatarEntity(
    var actionType: String?, // task
    var taskType: String?, // sendMsg
    var reqid: String?, // 123
    var result: Result?

) {
    data class Result(
        var avatar: String?
    ) {
        var avatarLocalPath: String = ""
    }
}