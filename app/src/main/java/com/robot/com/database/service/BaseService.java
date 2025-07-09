package com.robot.com.database.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/***
 *@author 
 *@date 2021/8/27
 *@description
 ****/
public abstract class BaseService<T> {


    public  static boolean enable=true;
    private static ExecutorService threadExecutor = Executors.newSingleThreadExecutor( );

    public void save(T object){
        if (enable)
        threadExecutor.submit(new Runnable() {
            @Override
            public void run() {
                saveOnce(object);
            }
        });
    }

     abstract void saveOnce(T object);

    /**
     * 子线程运行
     * @param runnable
     */
    public void runOnThread(Runnable runnable){
        if (enable)
        threadExecutor.submit(runnable);
    }


}
