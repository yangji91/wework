package com.robot.http.entity.req;

import com.robot.common.MD5;
import com.robot.common.MConfiger;
import com.robot.http.task.base.ReqBaseEntity;
import com.robot.util.MyLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReqTokenEntity extends ReqBaseEntity implements Serializable {
    public String app_key;
    public String bucket_config_name;
    public String app_product_id;

    @Override
    public String getReqURL() {
        return MConfiger.env.getTokenUrl();
    }

    @Override
    public Map<String, Object> getReqMap() {
        Map<String,Object> map = new HashMap<>();
        map.put("app_key",app_key);
        map.put("bucket_config_name",bucket_config_name);
        List<String> keyList = new ArrayList<>(map.keySet());
        Collections.sort(keyList);
        StringBuilder keySb = new StringBuilder();
        for (String key : keyList) {
            keySb.append(key).append("=").append(map.get(key)).append("&");
        }
        if (keySb.length() > 0) {
            keySb.deleteCharAt(keySb.length() - 1);
        }
        String sig = MD5.toString(keySb.toString() + MConfiger.env.getSecretKey());
        MyLog.debug(TAG,"[doTask]" + keySb.toString() + " sig:" + sig);
        map.put("sig", sig);
        map.put("sys", "android");
        map.put("app_product_id",app_product_id);
        return map;
    }
}
