package com.sl.shenmian.module.clearance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sl.shenmian.R;
import com.sl.shenmian.lib.base.activity.BaseActivity;

import butterknife.BindView;

public class ClearanceActivity extends BaseActivity {


    @BindView(R.id.feng_code_tv)
    TextView feng_code_tv;
    @BindView(R.id.clearance_user_tv)
    TextView clearance_user_tv;
    @BindView(R.id.clearance_addr_spinner)
    Spinner clearance_addr_spinner;
    @BindView(R.id.car_number_ed)
    EditText car_number_ed;
    @BindView(R.id.remark_ed)
    TextView remark_ed;
    @BindView(R.id.sig_add_btn)
    ImageView sig_add_btn;
    @BindView(R.id.sig_list_view)
    GridLayout sig_list_view;

    private String seal_code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_clearance_view);
        seal_code =  getIntent().getStringExtra("seal_code");
        initConfig();
        initData();
    }

    private void initConfig() {
        initBackToolbar(getString(R.string.clearance));
        Toolbar toolbar = getToolbar();
        toolbar.setNavigationIcon(R.mipmap.ic_title_back);
        toolbar.setOnClickListener(onClickListener);
    }

    private void initData(){
        feng_code_tv.setText(seal_code);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}
