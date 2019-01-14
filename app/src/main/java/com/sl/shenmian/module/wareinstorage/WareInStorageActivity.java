package com.sl.shenmian.module.wareinstorage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
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
import com.sl.shenmian.lib.utils.image.BitmapUtils;
import com.sl.shenmian.lib.utils.sharedpreferences.SpUtil;
import com.sl.shenmian.lib.utils.toast.ToastUtil;
import com.sl.shenmian.module.clearance.ClearanceActivity;
import com.sl.shenmian.module.commons.Constants;
import com.sl.shenmian.module.main.pojo.Station;
import com.sl.shenmian.module.main.pojo.StationType;
import com.sl.shenmian.module.main.ui.adapter.SpinnerStationAdapter;
import com.sl.shenmian.module.seachcode.pojo.SeachCodeInfo;
import com.sl.shenmian.module.seachcode.ui.adapter.SeachCodeAdapter;
import com.sl.shenmian.module.signature.SignatureActivity;

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
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.sl.shenmian.lib.constant.ActivityConstant.ACTIVITY_SIGNAGURE_KEY;

/**
 * 仓库出库解封
 */
public class WareInStorageActivity extends BaseActivity {

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


    @BindView(R.id.feng_code_tv) TextView feng_code_tv;
    /**
     * 施封信息
     */
    @BindView(R.id.ware_in_storage_user_tv) TextView ware_in_storage_user_tv;
    @BindView(R.id.ware_in_storage_addr_spinner) Spinner ware_in_storage_addr_spinner;
    @BindView(R.id.ware_in_storage_remark_tv) TextView ware_in_storage_remark_tv;
    @BindView(R.id.sig_add_btn) ImageView sig_add_btn;
    @BindView(R.id.sig_list_view) GridLayout sig_list_view;

    /**
     * 解封信息
     */
    @BindView(R.id.clearance_user_tv) TextView clearance_user_tv;
    @BindView(R.id.clearance_time_tv) TextView clearance_time_tv;
    @BindView(R.id.clearance_addr_tv) TextView clearance_addr_tv;
    @BindView(R.id.car_number_tv) TextView car_number_tv;
    @BindView(R.id.remark_tv) TextView remark_tv;
    @BindView(R.id.clearance_sig_list_view) GridLayout clearance_sig_list_view;
    @BindView(R.id.show_signature_list) TextView show_signature_list;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        seal_code =  getIntent().getStringExtra("seal_code");
        setContentView(R.layout.layout_ware_in_storage_view);

        initConfig();
        initData();
    }

    private void initConfig() {
        initBackToolbar(getString(R.string.ware_in_storage));
        Toolbar toolbar = getToolbar();
        toolbar.setNavigationIcon(R.mipmap.ic_title_back);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button titleButton = getTitleButton();
        titleButton.setVisibility(View.VISIBLE);
        titleButton.setText(getString(R.string.upload));

        titleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUploadConfimDialog();
            }
        });
    }

    private void initData(){
        feng_code_tv.setText(seal_code);
        String account = SpUtil.getString(mContext, ConstantValues.UserInfo.KEY_USER_ACCOUNT, "");
        ware_in_storage_user_tv.setText(account);

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

                }

                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onFailResponse(String dataJson, ServerResponseResult serverResponseResult) {

                }

                @Override
                public void onOkResponse(String dataJson) {
                    stations = JSON.parseArray(dataJson,Station.class);
                    updateStation();
                }
            });
        }

        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("stationType", StationType.WAREHOUSE.name());
        RetrofitService service = mRetrofitManage.createService();
        String urlPath = NetApi.App.LOAD_STATION;
        Observable<Response<ResponseBody>> observable = service.postFormNet(urlPath,paramMap);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mStationNetObserver);
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
            clearance_user_tv.setText(searchCodeInfo.getUsername());
            clearance_addr_tv.setText(searchCodeInfo.getAddr());
            car_number_tv.setText(searchCodeInfo.getLic());
            remark_tv.setText(searchCodeInfo.getRemark());
            clearance_time_tv.setText(searchCodeInfo.getTime());

            if (searchCodeInfo.getImg().size() > 0) {
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
                            imageDialog.setImagUrl(image.getUrl());
                            imageDialog.show(getSupportFragmentManager(),"image");
                        }
                    });

                }
            }
        }
    }


    private CustomDialog dialog = null;
    private void showUploadConfimDialog() {
        if (null == dialog) {
            dialog = new CustomDialog();
        }
        dialog.removeWindowTitle(true);
        dialog.setContentIconIsShow(false);
        dialog.setDialogContentMsg(R.string.confim_upload_clearance_dialog_tips);
        dialog.setDialogLeftBtnText(R.string.ok);
        dialog.setDialogRightBtnText(R.string.cancel);
        dialog.setDialogTitleBtnOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismissAllowingStateLoss();
            }
        });
        dialog.setDialogLeftBtnOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // submitData();
                dialog.dismissAllowingStateLoss();
            }
        });
        dialog.setDialogRightBtnOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismissAllowingStateLoss();
            }
        });
        dialog.show(getSupportFragmentManager(), "calearnce_upload_dialog");
    }

    private void dismiss() {
        if (null != dialog) {
            dialog.dismiss();
        }
    }

    @OnClick({R.id.sig_add_btn,R.id.show_signature_list})
    void OnClick(View v){

        switch (v.getId()){
            case R.id.sig_add_btn:

                showSignatureMenuDialog();
                break;
            case R.id.show_signature_list:
                    if(clearance_sig_list_view.getVisibility() == View.VISIBLE){
                        clearance_sig_list_view.setVisibility(View.GONE);
                    }else {
                        clearance_sig_list_view.setVisibility(View.VISIBLE);
                    }
                break;
        }
    }

    private MenuDialog menuDialog;
    private void showSignatureMenuDialog(){
        menuDialog = null;
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
                    Intent intent = new Intent(WareInStorageActivity.this, SignatureActivity.class);
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
            ToastUtil.show(WareInStorageActivity.this,getString(R.string.signature_save_error));
        }

        return fileName;
    }

    private void addShowSiglist(String path){

        Log.e(TAG, path);
        if(imagelist.size() < 3){
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
                        imageDialog.setImagUrl(image.getUrl());
                        imageDialog.show(getSupportFragmentManager(),"image_src");
                    }
                });
                sig_list_view.addView(imageView);
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
}
