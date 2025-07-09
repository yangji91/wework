//package com.robot.netty.handle.imple;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import com.robot.controller.LoginController;
//import com.robot.entity.ResEntity;
//import com.robot.http.HttpProtocalManager;
//import com.robot.http.ICallBack;
//import com.robot.http.entity.rsp.RspChangeModelEntity;
//import com.robot.netty.ProtocalManager;
//import com.robot.netty.entity.rsp.PRspRobotCfgEntity;
//import com.robot.netty.handle.BaseHandle;
//import com.robot.com.file.SharePreModel;
//import com.robot.util.DeviceUtil;
//import com.robot.util.MyLog;
//
//public class HandleRobotCfg extends BaseHandle {
//    protected final String TAG = getClass().getSimpleName();
//
//    @Override
//    public void onHandle( JsonObject jsonObj) {
//        Gson gson = new Gson();
//        String str = gson.toJson(jsonObj);
//        PRspRobotCfgEntity rspEntity = gson.fromJson(str,PRspRobotCfgEntity.class);
//        MyLog.debug(TAG,"[HandleRobotCfg]" + " rspEntity:" + rspEntity);
//        ProtocalManager.getInstance().sendAckDeviceCfg();
//        if(rspEntity != null && rspEntity.workMode != null){
//            String deviceId = DeviceUtil.getAndroidID();
//            long remoteId = LoginController.getInstance().getLoginUserId();
//            HttpProtocalManager.getInstance().reqChangeModel(remoteId, rspEntity.workMode, deviceId, false, new ICallBack<RspChangeModelEntity>() {
//                @Override
//                public void onCall(ResEntity<RspChangeModelEntity> resEntity) {
//                    MyLog.debug(TAG,"[onHandle]" + " onCall succ:" + resEntity.isSucc());
//                }
//            });
//            SharePreModel mSharePre = new SharePreModel();
//            mSharePre.saveInfo(rspEntity.workMode+"");
//        }
//    }
//}
