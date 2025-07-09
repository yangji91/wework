package com.robot.http.task.base;

import com.robot.http.entity.PHttpHeaderInfo;

import java.util.Map;

public abstract class ReqBaseEntity {
    protected final String TAG = getClass().getSimpleName();

    public boolean isJsonArray;

    public boolean isFullURL;

    public String hostUrl;

    public PHttpHeaderInfo headerInfo;

    public boolean isGet = false;

    public boolean isPostJson;

    abstract public String getReqURL();

    abstract public Map<String,Object> getReqMap();
}
