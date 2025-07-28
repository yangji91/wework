package com.robot.nettywss;

import android.os.Handler;
import android.os.SystemClock;

import com.robot.common.FloatHelper;
import com.robot.common.StatsHelper;
import com.google.gson.Gson;
import com.robot.common.Global;
import com.robot.common.MConfiger;
import com.robot.entity.ResEntity;
import com.robot.netty.NettyHandleProtoEnum;
import com.robot.netty.entity.ResBaseEntity;
import com.robot.netty.handle.BaseHandle;
import com.robot.nettywss.listener.IWssConnectListener;
import com.robot.nettywss.listener.IWssNettyListener;
import com.robot.nettywss.proto.IWssNettyTaskInte;
import com.robot.util.MyLog;
import com.robot.util.StrUtils;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class WssNettyEngine {
    private final String TAG = WssNettyEngine.class.getSimpleName();

    private static WssNettyEngine instance;
    private WssNettyClient mClient;
    public boolean isConnected;
    public boolean isRunning = true;
    private Handler mHandler;
    private long startTime;
    private ConcurrentLinkedDeque<IWssNettyTaskInte> mVector;
    private long mHeartBeatTime;
    private IWssNettyListener mNettyListener;
    private long mStartTime;
    private long mConnectTime;
    private int mConnectCnt;
    private boolean isLogout;
    private boolean isClientCloseSocket = false;

    private WssNettyEngine() {
        this.isConnected = false;
        this.isRunning = true;
        this.isLogout = false;
        this.startTime = System.currentTimeMillis();
        mVector = new ConcurrentLinkedDeque();
        mStartTime = System.currentTimeMillis();

    }

    public static final WssNettyEngine getInstance() {
        if (instance == null) {
            instance = new WssNettyEngine();
        }
        return instance;
    }

    public void startConnect() {
        isLogout = false;
        if (mClient == null) {
            mClient = new WssNettyClient();
            mClient.setConnectListener(mConnectListener);
        }
        if (!isConnected) {
            mClient.startConnect();

        }
        if (!rQueue.isAlive() || rQueue.isInterrupted()) {
            rQueue.start();
        }


    }

    public long getStartTime() {
        return this.mStartTime;
    }

    public long getConnectTime() {
        return this.mConnectTime;
    }

    public int getConnectCnt() {
        return this.mConnectCnt;
    }

    public void setINettyListener(IWssNettyListener mNettyListener) {
        this.mNettyListener = mNettyListener;
    }

    public void heartBeat() {
        this.mHeartBeatTime = System.currentTimeMillis();
    }

    private Thread rQueue = new Thread() {
        @Override
        public void run() {
            while (isRunning) {
                if (mHeartBeatTime != 0 && (Math.abs(mHeartBeatTime - System.currentTimeMillis()) >= MConfiger.HEART_BEAT_STOP_INTERVAL_1Minutes)) {
                    MyLog.debug(TAG, "[sendMsg]" + "超过 2分钟没有服务器数据，重新连接...", true);
                    StatsHelper.event("msgReport", "connect", "超过 1钟没有服务器数据，重新连接", "heartBeatTime" + mHeartBeatTime);
                    isClientCloseSocket = true;
                    mHeartBeatTime = System.currentTimeMillis();
                    stopNettyConnectAndTryConnect();
                    long time = new Random().nextInt(30) * 1000 + 30000;
                    SystemClock.sleep(time);
                }
                if (!isConnected) {
                    SystemClock.sleep(2000);
                } else {
                    if (mVector.size() > 0) {
                        final IWssNettyTaskInte taskItem = mVector.poll();
                        doTask(taskItem);
                    }
                    SystemClock.sleep(10);
                }

            }
        }
    };

    private void doTask(IWssNettyTaskInte taskInfo) {
        MyLog.debug(TAG, "[run]" + " Queueu mConnect isConnected:" + isConnected + " req:" + taskInfo.getReq());
        if (mClient != null && isConnected) {
            // send
            ResEntity<?> resEntity = mClient.sendNettyClient(taskInfo);
            // todo
            MyLog.debug(TAG, "[run]" + " Queueu mConnect->" + isConnected + " ok:" + resEntity.isSucc() + "taskInfo " + taskInfo.getReq() + " size:" + mVector.size());
        } else {
            // todo
            MyLog.debug(TAG, "[run]" + " 重新处理了该任务");
            SystemClock.sleep(1000);
            addItem2Queue(taskInfo, false);
        }
    }


    private IWssConnectListener mConnectListener = new IWssConnectListener() {
        @Override
        public void startConnect() {
            MyLog.debug(TAG, "[startConnect]" + " 开始链接..." + StrUtils.getTimeDetailStr(), true);
            StatsHelper.event("msgReport", "connect", "开始链接", "time " + StrUtils.getTimeDetailStr());
        }

        @Override
        public void onConnectSucc() {
            MyLog.debug(TAG, "[onConnectSucc]" + " 长链接链接成功..." + StrUtils.getTimeDetailStr(), true);
            StatsHelper.event("msgReport", "connect", "长链接链接成功", "time " + StrUtils.getTimeDetailStr());
            mConnectCnt++;
            mConnectTime = System.currentTimeMillis();
            isConnected = true;
            isClientCloseSocket = false;
            mHeartBeatTime = System.currentTimeMillis();
            // 发送鉴权信息
//            ProtocalManager.getInstance().sendAuth(() -> {
            if (mNettyListener != null) {
                mNettyListener.onReady();
            }
//            });
        }

        @Override
        public void connectError(String err) {
            isConnected = false;
            if (isClientCloseSocket) {
                isClientCloseSocket = false;
                MyLog.debug(TAG, "[connectError]" + " 客户端主动断开链接:" + err + StrUtils.getTimeDetailStr(), true);
                StatsHelper.event("msgReport", "connect", "客户端主动断开链接", "err " + err, "time " + StrUtils.getTimeDetailStr());
                String tips = "客户端主动断开链接\n请检查网络~";
                MConfiger.mRobotStatus = -1;
                MConfiger.mRobotTips = tips + "\n" + "设备标识:" + Global.getDeviceSn() + "\n"+ "\n";
                FloatHelper.notifyData();
            } else {
                MyLog.debug(TAG, "[connectError]" + " 长链接异常断开链接...err:" + err + StrUtils.getTimeDetailStr(), true);
                StatsHelper.event("msgReport", "connect", "长链接异常断开链接", "err " + err, "time " + StrUtils.getTimeDetailStr(), "isLogout " + isLogout);
                String tips = "长链接异常断开\n请检查网络~";
                MConfiger.mRobotStatus = -1;
                MConfiger.mRobotTips = tips + "\n" + "设备标识:" + Global.getDeviceSn() + "\n"+ "\n";
                FloatHelper.notifyData();
                if (!isLogout) {
                    tryConnect();
                }
            }

        }

        @Override
        public void onRecv(WebSocketFrame webSocketFrame) {
//             MyLog.debug(TAG, "[onRecv]" + " 接收消息: " + webSocketFrame);
            if (webSocketFrame instanceof TextWebSocketFrame) {
                String result = ((TextWebSocketFrame) webSocketFrame).text();
                MyLog.debug(TAG, "[onRecvText]" + " Text消息: " + ((TextWebSocketFrame) webSocketFrame).text());

                ResBaseEntity res = new Gson().fromJson(result, ResBaseEntity.class);
//                MyLog.debug(TAG, "[onRecvMessage]" + " res: type " + res.getReqid() + " actionType " + res.getActionType() + " taskType " + res.getTaskType() + " msgType " + res.getMsgType() + " result " + res.getResult());
                NettyHandleProtoEnum eum = NettyHandleProtoEnum.getEnumByType(res.getActionType());
                if (eum != null) {
                    BaseHandle baseHandle = eum.getBaseHandle();
                    if (baseHandle != null) {
                        if (!eum.getActionType().equals(NettyHandleProtoEnum.HEAT_BEAT.getActionType())) {
                            MyLog.debug(TAG, "[onRecv]" + " 处理协议【" + eum.getDesc() + "】", true);
                        } else {
                            MyLog.debug(TAG, "[onRecv]" + " 处理协议【" + eum.getDesc() + "】");
                        }
                        heartBeat();
                        baseHandle.onHandle(result);
                    }
                } else {
                    MyLog.debug(TAG, "[onRecv]" + " 未找到该协议");
                    StatsHelper.event("msgReport", "connect", "未找到该协议", "time " + StrUtils.getTimeDetailStr());
                }
            } else if (webSocketFrame instanceof BinaryWebSocketFrame) {
                MyLog.debug(TAG, "[onRecv]" + " 二进制消息: " + webSocketFrame);
            }
        }
    };

    private void addItem2Queue(IWssNettyTaskInte item, boolean isNotifyAll) {
        addItem2Queue(item, isNotifyAll, false);
    }

    private void addItem2Queue(IWssNettyTaskInte item, boolean isNotifyAll, boolean isFirst) {
        if (isFirst) {
            mVector.addFirst(item);
        } else {
            mVector.addLast(item);
        }
    }

    public boolean sendMsg(final IWssNettyTaskInte data, boolean isFirstQueue, boolean isNotifyAll) {
        Global.postNettyRunnableDelay(() -> {
            if (!isConnected) {
                tryConnect();
            }
            addItem2Queue(data, isNotifyAll, isFirstQueue);
            MyLog.debug(TAG, "[sendMsg]" + " size:" + mVector.size() + " isConnected:" + isConnected + "");
        }, 0);
        return isConnected;
    }


    private void stopNettyConnectAndTryConnect() {
        if (mClient != null) {
            isConnected = false;
            mClient.closeConnected();
            mClient = null;
        }
        tryConnectNow();

    }

    private void tryConnect() {
        MyLog.debug(TAG, "[tryConnect]" + "...");
        MyLog.debug(TAG, "[sendMsg]" + " size:" + mVector.size() + " isConnected:" + isConnected + "");

        if (!Global.getDeviceSn().isEmpty()) {
            Global.removeRunnable(rCheckRun);
            Global.postNettyRunnableDelay(rCheckRun, 1000 * 10);
        }
    }

    private void tryConnectNow() {
        MyLog.debug(TAG, "[tryConnect] tryConnectNow" + "...");
        if (!Global.getDeviceSn().isEmpty()) {
            startClient(true);
        }
    }

    private Runnable rCheckRun = new Runnable() {
        @Override
        public void run() {
            Global.removeRunnable(rCheckRun);
            boolean isConnected = false;
            if (mClient != null) {
                isConnected = mClient.isNettyConncted();
            }
            MyLog.debug(TAG, "[tryConnect]" + " run isConnected -> " + isConnected);
            if (!isConnected) {
                startClient(true);
            }
        }
    };

    private boolean startClient(boolean isReStart) {
        MyLog.debug(TAG, "[startClient]" + " isReStart:" + isReStart + " client:" + mClient);
        StatsHelper.event("msgReport", "connect", "startClient", "time " + StrUtils.getTimeDetailStr());
        if (isReStart) {
            if (mClient != null) {
                mClient.setConnectListener(null);
                mClient.closeConnected();
                mClient = null;
            }
            mClient = new WssNettyClient();
            mClient.setConnectListener(mConnectListener);
            mClient.startConnect();
        } else {
            mClient.startConnect();
        }
        return true;
    }

    public void closeConnect() {
        this.isConnected = false;
        this.isLogout = true;
        if (mClient != null) {
            mClient.closeConnected();
        }
        this.mClient = null;
        Global.removeRunnable(rCheckRun);
    }

    public boolean sendMsg(final IWssNettyTaskInte data) {
        sendMsg(data, false, true);
        return isConnected;
    }
}
