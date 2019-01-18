package com.sl.shenmian.module.scan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.sl.shenmian.R;
import com.sl.shenmian.lib.base.activity.ZbarActivity;
import com.sl.shenmian.lib.constant.ConstantValues;
import com.sl.shenmian.lib.net.RetrofitManage;
import com.sl.shenmian.lib.net.callback.NetCallback;
import com.sl.shenmian.lib.net.retrofit.RetrofitService;
import com.sl.shenmian.lib.net.rxjava.NetObserver;
import com.sl.shenmian.lib.net.url.NetApi;
import com.sl.shenmian.lib.net.url.NetBaseUrl;
import com.sl.shenmian.lib.utils.toast.ToastUtil;
import com.sl.shenmian.module.clearance.ClearanceActivity;
import com.sl.shenmian.module.commons.IntentKeys;
import com.sl.shenmian.module.main.pojo.Result;
import com.sl.shenmian.module.scan.pojo.CodeState;
import com.sl.shenmian.module.storeinstorage.StoreInStorageActivity;
import com.sl.shenmian.module.wareinstorage.WareInStorageActivity;
import com.sl.shenmian.module.wareoutstorage.WareOutStorageActivity;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class ScanActivity extends ZbarActivity {

    private int mTitleResId;
    private RetrofitManage mRetrofitManage;
    private NetObserver mNetObserver;
    private String code = null;

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
       // Log.e(TAG, data);
        code = data;
        searchCodeState(data);
    }

    private void searchCodeState(String data){
        mRetrofitManage = null;
        if (mRetrofitManage == null) {
            mRetrofitManage = new RetrofitManage();
        }
        if(null == mNetObserver) {
            mNetObserver = new NetObserver(mContext,new NetCallback() {

                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onResponse(String dataJson) {
                    Result result = JSON.parseObject(dataJson,Result.class);
                    if(null != result && null != result.getData() && result.getData().length() > 0){

                        CodeState codeState = JSON.parseObject(result.getData(),CodeState.class);
                        labelCodeStatus(codeState);
                    }
                }

                @Override
                public void onError(Throwable e) {

                }
            });
        }

        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("labelCode",data);
        RetrofitService service = mRetrofitManage.createService();
        String urlPath = NetApi.App.SEAL_STATUS;
        Observable<Response<ResponseBody>> observable = service.postFormNet(urlPath,paramMap);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mNetObserver);
    }

    private boolean checkHasSeal(CodeState codeState){
        if(!codeState.isHasSeal()){
            //封条信息不存在
            ToastUtil.show(mContext,getString(R.string.seal_no_found_info));
            return false;
        }
        if(codeState.isHasLocked()){
            //封条已施封
            ToastUtil.show(mContext,getString(R.string.seal_has_locked));
            return false;
        }
        return true;
    }

    private final static int CodeTypeHasLock = 0; //仓库
    private final static int CodeTypeHasUnLock = 1;//门店
    private boolean checkHasLock(CodeState codeState){
        if(!codeState.isHasSeal()){
            //封条信息不存在
            ToastUtil.show(mContext,getString(R.string.seal_no_found_info));
            return false;
        }
        if(!codeState.isHasLocked()){
            //未施封
            ToastUtil.show(mContext,getString(R.string.seal_no_has_locked));
            return false;
        }else {
            if(mTitleResId == R.string.store_in_storage
                    && codeState.getLogType() == CodeTypeHasLock){
                //仓库出库对应门店入库
                ToastUtil.show(ScanActivity.this,"交叉施解封,请使用仓库入库解封");
                return false;
            }
            if(mTitleResId == R.string.store_in_storage
                    && codeState.getLogType() == CodeTypeHasUnLock){
                //仓库出库对应门店入库
                ToastUtil.show(ScanActivity.this,"交叉施解封,请使用门店入库解封");
                return false;
            }
        }
        if(codeState.isHasUnblock()){
            //已解封
            ToastUtil.show(mContext,getString(R.string.seal_has_un_block));
            return false;
        }
        return true;
    }


    private void startIntent(Intent intent){
        intent.putExtra("seal_code",code);
        startActivity(intent);
    }


    private void labelCodeStatus(CodeState codeState){

        if(mTitleResId == R.string.clearance){
            //通关施封
            if(checkHasSeal(codeState)){
                Intent intent = new Intent(ScanActivity.this, ClearanceActivity.class);
                startIntent(intent);
            }

        }else  if(mTitleResId == R.string.ware_out_storage){
            //仓库出库施封
            if(checkHasSeal(codeState)){
                Intent intent = new Intent(ScanActivity.this, WareOutStorageActivity.class);
                startIntent(intent);
            }

        }else  if(mTitleResId == R.string.store_in_storage){
            //门店入库解封
            if(checkHasLock(codeState)){
                Intent intent = new Intent(ScanActivity.this, StoreInStorageActivity.class);
                startIntent(intent);
            }

        }else  if(mTitleResId == R.string.ware_in_storage){
            //仓库入库解封
            if(checkHasLock(codeState)){
                Intent intent = new Intent(ScanActivity.this, WareInStorageActivity.class);
                startIntent(intent);
            }

        }

        finish();
    }
}
