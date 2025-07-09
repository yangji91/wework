package com.robot.http;

import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;

import com.robot.common.CrashHandler;
import com.robot.common.FloatHelper;
import com.robot.common.StatsHelper;
import com.google.gson.Gson;
import com.robot.common.Global;
import com.robot.common.MConfiger;
import com.robot.entity.ActionResultEnum;
import com.robot.entity.ResEntity;
import com.robot.exception.MessageException;
import com.robot.http.entity.PHttpHeaderInfo;
import com.robot.util.MyLog;
import com.robot.util.OKHttpUtil;
import com.robot.util.StrUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {
    private static final String TAG = HttpUtil.class.getSimpleName();
    //重试次数
    private static final int RETRY_COUNT = 2;
    /**
     * 重新下载的时间 24小时
     */
    public static final int REDOWNLOAD_TIME = 60 * 1000 * 60 * 24;//

    public static ResEntity<String> sendGetMessage(String path, Map<String, Object> params, String encode) {
        return sendGetMessage(path, params, encode, true, true);
    }

    public static ResEntity<String> sendPostMsgByJsonIsCircle(String path, Map<String, Object> params, String encode, PHttpHeaderInfo headerInfo, boolean isCircle) {
        ResEntity<String> resEntity = new ResEntity<>();
        do {
            resEntity = sendPostMsgByJson(path, params, encode, headerInfo);
            try {
                MyLog.debug(TAG, "[sendPostMsgByJsonIsCircle]" + " rsp:" + resEntity.getData() + " msg:" + resEntity.getMsg());
                Thread.sleep(1000 * 2);
            } catch (Throwable e) {
                e.printStackTrace();
                StatsHelper.event("CrashHandler", "handleExceptionLoop", "sendPostMsgByJson throwable " + e + " stackTrace " + CrashHandler.getStackTrace(e), "time " + StrUtils.getTimeDetailStr());
            }
        } while (!resEntity.isSucc() && isCircle);
        return resEntity;
    }

    public static ResEntity<String> sendPostMsgByJson(String path, Map<String, Object> params, String encode, PHttpHeaderInfo headerInfo) {
        ResEntity<String> resEntity = null;
        URL url = null;
        try {
            url = new URL(path);
            byte[] mydata = null;
            if (params != null && params.size() > 0) {
                Gson gson = new Gson();
                String strData = gson.toJson(params);
                mydata = strData.getBytes(encode);
            }
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10 * 1000);
            connection.setDoInput(true);//表示从服务器获取数据
            connection.setDoOutput(true);//表示向服务器写数据
            connection.setRequestMethod("POST");
            //是否使用缓存
            connection.setUseCaches(false);
            //header info
            if (headerInfo != null) {
                Gson gson = new Gson();
                connection.setRequestProperty("info", gson.toJson(headerInfo));
            }
            //表示设置请求体的类型是文本类型
            connection.setRequestProperty("Content-Type", "application/json");
            if (mydata != null) {
                connection.setRequestProperty("Content-Length", String.valueOf(mydata.length));
            }
            connection.connect();   //连接，不写也可以。。？？有待了解
            //获得输出流，向服务器输出数据
            OutputStream outputStream = connection.getOutputStream();
            if (mydata != null) {
                outputStream.write(mydata, 0, mydata.length);
            }
            //获得服务器响应的结果和状态码
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                resEntity = changeInputeStream(connection.getInputStream(), encode, true);
            } else {
                resEntity = ResEntity.genErr("-2", responseCode + "");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            resEntity = ResEntity.genErr("-1", "网络异常~");
        } catch (ProtocolException e) {
            e.printStackTrace();
            resEntity = ResEntity.genErr("-1", "协议异常~");
        } catch (IOException e) {
            e.printStackTrace();
            resEntity = ResEntity.genErr("-1", "IO异常~");
        }
        return resEntity;
    }

    private static String getParams(Map<String, Object> params) {
        StringBuilder buffer = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            //迭代器
            //Map.Entry 是Map中的一个接口，他的用途是表示一个映射项（里面有Key和Value）
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                Object val = entry.getValue();
                if (val != null && !TextUtils.isEmpty(val + "")) {
                    try {
                        buffer.append(key).append("=").
                                append(URLEncoder.encode(entry.getValue() + "", "UTF-8")).
                                append("&");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            buffer.deleteCharAt(buffer.length() - 1);

        }
        return buffer.toString();
    }

    /**
     * @param params 填写的url的参数
     * @param encode 字节编码
     * @return
     */
    public static ResEntity<String> sendGetMessage(String path, Map<String, Object> params, String encode, boolean isGet, boolean isCircleForceGet) {
        ResEntity resEntity;
        if (isGet) {
            path = path + "?" + getParams(params);
        }
        do {
            URL url = null;
            try {
                url = new URL(path);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                resEntity = ResEntity.genErr("-1", "网络异常~");
            }
//            params.put("version", MConfiger.VERSION);
//            //时间t
//            params.put("t",System.currentTimeMillis()+"");
            byte[] mydata = null;
            try {//把请求的主体写入正文！！
//            System.out.println(buffer.toString());
                //删除最后一个字符&，多了一个;主体设置完毕
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(10 * 1000);
                connection.setReadTimeout(10 * 1000);
                //是否使用缓存
                connection.setUseCaches(false);
                //表示设置请求体的类型是文本类型
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                if (isGet) {
                    connection.setRequestMethod("GET");
                } else {
                    connection.setDoInput(true);//表示从服务器获取数据
                    connection.setDoOutput(true);//表示向服务器写数据
                    connection.setRequestMethod("POST");
                    String strParams = getParams(params);
                    MyLog.debug(TAG, "[sendGetMessage]" + " strParams:" + strParams);
                    if (strParams != null && strParams.length() > 0) {
                        mydata = strParams.getBytes(encode);
                        if (mydata != null) {
                            connection.setRequestProperty("Content-Length", String.valueOf(mydata.length));
                        }
                        //获得输出流，向服务器输出数据
                        OutputStream outputStream = connection.getOutputStream();
                        if (mydata != null) {
                            outputStream.write(mydata, 0, mydata.length);
                        }
                    }
                }
                //获得服务器响应的结果和状态码
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    resEntity = changeInputeStream(connection.getInputStream(), encode, true);
                } else {
                    resEntity = changeInputeStream(connection.getErrorStream(), encode, false);
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                resEntity = ResEntity.genErr(null, e.getMessage());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                resEntity = ResEntity.genErr(null, e.getMessage());
            } catch (Throwable th) {
                th.printStackTrace();
                resEntity = ResEntity.genErr(null, th.getMessage());
            }
            MyLog.debug(TAG, "[sendGetMessage]" + " resEntity:" + resEntity + " sleep... isSucc:" + resEntity == null ? "null" : resEntity.isSucc() + " thread:" + Thread.currentThread().getName() + " path:" + path);
            MyLog.debug(TAG, "[sendGetMessage]" + " rspMsg:" + resEntity.getData());
            if (isCircleForceGet && !resEntity.isSucc()) {
                try {
                    Thread.sleep(1000 * 6);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!resEntity.isSucc()) {
                    String tips = "运行失败\n服务器异常，再次尝试~"+ "\n" + "\n" + "设备标识:" + Global.getDeviceSn() + "\n" ;
                    MConfiger.mRobotStatus = -1;
                    MConfiger.mRobotTips = tips;
                    FloatHelper.notifyData();
                }
            }
        } while (!resEntity.isSucc() && isCircleForceGet);
        MyLog.debug(TAG, "[sendGetMessage]" + " result -> isSucc:" + resEntity.isSucc());
        return resEntity;
    }


    /**
     * 将一个输入流转换成字符串
     *
     * @param inputStream
     * @param encode
     * @return
     */
    private static ResEntity<String> changeInputeStream(InputStream inputStream, String encode, boolean succ) {
        //通常叫做内存流，写在内存中的
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        String result = "";
        if (inputStream != null) {
            try {
                while ((len = inputStream.read(data)) != -1) {
                    data.toString();
                    outputStream.write(data, 0, len);
                }
                //result是在服务器端设置的doPost函数中的
                result = new String(outputStream.toByteArray(), encode);
                outputStream.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        ResEntity<String> resEntity;
        if (succ) {
            resEntity = ResEntity.genSucc(result, -1);
        } else {
            resEntity = ResEntity.genErr(result, null);
        }
        return resEntity;
    }

    private static InputStream getImageStream(String path) throws MessageException, IOException {
        return getImageStreamOKHttp(path, 3);
    }


    private static InputStream getImageStreamOKHttp(String path, int retry) throws MessageException {
        for (int i = 1; i <= retry; i++) {//重试三次
            Request request = new Request.Builder()
                    .url(path)
                    .addHeader("Connection", "close")
                    .build();
            OkHttpClient client = OKHttpUtil.getNewClient();
            try {
                Response response = client.newCall(request).execute();
                if (response != null && response.isSuccessful()) {
                    return response.body().byteStream();
                }
            } catch (IOException e) {
                if (i == retry)
                    throw new MessageException(e.getMessage());
            }
        }
        throw new MessageException("图片下载失败 " + path);


    }

    /**
     * Get image from newwork
     *
     * @param path The path of image
     * @return InputStream
     * @throws Exception
     */
    private static InputStream getImageStream(String path, int retry) throws MessageException {
        try {
            if (TextUtils.isEmpty(path) || !path.startsWith("http")) {
                throw new MessageException("图片下载链接非法:" + path);
            }
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10 * 1000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Connection", "close");
            conn.setRequestProperty("Accept-Encoding", "identity");
            conn.setDoInput(true);
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                MyLog.debug("HttpUtil", "[onHandlePushMsg]" + " 下载文件 code:" + conn.getResponseCode());
                return conn.getInputStream();
            } else {
                if (retry < RETRY_COUNT) {
                    retry++;
                    MyLog.debug("HttpUtil", " 下载文件" + path + " getResponseCode:" + conn.getResponseCode() + "  重试第" + retry + "次", true);
                    SystemClock.sleep(1000);
                    return getImageStream(path, retry);
                }
            }
        } catch (IOException e) {
            if (retry < RETRY_COUNT) {
                retry++;
                MyLog.debug("HttpUtil", " 下载文件" + path + " IOException:" + e.getMessage() + "  重试第" + retry + "次", true);
                SystemClock.sleep(1000);
                return getImageStream(path, retry);
            }
        }

        return null;
    }

    /**
     * 保存 网络文件 如果存在 则不下载
     *
     * @param httpPath 请求地址
     * @param file     文件地址
     * @return
     * @throws Exception
     */
    private static String saveIntNetFile(String httpPath, File file) throws MessageException {

        try {
            if (!file.exists()) {
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                file.createNewFile();
            } else {

                if (file.length() > 0 && System.currentTimeMillis() - file.lastModified() < REDOWNLOAD_TIME) {//文件时间在一天内的
                    MyLog.debug("PushAction", "[onHandlePushMsg]" + " 下载文件存在" + (System.currentTimeMillis() - file.lastModified()));
                    return file.getAbsolutePath();
                }

            }
        } catch (IOException e) {
            MyLog.debug("PushLittleAppAction", "[onHandlePushMsg]" + " 下载文件异常 IOException:" + e.getMessage(), true);
            throw new MessageException(ActionResultEnum.ACTION_DOWNLOAD_FILE_FAIL);
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new MessageException("主线程下载文件！！");
        } else {
            return saveIntNetFileThread(httpPath, file);
        }

    }

    /**
     * 保存 网络文件 如果存在 则不下载
     *
     * @param httpUrl  请求地址
     * @param filePath 文件地址
     * @return
     * @throws Exception
     */
    public static String saveIntNetFile(String httpUrl, String filePath) throws MessageException {
        return saveIntNetFile(httpUrl, new File(filePath));
    }

    private static String saveIntNetFileThread(String httpPath, File file) throws MessageException {

        try {

            InputStream inputStream = getImageStream(httpPath);

            if (inputStream == null)
                throw new MessageException(ActionResultEnum.ACTION_DOWNLOAD_FILE_FAIL);
            FileOutputStream fos = new FileOutputStream(file);
            int length = 0;
            byte[] buff = new byte[1024 * 100];
            while ((length = inputStream.read(buff)) > 0) {
                fos.write(buff, 0, length);
                fos.flush();
            }
            if (fos != null)
                fos.close();
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            MyLog.debug("PushLittleAppAction", "[onHandlePushMsg]" + " 下载文件 FileNotFoundException:" + e.getMessage());
            throw new MessageException(ActionResultEnum.ACTION_FILE_NOT_EXIST);
        } catch (IOException e) {
            MyLog.debug("PushLittleAppAction", "[onHandlePushMsg]" + " 下载文件 IOException:" + e.getMessage());
            throw new MessageException(ActionResultEnum.ACTION_FILE_LEN_ZERO);
        } catch (MessageException e) {
            MyLog.debug("PushLittleAppAction", "[onHandlePushMsg]" + " 下载文件 Exception:" + e.getMessage());
            throw e;
        }
        return file.getAbsolutePath();
    }
}
