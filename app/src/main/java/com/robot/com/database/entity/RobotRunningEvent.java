package com.robot.com.database.entity;

/***
 *@author 
 *@date 2021/10/13
 *@description
 ****/
public class RobotRunningEvent {

    /**
     * 重启事件
     */
    public static String REBOOT ="reboot";//重启事件

    /**
     * 长连接 链接成功
     */
    public static String SOCKET_CONNECT="socket_connect";


    /**
     * 长连接 断开链接
     */
    public static String SOCKET_CLOSE="socket_close";


    /**
     * 遗漏消息检测事件
     */
    public static String MESSAGE_CHECK ="message_check";//重启事件

}
