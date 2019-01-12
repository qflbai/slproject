package com.sl.shenmian.lib.net;

import com.sl.shenmian.lib.net.clinet.NetClinet;
import com.sl.shenmian.lib.net.listener.DownloadListener;
import com.sl.shenmian.lib.net.retrofit.RetrofitService;
import com.sl.shenmian.lib.net.retrofit.converter.FastJsonConverterFactory;
import com.sl.shenmian.lib.net.url.NetBaseUrl;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @author WenXian Bai
 * @Date: 2018/3/13.
 * @Description:
 */

public class RetrofitManage {
    /**
     * 一般retrofit
     */
    private Retrofit retrofit;

    /**
     * 登陆retrorfit
     */
    private Retrofit loginRetrofit;

    /**
     * 下载retrorfit
     */
    private Retrofit downLoadRetrofit;

    Retrofit.Builder builder;

    private Retrofit.Builder createRetrofitBuilder() {
        if (builder == null) {
            builder = new Retrofit.Builder()
                    .client(NetClinet.getInstance())
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        }
        return builder;
    }

    /**
     * 获取Retrofit
     *
     * @param baseUrl
     * @return
     */
    private Retrofit createRetrofit(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(NetClinet.getInstance())
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /**
     * 获取RetrofitService
     *
     * @return
     */
    public RetrofitService createService() {
        // 获取服务器地址
        String baseUrl = NetBaseUrl.getBaseUrl();
        return createRetrofit(baseUrl).create(RetrofitService.class);
    }

    /**
     * 获取RetrofitService
     *
     * @param baseUrl ip地址
     * @return
     */
    public RetrofitService createService(String baseUrl) {
        return createRetrofit(baseUrl).create(RetrofitService.class);
    }

    /**
     * 获取Retrofit(仅限于登陆,即不需要token)
     *
     * @param baseUrl
     * @return
     */
    private Retrofit createLoginRetrofit(String baseUrl) {
        if (loginRetrofit == null) {
            loginRetrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(NetClinet.getLoginInstance())
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return loginRetrofit;
    }

    /**
     * 获取登陆RetrofitService(用于登陆)
     *
     * @return
     */
    public RetrofitService createLoginService() {
        // 获取服务器地址
        String baseUrl = NetBaseUrl.getBaseUrl();
        return createLoginRetrofit(baseUrl).create(RetrofitService.class);
    }

    /**
     * 获取Retrofit(下载)
     *
     * @param baseUrl
     * @return
     */
    private Retrofit createDownloadRetrofit(String baseUrl, DownloadListener downloadListener) {
        if (downLoadRetrofit == null) {
            downLoadRetrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(NetClinet.getDownloadInstance(downloadListener))
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return downLoadRetrofit;
    }

    /**
     * 获取下载RetrofitService(用于下载)
     *
     * @return
     */
    public RetrofitService createDownloadService(String baseUrl, DownloadListener downloadListener) {
        // 获取服务器地址
        //String baseUrl = NetBaseUrl.getBaseUrl();
        return createDownloadRetrofit(baseUrl, downloadListener).create(RetrofitService.class);
    }

    /**
     * 获取下载RetrofitService(用于下载)
     *
     * @return
     */
    public RetrofitService createDownloadService(DownloadListener downloadListener) {
        // 获取服务器地址
        String baseUrl = NetBaseUrl.getBaseUrl();
        return createDownloadRetrofit(baseUrl, downloadListener).create(RetrofitService.class);
    }

}
