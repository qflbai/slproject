package com.sl.shenmian.lib.net.clinet;

import com.sl.shenmian.lib.net.interceptor.DownloadInterceptor;
import com.sl.shenmian.lib.net.interceptor.LogInterceptor;
import com.sl.shenmian.lib.net.interceptor.TokenHeaderInterceptor;
import com.sl.shenmian.lib.net.listener.DownloadListener;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author WenXian Bai
 * @Date: 2018/3/13.
 * @Description:
 */

public class NetClinet {
    private static OkHttpClient okHttpClient = null;
    private static OkHttpClient okHttpLoginClient = null;
    private static OkHttpClient okHttpDownloadClient = null;
    /**
     * 网络读取超时时间
     */
    private static final long READ_TIME_OUT = 30;
    /*
     * 网络连接超时时间
     */
    private static final long CONNECT_TIME_OUT = 30;
    /**
     * 网络写入时间
     */
    private static final long WRITE_TIME_OUT = 30;
    /**
     * 时间单位（分钟）
     */
    private static TimeUnit timeUnit = TimeUnit.SECONDS;


    private NetClinet() {
    }

    /**
     * 适用于所有带token请求
     *
     * @return
     */
    public static synchronized OkHttpClient getInstance() {
        if (okHttpClient == null) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new LogInterceptor());
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIME_OUT, timeUnit)
                    .readTimeout(READ_TIME_OUT, timeUnit)
                    .writeTimeout(WRITE_TIME_OUT, timeUnit)
                    .addNetworkInterceptor(new TokenHeaderInterceptor())
                    .addNetworkInterceptor(logInterceptor)
                    .build();
        }
        return okHttpClient;
    }

    /**
     * 适用于不带token请求(登陆)
     *
     * @return
     */
    public static synchronized OkHttpClient getLoginInstance() {
        if (okHttpLoginClient == null) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new LogInterceptor());
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpLoginClient = new OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIME_OUT, timeUnit)
                    .readTimeout(READ_TIME_OUT, timeUnit)
                    .writeTimeout(WRITE_TIME_OUT, timeUnit)
                    .addNetworkInterceptor(logInterceptor)
                    .build();
        }
        return okHttpLoginClient;
    }

    /**
     * 适用于不带token请求
     *
     * @return
     */
    public static synchronized OkHttpClient getDownloadInstance(DownloadListener downloadListener) {
        if (okHttpDownloadClient == null) {
            okHttpDownloadClient = new OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIME_OUT, timeUnit)
                    .readTimeout(READ_TIME_OUT, timeUnit)
                    .writeTimeout(WRITE_TIME_OUT, timeUnit)
                    .addInterceptor(new DownloadInterceptor(downloadListener))
                    .build();
        }
        return okHttpDownloadClient;
    }


}
