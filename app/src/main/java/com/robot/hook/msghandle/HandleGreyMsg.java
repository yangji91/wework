package com.robot.hook.msghandle;

import com.robot.common.Global;
import com.robot.entity.MsgEntity;
import com.robot.entity.ParamLogoutEntity;
import com.robot.entity.ResEntity;
import com.robot.entity.UserEntity;
import com.robot.hook.HookMethodEnum;
import com.robot.hook.base.HookBaseMethod;
import com.robot.hook.base.IHookCallBack;
import com.robot.hook.msghandle.base.BaseHandleMsg;
import com.robot.netty.ProtocalManager;
import com.robot.nettywss.WssProtocalManager;
import com.robot.util.MyLog;

import com.robot.robothook.LoadPackageParam;
import com.robot.util.UserParseUtil;


public class HandleGreyMsg implements BaseHandleMsg {
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onHandleMsg(LoadPackageParam loadPackageParam, MsgEntity entity) {
        WssProtocalManager.sendMsgEntity(entity, "tip");
//        ProtocalManager.getInstance().sendMsgEntity(entity, "sendGreyMsg");
//
//        if (entity.content.contains("开启了联系人验证")) {
//            WssProtocalManager.sendMsgEntity(entity,"tip");
//        } else if (entity.content.contains("可以开始聊天了") || entity.content.contains("添加你为联系人")) {
//            //打招呼消息单独处理
//            long convId = entity.conversationId;
//            MyLog.debug(TAG, "[onHandleMsg]" + " handle 欢迎语...convId:" + convId + " content:" + entity.content + " id " + entity.id + " remoteId  " + entity.remoteId);
//            // 使用其他方式实现 这里的回话id不对，导致无法找到联系人
//            Global.postRunnable2UIDelay(new Runnable() {
//                @Override
//                public void run() {
//                    syncContactInfo(entity);
//                }
//            }, 1000 * 3);
//        } else if (entity.content.contains("账号的此功能已被封禁")) {  //账号被封  由于违规行为，你账号的此功能已被封禁
//            HookBaseMethod<ParamLogoutEntity> hookBaseMethod = HookMethodEnum.LOGOUT.getMethod();
//            hookBaseMethod.onInvokeMethod(null, loadPackageParam, new IHookCallBack<ParamLogoutEntity>() {
//                @Override
//                public void onCall(ResEntity<ParamLogoutEntity> resEntity) {
//                    MyLog.debug(TAG, "[onHandleMsg]" + " 退出账号执行结果:" + resEntity.isSucc());
//                }
//
//                @Override
//                public ParamLogoutEntity getParams() {
//                    ParamLogoutEntity pEntity = new ParamLogoutEntity();
//                    pEntity.msg = "账号的此功能已被封禁";
//                    pEntity.isBan = true;
//                    return pEntity;
//                }
//            });
//        }
//        //群公告消息
//        else if (entity.contentType == MsgHandleEnum.TextMSG.getType() && null != entity.atUsers && 1 == entity.atUsers.size() && entity.atUsers.get(0).nickName.equals("") && 0 == entity.atUsers.get(0).remoteId) {
//            //从待处理Map中找到对应的Map,并调用
//            //MyLog.debug(TAG,"收到自己发送的群公告: entity="+new Gson().toJson(entity));
//            //MessageReceivedController.getInstance().customCallback(entity);
//        }
    }

    /**
     * 同步增量外部联系人信息
     *
     * @param convEntity
     */
    private void syncContactInfo(MsgEntity convEntity) {

        Object userObj = Global.getUserObject(convEntity.sender);
        if (userObj != null) {
            UserEntity userEntity = UserParseUtil.parseUserModel(userObj);
            MyLog.debug(TAG, "[sychrContactInfo]" + " userEntity:" + userEntity);
//        userList.add(userEntity);
//        ProtocalManager.getInstance().sendContactInfo(userList);
            WssProtocalManager.sendAddContactInfo(userEntity);
            MyLog.debug(TAG, "[sychrContactInfofdfa]" + " code:" + convEntity.id + "sender =" + convEntity.sender);
        }


//        List<UserEntity> userList = new ArrayList<>();
//        ConvController.getInstance().getUserByIdWithConversation(Global.loadPackageParam.classLoader, convEntity.id, convEntity.sender, new ProxyUtil.ProxyUserResultCallBack() {
//            @Override
//            public void onResult(int i, Object[] userObj) {
//                MyLog.debug(TAG, "[sychrContactInfo]" + " code:" + i + "user =" + JSON.toJSONString(userObj));
//                if (i == 0) {
//                    UserEntity userEntity = UserParseUtil.parseUserModel(userObj[0]);
//                    List<UserEntity> userList = new ArrayList<>();
//                    MyLog.debug(TAG, "[sychrContactInfo]" + " userEntity:" + userEntity);
//                    userList.add(userEntity);
//                    ProtocalManager.getInstance().sendContactInfo(userList);
//                } else {
//
//                    Global.postRunnable2UI(new Runnable() {
//                        @Override
//                        public void run() {
//                            ConvController.getInstance().refreshUserObjectInMainThread(Global.loadPackageParam.classLoader, new long[]{convEntity.remoteId}, convEntity.id, new ProxyUtil.ProxyUserResultCallBack() {
//                                @Override
//                                public void onResult(int i, Object[] userObj) {
//                                    if (i == 0) {
//                                        UserEntity userEntity = UserParseUtil.parseUserModel(userObj[0]);
//                                        List<UserEntity> userList = new ArrayList<>();
//                                        MyLog.debug(TAG, "[sychrContactInfo]" + "refreshUserObjectInMainThread userEntity:" + userEntity);
//                                        userList.add(userEntity);
//                                        ProtocalManager.getInstance().sendContactInfo(userList);
//                                    }
//                                }
//                            });
//                        }
//                    });
//                }
//            }
//        });
    }
}
