package com.robot.com.database.service;

import com.robot.com.R;
import com.robot.com.database.AppDatabase;
import com.robot.com.database.dao.WeWorkFileInfoDao;
import com.robot.com.database.entity.WeWorkFileInfo;

/***
 *@author 
 *@date 2021/8/27
 *@description
 ****/
public class WeWorkFileInfoService extends BaseService<WeWorkFileInfo> {

    private static WeWorkFileInfoService instance;

    private WeWorkFileInfoDao dao;

    public WeWorkFileInfoService( ) {
        this.dao = AppDatabase.getWeWorkMessageAppDatabase().getWeWorkFileInfoDao();
    }

    public static WeWorkFileInfoService getInstance() {
        if (instance==null){
            instance =new WeWorkFileInfoService();
        }
        return instance;
    }

    @Override
      void saveOnce(WeWorkFileInfo object) {
            dao.insert(object);
    }
    public void updatePath(long msgId, String localPath, String url) {
        runOnThread(new Runnable() {
            @Override
            public void run() {
                WeWorkFileInfo fileInfo = dao.selectByMessageId(msgId);
                if (fileInfo!=null){
                    fileInfo.OSSUrl=url;
                    fileInfo.path=localPath;
                    fileInfo.completeTime =System.currentTimeMillis();
                    dao.update(fileInfo);
                }
            }
        });

    }
}
