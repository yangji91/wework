package com.robot.hook;


import com.robot.controller.db.DBController;
import com.robot.hook.base.HookBaseMethod;
import com.robot.hook.base.IHookCallBack;
import com.robot.util.MyLog;

import java.util.List;
import java.util.Map;

import com.robot.robothook.LoadPackageParam;

/***
 *@author 
 *@date 2021/7/30
 *@description sql 语句测试
 ****/
public class HookExecSqlMethod extends HookBaseMethod<String> {
    @Override
    public void onHookInfo(Class clazz, LoadPackageParam loadPackageParam, IHookCallBack callBack) {

    }

    @Override
    public boolean onInvokeMethod(Class classDb, LoadPackageParam loadPackageParam, IHookCallBack<String> paramCall) {
        try {
            String message = paramCall.getParams();
            String[] split = message.split(";");
            String sql = split[1];
            String databaseName = "Info.db";
            if (split.length > 2) {
                databaseName = split[2];
            }
            DBController.getInstance().execSql(loadPackageParam.classLoader, sql, DBController.getInstance().getDBPath(loadPackageParam.classLoader, databaseName), new DBController.IExecSqlCallback() {
                @Override
                public void onResult(String errMsg, long execTime, List<Map<String, String>> resultMap) {
                    MyLog.debug(TAG, " errMsg =" + errMsg + " execTime =" + execTime);
                    for (int i = 0; i < resultMap.size(); i++) {
                        Map map = resultMap.get(i);
                        MyLog.debug(TAG, " map = " + map);
                    }
                }
            });
        } catch (Exception e) {

        }

        return false;
    }
}
