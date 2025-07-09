package com.robot.util;

import com.robot.common.Global;
import com.robot.entity.RoomTips;
import com.robot.entity.RoomTipsList;
import com.robot.hook.KeyConst;
import com.robot.robothook.RobotHelpers;

import java.lang.reflect.Array;

import de.robv.android.xposed.XposedHelpers;

/***
 *@author 
 *@date 2022/1/18
 *@description
 ****/
class RoomTipsParseUtil {

    public static RoomTipsList parseData(byte[] bytes) {
        Class clazz = RobotHelpers.findClassIfExists(KeyConst.C_WwRichmessageBase_RoomTips, Global.loadPackageParam.classLoader);
        MyLog.debug("RoomTipsParseUtil", " class " + clazz);
        Object resultObj = RobotHelpers.callStaticMethod(clazz, KeyConst.M_WwRichmessageBase_RoomTips_parseFrom, bytes);
        MyLog.debug("RoomTipsParseUtil", " resultObj " + resultObj);
        RoomTipsList roomTipsList = new RoomTipsList();
        roomTipsList.subtype = XposedHelpers.getIntField(resultObj, KeyConst.F_WwRichmessageBase_RoomTips_parseFrom_subtype);

        Object tipslist = XposedHelpers.getObjectField(resultObj, KeyConst.F_WwRichmessageBase_RoomTips_parseFrom_tipslist);
        if (tipslist != null) {
            int length = Array.getLength(tipslist);
            roomTipsList.tipslist = new RoomTips[length];
            for (int i = 0; i < length; i++) {
                RoomTips roomTips = new RoomTips();
                Object item = Array.get(tipslist, i);
                roomTips.newRoomcreator = XposedHelpers.getLongField(item, KeyConst.F_WwRichmessageBase_RoomTips_parseFrom_tipslist_newRoomcreator);
                roomTips.oldRoomcreator = XposedHelpers.getLongField(item, KeyConst.F_WwRichmessageBase_RoomTips_parseFrom_tipslist_oldRoomcreator);
                roomTips.tips = (String) XposedHelpers.getObjectField(item, KeyConst.F_WwRichmessageBase_RoomTips_parseFrom_tipslist_tips);
                roomTips.vidNameShowType = XposedHelpers.getIntField(item, KeyConst.F_WwRichmessageBase_RoomTips_parseFrom_tipslist_vidNameShowType);
                roomTips.vid = (long[]) XposedHelpers.getObjectField(item, KeyConst.F_WwRichmessageBase_RoomTips_parseFrom_tipslist_vid);
                roomTipsList.tipslist[i] = roomTips;
            }
        }
        return roomTipsList;
    }
}
