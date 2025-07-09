package com.robot.com.activity.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import com.robot.com.component.listener.IItemListener;
import com.robot.com.R;
import com.robot.util.DeviceUtil;

/**
 * 模式切换
 */
public class DialogModelChanged extends Dialog implements  View.OnClickListener{
    private TextView mTxtRobot;
    private TextView mTxtPull;
    private TextView mTxtCancle;
    private IItemListener mItemListener;

    public static final int SRC_ROBOT = 1;
    public static final int SRC_PULL = 2;
    public static final int SRC_CANCLE = 3;

    public DialogModelChanged(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init(){
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = li.inflate(R.layout.common_dialog_modelchanged_layout,null);

        this.mTxtRobot = mView.findViewById(R.id.txt_robot);
        this.mTxtRobot.setOnClickListener(this);

        this.mTxtPull = mView.findViewById(R.id.txt_pull);
        this.mTxtPull.setOnClickListener(this);

        this.mTxtCancle = mView.findViewById(R.id.txt_cancle);
        this.mTxtCancle.setOnClickListener(this);

        int offset = (int) getContext().getResources().getDimension(R.dimen.dp_18);
        int mWidth = DeviceUtil.mWidth;
        mWidth = mWidth - offset*4;
        ViewGroup.LayoutParams llp = new ViewGroup.LayoutParams(mWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(mView, llp);
    }

    @Override
    public void onClick(View view) {
        if(mItemListener != null){
            if(view == this.mTxtRobot){
                this.mItemListener.onItemClick(null,SRC_ROBOT);
            }else if(view == this.mTxtPull){
                this.mItemListener.onItemClick(null,SRC_PULL);
            }else if(view == this.mTxtCancle){
                this.mItemListener.onItemClick(null,SRC_CANCLE);
            }
        }
    }

    public void setItemListener(IItemListener mListener){
        this.mItemListener = mListener;
    }
}
