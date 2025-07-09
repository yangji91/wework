package com.robot.netty.util;

import android.util.Log;

import java.io.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Compress helper utilities.
 * Migrated from couchbase java driver.
 *
 * @author Xiaohai Zhang
 * @version 20151109
 * @since Nov 9, 2015
 */
public class CompressUtils {
  private static final String TAG = CompressUtils.class.getSimpleName();

  private static final int ZIP_COMPRESS_RATIO = 8;

  public static byte[] compress(byte[] in) throws Exception {
    return gzipCompress(in);
  }

  public static byte[] decompress(byte[] in) {
    return gzipDecompress(in);
  }

  public static byte[] gzipCompress(byte[] in){
    if (in == null) {
      throw new NullPointerException("Can't compress null");
    }
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    GZIPOutputStream gz = null;
    try {
      gz = new GZIPOutputStream(bos);
      gz.write(in);
      gz.flush();
    } catch (IOException ex) {
      Log.e(TAG,ex.getMessage(),ex);
    } finally {
      if(gz != null){
        try {
          gz.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if(bos != null){
        try {
          bos.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return bos.toByteArray();
  }

  public static void deflateGZIP(InputStream in, OutputStream out) throws IOException {
    GZIPInputStream gis = new GZIPInputStream(in);
    byte[] buf = new byte[8192];
    int r;
    while ((r = gis.read(buf)) > 0) {
      out.write(buf, 0, r);
    }
  }

  public static byte[] gzipDecompress(byte[] in) {
    ByteArrayOutputStream bos = null;
    if (in != null) {
      ByteArrayInputStream bis = new ByteArrayInputStream(in);
      bos = new ByteArrayOutputStream();
      GZIPInputStream gis = null;
      try {
        gis = new GZIPInputStream(bis);
        byte[] buf = new byte[8192];
        int r;
        while ((r = gis.read(buf)) > 0) {
          bos.write(buf, 0, r);
        }
      } catch (IOException ex) {
        Log.e(TAG,"Could not decompress data.", ex);
        bos = null;
      } finally {
        if(bis != null){
          try {
            bis.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        if(gis != null){
          try {
            gis.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return bos == null ? null : bos.toByteArray();
  }

  public static byte[] zipCompress(byte[] in) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream(in.length);
    DeflaterOutputStream os = new DeflaterOutputStream(baos);
    try {
      os.write(in);
      os.finish();
      try {
        os.close();
      } catch (IOException e) {
        Log.e(TAG,"Close DeflaterOutputStream error", e);
      }
    } catch (IOException e) {
      throw new RuntimeException("IO exception compressing data", e);
    } finally {
      try {
        baos.close();
      } catch (IOException e) {
        Log.e(TAG,"Close ByteArrayOutputStream error", e);
      }
    }
    return baos.toByteArray();
  }

  public static byte[] zipDecompress(byte[] in) {
    int size = in.length * ZIP_COMPRESS_RATIO;
    ByteArrayInputStream bais = new ByteArrayInputStream(in);
    InflaterInputStream is = new InflaterInputStream(bais);
    ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
    try {
      byte[] uncompressMessage = new byte[size];
      while (true) {
        int len = is.read(uncompressMessage);
        if (len <= 0) {
          break;
        }
        baos.write(uncompressMessage, 0, len);
      }
      baos.flush();
      return baos.toByteArray();

    } catch (IOException e) {
      Log.e(TAG, e.getMessage(),e);
      //baos = null;
    } finally {
      try {
        is.close();
      } catch (IOException e) {
        Log.e(TAG,e.getMessage(),e);
      }
      try {
        bais.close();
      } catch (IOException e) {
        Log.e(TAG,e.getMessage());
      }
      try {
        baos.close();
      } catch (IOException e) {
        Log.e(TAG,e.getMessage());
      }
    }
    return baos.toByteArray();
  }
}
