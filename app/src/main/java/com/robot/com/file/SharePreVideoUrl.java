package com.robot.com.file;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.robot.common.Global;

public class SharePreVideoUrl {
    private final String FILE_NAME = "RMS_URL";

    public SharePreVideoUrl(){}

    public void saveData(String localUrl,String path){
        if(!TextUtils.isEmpty(localUrl) && !TextUtils.isEmpty(path)){
            Context mContext = Global.getContext();
            SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(localUrl,path);
            editor.commit();
        }
    }

    public String loadUrl(String localUrl){
        String url = null;
        Context mContext = Global.getContext();
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        url = sp.getString(localUrl,"");
        return url;
    }

}
