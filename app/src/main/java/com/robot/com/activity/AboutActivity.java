package com.robot.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.robot.common.MConfiger;
import com.robot.com.component.MyHeader;
import com.robot.entity.EnvirEnum;
import com.robot.com.component.listener.IItemListener;
import com.robot.com.R;
import com.robot.com.activity.base.BaseActivity;
import com.robot.util.PkgUtil;

/**
 * 关于页卡
 */
public class AboutActivity extends BaseActivity {

    public static final void startAboutActivity(Activity mActivity, int reqCode) {
        Intent intent = new Intent(mActivity, AboutActivity.class);
        mActivity.startActivityForResult(intent, reqCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_layout);

        initLayout();
    }

    private void initLayout() {
        MyHeader mHeader = this.findViewById(R.id.header);
        mHeader.updateType(MyHeader.TYPE_ABOUT);
        mHeader.setItemListener(new IItemListener() {
            @Override
            public void onItemClick(View mView, int tag) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }

            @Override
            public void onItemClick(View mView, Object obj, int tag) {
            }
        });
        String title = getResources().getString(R.string.about_title);
        mHeader.setTitle(title);

        TextView mTxtVerCode = this.findViewById(R.id.txt_version_code);
        String verCode = PkgUtil.getVersionCode(this) + "";
        mTxtVerCode.setText("version code:" + verCode);

        TextView mTxtVerName = this.findViewById(R.id.txt_version_name);
        String verName = PkgUtil.getVersionName(this);
        mTxtVerName.setText("version name:" + verName);

        TextView mTxtBuildInfo = this.findViewById(R.id.txt_buildinfo);
        ApplicationInfo appInfo = null;
        try {
            appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (appInfo != null) {
            String strBuildInfo = appInfo.metaData.getString("buildinfo");
            mTxtBuildInfo.setText("build:" + strBuildInfo);
        } else {
            mTxtBuildInfo.setText("");
        }

        TextView mTxtEnv = this.findViewById(R.id.txt_env);
//        String channel = MConfiger.phoneLocEnum.getDesc();
        if (MConfiger.env == EnvirEnum.TEST) {
            mTxtEnv.setText("env:" + "测试环境");
        } else if (MConfiger.env == EnvirEnum.PRODUCT) {
            mTxtEnv.setText("env:" + "正式环境");
        }
    }
}
