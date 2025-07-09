package com.robot.netty.entity;

import com.google.gson.Gson;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * @author sunli
 * @date 2020-01-1720:19
 */
public class TransmitData<T> implements Serializable {

    public int code;
    public String type;
    public String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return data;
    }

    public void setContent(byte[] content) {
        this.data = new String(content, StandardCharsets.UTF_8);
    }

    public void setContentObject(T t) {
        if (t != null) {
            Gson gson = new Gson();
            this.data = gson.toJson(t);
        }
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        String str = gson.toJson(this);
        return str;
    }

    public static TransmitData buildDefault(int code, String type, Object obj) {
        TransmitData transmitData = new TransmitData();
        transmitData.setCode(code);
        transmitData.setType(type);
//        if (obj != null) {
//            Gson gson = new Gson();
//            String str = gson.toJson(obj);
//            MyLog.debug("TransmitData ", str);
//            jsonObject = gson.fromJson(str, JsonObject.class);
//        }
//        RobotMsg robotMsg = RobotMsg.buildRobotMsg(userId, jsonObject);

        transmitData.setContentObject(obj);
        return transmitData;
    }
}
