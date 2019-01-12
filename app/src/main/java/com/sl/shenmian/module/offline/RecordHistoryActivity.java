package com.sl.shenmian.module.offline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.sl.shenmian.R;
import com.sl.shenmian.lib.base.activity.AbsActivity;
import com.sl.shenmian.lib.base.activity.BaseActivity;
import com.sl.shenmian.lib.constant.ConstantValues;
import com.sl.shenmian.lib.net.RetrofitManage;
import com.sl.shenmian.lib.utils.sharedpreferences.SpUtil;
import com.sl.shenmian.module.db.dao.DBDao;
import com.sl.shenmian.module.db.database.AppDatabase;
import com.sl.shenmian.module.db.entity.SealInfoEntity;
import com.sl.shenmian.module.main.source.MainRepository;
import com.sl.shenmian.module.offline.adpater.OffLineAdapter;
import com.sl.shenmian.module.offline.model.OfflineInfo;
import com.sl.shenmian.module.offline.model.SealType;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RecordHistoryActivity extends BaseActivity{
    @BindView(R.id.lv_code)
    ListView mLvCode;
    private ArrayList<OfflineInfo> mOfflineInfos;
    private OffLineAdapter mOffLineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istory);
        initUI();
        initData();
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
        getIntentInfo();
    }

    private void getIntentInfo() {
        Intent intent = getIntent();
        if (intent != null) {
            SealType sealType = (SealType) intent.getSerializableExtra(ConstantValues.OfflineInfo.KEY_SEAL_TYPE);
            if (sealType == null) {
                return;
            }
            queryDB(sealType);
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
                            offlineInfo.setAddress(sealInfoEntity.getAddress());
                            offlineInfo.setCoding(sealInfoEntity.getCoding());
                            offlineInfo.setRemark(sealInfoEntity.getRemark());
                            offlineInfo.setTime(sealInfoEntity.getTime());
                            int uploadingStae = sealInfoEntity.getUploadingStae();
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
        if (isHaveData) {
            return;
        }

        Flowable<String> flowable = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> s) throws Exception {

                RetrofitManage retrofitManage = new RetrofitManage();
                for (OfflineInfo offlineInfo : mOfflineInfos) {


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

    private void dataUpload(){

    }
}
