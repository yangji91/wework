package com.robot.netty.handle.imple.contact;


import com.robot.common.Global;
import com.robot.entity.ResEntity;
import com.robot.entity.UserEntity;
import com.robot.hook.KeyConst;
import com.robot.hook.base.HookBaseMethod;
import com.robot.hook.base.IHookCallBack;
import com.robot.robothook.LoadPackageParam;
import com.robot.robothook.RobotHelpers;
import com.robot.util.MyLog;
import com.robot.util.ProxyUtil;

/***
 *@author 
 *@date 2021/7/20
 *@description
 ****/
public class HookDeleteOuterFriendMethod extends HookBaseMethod<UserEntity> {
    @Override
    public void onHookInfo(Class clazz, LoadPackageParam loadPackageParam, IHookCallBack<UserEntity> callBack) {
    }

    @Override
    public boolean onInvokeMethod(Class classDb, LoadPackageParam loadPackageParam, IHookCallBack<UserEntity> paramCall) {
        if (paramCall != null) {
            try {
                MyLog.debug(TAG, "HookDeleteOuterFriendMethod [onInvokeMethod]" + " start... call:");
                UserEntity userEntity = paramCall.getParams();
                Object user = Global.getUserObject(userEntity.remoteId);
                Class[] clazzArray = {int.class, String.class, user.getClass(), RobotHelpers.findClassIfExists(KeyConst.C_ICommonStringCallback, Global.loadPackageParam.classLoader)};
                Object proxyInstance = ProxyUtil.GetProxyInstance(KeyConst.C_ICommonStringCallback, new ProxyUtil.ProxyStringResultCallBack() {
                    @Override
                    public void onResult(int i, String str) {
                        MyLog.debug(TAG, "HookDeleteOuterFriendMethod [onResult]" + "code =" + i + " result =" + str);
                        if (i == 0) {
                            paramCall.onCall(ResEntity.genSucc());
                        } else {
                            paramCall.onCall(ResEntity.genErr(userEntity, "执行失败"));
                        }
                    }
                });
                Object[] objArray = {4, "", user, proxyInstance};
                Global.postRunnable2UI(() -> {
                    RobotHelpers.callStaticMethod(RobotHelpers.findClass(KeyConst.C_ContactManager, loadPackageParam.classLoader), KeyConst.M_ContactManager_M0, clazzArray, objArray);
                    MyLog.debug(TAG, "[onResult]" + "调用结束");
                });
                return true;
            } catch (Exception e) {
                paramCall.onCall(ResEntity.genErr("", "执行失败-遭遇执行异常" + e));
                MyLog.error(TAG, e);
                return true;
            }
        } else {
            paramCall.onCall(ResEntity.genErr("", "执行失败-没有用户id"));
            return true;
        }
    }
}
