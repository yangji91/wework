package com.robot.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;

import com.robot.com.file.SharePreJuhe;
import com.robot.common.Global;
import com.robot.common.MConfiger;
import com.robot.entity.DeviceModelEnum;
import com.robot.entity.PhoneLocEnum;
import com.robot.netty.proto.req.ReqDeviceInfoEntity;
import com.robot.com.file.SharePreSn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class DeviceUtil {
    @SuppressWarnings("unused")
    private static final String TAG = "DeviceUtil";

    public static int mWidth;
    public static int mHeight;

    static {
        if (mWidth <= 0 || mHeight <= 0) {
            WindowManager wm = (WindowManager) Global.getContext().getSystemService(Context.WINDOW_SERVICE);
            mWidth = wm.getDefaultDisplay().getWidth();
            mHeight = wm.getDefaultDisplay().getHeight();
        }
    }

    /****
     * 获取udid
     * @return
     */
    public static String getBaiduPhoneCode() {
        String code = "";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            code = (String) get.invoke(c, "code");
        } catch (Exception ignored) {
        }
        return code;
    }

    public static String getSn() {
        String sn = null;
        try {
//            MConfiger.phoneLocEnum = PhoneLocEnum.LOCAL;
            SharePreSn mSharePre = new SharePreSn();
            sn = mSharePre.loadData();
//            if (!TextUtils.isEmpty(mSn)) {
//                sn = mSn;
//            } else {
//            sn = getIMEI();
//            }
            MyLog.debug(TAG, "[checkSnDetail]" + " phoneLocalEnum:" + MConfiger.phoneLocEnum + " sn:" + sn);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (sn == null) {
            sn = DeviceUtil.getAndroidID();
        }
        return sn;
    }

//    public static String getJuhe() {
//        String juhe = "";
//        try {
////            MConfiger.phoneLocEnum = PhoneLocEnum.LOCAL;
//            SharePreJuhe mSharePre = new SharePreJuhe();
//            juhe = mSharePre.loadData();
//            if (juhe == null || juhe.equals("null")) juhe = "";
////            if (!TextUtils.isEmpty(mSn)) {
////                sn = mSn;
////            } else {
////            sn = getIMEI();
////            }
//            MyLog.debug(TAG, "[checkSnDetail]" + " phoneLocalEnum:" + MConfiger.phoneLocEnum + " juhe:" + juhe);
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//        return juhe;
//    }

    /***
     * 获得IMEI
     * @return
     */
    @SuppressLint("MissingPermission")
    public static final String getIMEI() {
        String str = "";
        TelephonyManager telManager = (TelephonyManager) Global.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (telManager != null) {
            try {
                str = telManager.getDeviceId();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            if (TextUtils.isEmpty(str)) {
                str = Settings.Secure.getString(Global.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        return str;
    }

    public static final String getAndroidVer() {
        String ver = "";
        ver = Build.VERSION.SDK;
        return ver;
    }

    /****
     * android os序列号
     * @return
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static final String getAndroidSer() {
        String ser = "";
        ser = Build.SERIAL;
        return ser;
    }

    public static final String getAndroidTime() {
        String time = "";
        time = Build.TIME + "";
        return time;
    }

    /***
     * 获取mac地址
     * @return
     */
    public static final String getMacAdd() {
        // 获取mac地址：
        String command = "cat /sys/class/net/wlan0/address ";
        return getStringInfo(command);
    }

    /***
     * 获取CPU信息
     * @return
     */
    public static final String getCpuInfo() {
        String command = "cat /proc/cpuinfo";
        return getStringInfo(command);
    }

    /***
     * 获取android ID
     * @return
     */
    public static final String getAndroidID() {
        String androidID = "";
        androidID = Settings.Secure.getString(Global.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        if (TextUtils.isEmpty(androidID)) {
            androidID = Build.ID;
        }
        return androidID;
    }

    /***
     * 获取屏幕宽度
     * @return
     */
    public static final int getDeviceWidth() {
        return mWidth;
    }

    /***
     * 获取屏幕高度
     * @return
     */
    public static final int getDeviceHeight() {
        return mHeight;
    }


    public static final String getStringInfo(String command) {
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec(command);
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    str = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
            str = "00000000";
        }
        return str;
    }

    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(Build.VERSION.SDK);
        } catch (NumberFormatException e) {
        }
        return version;
    }

    /****
     * 获取android操作系统时间
     * @return
     */
    public static final long getAndroidOSTime() {
        long time = Build.TIME;
        return time;
    }

    /***
     * 获取android Model
     * @return
     */
    public static final String getAndroidModel() {
        String str = "";
        str = Build.MODEL;
        return str;
    }


    /****
     * 获取硬件信息
     * @return
     */
    public static final String getAndroidHardInfo() {
        return Build.HARDWARE;
    }

    // 获得可用的内存
    private static long getmem_UNUSED(Context mContext) {
        long MEM_UNUSED;
        // 得到ActivityManager
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        // 创建ActivityManager.MemoryInfo对象
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // 取得剩余的内存空间
        MEM_UNUSED = mi.availMem;
        return MEM_UNUSED;
    }

    //获得总内存
    private static long getmem_TOLAL() {
        long mTotal;
        // /proc/meminfo读出的内核信息进行解释
        String path = "/proc/meminfo";
        String content = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path), 8);
            String line;
            if ((line = br.readLine()) != null) {
                content = line;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // beginIndex
        int begin = content.indexOf(':');
        // endIndex
        int end = content.indexOf('k');
        // 截取字符串信息
        content = content.substring(begin + 1, end).trim();
        mTotal = Integer.parseInt(content);
        return mTotal;
    }

    public static DeviceModelEnum getDeviceModelType() {
        DeviceModelEnum result = DeviceModelEnum.UNKNOW;
        String brand = Build.BRAND;
        if (brand != null && brand.length() > 0) {
            brand = brand.toLowerCase();
            if (brand.contains("xiaomi") || brand.contains("redmi")) {
                result = DeviceModelEnum.XIAOMI;
            } else if (brand.contains("huawei")) {
                result = DeviceModelEnum.HUAWEI;
            } else if (brand.contains("vivo")) {
                result = DeviceModelEnum.VIVO;
            } else if (brand.contains("oppo")) {
                result = DeviceModelEnum.OPPO;
            } else if (brand.contains("meizu")) {
                result = DeviceModelEnum.MEIZU;
            }
        }
        return result;
    }

    public static String getSerialNumber() throws SecurityException {
        try {
            String serial = getIMEI();
//            if (serial.isEmpty()){
//                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
//                    serial = DeviceInfoManager.getDeviceInfo().getAndroidID();
//                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    serial = Build.getSerial();
//                } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
//                    serial = Build.SERIAL;
//                } else {
//                    Class<?> clz = Class.forName("android.os.SystemProperties");
//                    Method getMethod = clz.getMethod("get", String.class);
//                    getMethod.setAccessible(true);
//                    serial = (String) getMethod.invoke(clz, "ro.serialno");
//                }
//            }

            return serial;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getValByPropertis(String key) {
        Class<?> clz = null;
        String val = null;
        try {
            clz = Class.forName("android.os.SystemProperties");
            Method getMethod = clz.getMethod("get", String.class);
            getMethod.setAccessible(true);
            String serial = (String) getMethod.invoke(clz, key);
            val = serial;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return val;
    }

    public static final ReqDeviceInfoEntity.BaseInfoEntity getBaseInfo() {
        ReqDeviceInfoEntity.BaseInfoEntity baseInfoEntity = new ReqDeviceInfoEntity.BaseInfoEntity();
        baseInfoEntity.androidSys = Build.VERSION.RELEASE;
        baseInfoEntity.androidSysName = Build.BRAND;
        baseInfoEntity.phoneModel = Build.MODEL;
        baseInfoEntity.board = Build.BOARD;
        baseInfoEntity.baseband = Build.BOOTLOADER;
        baseInfoEntity.cpuModel = getCpuName();
        baseInfoEntity.resolution = DeviceUtil.getDeviceWidth() + "X" + DeviceUtil.getDeviceHeight();
        baseInfoEntity.deviceSer = Build.SERIAL;
        return baseInfoEntity;
    }

    private static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getCpuNumCores() {
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            Log.d(TAG, "CPU Count: " + files.length);
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            //Print exception
            Log.d(TAG, "CPU Count: Failed.");
            e.printStackTrace();
            //Default to return 1 core
            return 1;
        }
    }

    public static String getMinCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }

    /**
     * int类型ip转string类型ip
     */
    public static String intIpToStringIp(int intIp) {
        if (intIp != 0) {
            final long[] mask = {0x000000FF, 0x0000FF00, 0x00FF0000, 0xFF000000};
            final StringBuilder ipAddress = new StringBuilder();
            for (int i = 0; i < mask.length; i++) {
                ipAddress.insert(0, (intIp & mask[i]) >> (i * 8));
                if (i < mask.length - 1) {
                    ipAddress.insert(0, ".");
                }
            }
        }
        return "0.0.0.0";
    }


    /**
     * string类型ip转int类型ip
     */
    public static int stringIpToIntIp(String ip) {
        String[] ips = ip.split("\\.");
        if (ips.length != 4) {
            throw new IllegalArgumentException("请传入正确的ipv4地址");
        }
        StringBuilder str = new StringBuilder();
        for (String s : ips) {
            int i = Integer.parseInt(s);
            if (i > 255 || i < 0) {
                throw new IllegalArgumentException("请传入正确的ipv4地址");
            }
            String bs = Integer.toBinaryString(i);
            str.append(String.format("%8s", bs).replace(" ", "0"));
        }

        //二进制字符串转10进制,因为Integer.parseInt对负数转的问题,所以自己手写了转化的方法
        int n = 0;
        for (int i = 0; i < str.length(); i++) {
            String a = str.substring(i, i + 1);
            n = n << 1;
            if (a.equals("1")) {
                n = n | 1;
            }
        }
        return n;
    }


}
