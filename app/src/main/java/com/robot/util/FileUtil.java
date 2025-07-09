package com.robot.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Pair;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.tencent.smtt.utils.Md5Utils;
import com.robot.entity.UserEntity;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtil {
    public static final String TAG = FileUtil.class.getSimpleName();

    public static String COM_ROBOT_PATH = "robot/com";
    public static String COM_ROBOT_REQ_PATH = "req";
    public static String COM_ROBOT_RES_PATH = "res";
    public static String REQ_OPEN_WX_FILE = "open_wx.txt";
    public static String CURRENT_ID_FILENAME = "current_id";
    public static String PATH_USERACC = "acc_wx_work.txt";
    public static String PATH_USER_LOGIN = "acc_wx_work_login.txt";
    public static String PATH_NETTY_FlAG_FILE = "netty_flag.txt";
    public static String PATH_FILE_LOG = "file_flag_log.txt";
    public static String PATH_ADD_FRIENDS_FLAG = "add_friends_flag.txt";
    public static String PATH_IMG_THUMB = "imgthumb";
    public static String PATH_IMG = "img";
    public static String PATH_COMPRESSIMG = "compressImg";
    public static String PATH_VIODE = "video";
    public static String PATH_AUDIO = "audio";
    public static String PATH_VOICE = "silk";
    public static String PATH_RECORD = "record";
    public static String PATH_SYSTEM_RECORD = "call_rec";
    public static String PATH_LOG = "log";
    public static String PATH_FILE = "file";
    public static String PATH_CACHE = "cache";
    public static long KB = 1024;
    public static long MB = KB * 1024L;
    public static long GB = MB * 1024L;


    static {
        String sdcardPath = getSdcardPath();
        if (sdcardPath != null) {
            File file = new File(sdcardPath, PATH_USERACC);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String robotPath = getComRobotPath();
        {
            File file = new File(robotPath);
            if (file != null && !file.exists()) {
                file.mkdirs();
            }
        }
        //
        if (robotPath != null) {
            File nettyFile = new File(robotPath, PATH_NETTY_FlAG_FILE);
            if (!nettyFile.exists()) {
                try {
                    nettyFile.createNewFile();
                    saveNettyFlag("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //heart time info
        if (robotPath != null) {
            File heartFile = new File(robotPath, CommonUtil.PATH_FILE_WECHAT_HEART);
            if (!heartFile.exists()) {
                try {
                    heartFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (robotPath != null) {
            File heartFile = new File(robotPath, CommonUtil.PATH_FILE_WECOM_HEART);
            if (!heartFile.exists()) {
                try {
                    heartFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //video
        if (robotPath != null) {
            File file = new File(robotPath, PATH_VIODE);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        String logPath = getComLogPath();
        File fileLog = new File(logPath);
        if (!fileLog.exists()) {
            fileLog.mkdirs();
        }
        //audio
        if (robotPath != null) {
            File file = new File(robotPath, PATH_AUDIO);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        String imgThumbFilePath = getComImgThumbPath();
        if (!TextUtils.isEmpty(imgThumbFilePath)) {
            File file = new File(imgThumbFilePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        String imgDir = getComImgPath();
        if (!TextUtils.isEmpty(imgDir)) {
            File file = new File(imgDir);
            if (!file.exists()) {
                file.mkdirs();
            }
        }

        //user_acc_work_login.txt
        if (robotPath != null) {
            File file = new File(robotPath, PATH_USER_LOGIN);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //FILE LOG
        if (sdcardPath != null) {
            File file = new File(sdcardPath, PATH_FILE_LOG);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        }

        if (sdcardPath != null) {
            File file = new File(sdcardPath, PATH_ADD_FRIENDS_FLAG);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        }
    }

    public static final void saveFileLog(String path, boolean flag) {
        String sdcardPath = getSdcardPath();
        File file = new File(sdcardPath, path);
        if (file != null && file.exists()) {
            saveData(file, flag + "");
        }
    }

    public static final boolean getFileLogFlag(String path) {
        boolean result = false;
        String sdcardPath = getSdcardPath();
        File file = new File(sdcardPath, path);
        if (file != null) {
            String str = loadData(file);
            if (!TextUtils.isEmpty(str) && str.equals("true")) {
                result = true;
            }
        }
        return result;
    }

    //////////////sdcard////////////
    public static String getSdcardPath() {
        String sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        return sdcard;

    }

    public static boolean isLocalPath(String path) {
        if (TextUtils.isEmpty(path)) return false;
        if (path.startsWith("/sdcard") || path.startsWith(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath())) {
            return true;
        }

        return false;

    }
    ////////file common function/////


    /**
     * create dir
     *
     * @param dirPath
     */
    public static void CreateDir(String dirPath) {
        File file = new File(dirPath);
        if (file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            return;
        }
        file.mkdirs();
    }

    /**
     * create file
     *
     * @param fullPath
     * @return
     */
    public static File CreateFile(String fullPath) {
        File file = new File(fullPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * create file
     *
     * @param path
     * @param name
     * @return
     */
    public static File CreateFile(String path, String name) {
        File file = new File(path, name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * create file
     *
     * @param file
     */
    public static void CreateFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * delete file
     *
     * @param file
     */
    public static void deleteFile(File file) {
        file.delete();
    }

    /**
     * delete file
     *
     * @param path
     */
    public static void deleteFile(String path) {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
    }

    /**
     * save data to file
     *
     * @param file
     * @param dataStr
     */
    public static void saveData(File file, String dataStr) {
//        File file = new File(PATH_USERACC);
//        String info = userEntity.toString();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(dataStr.getBytes());
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void saveUserLoginInfo(UserEntity userEntity) {
        FileOutputStream out = null;
        try {
            String sdcard = getComRobotPath();
            File file = new File(sdcard, PATH_USER_LOGIN);
            out = new FileOutputStream(file);
            Gson gson = new Gson();
            String str = gson.toJson(userEntity);
            out.write(str.getBytes());
            out.flush();
        } catch (Exception ee) {
            ee.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static UserEntity loadUserLoginInfo() {
        UserEntity rEntity = null;
        try {
            String sdcard = getComRobotPath();
            File file = new File(sdcard, PATH_USER_LOGIN);
            FileInputStream in = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int cnt = in.read(buffer);
            StringBuilder builder = new StringBuilder();
            if (cnt <= 0) {
                return rEntity;
            } else {
                while (cnt > 0) {
                    String str = new String(buffer, 0, cnt);
                    cnt = in.read(buffer);
                    builder.append(str);
                }
                String str = builder.toString();
                Gson gson = new Gson();
                rEntity = gson.fromJson(str, UserEntity.class);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rEntity;
    }

    /**
     * load data from file
     *
     * @param file
     * @return
     */
    public static String loadData(File file) {
        try {
            FileInputStream in = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int cnt = in.read(buffer);
            if (cnt <= 0) {
                return null;
            } else {
                String str = new String(buffer, 0, cnt);
                return str;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    ///////////bussiness function///////

    public static String getComRobotPath() {
        String sdcard = getSdcardPath();
        if (TextUtils.isEmpty(sdcard)) {
            return null;
        }
        return sdcard + File.separator + COM_ROBOT_PATH;
    }

    public static String getComImgPath() {
        String path = getComRobotPath();
        return path + File.separator + PATH_IMG;
    }

    public static String getCompressComImgPath() {
        String path = getComRobotPath();
        return path + File.separator + PATH_COMPRESSIMG;
    }

    public static String getComLogPath() {
        String path = getComRobotPath();
        return path + File.separator + PATH_LOG;
    }

    public static String getComAudioPath() {
        String path = getComRobotPath();
        return path + File.separator + PATH_AUDIO;
    }

    public static String getComVoicePath() {
        String path = getComRobotPath();
        return path + File.separator + PATH_VOICE;
    }

    public static String getComRecordPath() {
        String path = getComRobotPath();
        return path + File.separator + PATH_RECORD;
    }

    public static String getMIUISystemRecordPath() {
        String sdcard = getSdcardPath();
        if (TextUtils.isEmpty(sdcard)) {
            return null;
        }
        return sdcard + File.separator + "MIUI" + File.separator + "sound_recorder" + File.separator + PATH_SYSTEM_RECORD;
    }

    public static String getComImgThumbPath() {
        String path = getComRobotPath();
        return path + File.separator + PATH_IMG_THUMB;
    }

    public static String getComVideoPath() {
        return getComRobotPath() + File.separator + PATH_VIODE;
    }

    public static String getComilePath() {
        return getComRobotPath() + File.separator + PATH_FILE;
    }

    public static String getComCachePath() {
        String path = getComRobotPath() + File.separator + PATH_CACHE;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * @param reqOrRes "req" or "res"
     * @return
     */
    public static String[] getFileArray(String reqOrRes) {

        String robotPath = getComRobotPath();
        if (robotPath == null) {
            return null;
        }

        File file = new File(robotPath + File.separator + reqOrRes);
        String[] files = file.list();
        return files;

    }

    /**
     * "req" or "res"
     *
     * @param reqOrRes
     * @return
     */
    public static List<String> getRequestList(String reqOrRes) {
        List<String> nameList = new ArrayList<>();

        String[] files = getFileArray(reqOrRes);
        if (files != null && files.length > 0) {
            for (String fileName : files) {
                nameList.add(fileName);
            }
        }
        return nameList;

    }

    /**
     * @param flag
     */
    public static final void saveNettyFlag(String flag) {
        String robotPath = getComRobotPath();
        if (robotPath == null) {
            return;
        }
        File file = new File(robotPath, PATH_NETTY_FlAG_FILE);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(flag.getBytes());
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void deleteReqFiles() {
        String robotPath = FileUtil.getComRobotPath();
        String reqPath = robotPath + File.separator + FileUtil.COM_ROBOT_REQ_PATH;

        File file = new File(reqPath);
        if (file != null && file.isDirectory()) {
            File[] fs = file.listFiles();
            if (fs != null && fs.length > 0) {
                for (File f : fs) {
                    f.delete();
                }
            }
        }
    }

    public static final Double getFileStr(long size) {
        double r = size / (double) (1000 * 1000);
        return r;
    }


    /**
     * 获取图片二进制
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(String filename) throws IOException {
        File f = new File(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }

    public static final String getSizeStr(long size) {
        String str = "";
        DecimalFormat formate = new DecimalFormat("0.00");
        if (size / GB > 0) {
            float result = (float) size / (float) GB;
            str = formate.format(result) + "GB";
        } else if (size / MB > 0) {
            float result = (float) size / (float) MB;
            str = formate.format(result) + "MB";
        } else if (size / KB > 0) {
            float result = (float) size / (float) KB;
            str = formate.format(result) + "KB";
        } else if (size <= 0) {
            str = "0MB";
        } else {
            str = size + "B";
        }
        return str;
    }

    public static final void saveWecomHeartBeatTime(long time) {
        String robotPath = FileUtil.getComRobotPath();
        File file = new File(robotPath, CommonUtil.PATH_FILE_WECOM_HEART);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        saveData(file, time + "");
    }

    public static final void saveWechatHeartBeatTime(long time) {
        String robotPath = FileUtil.getComRobotPath();
        File file = new File(robotPath, CommonUtil.PATH_FILE_WECHAT_HEART);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        saveData(file, time + "");
    }

    /**
     * 获取心跳时间
     *
     * @return
     */
    public static final long getWecomHeartBeatTime() {
        long time = 0;
        String robotPath = FileUtil.getComRobotPath();
        File file = new File(robotPath, CommonUtil.PATH_FILE_WECOM_HEART);
        if (file.exists()) {
            String str = loadData(file);
            if (!TextUtils.isEmpty(str) && TextUtils.isDigitsOnly(str)) {
                time = Long.valueOf(str);
            }
        }
        return time;
    }

    public static final String getHuaWeiYunCode() {
        String code = null;
        String path = "/sdcard/storm/clickid.txt";
        File file = new File(path);
        if (file.exists()) {
            String str = loadData(file);
            if (str != null) code = str.replaceAll("\\s*", "");
        }
        return code;
    }

    public static final String getBaiduPhoneCode() {
        String code = "";
        String path = "/data/misc/device.prop";
        File file = new File(path);
        MyLog.debug(TAG, "[getBaiduPhoneCode]" + " file.exist:" + file.exists() + " path:" + path);
        if (file.exists()) {
            String str = loadData(file);
            Map<String, String> mMap = new HashMap<>();
            if (!TextUtils.isEmpty(str)) {
                String[] array1 = str.split("\n");
                for (String s : array1) {
                    String[] array = s.split("=");
                    if (array != null && array.length == 2) {
                        String key = array[0];
                        String val = array[1];
                        if (key != null) {
                            key = key.trim();
                        }
                        if (val != null) {
                            val = val.trim();
                        }
                        mMap.put(key, val);
                    }
                }
            }
            if (mMap.size() > 0) {
                code = mMap.get("code2");
                if (code == null || code.isEmpty()) {
                    code = mMap.get("code");
                }
            }
        }
        return code;
    }

    /**
     * 拷贝文件
     *
     * @param srcPath
     * @param destPath
     */
    public static final String copyFile(String srcPath, String destPath, String salt) {
        String result = null;
        File file = new File(destPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileInputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(srcPath);
            outputStream = new FileOutputStream(destPath);
            byte[] bs = new byte[1024 * 1024];
            int cnt = 0;
            try {
                while ((cnt = inputStream.read(bs)) != -1) {
                    outputStream.write(bs, 0, cnt);
                }
                //写入salt
                if (!TextUtils.isEmpty(salt)) {
                    outputStream.write(salt.getBytes());
                }
                result = destPath;
                outputStream.flush();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String g_logFileName = StrUtils.getTimeStr(System.currentTimeMillis()) + "_" + System.currentTimeMillis() + "_log.txt";

    //================================================================
    public static final void logInfo(String TAG, String info) {
        /*
        long curTime = System.currentTimeMillis();
        String timeStr = StrUtils.getTimeStr(curTime);
        String g_logFileName = timeStr+"_"+System.currentTimeMillis()+"_log.txt";
         */
        String path = getComLogPath();
        //check file
        File fileDir = new File(path);
        File[] fileArray = fileDir.listFiles();
        if (fileArray != null && fileArray.length > 0) {
            int size = fileArray.length;
            if (size >= 20) {
                //删除前10个
                fileArray = listFileSortByModifyTime(fileArray);
                for (int i = 0; i < 10; i++) {
                    File f = fileArray[i];
                    if (f.exists()) {
                        f.delete();
                    }
                }
            }
        }
        path = path + File.separator + StrUtils.getTimeStr(System.currentTimeMillis());
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //添加append日志
        String str = " [" + TAG + "] " + info + "\n";
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true));
            out.write(str);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取目录下所有文件(按时间排序)
     *
     * @param list
     * @return 时间从小到大
     */
    public static File[] listFileSortByModifyTime(File[] list) {

        if (list != null && list.length > 0) {
            Arrays.sort(list, new Comparator<File>() {
                public int compare(File file, File newFile) {
                    if (file.lastModified() < newFile.lastModified()) {
                        return -1;
                    } else if (file.lastModified() == newFile.lastModified()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });
        }
        return list;
    }

    /**
     * 创建图片路径
     *
     * @param fileNameStr
     * @return
     */
    public static String getSaveImageFilePath(String fileNameStr) {
        String imgPath = FileUtil.getComImgPath();
        File file = new File(imgPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePathStr = imgPath + File.separator + fileNameStr + ".png";
        return filePathStr;

    }

    public static String getSaveFileDownloadPath(String name, String fileUrl) {

        String md5 = Md5Utils.getMD5(fileUrl);
        String sdPath = getComilePath();
        File file = new File(sdPath + File.separator + md5);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePathStr = file.getAbsolutePath() + File.separator + name;
        MyLog.debug(TAG, "getSaveFileDownloadPath " + filePathStr);
        return filePathStr;
    }

    /**
     * 获取 保存视频的地址
     *
     * @param fileNameStr
     * @return
     */
    public static String getSaveVideoFilePath(String fileNameStr) {
        String robotPath = FileUtil.getComVideoPath();
        File file = new File(robotPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePathStr = robotPath + File.separator + fileNameStr + ".mp4";
        File f = new File(filePathStr);
        return f.getAbsolutePath();
    }

    /**
     * 生成视频的缩略图并保存到本地
     *
     * @param videoPath 视频的路径
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public static String getVideoThumbnail(String videoPath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取缩略图
            retriever.setDataSource(videoPath);
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime(1);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return saveImage(bitmap, Md5Utils.getMD5(videoPath));

    }

    /**
     * 保存位图到本地
     *
     * @param bitmap
     * @param fileNameStr 不带后缀的文件名
     * @return void
     */
    public static String saveImage(Bitmap bitmap, String fileNameStr) {
        String sdPath = FileUtil.getComImgPath();
        String fileFullPath = "";
        File file = new File(sdPath);
        FileOutputStream fileOutputStream = null;
        //文件夹不存在，则创建它
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            fileFullPath = sdPath + File.separator + fileNameStr + ".png";
            File f = new File(fileFullPath);
            if (!f.exists()) {
                f.createNewFile();
            }
            fileOutputStream = new FileOutputStream(fileFullPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fileOutputStream = null;
            }
        }
        return fileFullPath;
    }

    public static String getSaveAudioPath(String toString) {
        return getComAudioPath() + "/" + toString + ".amr";
    }

    public static String getSaveVoicePath(String name) {
        return getComVoicePath() + "/" + name + ".silk";
    }

    /**
     * 获取录音文件目录
     */
    public static String getRecordFilePath(String name) {
        File recordPath = new File(FileUtil.getComRecordPath());
        if (!recordPath.exists() || !recordPath.isDirectory()) {
            recordPath.mkdirs();
        }
        String mp3FilePath = new File(recordPath, name + ".mp3").getAbsolutePath();
        return mp3FilePath;
    }

    public static String getSaveRecordPath(String name) {
        return getComRecordPath() + "/" + name + ".mp3";
    }

    public static String getAliYunCode() {
        Pair<JSONObject, String> jsonObjectStringPair = RootUtil.sendAdbCmd("cat /sys/class/dmi/id/product_uuid", true);
        if (jsonObjectStringPair == null) return "";
        MyLog.debug(TAG, jsonObjectStringPair.first + " " + jsonObjectStringPair.second);
        if (jsonObjectStringPair.first != null) {
            String re = jsonObjectStringPair.first.getString("redzsult");
            return re == null ? "" : re.trim();
        }
        return jsonObjectStringPair.second;
    }


    /**
     * 文件清理 相关
     */
    public static class FileClear {

        private static String TAG = "FileClear";

        /***
         * 查找微信相关文件路径
         * @param file
         * @param name
         * @param context
         * @param more
         * @return
         */
        public static List<File> getWeiXinFilePath(File file, String name, Context context, boolean more) {
            List<File> list = new ArrayList<File>();

            if (file.exists() && file.isDirectory()) {
                File[] fileList = file.listFiles();
                if (fileList == null) return null;
                for (int i = 0; i < fileList.length; i++) {
                    File fileDir = fileList[i];
                    if (fileDir.isDirectory()) {
                        MyLog.debug(TAG, "file_dir=" + fileDir.getName());
                        if (fileDir.getName().endsWith(name)) {
                            MyLog.debug(TAG, "file_dir=" + "find_File=" + fileDir.getAbsolutePath());
                            list.add(fileDir);
                        } else {
                            List<File> str = getWeiXinFilePath(fileDir, name, context, more);
                            list.addAll(str);
                        }
                    } else if (fileDir.isFile()) {
                        MyLog.debug(TAG, "file_name=" + fileDir.getName());

                        if (fileDir.getName().equals(name)) {
                            MyLog.debug(TAG, "file_dir=" + "find_File=" + fileDir.getAbsolutePath());
                            list.add(fileDir);
                        }
                    }
                }
            }
            return list;


        }

        /**
         * 获取微信的数据库路经
         *
         * @param file
         * @param name
         * @param context
         * @return
         */
        public static String getWeiXinXmlPath(File file, String name, Context context) {
            if (file.exists() && file.isDirectory()) {
                File[] fileList = file.listFiles();
                if (fileList == null) return null;
                for (int i = 0; i < fileList.length; i++) {
                    File fileDir = fileList[i];
                    if (fileDir.isDirectory()) {
                        String str = getWeiXinXmlPath(fileDir, name, context);
                        if (str != null) {
                            return str;
                        }
                    } else if (fileDir.isFile()) {
                        MyLog.debug(TAG, "file_name=" + fileDir.getName());
                        if (fileDir.getName().equals(name)) {
                            MyLog.debug(TAG, "file_dir=" + "find_File=" + fileDir.getAbsolutePath());
                            return fileDir.getAbsolutePath();
                        }
                    }
                }
            }
            return null;
        }

        /**
         * 通过反射调用获取内置存储和外置sd卡根路径(通用)
         *
         * @param mContext    上下文
         * @param is_removale 是否可移除，false返回内部存储路径，true返回外置SD卡路径
         * @return
         */
        public static String getStoragePath(Context mContext, boolean is_removale) {
            String path = "";
            //使用getSystemService(String)检索一个StorageManager用于访问系统存储功能。
            StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
            Class<?> storageVolumeClazz = null;
            try {
                storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
                Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
                Method getPath = storageVolumeClazz.getMethod("getPath");
                Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
                Object result = getVolumeList.invoke(mStorageManager);

                for (int i = 0; i < Array.getLength(result); i++) {
                    Object storageVolumeElement = Array.get(result, i);
                    path = (String) getPath.invoke(storageVolumeElement);
                    boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                    if (is_removale == removable) {
                        return path;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return path;
        }

        public static void deleteAllFile(File file) {
            if (file == null) return;
            if (file.isFile()) {
                file.delete();
            }
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files == null || files.length == 0) {
                    file.delete();
                } else {
                    for (File f : files) {
                        deleteAllFile(f);
                    }
                }

            }
        }

        public static long getFolderSize(File file) {
            long size = 0;
            try {
                File[] fileList = file.listFiles();
                for (int i = 0; i < fileList.length; i++) {
                    if (fileList[i].isDirectory()) {
                        size += getFolderSize(fileList[i]);
                    } else {
                        size += fileList[i].length();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return size;
            }
            return size;
        }

        public static long getStorageFileSize(Context context, boolean b) {
            String path = getStoragePath(context, b);
            File file = new File(path);
            return getFolderSize(file);
        }
    }

    public static String getFileName(String str) {
        int lastIndexOf;
        return (TextUtils.isEmpty(str) || str.length() < 2 || (lastIndexOf = str.lastIndexOf("/")) < 0) ? "" : str.substring(lastIndexOf + 1, str.length());
    }

    public static String getFileTail(String name) {
        String result = "";
        if (name.contains(".")) {
            int index = name.lastIndexOf(".");
            result = name.substring(index + 1);
        }
        return result;
    }

    public static long getFileSize(String str) {
        if (str == null) {
            return 0;
        }
        try {
            return new File(str).length();
        } catch (Throwable th) {
            th.printStackTrace();
            return 0;
        }
    }
}
