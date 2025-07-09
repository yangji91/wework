package com.robot.hook.util;

import android.view.View;

import com.robot.common.Global;
import com.robot.controller.LoginController;
import com.robot.entity.ConvEntity;
import com.robot.entity.ConvMember;
import com.robot.entity.UserEntity;
import com.robot.hook.KeyConst;
import com.robot.util.MyLog;
import com.robot.robothook.RobotHelpers;

import java.util.ArrayList;
import java.util.List;

public class ConvParseUtil {

    private static final String TAG = ConvParseUtil.class.getSimpleName();

    /**
     * public int convType;        //cuD()
     * public long convLocalId;    //getLocalId()
     * public long convRemoteId;   //getRemoteId()
     * public long convServiceId;  //cuE()
     * public String name;     //djz.j(getName(), 5);
     * public boolean whole;   //Boolean.valueOf(cuL());
     * public int flag;        //TextUtils.concat(new CharSequence[]{"0x", Integer.toHexString(this.jkT)});
     * public int corps;       //Integer.valueOf(this.jkU.size());
     * public int members;     //Integer.valueOf(getMemberCount());
     * public boolean exConv;
     * public boolean exMem;
     * public int exitType;
     * public boolean visible;
     * public int exMemCount;
     * public int unread;
     * public int modify;      //Long.valueOf(this.jkr);
     * public long create;     //Long.valueOf(this.mCreateTime);
     * public boolean top;     //Boolean.valueOf(this.jks);
     * public long messageLocalId;
     * public long messageRemoteId;
     * public long LatestID;   //fln.getMessageID(this.jkx);
     *
     * @return
     */
//    public static final BConvItemEntity parseConvItemEntity(Object obj) {
//        BConvItemEntity itemEntity = new BConvItemEntity();
//        itemEntity.convType = (int) RobotHelpers.callMethod(obj, "getConversationType");
//        itemEntity.convLocalId = (long) RobotHelpers.callMethod(obj, "getLocalId");
//        itemEntity.convRemoteId = (long) RobotHelpers.callMethod(obj, "getRemoteId");
////        itemEntity.convServiceId = (long) RobotHelpers.callMethod(obj,"cuE");
//        itemEntity.convServiceId = -1;
//        itemEntity.name = (String) RobotHelpers.callMethod(obj, "getName");
//        itemEntity.whole = (boolean) RobotHelpers.callMethod(obj, M_ConversationItem_getWhole);
//        itemEntity.flag = RobotHelpers.getIntField(obj, F_ConversationItem_flag);
//        itemEntity.members = (int) RobotHelpers.callMethod(obj, "getMemberCount");
//        return itemEntity;
//    }

    public static final ConvEntity.ConvExtras parseExtras(Object objExtras) {
        ConvEntity.ConvExtras convExtras = null;
        if (objExtras != null) {
            convExtras = new ConvEntity.ConvExtras();
            Object[] objArray = (Object[]) RobotHelpers.getObjectField(objExtras, KeyConst.F_Conversion_requestInfo_extras_admins);
            List<ConvEntity.ConvRoomAdmin> rList = null;
            if (objArray != null && objArray.length > 0) {
                rList = new ArrayList<>();
                for (Object obj : objArray) {
                    ConvEntity.ConvRoomAdmin convRoomAdmin = new ConvEntity.ConvRoomAdmin();
                    convRoomAdmin.vid = RobotHelpers.getLongField(obj, KeyConst.F_Conversion_requestInfo_extras_admins_vid);
                    rList.add(convRoomAdmin);
                }
            }
            convExtras.admins = rList;

            long tempNewFlag = RobotHelpers.getLongField(objExtras, KeyConst.F_Conversion_requestInfo_extras_newFlag);
            if (6 == tempNewFlag || 4 == tempNewFlag) {
                //允许改群名. 此处的6应该是一个组合标记, 后期再说吧
                convExtras.isAllowRename = true;
            } else if (7 == tempNewFlag) {
                convExtras.isAllowRename = false;
            } else {
                convExtras.isAllowRename = true;
                //  MyLog.error(TAG, new Exception("无法识别的new_flag=" + tempNewFlag));
            }
            View view;

        }
        return convExtras;
    }

    public static final ConvEntity parseConvEntity(final Object obj) {
        ConvEntity entity = null;
//        Class clazzConv = RobotHelpers.findClassIfExists(KeyConst.C_Conversation_NativeHandleHolder, Global.loadPackageParam.classLoader);
        if (obj != null) {
            Object info = RobotHelpers.callMethod(obj, KeyConst.M_Conversion_requestInfo);
            if (info != null) {
                boolean isExternalGroup = (boolean) RobotHelpers.callMethod(obj, KeyConst.M_Conversion_IsExternalGroup);
                boolean isVipConv = (boolean) RobotHelpers.callMethod(obj, KeyConst.M_Conversion_getIsVipConv);
//                boolean isOwnerManagerOnly = (boolean) RobotHelpers.callMethod(obj, "getIsOwnerManagerOnly");
//                boolean isAddMemberNeedConfirm = (boolean) RobotHelpers.callMethod(obj, "IsAddMemberNeedConfirm");
                boolean enableExteralAdmin = (boolean) RobotHelpers.callMethod(obj, KeyConst.M_Conversion_enableExteralAdmin);
                entity = new ConvEntity();
                entity.autoMarkRead = RobotHelpers.getBooleanField(info, KeyConst.F_Conversion_requestInfo_autoMarkRead);
                entity.createTime = RobotHelpers.getLongField(info, KeyConst.F_Conversion_requestInfo_createTime);
                entity.creatorId = RobotHelpers.getLongField(info, KeyConst.F_Conversion_requestInfo_creatorId);
                entity.exited = RobotHelpers.getBooleanField(info, KeyConst.F_Conversion_requestInfo_exited);
                entity.forwardLeaveMsg = (byte[]) RobotHelpers.getObjectField(info, KeyConst.F_Conversion_requestInfo_forwardLeaveMsg);
                entity.hidden = RobotHelpers.getBooleanField(info, KeyConst.F_Conversion_requestInfo_hidden);
                entity.id = RobotHelpers.getLongField(info, KeyConst.F_Conversion_requestInfo_id);
                entity.isStickied = RobotHelpers.getBooleanField(info, KeyConst.F_Conversion_requestInfo_isStickied);
                entity.modifyTime = RobotHelpers.getLongField(info, KeyConst.F_Conversion_requestInfo_modifyTime);
                entity.name = (String) RobotHelpers.getObjectField(info, KeyConst.F_Conversion_requestInfo_name);
                entity.notified = RobotHelpers.getBooleanField(info, KeyConst.F_Conversion_requestInfo_notified);
                entity.remoteId = RobotHelpers.getLongField(info, KeyConst.F_Conversion_requestInfo_remoteId);
                entity.searchTime = RobotHelpers.getLongField(info, KeyConst.F_Conversion_requestInfo_searchTime);
                entity.type = RobotHelpers.getIntField(info, KeyConst.F_Conversion_requestInfo_type);
                entity.updateMember = RobotHelpers.getBooleanField(info, KeyConst.F_Conversion_requestInfo_updateMember);
//            Object[] members = (Object[]) RobotHelpers.getObjectField(info,"memberList");
                entity.memberCount = (int) RobotHelpers.callMethod(obj, KeyConst.F_Conversion_requestInfo_getMemberCount);
                entity.isExternalGroup = isExternalGroup;
                Object objExtras = RobotHelpers.getObjectField(info, KeyConst.F_Conversion_requestInfo_extras);
                entity.extras = parseExtras(objExtras);
                long newFlag = RobotHelpers.getLongField(objExtras, KeyConst.F_Conversion_requestInfo_extras_newFlag);
                Object[] members = (Object[]) RobotHelpers.callMethod(obj, KeyConst.F_Conversion_requestInfo_getMembers);
                int size = entity.memberCount;
                enableExteralAdmin = entity.isAdmin();
                entity.enableExteralAdmin = enableExteralAdmin;
                if (isExternalGroup) {
                    MyLog.debug(TAG, "[会话]" + "  remoteId:" + entity.remoteId + " name:" + entity.name
                            + "  new_flag:" + newFlag + " type:" + entity.type + " entity.memberCount:" + entity.memberCount + " 是否是vip群:" + isVipConv + " 是否是管理员:" + enableExteralAdmin
                            + " 是否是群主:" + entity.isGroupOwner + " extra:" + entity.extras);
                } else {
                    MyLog.debug(TAG, "[会话]" + " remoteId:" + entity.remoteId + " name:" + entity.name);
                }
                if (LoginController.getInstance().getLoginUserId() == entity.creatorId) {
                    entity.isGroupOwner = true;
                }

                if (isExternalGroup) {
                    entity.memberList = new ConvMember[members.length];
                    if (members != null && members.length > 0) {
                        for (int i = 0; i < members.length; i++) {
                            Object m = members[i];
                            ConvMember cvMemberEntity = parseConvMemberEntity(m);
                            entity.memberList[i] = cvMemberEntity;
                        }
                    }
                }
            }
        }
        return entity;
    }


    public static final ConvMember[] convertByUserEntity(ConvMember[] convList, List<UserEntity> sList) {
        if (convList != null) {
            for (ConvMember convMember : convList) {
                long remoteId = convMember.userRemoteId;
                for (UserEntity userEntity : sList) {
                    if (userEntity.remoteId == remoteId) {
                        convMember.avatorUrl = userEntity.avatorUrl;
                        convMember.name = userEntity.name;
                        convMember.englishName = userEntity.englishName;
                        break;
                    }
                }
            }
        }
        return convList;
    }

    /**
     * 解析member
     *
     * @param obj
     * @return
     */
    public static ConvMember parseConvMemberEntity(Object obj) {
        ConvMember mEntity = null;
        if (obj != null) {
            mEntity = new ConvMember();
            mEntity.avatorUrl = (String) RobotHelpers.getObjectField(obj, KeyConst.F_ConvMember_avatorUrl);
            //  mEntity.banType = RobotHelpers.getIntField(obj,"banType");
            //mEntity.changeReceiptStatusTimestamp = RobotHelpers.getLongField(obj,"changeReceiptStatusTimestamp");
            mEntity.englishName = (String) RobotHelpers.getObjectField(obj, KeyConst.F_ConvMember_englishName);
            mEntity.invited = RobotHelpers.getBooleanField(obj, KeyConst.F_ConvMember_invited);
            mEntity.joinTime = RobotHelpers.getLongField(obj, KeyConst.F_ConvMember_joinTime) * 1000L;
            // mEntity.kfVid = RobotHelpers.getLongField(obj,"kfVid");
            mEntity.name = (String) RobotHelpers.getObjectField(obj, KeyConst.F_ConvMember_name);
            mEntity.nickName = (String) RobotHelpers.getObjectField(obj, KeyConst.F_ConvMember_nickName);
            //mEntity.operatorRemoteId = RobotHelpers.getLongField(obj,"operatorRemoteId");
            //mEntity.searchExtra = (byte[]) RobotHelpers.getObjectField(obj,"searchExtra");
            mEntity.userCorpId = RobotHelpers.getLongField(obj, KeyConst.F_ConvMember_userCorpId);
            mEntity.userRemoteId = RobotHelpers.getLongField(obj, KeyConst.F_ConvMember_userRemoteId);
        }
        return mEntity;
    }


    public static final Object getConvService() {
        Object convService = null;
        Class clazzApplication = RobotHelpers.findClassIfExists(KeyConst.C_Application, Global.loadPackageParam.classLoader);
        Object objApplication = RobotHelpers.callStaticMethod(clazzApplication, KeyConst.C_Application_getInstance);
        if (objApplication != null) {
            Object objProfileManager = RobotHelpers.callMethod(objApplication, KeyConst.M_Application_GetProfileManager);
            if (objProfileManager != null) {
                Object objCurrentProfile = RobotHelpers.callMethod(objProfileManager, KeyConst.M_Application_GetProfileManager_GetCurrentProfile);
                if (objCurrentProfile != null) {
                    Object objServiceManager = RobotHelpers.callMethod(objCurrentProfile, KeyConst.M_Application_GetProfileManager_GetCurrentProfile_getServiceManager);
                    if (objServiceManager != null) {
                        convService = RobotHelpers.callMethod(objServiceManager, KeyConst.M_Application_GetProfileManager_GetCurrentProfile_getServiceManager_GetConversationService);
                    }
                }
            }
        }
        return convService;
    }
    public static final Object getFileService() {
        Object fileService = null;
        Class clazzApplication = RobotHelpers.findClassIfExists(KeyConst.C_Application, Global.loadPackageParam.classLoader);
        Object objApplication = RobotHelpers.callStaticMethod(clazzApplication, KeyConst.C_Application_getInstance);
        if (objApplication != null) {
            Object objProfileManager = RobotHelpers.callMethod(objApplication, KeyConst.M_Application_GetProfileManager);
            if (objProfileManager != null) {
                Object objCurrentProfile = RobotHelpers.callMethod(objProfileManager, KeyConst.M_Application_GetProfileManager_GetCurrentProfile);
                if (objCurrentProfile != null) {
                    Object objServiceManager = RobotHelpers.callMethod(objCurrentProfile, KeyConst.M_Application_GetProfileManager_GetCurrentProfile_getServiceManager);
                    if (objServiceManager != null) {
                        fileService = RobotHelpers.callMethod(objServiceManager, KeyConst.M_Application_GetProfileManager_GetCurrentProfile_getServiceManager_getFileService);
                    }
                }
            }
        }
        return fileService;
    }

    public static long getID(Object conversation) {
        Object info = RobotHelpers.callMethod(conversation, KeyConst.M_SendExtraInfo_getInfo);
        return RobotHelpers.getLongField(info, KeyConst.F_SendExtraInfo_getInfo_id);
    }

    public static final Object getConvEnginInstance() {
        Object rObject;
        Class clazzConvEngin = RobotHelpers.findClassIfExists(KeyConst.C_ConversationEngine, Global.loadPackageParam.classLoader);
        rObject = RobotHelpers.callStaticMethod(clazzConvEngin, KeyConst.M_ConversationEngine_getInstance);
        return rObject;
    }
}
