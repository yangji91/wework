package com.robot.http.entity.rsp;

import com.robot.http.task.base.RspBaseEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

public class PRspSnDetailEntity extends RspBaseEntity implements Serializable {

    public Boolean success; //是否成功
    public String info;     //错误信息
    public PSnDetail data;  //

    public PRspSnDetailEntity(String data, boolean isJsonArray) {
        super(data, false);
    }

    @Override
    protected void parseInfo(JSONObject jsonObj, JSONArray jsonArray) {

    }


    public static class PSnDetail implements  Serializable{
        public String deviceIdentity;
        public Integer bizId;
        public Integer bizIdL2;
        public String account;
    }
}
