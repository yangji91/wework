package com.robot.http.entity.rsp;

import com.google.gson.Gson;
import com.robot.http.task.base.RspBaseEntity;
import com.robot.util.MyLog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class RspTokenEntity extends RspBaseEntity implements Serializable {
    private final String TAG = getClass().getSimpleName();
    public PTokenResultEntity tokenResultEntity;

    public RspTokenEntity(String data, boolean isJsonArray) {
        super(null,isJsonArray);
        try {
            JSONObject jsonObj = new JSONObject(data);
            parseInfo(jsonObj,null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void parseInfo(JSONObject jsonObj, JSONArray jsonArray) {
        MyLog.debug(TAG, "[parseInfo]" + " jsonObj:" + jsonObj);
        Gson gson = new Gson();
        tokenResultEntity = gson.fromJson(jsonObj.toString(), PTokenResultEntity.class);
        if(tokenResultEntity.isSuccess()){
            code = 0;
        }else{
            code = -1;
            msg = tokenResultEntity.result;
        }
    }
}
