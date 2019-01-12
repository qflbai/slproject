package com.sl.shenmian.module.seachcode.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sl.shenmian.R;
import com.sl.shenmian.lib.base.activity.BaseActivity;
import com.sl.shenmian.lib.net.RetrofitManage;
import com.sl.shenmian.lib.net.body.ServerResponseResult;
import com.sl.shenmian.lib.net.callback.DataNetCallback;
import com.sl.shenmian.lib.net.callback.NetCallback;
import com.sl.shenmian.lib.net.retrofit.RetrofitService;
import com.sl.shenmian.lib.net.rxjava.DataNetObserver;
import com.sl.shenmian.lib.net.rxjava.NetObserver;
import com.sl.shenmian.lib.net.url.NetApi;
import com.sl.shenmian.lib.net.url.NetBaseUrl;
import com.sl.shenmian.module.main.pojo.Result;
import com.sl.shenmian.module.seachcode.pojo.SeachCodeInfo;
import com.sl.shenmian.module.seachcode.ui.adapter.SeachCodeAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class SearchResultActivity extends BaseActivity {
    private String scan_code;

    @BindView(R.id.feng_code_tv)
    TextView feng_code_tv;
    @BindView(R.id.search_result_view)
    RecyclerView search_result_view;

    private RetrofitManage mRetrofitManage;
    private DataNetObserver mNetObserver;

    private List<SeachCodeInfo> seachCodeInfoList = new ArrayList<>();
    private SeachCodeAdapter adapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scan_code = getIntent().getStringExtra("scan_code");
        setContentView(R.layout.scan_search_result_view);
        initData();
        initConfig();
    }

    private void initData(){
        feng_code_tv.setText(scan_code);
        loadSeachResultData();
    }

    private void loadSeachResultData(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        search_result_view.setLayoutManager(linearLayoutManager);
        seachCodeInfoList.clear();
        mRetrofitManage = null;
        if(null == mRetrofitManage) {
            mRetrofitManage = new RetrofitManage();
        }
        if(null == mNetObserver) {
            mNetObserver = new DataNetObserver(this,new DataNetCallback() {

                @Override
                public void onOkResponse(String dataJson) {

                    List<SeachCodeInfo> mlist = JSON.parseArray(dataJson,SeachCodeInfo.class);
                    seachCodeInfoList.addAll(mlist);
                    setScanResult();

                }

                @Override
                public void onFailResponse(String dataJson, ServerResponseResult serverResponseResult) {

                }

                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onError(Throwable e) {

                }
            });
        }

        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("labelCode",scan_code);
        RetrofitService service = mRetrofitManage.createService();
        String urlPath = NetApi.App.SEARCH_CODE;
        Observable<Response<ResponseBody>> observable = service.postFormNet(urlPath,paramMap);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mNetObserver);

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

    private void setScanResult(){
        adapter = new SeachCodeAdapter(this);
        adapter.setData(seachCodeInfoList);
        search_result_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
