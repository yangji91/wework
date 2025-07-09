package com.robot.util;

import com.robot.common.Global;
import com.robot.hook.KeyConst;
import com.robot.robothook.RobotHelpers;

/***
 *@author 
 *@date 2022/2/8
 *@description
 ****/
public class GroupSendNotifyMsgParseUtil {


    public static String  parseData(byte[] bs) {

        Class<?> clazz_WwCustomer$GroupSendNotifyMsg = RobotHelpers.findClass(KeyConst.C_WwCustomer_GroupSendNotifyMsg, Global.loadPackageParam.classLoader);
        Object obj =CodedOutputByteBufferNanoUtil.bytesToMessage(bs, clazz_WwCustomer$GroupSendNotifyMsg);

        return  StrUtils.objectToJson(obj);

    }
}
