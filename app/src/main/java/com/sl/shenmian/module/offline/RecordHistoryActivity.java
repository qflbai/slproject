package com.sl.shenmian.module.offline;

import android.content.Intent;
import android.os.Bundle;
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
import com.sl.shenmian.lib.utils.sharedpreferences.SpUtil;
import com.sl.shenmian.module.db.dao.DBDao;
import com.sl.shenmian.module.db.database.AppDatabase;
import com.sl.shenmian.module.db.entity.SealInfoEntity;
import com.sl.shenmian.module.offline.adpater.OffLineAdapter;
import com.sl.shenmian.module.offline.model.OfflineInfo;
import com.sl.shenmian.module.offline.model.SealType;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istory);
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
        Button titleButton = getTitleButton();
        titleButton.setVisibility(View.VISIBLE);
        titleButton.setText("一键上传");

        titleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });

        mOfflineInfos = new ArrayList<>();
        mOffLineAdapter = new OffLineAdapter(mContext, mOfflineInfos);
        mLvCode.setAdapter(mOffLineAdapter);

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
        String userAccount = SpUtil.getString(mContext, ConstantValues.UserInfo.KEY_USER_ACCOUNT, "");

        DBDao dbDao = AppDatabase.getInstance().dbDao();
        Single<List<SealInfoEntity>> observable = dbDao.quserOverdueAcency(userAccount, sealType.getValue());
        Disposable subscribe = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<SealInfoEntity>>() {
                    @Override
                    public void accept(List<SealInfoEntity> sealInfoEntities) throws Exception {
                        for (SealInfoEntity sealInfoEntity : sealInfoEntities) {
                            OfflineInfo offlineInfo = new OfflineInfo();
                            offlineInfo.setId(sealInfoEntity.getId());
                            offlineInfo.setAddress(sealInfoEntity.getAddress());
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
                            if (uploadingStae != 1) {
                                isHaveData = true;
                                offlineInfo.setUploadingStae(0);
                            } else {
                                offlineInfo.setUploadingStae(1);
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
        if (isHaveData) {
            return;
        }

        Flowable<String> flowable = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> s) throws Exception {

                RetrofitManage retrofitManage = new RetrofitManage();
                for (OfflineInfo offlineInfo : mOfflineInfos) {
                    switch (mSealType) {
                        case tongGuanPadlock:
                            padlockDataSubmit(0, offlineInfo);
                            break;
                        case houseEnterDisassemble:
                            disassembleDaaSubmit(0, offlineInfo);
                            break;
                        case houseOutPadlock:
                            padlockDataSubmit(1, offlineInfo);
                            break;
                        case shopDisassemble:
                            disassembleDaaSubmit(1, offlineInfo);
                            break;
                        default:
                            break;
                    }
                    s.onNext("0k");
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
                mOffLineAdapter.notifyDataSetChanged();
                mSubscription.request(10);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

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
        paramMap.put("lockedAddrId", offlineInfo.getAddress());
        paramMap.put("lockedImei", offlineInfo.getLockedImei());


        final File file1 = new File(imagePath1);
        final File file2 = new File(imagePath2);
        final File file3 = new File(imagePath3);

        // 创建请求体，内容是文件
        RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
        MultipartBody.Part body1 = MultipartBody.Part.createFormData("file1", file1.getName(), requestFile1);
        // 创建请求体，内容是文件
        RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), file2);
        MultipartBody.Part body2 = MultipartBody.Part.createFormData("file2", file2.getName(), requestFile2);
        // 创建请求体，内容是文件
        RequestBody requestFile3 = RequestBody.create(MediaType.parse("multipart/form-data"), file3);
        MultipartBody.Part body3 = MultipartBody.Part.createFormData("file1", file3.getName(), requestFile3);

        ArrayList<MultipartBody.Part> parts = new ArrayList<>();
        parts.add(body1);
        parts.add(body2);
        parts.add(body3);

        Observable<Response<ResponseBody>> responseObservable = service.uplodas(pathUrl, paramMap, parts);
        Disposable subscribe = responseObservable.observeOn(AndroidSchedulers.mainThread())
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
                            } else {
                                offlineInfo.setUploadingStae(0);
                            }
                        } else {
                            offlineInfo.setUploadingStae(0);
                        }

                        mDbDao.upDateUpLoadingState(offlineInfo.getId(), offlineInfo.getUploadingStae());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    /**
     * 解封数据上传
     */
    private void disassembleDaaSubmit(int type, OfflineInfo offlineInfo) {
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
        paramMap.put("unlockRemark", offlineInfo.getRemark());
        paramMap.put("labelCode", offlineInfo.getCoding());
        paramMap.put("unlockAddrId", offlineInfo.getAddress());
        paramMap.put("unlockImei", offlineInfo.getLockedImei());


        final File file1 = new File(imagePath1);
        final File file2 = new File(imagePath2);
        final File file3 = new File(imagePath3);

        // 创建请求体，内容是文件
        RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
        MultipartBody.Part body1 = MultipartBody.Part.createFormData("file1", file1.getName(), requestFile1);
        // 创建请求体，内容是文件
        RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), file2);
        MultipartBody.Part body2 = MultipartBody.Part.createFormData("file2", file2.getName(), requestFile2);
        // 创建请求体，内容是文件
        RequestBody requestFile3 = RequestBody.create(MediaType.parse("multipart/form-data"), file3);
        MultipartBody.Part body3 = MultipartBody.Part.createFormData("file1", file3.getName(), requestFile3);

        ArrayList<MultipartBody.Part> parts = new ArrayList<>();
        parts.add(body1);
        parts.add(body2);
        parts.add(body3);

        Observable<Response<ResponseBody>> responseObservable = service.uplodas(pathUrl, paramMap, parts);
        Disposable subscribe = responseObservable.observeOn(AndroidSchedulers.mainThread())
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
                            } else {
                                offlineInfo.setUploadingStae(0);
                            }
                        } else {
                            offlineInfo.setUploadingStae(0);
                        }
                        mDbDao.upDateUpLoadingState(offlineInfo.getId(), offlineInfo.getUploadingStae());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }
}
