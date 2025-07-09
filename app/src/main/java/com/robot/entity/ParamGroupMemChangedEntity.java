package com.robot.entity;

public class ParamGroupMemChangedEntity {
    public static final int EVENT_TYPE_ADD = 1;
    public static final int EVENT_TYPE_REMOVED = 2;

    public int eventType;
    public ConvEntity mConvEntity;  //val
}
