package com.robot.entity;

import java.io.Serializable;

public class UserEntity implements Serializable{
    public static final int PCT_BothFriend = 2;
    public static final int PCT_NoneFriend = 3;
    public static final int PCT_OneWayFriend = 1;
    public static final int PCT_Unknown = 0;
    public static final int RCS_Card = 4;
    public static final int RCS_Phone = 1;
    public static final int RCS_Unknown = 0;
    public static final int RCS_WX = 2;
    public static final int RCS_WorkMate = 3;
    public static final int SMT_Name = 1;
    public static final int SMT_NickName = 2;
    public static final int SMT_RealName = 4;
    public static final int SMT_Unknown = 0;

    public long id;
    public String wechatOpenId;
    public long wechatRemoteId;
    public String wechatIcon;
    public String acctid;
    public boolean addContactDirectly;
    public long addContactRoomId;
    public String alias;
    public boolean applyHasRead;
    public long attr;
    public String avatorUrl;
    public String birthday;
    public long cardSourceVid;
    public String contactKey;
    public long corpid;
    public long createSeq;
    public int createSource;
    public int dispOrder;
    public String emailAddr;
    public String englishName;
    public UserExtras extras;
    public String fullPath;
    public int gender;
    public String internationCode;
    public boolean isNameVerified;
    public boolean isRecommendWorkmateAdded;
    public boolean isTrim;
    public boolean isValid;
    public String job;
    public int level;
    public String mobile;
    public String name;
    public String number;
    public int onlineStatus;
    public long[] parentIds;
    public String phone;
    public int phoneContactType;
    public int recommendContactSource;
    public String recommendNickName;
    public long remoteId;
    public long searchBidItem;
    public long searchMatchOptions;
    public String tagBidName;
    public byte[] ticket;
    public String unionid;
    public String realRemark;
    public long customerAddTime;  //添加时间
    public long customerUpdateTime;
    public String customerDescription;

    //extras
    public String newContactApplyContent;
    public Object userObj;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("wechatOpenId:" + wechatOpenId + " wechatRemoteId:" + wechatRemoteId + " wechatIcon:" + wechatIcon + " ");
        builder.append("accid:" + acctid + " alias:" + alias + " attr:" + attr + " avator:" + avatorUrl + " enName:" + englishName + " job:" + job + " level:" + level
        + " mobile:" + mobile + " name:" + name + " number:" + number + " phone:" + phone + " remoteId:" + remoteId + " unionId:" + unionid);
        return builder.toString();
    }

    public static class UserExtras{
        public long applyUpdateTime;
        public long attr2;
        public int attribute;
        public int bindEmailStatus;
        public int circleLanguage;
        public byte[] companyRemark;
        public long[] contactGroupIds;
        public UserEntity contactInfoWx;
        public String contactKey;
        public byte[] corpExternalName;
//        public CustomAttrInfo customInfo;
        public int customerAddTime;
        public String customerDescription;
        public long customerUpdateTime;
        public byte[] externJobTitle;
        public byte[] externPosition;
//        public SelfAttrInfo externalCustomInfo;
//        public HalfSelfAttr[] halfSelfAttr;
        public long headID;
//        public UserHolidayExtraInfo holidayExtraInfo;
//        public HolidayInfo holidayInfo;
        public long inviteVid;
        public boolean isSyncInnerPosition;
//        public ContactCustomerLabelId[] labelIds;
        public int lastRemarkTime;
        public int nameVerifyStatus;
        public String newContactApplyContent;
        public long newContactTime;
        public byte[] openid;
        public byte[] profileCode;
        public byte[] pstnExtensionNumber;
        public byte[] pstnExtensionNumberNew;
        public String realName;
        public byte[] realRemark;
        public int recommendReason;
//        public RemarkPhoneInfo[] remarkPhone;
        public byte[] remarkUrl;
        public String remarks;
//        public RobotProfile robotProfile;
//        public TencentInfo tencentInfo;
        public String underVerifyName;
//        public UserHolidayInfo userHolidayInfo;
        public String vCorpNameFull;
        public String vCorpNameShort;
//        public com.tencent.wework.foundation.model.pb.Common.VirtualRecommendInfo vRecommendInfo;
        public byte[] vcode;
        public String virtualCreateMail;
        public byte[] wcode;
        public byte[] wxIdentytyOpenid;
        public byte[] xcxCorpAddress;
        public int xcxStyle;
    }
}
