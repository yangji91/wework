package com.robot.com.activity.base;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.robot.common.Global;
import com.robot.com.R;
import com.robot.com.activity.dialog.DialogConfirm;
import com.robot.com.activity.dialog.MLoadingDialog;
import com.robot.util.MyLog;
import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends Activity {
    protected final String TAG = getClass().getSimpleName();

    protected void requestPermission(List<String> mList, int reqCode){
        if(mList.size() > 0){
            String[] arr = new String[mList.size()];
            for(int i = 0;i < mList.size();i++){
                String str = mList.get(i);
                arr[i] = str;
            }
            for(int i = 0;i < arr.length;i++)
                MyLog.debug(TAG,"[requestPer]" + " arr:" + arr[i]);
            ActivityCompat.requestPermissions(this,arr,reqCode);
        }
    }

    protected List<String> checkPermission(String[] array){
        List<String> mList = new ArrayList<>();
        for(int i = 0;i < array.length;i++){
            String p = array[i];
            if(!TextUtils.isEmpty(p)){
                if(ContextCompat.checkSelfPermission(this,p) != PackageManager.PERMISSION_GRANTED){	//权限通过
                    mList.add(p);
                }
            }
        }
        return mList;
    }

    protected boolean checkAllPer(String[] array){
        boolean flag = true;
        if(array != null){
            for(int i = 0;i < array.length;i++){
                String p = array[i];
                if(!TextUtils.isEmpty(p)){
                    if(ContextCompat.checkSelfPermission(this,p) != PackageManager.PERMISSION_GRANTED){	//权限通过
                        flag = false;
                    }
                }
            }
        }
        return flag;
    }


    protected void hideDialogCfg(){
        if(this.mDialogCfg != null){
            this.mDialogCfg.dismiss();
            this.mDialogCfg = null;
        }
    }

    protected DialogConfirm mDialogCfg;

    protected void showToast(final String tips){
        Global.postRunnable2UI(() -> {
            hideToast();
            mToast = Toast.makeText(BaseActivity.this,tips,Toast.LENGTH_LONG);
            mToast.show();
        });

    }

    protected void hideToast(){
        if(mToast != null){
            mToast.cancel();
            mToast = null;
        }
    }

    private Toast mToast;

    protected void handleMsg(Message msg){};

    private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(!isFinishing()){
                handleMsg(msg);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiHandler.removeCallbacksAndMessages(null);
    }

    protected void sendMsg(Message msg){
        uiHandler.sendMessage(msg);
    }

    protected void sendMsgDelay(Message msg,long milli){
        uiHandler.sendMessageDelayed(msg,milli);
    }

    protected void showDialogLoading(String title){
        hideDialogLoading();
        this.mLoadingDialog = new MLoadingDialog(this, R.style.MyDialogBg);
        this.mLoadingDialog.show();
        if(TextUtils.isEmpty(title)){
            title = getResources().getString(R.string.str_loading);
        }
        this.mLoadingDialog.setSubTitle(title);
    }

    protected void hideDialogLoading(){
        if(this.mLoadingDialog != null){
            this.mLoadingDialog.dismiss();
            this.mLoadingDialog = null;
        }
    }

    private MLoadingDialog mLoadingDialog;
}
