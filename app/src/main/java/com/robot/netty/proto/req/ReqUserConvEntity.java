package com.robot.netty.proto.req;

import com.alibaba.fastjson.JSON;
import com.robot.common.MConfiger;
import com.robot.entity.ConvEntity;
import com.robot.entity.ConvMember;
import com.robot.entity.UserEntity;
import com.robot.util.MyLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 上报联系人信息
 */
public class ReqUserConvEntity implements Serializable {
    public List<PContractEntity> contactInfos;  //用户信息
    public long robotUin;
    public List<PConvEntity> conversionInfos;

    public static class PConvEntity implements  Serializable{
        public long conversionId;
        public boolean autoMarkRead;
        public long convCreateTime;//createTime convCreateTime
        public long creatorId;
        public boolean exited;
        public boolean hidden;
        public boolean isStickied;
        public ConvMember[] memberList;
        public int memberCount;
        public long modifyTime;
        public String name;
        public boolean notified;
        public long remoteId;
        public long searchTime;
        public int type;
        public boolean updateMember;
        public int isUpdate;
        public boolean isExternalGroup;     //是否是外部群
        public boolean enableExteralAdmin;//是否是管理员
        public boolean isGroupOwner;    //是否是群主

        @Override
        public String toString() {
            return "PConvEntity{" +
                    "conversionId=" + conversionId +
                    ", autoMarkRead=" + autoMarkRead +
                    ", convCreateTime=" + convCreateTime +
                    ", creatorId=" + creatorId +
                    ", exited=" + exited +
                    ", hidden=" + hidden +
                    ", isStickied=" + isStickied +
                    ", memberList=" + Arrays.toString(memberList) +
                    ", memberCount=" + memberCount +
                    ", modifyTime=" + modifyTime +
                    ", name='" + name + '\'' +
                    ", notified=" + notified +
                    ", remoteId=" + remoteId +
                    ", searchTime=" + searchTime +
                    ", type=" + type +
                    ", updateMember=" + updateMember +
                    ", isUpdate=" + isUpdate +
                    ", isExternalGroup=" + isExternalGroup +
                    ", enableExteralAdmin=" + enableExteralAdmin +
                    ", isGroupOwner=" + isGroupOwner +
                    '}';
        }
    }

    public static class PContractEntity implements Serializable{
        public long remoteId;
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
        public UserEntity.UserExtras extras;
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
        public long searchBidItem;
        public long searchMatchOptions;
        public String tagBidName;
        public byte[] ticket;
        public String unionid;
        public String realRemark;
        public int isInBlackList;
        public int isUpdate;
        public Long addTime;

        @Override
        public String toString() {
            return "PContractEntity{" +
                    "remoteId=" + remoteId +
                    ", id=" + id +
                    ", wechatOpenId='" + wechatOpenId + '\'' +
                    ", wechatRemoteId=" + wechatRemoteId +
                    ", wechatIcon='" + wechatIcon + '\'' +
                    ", acctid='" + acctid + '\'' +
                    ", addContactDirectly=" + addContactDirectly +
                    ", addContactRoomId=" + addContactRoomId +
                    ", alias='" + alias + '\'' +
                    ", applyHasRead=" + applyHasRead +
                    ", attr=" + attr +
                    ", avatorUrl='" + avatorUrl + '\'' +
                    ", birthday='" + birthday + '\'' +
                    ", cardSourceVid=" + cardSourceVid +
                    ", contactKey='" + contactKey + '\'' +
                    ", corpid=" + corpid +
                    ", createSeq=" + createSeq +
                    ", createSource=" + createSource +
                    ", dispOrder=" + dispOrder +
                    ", emailAddr='" + emailAddr + '\'' +
                    ", englishName='" + englishName + '\'' +
                    ", extras=" + extras +
                    ", fullPath='" + fullPath + '\'' +
                    ", gender=" + gender +
                    ", internationCode='" + internationCode + '\'' +
                    ", isNameVerified=" + isNameVerified +
                    ", isRecommendWorkmateAdded=" + isRecommendWorkmateAdded +
                    ", isTrim=" + isTrim +
                    ", isValid=" + isValid +
                    ", job='" + job + '\'' +
                    ", level=" + level +
                    ", mobile='" + mobile + '\'' +
                    ", name='" + name + '\'' +
                    ", number='" + number + '\'' +
                    ", onlineStatus=" + onlineStatus +
                    ", parentIds=" + Arrays.toString(parentIds) +
                    ", phone='" + phone + '\'' +
                    ", phoneContactType=" + phoneContactType +
                    ", recommendContactSource=" + recommendContactSource +
                    ", recommendNickName='" + recommendNickName + '\'' +
                    ", searchBidItem=" + searchBidItem +
                    ", searchMatchOptions=" + searchMatchOptions +
                    ", tagBidName='" + tagBidName + '\'' +
                    ", ticket=" + Arrays.toString(ticket) +
                    ", unionid='" + unionid + '\'' +
                    ", realRemark='" + realRemark + '\'' +
                    ", isInBlackList='" + isInBlackList + '\'' +
                    ", isUpdate=" + isUpdate +
                    ", addTime=" + addTime +
                    '}';
        }
    }

    /**
     *   public boolean autoMarkRead;
     *         public long createTime;
     *         public long creatorId;
     *         public boolean exited;
     *         public boolean hidden;
     *         public boolean isStickied;
     *         public ConvMember[] memberList;
     *         public int memberCount;
     *         public long modifyTime;
     *         public String name;
     *         public boolean notified;
     *         public long remoteId;
     *         public long searchTime;
     *         public int type;
     *         public boolean updateMember;
     * @param convEntity
     * @return
     */
    public static final PConvEntity convertByConvEntity(ConvEntity convEntity){
        PConvEntity rEntity = new PConvEntity();
        rEntity.conversionId = convEntity.id;
        rEntity.remoteId = convEntity.remoteId;
        rEntity.autoMarkRead = convEntity.autoMarkRead;
        rEntity.exited = convEntity.exited;
        rEntity.hidden = convEntity.hidden;
        rEntity.isStickied = convEntity.isStickied;
        rEntity.memberList = convEntity.memberList;
        rEntity.memberCount = convEntity.memberCount;
        rEntity.modifyTime = convEntity.modifyTime;
        rEntity.name = convEntity.name;
        rEntity.notified = convEntity.notified;
        rEntity.searchTime = convEntity.searchTime;
        rEntity.type = convEntity.type;
        rEntity.updateMember = convEntity.updateMember;
        rEntity.convCreateTime = convEntity.createTime * 1000;
        rEntity.creatorId = convEntity.creatorId;
        rEntity.isExternalGroup = convEntity.isExternalGroup;
        rEntity.enableExteralAdmin = convEntity.enableExteralAdmin;
        rEntity.isGroupOwner = convEntity.isGroupOwner;
        return rEntity;
    }

    private static final PContractEntity convertByUEntity(UserEntity userEntity){
        PContractEntity rEntity = new PContractEntity();
        rEntity.remoteId = userEntity.remoteId;
        rEntity.id = userEntity.id;
        rEntity.wechatOpenId = userEntity.wechatOpenId;
        rEntity.wechatRemoteId = userEntity.wechatRemoteId;
        rEntity.wechatIcon = userEntity.wechatIcon;
        rEntity.acctid = userEntity.acctid;
        rEntity.addContactDirectly = userEntity.addContactDirectly;
        rEntity.addContactRoomId = userEntity.addContactRoomId;
        rEntity.alias = userEntity.alias;
        rEntity.applyHasRead = userEntity.applyHasRead;
        rEntity.attr = userEntity.attr;
        rEntity.avatorUrl = userEntity.avatorUrl;
        rEntity.birthday = userEntity.birthday;
        rEntity.cardSourceVid = userEntity.cardSourceVid;
        rEntity.contactKey = userEntity.contactKey;
        rEntity.corpid = userEntity.corpid;
        rEntity.createSeq = userEntity.createSeq;
        rEntity.createSource = userEntity.createSource;
        rEntity.dispOrder = userEntity.dispOrder;
        rEntity.emailAddr = userEntity.emailAddr;
        rEntity.englishName = userEntity.englishName;
        rEntity.mobile = userEntity.mobile;
        rEntity.name = userEntity.name;
        rEntity.number = userEntity.number;
        rEntity.phone = userEntity.phone;
        rEntity.unionid = userEntity.unionid;
        rEntity.onlineStatus = userEntity.onlineStatus;
        rEntity.job = userEntity.job;
        rEntity.level = userEntity.level;
        rEntity.ticket = userEntity.ticket;
        rEntity.gender = userEntity.gender;
        rEntity.extras = new UserEntity.UserExtras();
        rEntity.realRemark = userEntity.realRemark;
        rEntity.addTime = userEntity.customerAddTime * 1000;
        return rEntity;
    }

    public static final List<ReqUserConvEntity> convertByUList(List<UserEntity> mList){
        List<ReqUserConvEntity> list =new ArrayList<>();
        List<PContractEntity> rList = new ArrayList<>();
        for (UserEntity item:mList) {
            rList.add(convertByUEntity(item));
        }
        List<List> datas =checkPageSize(rList);
        for (List item :datas ){
            ReqUserConvEntity reqEntity = new ReqUserConvEntity();
            reqEntity.contactInfos=item;
            list.add(reqEntity);
        }

        return list;
    }

    public static final  List<ReqUserConvEntity> convertByConvList(List<ConvEntity> mList){
        List<ReqUserConvEntity> list =new ArrayList<>();

        List<PConvEntity> rList = new ArrayList<>();
        for (ConvEntity item:mList) {
            rList.add(convertByConvEntity(item));
        }
        List<List> datas =checkPageSize(rList);
        for (List item :datas ){
            ReqUserConvEntity reqEntity = new ReqUserConvEntity();
            reqEntity.conversionInfos=item;
            list.add(reqEntity);
        }


        return list;
    }

    /**
     * metadata 数据分包
     * @param rList
     * @return
     */
    private static List<List> checkPageSize(List rList) {
        List<List> datas=new ArrayList<>();
            int length=0;
        ArrayList all =new ArrayList();
            for(Object pConvEntity : rList){
                String jsonString = JSON.toJSONString(pConvEntity);
                int pageLength= jsonString.length();
                if (length>MConfiger.METADATA_DATA_ERROR_SIZE){//单个包 超过报警
                    MyLog.debug("Netty","单个包信息 超过报警限制 "+jsonString.substring(0,200) ,true);

                }else  if (pageLength>MConfiger.METADATA_DATA_MAX_SIZE){//单个包超过 300k
                    ArrayList item =new ArrayList();
                    item.add(pConvEntity);
                    datas.add(item);
                    MyLog.debug("Netty","单个包信息 超过300K限制 "+jsonString.substring(0,200) ,true);
                }else {
                    all.add(pConvEntity);
                    length+=pageLength;
                    if (length>MConfiger.METADATA_DATA_MAX_SIZE){//超过300 新建一组
                        datas.add(all);
                        all =new ArrayList<>();
                        length=0;
                    }
                }
            }
            datas.add(all);//剩余的 加到最后
            return datas;

    }

    public static final ReqUserConvEntity convertByConv( ConvEntity item){
        ReqUserConvEntity reqEntity = new ReqUserConvEntity();
        List<PConvEntity> rList = new ArrayList<>();
        PConvEntity pConvEntity = convertByConvEntity(item);
          rList.add(pConvEntity);
        reqEntity.conversionInfos = rList;
        return reqEntity;
    }

    @Override
    public String toString() {
        return "ReqUserConvEntity{" +
                "contactInfos=" + contactInfos +
                ", robotUin=" + robotUin +
                ", conversionInfos=" + conversionInfos +
                '}';
    }
}
