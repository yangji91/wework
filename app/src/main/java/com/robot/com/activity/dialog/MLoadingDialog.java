package com.robot.com.activity.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.robot.com.R;
import com.robot.util.DeviceUtil;

public class MLoadingDialog extends Dialog {
	private LayoutInflater li = null;
	private TextView txtView;
	private TextView txtViewSubTitle;
	private int paddingLeft;
	
	public MLoadingDialog(Context context) {
		super(context);
		init();
	}
	
	public MLoadingDialog(Context context, int theme) {
		super(context, theme);
		init();
	}

	private void init(){
		li = LayoutInflater.from(getContext());
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(true);
		this.paddingLeft = (int) getContext().getResources().getDimension(R.dimen.common_margin_left);
	}

	public void setMCancle(boolean cancleAble){
		this.setCancelable(cancleAble);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View mView = li.inflate(R.layout.dialog_loading, null);
		ImageView imgView = (ImageView) mView.findViewById(R.id.loading_img);
		txtView = (TextView) mView.findViewById(R.id.load_tips);
		txtViewSubTitle = (TextView) mView.findViewById(R.id.sub_title);
		
		Animation ani = AnimationUtils.loadAnimation(getContext(),R.anim.loading_anim_rotate);
		ani.setRepeatCount(Animation.INFINITE);
		imgView.startAnimation(ani);
		int mHeight = DeviceUtil.mHeight;
		int mWidth = DeviceUtil.mWidth;
		LayoutParams llp = new LayoutParams(mWidth, mHeight);
		this.setContentView(mView,llp);
	}
	
	public void setTips(String str, boolean isCenter){
		txtView.setVisibility(View.VISIBLE);
		txtView.setText(str);
		if(isCenter){
			txtView.setGravity(Gravity.CENTER);
		}else{
			txtView.setGravity(Gravity.LEFT);
		}
	}
	
	public void setSubTitle(String subTitle){
		if(!TextUtils.isEmpty(subTitle)){
			txtViewSubTitle.setVisibility(View.VISIBLE);
			txtViewSubTitle.setText(subTitle);
		}else{
			txtViewSubTitle.setVisibility(View.INVISIBLE);
		}
	}
	
	public void setTipsLeft(){
		txtView.setGravity(Gravity.LEFT);
		txtView.setPadding(this.paddingLeft, 0, 0, 0);
	}
}
