package com.robot.controller;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectRequest;
import com.obs.services.model.PutObjectResult;
import com.robot.com.BuildConfig;
import com.robot.com.database.service.WeWorkFileInfoService;
import com.robot.common.Global;
import com.robot.common.MConfiger;
import com.robot.controller.resource.ResourceController;
import com.robot.entity.MsgEntity;
import com.robot.http.entity.rsp.PTokenResultEntity;
import com.robot.netty.ProtocalManager;
import com.robot.sdk.android.oss.ClientConfiguration;
import com.robot.sdk.android.oss.ClientException;
import com.robot.sdk.android.oss.OSSClient;
import com.robot.sdk.android.oss.ServiceException;
import com.robot.sdk.android.oss.callback.OSSCompletedCallback;
import com.robot.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.robot.sdk.android.oss.common.auth.OSSCustomSignerCredentialProvider;
import com.robot.sdk.android.oss.common.utils.BinaryUtil;
import com.robot.sdk.android.oss.common.utils.OSSUtils;
import com.robot.sdk.android.oss.model.ObjectMetadata;
import com.robot.util.MyLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class OssController {
    private final String TAG = getClass().getSimpleName();
    private int retry = 3;// 可能有线程冲突
    private static OssController instance;
    private static final int MAX_CONCURRENT_REQUEST = 10;

    private OssController() {

    }

    public static final OssController getInstance() {
        if (instance == null) {
            instance = new OssController();
        }
        return instance;
    }

    public synchronized void uploadFile(String localPath, String objectName, MsgEntity msgEntity) {
        MyLog.debug(TAG, "[uploadFile]" + "localPath=" + localPath + " objectName=" + objectName, true);

        String url = ResourceController.getUploadUrl(localPath);
        if (!TextUtils.isEmpty(url) && url.equals(msgEntity.content)) {
            MyLog.debug(TAG, "[onHandle]" + "localPath=" + localPath + " 已经上传过 地址 =" + url, true);
            WeWorkFileInfoService.getInstance().updatePath(msgEntity.msgId, localPath, url);
            return;
        }
        if (!TextUtils.isEmpty(localPath) &&
                (localPath.startsWith("/sdcard") || localPath.startsWith("/storage") || localPath.startsWith("/data"))) {
            // 临时写死
            PTokenResultEntity ossToken = new PTokenResultEntity();
            ossToken.accesskey_id = "HPUAYX38LRW6RFBGD022";
            ossToken.accesskey_secret = "NBO4bk0m7Pm704v23x3VW0uRql91olM2GylIJ7OL";
            ossToken.security_token = ""; // cancel
            ossToken.endpoint = "obs.cn-east-3.myhuaweicloud.com";
            // ossToken.bucket_name = MConfiger.BUCKET_NAME;
            ossToken.bucket_name = "92b6f898fdc0b3534dea4081f1f61702-xi";
            try {
                upload(localPath, objectName, ossToken, msgEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void upload(String localPath, String objectName, PTokenResultEntity pTokenEntity, final MsgEntity msgEntity) throws IOException {
        String accessKeyId = pTokenEntity.accesskey_id;
        String accessKeySecret = pTokenEntity.accesskey_secret;
        String bucketName = pTokenEntity.bucket_name;
        String endpoint = pTokenEntity.endpoint;
        String securityToken = pTokenEntity.security_token;
        if (BuildConfig.customConfigLog) {
            MyLog.debug(TAG, "[upload]" + " securityToken:" + securityToken + "");
        }

        /*
        // OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, securityToken);
        OSSCredentialProvider credentialProvider = new OSSCustomSignerCredentialProvider() {
            @Override
            public String signContent(String content) {
                return OSSUtils.sign(accessKeyId, accessKeySecret, content);
            }
        };
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000);
        conf.setSocketTimeout(20 * 1000);
        conf.setMaxConcurrentRequest(MAX_CONCURRENT_REQUEST);
        conf.setMaxErrorRetry(2);
        // 修改完善一下
        OSSClient mOssClient = new OSSClient(Global.getContext(), endpoint, credentialProvider, conf);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentMD5(BinaryUtil.calculateBase64Md5(localPath));
        metadata.setContentType("application/octet-stream");
        PutObjectRequest putRequest = new PutObjectRequest(bucketName, objectName, localPath);
        putRequest.setMetadata(metadata);
        MyLog.debug(TAG, "[upload]" + " 开始上传:" + putRequest);
        mOssClient.asyncPutObject(putRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                MyLog.debug(TAG, "[onSuccess]" + JSON.toJSON(result));
                String fullImgUrl = MConfiger.getFullImgUrl(objectName);
                ResourceController.putUploadUrl(localPath, fullImgUrl);
                WeWorkFileInfoService.getInstance().updatePath(msgEntity.msgId, localPath, fullImgUrl);
//                ProtocalManager.getInstance().sendOssMsgNotify(msgEntity, true, null);
            }

/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
            /**
             * @param request
             * @param clientException
             * @param serviceException
             */
/* <<<<<<<<<<  5922ce98-969c-4bcd-b97d-5ce9bba86d13  >>>>>>>>>>> */
//            @Override
//            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
//                String failMsg = null;
//                if (serviceException != null) {
//                    failMsg = serviceException.getMessage();
//                } else {
//                    failMsg = "上传失败";
//                }
//                MyLog.debug(TAG, "[onFailure]" + serviceException.getMessage(), true);
////                ProtocalManager.getInstance().sendOssMsgNotify(msgEntity, false, failMsg);
//            }
//        });

        // 使用华为云
        ObsClient obsClient = new ObsClient(pTokenEntity.accesskey_id, pTokenEntity.accesskey_secret, pTokenEntity.endpoint);

        ObsThreadPool.getExecutor().execute(() -> {
            try {
                // 待上传的本地文件路径，需要指定到具体的文件名
                FileInputStream fis2 = new FileInputStream(new File(localPath));
                PutObjectRequest request = new PutObjectRequest();
                request.setBucketName(pTokenEntity.bucket_name);
                request.setObjectKey(objectName);
                request.setInput(fis2);

                PutObjectResult result = obsClient.putObject(request);
                if (result.getRequestId() != null) {
                    MyLog.debug(TAG, "[onSuccess]" + JSON.toJSON(result));
                    String fullImgUrl = MConfiger.getFullImgUrl(objectName);
                    ResourceController.putUploadUrl(localPath, fullImgUrl);
                    WeWorkFileInfoService.getInstance().updatePath(msgEntity.msgId, localPath, fullImgUrl);
//                ProtocalManager.getInstance().sendOssMsgNotify(msgEntity, true, null);
                } else {
                    String failMsg = null;
                    failMsg = result.toString();
                    MyLog.debug(TAG, "[onFailure]: 上传失败" + failMsg, true);
//                ProtocalManager.getInstance().sendOssMsgNotify(msgEntity, false, failMsg);
                }

            } catch (Exception e) {
                String failMsg = null;
                if (e != null) {
                    failMsg = e.getMessage();
                } else {
                    failMsg = "上传失败";
                }
                MyLog.debug(TAG, "[onFailure]" + failMsg, true);
//                ProtocalManager.getInstance().sendOssMsgNotify(msgEntity, false, failMsg);
            }
        });
    }
}
