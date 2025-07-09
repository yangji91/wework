package com.robot.util;

import com.robot.common.Global;
import com.robot.hook.KeyConst;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Comparator;

import com.robot.robothook.RobotHelpers;

/**
 * 企微 消息 与字节数组之间的转换工具
 */
public class CodedOutputByteBufferNanoUtil {

    /**
     *
     * @param classLoader
     * @param fileMessage 消息类型 例如 fileMessage LocationMessage VideoMessage
     * @return
     */
    public static   byte[] messageToBytes(ClassLoader classLoader,Object fileMessage){
        int length = (int) RobotHelpers.callMethod(fileMessage,KeyConst.M_FileMessage_computeSerializedSize);
        byte[] bytes =new byte[length];
        Class CodedOutputByteBufferNano=   RobotHelpers.findClass(KeyConst.C_CodedOutputByteBufferNano,classLoader);
        Object newInstance = RobotHelpers.callStaticMethod(CodedOutputByteBufferNano, KeyConst.M_CodedOutputByteBufferNano_newInstance, bytes);
        RobotHelpers.callMethod(fileMessage,KeyConst.M_FileMessage_writeTo,newInstance);
        ByteBuffer byteBuffer = (ByteBuffer) RobotHelpers.getObjectField(newInstance,KeyConst.F_CodedOutputByteBufferNano_buffer);
        return byteBuffer.array();

    }
    /**
     *
     * @param messageClass
     * @param
     * @return 消息类型 例如 fileMessage LocationMessage VideoMessage
     */
//    public static  Object mergeFrom(Class messageClass,byte[] bytes){
//
//        Class CodedOutputByteBufferNano=   RobotHelpers.findClass(KeyConst.C_CodedInputByteBufferNano,messageClass.getClassLoader());
//        Object newInstance = RobotHelpers.callStaticMethod(CodedOutputByteBufferNano, KeyConst.M_CodedInputByteBufferNano_newInstance, bytes);
//        Object instance = RobotHelpers.newInstance(messageClass);
//        RobotHelpers.callMethod(instance,"mergeFrom",newInstance);
//       return  instance;
//
//    }
    /**
     *
     *
     * @param bytes content 字节数据
     * @param messageClass 要转化的目标类型
     * @return
     */
    public static   Object bytesToMessage( byte[] bytes,Class messageClass){

        Object objMessage = RobotHelpers.callStaticMethod(messageClass,KeyConst.M_parseFrom,bytes);
        return objMessage;

    }
    public static void main(String[] args) {
        Integer[] list = new Integer[20]  ;
        for (int i = 0; i < 10; i++) {
            list[i]=i;
            list [10+i]=(2+i);
        }
        Arrays.sort(list, new Comparator<Integer>() {
            public int compare(Integer i1, Integer i2) {
                if (i1 < i2) {
                    return -1;
                } else if (i1 == i2) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        System.out.println(Arrays.toString(list ));
    }

    public static byte[] messageToBytes(Object MessageNano) {
        Class<?> aClass = RobotHelpers.findClass(KeyConst.C_MESSAGE_NANO, Global.loadPackageParam.classLoader);
        return (byte[]) RobotHelpers.callStaticMethod(aClass,KeyConst.M_MESSAGE_NANO_toByteArray,new Class[]{aClass},MessageNano);
    }
}
