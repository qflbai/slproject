package com.sl.shenmian.lib.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * @author WenXian Bai
 * @Date: 2018/11/2.
 * @Description:
 */
public class LibBaseApplication extends Application {
    /**
     * 上下文对象
     */
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }


    public static Context getAPPContext() {
        return mContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
