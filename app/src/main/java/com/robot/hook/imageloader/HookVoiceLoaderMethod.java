package com.robot.hook.imageloader;


import com.robot.common.Global;
import com.robot.controller.resource.ResourceController;
import com.robot.entity.ActionResultEnum;
import com.robot.entity.MsgEntity;
import com.robot.entity.PVoiceEntity;
import com.robot.entity.ParamLoadVoiceEntity;
import com.robot.entity.ResEntity;
import com.robot.hook.KeyConst;
import com.robot.hook.base.HookBaseMethod;
import com.robot.hook.base.IHookCallBack;
import com.robot.util.FileUtil;
import com.robot.util.MyLog;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.robot.robothook.RobotHelpers;
import com.robot.robothook.LoadPackageParam;

public class HookVoiceLoaderMethod extends HookBaseMethod<ParamLoadVoiceEntity> {

    public HookVoiceLoaderMethod() {

    }

    @Override
    public void onHookInfo(Class clazz, LoadPackageParam loadPackageParam, IHookCallBack<ParamLoadVoiceEntity> callBack) {
//        Class clzFileUpAndDownLoadEngine = RobotHelpers.findClassIfExists(KeyConst.C_FileUpAndDownLoadEngine, loadPackageParam.classLoader);
//        //sendImageMessage
//        RobotHelpers.hookAllMethods(clzFileUpAndDownLoadEngine, "a", new MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                if(param != null && param.args != null) {
//                    MyLog.debug(TAG,"[onHookInfo][afterHookedMethod]" + " param.length:" + param.args.length);
//                    if (param.args.length == 6 || param.args.length == 4) {
//                        /**
//                         * den
//                         * null
//                         * 0b816abdd60ec153962f341b396d7528
//                         * /data/user/0/com.tencent.wework/files/filecache/1688853014017889/0a/9166f7b4e8b19c0fe1c1d0e6a6966476/9166f7b4e8b19c0fe1c1d0e6a6966476
//                         * 308180020102047930770201000204a12c192502030f4df a0204 2770fb3a 02045d6 7d652 044c323730343032313739375f3 6363736 364 4363736433736373236353730373336313738373636373731 36345f3 062383136616264643630656331353339363266333431623339366437353238 020 100020210a 004000201050201000400
//                         308180020102047930770201000204a12c192502030f4df b0204 710e3379 02045d6 89f49 044c323730343032313739375f3 7413645 364 6373437373738373837343732373636453736364636453732 36345f3 361666433303733366430393433643331356466656435303863663736396434 020 10002020a8 004000201050201000400
//                         * 0
//                         */
//                        StringBuilder builder = buildStrBuilder(param);
//                        MyLog.debug(TAG,"[onHookInfo][afterHookedMethod]" + " builder:" + builder.toString());
//
//                        //for log
//                        for (int i = 0; i< param.args.length;i++){
//                            MyLog.debug(TAG,"[onHookInfo][afterHookedMethod]6" + " param.args[" + i + "]=" + param.args[i]);
//                        }
//
//                        //download success, then upload voice file;
//                        String voicePath = (String)param.args[3];
//                        String md5Str = (String)param.args[2];
//                        File file = new File(voicePath);
//                        if (!file.exists()) {
//                            MyLog.debug(TAG,"[onHookInfo][afterHookedMethod]" + " file:" + voicePath + " not found!");
//                        }else {
//                            MsgEntity msgEntity = Global.getMsgEntityFromMap(md5Str);
//                            if (msgEntity !=null ) {
//                                Global.deleteMsgEntityFromMap(md5Str);
//                                long userId = LoginController.getInstance().getLoginUserId();
//                                String objectName = MediaTypeEnum.VOICE.getObjectName(userId,file);
//                                String fullImgUrl = MConfiger.getFullImgUrl(objectName);
//                                msgEntity.content = fullImgUrl;
//                                ProtocalManager.getInstance().sendMsgEntity(msgEntity);
//                                //上传文件到oss
//                                OssController.getInstance().uploadFile(voicePath,objectName,msgEntity);
//                                MyLog.debug(TAG,"[afterHookedMethod]" + " 音频文件:" + voicePath + " url:" + fullImgUrl + " type:" + msgEntity.contentType);
//                            }
//                        }
//                    }
//                }
//            }
//        });
    }

    @Override
    public boolean onInvokeMethod(Class classDb, LoadPackageParam loadPackageParam, final IHookCallBack<ParamLoadVoiceEntity> paramCall) {
        boolean succ = false;
        if (paramCall != null) {
            ParamLoadVoiceEntity pEntity = paramCall.getParams();
            long cvId = pEntity.converId;//
            String fileId = pEntity.fileId; //101
            int fileType = 4; // 4
            long fileSize = pEntity.fileSize;
            String aeskey = pEntity.aeskey;// 247567l;
            byte[] bytes1 = new byte[0];
            byte[] bytes2 = new byte[0];
            byte[] bytes3 = new byte[0];
            String param8 = "";
            String md5 = pEntity.md5;
            final String filePath =  pEntity.filePath  ;

            Class clzFileUpAndDownLoadEngine = RobotHelpers.findClassIfExists(KeyConst.C_FileUpAndDownLoadEngine, loadPackageParam.classLoader);
            Class clazzIDenA = RobotHelpers.findClassIfExists(KeyConst.C_FileUpAndDownLoadEngine_a, loadPackageParam.classLoader);

            Class[] clazzArray = {String.class, int.class, long.class, String.class, String.class,
                    byte[].class, byte[].class, byte[].class,
                    String.class, String.class, clazzIDenA};

            Object objCallBack = Proxy.newProxyInstance(loadPackageParam.classLoader, new Class[]{clazzIDenA}, new InvocationHandler() {
                @Override
                public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                    String name = method.getName();
                    if (name.equals("E")) {
                        String str = (String) args[0];
                        int ret = (int) args[1];
                        if (ret == 0) {
                            MyLog.debug(TAG, "[onInvokeMethod]" + " 下载语音文件成功...path:" + filePath, true);
                            if (paramCall != null) {
                                ParamLoadVoiceEntity pEntity = new ParamLoadVoiceEntity();
                                pEntity.rFilePath = filePath;
                                ResourceController.putLocalPathByMD5( pEntity.md5,pEntity.filePath);
                                paramCall.onCall(ResEntity.genSucc(pEntity));
                            }
                        } else {
                            if (paramCall != null) {
                                paramCall.onCall(ResEntity.genErr(null, ActionResultEnum.ACTION_VOICE_DOWNLOAD_FAIL.getMsg()));
                            }
                            MyLog.debug(TAG, "[onInvokeMethod]" + " 下载语音文件失败,path:" + filePath, true);
                        }
                    }
                    return null;
                }
            });

            Object[] objArray = {fileId, fileType, fileSize, filePath, aeskey,
                    bytes1, bytes2, bytes3, param8, md5, objCallBack};

            MyLog.debug(TAG, "[afterHookedMethod]" + " fileId:" + fileId + " fileType:" + fileType + " fileSize:" + fileSize + " filePath:" + filePath + " aeskey:" + aeskey + " md5:" + md5);

            try {
                MyLog.debug(TAG, "[afterHookedMethod]" + " start call...");
                Object denObj = RobotHelpers.callStaticMethod(clzFileUpAndDownLoadEngine, KeyConst.M_FileUpAndDownLoadEngine_getInstance);
                Global.postRunnable2UIDelay(() -> {
                    RobotHelpers.callMethod(denObj, KeyConst.M_FILEDOWNLOAD_DOWNLOAD, clazzArray, objArray);
                    MyLog.debug(TAG, "[afterHookedMethod]" + " final...");
                }, 1000);
                succ = true;
                MyLog.debug(TAG, "[afterHookedMethod]" + " succ:" + succ);
                return succ;
            } catch (Exception e) {
                e.printStackTrace();
                MyLog.error(TAG, e);
            }
            MyLog.debug(TAG, "[afterHookedMethod]" + " final succ :" + succ);

            ResEntity<ParamLoadVoiceEntity> resEntity = ResEntity.genErr(pEntity, "下载语音文件失败！");
            paramCall.onCall(resEntity);
        }

        return succ;

    }

    /**
     * //public void a(String str, int i, long j, String str2, String str3, byte[] bArr, byte[] bArr2, byte[] bArr3, String str4, String str5, a aVar) {
     * /*
     * fileId,fileType,fileSize,filePath,aeskey,byte[],byte[],byte[],"",md5,den$a
     * param.args[0]=308180020102047930770201000204a12c192502030f4dfb0204710e337902045d68d9ef044c323730343032313739375f36373644373236313641373936353735363436363737374136423643373436435f31323934643965336465633037353861366433656130343038346139386562340201000202126004000201050201000400
     * <p>
     * param.args[1]=4
     * param.args[2]=4702
     * param.args[3]=/data/user/0/com.tencent.wework/files/filecache/1688853014017889/b3/c4c444c8d75c9fe8344ef5c798df9c42/c4c444c8d75c9fe8344ef5c798df9c42
     * param.args[4]=676D72616A7965756466777A6B6C746C
     * param.args[5]=[B@c04c29a
     * param.args[6]=[B@c04c29a
     * param.args[7]=[B@c04c29a
     * param.args[8]=
     * param.args[9]=1294d9e3dec0758a6d3ea04084a98eb4
     * param.args[10]=null
     */

    private void sendVoice(String md5, String filePath, MsgEntity msgEntity) {
        MyLog.debug(TAG, "[HookVoiceLoaderMethod][sendVoice]" + " filePath:" + filePath);
        MyLog.debug(TAG, "[HookVoiceLoaderMethod][sendVoice]" + " msgEntity:" + msgEntity);
        MyLog.debug(TAG, "[HookVoiceLoaderMethod][sendVoice]" + " msgEntity.fileMsgEntity:" + msgEntity.fileMsgEntity);
        Global.postRunnable(() -> {
            PVoiceEntity voiceEntity = new PVoiceEntity();
            try {
                voiceEntity.contentType = msgEntity.contentType;
                voiceEntity.id = msgEntity.id;
                voiceEntity.conversationId = msgEntity.conversationId;
                voiceEntity.sender = msgEntity.sender;
                voiceEntity.senderName = msgEntity.sendername;
                voiceEntity.receiver = msgEntity.receiver;
                voiceEntity.remoteId = msgEntity.remoteId;
                voiceEntity.sendTime = msgEntity.senderTime;
                voiceEntity.convType = msgEntity.convType;
                voiceEntity.contentType = msgEntity.contentType;
                //msgEntity.fileMsgEntity.fileName;

                //String filePathStr = msgEntity.fileMsgEntity.fileName;
                if (filePath == null) {
                    voiceEntity.voiceArray = new byte[0];
                } else {
                    byte[] array = FileUtil.toByteArray(filePath);
                    voiceEntity.voiceArray = array;
                }
                MyLog.debug(TAG, "[sendVoice]" + " file.len->" + voiceEntity.voiceArray.length);
                //FileUtil.deleteFile(filePath);
                //voice处理

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


}
