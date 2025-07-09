package com.robot.com;

import android.os.Environment;
import android.text.TextUtils;

import com.robot.common.MConfiger;
import com.robot.entity.PhoneLocEnum;
import com.robot.util.ACache;
import com.robot.util.FileUtil;
import com.robot.util.MyLog;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SDCardCleanUtil {
    private static final String TAG = SDCardCleanUtil.class.getSimpleName();
    private static String sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath();
    private static ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private static long WEIXINWORK_LOG_FILE_MAX_SIZE = FileUtil.GB * 1;
    private static long WEIXINWORK_RESOURCE_FILE_MAX_SIZE = FileUtil.GB * 4;
    private static long Robot_RESOURCE_FILE_MAX_SIZE = FileUtil.GB * 3;

    static {
        if (BuildConfig.build_env == 1) {
            WEIXINWORK_LOG_FILE_MAX_SIZE = FileUtil.GB;
            WEIXINWORK_RESOURCE_FILE_MAX_SIZE = FileUtil.GB;
            Robot_RESOURCE_FILE_MAX_SIZE = FileUtil.GB;
        }
    }

    private static final String[] robotFileDirectory = {

            sdcard + "/robot/com/cache",
            sdcard + "/robot/com/db",
            sdcard + "/robot/com/files",
            sdcard + "/robot/com/log",
            /*sdcard+"/robot/com/img",
            sdcard+"/robot/com/audio",
            sdcard+"/robot/com/file",
            sdcard+"/robot/com/imgthumb",
            sdcard+"/robot/com/video",*/

    };
    private static final String[] weixinWorkFileArray = {
            sdcard + "/Tencent/WeixinWork/imagecache", //
            sdcard + "/Tencent/WeixinWork/uploadTempMidbimage",
            sdcard + "/Tencent/WeixinWork/uploadTempThumbimage",
            sdcard + "/Tencent/WeixinWork/filecache",

    };
    private static final String[] weixinWorkLogFileArray = {
            sdcard + "/Tencent/WeixinWork/src_log",
            sdcard + "/Tencent/WeixinWork/src_clog",
    };

    public static void checkWeiXinWorkFileSize() {
        singleThreadExecutor.submit(new Runnable() {
            @Override
            public void run() {
                long logSize = 0l;
                logSize = getWeWorkLogSize();
                MyLog.debug(TAG, "[checkWeiXinWorkFileSize]" + " logSize " + FileUtil.getSizeStr(logSize) + "上限 " + FileUtil.getSizeStr(WEIXINWORK_LOG_FILE_MAX_SIZE));
                if (logSize > WEIXINWORK_LOG_FILE_MAX_SIZE) {//日志文件
                    for (String path : weixinWorkLogFileArray) {
                        MyLog.debug(TAG, "[checkWeiXinWorkFileSize]" + " 删除日志文件 " + path);
                        deleteAll(new File(path));
                    }
                }
                long sourceSize = FileUtil.FileClear.getFolderSize(new File(sdcard + "/Tencent"));
                MyLog.debug(TAG, "[checkWeiXinWorkFileSize]" + " sourceSize " + FileUtil.getSizeStr(sourceSize) + "上限 " + FileUtil.getSizeStr(WEIXINWORK_RESOURCE_FILE_MAX_SIZE));
                if (sourceSize > WEIXINWORK_RESOURCE_FILE_MAX_SIZE) {
                    deleteOldResourceFile(new File(sdcard + "/Tencent"));
                }
                long robotFileSize = FileUtil.FileClear.getFolderSize(new File(sdcard + "/robot/com"));
                MyLog.debug(TAG, "[checkWeiXinWorkFileSize]" + " robotFileSize " + FileUtil.getSizeStr(robotFileSize) + "上限 " + FileUtil.getSizeStr(Robot_RESOURCE_FILE_MAX_SIZE));
                if (robotFileSize > Robot_RESOURCE_FILE_MAX_SIZE) {//日志文件
                    charRobotWork();
                }
            }
        });

    }

    public static long getWeWorkLogSize() {
        long logSize = 0L;
        for (String path : weixinWorkLogFileArray) {
            logSize += FileUtil.FileClear.getFolderSize(new File(path));
        }
        return logSize;
    }

    private static void charRobotWork() {
        File file = new File(sdcard + "/robot/com");
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                file.delete();
            } else {
                if (file.isDirectory()) {
                    for (String path : robotFileDirectory) {
                        if (file.getAbsolutePath().startsWith(path))
                            return;
                    }
                    deleteOldResourceFile(file);
                }
            }
        }
    }

    private static void deleteOldResourceFile(File file) {
        if (file == null) return;
        MyLog.debug(TAG, "[deleteAll]" + " 路径 " + file.getAbsolutePath() + " 文件类型 isDirectory =" + file.isDirectory() + " ");
        if (file.isDirectory()) {
            if (file.list().length == 0) {
                file.delete();
                MyLog.debug(TAG, "[deleteAll]" + "删除文件夹 :" + file.getAbsolutePath(), true);
            } else {

                File[] files = file.listFiles();
                MyLog.debug(TAG, "[deleteAll]" + "files :" + files.length);
                for (int i = 0; i < files.length; i++) {
                    deleteOldResourceFile(files[i]);
                }
            }
        } else {
            boolean isOld = System.currentTimeMillis() - file.lastModified() >= 3 * ACache.TIME_DAY_MS;
            MyLog.debug(TAG, "[deleteAll]" + file.isFile() + "删除文件夹 :" + (isOld));
            if (isOld) {
                file.delete();
                MyLog.debug(TAG, "[deleteAll]" + "删除文件  :" + file.getAbsolutePath(), true);
            }
        }
    }


    //清理sd卡数据
    public static boolean clearCache() {
        boolean succ = false;
        File sdcard_filedir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);//得到sdcard的目录作为一个文件对象
        long usableSpace = sdcard_filedir.getUsableSpace();//获取文件目录对象剩余空间
        long cnt = 5;
        if (MConfiger.phoneLocEnum == PhoneLocEnum.BAIDU) {
            cnt = 8;
        } else if (MConfiger.phoneLocEnum == PhoneLocEnum.HUAWEI) {//华为的存储空间比较小
            cnt = 5;
        }
        if (BuildConfig.customConfigLog) {
            MyLog.debug(TAG, "[deleteAll]" + " check cnt:" + cnt, false);
        }
        if (usableSpace <= FileUtil.GB * cnt) { //内存空间小于5GB开始清理sd
            MyLog.debug(TAG, "[deleteAll]" + "开始清理图片...剩余空间 " + usableSpace, true);
            String path = robotFileDirectory[0];
            MyLog.debug(TAG, "[deleteAll]" + "删除文件夹:" + path, true);
            if (!TextUtils.isEmpty(path)) {
                MyLog.debug(TAG, "[deleteAll]" + "删除文件夹:" + path, true);
                File file = new File(path);
                File[] fArray = file.listFiles();
                if (fArray != null && fArray.length > 0) {
                    for (File f : fArray) {
                        if (f.isFile() && f.getAbsolutePath().endsWith(".png")) {
                            long time = file.lastModified();
                            if (time < (System.currentTimeMillis() - 60 * 1000 * 10))//创建十分钟以上的图片删除
                                f.delete();
                        }
                    }
                }
            }
            for (int i = 1; i < weixinWorkFileArray.length; i++) {
                String filePath = weixinWorkFileArray[i];
                File file = new File(filePath);
                if (file.exists()) {
                    deleteAll(file);
                }
            }
            succ = true;
        }
        return succ;
    }

    private static final void deleteAll(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] fileArray = file.listFiles();
                for (File f : fileArray) {
                    if (f != null) {
                        if (f.isDirectory()) {
                            MyLog.debug(TAG, "[deleteAll]" + "删除文件夹:" + f.getAbsolutePath(), true);
                            deleteAll(f);
                        } else {
                            f.delete();
                        }
                    }
                }
            } else {
                file.delete();
            }
        }
    }
}
