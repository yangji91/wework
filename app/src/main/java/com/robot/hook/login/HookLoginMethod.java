package com.robot.hook.login;

import com.robot.entity.ResEntity;
import com.robot.entity.UserEntity;
import com.robot.hook.KeyConst;
import com.robot.hook.base.HookBaseMethod;
import com.robot.hook.base.IHookCallBack;
import com.robot.util.MyLog;
import com.robot.util.UserParseUtil;
import com.robot.robothook.RobotHelpers;
import com.robot.robothook.LoadPackageParam;

/**
 * 获取用户登录信息
 */
public class HookLoginMethod extends HookBaseMethod<UserEntity> {

    @Override
    public void onHookInfo(Class clazz, LoadPackageParam loadPackageParam, IHookCallBack callBack) {

    }

    @Override
    public boolean onInvokeMethod(Class classDb, LoadPackageParam loadPackageParam, IHookCallBack paramCall) {
        boolean succ = false;
        Class clazzCC = RobotHelpers.findClassIfExists(KeyConst.C_IAccount_CC,loadPackageParam.classLoader);
        Object objImple = RobotHelpers.callStaticMethod(clazzCC,KeyConst.C_IAccount_CC_GET);
//        //getLoginUser
        Object objUser = RobotHelpers.callMethod(objImple, KeyConst.M_IACCOUNT_GETLOGINUSER);
        long userId = (long) RobotHelpers.callMethod(objImple,KeyConst.M_IACCOUNT_GETLOGINUSERID);
        UserEntity userEntity = null;
        if(objUser != null) {
            MyLog.debug(TAG,"[onInvokeMethod]" + " userObj=>" + objUser);
            userEntity = UserParseUtil.parseUserModel(objUser);
            userEntity.id = userId;
            if (paramCall != null) {
                ResEntity<UserEntity> resEntity;
                if (userEntity != null) {
                    resEntity = ResEntity.genSucc(userEntity);
                } else {
                    resEntity = ResEntity.genErr("获取登录用户失败");
                }
                paramCall.onCall(resEntity);
            }
        }
        succ = true;
        return succ;
    }
}
