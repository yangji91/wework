package com.robot.util;

import android.os.SystemClock;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLException;

import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OKHttpUtil {
    private int readTimeout = 10000;
    private int connectTimeout = 10000;
    private boolean isRetry = true;
    private int retryCount = 3;
    private long retryDelay = 1000;

    public static okhttp3.OkHttpClient getNewClient() {
        return new OKHttpUtil().okHttpClient();
    }

    public okhttp3.OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        ConnectionPool pool = new ConnectionPool(100, 30, TimeUnit.MINUTES);
        builder.connectionPool(pool);
        builder.retryOnConnectionFailure(isRetry);
        RetryInterceptor retryIntercepter = new RetryInterceptor(retryCount, retryDelay);
        builder.addInterceptor(retryIntercepter);
        builder.followRedirects(false);
        return builder.build();
    }

    public final class RetryInterceptor implements Interceptor {

        private final int retryCount;
        private final long retryDelay;
        private int retryNum = 0;//假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）
        private final Set<Class<? extends IOException>> nonRetriableClasses;//不会重试的类型

        protected RetryInterceptor(int retryCount, long retryDelay, Collection<Class<? extends IOException>> clazzes) {
            this.retryCount = retryCount;
            this.retryDelay = retryDelay;
            this.nonRetriableClasses = new HashSet();
            if (clazzes != null) {
                Iterator noRetryIterator = clazzes.iterator();
                while (noRetryIterator.hasNext()) {
                    Class<? extends IOException> clazz = (Class) noRetryIterator.next();
                    this.nonRetriableClasses.add(clazz);
                }
            }
        }

        public RetryInterceptor(int retryCount, long retryDelay) {
            this(retryCount, retryDelay, Arrays.asList(InterruptedIOException.class, UnknownHostException.class, SSLException.class, ConnectException.class));
        }

        public RetryInterceptor() {
            this(3, 500L);
        }

        public Response intercept(Chain chain) throws IOException {
            Response response = null;
            retryNum=0;
            try {
                Request request = chain.request();
                response = chain.proceed(request);
                while ((response==null||!response.isSuccessful()) && retryNum < retryCount) {
                    retryNum++;
                        MyLog.debug("OKHttpUtil", "重试第{" + retryNum + "}次,url:{" + request.url().url() + "}");
                    SystemClock.sleep(retryDelay);
                    response = chain.proceed(request);
                }
                return response;
            } catch (Exception e) {
                MyLog.debug("OKHttpUtil", "请求错误了:" + e.getMessage(),true);
            }
            return response;
        }


    }


}

