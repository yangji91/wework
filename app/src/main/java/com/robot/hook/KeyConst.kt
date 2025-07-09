package com.robot.hook

import com.robot.common.Global
import com.robot.common.MConfiger

object KeyConst {

    @JvmField
    var C_LoginWxAuthActivity = "com.tencent.wework.login.controller.LoginWxAuthActivity"

    @JvmField
    var C_WwMainActivity = "com.tencent.wework.launch.WwMainActivity"

    @JvmField
    var M_WwMainActivity_mine = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "aqj"
        MConfiger.WEWORK_VERSION_19717 -> "j9"
        MConfiger.WEWORK_VERSION_31145 -> "zb"
        else -> ""
    }

    @JvmField
    var M_WwMainActivity_mine_findIndex = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "aqg"
        MConfiger.WEWORK_VERSION_19717 -> "V8"
        MConfiger.WEWORK_VERSION_31145 -> "kb"
        else -> ""
    }

    @JvmField
    var F_WwMainActivity_mine_index = 2


    @JvmField
    var I_IPickMessageCallback = "com.tencent.wework.foundation.callback.IPickMessageCallback"

    @JvmField
    var C_SetConversationOpenCallback =
        "com.tencent.wework.foundation.callback.SetConversationOpenCallback"

    @JvmField
    var M_SetConversationOpenCallback_onResult = "onResult"

    @JvmField
    var C_WwCustomer_GroupSendNotifyMsg =
        "com.tencent.wework.foundation.model.pb.WwCustomer\$GroupSendNotifyMsg"


    @JvmField
    var C_mbi = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "mbi"
        MConfiger.WEWORK_VERSION_19717 -> "rjf"
        MConfiger.WEWORK_VERSION_31145 -> "dt60"
        else -> ""
    }

    @JvmField
    var M_mbi_onTPFEvent = "onTPFEvent"

    @JvmField
    var P_mbi_onTPFEvent_event = "wework.login.event"


    @JvmField
    var C_LinkMessage = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "com.tencent.wework.foundation.model.pb.WwRichmessage"
        MConfiger.WEWORK_VERSION_19717 -> "com.tencent.wework.foundation.model.pb.LinkMessageProto"
        MConfiger.WEWORK_VERSION_31145 -> "com.tencent.wework.foundation.model.pb.LinkMessageProto"
        else -> ""
    }

    @JvmField
    var F_LinkMessage_wEAPPMESSAGE = "wEAPPMESSAGE"

    @JvmField
    var M_WwRichmessage_buildMessage = "buildMessage"


    @JvmField
    var C_WwMessage_Message = "com.tencent.wework.foundation.model.pb.WwMessage\$Message"



    @JvmField
    var C_ContactManager = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_19717 -> "com.tencent.wework.contact.model.ContactManager"
        MConfiger.WEWORK_VERSION_31145 -> "com.tencent.wework.contact.model.ContactManager"
        else -> ""
    }

    @JvmField
    var M_ContactManager_M0 = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_19717 -> "M0"
        MConfiger.WEWORK_VERSION_31145 -> "Y0"
        else -> ""
    }

    @JvmField
    var C_CallEvent = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_19717 -> "wif"
        MConfiger.WEWORK_VERSION_31145 -> "d7"
        else -> ""
    }

    @JvmField
    var M_CallEvent_onVoipEvent = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_19717 -> "R0"
        MConfiger.WEWORK_VERSION_31145 -> "p0"
        else -> ""
    }

    @JvmField
    var F_CallEvent_onVoipEvent_STATE_TALK_READY = "STATE_TALK_READY"

    @JvmField
    var F_CallEvent_onVoipEvent_STATE_EXIT_ROOM = "STATE_EXIT_ROOM"


    @JvmField
    var M_ContactManager_SearchContact = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_19717 -> "f"
        MConfiger.WEWORK_VERSION_31145 -> "t"
        else -> ""
    }

    @JvmField
    var I_IGetUserByIdCallback2 = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_19717 -> "com.tencent.wework.foundation.callback.IGetUserByIdCallback2"
        MConfiger.WEWORK_VERSION_31145 -> "com.tencent.wework.foundation.callback.IGetUserByIdCallback2"
        else -> ""
    }


    @JvmField
    var I_IExecSqlCallback =
        "com.tencent.wework.foundation.logic.AppObserverService.IExecSqlCallback"

    @JvmField
    var M_IExecSqlCallback_onResult = "onResult"

    @JvmField
    var F_IExecSqlCallback_StorageDBResponse_errMsg = "errMsg"

    @JvmField
    var F_IExecSqlCallback_StorageDBResponse_execTime = "execTime"

    @JvmField
    var F_IExecSqlCallback_StorageDBResponse_rows = "rows"

    @JvmField
    var F_IExecSqlCallback_StorageDBResponse_rows_columns = "columns"

    @JvmField
    var F_IExecSqlCallback_StorageDBResponse_rows_values = "values"

    @JvmField
    var M_IExecSqlCallback_toString = "toString"

    @JvmField
    var C_AppObserverService = "com.tencent.wework.foundation.logic.AppObserverService"

    @JvmField
    var M_AppObserverService_getService = "getService"

    @JvmField
    var M_AppObserverService_execSql = "execSql"


    @JvmField
    var C_IConversation_a = "com.tencent.wework.msg.api.IConversation\$a"


    @JvmField
    var C_IFetchSessionListCallback =
        "com.tencent.wework.foundation.callback.IFetchSessionListCallback"

    @JvmField
    var M_IFetchSessionListCallback_onResult = "onResult"

    @JvmField
    var C_bugly_e = "com.tencent.bugly.crashreport.crash.e"

    @JvmField
    var C_WxaJvmCrashHandler = "com.tencent.luggage.wxaapi.internal.crash.WxaJvmCrashHandler"

    @JvmField
    var C_FileService = "com.tencent.wework.foundation.logic.FileService"

    @JvmField
    var M_FileService_getService = "getService"

    @JvmField
    var M_FileService_FtnDownloadFileToPath = "FtnDownloadFileToPath"

    @JvmField
    var M_FileService_CdnDownloadFileToPath = "CdnDownloadFileToPath"

    @JvmField
    var C_Application = "com.tencent.wework.foundation.logic.Application"

    @JvmField
    val C_Application_getInstance = "getInstance"

    @JvmField
    val M_Application_GetProfileManager = "GetProfileManager"

    @JvmField
    val M_Application_getCorpId = "getCorpId"

    @JvmField
    val M_Application_getVid = "getVid"

    @JvmField
    val M_Application_GetProfileManager_GetCurrentProfile = "GetCurrentProfile"

    @JvmField
    val M_Application_GetProfileManager_GetCurrentProfile_getServiceManager = "getServiceManager"

    @JvmField
    val M_Application_GetProfileManager_GetCurrentProfile_getServiceManager_getFileService =
        "getFileService"

    @JvmField
    val M_Application_GetProfileManager_GetCurrentProfile_getServiceManager_getFileService_FtnUploadFileOnPath =
        "FtnUploadFileOnPath"

    @JvmField
    val M_Application_GetProfileManager_GetCurrentProfile_getServiceManager_GetConversationService =
        "GetConversationService"

    @JvmField
    val M_Application_GetProfileManager_refreshCorpInfo = "refreshCorpInfo"

    @JvmField
    val M_Application_GetProfileManager_getCorpInfo = "getCorpInfo"

    @JvmField
    val F_Application_GetProfileManager_bbsId = "bbsId"

    @JvmField
    val F_Application_GetProfileManager_bbsName = "bbsName"

    @JvmField
    val F_Application_GetProfileManager_name = "name"

    @JvmField
    val F_Application_GetProfileManager_corpFullName = "corpFullName"


    @JvmField
    var C_ContactService = "com.tencent.wework.foundation.logic.ContactService"



    @JvmField
    var M_ContactService_getService = "getService"

    @JvmField
    var M_ContactService_OperateContact = "OperateContact"


    @JvmField
    var I_IGetUserByIdCallback = "com.tencent.wework.foundation.callback.IGetUserByIdCallback"


    @JvmField
    var C_IAccount = "com.tencent.wework.login.api.IAccount"

    @JvmField
    var C_MK = "com.tencent.wecomponent.MK"

    @JvmField
    var M_MK_SERVICE = "service"

    @JvmField
    var C_IAccount_CC = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "com.tencent.wework.login.api.IAccount\$-CC"
        MConfiger.WEWORK_VERSION_19717 -> "kge"
        MConfiger.WEWORK_VERSION_31145 -> "u4j"
        else -> ""
    }

    @JvmField
    var C_IAccount_CC_GET = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "get"
        MConfiger.WEWORK_VERSION_19717 -> "a"
        MConfiger.WEWORK_VERSION_31145 -> "a"
        else -> ""
    }

    @JvmField
    var M_IACCOUNT_GETLOGINUSER = "getLoginUser"

    @JvmField
    var M_IACCOUNT_GETLOGINUSERID = "getLoginUserId"


    @JvmField
    var C_ConversationService = "com.tencent.wework.foundation.logic.ConversationService"

    @JvmField
    var M_Conversation_getInfo = "getInfo"

    @JvmField
    var F_Conversation_getInfo_id = "id"

    @JvmField
    var M_ConversationService_setConversationOpen = "setConversationOpen"

    @JvmField
    var M_ConversationService_getService = "getService"


    @JvmField
    var M_ConversationService_FetchSessionList = "FetchSessionList"

    @JvmField
    var M_ConversationService_MaybeHasMoreConversation = "MaybeHasMoreConversation"


    @JvmField
    var M_ConversationService_GetMessageByAppInfo = "GetMessageByAppInfo"

    @JvmField
    var M_FileMessage_computeSerializedSize = "computeSerializedSize"

    @JvmField
    var M_FileMessage_writeTo = "writeTo"

    @JvmField
    var C_ConversationEngine = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "llh"
        MConfiger.WEWORK_VERSION_19717 -> "f6f"
        MConfiger.WEWORK_VERSION_31145 -> "t49"
        else -> ""
    }


    @JvmField
    var M_ConversationEngine_getConversationService_GetHistoryMessage = "GetHistoryMessage"


    @JvmField
    var M_ConversationEngine_a = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "a"
        MConfiger.WEWORK_VERSION_19717 -> "P"
        MConfiger.WEWORK_VERSION_31145 -> "Z1"
        else -> ""
    }

    @JvmField
    var M_ConversationEngine_getConversationService = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "getConversationService"
        MConfiger.WEWORK_VERSION_19717 -> "X2"
        MConfiger.WEWORK_VERSION_31145 -> "s4"
        else -> ""
    }

    @JvmField
    var M_ConversationEngine_USER = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "getUserWithoutUpate"
        MConfiger.WEWORK_VERSION_19717 -> "m4"
        MConfiger.WEWORK_VERSION_31145 -> "M5"
        else -> ""
    }

    @JvmField
    var M_ConversationEngine_USER_getInfo = "getInfo"

    @JvmField
    var F_ConversationEngine_USER_name = "name"

    @JvmField
    var F_ConversationEngine_USER_avatorUrl = "avatorUrl"

    @JvmField
    var F_ConversationEngine_USER_gender = "gender"

    @JvmField
    var F_ConversationEngine_USER_getCorpName = "getCorpName"

    @JvmField
    var F_CONVERSATIONENGINE_USER_USERREMOTEID = "userRemoteId"

    @JvmField
    var M_ConversationEngine_getInstance = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "hKK"
        MConfiger.WEWORK_VERSION_19717 -> "w3"
        MConfiger.WEWORK_VERSION_31145 -> "U4"
        else -> ""
    }

    @JvmField
    var M_ConversationEngine_updateConversationCache = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "updateConversationCache"
        MConfiger.WEWORK_VERSION_19717 -> "b8"
        MConfiger.WEWORK_VERSION_31145 -> "K9"
        else -> ""
    }

    @JvmField
    var M_Conversation_Get_ConvItem = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "tC"
        MConfiger.WEWORK_VERSION_19717 -> "K2"
        MConfiger.WEWORK_VERSION_31145 -> "f4"
        else -> ""
    }

    @JvmField
    var C_ConversationEngine_IConversationListObserver = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "$C_ConversationEngine\$77"
        MConfiger.WEWORK_VERSION_19717 -> "$C_ConversationEngine\$g2"
        MConfiger.WEWORK_VERSION_31145 -> "$C_ConversationEngine\$j2"
        else -> ""
    }

    @JvmField
    var M_ConversationEngine_IConversationListObserver_onAddConversations = "onAddConversations"

    @JvmField
    var C_User = "com.tencent.wework.foundation.model.User"


    @JvmField
    var I_ICommonConversationOperateCallback =
        "com.tencent.wework.foundation.callback.ICommonConversationOperateCallback"


    @JvmField
    var M_ICommonConversationOperateCallback_onResult = "onResult"

    @JvmField
    var I_ICommonResultCallback = "com.tencent.wework.foundation.callback.ICommonResultCallback"

    @JvmField
    var M_ICommonResultCallback_onResult = "onResult"


    @JvmField
    val C_Conversation_NativeHandleHolder = "com.tencent.wework.foundation.model.Conversation"

    @JvmField
    var C_GroupSettingEngine = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "lno"
        MConfiger.WEWORK_VERSION_19717 -> "t6f"
        MConfiger.WEWORK_VERSION_31145 -> "ueh"
        else -> ""
    }

    @JvmField
    var M_GroupSettingEngine_getInstance = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "hRy"
        MConfiger.WEWORK_VERSION_19717 -> "a0"
        MConfiger.WEWORK_VERSION_31145 -> "C0"
        else -> ""
    }

    @JvmField
    var M_GROUPSETTINGENGIN_SETCONVERSATION = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "setConversation"
        MConfiger.WEWORK_VERSION_19717 -> "e1"
        MConfiger.WEWORK_VERSION_31145 -> "I1"
        else -> ""
    }

    @JvmField
    var M_GROUPSETTINGENGIN_getConversationMembers = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "getConversationMembers"
        MConfiger.WEWORK_VERSION_19717 -> "K"
        MConfiger.WEWORK_VERSION_31145 -> "m0"
        else -> ""
    }

    @JvmField
    var C_ConversationEngine_IConversationObserver = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "$C_ConversationEngine\$76"
        MConfiger.WEWORK_VERSION_19717 -> "$C_ConversationEngine\$f2"
        MConfiger.WEWORK_VERSION_31145 -> "$C_ConversationEngine\$i2"
        else -> ""
    }

    @JvmField
    var M_ConversationEngine_IConversationObserver_onAddMessages = "onAddMessages"

    @JvmField
    var M_ConversationEngine_IConversationObserver_onUnReadCountChanged = "onUnReadCountChanged"


    @JvmField
    var C_MessageManager = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "lpt"
        MConfiger.WEWORK_VERSION_19717 -> "com.tencent.wework.msg.model.MessageManager"
        MConfiger.WEWORK_VERSION_31145 -> "m4r"
        else -> ""
    }

    @JvmField
    var M_MessageManager_sendTextualMessage = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "sendTextualMessage"
        MConfiger.WEWORK_VERSION_19717 -> "j5"
        MConfiger.WEWORK_VERSION_31145 -> "c7"
        else -> ""
    }

    @JvmField
    var M_MessageManager_buildVideoMessage = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "buildVideoMessage"
        MConfiger.WEWORK_VERSION_19717 -> "I0"
        MConfiger.WEWORK_VERSION_31145 -> "M1"
        else -> ""
    }


    @JvmField
    var C_WwRichmessage_RichMessage = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "com.tencent.wework.foundation.model.pb.WwRichmessage.RichMessage"
        else -> "com.tencent.wework.foundation.model.pb.WwRichmessageBase.RichMessage"
    }

    @JvmField
    var M_WwRichmessage_RichMessage_parseFrom = "parseFrom"

    @JvmField
    var F_WwRichmessage_RichMessage_messages = "messages"

    @JvmField
    var F_WwRichmessage_RichMessage_messages_contentType = "contentType"

    @JvmField
    var F_WwRichmessage_RichMessage_messages_data = "data"

    @JvmField
    var F_WwRichmessage_RichMessage_messages_data_name = "name"

    @JvmField
    var F_WwRichmessage_RichMessage_messages_data_uin = "uin"

    @JvmField
    var C_AttendanceApiImpl = "com.tencent.wework.enterprise.attendance.AttendanceApiImpl"

    @JvmField
    var M_AttendanceApiImpl_onLogout = "onLogout"


    @JvmField
    var C_i2e = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "ksv"
        MConfiger.WEWORK_VERSION_19717 -> "i2e"
        MConfiger.WEWORK_VERSION_31145 -> "tl80"
        else -> ""
    }

    @JvmField
    var uncaughtException = "uncaughtException"

    @JvmField
    var C_Log = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "hbk"
        MConfiger.WEWORK_VERSION_19717 -> "zz9"
        MConfiger.WEWORK_VERSION_31145 -> "a1"
        else -> ""
    }

    @JvmField
    var M_LOG_D = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "l"
        MConfiger.WEWORK_VERSION_19717 -> "l"
        MConfiger.WEWORK_VERSION_31145 -> "n"
        else -> ""
    }

    @JvmField
    var M_LOG_W = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "m"
        MConfiger.WEWORK_VERSION_19717 -> "C"
        MConfiger.WEWORK_VERSION_31145 -> "F"
        else -> ""
    }

    @JvmField
    var C_SendExtraInfo = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "com.tencent.wework.msg.api.SendExtraInfo"
        MConfiger.WEWORK_VERSION_19717 -> "com.tencent.wework.msg.api.SendExtraInfo"
        MConfiger.WEWORK_VERSION_31145 -> "cs00"
        else -> ""
    }

    @JvmField
    var M_SendExtraInfo_getInfo = "getInfo"

    @JvmField
    var F_SendExtraInfo_getInfo_id = "id"

    @JvmField
    var I_ISendMessageCallback = "com.tencent.wework.foundation.callback.ISendMessageCallback"

    @JvmField
    var C_WwRichmessageBase_LinkMessage = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "com.tencent.wework.foundation.model.pb.WwRichmessageBase\$LinkMessage"
        MConfiger.WEWORK_VERSION_19717 -> "com.tencent.wework.foundation.model.pb.LinkMessageProto\$LinkMessage"
        MConfiger.WEWORK_VERSION_31145 -> "com.tencent.wework.foundation.model.pb.LinkMessageProto\$LinkMessage"
        else -> ""
    }

    @JvmField
    var M_WwRichmessageBase_LinkMessage_setExtension = "setExtension"

    @JvmField
    var M_WwRichmessageBase_LinkMessage_parseFrom = "parseFrom"

    @JvmField
    var F_WwRichmessageBase_LinkMessage_parseFrom_cmd = "cmd"

    @JvmField
    var F_WwRichmessageBase_LinkMessage_parseFrom_description = "description"

    @JvmField
    var F_WwRichmessageBase_LinkMessage_parseFrom_imageData = "imageData"

    @JvmField
    var F_WwRichmessageBase_LinkMessage_parseFrom_imageUrl = "imageUrl"

    @JvmField
    var F_WwRichmessageBase_LinkMessage_parseFrom_linkUrl = "linkUrl"

    @JvmField
    var F_WwRichmessageBase_LinkMessage_parseFrom_title = "title"

    @JvmField
    var F_WwRichmessageBase_LinkMessage_parseFrom_openImageUri = "openImageUri"

    @JvmField
    var F_WwRichmessageBase_LinkMessage_parseFrom_aeskey = "aeskey"//byte[]

    @JvmField
    var F_WwRichmessageBase_LinkMessage_parseFrom_authkey = "authkey"//byte[]

    @JvmField
    var F_WwRichmessageBase_LinkMessage_parseFrom_md5 = "md5"//byte[]

    @JvmField
    var F_WwRichmessageBase_LinkMessage_parseFrom_size = "size"

    @JvmField
    var F_WwRichmessageBase_LinkMessage_parseFrom_url = "url"

    @JvmField
    var C_WwRichmessageBase_RoomTips = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "com.tencent.wework.foundation.model.pb.WwRichmessage\$RoomTipsList"
        MConfiger.WEWORK_VERSION_19717 -> "com.tencent.wework.foundation.model.pb.RoomTipProto\$RoomTipsList"
        MConfiger.WEWORK_VERSION_31145 -> "com.tencent.wework.foundation.model.pb.RoomTipProto\$RoomTipsList"
        else -> ""
    }

    @JvmField
    var M_WwRichmessageBase_RoomTips_parseFrom = "parseFrom"

    @JvmField
    var F_WwRichmessageBase_RoomTips_parseFrom_subtype = "subtype"

    @JvmField
    var F_WwRichmessageBase_RoomTips_parseFrom_tipslist = "tipslist"

    @JvmField
    var F_WwRichmessageBase_RoomTips_parseFrom_tipslist_newRoomcreator = "newRoomcreator"

    @JvmField
    var F_WwRichmessageBase_RoomTips_parseFrom_tipslist_oldRoomcreator = "oldRoomcreator"

    @JvmField
    var F_WwRichmessageBase_RoomTips_parseFrom_tipslist_tips = "tips"

    @JvmField
    var F_WwRichmessageBase_RoomTips_parseFrom_tipslist_vidNameShowType = "vidNameShowType"

    @JvmField
    var F_WwRichmessageBase_RoomTips_parseFrom_tipslist_vid = "vid"

    @JvmField
    var F_LinkMessage_linkUrl = "linkUrl"

    @JvmField
    var F_LinkMessage_title = "title"

    @JvmField
    var F_LinkMessage_description = "description"

    @JvmField
    var F_LinkMessage_cmd = "cmd"

    @JvmField
    var F_LinkMessage_imageUrl = "imageUrl"

    @JvmField
    var C_UserManager = "com.tencent.wework.contact.api.IUserManager"

    @JvmField
    var C_UserManager_CC = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "com.tencent.wework.contact.api.IUserManager\$-CC"
        MConfiger.WEWORK_VERSION_19717 -> "ypa"
        MConfiger.WEWORK_VERSION_31145 -> "r6k"
        else -> ""
    }

    @JvmField
    var C_UserManager_CC_get = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "get"
        MConfiger.WEWORK_VERSION_19717 -> "a"
        MConfiger.WEWORK_VERSION_31145 -> "a"
        else -> ""
    }

    @JvmField
    var M_UserManager_refreshUserByIdWithScene = "refreshUserByIdWithScene"

    @JvmField
    var M_UserManager_getUserByIdWithScene = "getUserByIdWithScene"

    @JvmField
    var C_FileUpAndDownLoadEngine = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "guw"
        MConfiger.WEWORK_VERSION_19717 -> "zs9"
        MConfiger.WEWORK_VERSION_31145 -> "v6f"
        else -> ""
    }

    @JvmField
    var M_FileUpAndDownLoadEngine_DownloadFile = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "DownloadFile"
        MConfiger.WEWORK_VERSION_19717 -> "a"
        MConfiger.WEWORK_VERSION_31145 -> "m"
        else -> ""
    }

    @JvmField
    var M_FileUpAndDownLoadEngine_getInstance = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "dDi"
        MConfiger.WEWORK_VERSION_19717 -> "E"
        MConfiger.WEWORK_VERSION_31145 -> "H"
        else -> ""
    }

    @JvmField
    val C_FTNDOWNLOAD_CALLBACK = "com.tencent.wework.foundation.callback.IFtnDownloadCallback"

    @JvmField
    val C_IFtnUploadCallback_CALLBACK = "com.tencent.wework.foundation.callback.IFtnUploadCallback"

    @JvmField
    val C_FTNPROGRO_CALLBACK = "com.tencent.wework.foundation.callback.IFtnProgressCallback"

    @JvmField
    val M_FTNPROGRO_CALLBACK_onResult = "onResult"

    @JvmField
    var C_FileUpAndDownLoadEngine_a = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "$C_FileUpAndDownLoadEngine\$a"
        MConfiger.WEWORK_VERSION_19717 -> "$C_FileUpAndDownLoadEngine\$d0"
        MConfiger.WEWORK_VERSION_31145 -> "$C_FileUpAndDownLoadEngine\$k0"
        else -> ""
    }

    @JvmField
    var M_FileUpAndDownLoadEngine_onResult = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "D"
        MConfiger.WEWORK_VERSION_19717 -> "b"
        MConfiger.WEWORK_VERSION_31145 -> "onDownloadCompleted"
        else -> ""
    }

    @JvmField
    val M_FILEDOWNLOAD_DOWNLOAD = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "a"
        MConfiger.WEWORK_VERSION_19717 -> "Z"
        MConfiger.WEWORK_VERSION_31145 -> "i0"
        else -> ""
    }

    @JvmField
    val C_MESSAGE_NANO = "com.google.protobuf.nano.MessageNano"

    @JvmField
    val M_MESSAGE_NANO_toByteArray = "toByteArray"

    @JvmField
    val C_MESSAGE_VIDEO = "com.tencent.wework.foundation.model.pb.WwRichmessage\$VideoMessage"

    @JvmField
    val M_MESSAGE_VIDEO_parseFrom = "parseFrom"

    @JvmField
    val C_MESSAGE = "com.tencent.wework.foundation.model.Message"

    @JvmField
    val M_MESSAGE_NewMessage = "NewMessage"


    @JvmField
    val F_MESSAGE_NewMessage_mNativeHandle = "mNativeHandle"

    @JvmField
    val M_MESSAGE_NewMessage_setInfo = "setInfo"

    @JvmField
    val C_WwRickMsg_FileMsg = "com.tencent.wework.foundation.model.pb.WwRichmessage\$FileMessage"

    @JvmField
    val M_WwRickMsg_FileMsg_parseFrom = "parseFrom"

    @JvmField
    val M_Conversion_requestInfo = "requestInfo"

    @JvmField
    val F_Conversion_requestInfo_autoMarkRead = "autoMarkRead"

    @JvmField
    val F_Conversion_requestInfo_createTime = "createTime"

    @JvmField
    val F_Conversion_requestInfo_creatorId = "creatorId"

    @JvmField
    val F_Conversion_requestInfo_exited = "exited"

    @JvmField
    val F_Conversion_requestInfo_forwardLeaveMsg = "forwardLeaveMsg"

    @JvmField
    val F_Conversion_requestInfo_hidden = "hidden"

    @JvmField
    val F_Conversion_requestInfo_id = "id"

    @JvmField
    val F_Conversion_requestInfo_isStickied = "isStickied"

    @JvmField
    val F_Conversion_requestInfo_modifyTime = "modifyTime"

    @JvmField
    val F_Conversion_requestInfo_name = "name"

    @JvmField
    val F_Conversion_requestInfo_notified = "notified"

    @JvmField
    val F_Conversion_requestInfo_remoteId = "remoteId"

    @JvmField
    val F_Conversion_requestInfo_searchTime = "searchTime"

    @JvmField
    val F_Conversion_requestInfo_type = "type"

    @JvmField
    val F_Conversion_requestInfo_updateMember = "updateMember"

    @JvmField
    val F_Conversion_requestInfo_getMemberCount = "getMemberCount"

    @JvmField
    val F_Conversion_requestInfo_extras = "extras"

    @JvmField
    val F_Conversion_requestInfo_getMembers = "getMembers"

    @JvmField
    val F_Conversion_requestInfo_extras_newFlag = "newFlag"

    @JvmField
    val F_Conversion_requestInfo_extras_admins = "admins"

    @JvmField
    val F_Conversion_requestInfo_extras_admins_vid = "vid"

    @JvmField
    val M_Conversion_IsExternalGroup = "IsExternalGroup"

    @JvmField
    val M_Conversion_getIsVipConv = "getIsVipConv"

    @JvmField
    val M_Conversion_enableExteralAdmin = "enableExteralAdmin"

    @JvmField
    val F_ConvMember_avatorUrl = "avatorUrl"

    @JvmField
    val F_ConvMember_englishName = "englishName"

    @JvmField
    val F_ConvMember_invited = "invited"

    @JvmField
    val F_ConvMember_joinTime = "joinTime"

    @JvmField
    val F_ConvMember_name = "name"

    @JvmField
    val F_ConvMember_nickName = "nickName"

    @JvmField
    val F_ConvMember_userCorpId = "userCorpId"

    @JvmField
    val F_ConvMember_userRemoteId = "userRemoteId"

    @JvmField
    val F_WwRickMsg_FileMsg_url = "url"

    @JvmField
    val F_WwRickMsg_FileMsg_fileName = "fileName"

    @JvmField
    val F_WwRickMsg_FileMsg_size = "size"

    @JvmField
    val F_WwRickMsg_FileMsg_width = "width"

    @JvmField
    val F_WwRickMsg_FileMsg_height = "height"

    @JvmField
    val F_WwRickMsg_FileMsg_aesKey = "aesKey"

    @JvmField
    val F_WwRickMsg_FileMsg_fileId = "fileId"

    @JvmField
    val F_WwRickMsg_FileMsg_md5 = "md5"


    @JvmField
    val C_WwRichmessage_AtMessage =
        "com.tencent.wework.foundation.model.pb.WwRichmessage\$AtMessage"


    @JvmField
    val M_WwRichmessage_AtMessage_parseFrom = "parseFrom"


    @JvmField
    val C_MSG_CC = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "com.tencent.wework.msg.api.IMsg\$-CC"
        MConfiger.WEWORK_VERSION_19717 -> "cue"
        MConfiger.WEWORK_VERSION_31145 -> "ftj"
        else -> ""
    }

    @JvmField
    val C_MSG_CC_buildLinkMessage = "buildLinkMessage"

    @JvmField
    val C_MSG_CC_buildFileMessage = "buildFileMessage"

    @JvmField
    val M_MSG_CC_formatExpressionText = "formatExpressionText"


    @JvmField
    val M_MSG_CC_buildTextualMessage = "buildTextualMessage"

    @JvmField
    val M_MSG_CC_GET = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "get"
        MConfiger.WEWORK_VERSION_19717 -> "a"
        MConfiger.WEWORK_VERSION_31145 -> "a"
        else -> ""
    }

    @JvmField
    val M_MSG_CC_sendFileMessage = "sendFileMessage"

    @JvmField
    val M_MSG_CC_getMessageItem = "getMessageItem"

    @JvmField
    val M_MSG_CC_getMessageItem_getMessage = "getMessage"

    @JvmField
    val M_MSG_TranslateVoiceText = "TranslateVoiceText"

    @JvmField
    val M_IApplyVoiceResultCallback_onResult = "onResult"

    @JvmField
    val F_MSG_TranslateVoice_transText = "transText"

    @JvmField
    val M_Message_requestInfo = "requestInfo"

    @JvmField
    val F_MSG_TranslateVoice_requestInfo_extras = "extras"

    @JvmField
    val F_MSG_TranslateVoice_requestInfo_extras_voiceTextInfo = "voiceTextInfo"

    @JvmField
    val F_MSG_TranslateVoice_requestInfo_extras_voiceTextInfo_transText = "transText"

    @JvmField
    val C_CONVERSATION_CC = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "com.tencent.wework.msg.api.IConversation\$-CC"
        MConfiger.WEWORK_VERSION_19717 -> "fte"
        MConfiger.WEWORK_VERSION_31145 -> "qcj"
        else -> ""
    }

    @JvmField
    val M_CONVERSATION__AddExternalChatWelcomeMsg = "AddExternalChatWelcomeMsg"

    @JvmField
    val M_CONVERSATION_fetchConversationByKey = "fetchConversationByKey"

    @JvmField
    val M_CONVERSATION_CC_GET = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "get"
        MConfiger.WEWORK_VERSION_19717 -> "a"
        MConfiger.WEWORK_VERSION_31145 -> "a"
        else -> ""
    }

    @JvmField
    val M_CONVERSATION_GETCONVERSATIONITEMBYREMOTEID = "getConversationItemByRemoteId"

    @JvmField
    val M_CONVERSATION_GETCONVERSATIONITEM = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "dTG"
        MConfiger.WEWORK_VERSION_19717 -> "M0"
        MConfiger.WEWORK_VERSION_31145 -> "y1" //属于scj
        else -> ""
    }

    @JvmField
    val M_CONVERSATION_GETITEM = "getConversationItem"

    @JvmField
    val C_ICommonStringCallback = "com.tencent.wework.foundation.callback.ICommonStringCallback"


    @JvmField
    val C_QueryCRMAntiSpamRuleRsp =
        "com.tencent.wework.foundation.model.pb.WwCrmRoom\$QueryCRMAntiSpamRuleRsp"

    @JvmField
    val M_QueryCRMAntiSpamRuleRsp_parseFrom = "parseFrom"

    @JvmField
    val F_QueryCRMAntiSpamRuleRsp_parseFrom_isEnd = "isEnd"

    @JvmField
    val F_QueryCRMAntiSpamRuleRsp_parseFrom_lastTime = "lastTime"

    @JvmField
    val F_QueryCRMAntiSpamRuleRsp_parseFrom_list = "list"


    @JvmField
    val M_ConversationService_GetConversationList = "GetConversationList"

    @JvmField
    val M_GetTemp = "getTemp"

    @JvmField
    val C_IProgressCallback = "com.tencent.wework.foundation.callback.IProgressCallback"

    @JvmField
    val F_MSGITEM_MessageNano_messages_contentType = "contentType"

    @JvmField
    val F_MSGITEM_MessageNano_messages_toByteArray = "toByteArray"

    @JvmField
    val F_MSGITEM_MessageNano_messages_content = "content"

    @JvmField
    val C_ICreateConversationCallback =
        "com.tencent.wework.foundation.callback.ICreateConversationCallback"

    @JvmField
    val M_ICreateConversationCallback_onResult = "onResult"


    @JvmField
    val M_UserManager_getgetUserByIdWithConversation = "getUserByIdWithConversation"


    @JvmField
    val C_MESSAGE_ITEM = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "lpk"
        MConfiger.WEWORK_VERSION_19717 -> "com.tencent.wework.msg.model.MessageItem"
        MConfiger.WEWORK_VERSION_31145 -> "hgq"
        else -> ""
    }

    @JvmField
    val M_MESSAGE_getInfo = "getInfo"

    @JvmField
    val F_MESSAGE_ITEM_id = "id"

    @JvmField
    val F_MESSAGE_ITEM_sendTime = "sendTime"

    @JvmField
    val F_MESSAGE_ITEM_flag = "flag"

    @JvmField
    val M_MESSAGE_ITEM_onMsgUpdate = "onMsgUpdate"

    @JvmField
    val C_UserSceneType = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "com.tencent.wework.common.model.UserSceneType"
        MConfiger.WEWORK_VERSION_19717 -> "com.tencent.wework.common.model.UserSceneType"
        MConfiger.WEWORK_VERSION_31145 -> "kz50"
        else -> ""
    }

    @JvmField
    val F_UserSceneType_mId2 = "mId2"

    @JvmField
    val F_UserSceneType_mSceneType = "mSceneType"

    @JvmField
    val F_UserSceneType_mSceneString = "mSceneString"

    @JvmField
    val C_WwRichmessage_WeAppMessage = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "com.tencent.wework.foundation.model.pb.WwRichmessage\$WeAppMessage"
        MConfiger.WEWORK_VERSION_19717 -> "com.tencent.wework.foundation.model.pb.WeAppMessageProto\$WeAppMessage"
        MConfiger.WEWORK_VERSION_31145 -> "com.tencent.wework.foundation.model.pb.WeAppMessageProto\$WeAppMessage"
        else -> ""
    }

    @JvmField
    val F_WwRichmessage_WeAppMessage_username = "username"

    @JvmField
    val F_WwRichmessage_WeAppMessage_appid = "appid"

    @JvmField
    val F_WwRichmessage_WeAppMessage_type = "type"

    @JvmField
    val F_WwRichmessage_WeAppMessage_pagepath = "pagepath"

    @JvmField
    val F_WwRichmessage_WeAppMessage_pkginfoType = "pkginfoType"

    @JvmField
    val F_WwRichmessage_WeAppMessage_version = "version"

    @JvmField
    val F_WwRichmessage_WeAppMessage_weappIconUrl = "weappIconUrl"

    @JvmField
    val F_WwRichmessage_WeAppMessage_title = "title"

    @JvmField
    val F_WwRichmessage_WeAppMessage_desc = "desc"

    @JvmField
    val F_WwRichmessage_WeAppMessage_appName = "appName"

    @JvmField
    val F_WwRichmessage_WeAppMessage_thumbUrl = "thumbUrl"

    @JvmField
    val F_WwRichmessage_WeAppMessage_thumbWidth = "thumbWidth"

    @JvmField
    val F_WwRichmessage_WeAppMessage_thumbHeight = "thumbHeight"

    @JvmField
    val C_LinkMessageUtil = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "los"
        MConfiger.WEWORK_VERSION_19717 -> "o7f"
        MConfiger.WEWORK_VERSION_31145 -> "y7n"
        else -> ""
    }

    @JvmField
    val M_LinkMessageUtil_getWeAppMessage = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "getWeAppMessage"
        MConfiger.WEWORK_VERSION_19717 -> "q"
        MConfiger.WEWORK_VERSION_31145 -> "x"
        else -> ""
    }

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_appMediaUrl = "appMediaUrl"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_appName = "appName"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_appid = "appid"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_appservicetype = "appservicetype"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_desc = "desc"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_pagepath = "pagepath"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_pkginfoType = "pkginfoType"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_shareId = "shareId"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_shareKey = "shareKey"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_shareName = "shareName"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_thumbAESKey = "thumbAESKey"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_thumbFileId = "thumbFileId"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_thumbHeight = "thumbHeight"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_thumbMD5 = "thumbMD5"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_thumbSize = "thumbSize"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_thumbUrl = "thumbUrl"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_thumbWidth = "thumbWidth"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_title = "title"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_type = "type"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_username = "username"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_version = "version"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_weappIconUrl = "weappIconUrl"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_wechatThumb = "wechatThumb"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_wechatThumb_height = "height"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_wechatThumb_width = "width"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_wechatThumb_imgUri = "imgUri"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_wechatThumb_imgUri_aeskey = "aeskey"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_wechatThumb_imgUri_authkey = "authkey"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_wechatThumb_imgUri_md5 = "md5"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_wechatThumb_imgUri_size = "size"

    @JvmField
    val F_LinkMessageUtil_getWeAppMessage_wechatThumb_imgUri_url = "url"

    @JvmField
    val M_parseFrom = "parseFrom"

    //找不到
    @JvmField
    val FLAG_RECALL = 16777248

    //找不到
    @JvmField
    val FLAG_RECALL_GROUP = 83886112

    @JvmField
    val M_sendMessage = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "sendMessage"
        MConfiger.WEWORK_VERSION_19717 -> "c5"
        MConfiger.WEWORK_VERSION_31145 -> "U6"
        else -> ""
    }

    @JvmField
    val C_SETTING_CC = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "com.tencent.wework.setting.api.ISetting\$-CC"
        MConfiger.WEWORK_VERSION_19717 -> "hng"
        MConfiger.WEWORK_VERSION_31145 -> "g2k"
        else -> ""
    }

    @JvmField
    val M_SETTING_CC_GET = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "get"
        MConfiger.WEWORK_VERSION_19717 -> "a"
        MConfiger.WEWORK_VERSION_31145 -> "a"
        else -> ""
    }


    @JvmField
    val M_SETTING_GETDBPATH = "getDBPath"

    @JvmField
    val M_SETTING_GETPROFILESETTING = "getProfileSetting"

    @JvmField
    val M_SETTING_GETPROFILESETTING_refreshCorpInfo = "refreshCorpInfo"

    @JvmField
    val C_CodedOutputByteBufferNano = "com.google.protobuf.nano.CodedOutputByteBufferNano"

    @JvmField
    val M_CodedOutputByteBufferNano_newInstance = "newInstance"

    @JvmField
    val F_CodedOutputByteBufferNano_buffer = "buffer"


    @JvmField
    val C_ICommonResultDataCallback =
        "com.tencent.wework.foundation.callback.ICommonResultDataCallback"



    @JvmField
    val I_IGetHistoryMessageCallback =
        "com.tencent.wework.foundation.callback.IGetHistoryMessageCallback"

    @JvmField
    val M_IGetHistoryMessageCallback_onResult = "onResult"

    @JvmField
    var I_IApplyVoiceResultCallback = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "lcu"
        MConfiger.WEWORK_VERSION_19717 -> "bte"
        MConfiger.WEWORK_VERSION_31145 -> "y5j"
        else -> ""
    }

    @JvmField
    var F_SendExtraInfo_ConversationID = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "mMQ"
        MConfiger.WEWORK_VERSION_19717 -> "y"
        MConfiger.WEWORK_VERSION_31145 -> "y"
        else -> ""
    }


    @JvmField
    var C_ConversationID = when (Global.getWecomVersion()) {
        MConfiger.WEWORK_VERSION_17857 -> "com.tencent.wework.msg.api.ConversationID"
        MConfiger.WEWORK_VERSION_19717 -> "com.tencent.wework.msg.api.ConversationID"
        MConfiger.WEWORK_VERSION_31145 -> "t79"
        else -> ""
    }

    @JvmField
    var M_User_getInfo = "getInfo"

    @JvmField
    var M_User_getWechatOpenId = "getWechatOpenId"

    @JvmField
    var M_User_getExtraWechatRemoteId = "getExtraWechatRemoteId"

    @JvmField
    var M_User_getExtraWechatHeadUrl = "getExtraWechatHeadUrl"

    @JvmField
    var F_User_getInfo_name = "name"
}