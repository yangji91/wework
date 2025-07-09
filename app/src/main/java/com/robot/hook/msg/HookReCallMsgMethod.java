package com.robot.hook.msg;

import com.robot.robothook.RobotMethodParam;
import com.robot.common.Global;
import com.robot.controller.message.MessageController;
import com.robot.entity.ConvEntity;
import com.robot.entity.MsgEntity;
import com.robot.entity.ParamsMsgReCallEntity;
import com.robot.hook.KeyConst;
import com.robot.hook.base.HookBaseMethod;
import com.robot.hook.base.IHookCallBack;
import com.robot.hook.util.ConvParseUtil;
import com.robot.netty.ProtocalManager;
import com.robot.util.MsgParseUtil;
import com.robot.util.MyLog;

import java.util.HashMap;
import java.util.Iterator;

import com.robot.robothook.RobotMethodHook;

import com.robot.robothook.RobotHelpers;
import com.robot.robothook.LoadPackageParam;

/**
 * 消息撤回监听 撤回消息
 */
public class HookReCallMsgMethod extends HookBaseMethod<ParamsMsgReCallEntity> {

    public  static HookReCallMsgMethod instance;
    private static MessageIDCache messageIDCache =new MessageIDCache();


    public static  HookReCallMsgMethod getInstance() {
        if (instance==null){
            instance =new HookReCallMsgMethod();
        }
        return instance;
    }
    @Override
    public void onHookInfo(Class clazz, LoadPackageParam loadPackageParam, IHookCallBack<ParamsMsgReCallEntity> callBack) {
        MyLog.debug(TAG,"[onHookInfo]" + "...");
       RobotHelpers.hookAllMethods(RobotHelpers.findClass(KeyConst.C_MESSAGE_ITEM, Global.loadPackageParam.classLoader), KeyConst.M_MESSAGE_ITEM_onMsgUpdate, new RobotMethodHook() {
        @Override
        protected void afterHookedMethod(RobotMethodParam param) throws Throwable {
            super.afterHookedMethod(param);
            if( param.args.length > 0) {
                 onMsgRevokeUpdate(param.args[0]);
            }
         }
    });

        Class clazzJiI = RobotHelpers.findClassIfExists(KeyConst.C_ConversationEngine_IConversationObserver,loadPackageParam.classLoader);
        RobotHelpers.findAndHookMethod(clazzJiI, KeyConst.M_ConversationEngine_IConversationObserver_onUnReadCountChanged, RobotHelpers.findClass(KeyConst.C_Conversation_NativeHandleHolder, loadPackageParam.classLoader), int.class, int.class, new RobotMethodHook() {
            @Override
            protected void afterHookedMethod(RobotMethodParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object converstaion =param.args[0];
                int from = (int) param.args[1];
                int to = (int) param.args[2];
                if (from-to>0&&from-to<=2){//考虑有消息撤回的情况
                    ConvEntity convEntity = ConvParseUtil.parseConvEntity(converstaion);
                    MessageController.getMessageFromDataBaseByIdAndTime(convEntity.id,System.currentTimeMillis()-60*1000*2, new MessageController.GetHistoryMessageCallback(){

                        @Override
                        public void onResult(int code, MsgEntity[] message) {
                            if (code==0){
                                MyLog.debug(TAG,"isRevoke  getHistoryMessage "+message.length);
                                for (MsgEntity item:message) {

                                    if (isRevoke(item.flag)){
                                        MyLog.debug(TAG,"isRevoke  通过查询消息查询到消息撤回 " );
                                        onMsgRevokeUpdate( item);
                                    }
                                }
                            }
                        }
                    });
                    // todo 暂时先取历史消息50条以后 有时间在改
                   /* boolean IsExternalGroup = (boolean) RobotHelpers.callMethod(converstaion,"IsExternalGroup");
                    int pageSize =IsExternalGroup?30:20;
                    MessageController.getHistoryMessage(param.thisObject.getClass().getClassLoader(), converstaion, null, pageSize, false, new MessageController.IGetHistoryMessageCallback() {
                        @Override
                        public void onResult(int code, Object[] message) {
                            if (code==0){
                                MyLog.debug(TAG,"isRevoke  getHistoryMessage "+message.length);
                                for (Object item:message) {
                                    Object wwMessage =RobotHelpers.callMethod(item,"getInfo");
                                    int flag =RobotHelpers.getIntField(wwMessage,"flag");
                                    int sendTime =RobotHelpers.getIntField(wwMessage,"sendTime");
                                    if (System.currentTimeMillis()-(sendTime*1000L)>messageIDCache.RRVOKE_TIME){//时间之前的不需要了
                                        continue;
                                    }
                                    MyLog.debug(TAG, "消息 "+JSON.toJSONString(item));
                                    if (isRevoke(flag)){
                                        MyLog.debug(TAG,"isRevoke  通过查询消息查询到消息撤回 " );
                                        onMsgRevokeUpdate( item);
                                    }
                                }
                            }
                        }
                    });*/
                }
            }
        });
    }
    public void checkMsgRevoke(){
        MessageController.getMessageFromDataBaseByRevokedAndTime( System.currentTimeMillis()-60*1000*2, new MessageController.GetHistoryMessageCallback(){
            @Override
            public void onResult(int code, MsgEntity[] message) {
                if (code==0){
                    MyLog.debug(TAG,"isRevoke  checkMsgRevoke "+message.length);
                    for (MsgEntity item:message) {
                        if (isRevoke(item.flag)){
                            MyLog.debug(TAG,"checkMsgRevoke  通过查询消息查询到消息撤回 " );
                            onMsgRevokeUpdate( item);
                        }
                    }
                }
            }
        });
    }

    private void onMsgRevokeUpdate( Object objMsg) {
        Object wwMessage = RobotHelpers.callMethod(objMsg,KeyConst.M_MESSAGE_getInfo);
        long id = RobotHelpers.getLongField(wwMessage,KeyConst.F_MESSAGE_ITEM_id);
        int sendTime = RobotHelpers.getIntField(wwMessage,KeyConst.F_MESSAGE_ITEM_sendTime);
        int flag = RobotHelpers.getIntField(wwMessage,KeyConst.F_MESSAGE_ITEM_flag);
        if (isRevoke( flag)&&!messageIDCache.contains(id+"")){//去重
            MsgEntity msgEntity = MsgParseUtil.parseMsgEntity(objMsg);
                if(isRevoke(msgEntity.flag)){
                    messageIDCache.add( id+"",sendTime*1000L);
                    MyLog.debug(TAG,"[onHookInfo]" + "MESSAGE_ITEM  撤回消息上报  messageIDCache size ="+messageIDCache.size()+" msgId="+msgEntity.msgId+"..state:" + msgEntity.state + " flag:" + msgEntity.flag + " msg:" + msgEntity,true );
                    ProtocalManager.getInstance().sendMsgEntity(msgEntity,"onMsgRevokeUpdate");
                }

        }
    }
    private void onMsgRevokeUpdate( MsgEntity msgEntity) {

        if (isRevoke( msgEntity.flag)&&!messageIDCache.contains(msgEntity.id+"")){//去重
                if(isRevoke(msgEntity.flag)){
                    messageIDCache.add( msgEntity.id+"",msgEntity.senderTime*1000L);
                    msgEntity.content=null;
                    MyLog.debug(TAG,"[onHookInfo]" + "MESSAGE_ITEM  撤回消息上报  messageIDCache size ="+messageIDCache.size()+" msgId="+msgEntity.msgId+"..state:" + msgEntity.state + " flag:" + msgEntity.flag + " msg:" + msgEntity,true );
                    ProtocalManager.getInstance().sendMsgEntity(msgEntity,"onMsgRevokeUpdate isRevoke");
                }
        }


    }
    private boolean isRevoke(int flag) {
        return  flag == KeyConst.FLAG_RECALL ||  flag == KeyConst.FLAG_RECALL_GROUP;
    }

    @Override
    public boolean onInvokeMethod(Class classDb, LoadPackageParam loadPackageParam, IHookCallBack<ParamsMsgReCallEntity> paramCall) {
        return false;
    }

    public static class MessageIDCache  {

        private HashMap<String,Long> hashtable=new HashMap<String,Long>();
        //撤回时间判断
        public int RRVOKE_TIME=60*1000*3;
        public   void add(String id,Long time) {
            hashtable.put(id,time);
            Iterator<String> iterator = hashtable.keySet().iterator();
            while (iterator.hasNext()){
                String key=iterator.next();
                long sendTime =hashtable.get(key);
                if (System.currentTimeMillis()-sendTime> RRVOKE_TIME){
                    iterator.remove();
                }
            }
        }

        public int size(){
            return hashtable.size();
        }
        public boolean contains(String key){
            MyLog.debug("HookReCallMsgMethod","[onHookInfo]" + " key="+key);
            MyLog.debug("HookReCallMsgMethod","[onHookInfo]" + " keys="+hashtable.keySet());
            return hashtable.containsKey(key);
        }
    }
}
