package com.robot.com.file;

import com.robot.util.FileUtil;

/**
 * 切换模式
 */
public class SharePreModel extends SharePreBase{
    protected final String FILE_NAME = FileUtil.getComRobotPath() + "/" + "RMS_MODEL.txt";

    @Override
    protected String getFileName() {
        return this.FILE_NAME;
    }
}
