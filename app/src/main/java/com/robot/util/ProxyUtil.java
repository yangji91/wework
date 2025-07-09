package com.robot.util;

import com.robot.common.Global;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


import com.robot.robothook.RobotHelpers;

/***
 *@author 
 *@date 2021/6/17
 *@description 代理类实例
 ****/
public class ProxyUtil {

    private ProxyUtil() {
    }

    private static Object GetProxyInstance(ClassLoader classLoader, Class interfaceClass, ProxyCallBack proxyCallBack) {
        return Proxy.newProxyInstance(classLoader, new Class[]{interfaceClass}, proxyCallBack);
    }

    public static Object GetProxyInstance(Class interfaceClass, ProxyCallBack proxyCallBack) {
        return GetProxyInstance(Global.loadPackageParam.classLoader, interfaceClass, proxyCallBack);
    }

    /**
     * @param interfaceClass 报名.类名
     * @param proxyCallBack  //回调
     * @return
     */
    public static Object GetProxyInstance(String interfaceClass, ProxyCallBack proxyCallBack) {
        return GetProxyInstance(RobotHelpers.findClass(interfaceClass, Global.loadPackageParam.classLoader), proxyCallBack);
    }
    /**
     * @param interfaceClass 报名.类名
     * @param proxyCallBack  //回调
     * @return
     */
    public static Object GetProxyInstance(ClassLoader loader,String interfaceClass, ProxyCallBack proxyCallBack) {
        return GetProxyInstance(RobotHelpers.findClass(interfaceClass, loader), proxyCallBack);
    }

    /***
     * 使用实现的抽象类 ProxyResultCallBack
     *
     */
    public interface ProxyCallBack extends InvocationHandler {
        @Override
        Object invoke(Object o, Method method, Object[] objects) throws Throwable;
    }

    /**
     *
     */
    public static abstract class ProxyStringResultCallBack implements ProxyCallBack {
        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            if ("onResult".equals(method.getName()) && objects.length == 2) {
                int i = 1;
                String str = null;
                try {
                    i = (int) objects[0];
                    str = (String) objects[1];
                } catch (Exception e) {
                    MyLog.error("ProxyResultCallBack 回调参数不匹配 ", e);
                }
                onResult(i, str);
            } else if ("toString".equals(method.getName())) {
                return "com.tencent.wework.friends.controller.FriendAddVerifyActivity$3$1@8124017";
            }
            return null;
        }

        public abstract void onResult(int i, String str);
    }

    /**
     *
     */
    public static abstract class ProxyObjectResultCallBack implements ProxyCallBack {
        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            if ("onResult".equals(method.getName())) {
                int i = 1;
                Object object = null;
                try {
                    i = (int) objects[0];
                    object = (Object) objects[1];
                } catch (Exception e) {
                    MyLog.error("ProxyResultCallBack 回调参数不匹配 ", e);
                }
                onResult(i, object);
            } else if ("toString".equals(method.getName())) {
                return "com.tencent.wework.friends.controller.FriendAddVerifyActivity$3$1@8124017";
            }
            return null;
        }

        public abstract void onResult(int i, Object object);
    }

    /**
     *
     */
    public static abstract class ProxyResultCallBack implements ProxyCallBack {
        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            if ("onResult".equals(method.getName()) && objects.length == 3) {
                int i = 1;
                String str = null;
                byte[] bytes = null;
                try {
                    i = (int) objects[0];
                    str = (String) objects[1];
                    bytes = (byte[]) objects[2];
                } catch (Exception e) {
                    MyLog.error("ProxyResultCallBack 回调参数不匹配 ", e);
                }
                onResult(i, str, bytes);
            } else if ("toString".equals(method.getName())) {
                return "com.tencent.wework.moments.controller.MomentsComposeActivity$e$1$1@5b1e110";
            }
            return null;
        }

        public abstract void onResult(int i, String str, byte[] bArr);
    }

    /**
     *
     */
    public static abstract class ProxyCommonResultDataCallBack implements ProxyCallBack {
        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            if ("onResult".equals(method.getName()) && objects.length == 2) {
                int i = 1;
                byte[] bytes = null;
                try {
                    i = (int) objects[0];
                    bytes = (byte[]) objects[1];
                } catch (Exception e) {
                    MyLog.error("ProxyResultCallBack 回调参数不匹配 ", e);
                }
                onResult(i, bytes);
            } else if ("toString".equals(method.getName())) {
                return "com.tencent.wework.moments.controller.MomentsComposeActivity$e$1$1@5b1e110";
            }
            return null;
        }

        public abstract void onResult(int i, byte[] bArr);
    }

    /**
     *
     */
    public static abstract class ProxyResultImplCallBack implements ProxyCallBack {
        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            if ("onResult".equals(method.getName())) {
                onResult(objects);
            } else if ("toString".equals(method.getName())) {
                return "com.tencent.wework.moments.controller.MomentsComposeActivity$e$1$1@5b1e110";
            }
            return null;
        }

        public void onResult(Object[] bArr) {
        }

        ;

        public void onResult(int code, Object[] bArr) {
        }

        ;
    }

    /**
     * ICommonStringLongCallback
     */
    public static abstract class ProxyResultImplStringLongCallback implements ProxyCallBack {
        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            if ("onResult".equals(method.getName()) && objects.length == 3) {
                int i = 1;
                String str = null;
                long l = 0L;
                try {
                    i = (int) objects[0];
                    str = (String) objects[1];
                    l = (long) objects[2];
                } catch (Exception e) {
                    RobotHelpers.log("ProxyUserResultCallBack 回调参数不匹配 " + e);
                }
                onResult(i, str, l);
            } else if ("toString".equals(method.getName())) {
                return "com.tencent.wework.moments.controller.MomentsComposeActivity$e$1$1@5b1e110";
            }
            return null;
        }

        public abstract void onResult(int code, String message, long l);
    }

    /**
     *
     */
    public static abstract class ProxyUserResultCallBack implements ProxyCallBack {
        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            if ("onResult".equals(method.getName()) && objects.length == 2) {
                int i = 1;
                String str = null;
                Object[] users = null;
                try {
                    i = (int) objects[0];
                    users = (Object[]) objects[1];
                } catch (Exception e) {
                    RobotHelpers.log("ProxyUserResultCallBack 回调参数不匹配 " + e);
                }
                onResult(i, users);
            } else if ("toString".equals(method.getName())) {
                return "com.tencent.wework.moments.controller.MomentsComposeActivity$e$1$1@5b1e110";
            }
            return null;
        }

        public abstract void onResult(int i, Object[] userObj);
    }
}
