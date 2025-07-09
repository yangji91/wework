package com.robot.controller.message;

import android.text.TextUtils;

import androidx.annotation.UiThread;

import com.alibaba.fastjson.JSON;
import com.robot.common.Global;
import com.robot.controller.ConvController;
import com.robot.controller.LoginController;
import com.robot.controller.db.DBController;
import com.robot.entity.MsgEntity;
import com.robot.hook.KeyConst;
import com.robot.hook.util.ConvParseUtil;
import com.robot.netty.entity.rsp.PRspActionTextMsgEntity;
import com.robot.util.MyLog;
import com.robot.util.ProxyUtil;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.robot.robothook.RobotHelpers;

/***
 *@author
 *@date 2021/7/16
 *@description 消息的控制类
 ****/
public class MessageController {


    public static final String TAG = "MessageController";

    /**
     * 根据会话获取最新的消息 时间从大到小排列
     *
     * @param classLoader
     * @param conversation 会话
     * @param message      上一条
     * @param size         获取多少条
     * @param page
     * @param callback     回调
     */
    public static void getHistoryMessage(ClassLoader classLoader, Object conversation, Object message, int size, boolean page, IGetHistoryMessageCallback callback) {
        if (callback == null) return;
        Global.postRunnable2UI(new Runnable() {
            @Override
            public void run() {
                final Class<?> ipi = RobotHelpers.findClass(KeyConst.C_ConversationEngine, classLoader);
                Object service = RobotHelpers.callStaticMethod(ipi, KeyConst.M_ConversationEngine_getConversationService);
                Class[] clazz = new Class[]{
                        RobotHelpers.findClass(KeyConst.C_Conversation_NativeHandleHolder, classLoader),
                        RobotHelpers.findClass(KeyConst.C_MESSAGE, classLoader),
                        int.class, boolean.class,
                        RobotHelpers.findClass(KeyConst.I_IGetHistoryMessageCallback, classLoader)
                };
                MyLog.debug(TAG, "获取历史消息 开始 " + System.currentTimeMillis());
                RobotHelpers.callMethod(service, KeyConst.M_ConversationEngine_getConversationService_GetHistoryMessage, clazz, conversation, message, size, page, ProxyUtil.GetProxyInstance(KeyConst.I_IGetHistoryMessageCallback, new ProxyUtil.ProxyCallBack() {
                            @Override
                            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                                if (method.getName().equals(KeyConst.M_IGetHistoryMessageCallback_onResult)) {
                                    int code = (int) objects[0];
                                    Object[] objects1 = (Object[]) objects[1];
                                    MyLog.debug(TAG, "获取历史消息 结束 " + System.currentTimeMillis() + " 消息量 " + objects1.length);
                                    callback.onResult(code, objects1);
                                }
                                return null;
                            }
                        })
                );
            }
        });

    }


    /**
     * 文本消息发送
     *
     * @param classLoader
     * @param conversationID 会话id
     * @param content        文本内容
     * @param callBack       回调
     */
    public static void postTextMessage(ClassLoader classLoader, long conversationID, String content, SendMessageCallBack callBack) {
        Object sendExtraInfo = WeWorkMessageUtil.getSendExtraInfo(classLoader, conversationID);
        Object textualMessage = WeWorkMessageUtil.buildTextualMessage(classLoader, content);
        sendTextualMessage(classLoader, conversationID, sendExtraInfo, textualMessage, callBack);
    }

    /**
     * 私聊 文件消息发送
     *
     * @param classLoader
     * @param conversationID 会话id
     * @param path           文件
     */
    public static void postFileMessage(ClassLoader classLoader, long conversationID, String path) {
        ConvController.getInstance().getConversationObjectByRemoteIDAndConversationId(classLoader, conversationID, 0l, false, new ConvController.GetOrCreateConversationCallBack() {
            @Override
            public void onResult(int code, Object conversationObject, String message) {
                WeWorkMessageUtil.sendFileMessage(classLoader, conversationObject, path);
            }
        });

    }

    private static void sendTextualMessage(ClassLoader classLoader, long conversationID, Object sendExtraInfo, Object textualMessage, SendMessageCallBack callBack) {
        Global.postRunnable2UI(new Runnable() {
            @Override
            public void run() {
                Class clazzFlp = RobotHelpers.findClassIfExists(KeyConst.C_MessageManager, classLoader);
                RobotHelpers.callStaticMethod(clazzFlp, KeyConst.M_MessageManager_sendTextualMessage, Global.getContext(), conversationID, textualMessage, sendExtraInfo, ProxyUtil.GetProxyInstance(KeyConst.I_ISendMessageCallback, new ProxyUtil.ProxyCallBack() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                        transforSendMessageCall(o, method, objects, callBack);
                        return null;
                    }
                }));
            }
        });
    }


    private static void transforSendMessageCall(Object o, Method method, Object[] objects, SendMessageCallBack callBack) {
        if (callBack != null) {
            if (method.getName().equals("onResult")) {
                callBack.onResult((int) objects[0], objects[1], objects[2]);
            } else if (method.getName().equals("onProgress")) {
                callBack.onProgress(objects[0], (long) objects[1], (long) objects[2]);
            }
        }
    }


    public static void getMessageFromDataBaseByIdAndTime(long id, long time, GetHistoryMessageCallback getHistoryMessageCallback) {
        String sql = "select m.*,a.appinfo from MESSAGE m left join MSG_APPINFO a on  m.LID=a.lid where  m.conv_id =" + id + " and m.send_time > " + (long) (time / 1000L) + " ";
        DBController.getInstance().execSql(Global.loadPackageParam.classLoader, sql, DBController.getInstance().getInfoDBPath(Global.loadPackageParam.classLoader), new DBController.IExecSqlCallback() {
            @Override
            public void onResult(String errMsg, long execTime, List<Map<String, String>> resultMap) {
                changeToMessage(errMsg, resultMap, getHistoryMessageCallback);
            }
        });

    }


    public static void getMessageFromDBByBetweenTime(long starTime, long endTime, GetHistoryMessageCallback getHistoryMessageCallback) {
        String sql = " select m.*,a.appinfo from MESSAGE m left join MSG_APPINFO a on  m.LID=a.lid where      m.send_time >=" + starTime + "  and  m.send_time <= " + endTime;
        DBController.getInstance().execSql(Global.loadPackageParam.classLoader, sql, DBController.getInstance().getInfoDBPath(Global.loadPackageParam.classLoader), new DBController.IExecSqlCallback() {
            @Override
            public void onResult(String errMsg, long execTime, List<Map<String, String>> resultMap) {
                changeToMessage(errMsg, resultMap, getHistoryMessageCallback);
            }
        });

    }


    private static String getIds(Collection collection) {
        Iterator iterator = collection.iterator();
        StringBuilder stringBuilder = new StringBuilder("(");
        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next() + ",");
        }
        return stringBuilder.subSequence(0, stringBuilder.length() - 1) + ")";
    }

    private static String getIds(long[] collection) {

        StringBuilder stringBuilder = new StringBuilder("(");
        for (Object key : collection) {
            stringBuilder.append(key + ",");
        }

        return stringBuilder.subSequence(0, stringBuilder.length() - 1) + ")";
    }

    public static void getMessageFromDBById(long id, GetHistoryMessageCallback getHistoryMessageCallback) {


        String sql = " select * from MESSAGE where  LID = " + id;

        DBController.getInstance().execSql(Global.loadPackageParam.classLoader, sql, DBController.getInstance().getInfoDBPath(Global.loadPackageParam.classLoader), new DBController.IExecSqlCallback() {
            @Override
            public void onResult(String errMsg, long execTime, List<Map<String, String>> resultMap) {
                changeToMessage(errMsg, resultMap, getHistoryMessageCallback);
            }
        });

    }

    public static void getMessageFromDBByIdBetweenTime(long id, long starTime, long endTime, GetHistoryMessageCallback getHistoryMessageCallback) {
        String sql = " select m.*,a.appinfo from MESSAGE m left join MSG_APPINFO a on  m.LID=a.lid where   m.conv_id =" + id + "  and m.sender_id=" + LoginController.getInstance().getLoginUserId() + " and m.content_type != 1011 and m.send_time >=" + starTime + "  and  m.send_time <= " + endTime;
        DBController.getInstance().execSql(Global.loadPackageParam.classLoader, sql, DBController.getInstance().getInfoDBPath(Global.loadPackageParam.classLoader), new DBController.IExecSqlCallback() {
            @Override
            public void onResult(String errMsg, long execTime, List<Map<String, String>> resultMap) {
                changeToMessage(errMsg, resultMap, getHistoryMessageCallback);
            }
        });

    }

    /**
     * 获取正在发送的一条消息
     *
     * @param id
     * @param size
     * @param getHistoryMessageCallback
     */
    public static void getLastSendingMessageFromDataBase(long id, int size, GetHistoryMessageCallback getHistoryMessageCallback) {
        String sql = " select * from MESSAGE where  flag=4 and  conv_id =" + id + " order by LID desc " + "  limit 0," + size;
        MyLog.debug(TAG, "sql =" + sql);
        DBController.getInstance().execSql(Global.loadPackageParam.classLoader, sql, DBController.getInstance().getInfoDBPath(Global.loadPackageParam.classLoader), new DBController.IExecSqlCallback() {
            @Override
            public void onResult(String errMsg, long execTime, List<Map<String, String>> resultMap) {
                changeToMessage(errMsg, resultMap, getHistoryMessageCallback);
            }
        });
    }

    public static void getMessageFromDataBaseByIdAndTime(long id, int size, GetHistoryMessageCallback getHistoryMessageCallback) {
        String sql = " select * from MESSAGE where  conv_id =" + id + "  limit 0," + size + " order by LID desc ";
        DBController.getInstance().execSql(Global.loadPackageParam.classLoader, sql, DBController.getInstance().getInfoDBPath(Global.loadPackageParam.classLoader), new DBController.IExecSqlCallback() {
            @Override
            public void onResult(String errMsg, long execTime, List<Map<String, String>> resultMap) {
                changeToMessage(errMsg, resultMap, getHistoryMessageCallback);
            }
        });
    }

    public static void changeToMessage(String errMsg, List<Map<String, String>> resultMap, GetHistoryMessageCallback getHistoryMessageCallback) {
        if (TextUtils.isEmpty(errMsg)) {
            MsgEntity[] msgEntities = new MsgEntity[resultMap.size()];
            for (int i = 0; i < resultMap.size(); i++) {
                Map<String, String> map = resultMap.get(i);
                MsgEntity msgEntity = new MsgEntity();
                msgEntity.flag = Integer.valueOf(map.get("flag"));
                msgEntity.msgId = Long.valueOf(map.get("LID"));
                msgEntity.asId = Long.valueOf(map.get("RID"));
                msgEntity.id = msgEntity.msgId;
                msgEntity.contentType = Integer.valueOf(map.get("content_type"));
                msgEntity.sender = Long.valueOf(map.get("sender_id"));
                msgEntity.senderTime = Long.valueOf(map.get("send_time"));
                msgEntity.conversationId = Long.valueOf(map.get("conv_id"));
                msgEntity.content = map.get("content");
                if (map.get("appinfo") != null)
                    msgEntity.appinfo = map.get("appinfo");
                msgEntity.seq = Long.valueOf(map.get("seq"));
                msgEntities[i] = msgEntity;
            }
            if (getHistoryMessageCallback != null) {
                getHistoryMessageCallback.onResult(0, msgEntities);
            }
        } else if (getHistoryMessageCallback != null) {
            MyLog.debug(TAG, "执行sql 出错" + errMsg);
            getHistoryMessageCallback.onResult(1, null);
        }
    }

    public static void getMessageFromWeWorkByConversationAndMsgId(long conversationId, long messageId, IGetHistoryMessageCallback getHistoryMessageCallback) {
        Global.postRunnable2UI(new Runnable() {
            @Override
            public void run() {
                Class<?> clazz_IMsg$CC = RobotHelpers.findClass(KeyConst.C_MSG_CC, Global.loadPackageParam.classLoader);
                Object obj_IMsg = RobotHelpers.callStaticMethod(clazz_IMsg$CC, KeyConst.M_MSG_CC_GET);
                Object messageItem = RobotHelpers.callMethod(obj_IMsg, KeyConst.M_MSG_CC_getMessageItem, conversationId, messageId);

                if (messageItem != null) {
                    Object message = RobotHelpers.callMethod(messageItem, KeyConst.M_MSG_CC_getMessageItem_getMessage);
                    getHistoryMessageCallback.onResult(0, new Object[]{message});
                } else {
                    MyLog.debug(TAG, "conversationId " + conversationId + " messageId " + messageId);
                    getHistoryMessageCallback.onResult(1, null);
                }

            }
        });

    }


    @UiThread
    public static Object getMessageObjectByNativeHandle(ClassLoader classLoader, long devinfo) {

        Class<?> clazz_Message = RobotHelpers.findClass(KeyConst.C_MESSAGE, classLoader);
        Object obj_Message = RobotHelpers.callStaticMethod(clazz_Message, KeyConst.M_MESSAGE_NewMessage);
        RobotHelpers.setLongField(obj_Message, KeyConst.F_MESSAGE_NewMessage_mNativeHandle, devinfo);
        return obj_Message;
    }

    public static void getMessageFromDataBaseByRevokedAndTime(long time, GetHistoryMessageCallback getHistoryMessageCallback) {
        String sql = "select m.*,a.appinfo from MESSAGE m left join MSG_APPINFO a on  m.LID=a.lid where  m.flag in (" + KeyConst.FLAG_RECALL + "," + KeyConst.FLAG_RECALL_GROUP + " ) and m.send_time > " + (long) (time / 1000L) + " ";
        MyLog.debug(TAG, "sql =" + sql);
        DBController.getInstance().execSql(Global.loadPackageParam.classLoader, sql, DBController.getInstance().getInfoDBPath(Global.loadPackageParam.classLoader), new DBController.IExecSqlCallback() {
            @Override
            public void onResult(String errMsg, long execTime, List<Map<String, String>> resultMap) {
                changeToMessage(errMsg, resultMap, getHistoryMessageCallback);
            }
        });
    }

    public static abstract class SendMessageCallBack implements ProxyUtil.ProxyCallBack {
        public abstract void onResult(int code, Object conversation, Object message);

        public abstract void onProgress(Object message, long j, long j2);


        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            transforSendMessageCall(o, method, objects, this);
            return null;
        }
    }

    public static interface IGetHistoryMessageCallback {
        public void onResult(int code, Object[] message);
    }

    public static interface GetHistoryMessageCallback {
        public void onResult(int code, MsgEntity[] message);
    }

}
