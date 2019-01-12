package com.sl.shenmian.module.main.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.sl.shenmian.R;
import com.sl.shenmian.lib.base.activity.BaseActivity;
import com.sl.shenmian.lib.net.RetrofitManage;
import com.sl.shenmian.lib.net.callback.NetCallback;
import com.sl.shenmian.lib.net.retrofit.RetrofitService;
import com.sl.shenmian.lib.net.rxjava.NetObserver;
import com.sl.shenmian.lib.net.url.NetApi;
import com.sl.shenmian.lib.net.url.NetBaseUrl;
import com.sl.shenmian.lib.ui.dialog.CustomDialog;
import com.sl.shenmian.module.commons.Constants;
import com.sl.shenmian.module.commons.IntentKeys;
import com.sl.shenmian.module.main.pojo.Result;
import com.sl.shenmian.module.main.pojo.Station;
import com.sl.shenmian.module.main.pojo.StationType;
import com.sl.shenmian.module.scan.ScanActivity;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

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

    private RetrofitManage mRetrofitManage;
    private NetObserver mNetObserver;

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

    @OnClick({R.id.main_clearance_btn,R.id.main_ware_in_storage_btn,R.id.main_ware_out_storage_btn,
            R.id.main_store_in_storage_btn,R.id.main_scan_search_btn,R.id.main_history_record_btn,
            R.id.main_settings_btn})
    void OnClick(View v) {
        switch (v.getId()){
            case R.id.main_clearance_btn:
                //通关施封
                startScanActivity(R.string.clearance);
                break;
            case R.id.main_ware_in_storage_btn:
                //仓库入库解封
                startScanActivity(R.string.ware_in_storage);
                break;
            case R.id.main_ware_out_storage_btn:
                //仓库出库施封
                startScanActivity(R.string.ware_out_storage);
                break;
            case R.id.main_store_in_storage_btn:
                //门店入库解封
                startScanActivity(R.string.store_in_storage);
                break;
            case R.id.main_scan_search_btn:
                //扫码查询

                break;
            case R.id.main_history_record_btn:
                //历史记录

                break;
            case R.id.main_settings_btn:
                //设置

                break;

        }
    }

    /**
     * 启动扫码页面
     */
    private void startScanActivity(int resId){
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(IntentKeys.KEY_ACTIVITY_TITLE_NAME,resId);
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
