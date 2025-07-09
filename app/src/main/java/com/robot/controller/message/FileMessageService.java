package com.robot.controller.message;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import android.util.Log;
import com.robot.com.MApp;
import com.robot.common.MD5;
import com.robot.common.Global;
import com.robot.controller.resource.ResourceController;
import com.robot.hook.Main;
import com.robot.hook.method_thread.FtnImageDownMethodRunnable;
import com.robot.hook.method_thread.ImageDownMethodRunnable;
import com.robot.hook.msghandle.MsgHandleEnum;
import com.robot.util.FileUtil;
import com.robot.util.MyLog;
import com.robot.util.ProxyUtil;

import java.io.File;

/***
 *@author 
 *@date 2021/8/24
 *@description
 ****/
public class FileMessageService {
    public static String TAG = "FileMessageService";
    private int fileDownReTry = 3;

    /**
     * 企微下载 OSS文件
     * @param contentType
     * @param file_name
     * @param url
     * @param aesKey
     * @param authKey
     * @param fileSize
     * @param md5
     * @param callBack
     */
    public void downLoadMessageFile(int contentType,  String file_name, String url, String aesKey, String authKey, long fileSize, String md5, ProxyUtil.ProxyStringResultCallBack callBack) {

        if(checkFileIsExit(url,fileSize)){
            ResourceController.putLocalPathByMD5(md5,url);
            callBack.onResult(0, url);
            return;
        }

        String path = ResourceController.getLocalPathByMD5(md5);
        if (checkFileIsExit(path,fileSize)) {//如果文件已经加入缓存
            MyLog.debug(TAG, " 文件已经存在缓存中 "+path  );
            callBack.onResult(0, path);
            return;
        }

        String filePath =getAppScopedFilePath(contentType, md5,url,file_name);

        //转发的下载 指定下载位置
        MyLog.debug(TAG, "[download]" +   " url " + url + " filePath " + filePath + " aesKey=" + aesKey + " authKey=" + authKey + " fileSize =" + fileSize + " md5=" + md5, true);

        if (checkFileIsExit(filePath,fileSize)){//如果文件已经下载
            MyLog.debug(TAG, " 文件已经下载过了 "+filePath  );
            callBack.onResult(0,filePath);
            return;
        }
        Global.postRunnableDownLoadImg(new ImageDownMethodRunnable(contentType, url, aesKey, authKey, fileSize, filePath,file_name, md5,callBack));

    }

    private boolean checkFileIsExit(String path,long fileSize) {
        if (TextUtils.isEmpty(path)) return false;
        File file =  new File(path);
        if (file.isFile()&&file.canRead()){
                return file.length()==fileSize;
        }
        return false;
    }

    public void downLoadLocalFile(int contentType , String url, String field,String aesKey,String md5 ,String fileName,  long size,ProxyUtil.ProxyStringResultCallBack callBack){


        MyLog.debug(TAG,"[onInvokeMethod]" + " field:" + field +" \n url ="+url+ " aesKey ="+aesKey+" md5="+md5+" fileName "+fileName+" size "+size);
        String filePath = getAppScopedFilePath(contentType, md5,field,fileName);
        String path = ResourceController.getLocalPathByMD5(md5);
        if(checkFileIsExit(url,size)){
            ResourceController.putLocalPathByMD5(md5,url);
            callBack.onResult(0, url);
            return;
        }
        if (checkFileIsExit(path,size)) {//如果文件已经加入缓存
            MyLog.debug(TAG, " 下载local文件已经存在缓存中 "+path  );
            callBack.onResult(0, path);
            return;
        }
        if (checkFileIsExit(filePath,size)){//如果文件已经下载
            MyLog.debug(TAG, " 文件local已经下载过了 "+filePath  );
            callBack.onResult(0,filePath);
            return;
        }
        Global.postRunnableDownLoadImg(new FtnImageDownMethodRunnable(contentType,url,field,aesKey,md5,fileName,size,filePath,callBack));
       /* byte[] emptyByte = new byte[0];
        Class clazzFtnDownload = RobotHelpers.findClassIfExists(KeyConst.C_FTNDOWNLOAD_CALLBACK,loadPackageParam.classLoader);
        Class clazzFtnProgress = RobotHelpers.findClassIfExists(KeyConst.C_FTNPROGRO_CALLBACK,loadPackageParam.classLoader);
        Object objFtnDownload = Proxy.newProxyInstance(loadPackageParam.classLoader, new Class[]{clazzFtnDownload}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String name = method.getName();
                if ("onResult".equals(name)) {
                    int ret = (int) args[0];
                    String str = (String) args[1];
                    MyLog.debug(TAG, "[downLoadLocalFile]" + " code:" + ret + " fileCompletePath =" + filePath+" str="+str);
                    if (ret != 0 && fileDownReTry > 0) {
                        fileDownReTry--;
                        downLoadLocalFile(contentType, url,field, aesKey,  md5,fileName, size, callBack);
                    } else {
                        ResourceController.putLocalPathByMD5(md5,filePath);
                        callBack.onResult(ret, filePath);
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
                    MyLog.debug(TAG,"[onProgress]" + "..."+ Arrays.toString(args));
                }
                return null;
            }
        });
        Class clazzConvService = RobotHelpers.findClassIfExists("com.tencent.wework.foundation.logic.FileService",loadPackageParam.classLoader);
        Object convService = RobotHelpers.callStaticMethod(clazzConvService,"getService");
        Class[] classArray = {String.class,String.class,String.class,int.class,long.class,String.class,clazzFtnDownload,clazzFtnProgress,byte[].class,byte[].class,byte[].class};
        int fileType =4;//文件

        if (!TextUtils.isEmpty(field)&&field.startsWith("*1*")){
            Object[] objArray = {field,"","",contentType != 100,size,filePath,emptyByte,emptyByte,emptyByte,"",objFtnDownload,objFtnProgress};
            try {
                RobotHelpers.callMethod(convService, "FtnDownloadFileToPath",  objArray);
            }catch (Exception ee){
                MyLog.error(TAG,ee);
            }
        }else {
            if (MsgHandleEnum.isImgType(contentType)){
                fileType=2;//图片
            }else if (MsgHandleEnum.isVideoType(contentType)){
                fileType=5;//视频
            }
            Object[] objArray = {field,aesKey,md5,fileType,size,filePath,objFtnDownload,objFtnProgress,emptyByte,emptyByte,emptyByte};
            try {
                Global.postRunnable2UI(new Runnable() {
                    @Override
                    public void run() {
                        RobotHelpers.callMethod(convService, "CdnDownloadFileToPath", classArray, objArray);
                    }
                });

            }catch (Exception ee){
                MyLog.error(TAG,ee);
            }
        }*/


    }

    private static String getFilePath(int contentType,String md5,String url,String file_name){
        String filePath;
        long receiver=Global.getRemoteId();

        String sdcard = FileUtil.getSdcardPath();
        String baseImgPath = sdcard+"/Tencent/WeixinWork/imagecache/imagemsg2";
        String baseFilePath =sdcard+ "/Tencent/WeixinWork/filecache";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // 查询 Downloads 目录
            Uri uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;

            String[] projection = { MediaStore.Images.Media.ALBUM };

            Cursor cursor = Main.getMainActivity().getContentResolver().query(uri, projection, null, null, null);
            MyLog.debug("getFilePath", "Downloads Path: " + cursor); // 如：/storage/emulated/0/Download/

            if (cursor != null && cursor.moveToFirst()) {
                baseImgPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Downloads.DATA));
                baseFilePath = baseImgPath;
                cursor.close();
                MyLog.debug("getFilePath", "Downloads Path: " + baseImgPath); // 如：/storage/emulated/0/Download/
            }
        }

        if (TextUtils.isEmpty(md5)){
            md5 = MD5.getMessageDigest(url.getBytes());
        }
        if (MsgHandleEnum.isImgType(contentType)) { //image
            filePath =   baseImgPath + "/" + md5.charAt(0) + "/" + md5 + ".0";
        } else { //other file
            String md51 = MD5.getMessageDigest(url.getBytes());
            String md52 = MD5.getMessageDigest(md51.getBytes());
            if (MsgHandleEnum.isVideoType(contentType)) {
                filePath =   baseFilePath + "/" + receiver + "/" + md52.substring(0, 2) + "/" + md51 + "/" + md52 + ".mp4";
            } else {
                filePath =  baseFilePath + "/" + receiver + "/" + md52.substring(0, 2) + "/" + md51 + "/" + file_name;
            }
        }
        return filePath;
    }

    public static String getAppScopedFilePath( int contentType, String md5, String url, String fileName) {

        Context context = Main.getMainActivity();

        String filePath;
        if (TextUtils.isEmpty(md5)) {
            md5 = MD5.getMessageDigest(url.getBytes());
        }

        File baseDir;
        if (MsgHandleEnum.isImgType(contentType)) {
            baseDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "imagemsg2");
            filePath = new File(baseDir, md5.charAt(0) + "/" + md5 + ".0").getAbsolutePath();
        } else {
            baseDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "filecache");
            String md51 = MD5.getMessageDigest(url.getBytes());
            String md52 = MD5.getMessageDigest(md51.getBytes());

            File targetDir = new File(baseDir, md52.substring(0, 2) + "/" + md51);
            String fileNameFinal = MsgHandleEnum.isVideoType(contentType) ? md52 + ".mp4" : fileName;
            filePath = new File(targetDir, fileNameFinal).getAbsolutePath();
        }

        return filePath;
    }


}

