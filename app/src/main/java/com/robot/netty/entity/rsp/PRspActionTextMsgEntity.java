package com.robot.netty.entity.rsp;

import java.io.Serializable;
import java.util.List;

public class PRspActionTextMsgEntity implements Serializable {
    public List<PContactMsgActionItem> allActions;

    public static class PContactMsgActionItem  implements  Serializable{
        public String uid;  //任务唯一id
        public String description;  //任务描述
        public Integer broadCast;
        public Integer actionType;
        public long executorUin;
        public Integer callback;
        public List<PMatcherItemEntity> matchers;
        public List<PMsgDetailEntity> messageDetails;

        public Long actionSubType;          //":661913602,;
    }

    public static class PMatcherItemEntity implements  Serializable{

        public long conversationId; //会话ID
        public long remoteId;       //目标企业微信ID
        public String match;        //预期搜索内容 个人 名称 | 群 名称
       // public String name;//名称
        public String secondMatch;//个人 头像 | 群 创建人信息
        public String thirdMatch;//         |群 创建群时间

        @Override
        public String toString() {
            return "PMatcherItemEntity{" +
                    "conversationId=" + conversationId +
                    ", remoteId=" + remoteId +
                    ", match='" + match + '\'' +
                    ", secondMatch='" + secondMatch + '\'' +
                    ", thirdMatch='" + thirdMatch + '\'' +
                    '}';
        }
    }

    /*
    *             "messageDetails":[
                    {
                        "h5Msg":{
                            "imageUrl":"https://cdn-oss-aie.robot.cn/wecom/png/47fddc742539e156.png",
                            "linkUrl":"http://www.baidu.com",
                            "description":"搜索引擎",
                            "title":"百度你好"
                        },
                        "contentType":13
                    }
                ],
    * */
    public static class PMsgDetailEntity implements  Serializable{
        public String content;
        public Integer contentType;
        public Integer subContentType;
        public String fileTitle;
        public String fileType;
        public long fileSize;
        public long msgSvrId;
        public String targetUrl;    //目标点击url
        public String title;        //title
        public String imgLogo;      //img logo
        public PRspActionTextMsgEntity.PMsgAtDetailEntity atDetail;

        public H5Msg h5Msg;
        public VoiceMsg voice;
        public MiniProgram miniProgram;
        public String uid="";
    }

    public static class H5Msg implements Serializable
    {
        public String  imageUrl;
        public String  linkUrl;
        public String  description;
        public String  title;
    }
    public static class VoiceMsg implements Serializable
    {
        public String  voiceUrl;
        public int seconds;
    }
    public static class MiniProgram implements Serializable
    {
        public String thumbUrl; //图片地址 选填
        public String appId; //  appId 选填 非必须
        public String username;//例 gh_5232ef019802@app 必填
        public String pagePath;// 打开小程序的页面 选填
        public String title;// 发送的title 选填
        public String iconUrl;//小程序 图标 选填
        public String appName;
    }
    public static class PMsgAtDetailEntity implements  Serializable{
        public List<AtMsgEntity> atMatchers;
        public int atAffix;
        public String atSeparator;
        public boolean atAll=false;
    }

    public static class AtMsgEntity implements  Serializable{
        public long remoteId;
        public String match;
    }
}
