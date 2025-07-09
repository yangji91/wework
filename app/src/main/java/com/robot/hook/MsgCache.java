package com.robot.hook;

import java.util.LinkedHashMap;

public class MsgCache extends LinkedHashMap<Long, Object> {
    private int maxSize;

    public MsgCache(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Entry<Long, Object> eldest) {
        boolean isOver = size() > maxSize;
        return isOver;
    }
}
