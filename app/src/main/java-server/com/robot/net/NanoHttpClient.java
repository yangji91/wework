package com.robot.net;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.robot.common.Global;
import com.robot.entity.ConvEntity;
import com.robot.entity.UserEntity;
import com.robot.netty.handle.imple.HandleActionNew;
import com.robot.com.BuildConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class NanoHttpClient extends NanoHTTPD {
    private final HandleActionNew handleActionNew = new HandleActionNew();
    //    private static final List<ReqUserConvEntity> userConvEntities = new ArrayList<>();
    private static final List<UserEntity> userList = new ArrayList<>();
    private static final List<ConvEntity> convEntityList = new ArrayList<>();

    public NanoHttpClient() {
        super(8080);
    }

//    public static void resetUserConv() {
//        userConvEntities.clear();
//        userList.clear();
//    }

    /**
     * 设置联系人列表
     *
     */
    public static void cacheUsers(List<UserEntity> users) {
        userList.clear();
        userList.addAll(users);
    }

    public static void cacheUser(UserEntity user) {
        userList.add(user);
    }


    /**
     * 设置会话列表
     *
     * @param convEntitys
     */
    public static void cacheConvs(List<ConvEntity> convEntitys) {
        convEntityList.clear();
        convEntityList.addAll(convEntitys);
    }

    public static void cacheConv(ConvEntity convEntity) {
        convEntityList.add(convEntity);
    }

    @Override
    public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parameters, Map<String, String> files) {
        System.out.println("what:" + uri);
        if (uri.endsWith("index.html")) {
            StringBuilder buffer = new StringBuilder("<html><head><title>");
            buffer.append("sn:" + Global.getDeviceSn() + " 版本：" + BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE);
            buffer.append("</title></head><body><br/><br/>");
            buffer.append("<table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\"style=\"border-collapse: collapse;table-layout: fixed;\">");
            buffer.append("<tr>");
            buffer.append("<th>任务</th>");
            buffer.append("<th>联系人</th>");
            buffer.append("<th>会话</th>");
            buffer.append("</tr>");
            buffer.append("<tr><td   style=\"vertical-align: top;\"><form action=\"execTestHook\" method=\"post\" target=\"_blank\">");
            buffer.append("<input min-height: 30% contenteditable=\"true type=\"textarea\" name=\"params\" style=\"word-break:break-all;BORDER-TOP-STYLE: none;BORDER-RIGHT-STYLE: none; BORDER-LEFT-STYLE: none; BORDER-BOTTOM-STYLE:solid;width: 100%;height: 300px;\"/><br/><br/>");
            buffer.append("<div><input type=\"submit\" style=\"width: 100%;\" value=\"提交\"/></div>");
            buffer.append("</form></td>");
            Gson gson = new Gson();
            //联系人
            buffer.append("<td  style=\"vertical-align: top\"><div style=\"margin-left:20px;margin-right:20px;overflow: auto;word-wrap:break-word;word-break:break-all;\">&nbsp;<br/>");
            for (int index = 0; index < userList.size(); index++) {
                UserEntity userConvEntity = userList.get(index);
                buffer.append(index + 1).append("、").append(gson.toJson(userConvEntity)).append("<br/><br/>");
            }
            buffer.append("</div></td>");
            //会话
            buffer.append("<td  style=\"vertical-align: top\"><div style=\"margin-left:20px;margin-right:20px;overflow:auto;word-wrap:break-word;word-break:break-all;\">&nbsp;<br/>");
            for (int index = 0; index < convEntityList.size(); index++) {
                ConvEntity convEntity = convEntityList.get(index);
                buffer.append(index + 1).append("、").append(gson.toJson(convEntity)).append("<br/><br/>");
            }
            buffer.append("</div></td>");
            buffer.append("</body></html>");
            return newFixedLengthResponse(buffer.toString());
        } else if (uri.endsWith("execTestHook")) {
            String params = parameters.get("params");
            if (params == null || params.length() == 0) {
                return newFixedLengthResponse("invalid request params");
            }
            try {
//                Gson gson = new Gson();
//                JsonObject jsonObject = gson.fromJson(params.trim(), JsonObject.class);
                handleActionNew.onHandleTest(params.trim());

            } catch (Exception e) {
                e.printStackTrace();
            }
            new Handler(Looper.getMainLooper()).post(() -> {
                try {
//                        Gson gson = new Gson();
//                        JsonObject jsonObject = gson.fromJson(params, JsonObject.class);
//                        handleActionNew.onHandle(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return newFixedLengthResponse("OK");
        } else {
            return newFixedLengthResponse("invalid request");
        }
    }
}
