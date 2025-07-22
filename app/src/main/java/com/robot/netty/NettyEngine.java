package com.robot.netty;

import android.os.Handler;
import android.os.SystemClock;

import com.robot.common.FloatHelper;
import com.robot.common.StatsHelper;
import com.robot.common.Global;
import com.robot.common.MConfiger;
import com.robot.controller.LoginController;
import com.robot.entity.ResEntity;
import com.robot.netty.entity.TransmitData;
import com.robot.netty.handle.BaseHandle;
import com.robot.netty.listener.IConnectListener;
import com.robot.netty.listener.INettyListener;
import com.robot.netty.proto.INettyTaskInte;
import com.robot.util.MyLog;
import com.robot.util.StrUtils;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

public class NettyEngine {
    private final String TAG = NettyEngine.class.getSimpleName();

    private static NettyEngine instance;
    private NettyClient mClient;
    boolean isConnected;
    boolean isRunning = true;
    private Handler mHandler;
    private long startTime;
    private ConcurrentLinkedDeque<INettyTaskInte> mVector;
    private long mHeartBeatTime;
    private INettyListener mNettyListener;
    private long mStartTime;
    private long mConnectTime;
    private int mConnectCnt;
    private boolean isLogout;
    private boolean isClientCloseSocket = false;

    private NettyEngine() {
        this.isConnected = false;
        this.isRunning = true;
        this.isLogout = false;
        this.startTime = System.currentTimeMillis();
        mVector = new ConcurrentLinkedDeque();
        mStartTime = System.currentTimeMillis();

    }

    public static final NettyEngine getInstance() {
        if (instance == null) {
            instance = new NettyEngine();
        }
        return instance;
    }

    public void startConnect() {
        isLogout = false;
        if (mClient == null) {
            mClient = new NettyClient();
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

    public void setINettyListener(INettyListener mNettyListener) {
        this.mNettyListener = mNettyListener;
    }

    public void heartBeat() {
        this.mHeartBeatTime = System.currentTimeMillis();
    }

    private Thread rQueue = new Thread() {
        @Override
        public void run() {
            while (isRunning) {
                if (mHeartBeatTime != 0 && (Math.abs(mHeartBeatTime - System.currentTimeMillis()) >= MConfiger.HEART_BEAT_STOP_INTERVAL_2Minutes)) {
                    MyLog.debug(TAG, "[sendMsg]" + "超过 2分钟没有服务器数据，重新连接...", true);
                    StatsHelper.event("msgReport", "connect", "超过 2分钟没有服务器数据，重新连接", "heartBeatTime" + mHeartBeatTime);
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
                        final INettyTaskInte taskItem = mVector.poll();
                        doTask(taskItem);
                    }
                    SystemClock.sleep(10);
                }

            }
        }
    };

    private void doTask(INettyTaskInte taskInfo) {
        MyLog.debug(TAG, "[run]" + " Queueu mConnect isConnected:" + isConnected + " req:" + taskInfo.getReq());
        if (mClient != null && isConnected) {
            //send
            ResEntity<?> resEntity = mClient.sendNettyClient(taskInfo);
            //todo
            MyLog.debug(TAG, "[run]" + " Queueu mConnect->" + isConnected + " ok:" + resEntity.isSucc() + "taskInfo " + taskInfo.getReq() + " size:" + mVector.size());
        } else {
            //todo
            MyLog.debug(TAG, "[run]" + " 重新处理了该任务");
            SystemClock.sleep(1000);
            addItem2Queue(taskInfo, false);
        }
    }


    private IConnectListener mConnectListener = new IConnectListener() {
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
            //发送鉴权信息
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
                MyLog.debug(TAG, "[connectError]" + " 客户端主动断开链接:" + err + " time" + StrUtils.getTimeDetailStr(), true);
                StatsHelper.event("msgReport", "connect", "客户端主动断开链接", "err " + err, "time " + StrUtils.getTimeDetailStr());
                String tips = "客户端主动断开链接\n请检查网络~";
                MConfiger.mRobotStatus = -1;
                MConfiger.mRobotTips = tips + "\n" + "设备标识:" + Global.getDeviceSn() + "\n";
                FloatHelper.notifyData();
            } else {
                MyLog.debug(TAG, "[connectError]" + " 长链接异常断开链接...err:" + err + " time" + StrUtils.getTimeDetailStr(), true);
                StatsHelper.event("msgReport", "connect", "长链接异常断开链接", "err " + err, "time " + StrUtils.getTimeDetailStr(), "isLogout " + isLogout);
                String tips = "长链接异常断开\n请检查网络~";
                MConfiger.mRobotStatus = -1;
                MConfiger.mRobotTips = tips + "\n" + "设备标识:" + Global.getDeviceSn() + "\n";
                FloatHelper.notifyData();
                if (!isLogout) {
                    tryConnect();
                }
            }

        }

        @Override
        public void onRecv(TransmitData transmitData) {
            int code = transmitData.getCode();
            NettyHandleProtoEnum eum = NettyHandleProtoEnum.getEnumByCode(code);
            if (eum != null) {
                String desc = eum.getDesc();
                BaseHandle baseHandle = eum.getBaseHandle();
                if (baseHandle != null) {
                    //todo
                    MyLog.debug(TAG, "[onRecv]" + " 处理协议【" + desc + "】");
                    heartBeat();
                    baseHandle.onHandle(transmitData);
                }
            } else {
                MyLog.debug(TAG, "[onRecv]" + " 未找到该协议");
                StatsHelper.event("msgReport", "connect", "未找到该协议", "time " + StrUtils.getTimeDetailStr());
            }
        }
    };

    private void addItem2Queue(INettyTaskInte item, boolean isNotifyAll) {
        addItem2Queue(item, isNotifyAll, false);
    }

    private void addItem2Queue(INettyTaskInte item, boolean isNotifyAll, boolean isFirst) {
        if (isFirst) {
            mVector.addFirst(item);
        } else {
            mVector.addLast(item);
        }
    }

    public boolean sendMsg(final INettyTaskInte data, boolean isFirstQueue, boolean isNotifyAll) {
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

        if (LoginController.getInstance().getLoginUserId() > 0) {
            Global.removeRunnable(rCheckRun);
            Global.postNettyRunnableDelay(rCheckRun, 1000 * 10);
        }
    }

    private void tryConnectNow() {
        MyLog.debug(TAG, "[tryConnectNow]" + "...");
        if (LoginController.getInstance().getLoginUserId() > 0) {
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
            mClient = new NettyClient();
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

    public boolean sendMsg(final INettyTaskInte data) {
        sendMsg(data, false, true);
        return isConnected;
    }
}
