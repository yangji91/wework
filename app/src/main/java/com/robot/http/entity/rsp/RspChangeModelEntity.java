package com.robot.http.entity.rsp;

import java.io.Serializable;

public class RspChangeModelEntity implements Serializable {
        //{"success":true,"deviceConfig":{"wxUin":1688851912177118,"workMode":1}}
    public Boolean success;
    public PDeviceConfig deviceConfig;

    public static class PDeviceConfig{
        public Long wxUin;
        public Integer workMode;
    }
}
