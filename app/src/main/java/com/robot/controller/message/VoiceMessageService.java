package com.robot.controller.message;

import com.robot.common.MD5;
import com.robot.common.Global;
import com.robot.hook.KeyConst;
import com.robot.util.FileUtil;
import com.robot.util.MyLog;
import com.robot.util.ProxyUtil;
import com.robot.util.StrUtils;
import com.robot.robothook.RobotHelpers;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/***
 *@author 
 *@date 2021/7/22
 *@description 语音消息处理
 ****/
public class VoiceMessageService {

    static String TAG = "VoiceMessageService";
    private int downLoadRetry = 3;

    public void autoTranslateVoiceText(ClassLoader classLoader, String fileId, String aeskey, IPickMessageCallback iPickMessageCallback) {
        Class IMsg$ = RobotHelpers.findClassIfExists(KeyConst.C_MSG_CC, classLoader);
        Object imsgimpl = RobotHelpers.callStaticMethod(IMsg$, KeyConst.M_MSG_CC_GET);
        RobotHelpers.callMethod(imsgimpl, KeyConst.M_MSG_TranslateVoiceText, fileId, aeskey, 0L, ProxyUtil.GetProxyInstance(KeyConst.I_IApplyVoiceResultCallback, new ProxyUtil.ProxyCallBack() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                if (method.getName().equals(KeyConst.M_IApplyVoiceResultCallback_onResult)) {
                    String message = null;
                    int code = (int) objects[0];
                    if (code == 0) {
                        byte[] transText = (byte[]) RobotHelpers.getObjectField(objects[1], KeyConst.F_MSG_TranslateVoice_transText);
                        message = StrUtils.byteToUTFStr(transText);
                        MyLog.debug(TAG, "[autoTranslateVoiceText]" + " 语音转换成功  " + message);
                    } else {
                        MyLog.debug(TAG, "[autoTranslateVoiceText]" + " 语音转换失败  code = " + code, true);
                        message = "转化失败";
                    }
                    if (iPickMessageCallback != null) {
                        iPickMessageCallback.onResult(code, message);
                    }
                }
                return null;
            }
        }));
    }

    public void autoTranslateVoiceText(ClassLoader classLoader, Object obj_Message, IPickMessageCallback iPickMessageCallback) {
        Global.postRunnable2UI(new Runnable() {
            @Override
            public void run() {
                Class clazzConvService = RobotHelpers.findClassIfExists(KeyConst.C_ConversationService, classLoader);
                Object convService = RobotHelpers.callStaticMethod(clazzConvService, KeyConst.M_ConversationService_getService);
                // 第二位是否优先本地转化
                RobotHelpers.callMethod(convService, KeyConst.M_MSG_TranslateVoiceText, obj_Message, true, ProxyUtil.GetProxyInstance(KeyConst.I_IPickMessageCallback, new ProxyUtil.ProxyCallBack() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                        MyLog.debug(TAG, "[onHookInfo]" + " method   " + method.getName());
                        if (method.getName().equals(KeyConst.M_IApplyVoiceResultCallback_onResult)) {
                            if (iPickMessageCallback != null) {
                                MyLog.debug(TAG, "[onHookInfo]" + " 语音转换   " + StrUtils.objectToJson(objects[1]));
                                int code = (int) objects[0];
                                String message = null;
                                if (code == 0) {
                                    Object msg = RobotHelpers.callMethod(objects[1], KeyConst.M_Message_requestInfo);
                                    Object extras = RobotHelpers.getObjectField(msg, KeyConst.F_MSG_TranslateVoice_requestInfo_extras);
                                    Object voiceTextInfo = RobotHelpers.getObjectField(extras, KeyConst.F_MSG_TranslateVoice_requestInfo_extras_voiceTextInfo);
                                    byte[] transText = (byte[]) RobotHelpers.getObjectField(voiceTextInfo, KeyConst.F_MSG_TranslateVoice_requestInfo_extras_voiceTextInfo_transText);
                                    message = StrUtils.byteToUTFStr(transText);
                                    MyLog.debug(TAG, "[onHookInfo]" + " 语音转换成功  " + message, true);
                                } else {
                                    MyLog.debug(TAG, "[onHookInfo]" + " 语音转换失败  ", true);
                                    message = "转化失败";
                                }
                                iPickMessageCallback.onResult(code, message);
                            }
                        }
                        return null;
                    }
                }));
                Global.postRunnable2UIDelay(new Runnable() {
                    @Override
                    public void run() {
                        Object msg = RobotHelpers.callMethod(obj_Message, KeyConst.M_Message_requestInfo);
                        if (msg != null) {
                            Object extras = RobotHelpers.getObjectField(msg, KeyConst.F_MSG_TranslateVoice_requestInfo_extras);
                            if (extras != null) {
                                Object voiceTextInfo = RobotHelpers.getObjectField(extras, KeyConst.F_MSG_TranslateVoice_requestInfo_extras_voiceTextInfo);
                                if (voiceTextInfo != null) {
                                    byte[] transText = (byte[]) RobotHelpers.getObjectField(voiceTextInfo, KeyConst.F_MSG_TranslateVoice_requestInfo_extras_voiceTextInfo_transText);
                                    if (transText != null) {
                                        MyLog.debug(TAG, "[onHookInfo]" + " 语音转换成功  " + StrUtils.byteToUTFStr(transText), true);
                                    }
                                }
                            }
                        }
                    }
                }, 4000);
                MyLog.debug(TAG, "[onHookInfo]" + " method TranslateVoiceText 调用结束  ");
            }
        });
    }

    /**
     * 语音文件
     *
     * @param fileId
     * @param aesKey
     * @param md5
     * @param fileSize
     * @param filePath
     * @param callBack
     */
    public void downVoiceFile(String fileId, String aesKey, String md5, long fileSize, String filePath, ProxyUtil.ProxyStringResultCallBack callBack) {
        int fileType = 4; // 4
        byte[] bytes1 = new byte[0];
        byte[] bytes2 = new byte[0];
        byte[] bytes3 = new byte[0];
        String param8 = "";
        Class clzFileUpAndDownLoadEngine = RobotHelpers.findClassIfExists(KeyConst.C_FileUpAndDownLoadEngine, Global.loadPackageParam.classLoader);
        Class clazzIDenA = RobotHelpers.findClassIfExists(KeyConst.C_FileUpAndDownLoadEngine_a, Global.loadPackageParam.classLoader);
        Class[] clazzArray = {String.class, int.class, long.class, String.class, String.class,
                byte[].class, byte[].class, byte[].class,
                String.class, String.class, clazzIDenA};

        Object denObj = RobotHelpers.callStaticMethod(clzFileUpAndDownLoadEngine, KeyConst.M_FileUpAndDownLoadEngine_getInstance);
        Object objCallBack = Proxy.newProxyInstance(Global.loadPackageParam.classLoader, new Class[]{clazzIDenA}, new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                String name = method.getName();
                if (name.equals(KeyConst.M_FileUpAndDownLoadEngine_onResult)) {
                    String str = (String) args[0];
                    int ret = (int) args[1];
                    MyLog.debug(TAG, "[onInvokeMethod]" + " 下载语音文件 onResult " + ret + " downLoadRetry: " + downLoadRetry + " filePath :" + filePath + " str =" + str + " downLoadRetry " + downLoadRetry, true);
                    if (ret != 0 && downLoadRetry > 0) {
                        downLoadRetry--;
                        downVoiceFile(fileId, aesKey, md5, fileSize, filePath, callBack);
                    } else {
                        callBack.onResult(ret, filePath);
                    }
                } else if (name.equals("onProgressChanged")) {
                    MyLog.debug(TAG, "[onInvokeMethod]" + " 下载语音文件 onProgressChanged" + args);
                }
                return null;
            }
        });
        Object[] objArray = {fileId, fileType, fileSize, filePath, aesKey,
                bytes1, bytes2, bytes3, param8, md5, objCallBack};
        MyLog.debug(TAG, "[onInvokeMethod]" + " 下载语音文件 参数 fileId " + fileId + " fileType: " + fileType + " fileSize: " + fileSize + " filePath :" + filePath + " aesKey =" + aesKey + " bytes1 =" + bytes1 + " bytes2 =" + bytes2 + " bytes3 =" + bytes3 + " param =" + param8 + " md5 =" + md5 + " downLoadRetry " + downLoadRetry);
        Global.postRunnable(() -> {
            RobotHelpers.callMethod(denObj, KeyConst.M_FILEDOWNLOAD_DOWNLOAD, clazzArray, objArray);
        });


    }

    public static String getNewVoiceFilePath(String receiver, String fileId) {
        String md5 = MD5.getMessageDigest(fileId.getBytes());
        String md52 = MD5.getMessageDigest(md5.getBytes());
        String filePath = FileUtil.getSdcardPath() + "/Tencent/WeixinWork/filecache/" + receiver + "/" + md52.substring(0, 2) + "/" + md5 + "/" + md5;
        return filePath;
    }

    public static String getVoiceFilePath(String receiver, String fileId) {
        String md5 = MD5.getMessageDigest(fileId.getBytes());
        String md52 = MD5.getMessageDigest(md5.getBytes());
        String filePath = FileUtil.getSdcardPath() + "/Android/data/com.tencent.wework/files/filecache/" + receiver + "/" + md52.substring(0, 2) + "/" + md5 + "/" + md5;
        return filePath;
    }

    public static String getVoiceSelfFilePath(String sender) {
        String fileParentPath = FileUtil.getSdcardPath() + "/Android/data/com.tencent.wework/files/voicemsg/" + sender;
        File latestFileFromDir = getLatestFileFromDir(fileParentPath);
        String filePath;
        if (latestFileFromDir != null) {
            filePath = latestFileFromDir.getAbsolutePath();
        } else {
            filePath = "";
        }
        MyLog.debug(TAG, "silk文件存在吗 " + filePath);
        return filePath;
    }

    public static File getLatestFileFromDir(String dirPath) {
        File dir = new File(dirPath);
        File latestFile = null;
        long lastModifiedTime = Long.MIN_VALUE;
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.lastModified() > lastModifiedTime) {
                        latestFile = file;
                        lastModifiedTime = file.lastModified();
                    }
                }
            }
        }
        return latestFile;
    }

    public static interface IPickMessageCallback {
        public void onResult(int i, String transText);
    }
}
