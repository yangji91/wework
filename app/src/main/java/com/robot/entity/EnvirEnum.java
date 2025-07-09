package com.robot.entity;

import com.robot.common.Global;

/**
 * Test Environment
 */
public enum EnvirEnum {
    TEST("测试环境",
            "****", 443,
            "****",
            "****",
            "wss://api.youdai666.com/infra/ws/1/{imei}/{wxid}",
            "wss://api.toou.top/hk/ws/gw/{imei}", //"wss://api.toou.top:443/workbench/websocket/A001",
            "https://92b6f898fdc0b3534dea4081f1f61702-xi.obs.cn-east-3.myhuaweicloud.com/",
            "****"
    ),

    PRODUCT("正式环境",
            "****", 443,
            "****",
            "****",
            "wss://api.youdai666.com/infra/ws/1/{imei}/{wxid}",
            "wss://api.toou.top/hk/ws/gw/{imei}", //"wss://api.toou.top:443/workbench/websocket/A001",
            "https://92b6f898fdc0b3534dea4081f1f61702-xi.obs.cn-east-3.myhuaweicloud.com/",
            "****"
    );


    private String desc;
    private String host;
    private int port;
    private String tokenUrl;
    private String secretKey;
    private String wecomSocketIp;
    private String wechatSocketIp;
    private String imgHost;
    private String httpAgentUrl;

    EnvirEnum(String msg, String host, int port, String tokenUrl, String secretKey, String wecomSocketIp, String wechatSocketIp, String imgHost, String httpAgentUrl) {
        this.desc = msg;
        this.host = host;
        this.port = port;
        this.tokenUrl = tokenUrl;
        this.secretKey = secretKey;
        this.wecomSocketIp = wecomSocketIp;
        this.wechatSocketIp = wechatSocketIp;
        this.imgHost = imgHost;
        this.httpAgentUrl = httpAgentUrl;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getHttpUrl() {
        return this.host;
    }

    public String getTokenUrl() {
        return this.tokenUrl;
    }

    public String getSocketIp(String imei, String wxid) {
        return this.wecomSocketIp.replace("{imei}", imei).replace("{wxid}", wxid);
    }

    public String getImgHost() {
        return this.imgHost;
    }

    public String getHttpAgentUrl() {
        return this.httpAgentUrl;
    }
}
