package com.robot.util;

import android.annotation.SuppressLint;
import android.os.Build;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

public class CPUUtils {
    private static final String TAG = CPUUtils.class.getSimpleName();

    public static final class CPUInfoEntity implements Serializable {
        public String cpuFramework;
        public String cpuModel;
        public String cpuType;
        public String cpuFreq;
        public String cputech;
        public String cpuMarketTime;
        public Integer cpuCore;

        @Override
        public String toString() {
            return "CPUInfoEntity{" +
                    "cpuFramework='" + cpuFramework + '\'' +
                    ", cpuModel='" + cpuModel + '\'' +
                    ", cpuType='" + cpuType + '\'' +
                    ", cpuFreq='" + cpuFreq + '\'' +
                    ", cputech='" + cputech + '\'' +
                    ", cpuMarketTime='" + cpuMarketTime + '\'' +
                    ", cpuCore=" + cpuCore +
                    '}';
        }
    }

    public static final CPUInfoEntity getCpuInfo(){
        CPUInfoEntity rEntity = new CPUInfoEntity();
        try {
            rEntity.cpuFramework = getCPUABI();
            rEntity.cpuModel = DeviceUtil.getCpuInfo();
            rEntity.cpuFreq = DeviceUtil.getMinCpuFreq();
            rEntity.cpuCore = DeviceUtil.getCpuNumCores();
        }catch (Exception ee){
            MyLog.error(TAG,ee);
        }
        return rEntity;
    }

    @SuppressLint("MissingPermission")
    private static String getCPUSerial() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Build.getSerial();
        }
        //读取CPU信息
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        String cpu = null;
        try {
            Process process = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            inputStreamReader = new InputStreamReader(process.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            while ((cpu = bufferedReader.readLine()) != null) {
                if (cpu.contains("Serial")) {
                    cpu = cpu.substring(cpu.indexOf(":") + 1).trim();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return cpu != null ? cpu.toUpperCase() : "0000000000000000";
    }

    private static String getCPUABI() {
        String CPUABI = null;
        if (CPUABI == null) {
            try {
                String os_cpuabi = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop ro.product.cpu.abi").getInputStream())).readLine();
                if (os_cpuabi.contains("x86")) {
                    CPUABI = "x86";
                } else if (os_cpuabi.contains("armeabi-v7a") || os_cpuabi.contains("arm64-v8a")) {
                    CPUABI = "armeabi-v7a";
                } else {
                    CPUABI = "armeabi";
                }
            } catch (Exception e) {
                CPUABI = "armeabi";
            }
        }
        return CPUABI;
    }

}
