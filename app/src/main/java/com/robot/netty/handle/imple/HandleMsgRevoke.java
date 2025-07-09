//package com.robot.netty.handle.imple;
//
//import android.os.SystemClock;
//
//import com.alibaba.fastjson.JSON;
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import com.robot.common.Global;
//import com.robot.controller.ConvController;
//import com.robot.controller.message.MessageController;
//import com.robot.entity.ActionResultEnum;
//import com.robot.entity.ActionStatusEnum;
//import com.robot.hook.KeyConst;
//import com.robot.netty.ProtocalManager;
//import com.robot.netty.entity.rsp.PRspMsgRevokeEntity;
//import com.robot.netty.handle.BaseHandle;
//import com.robot.netty.handle.imple.runnable.MessageTask;
//import com.robot.util.MyLog;
//import com.robot.util.ProxyUtil;
//import com.robot.util.StrUtils;
//
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.robot.robothook.RobotHelpers;
//
//public class HandleMsgRevoke extends BaseHandle {
//    protected String TAG = "HandleMsgRevoke";
//    private boolean isGroup;
//
//    public HandleMsgRevoke(boolean isGroup) {
//        this.isGroup = isGroup;
//    }
//
//    private long uin;
//    private String uid;
//    private long actionType;
//    private long actionSubType;
//    private List<Object> messages = new ArrayList<>();
//    private List<Object> revokeMessages = new ArrayList<>();
//    private List<Object> errorMessages = new ArrayList<>();
//
//    @Override
//    public void onHandle(JsonObject jsonObject) {
//        Gson gson = new Gson();
//        PRspMsgRevokeEntity revokeEntity = gson.fromJson(jsonObject, PRspMsgRevokeEntity.class);
//        if (revokeEntity.allActions != null
//                && revokeEntity.allActions.size() > 0) {
//
//            //boolean
//            for (PRspMsgRevokeEntity.MsgRevokeEntity msgRevokeEntity : revokeEntity.allActions) {
//                uin = msgRevokeEntity.executorUin;
//                uid = msgRevokeEntity.uid;
//                actionType=msgRevokeEntity.actionType;
////                actionSubType=msgRevokeEntity.actionSubType;
//                ConvController.getInstance().getConversationObjectByRemoteIDAndConversationId(Global.loadPackageParam.classLoader, msgRevokeEntity.matcher.conversationId, msgRevokeEntity.matcher.remoteId,
//                        isGroup, new ConvController.GetOrCreateConversationCallBack() {
//                            @Override
//                            public void onResult(int code, Object conversationObject, String message) {
//                                MyLog.debug(TAG, "getConversationObjectByRemoteIDAndConversationId code +" + code + " conversationObject" + conversationObject);
//                                MessageController.getHistoryMessage(Global.loadPackageParam.classLoader, conversationObject, null, 50, false, new MessageController.IGetHistoryMessageCallback() {
//                                    @Override
//                                    public void onResult(int code, Object[] message) {
//                                        MyLog.debug(TAG, "getHistoryMessage code +" + code + " message length " + (message == null ? null : message.length));
//                                        if (message != null) {
//                                            List<Long> ids = new ArrayList<>();
//                                            for (int i = 0; i < msgRevokeEntity.messageDetails.size(); i++) {
//                                                ids.add(msgRevokeEntity.messageDetails.get(i).messageId);
//                                            }
//                                            for (Object item : message) {
//                                                Object wwMessage = RobotHelpers.callMethod(item, KeyConst.M_MESSAGE_getInfo);
//                                                Long id = RobotHelpers.getLongField(wwMessage, KeyConst.F_MESSAGE_ITEM_id);
//                                                if (ids.contains(id)) {
//                                                    MyLog.debug(TAG, "获取到撤回消息" + id);
//                                                    messages.add(item);
//                                                }
//                                            }
//                                            revokeMessage(messages);
//
//
//                                        }
//                                    }
//                                });
//                            }
//                        });
//            }
//        }
//    }
//
//    private ProxyUtil.ProxyStringResultCallBack callBack = new ProxyUtil.ProxyStringResultCallBack() {
//        @Override
//        public void onResult(int i, String str) {
//            if (i == 0) {
//                revokeMessages.add(str);
//            } else {
//                try {
//                    JSONObject object = new JSONObject(str);
//                    int flag = object.optJSONObject("info").optInt("flag");
//                    if (flag == KeyConst.FLAG_RECALL_GROUP || flag == KeyConst.FLAG_RECALL) {
//                        revokeMessages.add(str);
//                    } else {
//                        if (i == 62) {
//                            errorMessages.add("code =" + i + " message =群内有微信联系人，消息发出2分钟后无法撤回");
//                        } else {
//                            errorMessages.add("code =" + i + " message =");
//                        }
//                    }
//                } catch (Exception e) {
//                    errorMessages.add("code =" + i + " message =" + e.getMessage());
//                }
//
//            }
//            if (errorMessages.size() + revokeMessages.size() == messages.size()) {
//                if (revokeMessages.size() == messages.size()) {
//                    MyLog.debug(TAG, "撤回消息全部成功" + StrUtils.objectToJson(revokeMessages));
//                    ProtocalManager.getInstance().sendMsgReportCallback(uin, uid, ActionStatusEnum.SUCC, "全部成功",actionType,actionSubType);
//                } else if (messages.size() == errorMessages.size()) {
//                    MyLog.debug(TAG, "撤回消息全部失败" + StrUtils.objectToJson(errorMessages));
//                    ProtocalManager.getInstance().sendMsgReportCallback(uin, uid, ActionStatusEnum.FAILURE, ActionResultEnum.ACTION_WEWORK_RESULT.getMsg(JSON.toJSONString(errorMessages)),actionType,actionSubType);
//                } else {
//                    MyLog.debug(TAG, "撤回消息部分成功 " + StrUtils.objectToJson(errorMessages));
//                    MyLog.debug(TAG, "撤回消息部分成功 " + StrUtils.objectToJson(revokeMessages));
//                    ProtocalManager.getInstance().sendMsgReportCallback(uin, uid, ActionStatusEnum.SUCC_PART, ActionResultEnum.ACTION_WEWORK_RESULT.getMsg(JSON.toJSONString(errorMessages)),actionType,actionSubType);
//                }
//            }
//
//
//        }
//    };
//
//    private void revokeMessage(List<Object> messages) {
//        MessageTask.submit(new Runnable() {
//            @Override
//            public void run() {
//                for (Object message : messages) {
//                    MessageController.revokeMessage(Global.loadPackageParam.classLoader, message, callBack);
//                    SystemClock.sleep(1000);
//                }
//            }
//        });
//    }
//}
