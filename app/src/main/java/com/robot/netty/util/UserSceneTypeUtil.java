package com.robot.netty.util;

import android.text.TextUtils;

import com.robot.common.Global;
import com.robot.hook.KeyConst;

import com.robot.robothook.RobotHelpers;

public class UserSceneTypeUtil {

    /**
     *   private long mId;
     *     private long mId2;
     *     private String mSceneString;
     *     private int mSceneType = 0;
     * @return
     */
    public static Object buildUserSceneType(long mId,long mId2,int scenceType,String str){
        Object rEntity;
        Class clazz = RobotHelpers.findClassIfExists(KeyConst.C_UserSceneType, Global.loadPackageParam.classLoader);
        Class[] clazzArray = {long.class};
        Object[] objArray = {mId};
        rEntity = RobotHelpers.newInstance(clazz,clazzArray,objArray);
        RobotHelpers.setLongField(rEntity,KeyConst.F_UserSceneType_mId2,mId2);
        RobotHelpers.setIntField(rEntity,KeyConst.F_UserSceneType_mSceneType,scenceType);
        if(!TextUtils.isEmpty(str)){
            RobotHelpers.setObjectField(rEntity,KeyConst.F_UserSceneType_mSceneString,str);
        }
        return rEntity;
    }
}
