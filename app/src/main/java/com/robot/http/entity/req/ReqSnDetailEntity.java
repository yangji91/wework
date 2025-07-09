package com.robot.http.entity.req;

import com.robot.http.task.base.ReqBaseEntity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ReqSnDetailEntity extends ReqBaseEntity implements Serializable {
    public String deviceIdentity;
    public String detectSafeStatus = "0";//0未检测 1企微版本不对 2都不安全 3模块不安全 4框架不安全 5全部安全

    @Override
    public String getReqURL() {
        return "/ww/deviceConfig.vpage";
    }

    @Override
    public Map<String, Object> getReqMap() {
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("deviceIdentity", deviceIdentity);
        mMap.put("detectSafeStatus", detectSafeStatus);
        return mMap;
    }
}
