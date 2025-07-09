package com.robot.common;

import com.robot.com.BuildConfig;
import com.robot.entity.EnvirEnum;
import com.robot.entity.PhoneLocEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class MConfiger {
    //环境相关
    public static EnvirEnum env = null;
    public static PhoneLocEnum phoneLocEnum = PhoneLocEnum.BAIDU;

    //协议版本
    public static int VER_PROTOCAL = 2;
    //上传metadata 包大小上限
    public static int METADATA_DATA_MAX_SIZE = 300 * 1024;

    //上传metadata 包大小 报错丢弃上限
    public static int METADATA_DATA_ERROR_SIZE = 800 * 1024;
    //打开文件log
    public static boolean isLogFileDebug = true;

    public static final String SELF_PKGNAME = "com.robot.com";
    //企业微信包名
    public static final String WX_ENTERPISE_PKGNAME = "com.tencent.wework";
    //企业微信push进程
    public static final String WEWORK_PUSH = "com.tencent.wework:push";
    //企业微信升级版包名
    public static final String WX_ENTERPIRSE_PKGNAME_WO = "com.tencent.wowork";
    //系统UI
    public static final String ANDROID_SYSTEM_UI = "com.android.systemui";
    //
    public static final String SOURCEDIR = "/data/app/com.robot.com-2/base.apk";

    //xposed checker
    public static final String CHECKER_PKGNAME = "ml.w568w.checkxposed";

    public static List<String> mProtectPkgList = new ArrayList<>();
    //
    public static final long SEARCH_INTERVAL = 1000 * 10;

    //
    public static final long HEART_BEAT_STOP_INTERVAL = 1000 * 60 * 1;
    //
    public static final long HEART_BEAT_STOP_INTERVAL_2Minutes = 1000 * 60 * 2;
    public static final long HEART_BEAT_STOP_INTERVAL_1Minutes = 1000 * 60 * 1;

    //AtMsg Default
    public static final String AT_CONTENT_MSG_DEF = ".";

    public static boolean startMainPageLock = false;

    //群成员个数
    public static final int GROUP_MEMBER_COUNT = 500;
    //检查SD卡间隔
    public static final int CHECK_SDCARD_INTERVAL = 60;
    //机器人是否成功
    public static int mRobotStatus = 0;//0 初始化中 -1 失败 1成功
    public static String mRobotTips = "";

    static {
        if (BuildConfig.build_env == 1) {
            MConfiger.env = EnvirEnum.TEST;
        } else {
            MConfiger.env = EnvirEnum.PRODUCT;
        }
        mProtectPkgList.add(WX_ENTERPISE_PKGNAME);
    }

    public static final String getHttpUrl(String url) {
        return MConfiger.env.getHttpUrl() + url;
    }

    // To run the sample correctly, the following variables must have valid values.
    // The endpoint value below is just the example. Please use proper value according to your region
    public static final String APP_KEY = "WeComRobot";
    public static final String APP_PRODUCT_ID = "4900";
    public static final String APP_PRODUCT_NAME = "WeComRobot";
    public static final String BUCKET_NAME = "aie-content";
    private static final AtomicLong automicSeqNo = new AtomicLong(60000);

    public static final long getSeqNo() {
        return automicSeqNo.incrementAndGet();
    }


    /**
     * 对应当前环境配置
     *
     * @return
     */
    public static final String getFullImgUrl(String url) {
        return MConfiger.env.getImgHost() + url;
    }

    public static final String WEWORK_VERSION_17857 = "3.1.16.17857";
    public static final String WEWORK_VERSION_19717 = "4.0.10.19717";
    public static final String WEWORK_VERSION_31145 = "4.1.13.31145";

    public static final String[] ADAPTER_WEWORK_VERSIONS = new String[]{WEWORK_VERSION_17857, WEWORK_VERSION_19717, WEWORK_VERSION_31145};

}
