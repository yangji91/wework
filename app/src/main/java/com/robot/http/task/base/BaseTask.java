package com.robot.http.task.base;

import com.robot.entity.ResEntity;
import com.robot.http.ICallBack;

public abstract class BaseTask<REQ,RSP>{
    protected REQ req;
    protected ICallBack<RSP> callBack;
    public boolean isCircleGet = false;

    public BaseTask(REQ req, ICallBack<RSP> callBack){
        this.req = req;
        this.callBack = callBack;
    }

    abstract public void doTask(ResEntity<String> resEntity);

    public REQ getReqEntity(){
        return this.req;
    }
}
