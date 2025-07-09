package com.robot.entity;

import android.text.TextUtils;

import java.io.Serializable;

public class DeviceInfoEntity implements Serializable{
    public String mGroupId;
    public String mDeviceId;


    @Override
    public String toString() {
        return mGroupId + "," + mDeviceId;
    }

    public static final DeviceInfoEntity parseInfo(String strInfo){
        String[] arr = strInfo.split("\n");
        DeviceInfoEntity entity = new DeviceInfoEntity();
        entity.mGroupId = arr[0];
        entity.mDeviceId = arr[1];
        return entity;
    }

    public static final String entity2Str(DeviceInfoEntity entity){
        StringBuilder builder = new StringBuilder();
        builder.append(entity.mGroupId);
        builder.append("\n");
        builder.append(entity.mDeviceId);
        return builder.toString();
    }

    public boolean isEmpty(){
        return TextUtils.isEmpty(mGroupId) || TextUtils.isEmpty(mDeviceId);
    }
}
