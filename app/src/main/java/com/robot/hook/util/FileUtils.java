package com.robot.hook.util;

import androidx.annotation.Nullable;

import com.robot.robothook.utils.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class FileUtils {

    public static @Nullable File getRecentFile(@Nullable String path) {
        if (StringUtils.isEmpty(path)){
            return null;
        }
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {
            Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
            return files[0];
        }
        return null;
    }
}
