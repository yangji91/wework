package com.robot.hook.imageloader;

import com.robot.entity.ParamLoadImageEntity;
import com.robot.entity.ResEntity;
import com.robot.hook.KeyConst;
import com.robot.hook.base.HookBaseMethod;
import com.robot.hook.base.IHookCallBack;
import com.robot.util.MyLog;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import com.robot.robothook.RobotHelpers;
import com.robot.robothook.LoadPackageParam;

public class HookImageLoaderSelfMethod extends HookBaseMethod<ParamLoadImageEntity> {

    //(String str, String str2, String str3, int i, long j, String str4, IFtnDownloadCallback iFtnDownloadCallback, IFtnProgressCallback iFtnProgressCallback, byte[] bArr, byte[] bArr2, byte[] bArr3
    @Override
    public void onHookInfo(Class clazz, LoadPackageParam loadPackageParam, IHookCallBack<ParamLoadImageEntity> callBack) {
        clazzFtnDownload = RobotHelpers.findClassIfExists(KeyConst.C_FTNDOWNLOAD_CALLBACK,loadPackageParam.classLoader);
        clazzFtnProgress = RobotHelpers.findClassIfExists(KeyConst.C_FTNPROGRO_CALLBACK,loadPackageParam.classLoader);
    }

    private Class clazzFtnDownload;
    private  Class clazzFtnProgress;

    @Override
    public boolean onInvokeMethod(Class classDb, LoadPackageParam loadPackageParam, IHookCallBack<ParamLoadImageEntity> paramCall) {
        boolean succ = false;
        if(paramCall == null){
            return succ;
        }
        ParamLoadImageEntity pEntity = paramCall.getParams();
        String field = pEntity.fieldId;
        String aeskey = pEntity.aesKey;
        String md5 = pEntity.md5;
        int fileType = pEntity.fileType;
        long size = pEntity.fileSize;
        String filePath = pEntity.filePath;
        byte[] emptyByte = new byte[0];
        MyLog.debug(TAG,"[onInvokeMethod]" + " field:" + field);
       /* String path =ResourceController.getLocalPathByMD5(pEntity.md5);
        if (!TextUtils.isEmpty(path)){
            pEntity.filePath=path;
            ResEntity resEntity = ResEntity.genSucc(pEntity);
            paramCall.onCall(resEntity);
            return true;
        }*/
        Object objFtnDownload = Proxy.newProxyInstance(loadPackageParam.classLoader, new Class[]{clazzFtnDownload}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String name = method.getName();
                if ("onResult".equals(name)) {
                   int ret = (int) args[0];
                   String str = (String) args[1];
                   if(ret == 0){
                       ResEntity resEntity = ResEntity.genSucc(pEntity);
                  //     ResourceController.putLocalPathByMD5(pEntity.md5,pEntity.filePath);
                       paramCall.onCall(resEntity);
                   }else{
                       ResEntity resEntity = ResEntity.genErr(pEntity,str);
                       paramCall.onCall(resEntity);
                   }
                }
                return null;
            }
        });
        Object objFtnProgress = Proxy.newProxyInstance(loadPackageParam.classLoader, new Class[]{clazzFtnProgress}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String name = method.getName();
                if ("onProgress".equals(name)) {
                    MyLog.debug(TAG,"[onProgress]" + "...");
                }
                return null;
            }
        });
        Class clazzConvService = RobotHelpers.findClassIfExists(KeyConst.C_FileService,loadPackageParam.classLoader);
        Object convService = RobotHelpers.callStaticMethod(clazzConvService,KeyConst.M_FileService_getService);
        Class[] classArray = {String.class,String.class,String.class,int.class,long.class,String.class,clazzFtnDownload,clazzFtnProgress,byte[].class,byte[].class,byte[].class};
        Object[] objArray = {field,aeskey,md5,fileType,size,filePath,objFtnDownload,objFtnProgress,emptyByte,emptyByte,emptyByte};
        try {
            RobotHelpers.callMethod(convService, KeyConst.M_FileService_CdnDownloadFileToPath, classArray, objArray);
            succ = true;
        }catch (Exception ee){
            MyLog.error(TAG,ee);
        }
        return succ;
    }

    //(String str, String str2, String str3, int i, long j, String str4, IFtnDownloadCallback iFtnDownloadCallback, IFtnProgressCallback iFtnProgressCallback,
    // byte[] bArr, byte[] bArr2, byte[] bArr3
}
