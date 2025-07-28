package com.robot.netty.proto.req

import java.io.Serializable

class ReqMsgEntity : Serializable {

    @JvmField
    var senderRemoteId: String = ""
    var receiverRemoteId: String = ""

//    @JvmField
//    var nickname: String = ""

    @JvmField
    var contentType: Int = -1

    @JvmField
    var msgId: Long = 0

    @JvmField
    var content: String = ""

    @JvmField
    var ttsText: String = ""

    @JvmField
    var imgUrl: String = ""

    @JvmField
    var linkUrl: String = ""

    @JvmField
    var linkTitle: String = ""

    @JvmField
    var linkDesc: String = ""

    @JvmField
    var linkImgUrl: String = ""

    @JvmField
    var miniProgramThumbUrl: String = ""//图片地址 选填

    @JvmField
    var miniProgramAppId: String = ""//  appId 选填 非必须

    @JvmField
    var miniProgramUsername: String = "" //例 gh_5232ef019802@app 必填

    @JvmField
    var miniProgramPagePath: String = ""  // 打开小程序的页面 选填

    @JvmField
    var miniProgramTitle: String = "" // 发送的title 选填

    @JvmField
    var miniProgramLogoUrl: String = "" //小程序 图标 选填

    @JvmField
    var miniProgramAppName: String = ""

}
