package com.robot.netty.handle;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.robot.netty.entity.RobotMsg;
import com.robot.netty.entity.TransmitData;

import java.io.UnsupportedEncodingException;

public abstract class BaseHandle {
    public String TAG = "BaseHandle";

    public void onHandle(TransmitData data) {
        RobotMsg robotMsg = parseRspObj(data);
        if (robotMsg != null) {
            JsonObject jsonObject = robotMsg.getBody();
            if (jsonObject != null) {
                onHandle(jsonObject);
            }
        }
    }

    public void onHandle(JsonObject data) {

    }

    public void onHandle(String data) {
    }

    protected RobotMsg parseRspObj(TransmitData data) {
        RobotMsg robotMsg = null;
        try {
            if (data != null && data.getContent() != null && data.getContent().getBytes("UTF-8").length > 0) {
                byte[] array = data.getContent().getBytes("UTF-8");
                try {
                    String str = new String(array, "UTF-8");
                    Gson gson = new Gson();
                    robotMsg = gson.fromJson(str, RobotMsg.class);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return robotMsg;
    }
}
