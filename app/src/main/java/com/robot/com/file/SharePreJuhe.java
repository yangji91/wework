package com.robot.com.file;

import com.robot.util.FileUtil;

public class SharePreJuhe extends SharePreBase{
    private final String FILE_NAME = FileUtil.getComRobotPath() + "/" + "RMS_JUHE.txt";

    @Override
    protected String getFileName() {
        return FILE_NAME;
    }
}
