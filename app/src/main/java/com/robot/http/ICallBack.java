package com.robot.http;


import com.robot.entity.ResEntity;

public interface ICallBack<R> {

    void onCall(ResEntity<R> resEntity);
}
