package com.sl.shenmian.module.main.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.sl.shenmian.R;
import com.sl.shenmian.lib.base.activity.BaseActivity;
import com.sl.shenmian.lib.constant.ConstantValues;
import com.sl.shenmian.lib.net.RetrofitManage;
import com.sl.shenmian.lib.net.body.ServerResponseResult;
import com.sl.shenmian.lib.net.callback.DataNetCallback;
import com.sl.shenmian.lib.net.retrofit.RetrofitService;
import com.sl.shenmian.lib.net.rxjava.DataNetObserver;
import com.sl.shenmian.lib.net.url.NetApi;
import com.sl.shenmian.lib.utils.sharedpreferences.SpUtil;
import com.sl.shenmian.lib.utils.toast.ToastUtil;
import com.sl.shenmian.module.commons.IntentKeys;
import com.sl.shenmian.module.main.pojo.CarLic;
import com.sl.shenmian.module.main.pojo.Station;
import com.sl.shenmian.module.main.pojo.StationType;
import com.sl.shenmian.module.offline.ChooseHistoryActivity;
import com.sl.shenmian.module.scan.ScanActivity;
import com.sl.shenmian.module.seachcode.ui.SeachCodeActivity;
import com.sl.shenmian.module.settings.SettingActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

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
        initdata();
    }

    private void initConfig() {
        initBackToolbar(getString(R.string.main_menu));
        Toolbar toolbar = getToolbar();
        toolbar.setNavigationIcon(R.mipmap.ic_title_back);

    }

    private void initdata(){
        loadCarLic();
        loadStation(StationType.CUSTOMS, mStationNetObserver1);
        loadStation(StationType.WAREHOUSE, mStationNetObserver2);
        loadStation(StationType.STORE, mStationNetObserver3);

        ToastUtil.show(this,"初始化数据完成");
    }

    @Override
    protected void onFinish() {
        showDialog("提示", getString(R.string.exit_app_tips), "退出", "取消");
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
                activityJumps(intent);
                break;
            case R.id.main_ware_in_storage_btn:
                //仓库入库解封
                intent.setClass(mContext, ScanActivity.class);
                intent.putExtra(IntentKeys.KEY_ACTIVITY_TITLE_NAME, R.string.ware_in_storage);
                activityJumps(intent);
                break;
            case R.id.main_ware_out_storage_btn:
                //仓库出库施封
                intent.setClass(mContext, ScanActivity.class);
                intent.putExtra(IntentKeys.KEY_ACTIVITY_TITLE_NAME, R.string.ware_out_storage);
                activityJumps(intent);
                break;
            case R.id.main_store_in_storage_btn:
                //门店入库解封
                intent.setClass(mContext, ScanActivity.class);
                intent.putExtra(IntentKeys.KEY_ACTIVITY_TITLE_NAME, R.string.store_in_storage);
                activityJumps(intent);
                break;
            case R.id.main_scan_search_btn:
                //扫码查询
                intent.setClass(mContext, SeachCodeActivity.class);
                activityJumps(intent);
                break;
            case R.id.main_history_record_btn:
                //历史记录
                intent.setClass(mContext, ChooseHistoryActivity.class);
                activityJumps(intent);
                break;
            case R.id.main_settings_btn:
                //设置
                intent.setClass(mContext, SettingActivity.class);
                activityJumps(intent);
                break;
            default:
                break;
        }


    }


    @Override
    public void onBackPressed() {
        onFinish();
    }

    private RetrofitManage mRetrofitManage;
    private DataNetObserver mStationNetObserver1;
    private DataNetObserver mStationNetObserver2;
    private DataNetObserver mStationNetObserver3;
    private DataNetObserver mCarLicNetObserver;
    private void loadStation(StationType stationType,DataNetObserver mStationNetObserver){
        mRetrofitManage = null;
        if (mRetrofitManage == null) {
            mRetrofitManage = new RetrofitManage();
        }
        if(null == mStationNetObserver) {
            mStationNetObserver = new DataNetObserver(mContext,new DataNetCallback() {
                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onFailResponse(String dataJson, ServerResponseResult serverResponseResult) {

                }

                @Override
                public void onOkResponse(String dataJson) {

                    switch (stationType){
                        case STORE:
                            SpUtil.putString(mContext, ConstantValues.OffLineData.StoreInStorage_address_local_key, dataJson);
                            break;
                        case CUSTOMS:
                            SpUtil.putString(mContext, ConstantValues.OffLineData.Clearance_address_local_key, dataJson);
                            break;
                        case WAREHOUSE:
                            SpUtil.putString(mContext, ConstantValues.OffLineData.WareStorage_address_local_key, dataJson);
                            break;

                    }
                }
            });
        }

        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("stationType", stationType.name());
        RetrofitService service = mRetrofitManage.createService();
        String urlPath = NetApi.App.LOAD_STATION;
        Observable<Response<ResponseBody>> observable = service.postFormNet(urlPath,paramMap);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mStationNetObserver);
    }

    private void loadCarLic() {
        mRetrofitManage = null;
        if (mRetrofitManage == null) {
            mRetrofitManage = new RetrofitManage();
        }
        if (null == mCarLicNetObserver) {
            mCarLicNetObserver = new DataNetObserver(mContext, new DataNetCallback() {
                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onFailResponse(String dataJson, ServerResponseResult serverResponseResult) {
                }

                @Override
                public void onOkResponse(String dataJson) {
                    savleOffileCarLic(dataJson);
                }
            });
        }

        HashMap<String, Object> paramMap = new HashMap<>();
        RetrofitService service = mRetrofitManage.createService();
        String urlPath = NetApi.App.LOAD_CARLIC;
        Observable<Response<ResponseBody>> observable = service.postFormNet(urlPath, paramMap);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mCarLicNetObserver);
    }

    private void savleOffileCarLic(String dataJson){
        SpUtil.putString(mContext, ConstantValues.OffLineData.WareStorage_car_local_key, dataJson);
    }
}
