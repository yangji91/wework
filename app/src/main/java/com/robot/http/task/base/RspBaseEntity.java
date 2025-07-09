package com.robot.http.task.base;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class RspBaseEntity {
    private final String TAG = getClass().getSimpleName();

    public int code;
    public String msg;

    public RspBaseEntity(String data,boolean isJsonArray){
        if(data != null){
            try {
                JSONObject jsonObj = new JSONObject(data);
                this.code = jsonObj.getInt("code");
                if(jsonObj.has("msg")){
                    this.msg = jsonObj.getString("msg");
                }
                if(code == 0){
                    if(!isJsonArray){
                        String strData = jsonObj.getString("data");
//                    JSONObject jsonData = jsonObj.getJSONObject("data");
                        JSONObject jsonData = new JSONObject(strData);
                        parseInfo(jsonData,null);
                    }else{
                        JSONArray jsonArray = jsonObj.getJSONArray("data");
                        parseInfo(null,jsonArray);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                this.code = -1;
            }
        }

    }

    protected abstract void parseInfo(JSONObject jsonObj, JSONArray jsonArray);
}
