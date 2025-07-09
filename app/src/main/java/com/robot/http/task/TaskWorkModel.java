package com.robot.http.task;

import com.google.gson.Gson;
import com.robot.entity.ResEntity;
import com.robot.http.ICallBack;
import com.robot.http.entity.req.ReqChangeModelEntity;
import com.robot.http.entity.rsp.RspChangeModelEntity;
import com.robot.http.task.base.BaseTask;

public class TaskWorkModel extends BaseTask<ReqChangeModelEntity,RspChangeModelEntity> {

    public TaskWorkModel(ReqChangeModelEntity reqChangeModelEntity, ICallBack<RspChangeModelEntity> callBack) {
        super(reqChangeModelEntity, callBack);
    }

    @Override
    public void doTask(ResEntity<String> resEntity) {
        if(resEntity.isSucc()){
            String data = resEntity.getData();
            Gson gson = new Gson();
            RspChangeModelEntity rspEntity = gson.fromJson(data, RspChangeModelEntity.class);
            if(callBack != null){
                if(rspEntity.success != null && rspEntity.success){
                    ResEntity rEntity = ResEntity.genSucc(rspEntity);
                    callBack.onCall(rEntity);
                }else{
                    ResEntity rEntity = ResEntity.genErr("解析异常~");
                    callBack.onCall(rEntity);
                }
            }
        }else{
            if(callBack != null){
                ResEntity rEntity = ResEntity.genErr(null,resEntity.getMsg());
                callBack.onCall(rEntity);
            }
        }
    }
}
