package com.sl.shenmian.module.offline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.sl.shenmian.R;
import com.sl.shenmian.lib.base.activity.BaseActivity;
import com.sl.shenmian.lib.constant.ConstantValues;
import com.sl.shenmian.lib.net.RetrofitManage;
import com.sl.shenmian.lib.net.body.ServerResponseResult;
import com.sl.shenmian.lib.net.retrofit.RetrofitService;
import com.sl.shenmian.lib.net.url.NetApi;
import com.sl.shenmian.lib.utils.log.LogUtil;
import com.sl.shenmian.lib.utils.sharedpreferences.SpUtil;
import com.sl.shenmian.lib.utils.toast.ToastUtil;
import com.sl.shenmian.module.db.dao.DBDao;
import com.sl.shenmian.module.db.database.AppDatabase;
import com.sl.shenmian.module.db.entity.SealInfoEntity;
import com.sl.shenmian.module.offline.adpater.OffLineAdapter;
import com.sl.shenmian.module.offline.model.OfflineInfo;
import com.sl.shenmian.module.offline.model.SealType;
import com.sl.shenmian.module.storeinstorage.StoreInStorageActivity;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class RecordHistoryActivity extends BaseActivity {
    @BindView(R.id.lv_code)
    ListView mLvCode;
    private ArrayList<OfflineInfo> mOfflineInfos;
    private OffLineAdapter mOffLineAdapter;
    private SealType mSealType;
    private String mUserAccount;
    private Flowable<String> flowable;
    private DBDao mDbDao;

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istory);
        title = getIntent().getStringExtra("title");
        initUI();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) {
            mSubscription.cancel();
        }
    }

    private void initUI() {
        initBackToolbar(title);
        Button titleButton = getTitleButton();
        titleButton.setVisibility(View.VISIBLE);
        titleButton.setBackgroundResource(R.drawable.shape_right_btn);
        titleButton.setText(getString(R.string.key_upload));

        titleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUploadConfimDialog();

            }
        });

        mOfflineInfos = new ArrayList<>();
        mOffLineAdapter = new OffLineAdapter(mContext, mOfflineInfos);
        mLvCode.setAdapter(mOffLineAdapter);

    }

    private void showUploadConfimDialog() {

        showDialog("", "确定所有离线信息都上传吗?", "", "");

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
        if (isQuit) {
            isQuit = false;
            alertDialog.dismiss();
            finish();
        } else {
            alertDialog.dismiss();
            submitData();
        }
    }

    private void initData() {
        mDbDao = AppDatabase.getInstance().dbDao();
        mUserAccount = SpUtil.getString(mContext, ConstantValues.UserInfo.KEY_USER_ACCOUNT, "");
        getIntentInfo();
    }

    private void getIntentInfo() {
        Intent intent = getIntent();
        if (intent != null) {
            mSealType = (SealType) intent.getSerializableExtra(ConstantValues.OfflineInfo.KEY_SEAL_TYPE);
            if (mSealType == null) {
                return;
            }
            queryDB(mSealType);
        }
    }


    private void queryDB(SealType sealType) {
        mOfflineInfos.clear();
        String userAccount = SpUtil.getString(mContext, ConstantValues.UserInfo.KEY_USER_ACCOUNT, "");

        DBDao dbDao = AppDatabase.getInstance().dbDao();
        Single<List<SealInfoEntity>> observable = dbDao.quserOverdueAcency(userAccount, sealType.getValue());
        Disposable subscribe = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<SealInfoEntity>>() {
                    @Override
                    public void accept(List<SealInfoEntity> sealInfoEntities) throws Exception {
                        Log.e(TAG, "data size:" + sealInfoEntities.size());
                        for (SealInfoEntity sealInfoEntity : sealInfoEntities) {
                            OfflineInfo offlineInfo = new OfflineInfo();
                            offlineInfo.setId(sealInfoEntity.getId());
                            offlineInfo.setAddress(sealInfoEntity.getAddress());
                            offlineInfo.setAddressId(sealInfoEntity.getAddressId());
                            offlineInfo.setCoding(sealInfoEntity.getCoding());
                            offlineInfo.setRemark(sealInfoEntity.getRemark());
                            offlineInfo.setTime(sealInfoEntity.getTime());
                            offlineInfo.setCarLicense(sealInfoEntity.getCarLicense());
                            offlineInfo.setLockedImei(sealInfoEntity.getLockedImei());
                            offlineInfo.setUserAccount(sealInfoEntity.getUserAccount());
                            offlineInfo.setImagePath1(sealInfoEntity.getImagePath1());
                            offlineInfo.setImagePath2(sealInfoEntity.getImagePath2());
                            offlineInfo.setImagePath3(sealInfoEntity.getImagePath3());
                            int uploadingStae = sealInfoEntity.getUploadingState();
                            offlineInfo.setUploadingStae(uploadingStae);
                            if (uploadingStae != 1) {
                                isHaveData = true;
                            }

                            mOfflineInfos.add(offlineInfo);
                        }
                        mOffLineAdapter.notifyDataSetChanged();
                    }
                });
    }

    private boolean isHaveData = false;
    private Subscription mSubscription;

    private void submitData() {
        if (mSealType == null) {
            return;
        }
        if (!isHaveData) {
            return;
        }

        Flowable<String> flowable = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> s) throws Exception {

                RetrofitManage retrofitManage = new RetrofitManage();
                for (OfflineInfo offlineInfo : mOfflineInfos) {
                    if (offlineInfo.getUploadingStae() != 1) {
                        switch (mSealType) {
                            case tongGuanPadlock:
                                //通关施封
                                padlockDataSubmit(0, offlineInfo);
                                break;
                            case houseEnterDisassemble:
                                //仓库入库解封
                                disassembleDaaSubmit(0, offlineInfo);
                                break;
                            case houseOutPadlock:
                                //仓库出库施封
                                padlockDataSubmit(1, offlineInfo);
                                break;
                            case shopDisassemble:
                                //门店入库解封
                                disassembleDaaSubmit(1, offlineInfo);
                                break;
                            default:
                                break;
                        }
                        s.onNext("0k");
                    }
                }
                s.onComplete();
            }
        }, BackpressureStrategy.ERROR);


        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                mSubscription = s;
                mSubscription.request(10);
            }

            @Override
            public void onNext(String o) {
                mSubscription.request(10);
                for (OfflineInfo offlineInfo : mOfflineInfos) {
                    LogUtil.d("tag", "getUploadingStae" + offlineInfo.getUploadingStae());
                }
                mOffLineAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {
                //queryDB(mSealType);
            }
        };
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    private RetrofitManage retrofitManage = new RetrofitManage();

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
        paramMap.put("lockedAddrId", offlineInfo.getAddressId());
        paramMap.put("lockedImei", offlineInfo.getLockedImei());


        ArrayList<MultipartBody.Part> parts = new ArrayList<>();

        if (null != imagePath1 && imagePath1.length() > 0) {
            final File file1 = new File(imagePath1);
            // 创建请求体，内容是文件
            RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
            MultipartBody.Part body1 = MultipartBody.Part.createFormData("file1", file1.getName(), requestFile1);
            parts.add(body1);
        } else {
            MultipartBody multipartBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file1", "")
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
        } else {
            MultipartBody multipartBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file2", "")
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
        } else {
            MultipartBody multipartBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file3", "")
                    .build();

            MultipartBody.Part part = multipartBody.part(0);
            parts.add(part);

        }


        Observable<Response<ResponseBody>> responseObservable = service.uplodas(pathUrl, paramMap, parts);
        Disposable subscribe = responseObservable
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
                                mHandle.sendEmptyMessage(upload_shi_data_suc);
                            } else {
                                offlineInfo.setUploadingStae(2);
                                Message msg = mHandle.obtainMessage(upload_shi_data_fail, serverResponseResult.getMessage());
                                mHandle.sendMessage(msg);
                            }
                        } else {
                            offlineInfo.setUploadingStae(2);
                            Message msg = mHandle.obtainMessage(upload_shi_data_fail, responseBodyResponse.message());
                            mHandle.sendMessage(msg);
                        }
                        mDbDao.upDateUpLoadingState(offlineInfo.getId(), offlineInfo.getUploadingStae());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        offlineInfo.setUploadingStae(2);
                        mDbDao.upDateUpLoadingState(offlineInfo.getId(), offlineInfo.getUploadingStae());
                        Message msg = mHandle.obtainMessage(upload_shi_data_fail, throwable.getMessage());
                        mHandle.sendMessage(msg);
                    }
                });
    }

    /**
     * 解封数据上传
     */
    private void disassembleDaaSubmit(int type, OfflineInfo offlineInfo) {
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

        if (null != imagePath1 && imagePath1.length() > 0) {
            final File file1 = new File(imagePath1);
            // 创建请求体，内容是文件
            RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
            MultipartBody.Part body1 = MultipartBody.Part.createFormData("file1", file1.getName(), requestFile1);
            parts.add(body1);
        } else {
            MultipartBody multipartBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file1", "")
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
        } else {
            MultipartBody multipartBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file2", "")
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
        } else {
            MultipartBody multipartBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file3", "")
                    .build();

            MultipartBody.Part part = multipartBody.part(0);
            parts.add(part);

        }


        Observable<Response<ResponseBody>> responseObservable = service.uplodas(pathUrl, paramMap, parts);
        Disposable subscribe = responseObservable
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
                                Log.e(TAG, "上传成功...");
                                Message msg = mHandle.obtainMessage(upload_data_suc, serverResponseResult.getMessage());
                                mHandle.sendMessage(msg);
                            } else {
                                offlineInfo.setUploadingStae(2);
                                Message msg = mHandle.obtainMessage(upload_data_fail, serverResponseResult.getMessage());
                                mHandle.sendMessage(msg);
                            }
                        } else {
                            offlineInfo.setUploadingStae(2);
                            //Log.e(TAG, "上传失败.." + responseBodyResponse.message());
                            Message msg = mHandle.obtainMessage(upload_data_fail, responseBodyResponse.message());
                            mHandle.sendMessage(msg);
                        }
                        mDbDao.upDateUpLoadingState(offlineInfo.getId(), offlineInfo.getUploadingStae());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Message msg = mHandle.obtainMessage(upload_data_fail, throwable.getMessage());
                        mHandle.sendMessage(msg);
                        offlineInfo.setUploadingStae(2);
                    }
                });
    }

    private final int upload_shi_data_suc = 10001;
    private final int upload_shi_data_fail = 10002;
    private final int upload_data_suc = 10003;
    private final int upload_data_fail = 10004;
    private Handler mHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case upload_data_suc:
                    ToastUtil.show(RecordHistoryActivity.this, "上传解封数据成功!");
                   // finish();
                    break;
                case upload_data_fail:
                    String data = "";
                    if(null != msg.obj){
                        data = msg.obj.toString();
                    }
                    ToastUtil.show(RecordHistoryActivity.this, "上传解封数据失败!"+data);
                    //finish();
                    break;
                case upload_shi_data_suc:
                    ToastUtil.show(RecordHistoryActivity.this, "上传施封数据成功!");
                    // finish();
                    break;
                case upload_shi_data_fail:
                    String shidata = "";
                    if(null != msg.obj){
                        shidata = msg.obj.toString();
                    }
                    ToastUtil.show(RecordHistoryActivity.this, "上传施封数据失败!"+shidata);
                    //finish();
                    break;
            }
        }
    };
}
