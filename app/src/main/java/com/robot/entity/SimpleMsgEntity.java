package com.robot.entity;

import java.io.UnsupportedEncodingException;

/**
 * 简单消息
 */
public class SimpleMsgEntity {
    public int contentType;
    public String content;
    public SimpleAtMsgEntity atMsg;

    public static class SimpleAtMsgEntity{
        public byte[] name;
        public long uin;

        @Override
        public String toString() {
            try {
                return "SimpleAtMsgEntity{" +
                        "name=" + name==null?"null":new String(name,"UTF-8") +
                        ", uin=" + uin +
                        '}';
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public String toString() {
        return "SimpleMsgEntity{" +
                "contentType=" + contentType +
                ", content='" + content + '\'' +
                ", atMsg=" + atMsg +
                '}';
    }
}
