package com.robot.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.robot.common.Global;
import com.robot.common.MConfiger;
import com.robot.entity.DeviceModelEnum;
import com.robot.entity.PhoneLocEnum;
import com.robot.com.component.listener.IItemListener;
import com.robot.com.R;
import com.robot.com.activity.base.BaseActivity;
import com.robot.com.component.MyHeader;
import com.robot.com.file.SharePreSn;
import com.robot.util.DeviceUtil;
import com.robot.util.FileUtil;

/**
 * sn输入UI
 */
public class InputSnActivity extends BaseActivity {

    private EditText mEditTxt;
    private EditText mEditTxtAgain;
    private Button mBtnOk;
    private TextView mTxtErr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputsn_layout);
        initLayout();

        DeviceModelEnum deviceModelEnum = DeviceUtil.getDeviceModelType();
        if(MConfiger.phoneLocEnum == PhoneLocEnum.BAIDU){  //直接获取
            int colorGrey = this.getResources().getColor(R.color.color_999);
            String serial = null;
            serial = FileUtil.getBaiduPhoneCode();
            this.mEditTxt.setText(serial);
            this.mEditTxt.setEnabled(false);
            this.mEditTxt.setTextColor(colorGrey);
            this.mEditTxtAgain.setText(serial);
            this.mEditTxtAgain.setEnabled(false);
            this.mEditTxtAgain.setTextColor(colorGrey);
            this.mBtnOk.setVisibility(View.GONE);
        }else if(MConfiger.phoneLocEnum == PhoneLocEnum.HUAWEI){    //华为手机
            int colorGrey = this.getResources().getColor(R.color.color_999);
            String serial = null;
            serial = FileUtil.getHuaWeiYunCode();
            this.mEditTxt.setText(serial);
            this.mEditTxt.setEnabled(false);
            this.mEditTxt.setTextColor(colorGrey);
            this.mEditTxtAgain.setText(serial);
            this.mEditTxtAgain.setEnabled(false);
            this.mEditTxtAgain.setTextColor(colorGrey);
            this.mBtnOk.setVisibility(View.GONE);
        }else if(deviceModelEnum != DeviceModelEnum.XIAOMI){
            int colorGrey = this.getResources().getColor(R.color.color_999);
            String serial = DeviceUtil.getSerialNumber();
            if(TextUtils.isEmpty(serial)){
                this.mEditTxt.setEnabled(true);
                this.mEditTxtAgain.setEnabled(true);
                this.mBtnOk.setVisibility(View.VISIBLE);
            }else{
                this.mEditTxt.setText(serial);
                this.mEditTxt.setEnabled(false);
                this.mEditTxt.setTextColor(colorGrey);
                this.mEditTxtAgain.setText(serial);
                this.mEditTxtAgain.setEnabled(false);
                this.mEditTxtAgain.setTextColor(colorGrey);
                this.mBtnOk.setVisibility(View.GONE);
            }
        }else{ //小米输入获取
            this.mEditTxt.setEnabled(true);
            this.mEditTxtAgain.setEnabled(true);
            this.mBtnOk.setVisibility(View.VISIBLE);
            delayTask();
        }
    }

    public static final void startInputSnActivity(Activity mAc){
        Intent intent = new Intent(mAc,InputSnActivity.class);
        mAc.startActivity(intent);
    }

    private void initLayout(){
        MyHeader mHeader = this.findViewById(R.id.header);
        mHeader.updateType(MyHeader.TYPE_ABOUT);
        mHeader.setTitle("SN输入");
        mHeader.setItemListener(new IItemListener(){
            @Override
            public void onItemClick(View mView, int tag) {
                finish();
            }
        });

        this.mEditTxt = this.findViewById(R.id.edit_txt);
        this.mEditTxtAgain = this.findViewById(R.id.edit_txt_again);

        this.mBtnOk = this.findViewById(R.id.btn_ok);
        this.mBtnOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String sn1 = mEditTxt.getText().toString();
                String sn2 = mEditTxtAgain.getText().toString();
                if(TextUtils.isEmpty(sn1)){
                    showToast("请输入合法SN");
                    mEditTxt.requestFocus();
                    mTxtErr.setText("请输入合法SN");
                    return;
                }
                if(TextUtils.isEmpty(sn2)){
                    showToast("请输入合法的SN");
                    mEditTxtAgain.requestFocus();
                    mTxtErr.setText("请输入合法SN");
                    return;
                }
                if(!sn1.endsWith(sn2)){
                    showToast("两次输入不一致请重新输入");
                    mTxtErr.setText("两次输入不一致请重新输入");
                    return;
                }
                if(sn1.length() != 15){
                    showToast("校验错误，正确的SN码为15个字符!");
                    mTxtErr.setText("校验错误，正确的SN码为15个字符!");
                    return;
                }
                SharePreSn mSharePre = new SharePreSn();
                mSharePre.saveInfo(sn1);
                showToast("SN码保存成功~");
                finish();
            }
        });
        this.mTxtErr = this.findViewById(R.id.err_txt);
    }

    private void delayTask(){
        Global.postRunnable(new Runnable() {
            @Override
            public void run() {
                SharePreSn mSharePre = new SharePreSn();
                String sn = mSharePre.loadData();
                Message msg = Message.obtain();
                msg.what = FLAG_INPUT_SN;
                msg.obj = sn;
                uiHandler.sendMessage(msg);
            }
        });
    }

    private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if(!isFinishing()){
                if(what == FLAG_INPUT_SN){
                    String txt = (String) msg.obj;
                    mEditTxt.setText(txt);
                    mEditTxtAgain.setText(txt);
                }
            }
        }
    };

    private final int FLAG_INPUT_SN = 0x10;




}
