package com.robot.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ActionRet implements Serializable
{
    public int eventStatus;// 是 流程状态 0：完成流程 1：crash
    public String crashInfo;    //   string 否 崩溃报告 eventStatus = 1时必填
    public List<FinishMatters> finishMatters=new ArrayList<FinishMatters>();   //   object array 是 已完成的实际可执行项  实际可执行项定义 不同任务类型的字段跟随action设计中对应类型的实际可执行项来生成

    public List<FailExpectMatters> failExpectMatters=new ArrayList<FailExpectMatters>();   //   object array 是 执行失败的预期可执行项 预期可执行项定义 不同任务类型的字段跟随action设计中对应类型的预期可执行项来生成

    public String lastSteps;    //   string array 是 最近流程 存储老版本的error_reason
    public Long eventCost;  //   long 是 自动化执行时长 结束毫秒值 - 启动毫秒值
    public Long downloadCost;   //   long 否 下载时长 download_end_mills - download_start_mills


    public static class FinishMatters implements Serializable
    {
        /**
         *
         * @param type
         * @param subType
         * @param info
         */
        public FinishMatters(Integer type, Integer subType, String info) {
            this.type = type;
            this.subType = subType;
            this.info = info;
        }
        public FinishMatters() {
        }

        /*
                            1
                            任务字段值
                            如：match

                            2
                            任务字段名
                            如：设置好友自动推荐

                            3
                            页面信息
                            如：通过好友的好友昵称
                        * */
        public Integer type;    //       成功事项类型 //
        public Integer subType; //       成功事项子类型
        public String info;     //     matter定义 如：matcher.match

        @Override
        public String toString() {
            return "FinishMatters{" +
                    "type=" + type +
                    ", subType=" + subType +
                    ", info='" + info + '\'' +
                    '}';
        }
    }

    public static class FailExpectMatters implements Serializable
    {
        /**
         *
         * @param type
         * @param info
         * @param errorMessage
         */
        public FailExpectMatters(Integer type, String info, String errorMessage) {
            this.type = type;
            this.info = info;
            this.errorMessage = errorMessage;
        }

        public FailExpectMatters( ) {
        }

        public Integer type;    // int 是 异常类型 失败事项类型        //FailExpectEnum
        public String info;    // string 是 matter定义 如：matcher.match
        public String errorMessage; // string 是 失败信息
    }
}
