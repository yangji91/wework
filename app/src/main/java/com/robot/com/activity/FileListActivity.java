package com.robot.com.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import com.robot.com.R;
import com.robot.com.activity.base.BaseActivity;

import java.util.ArrayList;


public class FileListActivity extends BaseActivity {


    private BroadcastReceiver receiver;
    private TextView robot,wework,weworkData;
    public static final void startFileListActivity(Activity mContext,int reqCode){
        Intent intent = new Intent(mContext, FileListActivity.class);
        mContext.startActivityForResult(intent,reqCode);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filelist_layout);
        robot=findViewById(R.id.txt_robot);
        wework =findViewById(R.id.txt_wework);
        weworkData=findViewById(R.id.txt_wework_data);
        this.receiver = new BroadcastReceiver() {
            /* class com.android.chearfile.MainActivity.AnonymousClass2 */

            public void onReceive(Context context, Intent intent) {
                if ("com.tencent.mm.FileSize".equals(intent.getAction())) {
                   ArrayList<String> FileList = intent.getStringArrayListExtra("FileList");
                    StringBuilder stringBuilder =new StringBuilder();
                    for (String file:FileList){
                        stringBuilder.append(" 目录 ").append(file).append("\n");
                    }
                    weworkData.setText(stringBuilder.toString());
                    return;
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.tencent.wework.FileSize");
        registerReceiver(this.receiver, filter);

    }

    @Override
    protected void onDestroy() {
        if (receiver!=null){
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }
}
