
package com.robot.entity;

public class ParamSendLittleAppEntity {
    public long remoteid; //对话id
    public String thumbUrl=""; //图片地址 选填
    public String appId=""; //  appId 选填 非必须
    public String username="";//例 gh_5232ef019802@app 必填
    public String pagePath="";// 打开小程序的页面 选填
    public String title ="";// 发送的title 选填
    public String iconUrl ="";//小程序 图标 选填
    public String appName;//名称
}
