package com.robot.netty.entity.rsp;

import java.io.Serializable;

public class BaseActionItem implements Serializable {
    public String uid;  //任务唯一id
    public String description;  //任务描述
    public Integer broadCast;
    public Long actionType;
    public Long executorUin;
    public Integer callback;
    public Long actionSubType;
}
