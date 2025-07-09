package com.robot.netty.proto.req

data class ReqMsgLinkEntity(
    var description: String?, // 智能表格，是比表格更强大的数据管理工具。适用于项目管理、经营分析、门店巡检、工单跟进等场景。
    var imageUrl: String?, // https://wwcdn.weixin.qq.com/node/wework/images/mini_cover_default.1409d641cf.png
    var linkUrl: String?, // https://work.weixin.qq.com/nl/act/p/35e4ddf7a00f45a0?ver=4.1.26&type=new&lang=zh
    var title: String? // 4.1.26 版本新功能介绍
)