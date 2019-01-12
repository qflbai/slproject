package com.sl.shenmian.module.seachcode.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.sl.shenmian.R;
import com.sl.shenmian.lib.base.activity.ZbarActivity;
import com.sl.shenmian.lib.utils.toast.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 扫码查询
 */
public class SeachCodeActivity extends ZbarActivity {

    @BindView(R.id.search_view)
    EditText search_view;
    @BindView(R.id.search_btn)
    RelativeLayout search_btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_search_view);
        initConfig();
    }

    @OnClick({R.id.search_btn})
    void onClik(View v){
        switch (v.getId()){
            case R.id.search_btn:
                    startSearch(search_view.getText().toString().trim());
                break;
        }
    }

    private void initConfig() {
        initBackToolbar(getString(R.string.scan_search));
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

    private void startSearch(String trim) {
        if(trim.length() > 0){
            Intent intent = new Intent(this,SearchResultActivity.class);
            intent.putExtra("scan_code",trim);
            startActivity(intent);
        }else {
            ToastUtil.show(this,getString(R.string.seal_empty));
        }
    }

    @Override
    protected void onData(String data) {
        super.onData(data);
        search_view.setText(data);
    }
}
