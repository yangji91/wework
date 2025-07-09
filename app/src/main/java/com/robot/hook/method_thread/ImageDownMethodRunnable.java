package com.robot.hook.method_thread;



import android.os.SystemClock;

import com.robot.common.Global;
import com.robot.controller.resource.ResourceController;
import com.robot.hook.KeyConst;
import com.robot.util.MyLog;
import com.robot.util.ProxyUtil;
import com.robot.robothook.RobotHelpers;

import java.lang.reflect.Method;

/***
 *@author 
 *@date 2022/1/17
 *@description 子线程执行
 ****/
public class ImageDownMethodRunnable implements Runnable {
    private final String TAG = "ImageDownMethodRunnable";
    int contentType;
    String url;
    String aesKey;
    String authKey;
    long fileSize;
    String filePath;
    String file_name;
    String md5;
    int methodDelay = 20 * 1000;
    boolean success = false;

    ProxyUtil.ProxyStringResultCallBack callBack;
    private int fileDownReTry = 3;
    private int fileDownInvokeReTry = 3;

    public ImageDownMethodRunnable(int contentType, String url, String aesKey, String authKey, long fileSize, String filePath, String file_name, String md5, ProxyUtil.ProxyStringResultCallBack callBack) {
        this.contentType = contentType;
        this.url = url;
        this.aesKey = aesKey;
        this.authKey = authKey;
        this.fileSize = fileSize;
        this.filePath = filePath;
        this.file_name = file_name;
        this.md5 = md5;
        this.callBack = callBack;
    }


    Class clazzCommonCallBack;
    Class clazzDen;
    Class clazzIProgressCallback;
    Class[] paramClassList;
    Object[] paramList;

    @Override
    public void run() {
        clazzCommonCallBack = RobotHelpers.findClassIfExists(KeyConst.I_ICommonResultCallback, Global.loadPackageParam.classLoader);
        clazzDen = RobotHelpers.findClassIfExists(KeyConst.C_FileUpAndDownLoadEngine, Global.loadPackageParam.classLoader);
        clazzIProgressCallback = RobotHelpers.findClassIfExists(KeyConst.C_IProgressCallback, Global.loadPackageParam.classLoader);
        paramClassList = new Class[]{int.class, String.class, String.class, String.class, long.class, String.class, String.class, clazzIProgressCallback, clazzCommonCallBack};

        paramList = new Object[]{contentType, url, aesKey, authKey, fileSize, filePath, md5, null, ProxyUtil.GetProxyInstance(clazzCommonCallBack, new ProxyUtil.ProxyCallBack() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) {
                if ("onResult".equals(method.getName())) {
                    int ret = (int) objects[0];
                    MyLog.debug(TAG, "[downLoadMessageFile]" + " code:" + ret + " fileCompletePath =" + filePath);
                    if (ret != 0 && fileDownReTry > 0) {
                        fileDownReTry--;
                        invokeMethod();
                    } else {
                        ResourceController.putLocalPathByMD5(md5, filePath);
                        success = true;
                        callBack.onResult(ret, filePath);

                    }

                }
                return null;
            }
        })};
        while (fileDownInvokeReTry > 0 && !success) {
            fileDownInvokeReTry--;
            invokeMethod();
            SystemClock.sleep(methodDelay);

        }


    }

    private void invokeMethod() {
        Global.postRunnable2UI(new Runnable() {
                                   @Override
         public void run() {
           RobotHelpers.callStaticMethod(clazzDen, KeyConst.M_FileUpAndDownLoadEngine_DownloadFile, paramClassList, paramList);
            }
           }
        );

    }


}
