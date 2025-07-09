package com.robot.hook.base;


import com.robot.entity.ResEntity;

public abstract class IHookCallBack<T>{
   public boolean isCallbackFinish =false;
    /**
     * 回调接口
     * @param resEntity
     */
    abstract public void onCall(ResEntity<T> resEntity);

    /**
     * 获取参数
     * @return
     */
    abstract public T getParams();
}
