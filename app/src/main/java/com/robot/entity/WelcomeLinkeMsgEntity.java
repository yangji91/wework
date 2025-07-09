package com.robot.entity;

public class WelcomeLinkeMsgEntity {
    public int cmd;
    public String desc;
    public String imgUrl;
    public String linkUrl;
    public String title;


    @Override
    public String toString() {
        return "WelcomeLinkeMsgEntity{" +
                "cmd=" + cmd +
                ", desc='" + desc + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
