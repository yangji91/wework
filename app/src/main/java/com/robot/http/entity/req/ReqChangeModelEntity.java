package com.robot.http.entity.req;

import com.robot.http.task.base.ReqBaseEntity;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ReqChangeModelEntity extends ReqBaseEntity implements Serializable {
    public long wxUin;
    public int workMode;
    public boolean active;  //是否是主动上报（默认）

    @Override
    public String getReqURL() {
        return "/robot/ww/report.vpage";
    }

    @Override
    public Map<String, Object> getReqMap() {
        Map<String,Object> map = new HashMap<>();
        map.put("wxUin",wxUin);
        map.put("workMode",workMode);
        map.put("active",active);
        return map;
    }
}
