package com.sl.shenmian.module.scan;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.sl.shenmian.R;
import com.sl.shenmian.lib.base.activity.ZbarActivity;
import com.sl.shenmian.module.commons.IntentKeys;

public class ScanActivity extends ZbarActivity {

    private int mTitleResId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_view);
        initData();
        initConfig();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initData(){
        mTitleResId = getIntent().getIntExtra(IntentKeys.KEY_ACTIVITY_TITLE_NAME,R.string.app_name);
    }
    private void initConfig() {
        initBackToolbar(getString(mTitleResId));
        Toolbar toolbar = getToolbar();
        toolbar.setNavigationIcon(R.mipmap.ic_title_back);
        toolbar.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    protected void onData(String data) {
        Log.e(TAG, data);
    }
}
