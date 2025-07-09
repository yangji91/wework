package com.robot.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QueryCRMAntiSpamRuleRspEntity implements Serializable {
    public boolean isEnd;
    public int lastTime;
    public List<CRMAntiSpamRule> list;

    //    public static final class CRMAntiSpamRuleList{
    //        public CRMAntiSpamRule[] items;
    //    }

    public static final class CRMAntiSpamRule implements Serializable {
        public int createTs;
        public long creator;
        public long id;
        //        public CRMAntiSpamDetailFrequen msgFre;
        //        public CRMAntiSpamDetailKeyWord msgKeyword;
        //        public CRMAntiSpamDetailMsgLen msgLen;
        //        public CRMAntiSpamDetailMsgType msgType;
        public String name;
        //        public CRMAntiSpamDetailKeyWord nicknameKeyword;
        public int type;
        public int updateTs;

        //skfly add begin
        public CRMAntiSpamAction action = new CRMAntiSpamAction();                      //踢人方式????
        public CRMAntiSpamDetailFrequen msgFre = new CRMAntiSpamDetailFrequen();        //防刷屏_消息发送频次
        public CRMAntiSpamDetailKeyWord msgKeyword = new CRMAntiSpamDetailKeyWord();        //防广告_消息包含关键词
        public CRMAntiSpamDetailKeyWord nicknameKeyword = new CRMAntiSpamDetailKeyWord();   //防广告_昵称包含关键词
        public CRMAntiSpamDetailMsgLen msgLen = new CRMAntiSpamDetailMsgLen();          //防刷屏_消息长度行数
        public CRMAntiSpamDetailMsgType msgType = new CRMAntiSpamDetailMsgType();       //防广告_网页小程序

        @NonNull
        @Override
        public String toString() {
            return "CRMAntiSpamRule{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

    //触发规则之后的反馈, 是否拉黑. 是否发消息, 发什么消息
    public static final class CRMAntiSpamAction implements Serializable {
        public boolean addBlackList;
        public int type;
        public String words;
    }

    //防刷屏_发送频次
    public static final class CRMAntiSpamDetailFrequen implements Serializable {
        public int frequency;
        public boolean isOpen;
        public int sort;
        public int timeInterval;
    }

    //防广告_关键词
    public static final class CRMAntiSpamDetailKeyWord implements Serializable {
        public boolean isOpen;
        public List<String> items = new ArrayList<String>();
        public int sort;

    }

    //防刷屏_消息长度行数
    public static final class CRMAntiSpamDetailMsgLen implements Serializable {
        public boolean isOpen;
        public int len;
        public int line;
        public int sort;
    }

    //防广告_网页小程序
    public static final class CRMAntiSpamDetailMsgType implements Serializable {
        public List<CRMSpamMsgType> msgtype = new ArrayList<CRMSpamMsgType>();
        //public CRMSpamMsgType[] msgtype;

        public static final class CRMSpamMsgType implements Serializable {
            public boolean isOpen;
            public int msgtype;
            public int sort;
            public List<String> whitelist = new ArrayList<String>();
        }
    }

    //skfly add end
}



