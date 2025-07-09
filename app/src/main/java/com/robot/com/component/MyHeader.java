package com.robot.com.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.robot.com.component.listener.IItemListener;
import com.robot.com.component.listener.NoConfusion;
import com.robot.com.R;


public class MyHeader extends RelativeLayout implements NoConfusion, View.OnClickListener {
    private ImageView mImgMore;
    private TextView mTxtTitle;
    private IItemListener mListener;
    private int mType;
    private ImageView imgArrow;

    public static final int TYPE_ABOUT = 1;
    public static final int TYPE_MAIN = 2;

    public static final int SRC_BACK = 1;
    public static final int SRC_MORE = 2;

    public MyHeader(Context context) {
        super(context);
        init();
    }

    public MyHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.header_layout,this, true);

        this.imgArrow = this.findViewById(R.id.img_arrow);
        this.mTxtTitle = this.findViewById(R.id.title);
        this.mImgMore = this.findViewById(R.id.img_more);
        this.mImgMore.setOnClickListener(this);
        this.imgArrow.setOnClickListener(this);
    }

    public void setTitle(String title){
        this.mTxtTitle.setText(title);
    }

    public void setItemListener(IItemListener mListener){
        this.mListener = mListener;
    }

    public void updateType(int mType){
        this.mType = mType;
        switch(this.mType){
            case TYPE_ABOUT:
                this.mImgMore.setVisibility(View.GONE);
                this.imgArrow.setVisibility(View.VISIBLE);
                break;
            case TYPE_MAIN:
                this.mImgMore.setVisibility(View.GONE);
                this.imgArrow.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if(mListener != null){
            if(v == this.mImgMore){
                mListener.onItemClick(v,SRC_MORE);
            }else if(v == this.imgArrow){
                mListener.onItemClick(v,SRC_BACK);
            }
        }
    }
}
