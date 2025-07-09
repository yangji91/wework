package com.robot.netty;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.robot.common.Global;
import com.robot.controller.ConvController;
import com.robot.entity.ConvEntity;
import com.robot.entity.ConvMember;
import com.robot.entity.UserEntity;
import com.robot.hook.KeyConst;
import com.robot.hook.util.ConvParseUtil;
import com.robot.nettywss.WssNettyEngine;
import com.robot.util.MyLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import com.robot.robothook.RobotHelpers;

/***
 *@author 
 *@date 2021/7/26
 *@description
 ****/
public class GroupManager {

    private String TAG = "GroupManager";
    private final int syncTime = 2 * 1000;//群会话同步时间
    private final int syncHZTime = 6 * 60 * 60 * 1000;//群会话同步频率 时间
    private long fastSyncTime = 0;
    private static GroupManager instance;

    public static GroupManager getInstance() {
        if (instance == null) {
            instance = new GroupManager();

        }
        return instance;
    }

    public GroupManager() {
        HandlerThread groupManager = new HandlerThread("GroupManager");
        groupManager.start();
        handler = new GroupHandler(groupManager.getLooper());
    }

    /**
     * 所有需要处理的数据
     */
    private Map<Long, ConvEntity> convEntityCache = new ConcurrentHashMap<Long, ConvEntity>();
    /**
     * 更新会员的缓存
     */
    private Map<Long, MemberInfo> memberEntityCache = new HashMap<Long, MemberInfo>();

    private Map<Long, ConvEntitySyncInfo> syncInfoCache = new HashMap<Long, ConvEntitySyncInfo>();
    private GroupHandler handler;

    public synchronized void sendGroupInfo(long conversationId) {

        Object conversion = ConvController.getInstance().getByConvId(conversationId);
        sendGroupInfo(conversion);
    }

//    public synchronized void sendGroupInfos(long conversationId) {
//
//        Object conversion = ConvController.getInstance().getByConvId(conversationId);
//        sendGroupInfo(conversion);
//    }

    public synchronized void sendGroupInfo(Object conversion) {
        if (conversion == null) return;
        Object info = RobotHelpers.callMethod(conversion, KeyConst.M_Conversion_requestInfo);
        long conversationId = RobotHelpers.getLongField(info, KeyConst.F_Conversion_requestInfo_id);
        //先缓存
        if (!convEntityCache.containsKey((Long) conversationId)) {
            ConvEntity convEntity = ConvParseUtil.parseConvEntity(conversion);
            ConvEntitySyncInfo convEntitySyncInfo = new ConvEntitySyncInfo(System.currentTimeMillis(), convEntity);
            if (!convEntitySyncInfo.equals(syncInfoCache.get(conversationId)))//频率管控
            {
                syncInfoCache.put(conversationId, convEntitySyncInfo);
                convEntityCache.put(conversationId, convEntity);
                MyLog.debug(TAG, " syncGroupInfo 缓存添加待提交的群" + conversationId + " convEntityCache " + convEntityCache.size());
            } else {
                MyLog.debug(TAG, " syncGroupInfo  " + conversationId + " 上传频繁 " + convEntitySyncInfo.md5 + "缓存的 " + syncInfoCache.get(conversationId).md5);
            }
        } else {
            MyLog.debug(TAG, " syncGroupInfo 缓存中已经存在待提交的群" + conversationId + " convEntityCache " + convEntityCache.size());
        }
        //10s 延后处理
        handler.notifyGroupSync();
    }

    /**
     * 立即更新某一个群
     *
     * @param conversationId
     */
    public synchronized void sendGroupInfoNow(long conversationId) {
        syncGroupInfo(conversationId);
    }

    public synchronized void sendGroupInfoNow(ConvEntity convEntity) {
        syncGroupInfo(convEntity);
    }

    private class GroupHandler extends Handler {
        public GroupHandler(Looper groupManager) {
            super(groupManager);
        }

        @Override
        public void handleMessage(Message msg) {
            MyLog.debug(TAG, " isConnected  " + WssNettyEngine.getInstance().isConnected);
            if (WssNettyEngine.getInstance().isConnected) {
                Iterator<Long> iterator = convEntityCache.keySet().iterator();
                if (iterator.hasNext()) {
                    long conversationId = iterator.next();
                    MyLog.debug(TAG, " 剩余处理群  " + convEntityCache.keySet().size());
                    syncGroupInfo(conversationId);
                } else {
                    syncing = false;
                    MyLog.debug(TAG, "网络更新数据量 " + memberEntityCache.size());
                    memberEntityCache.clear();
                }
            } else {
                //10s 延后处理
                handler.notifyGroupSync();
            }
        }

        public void notifyGroupSync() {
            removeMessages(1);
            if (fastSyncTime - System.currentTimeMillis() > 60 * 60 * 1000) {
                sendMessageDelayed(handler.obtainMessage(1), 3000);
            } else {
                sendMessageDelayed(handler.obtainMessage(1), 10000);
            }


        }

        public void notifyGroupSyncDelayed(int time) {
            removeMessages(2);
            sendMessageDelayed(handler.obtainMessage(2), time);
        }

        public void notifyGroupSyncNow() {
            removeMessages(3);
            sendMessageDelayed(handler.obtainMessage(3), 20);

        }
    }

    private volatile boolean syncing = false;

    /**
     * 同步群信息
     *
     * @param conversationId
     */
    private void syncGroupInfo(long conversationId) {
        if (fastSyncTime == 0) {
            fastSyncTime = System.currentTimeMillis();
        }
        Object conversion = ConvController.getInstance().getByConvId(conversationId);
        MyLog.debug(TAG, " conversion =" + conversion);
        if (conversion == null) return;
        ConvEntity convEntity = ConvParseUtil.parseConvEntity(conversion);
        syncGroupInfo(convEntity);

    }

    private void syncGroupInfo(ConvEntity convEntity) {
        long conversationId = convEntity.id;
        // if (convEntity.memberList == null || convEntity.memberList.length == 0) {
        convEntity.memberList = getGroupMembers(Global.loadPackageParam.classLoader, conversationId);
        //  convEntity.memberCount=convEntity.memberList.length;
        //   }
        // convEntityCache.put(conversationId, convEntity);
        ConvEntitySyncInfo convEntitySyncInfo = new ConvEntitySyncInfo(System.currentTimeMillis(), convEntity);
        syncInfoCache.put(conversationId, convEntitySyncInfo);
        MyLog.debug(TAG, " syncGroupInfo group name " + convEntity.name + " " + convEntity.memberCount);
        List<Long> needUpdateUserList = new ArrayList<>();
        for (int i = 0; i < convEntity.memberList.length; i++) {
            if (TextUtils.isEmpty(convEntity.memberList[i].name) && convEntity.memberList[i].userRemoteId > 0) {//可以没有 头像 但要有name
                if (memberEntityCache.containsKey(convEntity.memberList[i].userRemoteId)) {
                    MemberInfo memberInfo = memberEntityCache.get(convEntity.memberList[i].userRemoteId);
                    convEntity.memberList[i].name = memberInfo.n;
                    convEntity.memberList[i].avatorUrl = memberInfo.a;
                    convEntity.memberList[i].gender = memberInfo.g;
                } else {
                    //需要更新 这个好友的信息
                    // todo 取消好友信息同步
                    needUpdateUserList.add(convEntity.memberList[i].userRemoteId);

                }

            }
        }
        convEntityCache.remove(conversationId);
        //如果不需要 直接提交 群信息
        if (needUpdateUserList.size() == 0) {
            ProtocalManager.getInstance().sendConvListInfo(convEntity);
            syncing = false;
            handler.notifyGroupSyncNow();
            MyLog.debug(TAG, " syncGroupInfo group  立即同步 下一个group  ");
        } else {
            //先更新 然后推后 上传任务
            updateUserListByDB(conversationId, convEntity, needUpdateUserList);
            syncing = false;
            handler.notifyGroupSyncDelayed(getDelayedTime(needUpdateUserList));
            MyLog.debug(TAG, " syncGroupInfo group 需要更新好友 " + needUpdateUserList.size() + "同步任务延后  " + getDelayedTime(needUpdateUserList));
        }


    }

    /**
     * 上报 延时策略
     *
     * @param needUpdateUserList
     * @return
     */
    private int getDelayedTime(List<Long> needUpdateUserList) {
        int needWaitTime = needUpdateUserList.size() * 10;
        if (needWaitTime > 2 * syncTime) {
            return syncTime;//最长延时 倍
        } else if (needWaitTime > syncTime) {
            return needWaitTime / 2;
        } else {
            return needWaitTime / 3;
        }
        //   return 20;
    }

    /**
     * 数据库更新 群成员的信息
     *
     * @param conversationId
     * @param convEntity
     * @param needUpdateUserList
     */
    private void updateUserListByDB(long conversationId, ConvEntity convEntity, List<Long> needUpdateUserList) {
        Global.postRunnable2UI(new Runnable() {
            @Override
            public void run() {

                ConvController.getInstance().getUserObjectFromDBByIds(needUpdateUserList, new ConvController.GetUserCallback() {//数据库获取
                    @Override
                    public void onResult(int code, List<UserEntity> list) {
                        for (int i = 0; i < list.size(); i++) {
                            UserEntity userEntity = list.get(i);
                            for (ConvMember member : convEntity.memberList) {
                                if (member.userRemoteId == userEntity.remoteId) {
                                    if (!TextUtils.isEmpty(userEntity.name)) {
                                        needUpdateUserList.remove(member.userRemoteId);
                                    }

                                    member.name = userEntity.name;
                                    member.avatorUrl = userEntity.avatorUrl;
                                    member.gender = userEntity.gender;
                                    member.userCorpId = userEntity.corpid;
                                    //  MyLog.debug(TAG, convEntity.name + " 数据库 更新用户信息成功 name=" + userEntity.name + " avatorUrl =" + userEntity.avatorUrl + " gender =" + userEntity.gender);
                                }
                            }

                        }
                        if (needUpdateUserList.size() > 0) {//需要网络更新 暂时不加
                            //updateUserListByIntNet(conversationId,convEntity,needUpdateUserList);
                            ProtocalManager.getInstance().sendConvListInfo(convEntity);
                        } else {//否则直接提交全部数据
                            ProtocalManager.getInstance().sendConvListInfo(convEntity);
                        }


                    }
                });


            }
        });

    }
    /**
     * 网络更新 群成员的信息
     *
     * @param conversationId
     * @param convEntity
     * @param needUpdateUserList
     */
//    private void updateUserListByIntNet(long conversationId, ConvEntity convEntity, List<Long> needUpdateUserList) {
//
//        if (needUpdateUserList.size()>0){
//            long[] ids = new long[needUpdateUserList.size()];
//
//            for (int i = 0; i < needUpdateUserList.size(); i++) {
//                ids[i] = needUpdateUserList.get(i);
//            }
//            ConvController.getInstance().refreshUserObjectInMainThread(Global.loadPackageParam.classLoader, ids, conversationId, new ProxyUtil.ProxyUserResultCallBack() {
//                @Override
//                public void onResult(int code, Object[] userObj) {
//                    if (code == 0) {
//                        MyLog.debug(TAG, " 更新用户信息成功  " + StrUtils.objectToJson(userObj));
//                        Object IContactItems = Array.newInstance(RobotHelpers.findClass(KeyConst.I_IContactItem, Global.loadPackageParam.classLoader), userObj.length);
//                        StringBuilder sb2 = new StringBuilder();
//                        List<String> arrayList = new ArrayList<>();
//                        Object IContact = RobotHelpers.callStaticMethod(RobotHelpers.findClass(KeyConst.C_ICONTACT_CC, Global.loadPackageParam.classLoader), KeyConst.M_ICONTACT_CC_GET);
//                        Object ContactService = RobotHelpers.callStaticMethod(RobotHelpers.findClass(KeyConst.C_ContactService, Global.loadPackageParam.classLoader),  KeyConst.M_ContactService_getService);
//                        for (int i = 0; i < userObj.length; i++) {
//                            Object info = RobotHelpers.callMethod(userObj[i], "getInfo");
//                            long remoteId = RobotHelpers.getLongField(info, "remoteId");
//                            for (ConvMember member : convEntity.memberList) {
//                                if (member.userRemoteId == remoteId) {
//                                    member.name = (String) RobotHelpers.getObjectField(info, "name");
//                                    member.avatorUrl = (String) RobotHelpers.getObjectField(info, "avatorUrl");
//                                    member.gender = RobotHelpers.getIntField(info, "gender");
//                                    member.corpName = (String) RobotHelpers.callMethod(userObj[i], "getCorpName");
//                                    MyLog.debug(TAG, convEntity.name + " 网络 更新用户信息成功 name=" + member.name + " avatorUrl =" + member.avatorUrl + " gender =" + member.gender);
//                                    memberEntityCache.put(remoteId, new MemberInfo(member.name, member.avatorUrl, member.gender));
//                                }
//                            }
//                            Object user = RobotHelpers.callMethod(ContactService, KeyConst.M_ContactService_RefreshExtraUserInfo, userObj[i]);
//                            //IContact.CC.get().initContactItem
//                            Object initContactItem = RobotHelpers.callMethod(IContact, KeyConst.M_ICONTACT_initContactItem, 1, userObj[i], false);
//                            Array.set(IContactItems, i, initContactItem);
//                            //updateMemberCache
//                        }
//                        Object contactManager = RobotHelpers.callStaticMethod(RobotHelpers.findClass(KeyConst.C_ICONTACTMANAGER_CC, Global.loadPackageParam.classLoader), KeyConst.M_ICONTACTMANAGER_CC);
//                        RobotHelpers.callMethod(contactManager, KeyConst.M_ICONTACTMANAGER_updateMemberCache, IContactItems, sb2, arrayList);
//                        MyLog.debug(TAG, " 网络更新 更新缓存 " + sb2 + " Array =" + Arrays.toString(arrayList.toArray()));
//                        ProtocalManager.getInstance().sendConvListInfo(convEntity);
//                    } else {
//                        //更新失败
//                        convEntityCache.remove(conversationId);
//                        MyLog.debug(TAG, "网络更新  用户信息失败了 " + convEntity.name + " 需要更新的 size " + needUpdateUserList.size() + " Array =" + Arrays.toString(ids), true);
//                    }
//                }
//            });
//        }
//    }

    /**
     * 本地获取成员信息
     *
     * @param classLoader
     * @param conversationId
     * @return
     */
    public ConvMember[] getGroupMembers(ClassLoader classLoader, long conversationId) {
        try {
            Object instance = RobotHelpers.callStaticMethod(RobotHelpers.findClass(KeyConst.C_GroupSettingEngine, classLoader), KeyConst.M_GroupSettingEngine_getInstance);
            Object conversationEngine = RobotHelpers.callStaticMethod(RobotHelpers.findClass(KeyConst.C_ConversationEngine, classLoader), KeyConst.M_ConversationEngine_getInstance);
            RobotHelpers.callMethod(instance, KeyConst.M_GROUPSETTINGENGIN_SETCONVERSATION, conversationId);
            List convMembers = (List) RobotHelpers.callMethod(instance, KeyConst.M_GROUPSETTINGENGIN_getConversationMembers);
            if (convMembers != null) {
                List<ConvMember> rMemList = new ArrayList();
                for (int i = 0; i < convMembers.size(); i++) {
                    if (i < convMembers.size()) {
                        Object item = convMembers.get(i);
                        if (item == null) continue;
                        ConvMember cvMemberEntity = ConvParseUtil.parseConvMemberEntity(item);
                        Object user = RobotHelpers.callMethod(conversationEngine, KeyConst.M_ConversationEngine_USER, RobotHelpers.getLongField(item, KeyConst.F_CONVERSATIONENGINE_USER_USERREMOTEID));
                        if (user != null) {
                            Object info = RobotHelpers.callMethod(user, KeyConst.M_ConversationEngine_USER_getInfo);
                            if (TextUtils.isEmpty(cvMemberEntity.name) || TextUtils.isEmpty(cvMemberEntity.avatorUrl)) {

                                cvMemberEntity.name = (String) RobotHelpers.getObjectField(info, KeyConst.F_ConversationEngine_USER_name);
                                cvMemberEntity.avatorUrl = (String) RobotHelpers.getObjectField(info, KeyConst.F_ConversationEngine_USER_avatorUrl);
                                cvMemberEntity.gender = RobotHelpers.getIntField(info, KeyConst.F_ConversationEngine_USER_gender);
                                cvMemberEntity.corpName = (String) RobotHelpers.callMethod(user, KeyConst.F_ConversationEngine_USER_getCorpName);
                                // MyLog.debug(TAG, " syncGroupInfo " + StrUtils.objectToJson(cvMemberEntity));

                            }
                        }
                        rMemList.add(cvMemberEntity);
                    }
                }
                return rMemList.toArray(new ConvMember[rMemList.size()]);
            }
            return new ConvMember[0];
        } catch (Throwable e) {
            e.printStackTrace();
            return new ConvMember[0];
        }
    }

    public class MemberInfo {
        /**
         * 名称
         */
        public String n;
        /**
         * 头像
         */
        public String a;
        /**
         * 性别
         */
        public int g;

        public MemberInfo(String n, String a, int g) {
            this.n = n;
            this.a = a;
            this.g = g;
        }
    }

    public class ConvEntitySyncInfo {
        public long time;
        public String md5;

        public ConvEntitySyncInfo(long time, ConvEntity convEntity) {
            this.time = time;
            this.md5 = convEntity.getMD5();
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj == null) return false;
            if (!(obj instanceof ConvEntitySyncInfo)) {
                return false;
            }
            ConvEntitySyncInfo info = (ConvEntitySyncInfo) obj;
            if (info.md5.equals(md5) && Math.abs(time - info.time) < syncHZTime) {// 内容一样并且时间不到更新频率
                return true;
            }
            //内容不一样 时间长的
            //内容不一样 时间短的
            //内容一样时间长的
            return false;
        }

    }
}
