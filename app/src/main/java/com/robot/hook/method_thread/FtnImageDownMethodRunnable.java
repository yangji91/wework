package com.robot.hook.method_thread;


import android.os.SystemClock;
import android.text.TextUtils;

import com.robot.common.Global;
import com.robot.controller.resource.ResourceController;
import com.robot.hook.KeyConst;
import com.robot.hook.msghandle.MsgHandleEnum;
import com.robot.util.MyLog;
import com.robot.util.ProxyUtil;
import com.robot.robothook.RobotHelpers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/***
 *@author 
 *@date 2022/1/17
 *@description 子线程执行
 ****/
public class FtnImageDownMethodRunnable implements Runnable {
    private final String TAG = "FtnImageDownMethodRunnable";

    int methodDelay = 20 * 1000;
    boolean success = false;
    int contentType;
    String url;
    String field;
    String aesKey;
    String md5;
    String fileName;
    long size;
    String filePath;
    ProxyUtil.ProxyStringResultCallBack callBack;

    public FtnImageDownMethodRunnable(int contentType, String url, String field, String aesKey, String md5, String fileName, long size, String filePath, ProxyUtil.ProxyStringResultCallBack callBack) {
        this.contentType = contentType;
        this.url = url;
        this.field = field;
        this.aesKey = aesKey;
        this.md5 = md5;
        this.fileName = fileName;
        this.size = size;
        this.filePath = filePath;
        this.callBack = callBack;
    }

    private int fileDownReTry = 3;
    private int fileDownInvokeReTry = 3;


    Class clazzFtnDownload;
    Class clazzFtnProgress;
    Class[] paramClassList;
    Object[] paramList;

    @Override
    public void run() {
        byte[] emptyByte = new byte[0];
        clazzFtnDownload = RobotHelpers.findClassIfExists(KeyConst.C_FTNDOWNLOAD_CALLBACK, Global.loadPackageParam.classLoader);
        clazzFtnProgress = RobotHelpers.findClassIfExists(KeyConst.C_FTNPROGRO_CALLBACK, Global.loadPackageParam.classLoader);
        Object objFtnDownload = Proxy.newProxyInstance(Global.loadPackageParam.classLoader, new Class[]{clazzFtnDownload}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String name = method.getName();
                if (KeyConst.M_FTNPROGRO_CALLBACK_onResult.equals(name)) {
                    int ret = (int) args[0];
                    String str = (String) args[1];
                    MyLog.debug(TAG, "[downLoadLocalFile]" + " code:" + ret + " fileCompletePath =" + filePath + " str=" + str);
                    if (ret != 0 && fileDownReTry > 0) {
                        fileDownReTry--;
                        invokeMethod();
                    } else {
                        success = true;
                        ResourceController.putLocalPathByMD5(md5, filePath);
                        callBack.onResult(ret, filePath);

                    }
                }
                return null;
            }
        });
        Object objFtnProgress = Proxy.newProxyInstance(Global.loadPackageParam.classLoader, new Class[]{clazzFtnProgress}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String name = method.getName();
                if ("onProgress".equals(name)) {
                    MyLog.debug(TAG, "[onProgress]" + "..." + Arrays.toString(args));
                }
                return null;
            }
        });

        paramClassList = new Class[]{String.class, String.class, String.class, int.class, long.class, String.class, clazzFtnDownload, clazzFtnProgress, byte[].class, byte[].class, byte[].class};
        int fileType = 4;//文件

        if (!TextUtils.isEmpty(field) && field.startsWith("*1*")) {
            paramList = new Object[]{field, "", "", contentType != 100, size, filePath, emptyByte, emptyByte, emptyByte, "", objFtnDownload, objFtnProgress};
        } else {
            if (MsgHandleEnum.isImgType(contentType)) {
                fileType = 2;//图片
            } else if (MsgHandleEnum.isVideoType(contentType)) {
                fileType = 5;//视频
            }
            paramList = new Object[]{field, aesKey, md5, fileType, size, filePath, objFtnDownload, objFtnProgress, emptyByte, emptyByte, emptyByte};
        }
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
                Class clazzConvService = RobotHelpers.findClassIfExists(KeyConst.C_FileService, Global.loadPackageParam.classLoader);
                Object convService = RobotHelpers.callStaticMethod(clazzConvService, KeyConst.M_FileService_getService);
                if (!TextUtils.isEmpty(field) && field.startsWith("*1*")) {
                    RobotHelpers.callMethod(convService, KeyConst.M_FileService_FtnDownloadFileToPath, paramList);
                } else {
                    RobotHelpers.callMethod(convService, KeyConst.M_FileService_CdnDownloadFileToPath, paramClassList, paramList);
                }

            }
        });

    }


}
