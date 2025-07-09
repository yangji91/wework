package com.robot.netty.entity;

import com.robot.common.MD5;
import com.google.gson.Gson;
import com.robot.common.Global;
import com.robot.util.DeviceUtil;
import com.robot.util.PkgUtil;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class CommonHeader implements Serializable {
    public String deviceId;
    public List<Long> robotUins;
    public long sendTime;
    public String sign;
    public String agentType;
    public long agentVer;

    public void setDeviceId(String deviceId){
        this.deviceId = deviceId;
    }

    public void setRobotUins(List<Long> uins){
        this.robotUins = uins;
    }

    public void setSendTime(long sendTime){
        this.sendTime = sendTime;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public List<Long> getRobotUins(){
        return this.robotUins;
    }

    public long getSendTime(){
        return this.sendTime;
    }

    public void setSign(String sign){
        this.sign = sign;
    }

    public String getSign(){
        return this.sign;
    }

    public static final CommonHeader buildCommonHeader(Long remoteId){
        Gson gson = new Gson();
        String androidId = DeviceUtil.getAndroidID();
        CommonHeader header = new CommonHeader();
        header.setDeviceId(androidId);
        header.setRobotUins(Arrays.asList(remoteId));
        header.setSendTime(System.currentTimeMillis());
        header.agentType = "wework";
        header.agentVer = PkgUtil.getVersionCode(Global.getContext());
        String md5Sign = header.getDeviceId() + gson.toJson(header.getRobotUins()) + header.getSendTime();
        md5Sign = MD5.toString(md5Sign);
        header.setSign(md5Sign);
        return header;
    }
}
