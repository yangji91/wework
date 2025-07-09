package com.robot.com.file;

import com.robot.util.FileUtil;

public class ShareDetectSafeStatus extends SharePreBase{
    private final String FILE_NAME = FileUtil.getComRobotPath() + "/" + "RMS_DETECT_SAFE_STATUS.txt";

    @Override
    protected String getFileName() {
        return FILE_NAME;
    }
}
