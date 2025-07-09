package com.robot.controller.db;

import com.robot.hook.KeyConst;
import com.robot.util.MyLog;
import com.robot.util.ProxyUtil;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.robot.robothook.RobotHelpers;

/***
 *@author 
 *@date 2021/7/30
 *@description 数据库相关
 ****/
public class DBController {

    private String TAG = "DBController";

    private String InfoDB = "Info.db";

    private String SessionDB = "Session.db";

    private String ContactDB = "Contact.db";

    private static DBController instance;

    public static DBController getInstance() {
        if (instance == null) {
            instance = new DBController();
        }
        return instance;
    }

    public String getDBPath(ClassLoader classLoader, String dbName) {
        MyLog.debug(TAG, " getDBPath =" + dbName);
        Object ISetting = RobotHelpers.callStaticMethod(RobotHelpers.findClass(KeyConst.C_SETTING_CC, classLoader), KeyConst.M_SETTING_CC_GET);
        String getDBPath = (String) RobotHelpers.callMethod(ISetting, KeyConst.M_SETTING_GETDBPATH, dbName);
        MyLog.debug(TAG, " getDBPath =" + getDBPath);
        return getDBPath;
    }

    /**
     * 消息相关的 数据库表
     *
     * @return 地址
     */
    public String getInfoDBPath(ClassLoader classLoader) {
        return getDBPath(classLoader, InfoDB);
    }

    /**
     * 会话相关数据地址
     *
     * @return 地址
     */
    public String getSessionDBPath(ClassLoader classLoader) {
        return getDBPath(classLoader, SessionDB);
    }

    /**
     * 联系人 数据相关 的
     *
     * @return 地址
     */
    public String getContactDBPath(ClassLoader classLoader) {
        return getDBPath(classLoader, ContactDB);
    }

    // AppObserverService.getService().execSql

    /**
     * 查询数据 必须主线程执行
     *
     * @param sql
     * @param tablePath
     * @param execSqlCallback
     */
    public void execSql(ClassLoader classLoader, String sql, String tablePath, IExecSqlCallback execSqlCallback) {
        Object server = RobotHelpers.callStaticMethod(RobotHelpers.findClass(KeyConst.C_AppObserverService, classLoader), KeyConst.M_AppObserverService_getService);
        MyLog.debug(TAG, " sql " + sql);
        RobotHelpers.callMethod(server, KeyConst.M_AppObserverService_execSql, sql, tablePath, ProxyUtil.GetProxyInstance(KeyConst.I_IExecSqlCallback, new ProxyUtil.ProxyCallBack() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                if (KeyConst.M_IExecSqlCallback_onResult.equals(method.getName())) {
                    Object req = objects[0];
                    Object resp = objects[1];
                    //MyLog.debug(TAG, "onResult " + JSON.toJSONString(req) + "\n" + JSON.toJSONString(resp),true);
                    doResult(resp, execSqlCallback);
                } else if (KeyConst.M_IExecSqlCallback_toString.equals(method.getName())) {
                    return "fas$54";
                }
                return null;
            }
        }));
    }

    /**
     * 回调运行结果
     *
     * @param resp
     * @param execSqlCallback
     */
    private void doResult(Object resp, IExecSqlCallback execSqlCallback) {
        String errMsg = (String) RobotHelpers.getObjectField(resp, KeyConst.F_IExecSqlCallback_StorageDBResponse_errMsg);
        long execTime = RobotHelpers.getLongField(resp, KeyConst.F_IExecSqlCallback_StorageDBResponse_execTime);
        Object rows = RobotHelpers.getObjectField(resp, KeyConst.F_IExecSqlCallback_StorageDBResponse_rows);

        List data = new ArrayList();
        if (rows != null) {
            for (int m = 0; m < Array.getLength(rows); m++) {
                Object row = Array.get(rows, m);
                Object columns = RobotHelpers.getObjectField(row, KeyConst.F_IExecSqlCallback_StorageDBResponse_rows_columns);
                Object values = RobotHelpers.getObjectField(row, KeyConst.F_IExecSqlCallback_StorageDBResponse_rows_values);
                Map<String, Object> map = new HashMap<>();
                if (values != null) {
                    for (int i = 0; i < Array.getLength(values); i++) {
                        String column = (String) Array.get(columns, i);
                        String value = (String) Array.get(values, i);
                        map.put(column, value);
                    }
                }
                data.add(map);
            }
        }

        if (execSqlCallback != null) {
            try {
                execSqlCallback.onResult(errMsg, execTime, data);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public interface IExecSqlCallback {
        public void onResult(String errMsg, long execTime, List<Map<String, String>> resultMap) throws UnsupportedEncodingException;
    }


    public void Test(ClassLoader classLoader) {
        String sql = "select * from sqlite_master where type ='table';";

        execSql(classLoader, sql, getInfoDBPath(classLoader), new IExecSqlCallback() {
            @Override
            public void onResult(String errMsg, long execTime, List<Map<String, String>> resultMap) {

                MyLog.debug(TAG, "info db show tables execSql errMsg=" + errMsg + " execTime=" + execTime + " \n" + resultMap);
            }
        });
        execSql(classLoader, sql, getContactDBPath(classLoader), new IExecSqlCallback() {
            @Override
            public void onResult(String errMsg, long execTime, List<Map<String, String>> resultMap) {
                MyLog.debug(TAG, "Contact db show tables execSql errMsg=" + errMsg + " execTime=" + execTime + " \n" + resultMap);
            }
        });
        execSql(classLoader, sql, getSessionDBPath(classLoader), (errMsg, execTime, resultMap) -> MyLog.debug(TAG, "Session db show tables execSql errMsg=" + errMsg + " execTime=" + execTime + " \n" + resultMap));
    }


}
