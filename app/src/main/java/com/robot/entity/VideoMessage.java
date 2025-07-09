package com.robot.entity;

public class VideoMessage {
    public static final int QYDISK_TO_WX_BIG_FILE = 2;
    public static final int THIRD_ENCRYPT = 1;

    public String aesKey;
    public int decryptRet;
    public String encryptKey;
    public long encryptSize;
    public int flags;
    public String md5;
    public String previewImgAesKey;
    public String previewImgMd5;
    public int previewImgSize;
    public String previewImgUrl;
    public String randomKey;
    public String rawUploadDir;
    public String rawUrl;
    public String sessionId;
    public long size;
    public String thumbnailFileId;
    public String url;
    public int videoDuration;
    public int videoHeight;
    public String videoId;
    public int videoWidth;
    public String wechatAuthKey;


    @Override
    public String toString() {
        return "VideoMessage{" +
                "aesKey='" + aesKey + '\'' +
                ", decryptRet=" + decryptRet +
                ", encryptKey='" + encryptKey + '\'' +
                ", encryptSize=" + encryptSize +
                ", flags=" + flags +
                ", md5='" + md5 + '\'' +
                ", previewImgAesKey='" + previewImgAesKey + '\'' +
                ", previewImgMd5='" + previewImgMd5 + '\'' +
                ", previewImgSize=" + previewImgSize +
                ", previewImgUrl='" + previewImgUrl + '\'' +
                ", randomKey='" + randomKey + '\'' +
                ", rawUploadDir='" + rawUploadDir + '\'' +
                ", rawUrl='" + rawUrl + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", size=" + size +
                ", thumbnailFileId='" + thumbnailFileId + '\'' +
                ", url='" + url + '\'' +
                ", videoDuration=" + videoDuration +
                ", videoHeight=" + videoHeight +
                ", videoId='" + videoId + '\'' +
                ", videoWidth=" + videoWidth +
                ", wechatAuthKey='" + wechatAuthKey + '\'' +
                '}';
    }
}
