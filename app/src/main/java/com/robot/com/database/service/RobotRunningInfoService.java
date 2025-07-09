package com.robot.com.database.service;

import com.robot.controller.LoginController;
import com.robot.com.database.AppDatabase;
import com.robot.com.database.dao.RobotRunningInfoDao;
import com.robot.com.database.entity.RobotRunningEvent;
import com.robot.com.database.entity.RobotRunningInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

/***
 *@author 
 *@date 2021/10/13
 *@description
 ****/
public class RobotRunningInfoService  extends BaseService<RobotRunningInfo>{
    private   String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat dateFormat =new SimpleDateFormat(DEFAULT_TIME_FORMAT);
    private static RobotRunningInfoService instance;
    private RobotRunningInfoDao dao;
    private RobotRunningInfoService(){
        dao= AppDatabase.getWeWorkMessageAppDatabase().getRobotRunningInfoDao();
    }
    public static RobotRunningInfoService getInstance() {
        if (instance==null){
            instance =new RobotRunningInfoService();
        }
        return instance;
    }

    @Override
    void saveOnce(RobotRunningInfo info) {
        dao.insert(info);
    }

    /**
     * 添加重启事件
     * @param uin
     */
    public   void addRebootEvent(long uin){
        RobotRunningInfo info =new RobotRunningInfo();
        info.content="进程启动";
        info.createTime=System.currentTimeMillis();
        info.event= RobotRunningEvent.REBOOT;
        info.code=1;
        info.userId=uin;
        info.remark= dateFormat.format(new Date());
        save(info);
    }

    public void addCheckMessageEvent(RobotRunningInfo info){
        info.remark=dateFormat.format(new Date());
        info.message="遗漏消息检测";
        info.code=301;
        info.event= RobotRunningEvent.MESSAGE_CHECK;
        info.userId= LoginController.getInstance().getLoginUserId();
        info.updateTime=System.currentTimeMillis()-60*1000;
        if (info.id!=0){
            dao.insert(info);
        }else {
            dao.update(info);
        }
    }

    public RobotRunningInfo getRobotMessageCheckEvent() {
        RobotRunningInfo info =    dao.getRobotMessageCheckEvent(RobotRunningEvent.MESSAGE_CHECK);
        if (info ==null ){
            info =new RobotRunningInfo();
            info.createTime =System.currentTimeMillis();
            info.remark=dateFormat.format(new Date());
            info.message="遗漏消息检测";
            info.code=301;
            info.event= RobotRunningEvent.MESSAGE_CHECK;
            info.userId= LoginController.getInstance().getLoginUserId();
            info.updateTime=System.currentTimeMillis()-60*60*1000;
        }
        return info;
    }
}
