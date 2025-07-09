package com.robot.netty.handle.imple

import com.robot.common.FloatHelper.notifyData
import com.robot.common.Global
import com.robot.common.MConfiger
import com.robot.controller.ConvController
import com.robot.controller.LoginController
import com.robot.netty.entity.TransmitData
import com.robot.netty.handle.BaseHandle
import com.robot.nettywss.WssNettyEngine
import com.robot.nettywss.WssProtocalManager
import com.robot.util.FileUtil
import com.robot.util.MyLog
import com.robot.util.StrUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HandlePing : BaseHandle() {

    override fun onHandle(data: TransmitData<*>?) {
        //        if (cnt % 20 == 0) {
//            LoginController.getInstance().refreshCorpInfo(Global.loadPackageParam.classLoader, new ProxyUtil.ProxyCommonResultDataCallBack() {
//                @Override
//                public void onResult(int i, byte[] bArr) {
//                    boolean isShowBar = ExpansionUtil.isShowExpanionBar();
//                    MyLog.debug(TAG, "[onHandle]" + " isShowBar:" + isShowBar);
//                    ProtocalManager.getInstance().sendExpansionStatus(isShowBar);
//                }
//            });
//
//        }
    }

    override fun onHandle(data: String) {
        super.onHandle(data)
        heartWecom(data)
    }

    fun heartWecom(data: String) {
        MyLog.debug(TAG, "[onRecv] HandlePing $data")

        // WssNettyEngine.getInstance().heartBeat();
        MainScope().launch {
            withContext(Dispatchers.IO) {
                delay(5 * 1000)
                WssProtocalManager.sendHeartBeat()
            }
            cancel()
        }
//        MainScope().launch {
//            withContext(Dispatchers.IO) {
//                delay(30 * 1000)
//                WssProtocalManager.sendHeartBeatWithoutContent()
//            }
//            cancel()
//        }

        ConvController.getInstance().onHeartBeat()
//        HandleTaskCallBackCommon.checkCallBack()
        Global.postRunnable { // 心跳当前时间
            FileUtil.saveWecomHeartBeatTime(System.currentTimeMillis())
        }

        cnt++

        val envStr = MConfiger.env.desc
        val startTime = WssNettyEngine.getInstance().startTime
        val strStartTime = StrUtils.getRunStrTime(startTime)
        val connectTime = WssNettyEngine.getInstance().connectTime
        val strConnectTime = StrUtils.getRunStrTime(connectTime)
        val mConnectCnt = WssNettyEngine.getInstance().connectCnt
        val userSize = Global.getUserSize()
        val convSize = ConvController.getInstance().convSize
        val loginMobile = LoginController.getInstance().loginMobile

        if (MConfiger.mRobotStatus != 1) {
            if (MConfiger.mRobotStatus == 2) {
                if (cnt % 3 == 0) {
                    MConfiger.mRobotStatus = 1
                    MConfiger.mRobotTips = """正常运行 $envStr
设备标识:${Global.getDeviceSn()}
用户标识:${LoginController.getInstance().loginUserId}
手机号:$loginMobile
运行时长:$strStartTime 连接时长:$strConnectTime 
联系人:$userSize 
"""
                    notifyData()
                }
            } else {
                MConfiger.mRobotStatus = 1
                MConfiger.mRobotTips = """正常运行 $envStr
设备标识:${Global.getDeviceSn()}
用户标识:${LoginController.getInstance().loginUserId}
手机号:$loginMobile
运行时长:$strStartTime 
连接时长:$strConnectTime 
联系人:$userSize 
"""
                notifyData()
            }
        } else {
            MConfiger.mRobotTips = """正常运行 $envStr
设备标识:${Global.getDeviceSn()}
用户标识:${LoginController.getInstance().loginUserId}
手机号:$loginMobile
运行时长:$strStartTime 
连接时长:$strConnectTime 
联系人:$userSize 
"""
        }
    }

    companion object {
        private var cnt = 0
    }
}
