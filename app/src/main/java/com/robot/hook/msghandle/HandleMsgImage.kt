package com.robot.hook.msghandle

import android.text.TextUtils
import com.robot.common.Global
import com.robot.common.MConfiger
import com.robot.controller.LoginController
import com.robot.controller.OssController
import com.robot.controller.message.FileMessageService
import com.robot.controller.resource.ResourceController
import com.robot.entity.FileMsgEntity
import com.robot.entity.MsgEntity
import com.robot.hook.msghandle.base.BaseHandleMsg
import com.robot.nettywss.WssProtocalManager
import com.robot.robothook.LoadPackageParam
import com.robot.util.CompressHelper
import com.robot.util.FileImgParseUtil
import com.robot.util.MyLog
import com.robot.util.ProxyUtil.ProxyStringResultCallBack
import com.robot.util.StrUtils
import com.tencent.smtt.utils.Md5Utils
import java.io.File

/**
 * 处理图片消息
 */
open class HandleMsgImage : BaseHandleMsg {
    private val TAG: String = javaClass.simpleName

    override fun onHandleMsg(loadPackageParam: LoadPackageParam, msgEntity: MsgEntity) {
        MyLog.debug(
            TAG,
            "[onHandleMsg]" + "处理 图片消息 MsgEntity -> " + msgEntity.fileMsgEntity,
            true
        )
        loadImageFile(msgEntity.contentType, msgEntity.fileMsgEntity, msgEntity)
    }

    private fun getObjectName(fileName: String, userId: Long, prefix: String): String {
        val fileMd5Name = Md5Utils.getMD5(fileName)
        val objectName = String.format("wecom/robot/%d/%s/%s", userId, prefix, fileMd5Name) //文件;
        if (objectName.endsWith(".jpg") || objectName.endsWith(".png")
            || objectName.endsWith(".gif")
        ) {
            return objectName
        }
        return "$objectName.jpg"
    }

    protected open fun sendMsg(
        localPath: String?,
        contentType: Int,
        entity: FileMsgEntity,
        msgEntity: MsgEntity
    ) {
        MyLog.debug(TAG, "[sendMsg]" + "======ParamLoadImageEntity=001")
        // 图片消息类型
        val fileName = StrUtils.byteToUTFStr(entity.fileName)
        val fileSize = entity.size
        val file = File(localPath)
        var fullUrl = ResourceController.getUploadUrl(localPath)
        MyLog.debug(TAG, "[upload] fullUrl:$fullUrl" + " localPath " + localPath, true)
        if (TextUtils.isEmpty(fullUrl) && file.exists()) {
            val compressToFile = CompressHelper.getDefault(Global.getContext()).compressToFile(file)
            if (compressToFile != null && compressToFile.exists()) {
                MyLog.debug(TAG, "[upload] compressToFile fullUrl:$fullUrl", true)
                var inputFileName = fileName
                if (fileName.isEmpty()) {
                    inputFileName = compressToFile.name
                }
                val objectName =
                    getObjectName(inputFileName, LoginController.getInstance().loginUserId, "png")
                fullUrl = MConfiger.getFullImgUrl(objectName)
                msgEntity.content = fullUrl
                OssController.getInstance()
                    .uploadFile(compressToFile.absolutePath, objectName, msgEntity)
            } else {
                MyLog.debug(TAG, "[upload] normal fullUrl:$fullUrl", true)
                var inputFileName = fileName
                if (fileName.isEmpty()) {
                    inputFileName = file.name
                }
                val objectName =
                    getObjectName(inputFileName, LoginController.getInstance().loginUserId, "png")
                fullUrl = MConfiger.getFullImgUrl(objectName)
                msgEntity.content = fullUrl
                OssController.getInstance().uploadFile(localPath, objectName, msgEntity)
            }
        } else {
            msgEntity.content = fullUrl
        }
        // ProtocalManager.getInstance().sendMsgEntity(msgEntity,"sendMsgImage");
        WssProtocalManager.sendMsgEntity(msgEntity, "img")
        MyLog.debug(
            TAG,
            "[upload]" + " fullUrl:" + fullUrl + " contentType:" + msgEntity.contentType + " fileName:" + fileName + " size:" + fileSize,
            true
        )
    }

    private fun loadImageFile(
        contentType: Int,
        fileMsgEntity: FileMsgEntity,
        msgEntity: MsgEntity
    ) {
        if (fileMsgEntity.fileId.size == 0 && fileMsgEntity.url.size > 0) {
            FileMessageService().downLoadMessageFile(contentType,
                StrUtils.byteToUTFStr(
                    msgEntity.fileMsgEntity
                        .fileName
                ),
                FileImgParseUtil.StrUtilsDjz_cJ(msgEntity.fileMsgEntity.url),
                FileImgParseUtil.StrUtilsDjz_cJ(msgEntity.fileMsgEntity.aesKey),
                FileImgParseUtil.StrUtilsDjz_cJ(msgEntity.fileMsgEntity.wechatAuthKey),
                msgEntity.fileMsgEntity.size,
                StrUtils.byteToUTFStr(msgEntity.fileMsgEntity.md5),
                object : ProxyStringResultCallBack() {
                    override fun onResult(i: Int, str: String) {
                        if (i == 0) {
                            sendMsg(str, contentType, fileMsgEntity, msgEntity)
                        } else {
                            MyLog.debug(
                                TAG,
                                "文件下载失败 messageId" + msgEntity.msgId + "   path= " + str,
                                true
                            )
                            // 下载失败也发条消息上去
                            sendMsg(str, contentType, fileMsgEntity, msgEntity)
                        }
                    }
                })
        } else {
            FileMessageService().downLoadLocalFile(contentType,
                StrUtils.byteToUTFStr(
                    msgEntity.fileMsgEntity
                        .url
                ),
                StrUtils.byteToUTFStr(
                    msgEntity.fileMsgEntity
                        .fileId
                ),
                FileImgParseUtil.StrUtilsDjz_cJ(msgEntity.fileMsgEntity.aesKey),
                FileImgParseUtil.StrUtilsDjz_cJ(msgEntity.fileMsgEntity.md5),
                FileImgParseUtil.StrUtilsDjz_cJ(msgEntity.fileMsgEntity.fileName),
                msgEntity.fileMsgEntity.size,
                object : ProxyStringResultCallBack() {
                    override fun onResult(i: Int, str: String) {
                        if (i == 0) {
                            sendMsg(str, contentType, fileMsgEntity, msgEntity)
                        } else {
                            MyLog.debug(
                                TAG,
                                "本地文件下载失败 messageId" + msgEntity.msgId + "   path= " + str,
                                true
                            )
                            // 下载失败也发条消息上去
                            sendMsg(str, contentType, fileMsgEntity, msgEntity)
                        }
                    }
                })
        }
    }
}
