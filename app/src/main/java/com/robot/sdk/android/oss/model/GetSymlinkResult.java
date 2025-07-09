package com.robot.sdk.android.oss.model;

public class GetSymlinkResult extends OSSResult {
    private String targetObjectName;

    public String getTargetObjectName() {
        return targetObjectName;
    }

    public void setTargetObjectName(String targetObjectName) {
        this.targetObjectName = targetObjectName;
    }
}
