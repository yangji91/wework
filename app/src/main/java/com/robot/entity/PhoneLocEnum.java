package com.robot.entity;

public enum PhoneLocEnum {
    BAIDU(3, "百度云手机"),
    HUAWEI(2, "华为云手机"),
    Ali(4, "阿里云手机"),
    LOCAL(1, "线下手机");

    private int type;
    private String desc;
    private int channel;

    PhoneLocEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return this.type;
    }

    public String getDesc() {
        return this.desc;
    }

    public static final PhoneLocEnum getEnumByType(int type) {
        PhoneLocEnum phoneLocEnum = null;
        for (PhoneLocEnum en : PhoneLocEnum.values()) {
            if (en != null && en.getType() == type) {
                phoneLocEnum = en;
                break;
            }
        }
        return phoneLocEnum;
    }
}
