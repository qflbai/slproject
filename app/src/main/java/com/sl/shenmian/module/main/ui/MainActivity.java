package com.sl.shenmian.module.main.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.sl.shenmian.R;
import com.sl.shenmian.lib.base.activity.BaseActivity;
import com.sl.shenmian.lib.ui.dialog.CustomDialog;
import com.sl.shenmian.module.commons.IntentKeys;
import com.sl.shenmian.module.scan.ScanActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private CustomDialog dialog = null;

    /**
     * 通关施封
     */
    @BindView(R.id.main_clearance_btn)
    Button main_clearance_btn;
    /**
     * 仓库入库解封
     */
    @BindView(R.id.main_ware_in_storage_btn)
    Button main_ware_in_storage_btn;
    /**
     * 仓库出库解封
     */
    @BindView(R.id.main_ware_out_storage_btn)
    Button main_ware_out_storage_btn;
    /**
     * 门店入库解封
     */
    @BindView(R.id.main_store_in_storage_btn)
     Button main_store_in_storage_btn;
    /**
     * 扫码查询
     */
    @BindView(R.id.main_scan_search_btn)
    Button main_scan_search_btn;
    /**
     * 历史记录
     */
    @BindView(R.id.main_history_record_btn)
     Button main_history_record_btn;
    /**
     * 设置
     */
    @BindView(R.id.main_settings_btn)
    Button main_settings_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_view);
        initConfig();
    }

    private void initConfig() {
        initBackToolbar(getString(R.string.main_menu));
        Toolbar toolbar = getToolbar();
        toolbar.setNavigationIcon(R.mipmap.ic_title_back);
        toolbar.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_left_btn:

                        finish();
                    break;
                case R.id.dialog_right_btn:

                        dismiss();
                    break;
                case R.id.dialog_title_btn:
                        dismiss();
                    break;
                 default:
                        showExitDialog();
                     break;
            }
        }
    };

    @OnClick({R.id.main_clearance_btn})
    void onClick(View v) {
        switch (v.getId()){
            case R.id.main_clearance_btn:
                    startClearance();
                break;
        }
    }

    /**
     * 通关施封
     */
    private void startClearance(){
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(IntentKeys.KEY_ACTIVITY_TITLE_NAME,R.string.clearance);
        startActivity(intent);
    }

    private void showExitDialog(){
        if(null == dialog) {
            dialog = new CustomDialog();
        }
        dialog.removeWindowTitle(true);
        dialog.setContentIconIsShow(false);
        dialog.setDialogContentMsg(R.string.exit_app_tips);
        dialog.setDialogLeftBtnText(R.string.exit);
        dialog.setDialogTitleBtnOnClick(onClickListener);
        dialog.setDialogLeftBtnOnClick(onClickListener);
        dialog.setDialogRightBtnOnClick(onClickListener);
        dialog.show(getSupportFragmentManager(),"mainExitDialog");
    }

    private void dismiss(){
        if(null != dialog){
            dialog.dismiss();
        }
    }
}
