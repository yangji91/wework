package com.robot.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.robot.common.Global;
import com.robot.entity.BCheckEntity;
import com.robot.entity.UserEntity;
import com.robot.com.ComWorkServiceManager;
import com.robot.com.R;
import com.robot.com.activity.base.BaseActivity;
import com.robot.com.component.MyHeader;
import com.robot.com.component.listener.IItemListener;
import com.robot.util.CheckUtil;
import com.robot.util.FileUtil;


public class CheckActivity extends BaseActivity {
    private BCheckEntity checkEntity;
    private final int FLAG_USER_INFO = 0x1;
    private final int FLAG_WORKING = 0x2;

    private TextView mTxtConnectStatus;

    public static final void startCheckActivity(Activity mContext, int reqCode) {
        Intent intent = new Intent(mContext, CheckActivity.class);
        mContext.startActivityForResult(intent, reqCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_layout);
        MyHeader mHeader = this.findViewById(R.id.header);
        mHeader.updateType(MyHeader.TYPE_ABOUT);
        mHeader.setItemListener(new IItemListener() {
            @Override
            public void onItemClick(View mView, int tag) {
                finish();
            }
        });
        String title = getResources().getString(R.string.item_check);
        mHeader.setTitle(title);
        BCheckEntity bEntity = CheckUtil.check(CheckActivity.this);
        initLayout(bEntity);
        delayTask();
    }

    public static final int WOWORK_RUNNING_UNKNOWN = -1;
    public static final int WOWORK_RUNNING_NO = 0;
    public static final int WOWORK_RUNNING_YES = 1;

    private void delayTask() {
        Global.postRunnable(() -> {
            UserEntity userEntity = FileUtil.loadUserLoginInfo();
            if (userEntity != null) {
                Message msg = Message.obtain();
                msg.what = FLAG_USER_INFO;
                msg.obj = userEntity;
                sendMsg(msg);
            }
            int isWoWorkRunning = ComWorkServiceManager.getInstance().isWoWorkRunning();
            Message msg = Message.obtain();
            msg.what = FLAG_WORKING;
            String str;
            if (isWoWorkRunning == WOWORK_RUNNING_YES) {
                str = "已链接";
            } else if (isWoWorkRunning == WOWORK_RUNNING_NO) {
                str = "断开链接";
            } else {
                str = "状态未知";
            }
            msg.obj = str;
            sendMsg(msg);
        });
    }

    boolean isFirst = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirst) {
            delayTask();
        }
        isFirst = false;
    }

    @Override
    protected void handleMsg(Message msg) {
        int what = msg.what;
        switch (what) {
            case FLAG_USER_INFO:
                UserEntity userEntity = (UserEntity) msg.obj;
                setUserInfo(userEntity);
                break;
            case FLAG_WORKING:
                String str = (String) msg.obj;
                this.mTxtConnectStatus.setText("服务链接状态:" + str);
                break;
        }
    }

    private void initLayout(BCheckEntity checkEntity) {
        TextView mTxtModel = this.findViewById(R.id.txt_model);
        mTxtModel.setText("设备品牌:" + Build.BRAND);

        TextView mTxtVerCode = this.findViewById(R.id.txt_vercode);
        mTxtVerCode.setText("定制企业微信版本号:" + checkEntity.verCode);

        TextView mTxtVerName = this.findViewById(R.id.txt_vername);
        mTxtVerName.setText("定制企业微信版本名称:" + checkEntity.verName);

        TextView mTxtFileLen = this.findViewById(R.id.txt_filelen);
        mTxtFileLen.setText("定制企业微信文件大小:" + FileUtil.getFileStr(checkEntity.fileLen) + "MB");

        TextView mTxtOsVer = this.findViewById(R.id.txt_osver);
        mTxtOsVer.setText("操作系统:" + checkEntity.andrVer);

        TextView mRobotVerCode = this.findViewById(R.id.txt_agent_vercode);
        mRobotVerCode.setText("版本号:" + checkEntity.robotVerCode);

        TextView mRobotVerName = this.findViewById(R.id.txt_agent_vername);
        mRobotVerName.setText("机器人版本名称:" + checkEntity.robotVerName);

        TextView mRobotFileLen = this.findViewById(R.id.txt_agent_filelen);
        mRobotFileLen.setText("机器人文件大小:" + FileUtil.getFileStr(checkEntity.robotFileLen) + "MB");

        TextView mTxtResult = this.findViewById(R.id.txt_check_result);

        this.mTxtConnectStatus = this.findViewById(R.id.txt_connect_status);
        this.mTxtConnectStatus.setText("服务链接状态:");
    }

    private void setUserInfo(UserEntity userEntity) {
        TextView mTxtLoginUid = this.findViewById(R.id.txt_login_user_id);
        mTxtLoginUid.setText("登录企微ID:" + userEntity.remoteId);
        TextView mTxtLoginName = this.findViewById(R.id.txt_login_user_name);
        mTxtLoginName.setText("登录企微昵称:" + userEntity.name);
        TextView mTxtPhone = this.findViewById(R.id.txt_login_user_phone);
        mTxtPhone.setText("登录企微手机号:" + userEntity.phone + userEntity.mobile);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
