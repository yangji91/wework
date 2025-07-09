package com.robot.com.activity.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.robot.com.component.listener.IItemListener;
import com.robot.com.R;
import com.robot.util.DeviceUtil;

/**
 * Created by billy on 2017/7/27.
 */
public class DialogConfirm extends Dialog implements  View.OnClickListener{

    private TextView mTxtTitle;
    private TextView mTxtContents;
    private Button btnCancle;
    private Button btnOk;
    private IItemListener mListener;

    public static final int BUTTON_TYPE_CANCLE = 1;
    public static final int BUTTON_TYPE_OK = 2;
    private int mType;

    public static final int TYPE_PERMISSION = 1;
    public static final int TYPE_CFG_REFRESH = 2;

    public DialogConfirm(Context context, int theme) {
        super(context, theme);
        init();
    }

    private void init(){
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    public void updateType(int mType){
        this.mType = mType;
        String tips = null;
        String content = null;
        String strBtnCancle = null;
        String strBtnOk = null;
        switch(this.mType){
            case TYPE_PERMISSION:
                tips = "权限确认提示框";
                String appName = getContext().getResources().getString(R.string.app_name);
                content = appName + "应用需要以上权限，请您点击同意该权限";
                strBtnOk = "申请权限";
                strBtnCancle = "关闭";
                break;
            case TYPE_CFG_REFRESH:
                tips = "提示框";
                content = "确定刷新配置文件，新的配置文件会替代旧的配置文件";
                strBtnOk = "确定";
                strBtnCancle = "取消";
                break;
        }
        if(!TextUtils.isEmpty(tips)){
            mTxtTitle.setText(tips);
        }
        if(!TextUtils.isEmpty(content)){
            mTxtContents.setText(content);
        }
        if(!TextUtils.isEmpty(strBtnCancle)){
            this.btnCancle.setText(strBtnCancle);
        }
        if(!TextUtils.isEmpty(strBtnOk)){
            this.btnOk.setText(strBtnOk);
        }
    }

    public void updateContents(String contents){
        if(!TextUtils.isEmpty(contents)){
            this.mTxtContents.setText(contents);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = li.inflate(R.layout.common_dialog_confirm_layout,null);

        this.mTxtTitle = mView.findViewById(R.id.txt_title);
        this.mTxtContents = mView.findViewById(R.id.txt_contents);
        this.btnCancle = mView.findViewById(R.id.btn_cancle);
        this.btnCancle.setOnClickListener(this);
        this.btnOk = mView.findViewById(R.id.btn_ok);
        this.btnOk.setOnClickListener(this);

        int offset = (int) getContext().getResources().getDimension(R.dimen.dp_18);
        int mWidth = DeviceUtil.mWidth;
        mWidth = mWidth - offset*2;
        ViewGroup.LayoutParams llp = new ViewGroup.LayoutParams(mWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(mView, llp);
    }

    @Override
    public void onClick(View v) {
        if(this.mListener != null){
            if(v == this.btnCancle){
                this.mListener.onItemClick(null,BUTTON_TYPE_CANCLE);
            }else if(v == this.btnOk){
                this.mListener.onItemClick(null,BUTTON_TYPE_OK);
            }
        }
    }

    public void setListener(IItemListener mListener){
        this.mListener = mListener;
    }
}
