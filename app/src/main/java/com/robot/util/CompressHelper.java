package com.robot.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * from:
 * https://github.com/zetbaitsu/Compressor/tree/master/compressor/src/main/java/id/zelory/compressor
 */

public class CompressHelper {
    private static final String TAG = "CompressHelper";

    private static volatile CompressHelper INSTANCE;

    private Context context;
    /**
     * 最大宽度，默认为720
     */
    private float maxWidth = 1920;
    /**
     * 最大高度,默认为960
     */
    private float maxHeight = 1920;
    /**
     * 默认压缩后的方式为JPEG
     */
    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;

    /**
     * 默认的图片处理方式是ARGB_8888
     */
    private Bitmap.Config bitmapConfig = Bitmap.Config.RGB_565; //Bitmap.Config.ARGB_8888
    /**
     * 默认压缩质量为80
     */
    private int quality = 60;
    /**
     * 存储路径
     */
    private String destinationDirectoryPath;
    /**
     * 文件名前缀
     */
    private String fileNamePrefix;
    /**
     * 文件名
     */
    private String fileName;

    public static CompressHelper getDefault(Context context) {
        if (INSTANCE == null) {
            synchronized (CompressHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CompressHelper(context);
                }
            }
        }
        return INSTANCE;
    }


    private CompressHelper(Context context) {
        this.context = context;

        String compressImgPath = FileUtil.getCompressComImgPath();
        //FileUtil.deleteFile(filePath);
        FileUtil.CreateDir(compressImgPath);

        destinationDirectoryPath = compressImgPath;
        Log.i(TAG,"[CompressHelper]destinationDirectoryPath=" + destinationDirectoryPath);

        //destinationDirectoryPath = context.getCacheDir().getPath() + File.pathSeparator + FileUtil.FILES_PATH;
    }

    /**
     * 压缩成文件
     * @param imageFile  原始文件
     * @return      压缩后的文件
     */
    public File compressToFile(File imageFile) {

        //for log
        if (imageFile!=null && imageFile.exists()) {
            Log.i(TAG, "[compressToFile]imageFile==" + imageFile);
        }

        try {
            return ImageUtil.compressImage(imageFile, maxWidth, maxHeight,
                    compressFormat, quality, destinationDirectoryPath);

        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;


    }

    /**
     * 压缩为Bitmap
     * @param imageFile  原始文件
     * @return      压缩后的Bitmap
     */
    public Bitmap compressToBitmap(File imageFile) {
        try {
            return ImageUtil.decodeSampledBitmapFromFile(imageFile, maxWidth, maxHeight);

        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
        //return ImageUtil.getScaledBitmap(context, Uri.fromFile(file), maxWidth, maxHeight, bitmapConfig);
    }


    /**
     * 采用建造者模式，设置Builder
     */
    public static class Builder {
        private CompressHelper mCompressHelper;

        public Builder(Context context) {
            mCompressHelper = new CompressHelper(context);
        }

        /**
         * 设置图片最大宽度
         * @param maxWidth  最大宽度
         */
        public Builder setMaxWidth(float maxWidth) {
            mCompressHelper.maxWidth = maxWidth;
            return this;
        }

        /**
         * 设置图片最大高度
         * @param maxHeight 最大高度
         */
        public Builder setMaxHeight(float maxHeight) {
            mCompressHelper.maxHeight = maxHeight;
            return this;
        }

        /**
         * 设置压缩的后缀格式
         */
        public Builder setCompressFormat(Bitmap.CompressFormat compressFormat) {
            mCompressHelper.compressFormat = compressFormat;
            return this;
        }

        /**
         * 设置Bitmap的参数
         */
        public Builder setBitmapConfig(Bitmap.Config bitmapConfig) {
            mCompressHelper.bitmapConfig = bitmapConfig;
            return this;
        }

        /**
         * 设置压缩质量，建议80
         * @param quality   压缩质量，[0,100]
         */
        public Builder setQuality(int quality) {
            mCompressHelper.quality = quality;
            return this;
        }

        /**
         * 设置目的存储路径
         * @param destinationDirectoryPath  目的路径
         */
        public Builder setDestinationDirectoryPath(String destinationDirectoryPath) {
            mCompressHelper.destinationDirectoryPath = destinationDirectoryPath;
            return this;
        }

        /**
         * 设置文件前缀
         * @param prefix    前缀
         */
        public Builder setFileNamePrefix(String prefix) {
            mCompressHelper.fileNamePrefix = prefix;
            return this;
        }

        /**
         * 设置文件名称
         * @param fileName  文件名
         */
        public Builder setFileName(String fileName) {
            mCompressHelper.fileName = fileName;
            return this;
        }

        public CompressHelper build() {
            return mCompressHelper;
        }
    }
}