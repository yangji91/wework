package com.robot.http.task;

import com.robot.entity.ResEntity;
import com.robot.http.ICallBack;
import com.robot.http.entity.req.ReqTokenEntity;
import com.robot.http.entity.rsp.RspTokenEntity;
import com.robot.http.task.base.BaseTask;

public class TaskToken extends BaseTask<ReqTokenEntity, RspTokenEntity> {

    public TaskToken(ReqTokenEntity reqCmdEntity, ICallBack<RspTokenEntity> callBack) {
        super(reqCmdEntity, callBack);
    }

    @Override
    public void doTask(ResEntity<String> resEntity) {
        if(resEntity.isSucc()){
            String data = resEntity.getData();
            RspTokenEntity rspEntity = new RspTokenEntity(data,getReqEntity().isJsonArray);
            if(callBack != null){
                if(rspEntity.code == 0){
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
