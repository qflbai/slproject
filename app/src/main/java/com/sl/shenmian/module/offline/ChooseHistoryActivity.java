package com.sl.shenmian.module.offline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sl.shenmian.R;
import com.sl.shenmian.lib.base.activity.BaseActivity;
import com.sl.shenmian.lib.constant.ConstantValues;
import com.sl.shenmian.module.offline.model.SealType;

import butterknife.OnClick;

public class ChooseHistoryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_uploading);
        initUI();
    }

    private void initUI() {
        initBackToolbar("查看历史");
    }

    @OnClick({R.id.tv_1, R.id.tv_2, R.id.tv_3, R.id.tv_4})
    void onClick(View view) {
        SealType sealType = SealType.shopDisassemble;
        switch (view.getId()) {
            case R.id.tv_1:
                sealType = SealType.tongGuanPadlock;
                break;
            case R.id.tv_2:
                sealType = SealType.houseEnterDisassemble;
                break;
            case R.id.tv_3:
                sealType = SealType.houseOutPadlock;
                break;
            case R.id.tv_4:
                sealType = SealType.shopDisassemble;
                break;
            default:
                break;
        }
        Intent intent = new Intent(mContext, RecordHistoryActivity.class);
        intent.putExtra(ConstantValues.OfflineInfo.KEY_SEAL_TYPE,sealType);
        activityJumps(intent);
    }
}
