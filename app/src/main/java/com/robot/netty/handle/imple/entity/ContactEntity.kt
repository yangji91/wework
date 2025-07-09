package com.robot.netty.handle.imple.entity

data class ContactEntity(
    var actionType: String?, // task
    var taskType: String?, // sendMsg
    var msgType: String?, // txt
    var reqid: String?, // 123
    var result: Result?

) {
    data class Result(
        var verify: String?, // 哈喽 加个好友啊
        var phonenum: String?, // txt
        var wxtype: String?, // txt


        //查询微信好友信息
        var wxId: String?,// txt
        var wxNicName: String?, // txt
        var wxTicket: String?, // txt

        //企微同意需要的参数
        var wxProfileCode: String?,

        //申请加好友需要
        var wxApplyContent: String? // txt
    )
}