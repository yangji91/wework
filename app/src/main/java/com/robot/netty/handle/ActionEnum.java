package com.robot.netty.handle;

import com.robot.netty.handle.imple.HandleContactManager;
import com.robot.netty.handle.imple.HandleMediaMsg;

public enum ActionEnum {
    PUSH_CONTACT_MANAGER(10400L, "contact", "通讯录管理操作中"),
    PUSH_USER_MEDIA_MSG(10100L, "sendMsg", "私聊群发中");

    private long actionType;
    private String taskType;
    private String desc;


    ActionEnum(long actionType, String taskType, String desc) {
        this.actionType = actionType;
        this.taskType = taskType;
        this.desc = desc;

    }

    public long getActionType() {
        return this.actionType;
    }

    public String getTaskType() {
        return this.taskType;
    }

    public String getDesc() {
        return this.desc;
    }

    public BaseHandle getHandleAction() {
        switch (this) {
            case PUSH_USER_MEDIA_MSG:
                return new HandleMediaMsg();
            case PUSH_CONTACT_MANAGER:
                return new HandleContactManager();
        }
        return null;
    }

    public static ActionEnum getHandleActionByTaskType(int actionType) {
        ActionEnum handleEnum = null;
        for (ActionEnum e : ActionEnum.values()) {
            if (e != null) {
                long acType = e.getActionType();
                if (acType == actionType) {
                    handleEnum = e;
                    break;
                }
            }
        }
        return handleEnum;
    }

    public static ActionEnum getHandleActionByTaskType(String type) {
        ActionEnum handleEnum = null;
        for (ActionEnum e : ActionEnum.values()) {
            if (e != null) {
                String eType = e.getTaskType();
                if (eType.equals(type)) {
                    handleEnum = e;
                    break;
                }
            }
        }
        return handleEnum;
    }
}