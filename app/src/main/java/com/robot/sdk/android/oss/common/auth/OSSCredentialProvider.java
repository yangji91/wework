package com.robot.sdk.android.oss.common.auth;

import com.robot.sdk.android.oss.ClientException;

/**
 * Created by zhouzhuo on 11/4/15.
 */
public interface OSSCredentialProvider {

    /**
     * get OSSFederationToken instance
     *
     * @return
     */
    OSSFederationToken getFederationToken() throws ClientException;
}
