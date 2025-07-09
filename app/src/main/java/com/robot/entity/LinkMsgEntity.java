package com.robot.entity;

import com.robot.netty.proto.req.ReqLinkItemEntity;
import com.robot.util.StrUtils;

public class LinkMsgEntity {

    public int cmd;
    public String description;
    public String imageData;
    public String imageUrl;
    public String linkUrl;
    public OpenImCdnUri openImageUri = new OpenImCdnUri();
    public String title;

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
        return "LinkMsgEntity{" +
                "cmd=" + cmd +
                ", description=" + description +
                ", imageData=" + imageData +
                ", imageUrl=" + imageUrl +
                ", linkUrl=" + linkUrl +
//                ", openImageUri=" + openImageUri +
                ", title=" + title +
                '}';
    }


    public static final ReqLinkItemEntity parseLinkContent(LinkMsgEntity linkMsgEntity,String imgUrl){
        ReqLinkItemEntity rEntity = new ReqLinkItemEntity();
        rEntity.description = linkMsgEntity.description;
        rEntity.imageUrl = imgUrl;
        rEntity.linkUrl = linkMsgEntity.linkUrl;
        rEntity.title = linkMsgEntity.title;
        return rEntity;
    }
}

