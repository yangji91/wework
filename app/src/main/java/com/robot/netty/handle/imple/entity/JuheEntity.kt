package com.robot.netty.handle.imple.entity

data class JuheEntity(
    var actionType: String?, // task
    var taskType: String?, // sendMsg
    var reqid: String?, // 123
    var result: Result?

) {
    data class Result(
        var juhe: String?
    )
}