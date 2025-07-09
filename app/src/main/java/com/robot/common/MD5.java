package com.robot.common;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD5 {
  static char[] arrayOfChar1 = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
  public static final String getMessageDigest(byte[] paramArrayOfByte){

    try{
      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
      localMessageDigest.update(paramArrayOfByte);
      byte[] arrayOfByte = localMessageDigest.digest();
      int i = arrayOfByte.length;
      char[] arrayOfChar2 = new char[i * 2];
      int j = 0;
      int k = 0;
      while (true){
        if (j >= i)
          return new String(arrayOfChar2);
        int m = arrayOfByte[j];
        int n = k + 1;
        arrayOfChar2[k] = arrayOfChar1[(0xF & m >>> 4)];
        k = n + 1;
        arrayOfChar2[n] = arrayOfChar1[(m & 0xF)];
        j++;
      }
    }
    catch (Exception localException){
    }
    return "";
  }
  
  public static final byte[] getRawDigest(byte[] paramArrayOfByte){
    try{
      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
      localMessageDigest.update(paramArrayOfByte);
      byte[] arrayOfByte = localMessageDigest.digest();
      return arrayOfByte;
    }
    catch (Exception localException){
    	
    }
    return null;
  }

    /**
     * 获取文件的MD5
     * @param path
     * @return
     */
  public static String getFileMD5(String path) {
    StringBuffer sb = new StringBuffer();
            try {
                 byte[] buffer = new byte[8192];
                int len = 0;
                MessageDigest md = MessageDigest.getInstance("MD5");
                   File f = new File(path);
                 FileInputStream fis = new FileInputStream(f);
                 while ((len = fis.read(buffer)) != -1) {
                        md.update(buffer, 0, len);
                    }
                    fis.close();
                  byte[] arrayOfByte = md.digest();
              int i = arrayOfByte.length;
              char[] arrayOfChar2 = new char[i * 2];
              int j = 0;
              int k = 0;
              while (true){
                if (j >= i)
                  return new String(arrayOfChar2);
                int m = arrayOfByte[j];
                int n = k + 1;
                arrayOfChar2[k] = arrayOfChar1[(0xF & m >>> 4)];
                k = n + 1;
                arrayOfChar2[n] = arrayOfChar1[(m & 0xF)];
                j++;
              }
               } catch (NoSuchAlgorithmException e) {
                   e.printStackTrace();
             } catch (IOException e) {
                   e.printStackTrace();
             }
           return  "";
        }
  
  public static final String toString(String str){
	  String result = null;
	  if(!TextUtils.isEmpty(str)){
		  result = getMessageDigest(str.getBytes());
	  }
	  return result;
  }
}