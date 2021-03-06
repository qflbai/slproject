package com.sl.shenmian.module.wareoutstorage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.sl.shenmian.module.main.pojo.CarLic;
import com.sl.shenmian.module.main.pojo.Station;
import com.sl.shenmian.module.main.pojo.StationType;
import com.sl.shenmian.module.main.ui.adapter.SpinnerCarlicAdapter;
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
 * 仓库出库施封
 */
public class WareOutStorageActivity extends BaseActivity {


    @BindView(R.id.feng_code_tv)
    TextView feng_code_tv;
    @BindView(R.id.clearance_user_tv)
    TextView clearance_user_tv;
    @BindView(R.id.clearance_addr_spinner)
    Spinner clearance_addr_spinner;
    @BindView(R.id.clearance_car_number_spinner)
    Spinner clearance_car_number_spinner;
    @BindView(R.id.remark_ed)
    TextView remark_ed;
    @BindView(R.id.sig_add_btn)
    ImageView sig_add_btn;
    @BindView(R.id.sig_list_view)
    GridLayout sig_list_view;

    private List<SeachCodeInfo.ImgBean> imagelist = new ArrayList<>();

    private String seal_code;
    private List<CarLic> carLics = new ArrayList<>();
    private List<Station> stations = new ArrayList<>();

    private RetrofitManage mRetrofitManage;
    private DataNetObserver mCarLicNetObserver;
    private DataNetObserver mStationNetObserver;

    private SpinnerCarlicAdapter spinnerCarlicAdapter;
    private SpinnerStationAdapter spinnerStationAdapter;

    private final int REQUEST_CODE_SIGNATURE = 11001;
    private final int REQUEST_CODE_PHOTO = 11002;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ware_out_storage_view);
        seal_code = getIntent().getStringExtra("seal_code");
        initConfig();
        loadCarLic();
        loadStation();
        initData();
    }

    private void initConfig() {
        initBackToolbar(getString(R.string.ware_out_storage));
        Button titleButton = getTitleButton();
        titleButton.setVisibility(View.VISIBLE);
        titleButton.setText(getString(R.string.upload));

        titleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SystemUtil.isNetOk(WareOutStorageActivity.this)) {
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


    private void initData() {
        feng_code_tv.setText(seal_code);
        String account = SpUtil.getString(mContext, ConstantValues.UserInfo.KEY_USER_ACCOUNT, "");
        clearance_user_tv.setText(account);
        spinnerCarlicAdapter = new SpinnerCarlicAdapter(this, carLics);
        spinnerStationAdapter = new SpinnerStationAdapter(this, stations);
        mDbDao = AppDatabase.getInstance().dbDao();

        clearance_addr_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addrIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        clearance_car_number_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carnumberIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        clearance_car_number_spinner.setAdapter(spinnerCarlicAdapter);
        clearance_addr_spinner.setAdapter(spinnerStationAdapter);
    }

    private void updateCarlic() {
        spinnerCarlicAdapter.setData(carLics);
        spinnerCarlicAdapter.notifyDataSetChanged();
    }

    private void updateStation() {
        spinnerStationAdapter.setData(stations);
        spinnerStationAdapter.notifyDataSetChanged();
    }

    int addrIndex = 0;
    int carnumberIndex = 0;

    @OnClick({R.id.sig_add_btn})
    void OnClick(View v) {

        switch (v.getId()) {
            case R.id.sig_add_btn:

                showSignatureMenuDialog();
                break;
        }
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
                    loadOfflineCarLic();
                }

                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onFailResponse(String dataJson, ServerResponseResult serverResponseResult) {
                    loadOfflineCarLic();
                }

                @Override
                public void onOkResponse(String dataJson) {
                    carLics = JSON.parseArray(dataJson, CarLic.class);
                    updateCarlic();
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
    private void loadOfflineCarLic(){
        String data = SpUtil.getString(mContext,ConstantValues.OffLineData.WareStorage_car_local_key,"");
        if(null != data && data.length() > 0){
            carLics = JSON.parseArray(data, CarLic.class);
            updateCarlic();
        }
    }
    private void loadStation() {
        mRetrofitManage = null;
        if (mRetrofitManage == null) {
            mRetrofitManage = new RetrofitManage();
        }
        if (null == mStationNetObserver) {
            mStationNetObserver = new DataNetObserver(mContext, new DataNetCallback() {
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
                    stations = JSON.parseArray(dataJson, Station.class);
                    updateStation();
                    savleOffileAddress(dataJson);
                }
            });
        }

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("stationType", StationType.WAREHOUSE.name());
        RetrofitService service = mRetrofitManage.createService();
        String urlPath = NetApi.App.LOAD_STATION;
        Observable<Response<ResponseBody>> observable = service.postFormNet(urlPath, paramMap);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mStationNetObserver);
    }

    private void savleOffileAddress(String dataJson){
        SpUtil.putString(mContext, ConstantValues.OffLineData.WareStorage_address_local_key, dataJson);
    }
    private void loadOfflineAddress(){
        String data = SpUtil.getString(mContext,ConstantValues.OffLineData.WareStorage_address_local_key,"");
        if(null != data && data.length() > 0){
            stations = JSON.parseArray(data, Station.class);
            updateStation();
        }
    }

    private RetrofitManage retrofitManage = new RetrofitManage();
    private DBDao mDbDao;

    private void submitData() {

        OfflineInfo offlineInfo = new OfflineInfo();
        if(stations.size()>0) {
            offlineInfo.setAddress(stations.get(addrIndex).getId());
        }
        offlineInfo.setCarLicense(carLics.get(carnumberIndex).getCarLic());
        offlineInfo.setCoding(seal_code);
        offlineInfo.setLockedImei(SystemUtil.getImei(this));
        offlineInfo.setRemark(remark_ed.getText().toString().trim());
        offlineInfo.setUserAccount(SpUtil.getString(mContext, ConstantValues.UserInfo.KEY_USER_ACCOUNT, ""));
        if (imagelist.size() > 0) {
            for (int i = 0; i < imagelist.size(); i++) {
                if (i == 0) {
                    offlineInfo.setImagePath1(imagelist.get(i).getThumbUrl());
                }
                if (i == 1) {
                    offlineInfo.setImagePath2(imagelist.get(i).getThumbUrl());
                }
                if (i == 2) {
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


    private void saveData(OfflineInfo offlineInfo) {

        SealInfoEntity sealInfoEntity = new SealInfoEntity();
        if(stations.size()>0) {
            sealInfoEntity.setAddress(stations.get(addrIndex).getSiteName());
            sealInfoEntity.setAddressId(stations.get(addrIndex).getId());
        }
        sealInfoEntity.setCarLicense(carLics.get(carnumberIndex).getCarLic());
        sealInfoEntity.setCarLicenseId(carLics.get(carnumberIndex).getId());
        sealInfoEntity.setCoding(offlineInfo.getCoding());
        sealInfoEntity.setLockedImei(offlineInfo.getLockedImei());
        sealInfoEntity.setRemark(offlineInfo.getRemark());
        sealInfoEntity.setUploadingState(offlineInfo.getUploadingStae());
        sealInfoEntity.setSealType(SealType.houseOutPadlock.getValue());
        sealInfoEntity.setUserName(SpUtil.getString(mContext, ConstantValues.UserInfo.KEY_USER_USERNAME, ""));
        sealInfoEntity.setUserAccount(offlineInfo.getUserAccount());
        sealInfoEntity.setTime(StringUtils.getCurrentTimeStr());

        sealInfoEntity.setImagePath1(offlineInfo.getImagePath1());
        sealInfoEntity.setImagePath2(offlineInfo.getImagePath2());
        sealInfoEntity.setImagePath3(offlineInfo.getImagePath3());
        mDbDao.insert(sealInfoEntity);
    }

    private CustomDialog dialog = null;

    private void showUploadConfimDialog() {

        showDialog("", "确定上传解封信息吗?", "", "");

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
        if (isQuit) {
            isQuit = false;
            finish();
        } else {
            submitData();
        }
    }

    /**
     * 施封数据上传
     */
    private void padlockDataSubmit(int type, OfflineInfo offlineInfo) {
        RetrofitService service = retrofitManage.createService();
        String pathUrl = NetApi.App.PADLOCK_INFO;

        String imagePath1 = offlineInfo.getImagePath1();
        //    Bitmap bitmap1 = BitmapFactory.decodeFile(imagePath1);
        String imagePath2 = offlineInfo.getImagePath2();
        // Bitmap bitmap2 = BitmapFactory.decodeFile(imagePath2);
        String imagePath3 = offlineInfo.getImagePath3();
        //  Bitmap bitmap3 = BitmapFactory.decodeFile(imagePath3);

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("loginCode", offlineInfo.getUserAccount());
        paramMap.put("logType", type);
        paramMap.put("carLicense", offlineInfo.getCarLicense());
        paramMap.put("lockedRemark", offlineInfo.getRemark());
        paramMap.put("labelCode", offlineInfo.getCoding());
        paramMap.put("lockedAddrId", offlineInfo.getAddress());
        paramMap.put("lockedImei", offlineInfo.getLockedImei());

        ArrayList<MultipartBody.Part> parts = new ArrayList<>();

        if (null != imagePath1 && imagePath1.length() > 0) {
            final File file1 = new File(imagePath1);
            // 创建请求体，内容是文件
            RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
            MultipartBody.Part body1 = MultipartBody.Part.createFormData("file1", file1.getName(), requestFile1);
            parts.add(body1);
        }else {
            MultipartBody multipartBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file1","")
                    .build();

            MultipartBody.Part part = multipartBody.part(0);
            parts.add(part);

        }

        if (null != imagePath2 && imagePath2.length() > 0) {
            final File file2 = new File(imagePath2);
            // 创建请求体，内容是文件
            RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), file2);
            MultipartBody.Part body2 = MultipartBody.Part.createFormData("file2", file2.getName(), requestFile2);
            parts.add(body2);
        }else {
            MultipartBody multipartBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file2","")
                    .build();

            MultipartBody.Part part = multipartBody.part(0);
            parts.add(part);
        }

        if (null != imagePath3 && imagePath3.length() > 0) {
            final File file3 = new File(imagePath3);
            // 创建请求体，内容是文件
            RequestBody requestFile3 = RequestBody.create(MediaType.parse("multipart/form-data"), file3);
            MultipartBody.Part body3 = MultipartBody.Part.createFormData("file1", file3.getName(), requestFile3);
            parts.add(body3);
        }else {
            MultipartBody multipartBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file3","")
                    .build();

            MultipartBody.Part part = multipartBody.part(0);
            parts.add(part);

        }


        Observable<Response<ResponseBody>> responseObservable = service.uplodas(pathUrl, paramMap, parts);
        Disposable subscribe = responseObservable.subscribeOn(Schedulers.io())
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

                        } else {
                            offlineInfo.setUploadingStae(2);
                            saveData(offlineInfo);
                            mHandler.sendEmptyMessage(upload_data_fail);
                        }
                        finish();
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

    private MenuDialog menuDialog;

    private void showSignatureMenuDialog() {
        if (null != menuDialog) {
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
            switch (v.getId()) {
                case R.id.signature_menu:
                    Intent intent = new Intent(WareOutStorageActivity.this, SignatureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SIGNATURE);
                    menuDialog.dismissAllowingStateLoss();
                    break;
                case R.id.photo_menu:
                    takePhoto();
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
                    if (null != savePath) {
                        addShowSiglist(savePath);
                    }
                }
                break;
            case TAKE_PHOTO:
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(mContext, "取消了拍照", Toast.LENGTH_LONG).show();
                    return;
                }

                if (resultCode == RESULT_OK) {
                    addShowSiglist(mImageFile.getPath());
                    // bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
                    galleryAddPic(mImageUriFromFile);
                }

                break;
        }


    }

    private String saveBitmap(Bitmap bitmap) {
        String filePath = Constants.LocalFile.IMAGE_PATH;
        String fileName = filePath + System.currentTimeMillis() + ".jpg";
        bitmap = BitmapUtils.compressScale(bitmap);

        File directory = new File(filePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(fileName);
        try {
            BitmapUtils.saveBitmapToJPG(bitmap, file);
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.show(WareOutStorageActivity.this, getString(R.string.signature_save_error));
        }

        return fileName;
    }

    private void addShowSiglist(String path) {

        Log.e(TAG, path);
        if (imagelist.size() < 3) {
            sig_list_view.removeAllViews();
            sig_list_view.setVisibility(View.VISIBLE);
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
                        .load(Uri.fromFile(new File(image.getThumbUrl())))
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
                        imageDialog.show(getSupportFragmentManager(), "image_src");
                    }
                });
                sig_list_view.addView(imageView);
                if (imagelist.size() == 3) {
                    sig_add_btn.setVisibility(View.GONE);
                }
            }
        } else if (imagelist.size() == 3) {
            sig_add_btn.setVisibility(View.GONE);
        } else {

            ToastUtil.show(this, "签名图片最多三张!");
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
                    ToastUtil.show(WareOutStorageActivity.this, "上传施封数据成功!");
                    finish();
                    break;
                case upload_data_fail:
                    String data = "";
                    if(null != msg.obj){
                        data = msg.obj.toString();
                    }
                    ToastUtil.show(WareOutStorageActivity.this, "上传施封数据失败!"+data+"\n已离线保存");
                    finish();
                    break;
            }
        }
    };

    private static final int CHOOSE_PHOTO = 385;
    private static final int TAKE_PHOTO = 189;
    private static final String FILE_PROVIDER_AUTHORITY = "com.sl.shenmian.fileprovider";

    private Uri mImageUri;
    private Uri mImageUriFromFile;
    private File mImageFile;

    /**
     * 拍照
     */
    private void takePhoto() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//打开相机的Intent
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {//这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
            mImageFile = createImageFile();//创建用来保存照片的文件
            mImageUriFromFile = Uri.fromFile(mImageFile);

            if (mImageFile != null) {
                mImageUri = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    /*7.0以上要通过FileProvider将File转化为Uri*/
                    mImageUri = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, mImageFile);
                } else {
                    /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
                    mImageUri = Uri.fromFile(mImageFile);
                }
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);//将用于输出的文件Uri传递给相机
                startActivityForResult(takePhotoIntent, TAKE_PHOTO);//打开相机
            }
        }
    }

    /**
     * 创建用来存储图片的文件，以时间来命名就不会产生命名冲突
     *
     * @return 创建的图片文件
     */
    private File createImageFile() {
        String filePath = Constants.LocalFile.IMAGE_PATH;
        String fileName = filePath + System.currentTimeMillis() + ".jpg";

        File directory = new File(filePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(fileName);
        return file;
    }


    /**
     * 将拍的照片添加到相册
     *
     * @param uri 拍的照片的Uri
     */
    private void galleryAddPic(Uri uri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
        sendBroadcast(mediaScanIntent);
    }

}
