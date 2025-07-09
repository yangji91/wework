package com.robot.http;

import android.os.SystemClock;
import android.text.TextUtils;

import com.robot.common.MConfiger;
import com.robot.entity.ResEntity;
import com.robot.http.task.base.BaseTask;
import com.robot.http.task.base.ReqBaseEntity;
import com.robot.util.MyLog;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HttpEngine {
    private final String TAG = getClass().getSimpleName();

    private static HttpEngine instance;
    private Thread mThread;
    private boolean isRunning;
    private ConcurrentLinkedQueue<BaseTask> mVector;

    private HttpEngine(){
        mVector = new ConcurrentLinkedQueue<>();
        isRunning = true;
        initThread();
    }

    public static final HttpEngine getInstance(){
        if(instance == null){
            instance = new HttpEngine();
        }
        return instance;
    }

    private void initThread(){
        mThread = new Thread(TAG){
            @Override
            public void run() {
                super.run();
                while(isRunning){
                    if(mVector.size() > 0){
                       final BaseTask taskItem = mVector.poll();
                        doTask(taskItem);
                    }else {
                        SystemClock.sleep(100);
                    }
                   /* synchronized (mVector) {

                        BaseTask taskItem = mVector.poll();
                        if (taskItem!=null){
                            doTask(taskItem);
                        }
                    }
                    SystemClock.sleep(100);

                    */
                }
            }
        };
        mThread.start();
    }

    private boolean doTask(BaseTask task){
        ReqBaseEntity reqBaseEntity = (ReqBaseEntity) task.getReqEntity();
        String url = reqBaseEntity.getReqURL();
        boolean isFullURL = reqBaseEntity.isFullURL;
        if(!isFullURL){
            String host = reqBaseEntity.hostUrl;
            if(!TextUtils.isEmpty(host)){
                url = host + reqBaseEntity.getReqURL();
            }else{
                url = MConfiger.getHttpUrl(url);
            }
        }
        boolean isPostJson = reqBaseEntity.isPostJson;
        Map<String,Object> mReqMap = reqBaseEntity.getReqMap();
        ResEntity<String> resEntity;
        if(isPostJson){
            resEntity = HttpUtil.sendPostMsgByJsonIsCircle(url,mReqMap,"UTF-8",reqBaseEntity.headerInfo,task.isCircleGet);
        }else{
            resEntity = HttpUtil.sendGetMessage(url,mReqMap,"UTF-8",reqBaseEntity.isGet,task.isCircleGet);
        }
        if(mReqMap != null && mReqMap.size() > 0){
            for(Map.Entry<String,Object> entry : mReqMap.entrySet()){
                String key = entry.getKey();
                Object val = entry.getValue();
                MyLog.debug(TAG,"[doTask]" + " key:" + key + " val:" + val);
            }
        }
        MyLog.debug(TAG,"[doTask]" + " url:" + url + " clazz:" + task.getReqEntity().getClass() + " rsp:" + resEntity.getData() + " isGet:" + reqBaseEntity.isGet);
        task.doTask(resEntity);
        return resEntity.isSucc();
    }


    public void addTask2Queue(BaseTask task) {
        mVector.add(task);
      /*  synchronized (mVector){
            mVector.add(task);
            mVector.notifyAll();
        }*/
    }
}
