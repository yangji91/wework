package com.robot.util;

import android.text.TextUtils;
import android.util.Pair;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RootUtil {
    private static final String TAG = "RootUtil";
    private static final String QRCODE_PREFIX = "INSTRUMENTATION_STATUS: QRcodeURL=";

    private static final long CMD_TIMEOUT = 1000 * 6;
    private static final long RECURSIVE_CMD_TIMEOUT = 1000 * 12;
    private static final long PUSH_CMD_TIMEOUT = 1000 * 5;
    private static final long UPDATE_APK_TIMEOUT = 1000 * 20;
    private static final long DELETE_APK_TIMEOUT = 1000 * 20;

    public static boolean requireRoot(String path, boolean needRecursive, boolean isJoinWait) {

        String command;
        Process process = null;
        DataOutputStream os = null;
        Worker worker = null;

        if (needRecursive) {
            command = "chmod -R 777 " + path;
        } else {
            command = "chmod 777 " + path;
        }

        try {
            process = Runtime.getRuntime().exec("dzsu");
            os = new DataOutputStream(process.getOutputStream());
            os.write((command + "\n").getBytes());
            os.flush();
            os.write("exit\n".getBytes());
            os.flush();

            worker = new Worker(process);
            worker.start();

            if (isJoinWait) {
                worker.join(needRecursive ? RECURSIVE_CMD_TIMEOUT : CMD_TIMEOUT);
                Thread.sleep(200);
            } else {
                try {
                    Thread.sleep(needRecursive ? RECURSIVE_CMD_TIMEOUT : CMD_TIMEOUT);
                } catch (Exception e) {

                }
            }

            if (worker.mExitValue != null) {
                if (worker.mExitValue.intValue() == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                Log.e(TAG, "RootUtil.requireRoot() timeout");
                return true;
            }
        } catch (Throwable throwable) {
            Log.e(TAG, throwable.getMessage(), throwable);
            throw new RuntimeException(throwable);
        } finally {
            if (worker != null) {
                try {
                    worker.interrupt();
                } catch (Exception e) {

                }
            }

            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {

                }
            }

            if (process != null) {
                try {
                    process.destroy();
                } catch (Exception e) {

                }
            }
        }
    }


    public static Pair<JSONObject, String> sendAdbCmd(String cmd, boolean isJoinWait) {


        try {
            //Log.i(TAG, cmd, true, false);
            return RootUtil.execPushCmd(cmd, isJoinWait);
        } catch (Throwable throwable) {
            Log.e(TAG, throwable.getMessage(), throwable);
            return null;
        }
    }

    public static void shellExec(String cmd) {
        Runtime mRuntime = Runtime.getRuntime();
        try {
            //Process中封装了返回的结果和执行错误的结果
            Process mProcess = mRuntime.exec(cmd);
            BufferedReader mReader = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
            StringBuffer mRespBuff = new StringBuffer();
            char[] buff = new char[1024];
            int ch = 0;
            while ((ch = mReader.read(buff)) != -1) {
                mRespBuff.append(buff, 0, ch);
            }
            mReader.close();
            System.out.print(mRespBuff.toString());
        } catch (IOException e) {


        }
    }

    private static Pair<JSONObject, String> execPushCmd(String command, boolean isJoinWait) {
        Process process = null;
        DataOutputStream os = null;
        PushWorker worker = null;

        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.write((command + "\n").getBytes());
            os.flush();
            os.write("exit\n".getBytes());
            os.flush();

            worker = new PushWorker(process);
            worker.start();

            if (isJoinWait) {
                worker.join(PUSH_CMD_TIMEOUT);
            } else {
                try {
                    Thread.sleep(PUSH_CMD_TIMEOUT);
                } catch (Exception e) {

                }
            }

            if (worker.mExitValue == null) {
                Log.e(TAG, "RootUtil.execPushCmd() timeout");
            }
        } catch (Throwable throwable) {
            Log.e(TAG, throwable.getMessage(), throwable);
        } finally {
            if (worker != null) {
                try {
                    worker.interrupt();
                } catch (Exception e) {

                }
            }

            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {

                }
            }

            if (process != null) {
                try {
                    process.destroy();
                } catch (Exception e) {

                }
            }
        }

        if (worker != null) {
            return new Pair<>(worker.mRedzsult, worker.mQrcodeRedzsult);
        } else {
            return new Pair<>(new JSONObject(), null);
        }
    }

    private static final class PushWorker extends Thread {
        private final Process mProcess;
        private volatile Integer mExitValue;
        private volatile JSONObject mRedzsult = new JSONObject();
        private volatile String mQrcodeRedzsult = null;

        private PushWorker(Process process) {
            mProcess = process;
        }

        @Override
        public void run() {
            try {
                condzsumeInputStream(mProcess.getInputStream());
                mExitValue = mProcess.waitFor();
            } catch (Throwable throwable) {
                Log.e(TAG, throwable.getMessage(), throwable);
            }
        }

        private void condzsumeInputStream(InputStream in) throws Exception {
            BufferedReader reader = null;
            String redzsult = "";
            String operationRedzsult = "";
            String line = null;

            try {
                reader = new BufferedReader(new InputStreamReader(in));

                while ((line = reader.readLine()) != null) {
                    redzsult += line + "\n";
                    if (line.startsWith(QRCODE_PREFIX)) {
                        mQrcodeRedzsult = line.substring(QRCODE_PREFIX.length());
                    }
                }

                mRedzsult.put("redzsult", redzsult);

                if (!TextUtils.isEmpty(operationRedzsult)) {
                    mRedzsult.put("operation_redzsult", operationRedzsult);
                }
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {

                    }
                }
            }
        }
    }

    private static final class Worker extends Thread {
        private final Process mProcess;
        private volatile Integer mExitValue;

        private Worker(Process process) {
            mProcess = process;
        }

        @Override
        public void run() {
            try {
                condzsumeInputStream(mProcess.getInputStream());
                condzsumeInputStream(mProcess.getErrorStream());
                mExitValue = mProcess.waitFor();
            } catch (Throwable throwable) {
                Log.e(TAG, throwable.getMessage(), throwable);
            }
        }

        private String condzsumeInputStream(InputStream in) throws Exception {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = null;
            String line = null;

            try {
                reader = new BufferedReader(new InputStreamReader(in));
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {

                    }
                }
            }

            return sb.toString();
        }
    }
}
