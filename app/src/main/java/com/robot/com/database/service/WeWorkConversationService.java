package com.robot.com.database.service;

import com.robot.entity.ConvEntity;
import com.robot.entity.GroupData;
import com.robot.com.database.AppDatabase;
import com.robot.com.database.dao.WeWorkConversationDao;
import com.robot.com.database.entity.WeWorkConversation;
import com.robot.util.MyLog;
import com.robot.util.StrUtils;

import java.util.List;

/***
 *@author 
 *@date 2021/9/29
 *@description
 ****/
public class WeWorkConversationService extends BaseService<ConvEntity> {

    private static WeWorkConversationService instance;
    private String TAG ="WeWorkConversationService";

    public static WeWorkConversationService getInstance() {
        if (instance==null){
            instance =new WeWorkConversationService();
        }
        return instance;
    }



    private  WeWorkConversationService (){
        conversationDao= AppDatabase.getWeWorkMessageAppDatabase().getWeWorkConversationDao();
    }
    private WeWorkConversationDao conversationDao ;
    @Override
    void saveOnce(ConvEntity object) {
        WeWorkConversation conversation =conversationDao.selectByRemoteId(object.remoteId);
        if (conversation==null) conversation =new WeWorkConversation();
        conversation.conversationType=object.type;
        conversation.createrId=object.creatorId;
        conversation.createTime=object.createTime;
        conversation.exited=object.exited?1:0;
        conversation.memberBin= StrUtils.objectToJson(object.memberList);
        conversation.RID=object.remoteId;
        conversation.LID=object.id;
        conversation.name=object.name;
        conversation.searchTime=object.searchTime;
        conversation.extras=StrUtils.objectToJson(object.extras);
        conversation.hidden=object.hidden?1:0;
        conversation.fwId=object.notified?1:0;
        conversation.isSticky=object.isStickied?1:0;
        if (conversation.id>0){
            MyLog.debug(TAG,"更新 数据库 会话信息 remoteid "+conversation.RID+" name ="+conversation.name );
            conversationDao.update(conversation);
        }else {
            MyLog.debug(TAG,"插入 数据库 会话信息 remoteid "+conversation.RID+" name ="+conversation.name );
            conversationDao.insert(conversation);
        }

    }

    public void save(List<ConvEntity> convEntities) {
        runOnThread(new Runnable() {
            @Override
            public void run() {
                for (ConvEntity c :convEntities) {
                    saveOnce(c);
                }
            }
        });
    }


    public void saveGroupData(List<GroupData> groupDataList) {
        runOnThread(new Runnable() {
            @Override
            public void run() {
                for (GroupData groupData :groupDataList) {
                    WeWorkConversation conversation =conversationDao.selectByRemoteId(groupData.roomid);
                    if (conversation==null) conversation =new WeWorkConversation();
                    conversation.conversationType=1;
                    conversation.createrId=groupData.createId;
                    conversation.createTime=groupData.createts;
                    conversation.exited= 1;
                    conversation.RID=groupData.roomid;
                    conversation.LID=groupData.id;
                    conversation.name=groupData.roomname;
                    conversation.searchTime=groupData.updatets;
                    conversation.modifyTime=groupData.updatets;
                    if (conversation.id>0){
                        MyLog.debug(TAG,"更新 数据库 会话信息 remoteid "+conversation.RID+" name ="+conversation.name );
                        conversationDao.update(conversation);
                    }else {
                        MyLog.debug(TAG,"插入 数据库 会话信息 remoteid "+conversation.RID+" name ="+conversation.name );
                        conversationDao.insert(conversation);
                    }
                }
            }
        });
    }

    /**
     * 获取 会话
     * @param name
     * @param createId
     * @param createTime
     * @return
     */
    public ConvEntity getConversation(String name,long createId,long createTime){
        WeWorkConversation workConversation=null;


         if (createId>0&&createTime>0){
             workConversation=   conversationDao.selectByNameAndCreateIDAndCreateTime(name,createId,createTime);
         }else if (createId>0){
             workConversation=   conversationDao.selectByNameAndCreateID(name,createId );
         }else if (createTime>0){
             workConversation =conversationDao.selectByNameAndCreateTime(name,createTime);
         }else {
             workConversation =conversationDao.selectByName(name);
         }
         if (workConversation==null )return null;
         ConvEntity convEntity =new ConvEntity();
         convEntity.id =workConversation.LID;
         convEntity.remoteId=workConversation.RID;
         convEntity.creatorId=workConversation.createrId;
         convEntity.createTime =workConversation.createTime;
         convEntity.modifyTime =workConversation.modifyTime;
         convEntity.name =workConversation.name;
         return convEntity;
    };
}
