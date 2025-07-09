package com.robot.com.database.service;

import com.alibaba.fastjson.JSON;
import com.robot.netty.entity.rsp.PRspActionTextMsgEntity;
import com.robot.com.database.AppDatabase;
import com.robot.com.database.dao.TaskDao;
import com.robot.com.database.entity.Task;

/***
 *@author 
 *@date 2021/8/27
 *@description
 ****/
public class TaskService extends BaseService<PRspActionTextMsgEntity.PContactMsgActionItem>{

    private TaskDao dao;

    private static TaskService instance;
    private TaskService( ) {
        this.dao = AppDatabase.getWeWorkMessageAppDatabase().getTaskDao();
    }

    public static TaskService getInstance() {
        if (instance==null){
            instance =new TaskService();
        }
        return instance;
    }

    /**
     * 收到新任务登记
     * @param
     */
    protected void saveOnce(PRspActionTextMsgEntity.PContactMsgActionItem actionItem) {

            Task task =new Task();
            task.actionType=actionItem.actionType;
            task.businessId=actionItem.uid;
            task.description=actionItem.description;
            task.createTime =System.currentTimeMillis();
            task.uid=actionItem.uid;
            task.uin=actionItem.executorUin;
            task.content= JSON.toJSONString(actionItem);
            task.isUpload=0;
            dao.insert(task);

    }
    public Task getTaskByUidAndTime(String uid,long time,long endTime){
        return dao.selectByUidAndTime(uid,time,endTime);
    }

    /**
     * 完成任务登记
     * @param uid
     */
    public void completeTask(String uid,String upContent, int code,String message) {
      runOnThread(new Runnable() {
          @Override
          public void run() {
              Task task = dao.selectByUid(uid);
              if (task != null) {
                  task.isUpload = 1;
                  task.completeTime = System.currentTimeMillis();
                  task.code = code;
                  task.endTime = System.currentTimeMillis();
                  task.message = message;
                  task.upContent = upContent;
                  dao.update(task);
              }
          }
      });

    }
}
