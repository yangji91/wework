package com.robot.com.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.robot.com.component.listener.IItemListener;
import com.robot.com.component.listener.NoConfusion;
import com.robot.com.R;


public class ItemView extends RelativeLayout implements NoConfusion, View.OnClickListener{

    private TextView mTxtTitle;
    private RelativeLayout mRelaMain;
    private IItemListener mListener;
    private TextView mTxtContent;

    public ItemView(Context context) {
        super(context);
        init();
    }

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.itemview_layout,this,true);

        this.mRelaMain = this.findViewById(R.id.rela_main);
        this.mTxtTitle = this.findViewById(R.id.txt_title);
        this.mTxtContent = this.findViewById(R.id.txt_content);
        
        this.mRelaMain.setOnClickListener(this);
    }

    public void setContents(String contents){
        this.mTxtContent.setText(contents);
    }

    public void setTitle(String title){
        this.mTxtTitle.setText(title);
    }

    public void setItemListener(IItemListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onClick(View v) {
        if(mListener != null){
            if(v == this.mRelaMain){
                mListener.onItemClick(null,-1);
            }
        }
    }
}
