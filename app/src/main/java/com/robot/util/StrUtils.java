package com.robot.util;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.robot.common.Global;
import com.robot.common.MConfiger;
import com.robot.entity.SimpleMsgEntity;
import com.robot.hook.KeyConst;
import com.robot.com.BuildConfig;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.robot.robothook.RobotHelpers;

public class StrUtils {
    private static final String TAG = StrUtils.class.getSimpleName();

    public static long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
    public static long nh = 1000 * 60 * 60;//一小时的毫秒数
    public static long nm = 1000 * 60;//一分钟的毫秒数
    public static long ns = 1000;//一秒钟的毫秒数

    public static List<SimpleMsgEntity> ByteToString(byte[] bytes, int contentType) {
        if (contentType == 0 || contentType == 2) {
            List<SimpleMsgEntity> simpleList = parseTextData(bytes);
            return simpleList;
        }
        return null;
        //r = new String(bytes, StandardCharsets.UTF_8);
//        if(contentType == MsgHandleEnum.GREY.getType() || contentType == MsgHandleEnum.GROUP_INPUT_TXT.getType()){    //不同contentType解析数据不一样,打招呼的消息不做裁剪
//
//        }else if(contentType == 101 || contentType == 14){
//            Class[] clazzArray = {byte[].class};
//            Object[] objArray = {bytes};
//            Class clazz = RobotHelpers.findClassIfExists(KeyConst.C_WwRickMsg_FileMsg, Global.loadPackageParam.classLoader);
//            Object resultObj = RobotHelpers.callStaticMethod(clazz,"parseFrom",clazzArray,objArray);
//            byte[] bs = (byte[]) RobotHelpers.getObjectField(resultObj,"url");
//            String url = StrUtilsDjz_cJ(bs);
//            MyLog.debug(TAG,"[ByteToString]" + " resultObj file:" + resultObj + " url:" + url);
//        }else {
//            if(r != null && r.length() > 8){
//                r = r.substring(8);
//            }
//        }
    }


    private static final Object parseAtMsg(byte[] data) {
        Object obj;
        Class clazzRickMsg = RobotHelpers.findClassIfExists(KeyConst.C_WwRichmessage_AtMessage, Global.loadPackageParam.classLoader);
        Class[] clazzArray = {byte[].class};
        Object[] objArray = {data};
        obj = RobotHelpers.callStaticMethod(clazzRickMsg, KeyConst.M_WwRichmessage_AtMessage_parseFrom, clazzArray, objArray);
        return obj;
    }

    public static final String getContentTextInfo(List<SimpleMsgEntity> rList) {
        StringBuilder builder = new StringBuilder();
        //handle string
        if (rList != null && rList.size() > 0) {
            for (SimpleMsgEntity sEntity : rList) {
                if (sEntity.contentType == 5) { //5自己at
                    if (sEntity.atMsg != null && sEntity.atMsg.name != null) {
                        String atName = StrUtils.byteToUTFStr(sEntity.atMsg.name);
                        builder.append("@" + atName);
                        if (BuildConfig.customConfigLog) {
                            MyLog.debug(TAG, "[getContentTextInfo]" + " sEntity.atMsg:" + sEntity.atMsg);
                        }
                    }
                } else if (sEntity.contentType != 10) {    //10是群内人at
                    builder.append(sEntity.content);
                    if (BuildConfig.customConfigLog) {
                        MyLog.debug(TAG, "[getContentTextInfo]" + " content:" + sEntity.content + " sEntity.atMsg:" + sEntity.atMsg + " sEntity.contentType:" + sEntity.contentType);
                    }
                }
            }
        }
        return builder.toString();
    }

    public static final List<SimpleMsgEntity> parseTextData(byte[] infos) {
        List<SimpleMsgEntity> rList = new ArrayList<>();
        try {

            Class clazzRichMsg = RobotHelpers.findClassIfExists(KeyConst.C_WwRichmessage_RichMessage, Global.loadPackageParam.classLoader);
            Class[] clazzArray = {byte[].class};
            Object[] objArray = {infos};
            Object objRichMsg = RobotHelpers.callStaticMethod(clazzRichMsg, KeyConst.M_WwRichmessage_RichMessage_parseFrom, clazzArray, objArray);
            Object[] objMsgs = (Object[]) RobotHelpers.getObjectField(objRichMsg, KeyConst.F_WwRichmessage_RichMessage_messages);
            if (objMsgs != null && objMsgs.length > 0) {
                for (Object obj : objMsgs) {
                    int contentType = RobotHelpers.getIntField(obj, KeyConst.F_WwRichmessage_RichMessage_messages_contentType);
                    byte[] content = (byte[]) RobotHelpers.getObjectField(obj, KeyConst.F_WwRichmessage_RichMessage_messages_data);
                    SimpleMsgEntity sEntity = new SimpleMsgEntity();
                    sEntity.contentType = contentType;
                    MyLog.debug(TAG, "[parseTextData]" + " contentType:" + contentType);
                    if (contentType == 5 || contentType == 10) {  //5自己at  10是群内人at
                        SimpleMsgEntity.SimpleAtMsgEntity atMsg = new SimpleMsgEntity.SimpleAtMsgEntity();
                        Object objAtMsg = parseAtMsg(content);
                        atMsg.name = (byte[]) RobotHelpers.getObjectField(objAtMsg, KeyConst.F_WwRichmessage_RichMessage_messages_data_name);
                        atMsg.uin = RobotHelpers.getLongField(objAtMsg, KeyConst.F_WwRichmessage_RichMessage_messages_data_uin);
                        sEntity.atMsg = atMsg;
                        rList.add(sEntity);
                    } else {
                        sEntity.content = StrUtils.byteToUTFStr(content);
                        if (!TextUtils.isEmpty(sEntity.content)) {
                            sEntity.content = removeFirstSpace(sEntity.content);
                        }
                        MyLog.debug(TAG, "[parseTextData]" + " content:" + sEntity.content + " contentType:" + contentType);
                        rList.add(sEntity);
                    }
                }
            }
        } catch (Exception e) {

        }

        return rList;
    }

    public static final String removeFirstSpace(String data) {
        if (!TextUtils.isEmpty(data)) {
            StringBuilder builder = new StringBuilder();
//            for(int i = 0;i < data.length();i++){
//                char c = data.charAt(i);
//                if(ok || ((c > 10 && c < 65533) || c == '.')){
//                    builder.append(c);
//                    ok = true;
//                }
//            }
            if (data.length() > 2) {
                String info = data.substring(2);
                if (info != null && info.length() > 0) {
                    char c = info.charAt(0);
                    if (c < 31 && c != '.') {
                        info = info.substring(1);
                    }
                }
                builder.append(info);
            } else {
                builder.append(data);
            }
            String result = builder.toString();
            if (result.length() > 0) {
                char cFirst = result.charAt(0);
                int iFirst = cFirst;
                MyLog.debug(TAG, "[removeFirstSpace]" + " iFirst:" + iFirst);
            }
            return result;
        }
        return "";
    }

    public static final String byteToUTFStr(byte[] b) {
        if (b != null && b.length > 0) {
            try {
                return new String(b, StandardCharsets.UTF_8);
            } catch (Exception ee) {
                MyLog.error(TAG + " byteToUTFStr", ee);
            }
        }
        return "";
    }

    public static final byte[] strToUTFByte(String str) {
        if (str != null && !str.isEmpty()) {
            try {
                return str.getBytes(StandardCharsets.UTF_8);
            } catch (Throwable ee) {
                MyLog.error(TAG + " strToUTFByte", ee);
            }
        }
        return new byte[0];
    }


    public static String getStr(String str, boolean z) {
        if (str.length() < 1 || str.indexOf("\r") < 0) {
            return str;
        }
        char[] charArray = str.toCharArray();
        int i = 0;
        int i2 = 0;
        while (i < charArray.length - 1) {
            char c2 = charArray[i];
            int i3 = i + 1;
            char c3 = charArray[i3];
            if (charArray[i] == 13 && (c3 == 10 || (z && c3 == 13))) {
                charArray[i3] = 10;
                charArray[i] = 10;
                while (i < charArray.length - 1) {
                    int i4 = i + 1;
                    charArray[i] = charArray[i4];
                    i = i4;
                }
                i2++;
            } else if (c2 == 13) {
                charArray[i] = 10;
            }
            i = i3;
        }
        if (13 == charArray[charArray.length - 1]) {
            charArray[charArray.length - 1] = 10;
        }
        return new String(charArray, 0, charArray.length - i2);
    }


    public static String oV(String str) {
        if (str == null) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        int length = str.length();
        int i = 0;
        while (i < length) {
            char charAt = str.charAt(i);
            int i2 = 1;
            if (charAt == 13) {
                if (i < length - 1 && str.charAt(i + 1) == 10) {
                    i2 = 2;
                }
                sb.append(' ');
            } else if (charAt == 10) {
                if (i < length - 1 && str.charAt(i + 1) == 13) {
                    i2 = 2;
                }
                sb.append(' ');
            } else if (charAt == 8233 || charAt == 12) {
                sb.append(' ');
            } else {
                sb.append(charAt);
            }
            i += i2;
        }
        return sb.toString();
    }


    public static final String getRunStrTime(long t) {
        StringBuilder builder = new StringBuilder();
        long diff = Math.abs(System.currentTimeMillis() - t);
        long day = diff / nd;//计算差多少天
        long hour = diff % nd / nh;//计算差多少小时
        long min = diff % nd % nh / nm;//计算差多少分钟
        long sec = diff % nd % nh % nm / ns;//计算差多少秒//输出结果
        if (day > 0) {
            builder.append(day + "天");
        }
        if (hour > 0) {
            builder.append(hour + "小时");
        }
        if (min > 0) {
            builder.append(min + "分钟");
        }
        if (sec > 0) {
            builder.append(sec + "秒");
        }
        return builder.toString();
    }

    /***
     * 获取时间
     * @param t
     * @return
     */
    public static final String getTimeStr(long t) {
        String str;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(t);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = cal.getTime();
        str = sdf.format(date);
        return str;
    }

    /**
     * 获取具体时间状态
     *
     * @return
     */
    public static final String getTimeDetailStr() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static final String buildCommonToast(boolean isSucc) {
        StringBuilder builder = new StringBuilder();
        builder.append("Sn:" + Global.getDeviceSn() + "\n");
        builder.append(MConfiger.env.getDesc() + "\n");
        builder.append("此设备不属于任何业务\n");
        builder.append("请先到机器人控制台录入新设备\n");
        builder.append("然后重启企微App\n");
        return builder.toString();
    }


    public static String objectToJson(Object object) {

        ValueFilter profilter = new ValueFilter() {
            @Override
            public Object process(Object object, String name, Object value) {
                if (value instanceof byte[]) {
                    return new String((byte[]) value,StandardCharsets.UTF_8);
                }
                    /* if (value instanceof byte[][]){
                        byte[][] data =(byte[][]) value;
                       String[] array = new String[data.length];
                        for (int i = 0; i < data.length; i++) {
                            array[i]=(new String(  data[i]));
                        }
                        return  array;
                    }*/
                return value;
            }
        };
        return JSON.toJSONString(object, profilter);

    }

    public static String getSystemRecordPhoneNum(String originalString) {
        try {
            int startIndex = originalString.indexOf("(") + 1;
            int endIndex = originalString.indexOf(")");
            return originalString.substring(startIndex, endIndex);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    public static long getSystemRecordTime(String originalString) {
        try {
            int startIndex = originalString.indexOf("_") + 1;
            int endIndex = originalString.lastIndexOf(".");
            String dateTimeString = originalString.substring(startIndex, endIndex);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = format.parse(dateTimeString);
            // 现在，milliseconds 中包含了时间对应的毫秒数
            return date.getTime();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean isRightRecord(long timestamp1, long timestamp2) {
        try {
            if (timestamp2 > 0 && (timestamp1 > timestamp2)) {
                Log.d("PhoneUtils", "isRightRecord:是 " + timestamp1 + " " + timestamp2);
                return true;
            } else {
                Log.d("PhoneUtils", "isRightRecord:否" + timestamp1 + " " + timestamp2);
                return false;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }
}
