package com.sl.shenmian.module.storeinstorage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sl.shenmian.R;
import com.sl.shenmian.lib.base.activity.BaseActivity;
import com.sl.shenmian.lib.constant.ConstantValues;
import com.sl.shenmian.lib.net.RetrofitManage;
import com.sl.shenmian.lib.net.body.ServerResponseResult;
import com.sl.shenmian.lib.net.callback.DataNetCallback;
import com.sl.shenmian.lib.net.retrofit.RetrofitService;
import com.sl.shenmian.lib.net.rxjava.DataNetObserver;
import com.sl.shenmian.lib.net.url.NetApi;
import com.sl.shenmian.lib.ui.dialog.CustomDialog;
import com.sl.shenmian.lib.ui.dialog.ImageDialog;
import com.sl.shenmian.lib.ui.dialog.MenuDialog;
import com.sl.shenmian.lib.utils.StringUtils;
import com.sl.shenmian.lib.utils.SystemUtil;
import com.sl.shenmian.lib.utils.image.BitmapUtils;
import com.sl.shenmian.lib.utils.sharedpreferences.SpUtil;
import com.sl.shenmian.lib.utils.toast.ToastUtil;
import com.sl.shenmian.module.clearance.ClearanceActivity;
import com.sl.shenmian.module.commons.Constants;
import com.sl.shenmian.module.db.dao.DBDao;
import com.sl.shenmian.module.db.database.AppDatabase;
import com.sl.shenmian.module.db.entity.SealInfoEntity;
import com.sl.shenmian.module.main.pojo.Station;
import com.sl.shenmian.module.main.pojo.StationType;
import com.sl.shenmian.module.main.ui.adapter.SpinnerStationAdapter;
import com.sl.shenmian.module.offline.model.OfflineInfo;
import com.sl.shenmian.module.offline.model.SealType;
import com.sl.shenmian.module.seachcode.pojo.SeachCodeInfo;
import com.sl.shenmian.module.signature.SignatureActivity;
import com.sl.shenmian.module.wareinstorage.WareInStorageActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.sl.shenmian.lib.constant.ActivityConstant.ACTIVITY_SIGNAGURE_KEY;

/**
 * 门店入库解封
 */
public class StoreInStorageActivity extends BaseActivity {
    private String seal_code;

    /**
     * 站点
     */
    private List<Station> stations = new ArrayList<>();

    private RetrofitManage mRetrofitManage;
    private DataNetObserver mStationNetObserver;
    private DataNetObserver mSeachCodeNetObserver;
    private SpinnerStationAdapter spinnerStationAdapter;

    private int addrIndex = -1;

    private final int REQUEST_CODE_SIGNATURE = 11001;
    private final int REQUEST_CODE_PHOTO = 11002;

    private List<SeachCodeInfo.ImgBean> imagelist = new ArrayList<>();
    private List<SeachCodeInfo> seachCodeInfoList = new ArrayList<>();


    /**
     * 施封信息
     */
    @BindView(R.id.feng_code_tv) TextView feng_code_tv;
    @BindView(R.id.ware_out_storage_user_tv) TextView ware_out_storage_user_tv;
    @BindView(R.id.ware_out_storage_time_tv) TextView ware_out_storage_time_tv;
    @BindView(R.id.ware_out_storage_addr_tv) TextView ware_out_storage_addr_tv;
    @BindView(R.id.ware_out_car_number_tv) TextView ware_out_car_number_tv;
    @BindView(R.id.ware_out_remark_tv) TextView ware_out_remark_tv;
    @BindView(R.id.show_signature_list) TextView show_signature_list;
    @BindView(R.id.ware_out_storage_sig_list_view) GridLayout ware_out_storage_sig_list_view;

    /**
     * 解封信息
     */
    @BindView(R.id.ware_in_storage_user_tv) TextView ware_in_storage_user_tv;
    @BindView(R.id.ware_in_storage_addr_spinner) Spinner ware_in_storage_addr_spinner;
    @BindView(R.id.ware_in_storage_remark_tv) EditText ware_in_storage_remark_tv;
    @BindView(R.id.sig_add_btn) ImageView sig_add_btn;
    @BindView(R.id.clearance_sig_list_view) GridLayout clearance_sig_list_view;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        seal_code =  getIntent().getStringExtra("seal_code");
        setContentView(R.layout.layout_store_in_storage_view);

        initConfig();
        initData();
    }

    private void initConfig() {
        initBackToolbar(getString(R.string.store_in_storage));
        Button titleButton = getTitleButton();
        titleButton.setVisibility(View.VISIBLE);
        titleButton.setText(getString(R.string.upload));

        titleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(imagelist.size() == 0){
                    ToastUtil.show(StoreInStorageActivity.this,"请添加签名！");
                    return;
                }

                if(SystemUtil.isNetOk(StoreInStorageActivity.this)) {
                    showUploadConfimDialog();
                }else {
                    showNoFoundNetDialog();
                }
            }
        });
    }
    private CustomDialog noFoundNetDialog = null;
    private void showNoFoundNetDialog() {
        noFoundNetDialog = null;
        if (null == noFoundNetDialog) {
            noFoundNetDialog = new CustomDialog();
        }
        noFoundNetDialog.removeWindowTitle(true);
        noFoundNetDialog.setContentIconIsShow(true);
        noFoundNetDialog.setRightBtnIsShow(true);
        noFoundNetDialog.setDialogContentIcon(R.mipmap.fail);
        noFoundNetDialog.setDialogContentMsg(R.string.net_fail_login_fail_msg);
        noFoundNetDialog.setDialogLeftBtnText(R.string.ok);
        noFoundNetDialog.setDialogRightBtnText(R.string.local_save_data);
        noFoundNetDialog.setDialogTitleBtnOnClick(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismissnoFoundNetDialog();
            }
        });
        noFoundNetDialog.setDialogLeftBtnOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissnoFoundNetDialog();
            }
        });
        noFoundNetDialog.setDialogRightBtnOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitData();
                dismissnoFoundNetDialog();
                finish();
            }
        });
        noFoundNetDialog.show(getSupportFragmentManager(), "mainExitDialog");
    }

    private void dismissnoFoundNetDialog() {
        if (null != noFoundNetDialog) {
            noFoundNetDialog.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        dismissnoFoundNetDialog();
    }
    private void initData(){
        feng_code_tv.setText(seal_code);
        String account = SpUtil.getString(mContext, ConstantValues.UserInfo.KEY_USER_USERNAME, "");
        ware_in_storage_user_tv.setText(account);
        mDbDao = AppDatabase.getInstance().dbDao();
        loadStation();

        spinnerStationAdapter = new SpinnerStationAdapter(this,stations);
        ware_in_storage_addr_spinner.setAdapter(spinnerStationAdapter);
        ware_in_storage_addr_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addrIndex = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        loadSeachResultData();
    }

    private void updateStation(){
        spinnerStationAdapter.setData(stations);
        spinnerStationAdapter.notifyDataSetChanged();
    }

    private void loadStation(){
        mRetrofitManage = null;
        if (mRetrofitManage == null) {
            mRetrofitManage = new RetrofitManage();
        }
        if(null == mStationNetObserver) {
            mStationNetObserver = new DataNetObserver(mContext,new DataNetCallback() {
                @Override
                public void onError(Throwable e) {
                    loadOfflineAddress();
                }

                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onFailResponse(String dataJson, ServerResponseResult serverResponseResult) {
                    loadOfflineAddress();
                }

                @Override
                public void onOkResponse(String dataJson) {
                    stations = JSON.parseArray(dataJson,Station.class);
                    updateStation();
                    savleOffileAddress(dataJson);
                }
            });
        }

        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("stationType", StationType.STORE.name());
        RetrofitService service = mRetrofitManage.createService();
        String urlPath = NetApi.App.LOAD_STATION;
        Observable<Response<ResponseBody>> observable = service.postFormNet(urlPath,paramMap);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mStationNetObserver);
    }

    private void savleOffileAddress(String dataJson){
        SpUtil.putString(mContext, ConstantValues.OffLineData.StoreInStorage_address_local_key, dataJson);
    }
    private void loadOfflineAddress(){
        String data = SpUtil.getString(mContext,ConstantValues.OffLineData.StoreInStorage_address_local_key,"");
        if(null != data && data.length() > 0){
            stations = JSON.parseArray(data, Station.class);
            updateStation();
        }
    }

    private void loadSeachResultData(){
        seachCodeInfoList.clear();
        mRetrofitManage = null;
        if(null == mRetrofitManage) {
            mRetrofitManage = new RetrofitManage();
        }
        if(null == mSeachCodeNetObserver) {
            mSeachCodeNetObserver = new DataNetObserver(this,new DataNetCallback() {

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
        paramMap.put("labelCode",seal_code);
        RetrofitService service = mRetrofitManage.createService();
        String urlPath = NetApi.App.SEARCH_CODE;
        Observable<Response<ResponseBody>> observable = service.postFormNet(urlPath,paramMap);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mSeachCodeNetObserver);

    }

    private void setScanResult(){
        //seachCodeInfoList
        if(seachCodeInfoList.size() > 0){
            SeachCodeInfo searchCodeInfo = seachCodeInfoList.get(seachCodeInfoList.size() - 1);
            ware_out_storage_user_tv.setText(searchCodeInfo.getUsername());
            ware_out_storage_time_tv.setText(searchCodeInfo.getTime());
            ware_out_storage_addr_tv.setText(searchCodeInfo.getAddr());
            ware_out_car_number_tv.setText(searchCodeInfo.getLic());
            ware_out_remark_tv.setText(searchCodeInfo.getRemark());

            if (searchCodeInfo.getImg().size() > 0) {
                ware_out_storage_sig_list_view.removeAllViews();
                for (SeachCodeInfo.ImgBean image : searchCodeInfo.getImg()) {
                    ImageView imageView = new ImageView(mContext);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(75, 75));
                    imageView.setPadding(5, 5, 5, 5);
                    //imageView.setim
                    Glide.with(mContext)
                            .load(image.getThumbUrl())
                            .asBitmap()//只加载静态图片，如果是git图片则只加载第一帧。
                            .placeholder(R.mipmap.loading)
                            .error(R.mipmap.fail)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(imageView);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ImageDialog imageDialog = new ImageDialog();
                            imageDialog.setDialogTitleBtnOnClick(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    imageDialog.dismissAllowingStateLoss();
                                }
                            });
                            imageDialog.setImagUrl(image.getThumbUrl());
                            imageDialog.show(getSupportFragmentManager(),"image");
                        }
                    });

                    ware_out_storage_sig_list_view.addView(imageView);

                }
            }
        }
    }


    private CustomDialog dialog = null;
    private void showUploadConfimDialog() {
        showDialog("", "确定上传施封信息吗?", "", "");

    }


    @Override
    protected void dialogRightClick(AlertDialog alertDialog) {
        alertDialog.dismiss();
        isQuit = false;
    }

    @Override
    protected void dialogTitleRight(AlertDialog alertDialog) {
        alertDialog.dismiss();
        isQuit = false;
    }
    @Override
    protected void dialogLeftClick(AlertDialog alertDialog) {
        alertDialog.dismiss();
        if(isQuit){
            finish();
        }else {
            submitData();
        }
    }


    @OnClick({R.id.sig_add_btn,R.id.show_signature_list})
    void OnClick(View v){

        switch (v.getId()){
            case R.id.sig_add_btn:

                showSignatureMenuDialog();
                break;
            case R.id.show_signature_list:
                if(ware_out_storage_sig_list_view.getVisibility() == View.VISIBLE){
                    ware_out_storage_sig_list_view.setVisibility(View.GONE);
                }else {
                    ware_out_storage_sig_list_view.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void submitData(){

        OfflineInfo offlineInfo = new OfflineInfo();
        if(stations.size()>0) {
            offlineInfo.setAddressId(stations.get(addrIndex).getId());
            offlineInfo.setAddress(stations.get(addrIndex).getSiteName());
        }
        offlineInfo.setCoding(seal_code);
        offlineInfo.setLockedImei(SystemUtil.getImei(this));
        offlineInfo.setRemark(ware_in_storage_remark_tv.getText().toString().trim());
        offlineInfo.setUserAccount(SpUtil.getString(mContext, ConstantValues.UserInfo.KEY_USER_ACCOUNT, ""));
        if(imagelist.size() > 0){
            for(int i = 0; i< imagelist.size(); i++){
                if(i == 0){
                    offlineInfo.setImagePath1(imagelist.get(i).getThumbUrl());
                }
                if(i == 1){
                    offlineInfo.setImagePath2(imagelist.get(i).getThumbUrl());
                }
                if(i == 2){
                    offlineInfo.setImagePath3(imagelist.get(i).getThumbUrl());
                }
            }
        }

        if(SystemUtil.isNetOk(this)) {
            padlockDataSubmit(1, offlineInfo);
        }else {
            ToastUtil.show(mContext,"离线数据已保存到历史记录中");
            offlineInfo.setUploadingStae(0);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    saveData(offlineInfo);
                }
            }).start();
            finish();
        }
    }
    private RetrofitManage retrofitManage = new RetrofitManage();
    private DBDao mDbDao;
    /**
     * 解封数据上传
     */
    private void padlockDataSubmit(int type, OfflineInfo offlineInfo) {
        RetrofitService service = retrofitManage.createService();
        String pathUrl = NetApi.App.UNLOCK_INFO;

        String imagePath1 = offlineInfo.getImagePath1();
        //    Bitmap bitmap1 = BitmapFactory.decodeFile(imagePath1);
        String imagePath2 = offlineInfo.getImagePath2();
        // Bitmap bitmap2 = BitmapFactory.decodeFile(imagePath2);
        String imagePath3 = offlineInfo.getImagePath3();
        //  Bitmap bitmap3 = BitmapFactory.decodeFile(imagePath3);

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("loginCode", offlineInfo.getUserAccount());
        paramMap.put("logType", type);
        paramMap.put("unlockRemark", offlineInfo.getRemark());
        paramMap.put("labelCode", offlineInfo.getCoding());
        paramMap.put("unlockAddrId", offlineInfo.getAddressId());
        paramMap.put("unlockImei", offlineInfo.getLockedImei());

        ArrayList<MultipartBody.Part> parts = new ArrayList<>();

        if(null != imagePath1 && imagePath1.length() > 0) {
            final File file1 = new File(imagePath1);
            // 创建请求体，内容是文件
            RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
            MultipartBody.Part body1 = MultipartBody.Part.createFormData("file1", file1.getName(), requestFile1);
            parts.add(body1);
        }

        if(null != imagePath2 && imagePath2.length() > 0) {
            final File file2 = new File(imagePath2);
            // 创建请求体，内容是文件
            RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), file2);
            MultipartBody.Part body2 = MultipartBody.Part.createFormData("file2", file2.getName(), requestFile2);
            parts.add(body2);
        }

        if(null != imagePath3 && imagePath3.length() > 0) {
            final File file3 = new File(imagePath3);
            // 创建请求体，内容是文件
            RequestBody requestFile3 = RequestBody.create(MediaType.parse("multipart/form-data"), file3);
            MultipartBody.Part body3 = MultipartBody.Part.createFormData("file1", file3.getName(), requestFile3);
            parts.add(body3);
        }

        if(parts.size()<=0){
            // 创建请求体，内容是文件
            RequestBody requestFile3 = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            MultipartBody.Part body3 = MultipartBody.Part.createFormData("", null, requestFile3);
            // parts.add(body3);
        }

        Observable<Response<ResponseBody>> responseObservable = service.uplodas(pathUrl, paramMap, parts);
        Disposable subscribe = responseObservable
                .subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> responseBodyResponse) throws Exception {
                        int code = responseBodyResponse.code();
                        if (code == 200) {
                            ResponseBody body = responseBodyResponse.body();
                            assert body != null;
                            String string = body.string();
                            ServerResponseResult serverResponseResult = JSON.parseObject(string, ServerResponseResult.class);
                            if (serverResponseResult.isSuccess()) {
                                offlineInfo.setUploadingStae(1);
                                saveData(offlineInfo);
                                mHandler.sendEmptyMessage(upload_data_suc);
                            } else {
                                offlineInfo.setUploadingStae(2);
                                saveData(offlineInfo);
                                Message msg = mHandler.obtainMessage(upload_data_fail, serverResponseResult.getMessage());
                                mHandler.sendMessage(msg);
                            }
                            if(!serverResponseResult.getResultCode().equals("0")){
                                offlineInfo.setUploadingStae(2);
                                saveData(offlineInfo);
                                Message msg = mHandler.obtainMessage(upload_data_fail, serverResponseResult.getMessage());
                                mHandler.sendMessage(msg);
                            }

                        } else {

                            offlineInfo.setUploadingStae(2);
                            saveData(offlineInfo);
                            Message msg = mHandler.obtainMessage(upload_data_fail, responseBodyResponse.message());
                            mHandler.sendMessage(msg);
                        }


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        offlineInfo.setUploadingStae(2);
                        saveData(offlineInfo);
                        Message msg = mHandler.obtainMessage(upload_data_fail, throwable.getMessage());
                        mHandler.sendMessage(msg);

                    }
                });
    }

    private void saveData(OfflineInfo offlineInfo){

        SealInfoEntity sealInfoEntity = new SealInfoEntity();
        if(stations.size()>0) {
            sealInfoEntity.setAddress(stations.get(addrIndex).getSiteName());
            sealInfoEntity.setAddressId(stations.get(addrIndex).getId());
        }
        sealInfoEntity.setCarLicense(offlineInfo.getCarLicense());
        sealInfoEntity.setCoding(offlineInfo.getCoding());
        sealInfoEntity.setLockedImei(offlineInfo.getLockedImei());
        sealInfoEntity.setRemark(offlineInfo.getRemark());
        sealInfoEntity.setUploadingState(offlineInfo.getUploadingStae());
        sealInfoEntity.setSealType(SealType.shopDisassemble.getValue());
        sealInfoEntity.setUserName(SpUtil.getString(mContext, ConstantValues.UserInfo.KEY_USER_USERNAME, ""));
        sealInfoEntity.setUserAccount(offlineInfo.getUserAccount());
        sealInfoEntity.setTime(StringUtils.getCurrentTimeStr());

        mDbDao.insert(sealInfoEntity);
    }

    private MenuDialog menuDialog;
    private void showSignatureMenuDialog(){
        if(null != menuDialog){
            menuDialog.dismissAllowingStateLoss();
            menuDialog = null;
        }
        if (null == menuDialog) {
            menuDialog = new MenuDialog();
        }
        menuDialog.removeWindowTitle(true);
        menuDialog.setSignatureMenuOnClickListener(onMenuOnClickListener);
        menuDialog.setPhotoMenuOnClickListener(onMenuOnClickListener);
        menuDialog.show(getSupportFragmentManager(), "signature_menu_dialog");
    }

    private View.OnClickListener onMenuOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.signature_menu:
                    Intent intent = new Intent(StoreInStorageActivity.this, SignatureActivity.class);
                    startActivityForResult(intent,REQUEST_CODE_SIGNATURE);
                    menuDialog.dismissAllowingStateLoss();
                    break;
                case R.id.photo_menu:
                    Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //系统常量， 启动相机的关键
                    startActivityForResult(openCameraIntent, REQUEST_CODE_PHOTO);
                    menuDialog.dismissAllowingStateLoss();
                    break;
            }
        }
    };

    private String savePath;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        savePath = "";
        switch (requestCode) {
            case REQUEST_CODE_SIGNATURE:
                if (resultCode == RESULT_OK) {
                    savePath = data.getStringExtra(ACTIVITY_SIGNAGURE_KEY);
                    if(null != savePath) {
                        addShowSiglist(savePath);
                    }
                }
                break;
            case REQUEST_CODE_PHOTO:
                if ( resultCode == RESULT_OK) {
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    savePath = saveBitmap(bm);
                    if(null != savePath) {
                        addShowSiglist(savePath);
                    }
                }
                break;
        }


    }

    private String  saveBitmap(Bitmap bitmap){
        String filePath = Constants.LocalFile.IMAGE_PATH;
        String fileName = filePath+System.currentTimeMillis()+".jpg";
        bitmap = BitmapUtils.compressScale(bitmap);

        File directory = new File(filePath);
        if(!directory.exists()){
            directory.mkdirs();
        }
        File file = new File(fileName);
        try {
            BitmapUtils.saveBitmapToJPG(bitmap,file);
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.show(StoreInStorageActivity.this,getString(R.string.signature_save_error));
        }

        return fileName;
    }

    private void addShowSiglist(String path){

        Log.e(TAG, path);
        if(imagelist.size() < 3){
            clearance_sig_list_view.removeAllViews();
            clearance_sig_list_view.setVisibility(View.VISIBLE);
            sig_add_btn.setVisibility(View.VISIBLE);
            SeachCodeInfo.ImgBean imgBean = new SeachCodeInfo.ImgBean();
            imgBean.setThumbUrl(path);
            imagelist.add(imgBean);

            for (SeachCodeInfo.ImgBean image : imagelist) {
                ImageView imageView = new ImageView(mContext);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(75, 75));
                imageView.setPadding(5, 5, 5, 5);
                //imageView.setim
                Glide.with(mContext)
                        .load(Uri.fromFile( new File( image.getThumbUrl() )))
                        .asBitmap()//只加载静态图片，如果是git图片则只加载第一帧。
                        .placeholder(R.mipmap.loading)
                        .error(R.mipmap.fail)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageDialog imageDialog = new ImageDialog();
                        imageDialog.setDialogTitleBtnOnClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imageDialog.dismissAllowingStateLoss();
                            }
                        });
                        imageDialog.setImagUrl(image.getThumbUrl());
                        imageDialog.show(getSupportFragmentManager(),"image_src");
                    }
                });
                clearance_sig_list_view.addView(imageView);
                if(imagelist.size() == 3){
                    sig_add_btn.setVisibility(View.GONE);
                }
            }
        }else if(imagelist.size() == 3){
            sig_add_btn.setVisibility(View.GONE);
        }else {

            ToastUtil.show(this,"签名图片最多三张!");
        }
    }

    boolean isQuit;

    @Override
    public void onBackPressed() {
        isQuit = true;
        showDialog("", "是否退出?", "", "");
    }

    @Override
    protected void onFinish() {
        isQuit = true;
        showDialog("提示", "是否退出", "退出", "取消");
    }

    private final int upload_data_suc = 10001;
    private final int upload_data_fail = 10002;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case upload_data_suc:
                    ToastUtil.show(StoreInStorageActivity.this, "上传解封数据成功!");
                    finish();
                    break;
                case upload_data_fail:
                    String data = "";
                    if(null != msg.obj){
                        data = msg.obj.toString();
                    }
                    ToastUtil.show(StoreInStorageActivity.this, "上传解封数据失败!"+data+"\n已离线保存");
                    finish();
                    break;
            }
        }
    };
}
