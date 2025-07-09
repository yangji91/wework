package com.robot.util;

import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.robot.common.Global;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * 视频工具
 */
public class SightVideoUtil {


    /**
     * 获取视频相关的 信息 包括videoDuration 时间 videoWidth 宽 videoHeight  高
     * @param classLoader
     * @param localPath
     */
    public static  JSONObject getSimpleMp4InfoVFS(ClassLoader classLoader,String localPath){
        JSONObject jSONObject = new JSONObject();
            android.media.MediaMetadataRetriever media = new android.media.MediaMetadataRetriever();
            try {
                if (localPath != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri newUri = FileProvider.getUriForFile(Global.getContext(), Global.getContext().getPackageName() + ".fileprovider", new File(localPath));
                        media.setDataSource(Global.getContext(), newUri);// videoPath 本地视频的路径
                    } else {
                        media.setDataSource(localPath, new HashMap<String, String>());// videoPath 本地视频的路径
                    }
                }
                String duration = media.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);//时长(毫秒)
                String width = media.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);//宽
                String height = media.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);//高
                jSONObject.put("videoDuration",Integer.valueOf(duration)/1000);
                jSONObject.put("videoWidth",Integer.valueOf(width));
                jSONObject.put("videoHeight",Integer.valueOf(height));
                MyLog.debug("SightVideoUtil"," video ="+jSONObject);
            } catch (Exception ex)
            {
                Log.e("TAG", "MediaMetadataRetriever exception " + ex);
            } finally {
                media.release();
            }
        return jSONObject;

    }
}
