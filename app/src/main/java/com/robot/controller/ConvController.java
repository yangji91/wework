package com.robot.controller;

import android.os.Looper;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.robot.common.StatsHelper;
import com.robot.common.Global;
import com.robot.controller.db.DBController;
import com.robot.entity.ConvEntity;
import com.robot.entity.GroupData;
import com.robot.entity.UserEntity;
import com.robot.hook.KeyConst;
import com.robot.hook.MsgCache;
import com.robot.hook.msg.HookReCallMsgMethod;
import com.robot.hook.util.ConvParseUtil;
import com.robot.netty.GroupManager;
//import com.robot.netty.handle.imple.runnable.MessageForwardRunnable;
import com.robot.com.BuildConfig;
import com.robot.com.database.service.WeWorkConversationService;
import com.robot.nettywss.WssProtocalManager;
import com.robot.util.CodedOutputByteBufferNanoUtil;
import com.robot.util.MyLog;
import com.robot.util.ProxyUtil;
import com.robot.util.StrUtils;
import com.robot.util.UserParseUtil;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.robot.robothook.RobotHelpers;

import static com.robot.common.Global.loadPackageParam;

/**
 * 会话管理
 */
public class ConvController {
    private static final String TAG = "ConvController";
    private static ConvController instance;
    private Map<Long, Long> mRemoteMap;
    private Map<Long, Long> mConvMap;
    private int count;
    private MsgCache msgCache;
    private MsgCache forwardMsgCache;
    private final int FLAG_REPORT = 0x100;
    private boolean isContactLoaded;

    private ConvController() {
        this.mRemoteMap = new ConcurrentHashMap<>();
        this.mConvMap = new ConcurrentHashMap<>();
        this.forwardMsgCache = new MsgCache(100);
        this.isContactLoaded = false;
    }

    public synchronized static final ConvController getInstance() {
        if (instance == null) {
            instance = new ConvController();
        }
        return instance;
    }

    public void put(Long remoteId, Long convId, long convObj) {
        mRemoteMap.put(remoteId, convObj);
        mConvMap.put(convId, convObj);
    }

    public void loadContactComplete() {
        this.isContactLoaded = true;
    }

    public boolean isContactLoaded() {
        return this.isContactLoaded;
    }

    public Object getByRemoteId(Long remoteId) {
        Object get = RobotHelpers.callStaticMethod(RobotHelpers.findClass(KeyConst.C_CONVERSATION_CC, loadPackageParam.classLoader), KeyConst.M_CONVERSATION_CC_GET);
        Object itemByRemoteId = RobotHelpers.callMethod(get, KeyConst.M_CONVERSATION_GETCONVERSATIONITEMBYREMOTEID, remoteId);
        if (itemByRemoteId != null) {
//            StatsHelper.event("msgReport1", "callback", "itemByRemoteId != null " + remoteId);
            MyLog.debug(TAG, "[handleItem]" + "itemByRemoteId != null " + remoteId, false);
            return RobotHelpers.callMethod(itemByRemoteId, KeyConst.M_CONVERSATION_GETCONVERSATIONITEM);
        }
//        StatsHelper.event("msgReport1", "callback", "getByRemoteId remoteId " + remoteId);
        MyLog.debug(TAG, "[handleItem]" + "getByRemoteId remoteId " + remoteId, false);
        if (remoteId == null) return null;
        Long conversationID = mRemoteMap.get(remoteId);
//        StatsHelper.event("msgReport1", "callback", "getByRemoteId conversationID" + conversationID);
        MyLog.debug(TAG, "[handleItem]" + "getByRemoteId conversationID" + conversationID, false);
        if (conversationID == null)
            return null;
        return getConversationObjectFromCache(loadPackageParam.classLoader, conversationID);
    }

    public ConvEntity getConvEntityByRemoteId(Long remoteId) {
        Object obj = getByRemoteId(remoteId);
        if (obj != null) {
            ConvEntity convEntity = ConvParseUtil.parseConvEntity(obj);
            MyLog.debug(TAG, "[handleItem]" + "getConvEntityByRemoteId..." + convEntity);
            return convEntity;
        }
        return null;
    }

    public Object getByConvId(Long convId) {

        Long conversationID = mConvMap.get(convId);
        if (conversationID == null) {
            conversationID = mRemoteMap.get(convId);
        }
        if (conversationID == null) conversationID = convId;
        // MyLog.debug(TAG, "conversationID  =" + conversationID + " convId=" + convId);
        return getConversationObjectFromCache(loadPackageParam.classLoader, conversationID);
    }

    public int getConvSize() {
        return mConvMap.size();
    }

    /***
     * 心跳
     */
    public void onHeartBeat() {
        count++;    //6秒 * 10  = 1分钟
        MyLog.debug(TAG, "[onHeartBeat]" + " count:" + count);
        if (count % (10 * 600) == 0 && count != 0) {   //600分钟上报一次 150次心跳会上报
            count = 0;
            MyLog.debug(TAG, "[onHeartBeat]" + "上报会话列表...");
            //获取会话列表 心跳上传会话
            getAndSendConvInfo();
        }
        // 撤回消息 上报
        HookReCallMsgMethod.getInstance().checkMsgRevoke();
    }

    public void getAndSendConvInfo() {
        String sql = " select * from CONVERSATION where conversationtype =1";
        DBController.getInstance().execSql(loadPackageParam.classLoader, sql, DBController.getInstance().getInfoDBPath(loadPackageParam.classLoader), new DBController.IExecSqlCallback() {
            @Override
            public void onResult(String errMsg, long execTime, List<Map<String, String>> resultMap) throws UnsupportedEncodingException {
                Global.postRunnableDownLoadImg(new Runnable() {
                    @Override
                    public void run() {
                        List<ConvEntity> list = new ArrayList<>();
                        if (resultMap != null && resultMap.size() > 0) {
                            for (int i = 0; i < resultMap.size(); i++) {
                                Map<String, String> map = resultMap.get(i);
                                ConvEntity convEntity = null;
                                try {
                                    convEntity = transToConv(map);
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                                ConvController.getInstance().put(convEntity.remoteId, convEntity.id, convEntity.id);
                                list.add(convEntity);
                            }
                            //NanoHttpClient.cacheConv(list);
                            sendConvInfoList(list);
                        }
                    }
                });

            }
        });
        /*Class clazzConvService = XposedHelpers.findClassIfExists(KeyConst.C_ConversationService, loadPackageParam.classLoader);
        Object convService = XposedHelpers.callStaticMethod(clazzConvService, KeyConst.M_ConversationService_getService);
        Object[] conversationList = (Object[]) XposedHelpers.callMethod(convService, "GetConversationList", new Class[]{XposedHelpers.findClass("com.tencent.wework.foundation.model.Conversation", loadPackageParam.classLoader), int.class}, null, -1);
        MyLog.debug(TAG, "[conversationList]" + " result ok :" + Array.getLength(conversationList));
        HookBaseMethod hookBaseMethod = HookMethodEnum.CONV_LIST.getMethod();
        hookBaseMethod.onInvokeMethod(null, loadPackageParam, new IHookCallBack<List<ConvEntity>>() {
            @Override
            public void onCall(ResEntity<List<ConvEntity>> resEntity) {
                MyLog.debug(TAG, "[onInvokeMethod]" + " result ok:" + resEntity.isSucc());
                if (resEntity.isSucc()) {
                    final List<ConvEntity> dataList = resEntity.getData();
                    MyLog.debug(TAG, "[onInvokeMethod]" + " dataList size =" + dataList.size());

                }
            }

            @Override
            public List<ConvEntity> getParams() {
                return null;
            }
        });*/
    }

    private ConvEntity transToConv(Map<String, String> map) throws UnsupportedEncodingException {
        ConvEntity convEntity = new ConvEntity();
        convEntity.remoteId = Long.valueOf(map.get("RID"));
        convEntity.id = Long.valueOf(map.get("LID"));
        convEntity.type = Integer.valueOf(map.get("conversationtype"));
        convEntity.name = map.get("name");
        convEntity.creatorId = Long.valueOf(map.get("creater_id"));
        convEntity.createTime = Long.valueOf(map.get("create_time"));
        convEntity.isExternalGroup = map.get("RID").startsWith("106");
        convEntity.isGroupOwner = convEntity.creatorId == LoginController.getInstance().getLoginUserId();
        convEntity.modifyTime = Long.valueOf(map.get("modify_time"));
        convEntity.searchTime = Long.valueOf(map.get("search_time"));
        convEntity.exited = "1".equals(map.get("exited"));
        convEntity.memberList = GroupManager.getInstance().getGroupMembers(loadPackageParam.classLoader, convEntity.id);
        convEntity.memberCount = convEntity.memberList == null ? 0 : convEntity.memberList.length;
        return convEntity;
    }

    public void sendConvInfoList(List<ConvEntity> dataList) {
        if (dataList == null) return;
        WeWorkConversationService.getInstance().save(dataList);
        List<ConvEntity> contactList = new ArrayList<>();
        List<ConvEntity> groupList = new ArrayList<>();
        for (ConvEntity convEntity : dataList) {
            if (convEntity.isExternalGroup || convEntity.isInnerGroup()) {
                groupList.add(convEntity);
            } else {
                contactList.add(convEntity);
            }
        }
        MyLog.debug(TAG, "[synchrConversationList]" + " groupList.size:" + groupList.size());
        MyLog.debug(TAG, "[synchrConversationList]" + " contactList.size:" + contactList.size());
        if (groupList.size() > 0) {
            WssProtocalManager.sendConvListInfo(groupList);
        }
//        //群会话单独处理
//        for (ConvEntity convEntity : groupList) {
//            // ProtocalManager.getInstance().sendConvListInfo(convEntity);
        //todo 各种缓存需要吗？
//            GroupManager.getInstance().sendGroupInfo(convEntity.id);
//        }
//        if (contactList.size() > 100) {
//            List<List> sList = SpliteUtil.splistList(contactList, 50);
//            if (sList != null && sList.size() > 0) {
//                for (List<ConvEntity> ll : sList) {
//                    WssProtocalManager.sendConvListInfo(ll);
//                }
//            }
//        } else {
//            WssProtocalManager.sendConvListInfo(contactList);
//            MyLog.debug(TAG, "[synchrConversationList]" + " size:" + contactList.size());
//        }

    }




    /**
     * 根据userid 渠道用户UserObject
     *
     * @param classLoader             类加载器
     * @param userRemoteId            用户id
     * @param proxyUserResultCallBack 回调接口
     */
    public void getUserObject(final ClassLoader classLoader, long userRemoteId, final ProxyUtil.ProxyUserResultCallBack proxyUserResultCallBack) {
        final long[] uids = new long[]{userRemoteId};
        if (Looper.myLooper() == Looper.getMainLooper()) {
            getUserObjectInMainThread(classLoader, uids, proxyUserResultCallBack);
        } else {
            Global.postRunnable2UI(new Runnable() {
                @Override
                public void run() {
                    getUserObjectInMainThread(classLoader, uids, proxyUserResultCallBack);
                }
            });
        }


    }

    /**
     * 获取
     *
     * @param classLoader
     * @param uids
     * @param proxyUserResultCallBack
     */
    public void getUserObjectInMainThread(ClassLoader classLoader, long[] uids, ProxyUtil.ProxyUserResultCallBack proxyUserResultCallBack) {
        getUserObjectFromConversationInMainThread(classLoader, uids, 0L, proxyUserResultCallBack);
    }

    /**
     * 获取
     *
     * @param classLoader
     * @param uids
     * @param proxyUserResultCallBack
     */
    public void getUserObjectFromConversationInMainThread(ClassLoader classLoader, long[] uids, long conversation, ProxyUtil.ProxyUserResultCallBack proxyUserResultCallBack) {
        Class<?> IUsermanagerClass = RobotHelpers.findClass(KeyConst.C_UserManager, classLoader);
        Object IUserManager = RobotHelpers.callStaticMethod(RobotHelpers.findClass(KeyConst.C_MK, classLoader), KeyConst.M_MK_SERVICE, IUsermanagerClass);
        MyLog.debug(" Conversation ", "[invoke]IUserManager = " + IUserManager);
        MyLog.debug(" Conversation ", " 是不是主线程 " + (Looper.myLooper() == Looper.getMainLooper()));
        RobotHelpers.callMethod(IUserManager, KeyConst.M_UserManager_getUserByIdWithScene, uids, 4, conversation, ProxyUtil.GetProxyInstance(KeyConst.I_IGetUserByIdCallback, proxyUserResultCallBack));
    }

    /**
     * 更新 用于群成员列表
     *
     * @param classLoader
     * @param uids
     * @param conversationId          群会话 id
     * @param proxyUserResultCallBack
     */
    public void refreshUserObjectInMainThread(ClassLoader classLoader, long[] uids, long conversationId, ProxyUtil.ProxyUserResultCallBack proxyUserResultCallBack) {
        Class<?> IUsermanagerClass = RobotHelpers.findClass(KeyConst.C_UserManager, classLoader);
        Object IUserManager = RobotHelpers.callStaticMethod(RobotHelpers.findClass(KeyConst.C_MK, classLoader), KeyConst.M_MK_SERVICE, IUsermanagerClass);
        MyLog.debug(" Conversation ", "[invoke]IUserManager = " + IUserManager);
        MyLog.debug(" Conversation ", " 是不是主线程 " + (Looper.myLooper() == Looper.getMainLooper()));
        RobotHelpers.callMethod(IUserManager, KeyConst.M_UserManager_refreshUserByIdWithScene, uids, 1, conversationId, ProxyUtil.GetProxyInstance(KeyConst.I_IGetUserByIdCallback, proxyUserResultCallBack));
    }

    public static void getUserObjectFromDBByIds(Collection ids, GetUserCallback getUserCallback) {
        Iterator iterator = ids.iterator();
        StringBuilder stringBuilder = new StringBuilder("(");
        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next() + ",");
        }
        String idStr = stringBuilder.subSequence(0, stringBuilder.length() - 1) + ")";
        String sql = " select * from USER where RID in  " + idStr + " ";

        DBController.getInstance().execSql(Global.loadPackageParam.classLoader, sql, DBController.getInstance().getSessionDBPath(Global.loadPackageParam.classLoader), new DBController.IExecSqlCallback() {
            @Override
            public void onResult(String errMsg, long execTime, List<Map<String, String>> resultMap) {
                List<UserEntity> list = new ArrayList<>();
                int code = 1;
                if (TextUtils.isEmpty(errMsg)) {
                    code = 0;
                    for (Map<String, String> map : resultMap) {
                        UserEntity userEntity = new UserEntity();
                        userEntity.name = map.get("name");
                        userEntity.avatorUrl = map.get("avatarurl");
                        userEntity.gender = Integer.valueOf(map.get("gender"));
                        userEntity.acctid = map.get("acctid");
                        userEntity.remoteId = Long.valueOf(map.get("RID"));
                        userEntity.corpid = Long.valueOf(map.get("corpid"));
                        userEntity.unionid = String.valueOf(map.get("unionid"));
                        list.add(userEntity);
                    }
                }
                getUserCallback.onResult(code, list);
            }
        });

    }

    /**
     * 获取当前登录的用户
     *
     * @param classLoader
     * @return
     */
    public Object getCurrentLoginUserObject(ClassLoader classLoader) {
        Class<?> IAccountClass = RobotHelpers.findClass(KeyConst.C_IAccount, classLoader);
        Object IAccount = RobotHelpers.callStaticMethod(RobotHelpers.findClass(KeyConst.C_MK, classLoader), KeyConst.M_MK_SERVICE, IAccountClass);
        Object loginUserObj = RobotHelpers.callMethod(IAccount, KeyConst.M_IACCOUNT_GETLOGINUSER);
        MyLog.debug(" Conversation ", "[invoke]" + "获取登录用户." + loginUserObj);
        return loginUserObj;
    }

    /**
     * 从缓存中获取到 ConversationItem ipk 类
     *
     * @param classLoader    类加载器
     * @param conversationID id 一般为0
     * @return
     */
    public static Object getConversationObjectFromCache(ClassLoader classLoader, long conversationID) {
        Object get = RobotHelpers.callStaticMethod(RobotHelpers.findClass(KeyConst.C_CONVERSATION_CC, loadPackageParam.classLoader), KeyConst.M_CONVERSATION_CC_GET);
        Object itemByRemoteId = RobotHelpers.callMethod(get, KeyConst.M_CONVERSATION_GETITEM, conversationID);
        //MyLog.debug(TAG, "[handleItem]" + "getConversationObjectFromCache1 conversationID" + conversationID + "", true);
        //StatsHelper.event("msgReport1", "callback", "getConversationObjectFromCache1 conversationID" + conversationID);
        if (itemByRemoteId != null) {
            return RobotHelpers.callMethod(itemByRemoteId, KeyConst.M_CONVERSATION_GETCONVERSATIONITEM);
        }
        MyLog.debug(TAG, "[handleItem]" + "getConversationObjectFromCache2 conversationID" + conversationID + "", false);
        //StatsHelper.event("msgReport1", "callback", "getConversationObjectFromCache2 conversationID" + "" + conversationID);

        final Class<?> ipi = RobotHelpers.findClass(KeyConst.C_ConversationEngine, classLoader);
        final Object ipiObj = RobotHelpers.callStaticMethod(ipi, KeyConst.M_ConversationEngine_getInstance);
        final Object ipk = RobotHelpers.callMethod(ipiObj, KeyConst.M_Conversation_Get_ConvItem, conversationID);
        // StatsHelper.event("msgReport1", "callback", "getConversationObjectFromCache3 ipk");
        MyLog.debug(TAG, "[handleItem]" + "getConversationObjectFromCache3 ipk" + "", false);
        if (ipk == null) {//获取到的是空
            //StatsHelper.event("msgReport1", "callback", "getConversationObjectFromCache4 ipk null");
            // MyLog.debug(TAG, "[handleItem]" + "getConversationObjectFromCache3 ipk" + "null", true);
            return null;
        }
        Object conversation = RobotHelpers.callMethod(ipk, KeyConst.M_CONVERSATION_GETCONVERSATIONITEM);
        if (conversation == null) {
            //StatsHelper.event("msgReport1", "callback", "getConversationObjectFromCache4 conversation null");
            //MyLog.debug(TAG, "[handleItem]" + "getConversationObjectFromCache4 conversation null", true);
            return null;
        }
        StatsHelper.event("msgReport", "reportlog", "getConversationObjectFromCache4 conversation");
        MyLog.debug(TAG, "[handleItem]" + "getConversationObjectFromCache4 conversation ", false);
        return conversation;
    }

    /**
     * 根据 会话id和 用户id 获取会话 支持会话id是0
     * 1.先从缓存中获取 获取到的话 直接返回
     * 2.缓存获取失败 创建会话
     *
     * @param classLoader    类加载器
     * @param conversationId 会话id可以是0L
     * @param userRemoteId   用户id
     * @param isGroup        是否是群(暂时不知道群的remote 判断规则  remoteId1069 是群 1688 是人)
     * @param callBack       回调接口
     */
    public void getConversationObjectByRemoteIDAndConversationId(final ClassLoader classLoader, long conversationId, long userRemoteId, boolean isGroup, final GetOrCreateConversationCallBack callBack) {

        if (conversationId != 0L) {
            Object conversation = getConversationObjectFromCache(classLoader, conversationId);
            if (conversation != null) {
                MyLog.debug(TAG, "[handlePush]" + " 通过conversation从缓存获取  conversationId:" + conversationId + " userRemoteId : " + userRemoteId, false);
                Object requestInfo = RobotHelpers.callMethod(conversation, KeyConst.M_Conversion_requestInfo);
                if (requestInfo != null) {
                    long remoteId = RobotHelpers.getLongField(requestInfo, KeyConst.F_Conversion_requestInfo_remoteId);
                    if (remoteId == userRemoteId && callBack != null) {
                        callBack.onResult(0, conversation, "从缓存获取到conversation");
                        return;
                    }
                }
            }
        }
        if (userRemoteId != 0L) {
            Object conversation = getByRemoteId(userRemoteId);
            StatsHelper.event("msgReport", "reportlog", "getByRemoteId conversationId " + conversationId + " userRemoteId : " + userRemoteId);
            if (conversation != null) {
                MyLog.debug(TAG, "[handlePush]" + "通过Remote从缓存获取到 conversationId:" + conversationId + " userRemoteId : " + userRemoteId, false);
                Object requestInfo = RobotHelpers.callMethod(conversation, KeyConst.M_Conversion_requestInfo);
                if (requestInfo != null) {
                    long remoteId = RobotHelpers.getLongField(requestInfo, KeyConst.F_Conversion_requestInfo_remoteId);
                    if (remoteId == userRemoteId && callBack != null) {
                        callBack.onResult(0, conversation, "从userRemoteId获取到conversation");
                        //StatsHelper.event("msgReport1", "callback", "fetchConversationByKey" +  conversationId + " userRemoteId : " + userRemoteId);
                        return;
                    }
                }
            }
        }
        if (isGroup) {
            fetchConversationByKey(classLoader, userRemoteId, callBack);
            MyLog.debug(TAG, "[handlePush]" + "getConversationObjectFromCache 为null conversationId:" + conversationId + " userRemoteId : " + userRemoteId, false);
//            StatsHelper.event("msgReport", "reportlog", "fetchConversationByKey" + conversationId + " userRemoteId : " + userRemoteId);
        } else {
            createConversationObjectByRemoteID(classLoader, userRemoteId, callBack);
//            StatsHelper.event("msgReport", "reportlog", "createConversationObjectByRemoteID" + conversationId + " userRemoteId : " + userRemoteId);
            MyLog.debug(TAG, "[handlePush]" + "getConversationObjectFromCache 为null conversationId:" + conversationId + " userRemoteId : " + userRemoteId, false);
        }
    }

    public void fetchConversationByKey(ClassLoader classLoader, long userRemoteId, GetOrCreateConversationCallBack callBack) {
        Global.postRunnable2UI(new Runnable() {
            @Override
            public void run() {
                Object conversionID = RobotHelpers.newInstance(RobotHelpers.findClass(KeyConst.C_ConversationID, classLoader), 1, 0L, userRemoteId);
                Object get = RobotHelpers.callStaticMethod(RobotHelpers.findClass(KeyConst.C_CONVERSATION_CC, classLoader), KeyConst.M_CONVERSATION_CC_GET);
                RobotHelpers.callMethod(get, KeyConst.M_CONVERSATION_fetchConversationByKey, conversionID,
                        ProxyUtil.GetProxyInstance(KeyConst.C_ICreateConversationCallback, new ProxyUtil.ProxyCallBack() {
                            @Override
                            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                                if (method.getName().equals(KeyConst.M_ICreateConversationCallback_onResult)) {
                                    MyLog.debug(TAG, "[onInvokeMethod]" + " 创建群会话 onResult len:" + Arrays.toString(objects), true);
                                    if ((int) objects[0] == 0) {
                                        Object conversion = objects[1];
                                        Object getService = RobotHelpers.callStaticMethod(RobotHelpers.findClass(KeyConst.C_ConversationService, classLoader), KeyConst.M_ConversationService_getService);
                                        boolean open = true;
                                        RobotHelpers.callMethod(getService, KeyConst.M_ConversationService_setConversationOpen, conversion, open, ProxyUtil.GetProxyInstance(KeyConst.C_SetConversationOpenCallback, new ProxyUtil.ProxyCallBack() {
                                            @Override
                                            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                                                if (KeyConst.M_SetConversationOpenCallback_onResult.equals(method.getName())) {
                                                    MyLog.debug(TAG, "[onInvokeMethod]" + " 打开群会话 onResult len:" + Arrays.toString(objects), true);

                                                }
                                                return null;
                                            }
                                        }));
                                        Object conversionID = RobotHelpers.newInstance(RobotHelpers.findClass(KeyConst.C_ConversationID, classLoader), conversion);
                                        Object get = RobotHelpers.callStaticMethod(RobotHelpers.findClass(KeyConst.C_CONVERSATION_CC, classLoader), KeyConst.M_CONVERSATION_CC_GET);
                                        RobotHelpers.callMethod(get, KeyConst.M_CONVERSATION__AddExternalChatWelcomeMsg, conversionID);
                                        callBack.onResult(0, conversion, "创建成功");
                                        Global.postRunnable(new Runnable() {
                                            @Override
                                            public void run() {
                                                GroupManager.getInstance().sendGroupInfo(conversion);
                                            }
                                        });
                                    } else {
                                        callBack.onResult(1, null, "创建群会话失败");
                                    }
                                }
                                return null;
                            }
                        }));
            }
        });
    }

    /**
     * 通过RemoteID 获取 ConversationObject
     *
     * @param classLoader
     * @param userRemoteId
     * @param callBack
     * @return
     */
    private void createConversationObjectByRemoteID(final ClassLoader classLoader, long userRemoteId, final GetOrCreateConversationCallBack callBack) {
        getUserObject(classLoader, userRemoteId, new ProxyUtil.ProxyUserResultCallBack() {
            @Override
            public void onResult(int code, final Object[] userObj) {
                if (code == 0) {
                    MyLog.debug(TAG, "[handlePush]" + "createConversationObjectByRemoteID 获取到用户 :" + JSON.toJSONString(userObj[0]) + " userRemoteId : " + userRemoteId, true);
                    final Class<?> ipi = RobotHelpers.findClass(KeyConst.C_ConversationEngine, classLoader);
                    final Object user = userObj[0];
                    Object[] userObjArray = (Object[]) Array.newInstance(userObj[0].getClass(), 2);
                    userObjArray[0] = user;
                    userObjArray[1] = getCurrentLoginUserObject(classLoader);
                    RobotHelpers.callStaticMethod(ipi, KeyConst.M_ConversationEngine_a, new Class[]{
                            String.class, userObjArray.getClass(), userObjArray.getClass(), long.class, byte[].class,
                            RobotHelpers.findClass(KeyConst.C_IConversation_a, classLoader)
                            , RobotHelpers.findClass(KeyConst.I_ICommonConversationOperateCallback, classLoader)
                    }, "", userObjArray, null, 0, null, null, ProxyUtil.GetProxyInstance(KeyConst.I_ICommonConversationOperateCallback, new ProxyUtil.ProxyCallBack() {
                        @Override
                        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                            if (method.getName().equals(KeyConst.M_ICommonConversationOperateCallback_onResult)) {
                                int code = (int) objects[0];
                                MyLog.debug(TAG, "[handlePush]" + "创建私聊会话 :" + " userRemoteId : " + userRemoteId + "  参数" + Arrays.toString(objects), true);
                                if (code == 0) {
                                    Object conversation = objects[1];
                                    saveConversationCache(conversation, classLoader);
                                    callBack.onResult(0, conversation, "");
                                } else {
                                    String message = objects[2] + "";
                                    //todo 没有找到用户
                                    callBack.onResult(code, null, message);
                                }
                            }
                            return null;
                        }
                    }));
                } else {
                    MyLog.debug(TAG, "[handlePush]" + "createConversationObjectByRemoteID 没有获取到用户 :" + " userRemoteId : " + userRemoteId, true);
                    //todo 没有找到用户
                    callBack.onResult(code, null, "没有找到用户");
                }
            }
        });
    }



    /**
     * 更新缓存
     *
     * @param conversation
     * @param classLoader
     */
    private void saveConversationCache(Object conversation, ClassLoader classLoader) {
        Object conObj = RobotHelpers.callMethod(conversation, KeyConst.M_Conversation_getInfo);
        long coversationId = RobotHelpers.getLongField(conObj, KeyConst.F_Conversation_getInfo_id);
        Object conversationCache = getConversationObjectFromCache(classLoader, coversationId);
        if (conversationCache == null) {//缓存不存在的情况 创建会话
            StatsHelper.event("msgReport", "reportlog", "直接创建的 更新企微缓存:" + JSON.toJSONString(conversation));
            MyLog.debug(TAG, "[handlePush]" + "直接创建的 更新企微缓存:" + JSON.toJSONString(conversation) + " coversationId : " + coversationId, true);
            //  createConversation(user,callBack);
            Object array = Array.newInstance(conversation.getClass(), 1);
            Array.set(array, 0, conversation);
            final Class<?> ipi = RobotHelpers.findClass(KeyConst.C_ConversationEngine, classLoader);
            final Object ipiObj = RobotHelpers.callStaticMethod(ipi, KeyConst.M_ConversationEngine_getInstance);
            RobotHelpers.callMethod(ipiObj, KeyConst.M_ConversationEngine_updateConversationCache, array);
        } else {
            StatsHelper.event("msgReport", "reportlog", "conversationCache != null ");
            MyLog.debug(TAG, "[handlePush]" + "conversationCache != null : ");
        }
    }

    /**
     * 获取 外部联系人列表
     *
     * @param getUserCallback
     */
    public void getOuterContact(GetUserCallback getUserCallback) {
        MyLog.debug(TAG, "  getOuterContact ");
//        Global.postRunnable2UI(new Runnable() {
//            @Override
//            public void run() {
//
//                Class clazzContactService = RobotHelpers.findClassIfExists(KeyConst.C_ContactService, loadPackageParam.classLoader);
//                Object objInstance = RobotHelpers.callStaticMethod(clazzContactService, KeyConst.M_ContactService_getService);
//                Class clazzCall = RobotHelpers.findClassIfExists(KeyConst.I_IGetUserByIdCallback, loadPackageParam.classLoader);
//                Object objCallBack = Proxy.newProxyInstance(loadPackageParam.classLoader, new Class[]{clazzCall}, new InvocationHandler() {
//                    @Override
//                    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
//                        if (method.getName().equals(KeyConst.M_IGetUserByIdCallback_onResult)) {
//                            MyLog.debug(TAG, "  getOuterContact finish " + Arrays.toString(args));
//                            List<UserEntity> userList = new ArrayList<>();
//                            if ((int) args[0] == 0) {
//                                Object[] users = (Object[]) args[1];
//                                if (users != null && users.length > 0) {
//                                    for (Object obj : users) {
//                                        UserEntity userEntity = UserParseUtil.parseUserModel(obj);
//                                        long remoteId = userEntity.remoteId;
//                                        Global.setUserObject(remoteId, obj);
//                                        userList.add(userEntity);
//                                        if (BuildConfig.customConfigLog) {
//                                            MyLog.debug(TAG, "[afterHookedMethod]" + " name:" + userEntity.name + " remoteid:" + remoteId + "faceUrl:" + userEntity.avatorUrl + " unionId:" +
//                                                    userEntity.unionid + " openId:" + userEntity.wechatOpenId + " gender:" + userEntity.gender
//                                                    + " remark:" + userEntity.realRemark + " addTime:" + userEntity.customerAddTime + " updateTime:" + userEntity.customerUpdateTime
//                                                    + " customerDesc:" + userEntity.customerDescription);
//                                        }
//                                    }
//                                }
//                            }
//                            getUserCallback.onResult(0, userList);
//                        }
//                        return null;
//                    }
//                });
//                Class[] clazzArray = {int.class, clazzCall};
//                Object[] objArray = {18, objCallBack};
//                RobotHelpers.callMethod(objInstance, KeyConst.M_OUTER_CONTACTS, clazzArray, objArray);
//            }
//        });

        Global.postRunnable2UI(new Runnable() {
            @Override
            public void run() {

                Class clazzContactService = RobotHelpers.findClassIfExists(KeyConst.C_ContactService, loadPackageParam.classLoader);
                Object objInstance = RobotHelpers.callStaticMethod(clazzContactService, KeyConst.M_ContactService_getService);
//                Class clazzCall = RobotHelpers.findClassIfExists("com.tencent.wework.foundation.callback.IGetCorpListAndUserListCallback", loadPackageParam.classLoader);
//                Object objCallBack = Proxy.newProxyInstance(loadPackageParam.classLoader, new Class[]{clazzCall}, new InvocationHandler() {
//                    @Override
//                    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
//                        if (method.getName().equals(KeyConst.M_IGetUserByIdCallback_onResult)) {
//                            MyLog.debug(TAG, "  GetMatchedContactListByCorpAZOrder finish " + Arrays.toString(args));
//                            List<UserEntity> userList = new ArrayList<>();
//                            if ((int) args[0] == 0) {
//                                Object[] users = (Object[]) args[2];
//                                if (users != null && users.length > 0) {
//                                    for (Object obj : users) {
//                                        UserEntity userEntity = UserParseUtil.parseUserModel(obj);
//                                        long remoteId = userEntity.remoteId;
//                                        Global.setUserObject(remoteId, obj);
//                                        userList.add(userEntity);
//                                        if (BuildConfig.customConfigLog) {
//                                            MyLog.debug(TAG, "[afterHookedMethod]" + " name:" + userEntity.name + " remoteid:" + remoteId + "faceUrl:" + userEntity.avatorUrl + " unionId:" +
//                                                    userEntity.unionid + " openId:" + userEntity.wechatOpenId + " gender:" + userEntity.gender
//                                                    + " remark:" + userEntity.realRemark + " addTime:" + userEntity.customerAddTime + " updateTime:" + userEntity.customerUpdateTime
//                                                    + " customerDesc:" + userEntity.customerDescription);
//                                        }
//                                    }
//                                }
//                            }
//                            getUserCallback.onResult(0, userList);
//                        }
//                        return null;
//                    }
//                });
//                Class[] clazzArray = {int.class, clazzCall};
//                Object[] objArray = {18, objCallBack};
                Object[] getExtraContactList = (Object[]) RobotHelpers.callMethod(objInstance, "GetCachedContactList");
                if (getExtraContactList != null) {
                    MyLog.debug(TAG, "  getOuterContact GetCachedContactList " + getExtraContactList.length);
//                Arrays.toString(getExtraContactList);
                    List<UserEntity> userList = new ArrayList<>();
                    if (getExtraContactList != null && getExtraContactList.length > 0) {
                        for (Object obj : getExtraContactList) {
                            UserEntity userEntity = UserParseUtil.parseUserModel(obj);
                            long remoteId = userEntity.remoteId;
                            Global.setUserObject(remoteId, obj);
                            userList.add(userEntity);
                            if (BuildConfig.customConfigLog) {
                                MyLog.debug(TAG, "[afterHookedMethod]" + " name:" + userEntity.name + " remoteid:" + remoteId + "faceUrl:" + userEntity.avatorUrl + " unionId:" +
                                        userEntity.unionid + " openId:" + userEntity.wechatOpenId + " gender:" + userEntity.gender
                                        + " remark:" + userEntity.realRemark + " addTime:" + userEntity.customerAddTime + " updateTime:" + userEntity.customerUpdateTime
                                        + " customerDesc:" + userEntity.customerDescription);
                            }
                        }
                    }
                    getUserCallback.onResult(0, userList);
                }
            }
        });
    }

    /**
     * @param classLoader
     * @param conversationId
     * @param userRemoteId
     * @param callback
     */
    public void getUserByIdWithConversation(ClassLoader classLoader, long conversationId, long userRemoteId, ProxyUtil.ProxyUserResultCallBack callback) {
        Global.postRunnable2UI(new Runnable() {
            @Override
            public void run() {
                Class clazzUserManager = RobotHelpers.findClassIfExists(KeyConst.C_UserManager_CC, classLoader);
                Object objUserManager = RobotHelpers.callStaticMethod(clazzUserManager, KeyConst.C_UserManager_CC_get);
                //开始调用该

                Object[] objArray = {userRemoteId, conversationId, ProxyUtil.GetProxyInstance(KeyConst.I_IGetUserByIdCallback, callback)};
                RobotHelpers.callMethod(objUserManager, KeyConst.M_UserManager_getgetUserByIdWithConversation, objArray);
            }
        });
    }

    public void getMessageByAppId(ClassLoader classLoader, String appinfo, long time, long uin, MessageCallback callback) {
        Global.postRunnable2UI(new Runnable() {
            @Override
            public void run() {
                Class clazzConvService = RobotHelpers.findClassIfExists(KeyConst.C_ConversationService, classLoader);
                Object convService = RobotHelpers.callStaticMethod(clazzConvService, KeyConst.M_ConversationService_getService);
                RobotHelpers.callMethod(convService, KeyConst.M_ConversationService_GetMessageByAppInfo, appinfo, time, uin, ProxyUtil.GetProxyInstance(KeyConst.I_IPickMessageCallback, new ProxyUtil.ProxyCallBack() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                        if ("onResult".equals(method.getName())) {
                            if (callback != null) {
                                MyLog.debug(TAG, "appinfo =" + appinfo + " \n code =" + objects[0] + " message " + StrUtils.objectToJson(objects[1]));
                                callback.onResult((int) objects[0], objects[1]);
                            }
                        }
                        return null;
                    }
                }));
            }
        });


    }


    /**
     * 根据 名称和头像匹配好友
     *
     * @param name
     * @param avatorUrl
     * @param callback
     */
    public void getUserByNameAndHead(String name, String avatorUrl, GetUserCallback callback) {
        Global.postRunnable2UI(new Runnable() {
            @Override
            public void run() {
                String sql = "select * from USER  where name =\"" + name + "\" ";
                if (!TextUtils.isEmpty(avatorUrl)) {
                    sql += " and avatarurl = \"" + avatorUrl + "\"";
                }
                DBController.getInstance().execSql(loadPackageParam.classLoader, sql, DBController.getInstance().getSessionDBPath(loadPackageParam.classLoader), new DBController.IExecSqlCallback() {
                    @Override
                    public void onResult(String errMsg, long execTime, List<Map<String, String>> resultMap) {
                        try {
                            if (TextUtils.isEmpty(errMsg) && resultMap.size() == 1) {
                                UserEntity userEntity = new UserEntity();
                                Map<String, String> map = resultMap.get(0);
                                userEntity.remoteId = Long.valueOf(map.get("RID"));
                                ArrayList<UserEntity> userEntities = new ArrayList<>();
                                userEntities.add(userEntity);
                                callback.onResult(0, userEntities);
                            } else {
                                callback.onResult(1, null);
                            }
                        } catch (Exception e) {
                            MyLog.debug(TAG, "getByUserRemoteId  " + MyLog.getThrowableTask(e));
                            callback.onResult(1, null);
                        }

                    }
                });
            }
        });

    }

    public static interface GetUserCallback {
        public void onResult(int code, List<UserEntity> list);
    }

    public static interface StringCallback {
        public void onResult(int code, String json);
    }

    public static interface MessageCallback {
        public void onResult(int code, Object message);
    }

    public static interface GetOrCreateConversationCallBack {
        public void onResult(int code, Object conversationObject, String message);
    }

    public static interface ICallback<T> {
        public void onResult(int code, String message, T data);
    }

    public static interface DismissGroupCallBack {
        public void onResult(int code, Object conversationObject, String message);
    }


}
