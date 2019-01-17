package com.sl.shenmian.module.main.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.sl.shenmian.R;
import com.sl.shenmian.lib.base.activity.BaseActivity;
import com.sl.shenmian.module.commons.IntentKeys;
import com.sl.shenmian.module.offline.ChooseHistoryActivity;
import com.sl.shenmian.module.scan.ScanActivity;
import com.sl.shenmian.module.seachcode.ui.SeachCodeActivity;
import com.sl.shenmian.module.settings.SettingActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


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

    }

    @Override
    protected void onFinish() {
        showDialog("提示","是否退出","退出","取消");
    }

    @Override
    protected void dialogLeftClick(AlertDialog alertDialog) {
        alertDialog.dismiss();
       finish();
    }

    @Override
    protected void dialogRightClick(AlertDialog alertDialog) {
        alertDialog.dismiss();
    }

    @Override
    protected void dialogTitleRight(AlertDialog alertDialog) {
        alertDialog.dismiss();
    }

    @OnClick({R.id.main_clearance_btn, R.id.main_ware_in_storage_btn, R.id.main_ware_out_storage_btn,
            R.id.main_store_in_storage_btn, R.id.main_scan_search_btn, R.id.main_history_record_btn,
            R.id.main_settings_btn})
    void OnClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {

            case R.id.main_clearance_btn:
                //通关施封
                intent.setClass(mContext, ScanActivity.class);
                intent.putExtra(IntentKeys.KEY_ACTIVITY_TITLE_NAME, R.string.clearance);
                break;
            case R.id.main_ware_in_storage_btn:
                //仓库入库解封
                intent.setClass(mContext, ScanActivity.class);
                intent.putExtra(IntentKeys.KEY_ACTIVITY_TITLE_NAME, R.string.ware_in_storage);
                break;
            case R.id.main_ware_out_storage_btn:
                //仓库出库施封
                intent.setClass(mContext, ScanActivity.class);
                intent.putExtra(IntentKeys.KEY_ACTIVITY_TITLE_NAME, R.string.ware_out_storage);
                break;
            case R.id.main_store_in_storage_btn:
                //门店入库解封
                intent.setClass(mContext, ScanActivity.class);
                intent.putExtra(IntentKeys.KEY_ACTIVITY_TITLE_NAME, R.string.store_in_storage);
                break;
            case R.id.main_scan_search_btn:
                //扫码查询
                intent.setClass(mContext, SeachCodeActivity.class);
                break;
            case R.id.main_history_record_btn:
                //历史记录
                intent.setClass(mContext, ChooseHistoryActivity.class);
                break;
            case R.id.main_settings_btn:
                //设置
                intent.setClass(mContext, SettingActivity.class);
                break;
            default:
                break;
        }

        activityJumps(intent);
    }





}
