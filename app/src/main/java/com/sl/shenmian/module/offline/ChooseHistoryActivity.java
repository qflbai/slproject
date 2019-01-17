package com.sl.shenmian.module.offline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.sl.shenmian.R;
import com.sl.shenmian.lib.base.activity.BaseActivity;
import com.sl.shenmian.lib.constant.ConstantValues;
import com.sl.shenmian.module.offline.model.SealType;

import butterknife.BindView;
import butterknife.OnClick;

public class ChooseHistoryActivity extends BaseActivity {

    @BindView(R.id.tv_1)
    RelativeLayout tv_1;
    @BindView(R.id.tv_2)
    RelativeLayout tv_2;
    @BindView(R.id.tv_3)
    RelativeLayout tv_3;
    @BindView(R.id.tv_4)
    RelativeLayout tv_4;
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
        String title = "";
        switch (view.getId()) {
            case R.id.tv_1:
                sealType = SealType.tongGuanPadlock;
                title = getString(R.string.clearance);
                break;
            case R.id.tv_2:
                sealType = SealType.houseEnterDisassemble;
                title = getString(R.string.ware_in_storage);
                break;
            case R.id.tv_3:
                sealType = SealType.houseOutPadlock;
                title = getString(R.string.ware_out_storage);
                break;
            case R.id.tv_4:
                sealType = SealType.shopDisassemble;
                title = getString(R.string.store_in_storage);
                break;
            default:
                break;
        }
        Intent intent = new Intent(mContext, RecordHistoryActivity.class);
        intent.putExtra("title",title);
        intent.putExtra(ConstantValues.OfflineInfo.KEY_SEAL_TYPE,sealType);
        activityJumps(intent);
    }
}
