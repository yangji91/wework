package com.robot.entity;

import com.robot.netty.proto.req.ReqMiniAppEntity;
import com.robot.util.StrUtils;

public class WeAppMsgEntity {
    public String appMediaUrl;
    public String appName;
    public String appid;
    public int appservicetype;
    public String desc;
    public String pagepath;
    public int pkginfoType;
    public String shareId;
    public String shareKey;
    public String shareName;
    public String thumbAESKey;
    public String thumbFileId;
    public int thumbHeight;
    public String thumbMD5;
    public long thumbSize;
    public String thumbUrl;
    public int thumbWidth;
    public String title;
    public int type;
    public String username;
    public int version;
    public String weappIconUrl;
    public OpenImCdnImg wechatThumb;


    /**
     *  public String logo;
     *     public String appName;
     *     public String appid;
     *     public String desc;
     *     public String title;
     *     public String username;
     *     public int type;
     *     public String imgUrl;
     */
    public static final ReqMiniAppEntity parseContent(WeAppMsgEntity weAppMsgEntity,String imgUrl){
        ReqMiniAppEntity rEntity = new ReqMiniAppEntity();
        rEntity.logo = weAppMsgEntity.weappIconUrl;
        rEntity.appid = weAppMsgEntity.appid;
        rEntity.appName = weAppMsgEntity.appName;
        rEntity.desc = weAppMsgEntity.desc;
        rEntity.title = weAppMsgEntity.title;
        rEntity.username = weAppMsgEntity.username;
        rEntity.imgUrl = imgUrl;
        rEntity.type = weAppMsgEntity.type;
        rEntity.pagePath = weAppMsgEntity.pagepath;
        return rEntity;
    }

    public static class OpenImCdnImg{
        public int height;
        public OpenImCdnUri imgUri;
        public int width;

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("height:" + height + " width:" + width + " imgUri:" + imgUri);
            return builder.toString();
        }
    }

    public static class OpenImCdnUri{
        public byte[] aeskey;
        public byte[] authkey;
        public byte[] md5;
        public int size;
        public byte[] url;

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("aesKey:" + StrUtils.byteToUTFStr(aeskey) + " authKey:" + StrUtils.byteToUTFStr(authkey) + " md5:" + StrUtils.byteToUTFStr(md5) + " size:" + size
             + " url:" + StrUtils.byteToUTFStr(url));
            return builder.toString();
        }
    }

    @Override
    public String toString() {
        return "WeAppMsgEntity{" +
                "appMediaUrl='" + appMediaUrl + '\'' +
                ", appName='" + appName + '\'' +
                ", appid='" + appid + '\'' +
                ", appservicetype=" + appservicetype +
                ", desc='" + desc + '\'' +
                ", pagepath='" + pagepath + '\'' +
                ", pkginfoType=" + pkginfoType +
                ", shareId='" + shareId + '\'' +
                ", shareKey='" + shareKey + '\'' +
                ", shareName='" + shareName + '\'' +
                ", thumbAESKey='" + thumbAESKey + '\'' +
                ", thumbFileId='" + thumbFileId + '\'' +
                ", thumbHeight=" + thumbHeight +
                ", thumbMD5='" + thumbMD5 + '\'' +
                ", thumbSize=" + thumbSize +
                ", thumbUrl='" + thumbUrl + '\'' +
                ", thumbWidth=" + thumbWidth +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", username='" + username + '\'' +
                ", version=" + version +
                ", weappIconUrl='" + weappIconUrl + '\'' +
                ", wechatThumb=" + wechatThumb +
                '}';
    }
}

