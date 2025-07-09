package com.robot.entity

import java.io.Serializable

data class UserData(
    var addFriendSourceType: Int, // 0
    var applyContent: String?,
    var b2BCorpFriend: Boolean, // false
    var circleCorpFriend: Boolean, // false
    var circleCorpUserEngNameMode: Boolean, // false
    var colleagueRealRemark: String?, // 劉磊33
    var conversationApi: Boolean, // false
    var corpId: Long, // 1970325134026788
    var corpName: String?,
    var corpRemark: String?,
    var customerAddTime: Int, // 1694510178
    var defaultCheckedWhenActiveRecomm: Boolean, // false
    var description: String?,
    var displayName: String?, // 劉磊33
    var emailAddr: String?,
    var englishName: String?,
    var externalCustomerServer: Boolean, // false
    var extraAddFriendSourceInfo: ExtraAddFriendSourceInfo?,
    var extraAttr: Int, // 0
    var extraAttr2: Int, // 268435456
    var extraAttr3: Int, // 1
    var extraAttribute: Int, // 0
    var extraContactKey: String?,
    var extraNationCode: String?,
    var extraRecommendNickName: String?,
    var extraWechatAttribute: Int, // 0
    var extraWechatHeadUrl: String?,
    var extraWechatMobile: String?,
    var extraWechatName: String?,
    var extraWechatRealRemark: String?, // 劉磊33
    var extraWechatRemoteId: Int, // 0
    var filterUser: Boolean, // false
    var gender: Int, // 1
    var groupCorpFriend: Boolean, // false
    var groupRobot: Boolean, // false
    var hasRealName: Boolean, // false
    var headUrl: String?, // http://wx.qlogo.cn/mmhead/kajZiag34FVdABXI0q5RzAsDJLb6NMQIuVHyrOFakNCs/0
    var headUrlIgnoreRTX: String?, // http://wx.qlogo.cn/mmhead/kajZiag34FVdABXI0q5RzAsDJLb6NMQIuVHyrOFakNCs/0
    var holidayExtraInfo: HolidayExtraInfo?,
    var holidayInfo: HolidayInfo?,
    var homeSchoolParent: Boolean, // false
    var info: Info?,
    var innerCustomerServer: Boolean, // false
    var internationMobilePhone: String?,
    var job: String?,
    var mNativeHandle: Long, // 2743130128
    var mobilePhone: String?,
    var name: String?, // 劉磊
    var nameOrEngName: String?, // 劉磊
    var nativeCorpId: Long, // 1970325134026788
    var nativeHandle: Long, // 2743130128
    var nativeRemoteId: Long, // 7881302513908546
    var needShowRealName: Boolean, // false
    var newUserExternJob: String?,
    var nickAvailable: Boolean, // false
    var nickNameBlank: Boolean, // false
    var onlineCustomer: Boolean, // false
    var opvid: Int, // 0
    var otherContact: Boolean, // false
    var outFriend: Boolean, // true
    var personalWorkType: Int, // 0
    var phone: String?,
    var rTXAvatarUrl: String?, // http://wx.qlogo.cn/mmhead/kajZiag34FVdABXI0q5RzAsDJLb6NMQIuVHyrOFakNCs/0
    var rTXAvatarUrlOrEmpty: String?,
    var realName: String?,
    var realRemark: String?, // 劉磊33
    var recommendReason: Int, // 0
    var recommendSource: Int, // 0
    var remoteId: Long, // 7881302513908546
    var robotProfile: RobotProfile?,
    var searchFiledDesc: String?,
    var searchSrc: Int, // 0
    var searchedMatchedStr: String?,
    var searchedUserHideWxName: Boolean, // false
    var selfAttrInfo: SelfAttrInfo?,
    var tencentMember: Boolean, // false
    var underVerifyName: String?,
    var userActivated: Boolean, // false
    var userAppActivated: Boolean, // false
    var userAttr: Int, // 0
    var userCorpAddress: String?,
    var userDeptListSize: Int, // 0
    var userElectronicCardStyle: Int, // 0
    var userExternJob: String?,
    var userHolidayId: Int, // 0
    var userMobileFilterModeOn: Boolean, // false
    var userRealName: String?, // 劉磊
    var userResignation: Boolean, // false
    var userStatus: Int, // 1
    var userStatusDesc: String?,
    var userStatusIconIndex: Int, // 0
    var userWxFinder: UserWxFinder?,
    var verfiedUser: Boolean, // true
    var vipUser: Boolean, // false
    var virtualCustomerServer: Boolean, // false
    var wechatInfo: WechatInfo?,
    var wechatOpenId: String?,
    var wechatStranger: Boolean, // false
    var weixinXidUser: Boolean, // true
    var wxFriendFlag: Int, // 0
    var xidSearchUserSrc: Int, // 0
    var zhName: String? // 劉磊
) : Serializable {
    data class ExtraAddFriendSourceInfo(
        var applyMode: Int, // 2
        var cachedSize: Int, // 4
        var mobile: String?,
        var serializedSize: Int, // 4
        var sourceRoomid: Int, // 0
        var sourceType: Int, // 1
        var vid: Int, // 0
        var wxFriendName: String?
    ): Serializable

    data class HolidayExtraInfo(
        var bClickedByme: Boolean, // false
        var cachedSize: Int, // 0
        var clickGoodNum: Int, // 0
        var holidayListReadTime: Int, // 0
        var serializedSize: Int // 0
    ): Serializable

    data class HolidayInfo(
        var cachedSize: Int, // 0
        var createTime: Int, // 0
        var docShareInfo: List<Any?>?,
        var holidayDesc: String?,
        var holidayGenerateSrc: Int, // 0
        var holidayIconIndex: Int, // 0
        var holidayInfoId: Int, // 0
        var holidayStatus: Int, // 0
        var holidayStatusNew: Int, // 0
        var oldHolidayIconIndex: Int, // 0
        var serializedSize: Int // 0
    ): Serializable

    data class Info(
        var acctid: String?,
        var addContactDirectly: Boolean, // false
        var addContactRoomId: Int, // 0
        var alias: String?,
        var applyHasRead: Boolean, // true
        var attr: Int, // 0
        var avatorUrl: String?, // http://wx.qlogo.cn/mmhead/kajZiag34FVdABXI0q5RzAsDJLb6NMQIuVHyrOFakNCs/0
        var birthday: String?,
        var cachedSize: Int, // 624
        var cardSourceVid: Int, // 0
        var contactKey: String?,
        var corpid: Long, // 1970325134026788
        var createSeq: Int, // 0
        var createSource: Int, // 1
        var dispOrder: Int, // 0
        var emailAddr: String?,
        var englishName: String?,
        var extras: Extras?,
        var fromLimitSearch: Boolean, // false
        var fts: Int, // 0
        var fullPath: String?,
        var gender: Int, // 1
        var internationCode: String?,
        var isNameVerified: Boolean, // false
        var isRecommendWorkmateAdded: Boolean, // false
        var isTrim: Boolean, // false
        var isValid: Boolean, // true
        var job: String?,
        var level: Int, // 0
        var mobile: String?,
        var name: String?, // 劉磊
        var number: String?,
        var onlineStatus: Int, // 101
        var parentIds: List<Any?>?,
        var phone: String?,
        var phoneContactType: Int, // 0
        var recommendContactSource: Int, // 0
        var recommendNickName: String?,
        var remoteId: Long, // 7881302513908546
        var searchBidItem: Int, // 0
        var searchMatchOptions: Int, // 0
        var serializedSize: Int, // 624
        var tagBidName: String?,
        var ticket: String?,
        var unionid: String?, // ozynqsg0P1el0x-1sYBH-i0B0IzE
        var userDeptInfoList: List<Any?>?
    ): Serializable {
        data class Extras(
            var addFriendSource: AddFriendSource?,
            var applyMsgFlow: List<Any?>?,
            var applyUpdateTime: Int, // 0
            var attr2: Int, // 268435456
            var attr3: Int, // 1
            var attribute: Int, // 0
            var b2BJoinStatus: Int, // 0
            var bindEmailStatus: Int, // 2
            var bizMail: String?,
            var cachedSize: Int, // 480
            var circleLanguage: Int, // 0
            var companyRemark: String?,
            var contactGroupIds: List<Any?>?,
            var contactInfoWx: ContactInfoWx?,
            var contactKey: String?,
            var corpExternalName: String?,
            var customInfo: CustomInfo?,
            var customerAddTime: Int, // 1694510178
            var customerDescription: String?,
            var customerUpdateTime: Int, // 0
            var externFinder: ExternFinder?,
            var externJobTitle: String?,
            var externPosition: String?,
            var externalCustomInfo: ExternalCustomInfo?,
            var halfSelfAttr: List<Any?>?,
            var headID: Int, // 0
            var holidayExtraInfo: HolidayExtraInfo?,
            var holidayInfo: HolidayInfo?,
            var inviteVid: Int, // 0
            var isSyncInnerPosition: Boolean, // true
            var labelIds: List<LabelId?>?,
            var lastRemarkTime: Int, // 1695029970
            var nameVerifyStatus: Int, // 3
            var nationCode: String?,
            var newContactApplyContent: String?,
            var newContactTime: Int, // 0
            var openid: String?,
            var openkfprofile: Openkfprofile?,
            var personalWorkType: Int, // 0
            var profileCode: String?,
            var pstnExtensionNumber: String?,
            var pstnExtensionNumberNew: String?,
            var qymFindEmail: String?,
            var realName: String?,
            var realRemark: String?, // 劉磊33
            var recommendReason: Int, // 0
            var remarkPhone: List<Any?>?,
            var remarkUrl: String?,
            var remarks: String?,
            var robotProfile: RobotProfile?,
            var sceneLevel: Int, // 3
            var schoolAttr: List<Any?>?,
            var serializedSize: Int, // 480
            var superoirs: Superoirs?,
            var tencentInfo: TencentInfo?,
            var underVerifyName: String?,
            var vCorpNameFull: String?,
            var vCorpNameShort: String?,
            var vRecommendInfo: VRecommendInfo?,
            var vcode: String?,
            var virtualCreateMail: String?,
            var wcode: String?,
            var wxIdentytyOpenid: String?, // o9cq800VwpLdA97cocg4dRlNYL00
            var xcxCorpAddress: String?,
            var xcxStyle: Int // 0
        ): Serializable {
            data class AddFriendSource(
                var cachedSize: Int, // 0
                var opvid: Int, // 0
                var serializedSize: Int, // 0
                var type: Int // 0
            ): Serializable

            data class ContactInfoWx(
                var acctid: String?,
                var addContactDirectly: Boolean, // false
                var addContactRoomId: Int, // 0
                var alias: String?,
                var applyHasRead: Boolean, // true
                var attr: Int, // 0
                var avatorUrl: String?,
                var birthday: String?,
                var cachedSize: Int, // 195
                var cardSourceVid: Int, // 0
                var contactKey: String?,
                var corpid: Int, // 0
                var createSeq: Int, // 0
                var createSource: Int, // 0
                var dispOrder: Int, // 0
                var emailAddr: String?,
                var englishName: String?,
                var extras: Extras?,
                var fromLimitSearch: Boolean, // false
                var fts: Int, // 0
                var fullPath: String?,
                var gender: Int, // 0
                var internationCode: String?,
                var isNameVerified: Boolean, // true
                var isRecommendWorkmateAdded: Boolean, // false
                var isTrim: Boolean, // false
                var isValid: Boolean, // true
                var job: String?,
                var level: Int, // 0
                var mobile: String?,
                var name: String?,
                var number: String?,
                var onlineStatus: Int, // 101
                var parentIds: List<Any?>?,
                var phone: String?,
                var phoneContactType: Int, // 0
                var recommendContactSource: Int, // 0
                var recommendNickName: String?,
                var remoteId: Int, // 0
                var searchBidItem: Int, // 0
                var searchMatchOptions: Int, // 0
                var serializedSize: Int, // 195
                var tagBidName: String?,
                var ticket: String?,
                var unionid: String?,
                var userDeptInfoList: List<Any?>?
            ): Serializable {
                data class Extras(
                    var applyMsgFlow: List<Any?>?,
                    var applyUpdateTime: Int, // 0
                    var attr2: Int, // 0
                    var attr3: Int, // 0
                    var attribute: Int, // 0
                    var b2BJoinStatus: Int, // 0
                    var bindEmailStatus: Int, // 2
                    var bizMail: String?,
                    var cachedSize: Int, // 191
                    var circleLanguage: Int, // 0
                    var companyRemark: String?,
                    var contactGroupIds: List<Any?>?,
                    var contactKey: String?,
                    var corpExternalName: String?,
                    var customerAddTime: Int, // 0
                    var customerDescription: String?,
                    var customerUpdateTime: Int, // 0
                    var externJobTitle: String?,
                    var externPosition: String?,
                    var halfSelfAttr: List<Any?>?,
                    var headID: Int, // 0
                    var inviteVid: Int, // 0
                    var isSyncInnerPosition: Boolean, // true
                    var labelIds: List<LabelId?>?,
                    var lastRemarkTime: Int, // 0
                    var nameVerifyStatus: Int, // 3
                    var nationCode: String?,
                    var newContactApplyContent: String?,
                    var newContactTime: Int, // 0
                    var openid: String?,
                    var personalWorkType: Int, // 0
                    var profileCode: String?,
                    var pstnExtensionNumber: String?,
                    var pstnExtensionNumberNew: String?,
                    var qymFindEmail: String?,
                    var realName: String?,
                    var realRemark: String?, // 劉磊33
                    var recommendReason: Int, // 0
                    var remarkPhone: List<Any?>?,
                    var remarkUrl: String?,
                    var remarks: String?,
                    var sceneLevel: Int, // 0
                    var schoolAttr: List<Any?>?,
                    var serializedSize: Int, // 191
                    var underVerifyName: String?,
                    var vCorpNameFull: String?,
                    var vCorpNameShort: String?,
                    var vcode: String?,
                    var virtualCreateMail: String?,
                    var wcode: String?,
                    var wxIdentytyOpenid: String?,
                    var xcxCorpAddress: String?,
                    var xcxStyle: Int // 0
                ): Serializable {
                    data class LabelId(
                        var businessType: Int, // 0
                        var cachedSize: Int, // 27
                        var corpOrVid: Long, // 1970325132232836
                        var groupId: Long, // 14073749462633018
                        var labelId: Long, // 14073749462633020
                        var serializedSize: Int, // 27
                        var serviceGroupid: Int // 0
                    ): Serializable
                }
            }

            data class CustomInfo(
                var attrs: List<Any?>?,
                var cachedSize: Int, // 0
                var serializedSize: Int // 0
            ): Serializable

            data class ExternFinder(
                var addtime: Int, // 0
                var cachedSize: Int, // 0
                var finderId: String?,
                var finderIntid: Int, // 0
                var image: String?,
                var invisible: Boolean, // false
                var nickName: String?,
                var serializedSize: Int, // 0
                var status: Int // 0
            ): Serializable

            data class ExternalCustomInfo(
                var attrs: List<Any?>?,
                var cachedSize: Int, // 0
                var serializedSize: Int // 0
            ): Serializable

            data class HolidayExtraInfo(
                var `$ref`: String? // $.holidayExtraInfo
            ): Serializable

            data class HolidayInfo(
                var `$ref`: String? // $.holidayInfo
            ): Serializable

            data class LabelId(
                var businessType: Int, // 0
                var cachedSize: Int, // 27
                var corpOrVid: Long, // 1970325132232836
                var groupId: Long, // 14073749462633018
                var labelId: Long, // 14073749462633020
                var serializedSize: Int, // 27
                var serviceGroupid: Int // 0
            ): Serializable

            data class Openkfprofile(
                var cachedSize: Int, // 0
                var name: String?,
                var serializedSize: Int, // 0
                var type: Int // 0
            ): Serializable

            data class RobotProfile(
                var bCallbackMode: Boolean, // false
                var bCanShared: Boolean, // false
                var bClose: Boolean, // false
                var bFavor: Boolean, // false
                var bSysGlobal: Boolean, // false
                var cachedSize: Int, // 0
                var corpid: Int, // 0
                var createtime: Int, // 0
                var creatorVid: Int, // 0
                var description: String?,
                var robotEnglishName: String?,
                var robotHeadUrl: String?,
                var robotId: Int, // 0
                var robotMsgUrl: String?,
                var robotName: String?,
                var robotType: Int, // 0
                var roomid: Int, // 0
                var serializedSize: Int // 0
            ): Serializable

            data class Superoirs(
                var cachedSize: Int, // 0
                var serializedSize: Int, // 0
                var vids: List<Any?>?
            ): Serializable

            data class TencentInfo(
                var cachedSize: Int, // 0
                var serializedSize: Int, // 0
                var workCardImage: String?
            ): Serializable

            data class VRecommendInfo(
                var cachedSize: Int, // 0
                var friendVids: List<Any?>?,
                var moreThanTwoFriend: Boolean, // false
                var serializedSize: Int, // 0
                var type: Int // 0
            ): Serializable
        }
    }

    data class RobotProfile(
        var `$ref`: String? // $.info.extras.robotProfile
    ): Serializable

    data class SelfAttrInfo(
        var `$ref`: String? // $.info.extras.externalCustomInfo
    ): Serializable

    data class UserWxFinder(
        var `$ref`: String? // $.info.extras.externFinder
    ): Serializable

    data class WechatInfo(
        var `$ref`: String? // $.info.extras.contactInfoWx
    ): Serializable
}