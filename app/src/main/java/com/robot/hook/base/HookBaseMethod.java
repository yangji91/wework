package com.robot.hook.base;

import com.robot.robothook.RobotMethodParam;
import com.robot.robothook.LoadPackageParam;

public abstract class HookBaseMethod<T>{
    protected final String TAG = getClass().getSimpleName();
    protected final String XPOSED_PKGNAME = "de.robv.android.xposed.installer";
    protected final String XPOSED_PKGNAME_START = "de.robv.android.xposed";
    protected final String X_INSTALLER = "com.pyler.xinstaller";

    protected final String XPOSE_CLASS_RENAME_RESULT = "com.info.cc";
    public static final String WECHAT_DATABASE_PACKAGE_NAME = "com.tencent.wcdb.database.SQLiteDatabase";

    /***
     * hookinfo被动调用
     * @param clazz
     * @param loadPackageParam
     * @param callBack
     */
    abstract public void onHookInfo(final Class clazz, final LoadPackageParam loadPackageParam, IHookCallBack<T> callBack);

    /**
     * invoke主动调用
     * @param classDb
     * @param loadPackageParam
     * @param paramCall
     * @return
     */
    abstract public boolean onInvokeMethod(final Class classDb, final LoadPackageParam loadPackageParam, IHookCallBack<T> paramCall);

    public String getOp(){
        return null;
    }


    protected void printHookLog(RobotMethodParam param, String classFullName, String methodName) {
        if (param.args == null || param.args.length == 0) {
            return;
        }

        StringBuffer sb = new StringBuffer();
        sb.append(classFullName).append(".").append(methodName).append("(");
        if (null != param.args && param.args.length > 0) {
            for (int i = 0; i < param.args.length; i++) {
                Object obj = param.args[i];
                sb.append(obj);
                if (i != param.args.length - 1) {
                    sb.append(", ");
                } else if (param.args[i] instanceof Object[]) {
                    sb.append(": {");
                    for (Object o : (Object[]) param.args[i]) {
                        sb.append(" " + o);
                    }
                    sb.append("}");
                }
            }
        }
        sb.append(") RESULT=").append(param.getResult()+"");
        //RobotHelpers.log(sb.toString());
    }

    protected StringBuilder buildStrBuilder(RobotMethodParam param){
        StringBuilder builder = new StringBuilder();
        if(param != null){
            Object[] objs = param.args;
            if(objs != null){
                for(Object obj : objs){
                    if(obj instanceof Object[]){
                        Object[] arrays = (Object[]) obj;
                        for(Object o : arrays){
                            builder.append("obj1->" + o + "\t");
                        }
                    }else{
                        builder.append("obj->" + obj + "\t");
                    }
                }
                builder.append("thisObj->" + param.thisObject+"\t");
                builder.append("result->" + param.getResult());
            }
        }
        return builder;
    }
}
