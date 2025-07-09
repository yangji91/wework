package com.robot.http;


import com.robot.common.Global;
import com.robot.common.MConfiger;
import com.robot.http.entity.PHttpHeaderInfo;
import com.robot.http.entity.req.ReqChangeModelEntity;
import com.robot.http.entity.req.ReqSnDetailEntity;
import com.robot.http.entity.req.ReqTokenEntity;
import com.robot.http.entity.rsp.PRspSnDetailEntity;
import com.robot.http.entity.rsp.RspChangeModelEntity;
import com.robot.http.entity.rsp.RspTokenEntity;
import com.robot.http.task.TaskSnDetail;
import com.robot.http.task.TaskToken;
import com.robot.http.task.TaskWorkModel;
import com.robot.util.DeviceUtil;

import java.util.ArrayList;

public class HttpProtocalManager {
    private final String TAG = getClass().getSimpleName();

    private static HttpProtocalManager instance;
    private HttpEngine engin;

    private HttpProtocalManager(){
        engin = HttpEngine.getInstance();
    }

    public static final HttpProtocalManager getInstance(){
        if(instance == null){
            instance = new HttpProtocalManager();
        }
        return instance;
    }

    public void reqToken(ICallBack<RspTokenEntity> callBack){
        ReqTokenEntity reqTokenEntity = new ReqTokenEntity();
        reqTokenEntity.isFullURL = true;
        reqTokenEntity.app_key = MConfiger.APP_KEY;
        reqTokenEntity.app_product_id = MConfiger.APP_PRODUCT_ID;
        reqTokenEntity.bucket_config_name = MConfiger.BUCKET_NAME;
        reqTokenEntity.isPostJson = false;
        reqTokenEntity.isGet = false;
        TaskToken taskToken = new TaskToken(reqTokenEntity,callBack);
        engin.addTask2Queue(taskToken);
    }

    /**
     * 更改工作模式
     * @param remoteId
     * @param workModel
     * @param callBack
     */
    public void reqChangeModel(long remoteId,int workModel,String deviceId,boolean activi,ICallBack<RspChangeModelEntity> callBack){
        ReqChangeModelEntity reqEntity = new ReqChangeModelEntity();
        reqEntity.isFullURL = false;
        reqEntity.hostUrl = MConfiger.env.getHttpAgentUrl();
        reqEntity.workMode = workModel;
        reqEntity.wxUin = remoteId;
        reqEntity.isPostJson = true;
        reqEntity.headerInfo = buildHeaderInfo(deviceId,remoteId);
        reqEntity.active = activi;
        TaskWorkModel task = new TaskWorkModel(reqEntity,callBack);
        engin.addTask2Queue(task);
    }

    /**
     * 请求详情
     * @param remoteId
     * @param callBack
     */
    public void reqSnDetail( Long remoteId,ICallBack<PRspSnDetailEntity> callBack){
        ReqSnDetailEntity reqEntity = new ReqSnDetailEntity();
        reqEntity.deviceIdentity = Global.getDeviceSn();
        reqEntity.detectSafeStatus = Global.getDetectSafeStatus();
        reqEntity.isFullURL = false;
        reqEntity.hostUrl = MConfiger.env.getHttpAgentUrl();
        reqEntity.isPostJson = false;
        reqEntity.isGet = true;
        reqEntity.headerInfo = buildHeaderInfo(DeviceUtil.getAndroidID(),remoteId);
        TaskSnDetail taskSnDetail = new TaskSnDetail(reqEntity,callBack);
        taskSnDetail.isCircleGet = true;
        engin.addTask2Queue(taskSnDetail);
    }

    public PHttpHeaderInfo buildHeaderInfo(String deviceId,long remoteId) {
        PHttpHeaderInfo headerInfo = new PHttpHeaderInfo();
        headerInfo.sendTime = System.currentTimeMillis();
        headerInfo.deviceId = deviceId;
        headerInfo.robotUins = new ArrayList<>();
        headerInfo.robotUins.add(remoteId);
        headerInfo.sign = headerInfo.buildSn();
        return headerInfo;
    }
}
