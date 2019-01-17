package com.sl.shenmian.lib.base.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;


public class ZbarActivity extends BaseActivity{

    private final static String DATA_NAME = "com.motorolasolutions.emdk.datawedge.data_string";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter inf = new IntentFilter();
        inf.addAction("zebra_scan_data");
        registerReceiver(broadcastReceiver,inf);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    /**
     * 响应数据
     * @param data
     */
    protected void onData(String data) {

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String data = intent.getStringExtra(DATA_NAME);
            onData(data);
        }
    };
}
