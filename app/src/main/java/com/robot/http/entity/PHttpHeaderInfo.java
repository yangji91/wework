package com.robot.http.entity;

import com.robot.common.MD5;
import com.google.gson.Gson;
import java.io.Serializable;
import java.util.List;

public class PHttpHeaderInfo implements Serializable{
    public String deviceId;
    public long sendTime;
    public String sign;
    public List<Long> robotUins;

    public String buildSn(){
        String sn = "";
        Gson gson = new Gson();
        String srcStr = deviceId + gson.toJson(robotUins) + sendTime;
        sn = MD5.toString(srcStr);
        return sn;
    }
}
