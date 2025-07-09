package com.robot.controller.resource;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.robot.common.MD5;

import com.robot.entity.ConvMember;
import com.robot.exception.MessageException;
import com.robot.http.HttpUtil;
import com.robot.http.entity.rsp.PTokenResultEntity;
import com.robot.util.ACache;
import com.robot.util.FileUtil;

import java.io.File;
import java.io.Serializable;

/***
 *@author 
 *@date 2021/8/9
 *@description 资源管理
 ****/
public class ResourceController {

    private static ACache mACache;


    public static enum MResource {
        IMAGE, VIDEO, AUDIO, VOICE, FILE,
    }


    public static void initCache(Context context) {
        mACache = ACache.get(new File(FileUtil.getComCachePath(), "ACache"));
    }


    /**
     * 获取token
     *
     * @return
     */
    public static PTokenResultEntity getOSSToken() {
        //return null;
        return (PTokenResultEntity) mACache.getAsObject("ossToken");
    }

    /**
     * 20分钟
     *
     * @param ossToken
     */
    public static void saveOSSToken(PTokenResultEntity ossToken) {
        mACache.put("ossToken", ossToken, ACache.TIME_HOUR / 3);
    }

    /**
     * 根据文件路径获取已经上传的文件url
     *
     * @param filePath
     * @return
     */
    public static String getUploadUrl(String filePath) {
        String asString = mACache.getAsString(filePath);
        if (asString == null) {
            return "";
        } else {
            return asString;
        }
    }

    /**
     * filePath 对应 url
     *
     * @param filePath
     * @param url
     */
    public static void putUploadUrl(String filePath, String url) {
        mACache.put(filePath, url, ACache.TIME_DAY);
    }

    public static void putGroupMember(long remoteId, ConvMember convMember) {
        mACache.put(String.valueOf(remoteId), JSON.toJSONString(convMember), ACache.TIME_DAY);
    }

    ;

    public static ConvMember getGroupMember(long remoteId) {
        String object = mACache.getAsString(String.valueOf(remoteId));
        return JSON.parseObject(object, ConvMember.class);
    }

    /**
     * 根据文件MD5取已经上传的文件url
     *
     * @param md5
     * @return public static String getUploadUrlByMD5(String md5) {
    return mACache.getAsString(md5);
    }
     */
    ;

    /**
     * 根据文件MD5取已经上传的文件url
     *
     * @param md5
     * @return public static void putUploadUrlByMD5(String md5, String url) {
    FileInfo fileInfo = new FileInfo(url);
    mACache.put(md5, fileInfo);
    }
     */
    ;

    /**
     * 根据下载路径获取已经本地的文件路径
     *
     * @param url
     * @return localPath
     */
    public static String getLocalPathByUrl(String url) {
        return mACache.getAsString(url);
    }

    /**
     * 根据下载路径获取已经本地的文件路径
     *
     * @param md5
     * @return localPath
     */
    public static String getLocalPathByMD5(String md5) {
       /* if (TextUtils.isEmpty(md5)) return null;
        Object   fileUpAndDownLoadEngine= RobotHelpers.callStaticMethod(RobotHelpers.findClass(KeyConst.C_FileUpAndDownLoadEngine,Global.loadPackageParam.classLoader ),"bMX");
        List<String> list = (List<String>) RobotHelpers.callMethod(fileUpAndDownLoadEngine,"rj",md5);
        if (list!=null){
            MyLog.debug("msgImage", JSON.toJSONString(list));
            for (String item:list){

                if (new File(item).canRead()){
                    return item;
                }
            }
        }*/
        return checkFileInfoByUrl(md5);
    }


    /**
     * 根据文件路径获取已经上传的文件url
     *
     * @param url
     * @param filePath
     */
    public static void putLocalPathByUrl(String url, String filePath) {
        FileInfo fileInfo = new FileInfo(url);
        mACache.put(url, filePath);
    }

    /**
     * 根据md5获取已经下载的文件url
     *
     * @param md5
     * @param filePath
     */
    public static void putLocalPathByMD5(String md5, String filePath) {
        mACache.put(md5, filePath, ACache.TIME_DAY);
    }

    public static String saveIntNetFile(String url, MResource resource) throws MessageException {
        return saveIntNetFile(url, null, resource);
    }

    public static String saveIntNetFile(String url, String name, MResource resource) throws MessageException {

        String fileFullPath = checkFileInfoByUrl(url);
        if (!TextUtils.isEmpty(fileFullPath)) {
            return fileFullPath;
        }
        switch (resource) {
            case IMAGE:
                fileFullPath = FileUtil.getSaveImageFilePath(MD5.toString(url));
                break;
            case VIDEO:
                fileFullPath = FileUtil.getSaveVideoFilePath(MD5.toString(url));
                break;
            case AUDIO:
                fileFullPath = FileUtil.getSaveAudioPath(MD5.toString(url));
                break;
            case VOICE:
                fileFullPath = FileUtil.getSaveVoicePath(MD5.toString(url));
                break;
            case FILE:
                fileFullPath = FileUtil.getSaveFileDownloadPath(name, MD5.toString(url));
                break;

        }
        fileFullPath = HttpUtil.saveIntNetFile(url, fileFullPath);
        FileInfo fileInfo = new FileInfo(fileFullPath);
        mACache.put(url, fileInfo);
        putUploadUrl(fileFullPath, url);
        if (TextUtils.isEmpty(fileInfo.md5)) {
            putLocalPathByMD5(fileInfo.md5, fileFullPath);
        }
        return fileFullPath;
    }

    private static String checkFileInfoByUrl(String url) {
        Object info = mACache.getAsObject(url);
        if (info instanceof FileInfo) {
            ResourceController.FileInfo fileInfo = (ResourceController.FileInfo) info;
            File file = new File(fileInfo.path);
            if (file.canRead() && file.length() == fileInfo.length)
                return fileInfo.path;
        } else if (info instanceof String) {
            return (String) info;
        }
        return null;
    }

    private static class FileInfo implements Serializable {
        public String path;
        public long length;
        public String md5;

        public FileInfo(String path) {
            this.path = path;
            File file = new File(path);
            if (file.canRead()) {
                this.length = file.length();
                if (length < 15 * 1024 * 1024) {
                    md5 = MD5.getFileMD5(path);
                }
            }

        }
    }
}
