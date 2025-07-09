package com.robot.netty.entity.rsp;

import java.io.Serializable;

public class PRspRobotCfgEntity implements Serializable {
    public PDeviceCfgConfig deviceConfig;       //设备控制配置
    public Integer workMode;                    //重传deviceInfo信息，0:否 1:是
    public PWxMsgConfig wxMsgConfig;            //微信消息控制配置
    public PDeviceCfgConfig wxMetadataConfig;   //微信数据控制配置
    public WxActionCount wxActionCount;         //微信任务相关实时统计数据


    @Override
    public String toString() {
        return "PRspRobotCfgEntity{" +
                "deviceConfig=" + deviceConfig +
                ", workMode=" + workMode +
                ", wxMsgConfig=" + wxMsgConfig +
                ", wxMetadataConfig=" + wxMetadataConfig +
                ", wxActionCount=" + wxActionCount +
                '}';
    }

    public static class PDeviceCfgConfig implements Serializable{
        public Integer full;
    }

    public static class PWxMsgConfig implements  Serializable{
        public Integer recall;
    }

    public static class WxActionCount implements  Serializable{
        public Integer todoActionCount;
        public Integer todoActionDuration;
        public Integer timeOutActionCount;
    }
}
