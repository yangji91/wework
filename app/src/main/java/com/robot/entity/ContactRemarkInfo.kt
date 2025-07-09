package com.robot.entity

import java.io.Serializable

data class ContactRemarkInfo(
    var cachedSize: Int, // 184
    var companyRemark: String?,
    var labelIds: List<LabelId?>?,
    var noLabelIds: Boolean, // false
    var noRemarkPhone: Boolean, // false
    var realRemark: String?, // 劉磊32
    var remarkPhone: List<Any?>?,
    var remarkUrl: String?,
    var remarks: String?,
    var serializedSize: Int // 184
) : Serializable {
    data class LabelId(
        var businessType: Int, // 0
        var cachedSize: Int, // 27
        var corpOrVid: Long, // 1970325132232836
        var groupId: Long, // 14073749462633018
        var labelId: Long, // 14073749462633020
        var serializedSize: Int, // 27
        var serviceGroupid: Int // 0
    ) : Serializable
}