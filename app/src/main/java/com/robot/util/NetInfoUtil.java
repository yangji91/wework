package com.robot.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.robot.common.Global;

import java.io.Serializable;

public class NetInfoUtil {

    public static final class NetInfoEntity implements Serializable {
        public String ssid;
        public String macAddr;
        public String ipAddr;

        @Override
        public String toString() {
            return "NetInfoEntity{" +
                    "ssid='" + ssid + '\'' +
                    ", macAddr='" + macAddr + '\'' +
                    ", ipAddr='" + ipAddr + '\'' +
                    '}';
        }
    }

    public static final NetInfoEntity getNetInfoEntity() {
        NetInfoEntity rEntity = new NetInfoEntity();
        rEntity.ssid = getWifiSSID();
        rEntity.macAddr = getMacAddress();
        rEntity.ipAddr = getIpAddr();
        return rEntity;
    }

    private static final String getIpAddr() {
        String strIp = null;
        WifiManager wifiMgr = (WifiManager) Global.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiMgr != null) {
            WifiInfo info = wifiMgr.getConnectionInfo();
            if (info != null) {
                int ip = info.getIpAddress();
                strIp = intToIp(ip);
            }
        }
        return strIp;
    }

    private static String intToIp(int paramInt) {
        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
                + (0xFF & paramInt >> 24);
    }

    private static final String getMacAddress() {
        String macAddr = null;
        WifiManager wifiMgr = (WifiManager) Global.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiMgr != null) {
            WifiInfo info = wifiMgr.getConnectionInfo();
            if (info != null) {
                macAddr = info.getMacAddress();
            }
        }
        return macAddr;
    }

    private static final String getWifiSSID() {
        String ssid = null;
        WifiManager wifiMgr = (WifiManager) Global.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiMgr != null) {
            WifiInfo info = wifiMgr.getConnectionInfo();
            if (info != null) {
                ssid = info.getSSID();
            }
        }
        return ssid;
    }
}
