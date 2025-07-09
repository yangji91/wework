package com.robot.netty.handle.imple.entity

data class QueryResEntity(
    var actionType: String?, // task
    var taskType: String?, // sendMsg
    var msgType: String?, // txt
    var reqid: String?, // 123
    var result: Result?

) {
    data class Result(
        //客服
        var url: String? // txt
    )
}