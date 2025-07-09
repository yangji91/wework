package com.robot.entity;

import java.util.List;

public class ParamCreateGroupEntity {
    public List<Long> userIds;
    public long covId;
    public long remoteId;

    //群信息
    public ConvEntity convEntity;
    public Object convObj;
}
