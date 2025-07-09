package com.robot.entity;

import com.robot.util.StrUtils;

public class FileMsgEntity {
    public byte[] aesKey;
    public int decryptRet;
    public byte[] encryptKey;
    public long encryptSize;
    public byte[] extra;
    public byte[] fileId;
    public byte[] fileName;
    public int flags;
    public int height;
    public boolean isHd;
    public boolean iscomplex;
    public byte[] md5;
    public long midImgSize;
    public byte[] midThumbnailFileId;
    public byte[] midThumbnailPath;
    public byte[] randomKey;
    public byte[] receiverDeviceid;
    public byte[] senderDeviceid;
    public byte[] sessionId;
    public long size;
    public long thumbImgSize;
    public byte[] thumbnailFileId;
    public byte[] thumbnailPath;
    public byte[] url;
    public int voiceTime;
    public byte[] wechatAuthKey;
    public byte[] wechatCdnLdAeskey;
    public int wechatCdnLdHeight;
    public byte[] wechatCdnLdMd5;
    public int wechatCdnLdSize;
    public byte[] wechatCdnLdUrl;
    public int wechatCdnLdWidth;
    public OpenImCdnImg wechatMidImage;
    public int width;


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
        StringBuilder builder = new StringBuilder();
        builder.append("aesKey->" + StrUtils.byteToUTFStr(aesKey) + " ");
        builder.append("md5 -> " + StrUtils.byteToUTFStr(md5) + " ");
        builder.append("url -> " + StrUtils.byteToUTFStr(url) + " ");
        builder.append("voiceTime -> " + voiceTime + " ");
        builder.append("wechatAuthKey -> " + StrUtils.byteToUTFStr(wechatAuthKey) + " ");
        builder.append("wechatCdnLdAeskey -> " + StrUtils.byteToUTFStr(wechatCdnLdAeskey) + " ");
        builder.append("wechatCdnLdMd5 -> " + StrUtils.byteToUTFStr(wechatCdnLdMd5) + " " + " wechatMidImage->" + wechatMidImage);
        builder.append("fileSize -> " + size);
        builder.append("fieldId -> " + StrUtils.byteToUTFStr(fileId));
        builder.append("fileName->" + StrUtils.byteToUTFStr(fileName));
        return builder.toString();
    }
}

