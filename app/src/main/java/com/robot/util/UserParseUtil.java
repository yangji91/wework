package com.robot.util;

import android.text.TextUtils;

import com.robot.common.Global;
import com.robot.entity.UserEntity;
import com.robot.hook.KeyConst;

import com.robot.robothook.RobotHelpers;

public class UserParseUtil {
    public static final String TAG = UserParseUtil.class.getSimpleName();

    /**
     * public String wechatOpenId;
     * public String wechatRemoteId;
     * public String wechatIcon;
     *
     * @param obj
     * @return
     */
    public static final UserEntity parseUserModel(Object obj) {
        UserEntity userEntity = null;
        userEntity = new UserEntity();
        userEntity.wechatOpenId = (String) RobotHelpers.callMethod(obj, KeyConst.M_User_getWechatOpenId);
        userEntity.wechatRemoteId = (long) RobotHelpers.callMethod(obj, KeyConst.M_User_getExtraWechatRemoteId);
        userEntity.wechatIcon = (String) RobotHelpers.callMethod(obj, KeyConst.M_User_getExtraWechatHeadUrl);
        Object userObj = RobotHelpers.callMethod(obj, KeyConst.M_User_getInfo);
//        MyLog.debug(TAG,"[parseUserModel]" + " obj:" + obj);
        if (userObj != null) {
//            MyLog.debug(TAG,"[parseUserModel]" + " userObj:" + userObj);
            userEntity.acctid = (String) RobotHelpers.getObjectField(userObj, "acctid");
            userEntity.addContactDirectly = RobotHelpers.getBooleanField(userObj, "addContactDirectly");
            userEntity.addContactRoomId = RobotHelpers.getLongField(userObj, "addContactRoomId");
            userEntity.alias = (String) RobotHelpers.getObjectField(userObj, "alias");
            userEntity.applyHasRead = RobotHelpers.getBooleanField(userObj, "applyHasRead");
            userEntity.attr = RobotHelpers.getLongField(userObj, "attr");
            userEntity.avatorUrl = (String) RobotHelpers.getObjectField(userObj, "avatorUrl");
            //userEntity.birthday = (String) RobotHelpers.getObjectField(userObj,"birthday");
            userEntity.emailAddr = (String) RobotHelpers.getObjectField(userObj, "emailAddr");
            userEntity.englishName = (String) RobotHelpers.getObjectField(userObj, "englishName");
            userEntity.job = (String) RobotHelpers.getObjectField(userObj, "job");
            userEntity.level = RobotHelpers.getIntField(userObj, "level");
            userEntity.mobile = (String) RobotHelpers.getObjectField(userObj, "mobile");
            userEntity.name = (String) RobotHelpers.getObjectField(userObj, "name");
            userEntity.number = (String) RobotHelpers.getObjectField(userObj, "number");
            userEntity.phone = (String) RobotHelpers.getObjectField(userObj, "phone");
            userEntity.remoteId = RobotHelpers.getLongField(userObj, "remoteId");
            userEntity.unionid = (String) RobotHelpers.getObjectField(userObj, "unionid");
            userEntity.corpid = RobotHelpers.getLongField(userObj, "corpid");
            userEntity.onlineStatus = RobotHelpers.getIntField(userObj, "onlineStatus");
            userEntity.gender = RobotHelpers.getIntField(userObj, "gender");
            Object objExtr = RobotHelpers.getObjectField(userObj, "extras");
            if (obj != null) {
                long customerAddTime = RobotHelpers.getIntField(objExtr, "customerAddTime");
                long customerUpdateTime = RobotHelpers.getLongField(objExtr, "customerUpdateTime");
                String customerDescription = (String) RobotHelpers.getObjectField(objExtr, "customerDescription");
                userEntity.customerAddTime = customerAddTime;
                userEntity.customerUpdateTime = customerUpdateTime;
                userEntity.customerDescription = customerDescription;
                Object objUser = RobotHelpers.getObjectField(objExtr, "contactInfoWx");
                Object[] objRemarkPhones = (Object[]) RobotHelpers.getObjectField(objExtr, "remarkPhone");
                if (TextUtils.isEmpty(userEntity.phone)) {
                    if (objRemarkPhones != null && objRemarkPhones.length > 0) {
                        Object objRemarkPhone = objRemarkPhones[0];
                        byte[] phone = (byte[]) RobotHelpers.getObjectField(objRemarkPhone, "phone");
                        if (phone != null && phone.length > 0) {
                            String strPhone = new String(phone);
                            userEntity.phone = strPhone;
                        }
                    }
                }
                byte[] remarkBytes = (byte[]) RobotHelpers.getObjectField(objExtr, "realRemark");
                if (remarkBytes != null && remarkBytes.length > 0) {
                    String strRemark = StrUtils.byteToUTFStr(remarkBytes);
                    userEntity.realRemark = strRemark;
                }
                String accId = "";
                long remoteId = 0;
                if (objUser != null) {
                    accId = (String) RobotHelpers.getObjectField(objUser, "acctid");
                    remoteId = RobotHelpers.getLongField(objUser, "remoteId");
                }
//                MyLog.debug(TAG,"[parseUserModel]" + " objUser:" + objUser + " accId:" + accId + " remoteId:" + remoteId + " objExtr:" + objExtr);
            }
        }
        return userEntity;
    }


    public static final UserEntity.UserExtras parseUserExtra(Object obj) {
        UserEntity.UserExtras extra = null;
        if (obj != null) {
            extra = new UserEntity.UserExtras();
        }
        return extra;
    }


    public static final Object getUserObj(long remoteId) {
        Object rUserEntity;
        Class clazzUser = RobotHelpers.findClassIfExists(KeyConst.C_User, Global.loadPackageParam.classLoader);
        Class[] clazzArray = {long.class};
        Object[] objArray = {remoteId};
        rUserEntity = RobotHelpers.callStaticMethod(clazzUser, KeyConst.M_GetTemp, clazzArray, objArray);
        return rUserEntity;
    }


//    public static final void fillUserInfo(Object objUserInfo,long remoteId){
//        Object objUser = RobotHelpers.getObjectField(objUserInfo,"mUser");
//        if(objUser != null){
//            Object objInfo = RobotHelpers.callMethod(objUser,"getInfo");
//            RobotHelpers.setLongField(objInfo,"addContactRoomId",remoteId);
//            //设置info
//            Class clazzWwUser = RobotHelpers.findClassIfExists(KeyConst.C_WwUser_USER,Global.loadPackageParam.classLoader);
//            Class[] clazzArray = {clazzWwUser};
//            Object[] objArray = {objInfo};
//            RobotHelpers.callMethod(objUser,"setInfo",clazzArray,objArray);
//        }
//    }

    public static final Object getUserByUserInfo(Object objUserInfo) {
        return RobotHelpers.getObjectField(objUserInfo, "mUser");
    }
}
