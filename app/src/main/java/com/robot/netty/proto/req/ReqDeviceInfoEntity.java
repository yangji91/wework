package com.robot.netty.proto.req;

import android.os.Build;

import com.robot.common.Global;
import com.robot.entity.DeviceModelEnum;
import com.robot.com.BuildConfig;
import com.robot.util.CPUUtils;
import com.robot.util.DeviceUtil;
import com.robot.util.MyLog;
import com.robot.util.NetInfoUtil;
import com.robot.util.OSUtils;

import java.io.Serializable;
import java.util.List;

public class ReqDeviceInfoEntity implements Serializable {
    private String TAG = getClass().getSimpleName();

    public List<PRobotEntity> robotInfos;
    public String deviceId;
    public long lastUpdateTime;  //更新时间
    public String agentVersion;  //版本探针
    public String weworkVersion; //企业微信版本
    public int osType;           //os类型 1:Android 2:Windows 3:Web
    public String osVersion;     //安卓版本
    public int uiType;           //系统类型 0: 未知 1: 小米 2: 华为 3: vivo 4: oppo 5: 魅族
    public String serialNo;     //SN号
    public int storageCapacity;  //系统容量
    public int usedCapacity;     //已使用容量
//    public int status;         //设备状态
    public int isUpdate;         //0：insert（所有参数必填） 1：update
    public int status;          //状态
    public BaseInfoEntity baseInfo; //基本信息
    public CPUUtils.CPUInfoEntity cpuInfo;  //CPU信息
    public OSUtils.OSInfoEntity osInfo; //操作系统信息
    public NetInfoUtil.NetInfoEntity netInfo;//网络信息
    public int platform;    //1：线下 2:华为 3:百度

    public static class BaseInfoEntity implements  Serializable{
        public String phoneModel;   //手机型号
        public String board;    //主板
        public String androidSys;//android操作系统
        public String androidSysName;   //版本名
        public String baseband;
        public String cpuModel; //cpu型号
        public String gpuRender;    //gpu渲染器
        public String resolution;   //分辨率
        public String deviceSer;    //设备序列号

        @Override
        public String toString() {
            return "BaseInfoEntity{" +
                    "phoneModel='" + phoneModel + '\'' +
                    ", board='" + board + '\'' +
                    ", androidSys='" + androidSys + '\'' +
                    ", androidSysName=" + androidSysName + '\'' +
                    ", baseband='" + baseband + '\'' +
                    ", cpuModel='" + cpuModel + '\'' +
                    ", gpuRender='" + gpuRender + '\'' +
                    ", resolution='" + resolution + '\'' +
                    ", deviceSer='" + deviceSer + '\'' +
                    '}';
        }
    }


    public static class PRobotEntity implements Serializable{
        public String tel;  //base 64
        public String imei;
        public int work;    //是否在工作时间
        public int status;  //微信状态 默认 0：正常 1：未知异常 2：下线 3：登陆账号变更
        public int wxStatus;//微信状态 0正常 1未知异常 2下线被顶 3封号
        public String nickname;
        public String icon;
        public String qrcode;
        public long robotUin;   //早期使用remoteId替换
        public Long bbsId;     //企业Id
        public String bbsName;   //企业名称
        public String acctid;   //UserId账号id
    }

    public ReqDeviceInfoEntity(){
        agentVersion = BuildConfig.VERSION_NAME;
        weworkVersion = Global.getWecomVersion();
        osType = 1;
        osVersion = DeviceUtil.getAndroidSDKVersion() + "";
        uiType = DeviceModelEnum.getDeviceModelByName(Build.MODEL).getType();
        MyLog.debug(TAG,"[ReqDeviceInfoEntity]" + " agentver:" + agentVersion + " weworkVer:" + weworkVersion + " osType:" + osType + " osVer:" + osVersion + " uiType:" + uiType);
    }


    @Override
    public String toString() {
        return "ReqDeviceInfoEntity{" +
                "TAG='" + TAG + '\'' +
                ", robotInfos=" + robotInfos +
                ", deviceId='" + deviceId + '\'' +
                ", lastUpdateTime=" + lastUpdateTime +
                ", agentVersion='" + agentVersion + '\'' +
                ", weworkVersion='" + weworkVersion + '\'' +
                ", osType=" + osType +
                ", osVersion='" + osVersion + '\'' +
                ", uiType=" + uiType +
                ", serialNo='" + serialNo + '\'' +
                ", storageCapacity=" + storageCapacity +
                ", usedCapacity=" + usedCapacity +
                ", isUpdate=" + isUpdate +
                ", baseInfo=" + baseInfo +
                ", cpuInfo=" + cpuInfo +
                ", osInfo=" + osInfo +
                ", netInfo=" + netInfo +
                '}';
    }
}
