package com.robot.http.entity.base;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public abstract class HttpBaseEntity<T> implements Serializable {
    public int code;
    public String msg;
    public T obj;

    public HttpBaseEntity(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            this.code = jsonObject.getInt("code");
            this.msg = jsonObject.getString("msg");
            if(this.code == 0){
                JSONObject item = jsonObject.getJSONObject("data");
                parseObj(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    abstract public void parseObj(JSONObject item);


    public boolean isSucc(){
        return this.code == 0;
    }
}
