package com.robot.com.file;

import com.robot.util.FileUtil;

public class SharePrePermissonTime extends SharePreBase{
    private final String FILE_NAME = FileUtil.getComRobotPath() + "/" + "PermissionTimes.txt";

    @Override
    protected String getFileName() {
        return FILE_NAME;
    }
}
