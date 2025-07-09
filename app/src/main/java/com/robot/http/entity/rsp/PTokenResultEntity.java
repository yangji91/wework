package com.robot.http.entity.rsp;

import android.text.TextUtils;

import com.robot.sdk.android.oss.common.OSSLog;
import com.robot.sdk.android.oss.common.utils.DateUtil;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class PTokenResultEntity implements Serializable {
    public String result;
    public String accesskey_id;
    public String accesskey_secret;
    public String security_token;
    public String expiration;
    public String bucket_name;
    public String endpoint;

    public boolean isSuccess() {
        if (result == null || !"success".equalsIgnoreCase(result)) {
            return false;
        }

        return !TextUtils.isEmpty(accesskey_id) &&
                !TextUtils.isEmpty(accesskey_secret) &&
                !TextUtils.isEmpty(security_token) &&
                !TextUtils.isEmpty(expiration) &&
                !TextUtils.isEmpty(bucket_name) &&
                !TextUtils.isEmpty(endpoint);
    }

    @Override
    public String toString() {
        return "TokenResult{" +
                "result='" + result + '\'' +
                ", accesskey_id='" + accesskey_id + '\'' +
                ", accesskey_secret='" + accesskey_secret + '\'' +
                ", security_token='" + security_token + '\'' +
                ", expiration='" + expiration + '\'' +
                ", bucket_name='" + bucket_name + '\'' +
                ", endpoint='" + endpoint + '\'' +
                '}';
    }

    private long getExpirationInGMTFormat(String expirationInGMTFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = sdf.parse(expirationInGMTFormat);
            return date.getTime();
        } catch (ParseException e) {
            if (OSSLog.isEnableLog()) {
                e.printStackTrace();
            }
            return DateUtil.getFixedSkewedTimeMillis() + 30 * 1000;
        }
    }

    private static final Long EXPIRATION_TIME = 5 * 60 * 1000L;

    /**
     * 是否过期
     */
    public boolean isExpiration(){
     return   DateUtil.getFixedSkewedTimeMillis() >
                getExpirationInGMTFormat(expiration) - EXPIRATION_TIME;
    }
}
