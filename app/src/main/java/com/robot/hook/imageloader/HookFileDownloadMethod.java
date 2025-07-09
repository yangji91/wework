package com.robot.hook.imageloader;

import android.text.TextUtils;

import com.robot.common.MD5;
import com.robot.robothook.RobotMethodParam;
import com.robot.common.Global;
import com.robot.entity.ParamLoadImageEntity;
import com.robot.entity.ResEntity;
import com.robot.hook.KeyConst;
import com.robot.hook.base.HookBaseMethod;
import com.robot.hook.base.IHookCallBack;
import com.robot.com.BuildConfig;
import com.robot.util.FileUtil;
import com.robot.util.MyLog;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import com.robot.robothook.RobotMethodHook;

import com.robot.robothook.RobotHelpers;
import com.robot.robothook.LoadPackageParam;

public class HookFileDownloadMethod extends HookBaseMethod<ParamLoadImageEntity> {

    public HookFileDownloadMethod(){

    }

    @Override
    public void onHookInfo(Class clazz, LoadPackageParam loadPackageParam, IHookCallBack<ParamLoadImageEntity> callBack) {
        Class clzFileUpAndDownLoadEngine = RobotHelpers.findClassIfExists(KeyConst.C_FileUpAndDownLoadEngine, loadPackageParam.classLoader);
        //sendImageMessage
        RobotHelpers.hookAllMethods(clzFileUpAndDownLoadEngine, KeyConst.M_FileUpAndDownLoadEngine_DownloadFile, new RobotMethodHook() {
            @Override
            protected void afterHookedMethod(RobotMethodParam param) throws Throwable {
                super.afterHookedMethod(param);
                if(BuildConfig.customConfigLog){
                    StringBuilder builder = buildStrBuilder(param);
                    MyLog.debug(TAG,"[HookSendImgMethod]2" + " builder:" + builder.toString());
                }
            }
        });
    }

    @Override
    public boolean onInvokeMethod(Class classDb, LoadPackageParam loadPackageParam, IHookCallBack<ParamLoadImageEntity> paramCall) {
        //this.callBack = paramCall
        boolean succ = false;
        Object objCallBack;
        if(paramCall != null){
            ParamLoadImageEntity pEntity = paramCall.getParams();
            //this.pEntity = pEntity;
            long cvId = pEntity.converId;//
            int i = pEntity.contentType; //101
            String url = pEntity.url; // "https://imunion.weixin.qq.com/cgi-bin/mmae-bin/tpdownloadmedia?param=v1_859801cd3cd3c5380f8cd71b4d6b4fbdddc561f06b94af785766d6fe394c4edfcde24731baa8500361aac2e4fb468ce4c9f0ec0b7ea4cf61488bd15a73ab43a1a15d1c28abfdbf37d36fad86500ebb9568b8e269a73df4a6c7068074d4a5c204e6ad01d276dd635ef7f22493ac7b7ada0466e957e555fc89c929f5956d07576eb7250ae26a0e1dc564c9d63e64acf60dbfd602c03cea70e65c20d02c227024ecdb0611263cebee750324e7519e0adff4013602d4af77ea950748309744586c07b620214fa019b6045ee3f7e1270dc364fbd03725bca3f08f72cfee8e93bebb8e2f9ef17855ccd9494106af5761689168324a1a307f965b4b5e39c09c0000a06a";
            String aesKey = pEntity.aesKey; // "9c598362b234ee16d33e7d841ddf2f3f";
            String authKey = pEntity.authKey; //"v1_859801cd3cd3c5380f8cd71b4d6b4fbdddc561f06b94af785766d6fe394c4edf702057645d6bba2eaf7bd0c3e014ded9972e662a4c1154fa794c37d31add195a";
            long fileSize = pEntity.fileSize;// 247567l;
            String filePath = pEntity.filePath;
            String md5 = pEntity.md5;

//            String url = "https://imunion.weixin.qq.com/cgi-bin/mmae-bin/tpdownloadmedia?param=v1_859801cd3cd3c5380f8cd71b4d6b4fbdddc561f06b94af785766d6fe394c4edfcde24731baa8500361aac2e4fb468ce4c9f0ec0b7ea4cf61488bd15a73ab43a1a15d1c28abfdbf37d36fad86500ebb9568b8e269a73df4a6c7068074d4a5c204e6ad01d276dd635ef7f22493ac7b7ada0466e957e555fc89c929f5956d07576eb7250ae26a0e1dc564c9d63e64acf60dbfd602c03cea70e65c20d02c227024ecdb0611263cebee750324e7519e0adff4013602d4af77ea950748309744586c07b620214fa019b6045ee3f7e1270dc364fbd03725bca3f08f72cfee8e93bebb8e2f9ef17855ccd9494106af5761689168324a1a307f965b4b5e39c09c0000a06a";
//            String aesKey = "9c598362b234ee16d33e7d841ddf2f3f";
//            String authKey = "v1_859801cd3cd3c5380f8cd71b4d6b4fbdddc561f06b94af785766d6fe394c4edf702057645d6bba2eaf7bd0c3e014ded9972e662a4c1154fa794c37d31add195a";
//            long fileSize = 0l;  //247567l
//            String filePath = pEntity.filePath;
//            String md5 = pEntity.md5;

            String baseImgPath = "/Tencent/WeixinWork/imagecache/imagemsg";
            String baseFilePath = "/Tencent/WeixinWork/filecache";
            //FileUtil.deleteFile(filePath);
            String sdcard = FileUtil.getSdcardPath();
            //转发的下载 指定下载位置
            MyLog.debug(TAG,"[download]" + " pEntity.filePath:" + pEntity.filePath+" sdcard:"+sdcard+"url "+url,true);
            if (TextUtils.isEmpty(pEntity.filePath )||!pEntity.filePath.startsWith(sdcard)) {
                if (pEntity.contentType == 101) { //image
                    String p = sdcard + baseImgPath;
                    FileUtil.CreateDir(p);
                    md5 = MD5.getMessageDigest(url.getBytes());
                    filePath = p +"/"+md5+".jpg";
                  // filePath = p + "/" + md5.charAt(0) + "/" + md5 + ".0";
                    pEntity.filePath = filePath;
                } else if (pEntity.contentType == 102) { //other file
                    String p = sdcard + baseFilePath;
                    FileUtil.CreateDir(p);
                    filePath = p + "/" + filePath;
                    pEntity.filePath = filePath;
                }
            }
           /* String path = ResourceController.getLocalPathByMD5(pEntity.md5);
            if (TextUtils.isEmpty(path)){
                pEntity.filePath=path;
                ResEntity resEntity = ResEntity.genSucc(pEntity);
                paramCall.onCall(resEntity);
                return true;
            }*/
            Class clazzCommonCallBack = RobotHelpers.findClassIfExists(KeyConst.I_ICommonResultCallback,Global.loadPackageParam.classLoader);
            {
                Object objCommonCallBack = Proxy.newProxyInstance(loadPackageParam.classLoader, new Class[]{clazzCommonCallBack}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        String name = method.getName();
                        if (KeyConst.M_ICommonResultCallback_onResult.equals(name)) {
                            int ret = (int) args[0];
                            MyLog.debug(TAG,"[onResult]" + " code:" + ret);
                            if(ret == 0){
                            ResEntity resEntity = ResEntity.genSucc(pEntity);
                                /*   ResourceController.putLocalPathByMD5( pEntity.md5,pEntity.filePath);*/
                                paramCall.onCall(resEntity);
                            }else{
                                ResEntity resEntity = ResEntity.genErr(pEntity);
                                paramCall.onCall(resEntity);
                            }
                        }
                        return null;
                    }
                });
                objCallBack = objCommonCallBack;
            }

            Class clazzDen = RobotHelpers.findClassIfExists(KeyConst.C_FileUpAndDownLoadEngine, loadPackageParam.classLoader);
            Class clazzIProgressCallback = RobotHelpers.findClassIfExists(KeyConst.C_IProgressCallback,loadPackageParam.classLoader);
            Class[] clazzArray = {int.class,String.class,String.class,String.class, long.class,String.class,String.class,clazzIProgressCallback,clazzCommonCallBack};
            Object[] objArray = {i,url,aesKey,authKey, fileSize,filePath,md5, null,objCallBack};

            try {
               RobotHelpers.callStaticMethod(clazzDen, KeyConst.M_FileUpAndDownLoadEngine_DownloadFile, clazzArray, objArray);
                succ = true;
                MyLog.debug(TAG,"[afterHookedMethod]" + " final succ :" + succ);
                return succ;
            }catch (Exception e) {
            MyLog.debug(TAG,"[afterHookedMethod]" + " final e :" + MyLog.getThrowableTask(e),true);
            ResEntity<ParamLoadImageEntity> resEntity = ResEntity.genErr(pEntity,"下载图片失败！");
            paramCall.onCall(resEntity);
            }
        }
        return succ;
    }
}
