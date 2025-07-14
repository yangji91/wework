package com.robot.netty.handle.imple.entity

data class MessageEntity(
    var actionType: String?, // task
    var taskType: String?, // sendMsg
    var msgType: String?, // txt
    var reqid: String?, // 123
    var result: Result?
) {
    public data class Result(
        var remoteId: Long, // remoteId

        //文本消息内容 or 图片消息url
        var content: String?,

        //图片消息url
//        var imgUrl: String?,
        //自加字段
        var imgLocalPath: String?,

        var videoLocalPath: String?,

        var voiceLocalPath: String?,


        //卡片消息地址
        var linkUrl: String?,
        //卡片消息名称
        var linkTitle: String?,
        //卡片消息描述
        var linkDesc: String?,
        //卡片消息图片
        var linkImgUrl: String?,


        var miniProgramThumbUrl: String?,//图片地址 选填
        var miniProgramAppId: String?,//  appId 选填 非必须
        var miniProgramUsername: String?, //例 gh_5232ef019802@app 必填
        var miniProgramPagePath: String?,  // 打开小程序的页面 选填
        var miniProgramTitle: String?, // 发送的title 选填
        var miniProgramLogoUrl: String?, //小程序 图标 选填
        var miniProgramAppName: String?,


        var miniProgramLocalThumbUrl: String?//自加字段
    )
}