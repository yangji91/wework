package com.robot.netty;

import com.robot.netty.handle.BaseHandle;
import com.robot.netty.handle.imple.HandleActionNew;
//import com.robot.netty.handle.imple.HandleDevice;
import com.robot.netty.handle.imple.HandlePing;
//import com.robot.netty.handle.imple.HandleRobotCfg;
//import com.robot.netty.handle.imple.HandleTaskCallBackCommon;
import com.robot.netty.handle.imple.HandleUploadMsg;
import com.robot.netty.proto.req.ReqDeviceInfoEntity;
import com.robot.netty.proto.req.ReqMsgCallBackEntity;
import com.robot.netty.proto.req.ReqMsgRecvEntity;
import com.robot.netty.proto.req.ReqUserConvEntity;

import java.util.Objects;

public enum NettyHandleProtoEnum {

    HEAT_BEAT("0", 0, "heartbeat", "心跳", new HandlePing(), null, null),
    TASK_ACTION("0", 0, "task", "任务下发", new HandleActionNew(), null, null),
    UPLOAD_MSG("0", 0, "uploadMsg", "信息上报", new HandleUploadMsg(), null, null);

//    PING("0", 0, "", "PING命令", new HandlePing(), null, null),
//    ACTION("0", 0, "", "任务下发", new HandleActionNew(), null, null),
//    USER_CONV_INFO("0", 0, "", "用户用户&会话信息", new HandleUploadMsg(), ReqUserConvEntity.class, null),
//    MSG_RECV("0", 0, "", "接受消息上报", new HandleUploadMsg(), ReqMsgRecvEntity.class, null);
//    DEVICE("0", 0, "设备信息", "设备信息", new HandleDevice(), ReqDeviceInfoEntity.class, null),
//    MSG_CALLBACK("0", 0, "消息执行callback", "消息执行callback", new HandleTaskCallBackCommon(), ReqMsgCallBackEntity.class, null);
//    ROBOT_CFG("0", 0, "机器人控制模块配置等消息", "机器人控制模块配置等消息", new HandleRobotCfg(), null, null);

    private String reqid;
    private int code;
    private String actionType;
    private String desc;
    private BaseHandle baseHandle;
    private Class reqClazz;
    private Class rspClazz;

    NettyHandleProtoEnum(String reqid, int code, String actionType, String desc, BaseHandle baseHandle, Class req, Class rsp) {
        this.reqid = reqid;
        this.code = code;
        this.actionType = actionType;
        this.desc = desc;
        this.baseHandle = baseHandle;
        this.reqClazz = req;
        this.rspClazz = rsp;
    }

    public String getId() {
        return this.reqid;
    }

    public int getCode() {
        return this.code;
    }

    public String getActionType() {
        return this.actionType;
    }

    public String getDesc() {
        return this.desc;
    }

    public BaseHandle getBaseHandle() {
        return this.baseHandle;
    }

    public static final NettyHandleProtoEnum getEnumByCode(int code) {
        NettyHandleProtoEnum rEnum = null;
        for (NettyHandleProtoEnum en : NettyHandleProtoEnum.values()) {
            if (en != null && en.getCode() == code) {
                rEnum = en;
                break;
            }
        }
        return rEnum;
    }

    public static final NettyHandleProtoEnum getEnumById(String reqid) {
        NettyHandleProtoEnum rEnum = null;
        for (NettyHandleProtoEnum en : NettyHandleProtoEnum.values()) {
            if (en != null && Objects.equals(en.getId(), reqid)) {
                rEnum = en;
                break;
            }
        }
        return rEnum;
    }

    public static final NettyHandleProtoEnum getEnumByType(String type) {
        NettyHandleProtoEnum rEnum = null;
        for (NettyHandleProtoEnum en : NettyHandleProtoEnum.values()) {
            if (en != null && Objects.equals(en.getActionType(), type)) {
                rEnum = en;
                break;
            }
        }
        return rEnum;
    }

    public Class getReqClazz() {
        return this.reqClazz;
    }

    public Class getRspClazz() {
        return this.rspClazz;
    }
}
