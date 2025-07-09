package com.robot.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import com.robot.common.MConfiger;
import com.robot.entity.BCheckEntity;
import java.io.File;

public class CheckUtil {
    private static final String TAG = CheckUtil.class.getSimpleName();

    /**
     *  public int verCode;
     *     public String verName;
     *     public String fileMd5;
     *
     *     public boolean isHookOk;
     *     public boolean isRoot;  //手机是否获取root
     *     public int andrVer;     //android verion版本 6-7
     * @return
     */
    public static final BCheckEntity check(Context mContext){
        BCheckEntity entity = new BCheckEntity();
        String pkgName = MConfiger.WX_ENTERPISE_PKGNAME;
        ApplicationInfo appInfo = getApplicationInfo(mContext,pkgName);
        if(appInfo == null){
            pkgName = MConfiger.WX_ENTERPIRSE_PKGNAME_WO;
            appInfo = getApplicationInfo(mContext,pkgName);
        }
        PackageInfo packageInfo = getPackageInfo(mContext,pkgName);
        if(packageInfo != null){
            entity.verCode = packageInfo.versionCode;
            entity.verName = packageInfo.versionName;
            String filePath = appInfo.sourceDir;
            File file = new File(filePath);
            entity.fileLen = file.length();
        }
        ApplicationInfo robotInfo = getApplicationInfo(mContext,MConfiger.SELF_PKGNAME);
        PackageInfo robotPackageInfo = getPackageInfo(mContext,MConfiger.SELF_PKGNAME);
        if(robotPackageInfo != null){
            entity.robotVerCode = robotPackageInfo.versionCode;
            entity.robotVerName = robotPackageInfo.versionName;
            String filePath = robotInfo.sourceDir;
            File file = new File(filePath);
            entity.robotFileLen = file.length();
        }
        //boolean check
        int androidVer = Build.VERSION.SDK_INT;
        entity.andrVer = androidVer;
        MyLog.debug(TAG,"[check]" + " entity:" + entity.toString());
        return entity;
    }

    public static ApplicationInfo getApplicationInfo(Context mContext,String pkgName){
        ApplicationInfo applicationInfo = null;
        try {
            ApplicationInfo appInfo = mContext.getPackageManager().getApplicationInfo(pkgName,0);
            applicationInfo = appInfo;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return applicationInfo;
    }

    public static PackageInfo getPackageInfo(Context mContext,String pkgName){
        PackageInfo result = null;
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(pkgName, 0);
            if(info != null){
                result = info;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }
}