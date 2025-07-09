package com.robot.hook.msghandle;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.robot.common.MD5;
import com.robot.common.MConfiger;
import com.robot.controller.LoginController;
import com.robot.controller.OssController;
import com.robot.controller.resource.ResourceController;
import com.robot.entity.LinkMsgEntity;
import com.robot.entity.MsgEntity;
import com.robot.hook.msghandle.base.BaseHandleMsg;
import com.robot.netty.proto.req.ReqLinkItemEntity;
import com.robot.nettywss.WssProtocalManager;
import com.robot.util.FileUtil;
import com.robot.util.MyLog;
import com.robot.util.StrUtils;

import com.robot.robothook.LoadPackageParam;

import java.io.File;

public class HandleMsgLink implements BaseHandleMsg {

    private final String TAG = getClass().getSimpleName();

    @Override
    public void onHandleMsg(LoadPackageParam loadPackageParam, MsgEntity msgEntity) {
        MyLog.debug(TAG, "[onHandleMsg]" + " LinkMsgEntity -> " + msgEntity.linkMsgEntity, true);
        loadLinkCard(msgEntity);
    }

    /**
     * "linkMsgEntity": {
     * "cmd": 0,
     * "description": "15年，5475天，小彤和你在一起~",
     * "imageData": "",
     * "imageUrl": "http://mmbiz.qpic.cn/mmbiz_jpg/V1tKiaFkMHRic76Cicia4fCgU4micanyVjRHuV9XkFEOa3fiahiagicu45VIvQ9PGiaUHxook6rcAe5DeurtWPia01wdSsZw/300?wx_fmt=jpeg&wxfrom=1",
     * "linkUrl": "http://mp.weixin.qq.com/s?__biz=MjM5MDQ2MTY5Mg==&mid=2652074334&idx=1&sn=6340ba1397fab07d3d692d5045107a10&chksm=bda3aacd8ad423db42e2e0041147a6d994d3f7f44b41c55ec1296fabfdab309c9c55c8f14979&scene=132#wechat_redirect",
     * "openImageUri": {
     * "size": 0
     * },
     * "title": "【十五周年庆】免费吃火锅！百万好礼送！更多惊喜标题放不下！！"
     * },
     * <p>
     * {
     * "cmd": 0,
     * "description": "节后福利！",
     * "imageData": "",
     * "imageUrl": "",
     * "linkUrl": "http://mp.weixin.qq.com/s?__biz=MzA5OTM5NTEwOA==&mid=2817688832&idx=1&sn=33b3423b31f4ac60d4f3e29540948a69&chksm=b284d64b85f35f5dab8f837953958215ea9abbb049d491478baa86f3144d166ecc401869629e&mpshare=1&scene=1&srcid=0924Q0N7DsHo4XpSWamYfgyO&sharer_sharetime=1632467718214&sharer_shareid=9f20761044d1c33ba90b925251038b34#rd",
     * "openImageUri": {
     * "aeskey": "3edf4d296b0a4c09297651056f7cbccf",
     * "authkey": "v1_19ac337fe84b739a655a56375622ce81446ff0c20512b435daf8027f726bfac5ac290fa74a06e584fdf31b86a21479af7c32a3ce4513ce09ac5a8a517aa794c7bbad01cc5a7fd43ca548901800f848f0",
     * "md5": "3059196b98b3f5b2d5cf0b878efa33a8",
     * "size": 15845,
     * "url": "https://imunion.weixin.qq.com/cgi-bin/mmae-bin/tpdownloadmedia?param=v1_19ac337fe84b739a655a56375622ce81446ff0c20512b435daf8027f726bfac5ac290fa74a06e584fdf31b86a21479af607ba6cdaa7f2cfbb3cdddc92052f55274330bebb1cb89a740470521663694add48f432d3f7d904c90bd620e61d7553702227ed4f89f257df4d4b58c7ac4e6ad9cf48c5e5c85dbfd33ab859ab5e3f70188a8b517d824d50ede7ca07f06e9c7b73e1935ab9db658ea709d81511dd9d2662375986de8830604634ece77449ce190ba536fd915056e0072cb0d1bf93359cdba4d0df8905ce73e67973b50aafc97b241c35400540c2a380c4143ea897f1f3f65155b4d54e3c2046f04f8398156e99aadd8a25ec65cb685fde024346290bb7c4aaac01bddbfff42b698d58e07bfdb96bf2ffa42bbf8f8870f16391df43b07dcff7a5a072c1bedca5acfd5b15a730f034343482b73f86f9bd5c48e99277a64f445524001d027efae9dd027c268a6cd4923d8bb9e09dad81d044c76f02076e283f2894722fc9f0748670ead9cdb56600a"
     * },
     * "title": "迎国庆！流量特惠还送5元话费~"
     * }
     *
     * @param msgEntity
     */
    private void loadLinkCard(MsgEntity msgEntity) {
        LinkMsgEntity linkMsgEntity = msgEntity.linkMsgEntity;
        if (FileUtil.isLocalPath(linkMsgEntity.imageUrl)) {
            MyLog.debug(TAG, "FileUtil" + msgEntity);
            uploadLink(linkMsgEntity.imageUrl, msgEntity);
        } else if (linkMsgEntity.openImageUri != null && linkMsgEntity.openImageUri.url != null && linkMsgEntity.openImageUri.url.length > 0) {
            MyLog.debug(TAG, "linkMsgEntity" + msgEntity);
            linkMsgEntity.imageUrl = StrUtils.byteToUTFStr(linkMsgEntity.openImageUri.url);
            sendLink(linkMsgEntity.imageUrl, msgEntity);
//            new FileMessageService().downLoadMessageFile(101, "", StrUtils.byteToUTFStr(linkMsgEntity.openImageUri.url),
//                    StrUtils.byteToUTFStr(linkMsgEntity.openImageUri.aeskey), StrUtils.byteToUTFStr(linkMsgEntity.openImageUri.authkey), linkMsgEntity.openImageUri.size, StrUtils.byteToUTFStr(linkMsgEntity.openImageUri.md5), new ProxyUtil.ProxyStringResultCallBack() {
//                        @Override
//                        public void onResult(int i, String str) {
//                            if (i == 0) {
//                                uploadLink(str, msgEntity);
//                            } else {
//                                MyLog.debug(TAG, "小程序 文件下载失败 messageId" + msgEntity.msgId + "   path= " + str, true);
//                                // 下载失败也发条消息上去
//                                sendLink(str, msgEntity);
//                            }
//                        }
//                    });

        } else {
            MyLog.debug(TAG, "sendLink" + msgEntity);
            sendLink(linkMsgEntity.imageUrl, msgEntity);
        }
    }

    private String getObjectName(String fileName) {
        Long userId = LoginController.getInstance().getLoginUserId();
        String prefix = "png";
        String objectName = String.format("wecom/robot/%d/%s/%s", userId, prefix, fileName);// 文件;
        if (objectName.endsWith(".jpg") || objectName.endsWith(".png")) {
            return objectName;
        }
        return objectName + ".jpg";
    }

    /**
     * 上传连接信息
     *
     * @param localPath
     * @param msgEntity
     */
    private void uploadLink(String localPath, MsgEntity msgEntity) {
        String fullUrl = ResourceController.getUploadUrl(localPath);
        if (TextUtils.isEmpty(fullUrl) && new File(fullUrl).exists()) {
            String fileName = MD5.getMessageDigest(localPath.getBytes()) + ".jpg";
            String objectName = getObjectName(fileName);
            fullUrl = MConfiger.getFullImgUrl(objectName);
            String content = JSON.toJSONString(LinkMsgEntity.parseLinkContent(msgEntity.linkMsgEntity, fullUrl));
            msgEntity.content = content;
            OssController.getInstance().uploadFile(localPath, objectName, msgEntity);
            // ProtocalManager.getInstance().sendMsgEntity(msgEntity, "sendLinkMsg");
            WssProtocalManager.sendMsgEntity(msgEntity, "link");
            MyLog.debug(TAG, "[onInvokeMethod]" + " img 上传 succ :" + " fullUrl:" + fullUrl, true);
        } else {
            MyLog.debug(TAG, "[onInvokeMethod]" + " sendLink" + fullUrl, true);
            sendLink(fullUrl, msgEntity);
        }


    }

    private void sendLink(String url, MsgEntity msgEntity) {
        // 数据组合上传到服务器
        ReqLinkItemEntity reqLinkEntity = LinkMsgEntity.parseLinkContent(msgEntity.linkMsgEntity, url);
        String content = JSON.toJSONString(reqLinkEntity);
        msgEntity.content = content;
        // ProtocalManager.getInstance().sendMsgEntity(msgEntity, "sendLinkMsg");
        WssProtocalManager.sendMsgEntity(msgEntity, "link");
    }
}
