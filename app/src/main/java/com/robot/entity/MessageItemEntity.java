package com.robot.entity;

import java.util.List;

public class MessageItemEntity {
    public List<ItemEntity> messages;
    public String title;
    public int type;
    public WelcomFileMsgEntity imgMsgEntity;
    public WelcomeLinkeMsgEntity linkMsgEntity;

    public static final int TYPE_TEXT = 1;
    public static final int TYPE_IMG = 2;
    public static final int TYPE_LINK = 3;  //链接

    public static final class ItemEntity{
        public int contentType;
        public String data;
        public WelcomFileMsgEntity fileMsgEntity;
    }

    public static boolean S(int i, long j) {
        return 3 == i && 10025 == j;
    }

    public static boolean UO(int i) {
        return 1007 == i;
    }

    public static boolean UP(int i) {
        return 1011 == i;
    }

    public static boolean UQ(int i) {
        return 1052 == i;
    }

    public static boolean UR(int i) {
        return 1053 == i;
    }

    public static boolean US(int i) {
        return 1056 == i;
    }

    public static boolean UU(int i) {
        return 1010 == i;
    }

    public static boolean UV(int i) {
        return 1018 == i || 1015 == i || 40 == i || 30 == i;
    }

    public static boolean UW(int i) {
        return 1022 == i;
    }

    public static boolean UX(int i) {
        return 1026 == i;
    }

    public static boolean UY(int i) {
        return i == 26 || i == 58 || i == 76;
    }

    public static boolean UZ(int i) {
        return 1002 == i;
    }

    public static boolean Va(int i) {
        return i == 41;
    }

    public static boolean Vb(int i) {
        return i == 88;
    }

    public static boolean Vc(int i) {
        return 1019 == i;
    }

    public static boolean Vd(int i) {
        return 69 == i;
    }

    public static boolean Ve(int i) {
        return i == 86;
    }

    public static boolean Vf(int i) {
        return 70 == i;
    }

    public static boolean Vk(int i) {
        return i >= 0 && i < 1000;
    }

    public static boolean Vl(int i) {
        return i == 87 || i == 533;
    }

    public static boolean Vn(int i) {
        return (i & 2) != 0;
    }

    public static boolean Vo(int i) {
        return (i & 16) != 0;
    }

    public static boolean Vp(int i) {
        return 80 == i;
    }

    public static boolean Vq(int i) {
        return 123 == i;
    }

    public static boolean Vr(int i) {
        return 6 == i;
    }

    public static boolean Vs(int i) {
        return 6 == i;
    }

    public static boolean Vx(int i) {
        return (i & 4) == 0;
    }

    private static boolean Vy(int i) {
        return (i & 4) == 0;
    }

    public static boolean c(int i, long j, int i2) {
        return i == 3 && j == 10001 && 10 == i2;
    }

    public static boolean eEF() {
        return true;
    }

    public static boolean isDynamicExpression(int i) {
        return 29 == i || 104 == i;
    }

    public static boolean isFileMessage(int i) {
        return 8 == i || 15 == i || 20 == i || 221 == i || 49 == i || 102 == i;
    }

    public static boolean isForwardMessage(int i) {
        return 4 == i || 36 == i;
    }

    public static boolean isFtnPic(int i) {
        return i == 19 || i == 48;
    }

    public static boolean isFtnVideoThumbnailPic(int i) {
        return i == 51;
    }

    public static boolean isImageMessage(int i) {
        return 1 == i || 7 == i || 14 == i || 19 == i || 48 == i || 101 == i;
    }

    public static boolean isLink(int i) {
        return 13 == i;
    }

    public static boolean isNewUserItem(int i) {
        return i >= 4 && i < 999;
    }

    public static boolean isP2PImage(int i) {
        return i == 63;
    }

    public static boolean isPicTxtMessage(int i) {
        return 123 == i;
    }

    public static boolean isSystemItem(int i) {
        return i > 1000 && i < 1999;
    }

    public static boolean isTextMessage(int i) {
        return i == 0 || 2 == i;
    }

    public static boolean isVideoMessage(int i) {
        return 5 == i || 17 == i || 23 == i || 22 == i || 51 == i || 103 == i;
    }

    public static boolean isVoiceMessage(int i) {
        return 9 == i || 16 == i || 21 == i || 50 == i;
    }

    public static boolean isWeAppMessage(int i) {
        return i == 78;
    }

    public static boolean isWechatFile(int i) {
        return i == 102;
    }

    public static boolean isWechatImage(int i) {
        return i == 101;
    }

    public static boolean isWechatVideo(int i) {
        return i == 103;
    }

    public static boolean ok(long j) {
        return 15 == j;
    }
}
