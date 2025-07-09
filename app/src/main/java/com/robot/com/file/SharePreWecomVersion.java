package com.robot.com.file;

import com.robot.util.FileUtil;

public class SharePreWecomVersion extends SharePreBase{
    private final String FILE_NAME = FileUtil.getComRobotPath() + "/" + "RMS_WECOM_VERSION.txt";

    @Override
    protected String getFileName() {
        return FILE_NAME;
    }
}
