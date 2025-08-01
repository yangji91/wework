package com.robot.entity

data class OriginMsgEntity(
//    var fileMessage: Boolean, // false
    var info: Info?
//    var mNativeHandle: Long, // 3423200760
//    var nativeHandle: Long // 3423200760
) {
    data class Info(
//        var ackSendState: Int, // 0
//        var appinfo: String?, // CAMQ8cKwswYYgej/4JCAgAMgsLTV9wk=
//        var asId: Int, // 0
//        var cachedSize: Int, // 142
//        var chatReqForwardKey: Int, // 0
        var content: String? //  哈喽 早上好111
//        var contentType: Int, // 0
//        var convType: Int, // 0
//        var conversationId: Long, // 7881301527936324
//        var devinfo: Int, // 131073
//        var doNotAddUnreadCnt: Boolean, // false
//        var dontIncreaseUnreadCount: Boolean, // false
//        var extraContent: ExtraContent?,
//        var extras: Extras?,
//        var fakeCollectionMsgAppinfo: String?,
//        var flag: Int, // 4
//        var forceAddUnreadCnt: Boolean, // false
//        var id: Long, // 7380310659239834282
//        var innerkfVid: Int, // 0
//        var isInnerkfMannager: Boolean, // false
//        var isUpdateByMemory: Boolean, // false
//        var isUpdateMsg: Boolean, // false
//        var outContact: Int, // 1
//        var progress: Progress?,
//        var referid: Int, // 0
//        var remoteId: Int, // 0
//        var sendScene: Int, // 4
//        var sendTime: Int, // 1718362481
//        var sender: Long, // 1688854358651905
//        var seq: Int, // 0
//        var serializedSize: Int, // 142
//        var state: Int, // 1
//        var url: String?
    ) {
        data class ExtraContent(
            var cachedSize: Int, // 0
            var serializedSize: Int // 0
        )

        data class Extras(
            var cachedSize: Int, // 11
            var canNotBeLastmessage: Boolean, // false
            var decryptRet: Int, // -1
            var disableDataDetector: Boolean, // false
            var firstSentMessageThatDay: Boolean, // false
            var isAlertReachedReaded: Boolean, // false
            var isCancelSend: Boolean, // false
            var isSphLiveStop: Boolean, // false
            var isSvrFail: Boolean, // false
            var noneedreaduins: List<Any?>?,
            var openapiAssociateKey: String?,
            var openapiAssociateType: Int, // 0
            var orderTime: Int, // 0
            var originalContent: String?,
            var originalContenttype: Int, // 0
            var readuins: List<Any?>?,
            var receiptModeEntry: Boolean, // false
            var `receiver`: Int, // 0
            var revokeTime: Int, // 0
            var sendErrorCode: Int, // 0
            var sendername: String?, // 李泽鹏
            var serializedSize: Int, // 11
            var showTranslation: Boolean, // false
            var staffVid: Int, // 0
            var topMsgCreatorId: Int, // 0
            var translation: String?,
            var translationProvider: String?,
            var unreaduins: List<Any?>?,
            var updateMsgRemoteId: Int // 0
        )

        data class Progress(
            var cachedSize: Int, // 14
            var now: Int, // 0
            var sendTimeActual: Long, // 1718362481795
            var sendTimeEnqueue: Long, // 1718362481733
            var serializedSize: Int, // 14
            var total: Int // 0
        )
    }
}