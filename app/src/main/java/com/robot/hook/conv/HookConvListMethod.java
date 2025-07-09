package com.robot.hook.conv;

import android.os.Handler;
import android.os.Message;

import com.robot.common.Global;
import com.robot.controller.ConvController;
import com.robot.entity.ActionResultEnum;
import com.robot.entity.ConvEntity;
import com.robot.entity.ResEntity;
import com.robot.hook.KeyConst;
import com.robot.hook.base.HookBaseMethod;
import com.robot.hook.base.IHookCallBack;
import com.robot.hook.util.ConvParseUtil;
import com.robot.util.MyLog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import com.robot.robothook.RobotHelpers;
import com.robot.robothook.LoadPackageParam;

/**
 * 主动获取会话列表
 */
public class HookConvListMethod extends HookBaseMethod<List<ConvEntity>> {

    private final int FLAG_LOAD_MORE = 0x1;

    @Override
    public void onHookInfo(Class clazz, LoadPackageParam loadPackageParam, IHookCallBack<List<ConvEntity>> callBack) {

    }

    private IHookCallBack<List<ConvEntity>> paramCall;

    @Override
    public boolean onInvokeMethod(Class classDb, LoadPackageParam loadPackageParam, IHookCallBack<List<ConvEntity>> paramCall) {
        boolean succ = true;
        try {
            this.paramCall = paramCall;
            Class clazzConvService = RobotHelpers.findClassIfExists(KeyConst.C_ConversationService, loadPackageParam.classLoader);
            Object convService = RobotHelpers.callStaticMethod(clazzConvService, KeyConst.M_ConversationService_getService);
            MyLog.debug(TAG, "[onInvokeMethod]" + " convService -> " + convService + " thread:" + Thread.currentThread().getName());
            if (convService != null) {
                //先回调首页部分
                //step_02_get_conversions(null, loadPackageParam, paramCall, getConvService());
                //
                boolean isLoadMore = (boolean) RobotHelpers.callMethod(getConvService(), KeyConst.M_ConversationService_MaybeHasMoreConversation);
                MyLog.debug(TAG, "[onInvokeMethod]" + "是否继续加载更多页:" + isLoadMore);
                if (isLoadMore) {
                    Message msg = Message.obtain();
                    msg.what = FLAG_LOAD_MORE;
                    mHandler.sendMessage(msg);
                }
            } else {
                ResEntity resEntity = ResEntity.genErr(ActionResultEnum.ACTION_CLIENT_ERR.getMsg());
                paramCall.onCall(resEntity);
            }
        } catch (Exception e) {
            MyLog.error(TAG, e);
        }
        return succ;
    }

    private int page = 0;
    private final int MAX_CNT = 500; //400*50=20000

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case FLAG_LOAD_MORE:
                    mHandler.removeMessages(what);
                    loadMore();
                    MyLog.debug(TAG, "[handleMessage]" + "加载第" + (page + 1) + "页...");
                    page++;
                    if (page <= MAX_CNT) {
                        boolean isLoadMore = (boolean) RobotHelpers.callMethod(getConvService(), KeyConst.M_ConversationService_MaybeHasMoreConversation);
                        MyLog.debug(TAG, "[handleMessage]" + "isLoadMore:" + isLoadMore + "...");
                        if (isLoadMore) {
                            msg = Message.obtain();
                            msg.what = FLAG_LOAD_MORE;
                            mHandler.sendMessageDelayed(msg, 1800);
                        } else {
                            Global.postRunnable(() -> step_02_get_conversions(null, Global.loadPackageParam, paramCall, getConvService()));

                        }
                    } else {
                        Global.postRunnable(() -> step_02_get_conversions(null, Global.loadPackageParam, paramCall, getConvService()));
                    }
                    break;
            }
        }
    };

    private Object getConvService() {
        Class clazzConvService = RobotHelpers.findClassIfExists(KeyConst.C_ConversationService, Global.loadPackageParam.classLoader);
        Object convService = RobotHelpers.callStaticMethod(clazzConvService, KeyConst.M_ConversationService_getService);
        return convService;
    }

    private void loadMore() {
        Class clazzConvService = RobotHelpers.findClassIfExists(KeyConst.C_ConversationService, Global.loadPackageParam.classLoader);
        Object convService = RobotHelpers.callStaticMethod(clazzConvService, KeyConst.M_ConversationService_getService);
        Class clazzCallBack = RobotHelpers.findClassIfExists(KeyConst.C_IFetchSessionListCallback, Global.loadPackageParam.classLoader);
        Object objCallBack = Proxy.newProxyInstance(Global.loadPackageParam.classLoader, new Class[]{clazzCallBack}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String name = method.getName();
                if ("onResult".equals(name)) {
                    MyLog.debug(TAG, "[loadMore]" + " onResult callBack...");
                }
                return null;
            }
        });
        Global.postRunnable2UI(new Runnable() {
            @Override
            public void run() {
                try {
                    Object obj = RobotHelpers.callMethod(convService, KeyConst.M_ConversationService_FetchSessionList, objCallBack);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void step_01_get_more_conversatiom(Class classDb, LoadPackageParam loadPackageParam, IHookCallBack<List<ConvEntity>> paramCall, Object convService) {
        boolean tempB = (boolean) RobotHelpers.callMethod(convService, KeyConst.M_ConversationService_MaybeHasMoreConversation);
        if (!tempB) {
            step_02_get_conversions(classDb, loadPackageParam, paramCall, convService);
            return;
        }
        //继续获取更多

        Class clazzCallBack = RobotHelpers.findClassIfExists(KeyConst.C_IFetchSessionListCallback, Global.loadPackageParam.classLoader);
        Object objCallBack = Proxy.newProxyInstance(loadPackageParam.classLoader, new Class[]{clazzCallBack}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String name = method.getName();
                if (KeyConst.M_IFetchSessionListCallback_onResult.equals(name)) {
                    step_01_get_more_conversatiom(classDb, loadPackageParam, paramCall, convService);
                }
                return null;
            }
        });
        Global.postRunnable2UI(new Runnable() {
            @Override
            public void run() {
                try {
                    Object obj = RobotHelpers.callMethod(convService, KeyConst.M_ConversationService_FetchSessionList, objCallBack);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return;
    }

    public void step_02_get_conversions(Class classDb, LoadPackageParam loadPackageParam, IHookCallBack<List<ConvEntity>> paramCall, Object convService) {
        boolean succ = false;
        List<ConvEntity> rList = new ArrayList<>();
        //GetConversationList
        Object obj = RobotHelpers.callMethod(convService, KeyConst.M_ConversationService_GetConversationList);
        succ = true;
        if (obj != null) {
            Object[] convArray = (Object[]) obj;
            if (convArray.length > 0) {
                MyLog.debug(TAG, "[onInvokeMethod]" + " conv list.size:" + convArray.length);
                for (int i = 0; i < convArray.length; i++) {
                    Object conv = convArray[i];
                    ConvEntity convEntity = ConvParseUtil.parseConvEntity(conv);

                    MyLog.debug(TAG, "[onInvokeMethod]" + " convEntity:" + convEntity);
                    // convEntity.objConv = conv;
                    rList.add(convEntity);
                    ConvController.getInstance().put(convEntity.remoteId, convEntity.id, convEntity.id);
                }
            }
        }
        if (paramCall != null) {
            ResEntity resEntity = ResEntity.genSucc(rList);
            paramCall.onCall(resEntity);
        }
    }
}
