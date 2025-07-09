package com.robot.com;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.robot.com.activity.base.BaseActivity;
import com.robot.com.activity.dialog.DialogConfirm;
import com.robot.com.activity.dialog.DialogModelChanged;
import com.robot.com.component.ItemView;
import com.robot.com.component.MyHeader;
import com.robot.com.component.listener.IItemListener;
import com.robot.com.file.SharePreModel;
import com.robot.com.file.SharePreSn;
import com.robot.com.file.SharePreWecomVersion;
import com.robot.common.Global;
import com.robot.common.MConfiger;
import com.robot.entity.PhoneLocEnum;
import com.robot.entity.ResEntity;
import com.robot.entity.RobotModelEnum;
import com.robot.entity.UserEntity;
import com.robot.http.HttpProtocalManager;
import com.robot.http.ICallBack;
import com.robot.http.entity.rsp.RspChangeModelEntity;
//import com.robot.util.DetectorUtils;
import com.robot.util.DeviceUtil;
import com.robot.util.FileUtil;
import com.robot.util.MyLog;
import com.robot.util.NetInfoUtil;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    private String[] permissionArray = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WAKE_LOCK,};

    private final int REQ_CODE_PERMISSION = 0x1;

    private final int FLAG_INIT_MODEL = 0x1;
    private final int FLAG_TOAST = 0x2;
    private final int FLAG_INIT_FILE_SIZE = 0x3;

    private ItemView itemViewChangedModel;
    private TextView mTxtSdCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.robot.com.R.layout.activity_main);
        initLayout();
        List<String> list = checkPermission(permissionArray);
        if (list.size() > 0) {
            requestPermission(list, REQ_CODE_PERMISSION);
        } else {
//            String sn = Global.getDeviceSn();
//            if (!TextUtils.isEmpty(sn)) {
//                SharePreSn shareSn = new SharePreSn();
//                String snShare = shareSn.loadData();
//                if (snShare == null || snShare.isEmpty() || !Objects.equals(sn, snShare)) {
//                    shareSn.saveInfo(sn);
//                }
//            } else {
//                showToast("SN码保存失败~，请检查获取手机信息权限是否打开");
//            }
            String packageWecomVersion = Global.getPackageWecomVersion();
            if (!TextUtils.isEmpty(packageWecomVersion)) {
                SharePreWecomVersion sharePreWecomVersion = new SharePreWecomVersion();
                String wecomVersion = sharePreWecomVersion.loadData();
                if (wecomVersion == null || wecomVersion.isEmpty() || !Objects.equals(packageWecomVersion, wecomVersion)) {
                    sharePreWecomVersion.saveInfo(Global.getPackageWecomVersion());
                }
            }
//            delayTask();
            startComWorkService();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        delayTask();
//        Global.postRunnableDownLoadImg(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    ArrayList<Object> detector = DetectorUtils.detector(MainActivity.this);
//                    String detectStatus = (String) detector.get(0);
//                    ShareDetectSafeStatus shareDetectSafeStatus = new ShareDetectSafeStatus();
//                    shareDetectSafeStatus.saveInfo(detectStatus);
//                    String detectDesc = (String) detector.get(1);
//                    String detectDetail = (String) detector.get(1);
//                    StatsHelper.event("msgReport", "detector", detectStatus, detectDesc, detectDetail);
//                } catch (Throwable e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        ComWorkServiceManager.getInstance().unregisterService(getApplicationContext());
        super.onDestroy();
    }

    private void startComWorkService() {
        Intent serviceIntent = new Intent(getApplicationContext(), ComWorkService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                ComWorkServiceManager.getInstance().registerService(getApplicationContext());
            }
        }, 1000);
    }

    private void delayTask() {
        Global.postRunnable(new Runnable() {
            @Override
            public void run() {
//                String baiduCode = FileUtil.getBaiduPhoneCode();
//                String huaweiCode = FileUtil.getHuaWeiYunCode();
//                if (!TextUtils.isEmpty(baiduCode)) {
//                    MConfiger.phoneLocEnum = PhoneLocEnum.BAIDU;
//                } else if (!TextUtils.isEmpty(huaweiCode)) {
//                    MConfiger.phoneLocEnum = PhoneLocEnum.HUAWEI;
//                } else {
                MConfiger.phoneLocEnum = PhoneLocEnum.LOCAL;
//                }
                SharePreModel mSharePreModel = new SharePreModel();
                String str = mSharePreModel.loadData();
                if (!TextUtils.isEmpty(str) && TextUtils.isDigitsOnly(str)) {
                    int type = Integer.valueOf(str).intValue();
                    RobotModelEnum robotModelEnum = RobotModelEnum.getEnumByType(type);
                    if (robotModelEnum != null) {
                        String desc = robotModelEnum.getDesc();
                        Message msg = Message.obtain();
                        msg.what = FLAG_INIT_MODEL;
                        msg.obj = desc;
                        sendMsg(msg);
                    }
                } else {
                    //不填写，默认是机器人模式
                    Message msg = Message.obtain();
                    msg.what = FLAG_INIT_MODEL;
                    msg.obj = RobotModelEnum.MODEL_ROBOT.getDesc();
                    sendMsg(msg);
                }
                File sdcard_filedir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);//得到sdcard的目录作为一个文件对象
                long usableSpace = sdcard_filedir.getUsableSpace();//获取文件目录对象剩余空间
                long totalSpace = sdcard_filedir.getTotalSpace();
                long sourceSize = FileUtil.FileClear.getFolderSize(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath() + "/Tencent"));
                long logSize = SDCardCleanUtil.getWeWorkLogSize();
                String totalSpaceStr = FileUtil.getSizeStr(totalSpace);
                String usableSpaceStr = FileUtil.getSizeStr(usableSpace);
//                long storageFileSize = FileUtil.FileClear.getStorageFileSize(MainActivity.this, true);
//                String hasUserdSpaceStr = FileUtil.getSizeStr(storageFileSize);
                String logSizeStr = FileUtil.getSizeStr(logSize);
                String sourceSizeStr = FileUtil.getSizeStr(sourceSize);
                String txt = "\t总共存储空间:" + totalSpaceStr //"SD卡空间:" +
                        + "\n\t剩余空间:" + usableSpaceStr
//                        + "\n\t已经使用外部存储:" + hasUserdSpaceStr
                        + "\n\t日志缓存:" + logSizeStr + "\n\t企微文件缓存:" + sourceSizeStr;

                Message msg = Message.obtain();
                msg.what = FLAG_INIT_FILE_SIZE;
                msg.obj = txt;
                sendMsg(msg);
            }
        });
    }

    @Override
    protected void handleMsg(Message msg) {
        int what = msg.what;
        switch (what) {
            case FLAG_INIT_MODEL:
                String tips = (String) msg.obj;
                itemViewChangedModel.setContents(tips);
                break;
            case FLAG_TOAST:
                tips = (String) msg.obj;
                showToast(tips);
                hideDialogLoading();
                break;
            case FLAG_INIT_FILE_SIZE:
                String str = (String) msg.obj;
                this.mTxtSdCard.setText(str);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_CODE_PERMISSION) {
            if (checkAllPer(permissionArray)) {

            } else {
                showDialogCfg();
            }
//            String sn = Global.getDeviceSn();
//            if (!TextUtils.isEmpty(sn)) {
//                SharePreSn mSharePre = new SharePreSn();
//                mSharePre.saveInfo(Global.getDeviceSn());
//                showToast("SN码保存成功~");
//            } else {
//                showToast("SN码保存失败~，请检查获取手机信息权限是否打开");
//            }
            startComWorkService();
        }
    }

    protected void showDialogCfg() {
        hideDialogCfg();
        this.mDialogCfg = new DialogConfirm(this, R.style.MyDialogBg);
        this.mDialogCfg.show();
        this.mDialogCfg.setCancelable(false);
        this.mDialogCfg.setCanceledOnTouchOutside(false);
        this.mDialogCfg.updateType(DialogConfirm.TYPE_PERMISSION);
        this.mDialogCfg.setListener(new IItemListener() {
            @Override
            public void onItemClick(View mView, int tag) {
                if (tag == DialogConfirm.BUTTON_TYPE_CANCLE) {
                    finish();
                } else if (tag == DialogConfirm.BUTTON_TYPE_OK) {
                    List<String> mList = checkPermission(permissionArray);
                    if (mList.size() > 0) {
                        requestPermission(mList, REQ_CODE_PERMISSION);
                    }
                }
            }

            @Override
            public void onItemClick(View mView, Object obj, int tag) {

            }
        });
    }

    private void initLayout() {
        MyHeader mHeader = this.findViewById(R.id.header);
        mHeader.updateType(MyHeader.TYPE_MAIN);

        View snTitleView = this.findViewById(R.id.snTitleView);
        EditText snEditView = this.findViewById(R.id.snEditView);
        View snSaveView = this.findViewById(R.id.snSaveView);
        snEditView.setText(Global.getDeviceSn());
        snTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sn = Global.getDeviceSn();
//                InputSnActivity.startInputSnActivity(MainActivity.this);
                ClipboardManager cm = (ClipboardManager) Global.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText("copySn", sn));

                String packageWecomVersion = Global.getPackageWecomVersion();
                if (!TextUtils.isEmpty(packageWecomVersion)) {
                    SharePreWecomVersion sharePreWecomVersion = new SharePreWecomVersion();
                    String wecomVersion = sharePreWecomVersion.loadData();
                    if (wecomVersion == null || wecomVersion.isEmpty() || !Objects.equals(packageWecomVersion, wecomVersion)) {
                        sharePreWecomVersion.saveInfo(Global.getPackageWecomVersion());
                    }
                }
                Global.showToast("设备标识: " + sn);
            }
        });
        snSaveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sn = snEditView.getText().toString().trim();
//                if (!TextUtils.isEmpty(sn)) {
                SharePreSn shareSn = new SharePreSn();
                String snShare = shareSn.loadData();
                if (snShare == null || snShare.isEmpty() || !Objects.equals(sn, snShare)) {
                    shareSn.saveInfo(sn);
                }
//                } else {
                showToast("SN码保存成功");
//                }
            }
        });

        ItemView itemWifi = this.findViewById(R.id.item_wifi);
        itemWifi.setTitle("测试地址");
        itemWifi.setItemListener(new IItemListener() {
            @Override
            public void onItemClick(View mView, int tag) {
                String wifiAddr = NetInfoUtil.getNetInfoEntity().ipAddr + ":" + ("8080") + "/index.html";
                ClipboardManager cm = (ClipboardManager) Global.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText("copy", wifiAddr));
                Global.showToast(wifiAddr);
            }

            @Override
            public void onItemClick(View mView, Object obj, int tag) {
            }
        });
    }


    private void clearFileImg() {
        Global.postRunnable(() -> {
            //
            String path = FileUtil.getComRobotPath();
            if (!TextUtils.isEmpty(path)) {
                File file = new File(path);
                File[] fArray = file.listFiles();
                if (fArray != null && fArray.length > 0) {
                    for (File f : fArray) {
                        if (f.isFile() && f.getAbsolutePath().endsWith(".png")) {
                            f.delete();
                            MyLog.debug(TAG, "[clearFileImg]" + "清理图片:" + f.getAbsolutePath());
                        }
                    }
                }
            }
            Message msg = Message.obtain();
            msg.what = FLAG_TOAST;
            msg.obj = "清理完成";
            sendMsg(msg);
        });
    }

    private void showDialogModelChanged() {
        hideDialogModelChanged();
        this.mDialogModelChanged = new DialogModelChanged(this, R.style.MyDialogBg);
        this.mDialogModelChanged.show();
        this.mDialogModelChanged.setItemListener(new IItemListener() {
            @Override
            public void onItemClick(View mView, int tag) {
                if (tag == DialogModelChanged.SRC_ROBOT) {
                    changeModel(RobotModelEnum.MODEL_ROBOT);
                } else if (tag == DialogModelChanged.SRC_PULL) {
                    changeModel(RobotModelEnum.MODEL_PULL);
                }
                hideDialogModelChanged();

            }
        });
    }

    private void changeModel(RobotModelEnum en) {
        showDialogLoading(null);
        Global.postRunnable(new Runnable() {
            @Override
            public void run() {
                UserEntity userEntity = FileUtil.loadUserLoginInfo();
                if (userEntity == null) {
                    Message msg = Message.obtain();
                    msg.what = FLAG_TOAST;
                    msg.obj = "请先登录";
                    sendMsg(msg);
                    return;
                }
                long remoteId = userEntity.remoteId;
                String deviceId = DeviceUtil.getAndroidID();
                HttpProtocalManager.getInstance().reqChangeModel(remoteId, en.getType(), deviceId, true, new ICallBack<RspChangeModelEntity>() {
                    @Override
                    public void onCall(ResEntity<RspChangeModelEntity> resEntity) {
                        if (resEntity.isSucc()) {
                            Message msg = Message.obtain();
                            msg.what = FLAG_TOAST;
                            msg.obj = "修改成功";
                            sendMsg(msg);
                            SharePreModel mSharePreModel = new SharePreModel();
                            mSharePreModel.saveInfo(en.getType() + "");
//                            delayTask();
                        } else {
                            Message msg = Message.obtain();
                            msg.what = FLAG_TOAST;
                            msg.obj = "修改失败";
                            sendMsg(msg);
                        }
                    }
                });
            }
        });
    }

    private void hideDialogModelChanged() {
        if (this.mDialogModelChanged != null) {
            this.mDialogModelChanged.dismiss();
            this.mDialogModelChanged = null;
        }
    }

    private DialogModelChanged mDialogModelChanged;
}
