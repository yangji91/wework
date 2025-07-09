package com.robot.entity;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class BResultEntity implements Serializable {
    public boolean isOk;
    @Nullable
    public String tips;
    @Nullable
    public String desc;

    @Override
    public String toString() {
        return "BResultEntity{" +
                "isOk=" + isOk +
                ", tips='" + tips + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
