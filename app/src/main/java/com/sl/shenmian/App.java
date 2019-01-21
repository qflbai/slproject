package com.sl.shenmian;

import android.os.StrictMode;

import com.sl.shenmian.lib.base.LibBaseApplication;

public class App extends LibBaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }
}

