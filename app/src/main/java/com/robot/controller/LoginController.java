package com.robot.controller;

import android.text.TextUtils;

import com.robot.common.Global;
import com.robot.controller.message.MessageController;
import com.robot.entity.ConvEntity;
import com.robot.entity.EnterpriseEntity;
import com.robot.entity.UserEntity;
import com.robot.hook.KeyConst;
import com.robot.hook.util.ConvParseUtil;
import com.robot.util.MyLog;
import com.robot.util.ProxyUtil;
import com.robot.util.UserParseUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import com.robot.robothook.RobotHelpers;

public class LoginController {
    private final String TAG = getClass().getSimpleName();
    private static LoginController instance;
    private UserEntity mUserEntity;
    private Set<ILoginListener> mSet;

    private LoginController() {
        mSet = new HashSet<>();
    }

    public static LoginController getInstance() {
        if (instance == null) {
            instance = new LoginController();
        }
        return instance;
    }

    public void registerListener(ILoginListener mListener) {
        mSet.add(mListener);
    }

    public void unRegisterListener(ILoginListener mListener) {
        mSet.remove(mListener);
    }


    public void setLoginUserInfo(UserEntity userEntity) {
        this.mUserEntity = userEntity;
    }

    public UserEntity getLoginUserEntity() {
        return this.mUserEntity;
    }

    public String getLoginMobile() {
        if (this.mUserEntity != null && !TextUtils.isEmpty(this.mUserEntity.mobile)) {
            return this.mUserEntity.mobile;
        }
        return null;
    }

    public String getLoginUserName() {
        if (this.mUserEntity != null && !TextUtils.isEmpty(this.mUserEntity.name)) {
            return this.mUserEntity.name;
        }
        return null;
    }

    public long getLoginUserId() {
        if (this.mUserEntity != null) {
            return this.mUserEntity.remoteId;
        }
        return 0;
    }

    public void logout(final String tips) {
        for (ILoginListener mListener : mSet) {
            if (mListener != null) {
                mListener.onLogout(tips);
            }
        }
    }

    public boolean isLogin() {
        return mUserEntity != null && !TextUtils.isEmpty(this.mUserEntity.mobile);
    }

    public ConvEntity getLoginConv() {
        ConvEntity rEntity = null;
        if (mUserEntity != null) {
            long remoteId = mUserEntity.remoteId;
            Object cItem = ConvController.getInstance().getByRemoteId(remoteId);
            if (cItem != null) {
                rEntity = ConvParseUtil.parseConvEntity(cItem);
            }
        }
        return rEntity;
    }

    public void sendRsp2Self(String msg) {
        ConvEntity convEntity = getLoginConv();
        if (convEntity != null)
            MessageController.postTextMessage(Global.loadPackageParam.classLoader, convEntity.id, msg, null);

    }

    public void sendRsp2Self(File file) {
        ConvEntity convEntity = getLoginConv();
        if (convEntity != null) {
            MessageController.postFileMessage(Global.loadPackageParam.classLoader, convEntity.id, file.getAbsolutePath());
        }
    }

    public void sendFileHelper(String msg) {
        Object obj = ConvController.getInstance().getByRemoteId(10006l);
        if (obj != null) {
            ConvEntity convEntity = ConvParseUtil.parseConvEntity(obj);
            MessageController.postTextMessage(Global.loadPackageParam.classLoader, convEntity.id, msg, null);
        }
    }

    /**
     * 获取登录账号的信息
     *
     * @return
     */
    public UserEntity getLoginUser() {
        Class clazzCC = RobotHelpers.findClassIfExists(KeyConst.C_IAccount_CC, Global.loadPackageParam.classLoader);
        Object objImple = RobotHelpers.callStaticMethod(clazzCC, KeyConst.C_IAccount_CC_GET);
        Object objUser = RobotHelpers.callMethod(objImple, KeyConst.M_IACCOUNT_GETLOGINUSER);
        long userId = (long) RobotHelpers.callMethod(objImple, KeyConst.M_IACCOUNT_GETLOGINUSERID);
        UserEntity userEntity = null;
        if (objUser != null) {
            MyLog.debug(TAG, "[登录 账号的企业信息 ]" + " userObj=>" + objUser);
            userEntity = UserParseUtil.parseUserModel(objUser);
            userEntity.id = userId;
        }
        return userEntity;
    }

    /**
     * 获取 登录账号的企业信息
     *
     * @return EnterpriseEntity
     */
    public EnterpriseEntity getEnterpriseInfo() {
        Class<?> clazz = RobotHelpers.findClassIfExists(KeyConst.C_Application, Global.loadPackageParam.classLoader);
        Object objIntance = RobotHelpers.callStaticMethod(clazz, KeyConst.C_Application_getInstance);
        Object objProfileManager = RobotHelpers.callMethod(objIntance, KeyConst.M_Application_GetProfileManager);
        long corpId = (long) RobotHelpers.callMethod(objIntance, KeyConst.M_Application_getCorpId);
        long vId = (long) RobotHelpers.callMethod(objIntance, KeyConst.M_Application_getVid);
        Object objCurrentProfile = RobotHelpers.callMethod(objProfileManager, KeyConst.M_Application_GetProfileManager_GetCurrentProfile);
        Object objCorpCfg = RobotHelpers.callMethod(objCurrentProfile, KeyConst.M_Application_GetProfileManager_getCorpInfo);
        long bbsId = RobotHelpers.getLongField(objCorpCfg, KeyConst.F_Application_GetProfileManager_bbsId);
        byte[] bbsName = (byte[]) RobotHelpers.getObjectField(objCorpCfg, KeyConst.F_Application_GetProfileManager_bbsName);
        String strBbsName = null;
        strBbsName = new String(bbsName, StandardCharsets.UTF_8);
        String name = (String) RobotHelpers.getObjectField(objCorpCfg, KeyConst.F_Application_GetProfileManager_name);
        String corpFullName = (String) RobotHelpers.getObjectField(objCorpCfg, KeyConst.F_Application_GetProfileManager_corpFullName);
        Global.bbsId = bbsId;
        Global.bbsName = strBbsName;
        //skfly add begin
        Global.corpId = corpId;
        Global.corpName = name;
        Global.vId = vId;
        //skfly add end
        EnterpriseEntity paramEntity = new EnterpriseEntity();
        paramEntity.bbsName = strBbsName;
        paramEntity.bbsId = bbsId;
        paramEntity.corpId = corpId;
        paramEntity.name = corpFullName;

        MyLog.debug(TAG, "[登录 账号的企业信息 ]" + paramEntity.toString());
        return paramEntity;
    }

    /**
     * 刷新扩容状态
     *
     * @param classLoader
     * @param callBack
     */
    public void refreshCorpInfo(ClassLoader classLoader, ProxyUtil.ProxyCommonResultDataCallBack callBack) {
        Class clazzSettingCC = RobotHelpers.findClassIfExists(KeyConst.C_SETTING_CC, classLoader);
        Object objSettingCC = RobotHelpers.callStaticMethod(clazzSettingCC, KeyConst.M_SETTING_CC_GET);
        Object objProfileSetting = RobotHelpers.callMethod(objSettingCC, KeyConst.M_SETTING_GETPROFILESETTING);
        Object objCallBack = ProxyUtil.GetProxyInstance(KeyConst.C_ICommonResultDataCallback, callBack);
        Object[] objArray = {objCallBack};
        Global.postRunnable2UI(() -> {
            MyLog.debug(TAG, "[onInvokeMethod]" + " start refresh ...");
            RobotHelpers.callMethod(objProfileSetting, KeyConst.M_SETTING_GETPROFILESETTING_refreshCorpInfo, objArray);
            refreshCorpInfo(classLoader);
            MyLog.debug(TAG, "[onInvokeMethod]" + " call succ...");
        });
    }

    private void refreshCorpInfo(ClassLoader classLoader) {
        Class<?> clazz = RobotHelpers.findClassIfExists(KeyConst.C_Application, classLoader);
        Object objIntance = RobotHelpers.callStaticMethod(clazz, KeyConst.C_Application_getInstance);
        Object objProfileManager = RobotHelpers.callMethod(objIntance, KeyConst.M_Application_GetProfileManager);
        Object objCurrentProfile = RobotHelpers.callMethod(objProfileManager, KeyConst.M_Application_GetProfileManager_GetCurrentProfile);
        RobotHelpers.callMethod(objCurrentProfile, KeyConst.M_Application_GetProfileManager_refreshCorpInfo);
    }

    public static interface ILoginListener {
        public void onLogout(String tips);
    }
}
