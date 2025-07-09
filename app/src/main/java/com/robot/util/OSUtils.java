package com.robot.util;

import android.os.Build;
import android.webkit.WebSettings;
import com.robot.common.Global;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Locale;

public class OSUtils {
    private static final String TAG = OSUtils.class.getSimpleName();

    public static final class OSInfoEntity implements Serializable {
        public String androidVer;
        public String sdkVer;
        public String patchVer;
        public String androidId;
        public boolean isRoot;
        public String basebandVer;
        public String buildId;
        public String buildCode;
        public String BuildFingerprint;
        public String incremental;
        public String JavaVM;
        public String imei;
        public String javaRuntime;
        public String webView;
        public String language;

        @Override
        public String toString() {
            return "OSInfoEntity{" +
                    "androidVer='" + androidVer + '\'' +
                    ", sdkVer='" + sdkVer + '\'' +
                    ", patchVer='" + patchVer + '\'' +
                    ", androidId='" + androidId + '\'' +
                    ", isRoot=" + isRoot +
                    ", basebandVer='" + basebandVer + '\'' +
                    ", buildId='" + buildId + '\'' +
                    ", buildCode='" + buildCode + '\'' +
                    ", BuildFingerprint='" + BuildFingerprint + '\'' +
                    ", incremental='" + incremental + '\'' +
                    ", JavaVM='" + JavaVM + '\'' +
                    ", imei='" + imei + '\'' +
                    ", javaRuntime='" + javaRuntime + '\'' +
                    ", webView='" + webView + '\'' +
                    ", language='" + language + '\'' +
                    '}';
        }
    }

    public static final OSInfoEntity getOSInfoEntity() {
        OSInfoEntity rEntity = new OSInfoEntity();
        rEntity.androidVer = DeviceUtil.getAndroidVer();
        rEntity.sdkVer = DeviceUtil.getAndroidSDKVersion() + "";
        rEntity.patchVer = "";
        rEntity.androidId = DeviceUtil.getAndroidID();
        File file = new File("./su");
        if (file.exists()) {
            rEntity.isRoot = true;
        }
        rEntity.basebandVer = getBaseband_Ver();
        rEntity.buildId = Build.ID;
        rEntity.buildCode = Build.MANUFACTURER;
        rEntity.BuildFingerprint = Build.FINGERPRINT;
        rEntity.incremental= getLinuxCore_Ver();
        rEntity.javaRuntime = "";
        rEntity.webView = getUserAgent();
        rEntity.language = Locale.getDefault().getLanguage();
        return rEntity;
    }

    private static String getUserAgent(){
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(Global.getContext());
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return userAgent;
    }

    public static String getLinuxCore_Ver() {
        String linuxCoreVer;
        linuxCoreVer = DeviceUtil.getStringInfo("cat /proc/version");
        return linuxCoreVer;
    }



        public static String getBaseband_Ver(){
        String Version = "";
        try {
            Class cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();
            Method m = cl.getMethod("get", new Class[] { String.class,String.class });
            Object result = m.invoke(invoker, new Object[]{"gsm.version.baseband", "no message"});
            Version = (String)result;
        } catch (Exception e) {
        }
        return Version;
    }

}
