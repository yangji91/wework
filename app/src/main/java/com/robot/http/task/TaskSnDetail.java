package com.robot.http.task;

import com.google.gson.Gson;
import com.robot.entity.ResEntity;
import com.robot.http.ICallBack;
import com.robot.http.entity.req.ReqSnDetailEntity;
import com.robot.http.entity.rsp.PRspSnDetailEntity;
import com.robot.http.task.base.BaseTask;
import com.robot.util.MyLog;

public class TaskSnDetail extends BaseTask<ReqSnDetailEntity, PRspSnDetailEntity> {
    private final String TAG = getClass().getSimpleName();

    public TaskSnDetail(ReqSnDetailEntity reqSnDetailEntity, ICallBack<PRspSnDetailEntity> callBack) {
        super(reqSnDetailEntity, callBack);
    }

    @Override
    public void doTask(ResEntity<String> resEntity) {
        if(resEntity.isSucc()){
            String data = resEntity.getData();
            MyLog.debug(TAG,"[doTask]" + " data->" + data + " thread:" + Thread.currentThread().getName());
            Gson gson = new Gson();
            PRspSnDetailEntity rspEntity = gson.fromJson(data,PRspSnDetailEntity.class);
            if(callBack != null){
                if(rspEntity.success != null && rspEntity.success){
                    if(rspEntity.data != null){
                        ResEntity rEntity = ResEntity.genSucc(rspEntity);
                        callBack.onCall(rEntity);
                    }else{
                        ResEntity rEntity = ResEntity.genErr(null,"SN未绑定");
                        callBack.onCall(rEntity);
                    }
                }else{
                    ResEntity rEntity = ResEntity.genErr(null,rspEntity.info);
                    callBack.onCall(rEntity);
                }
            }
        }else{
            MyLog.debug(TAG,"[doTask]" + " rsp err:" + resEntity.isSucc());
            if(callBack != null){
                ResEntity rEntity = ResEntity.genErr(null,resEntity.getMsg());
                callBack.onCall(rEntity);
            }
        }
    }
}
