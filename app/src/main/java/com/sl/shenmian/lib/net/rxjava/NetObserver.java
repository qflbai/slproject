package com.sl.shenmian.lib.net.rxjava;

import com.sl.shenmian.lib.base.repository.BaseRepository;
import com.sl.shenmian.lib.net.callback.NetCallback;

import java.io.IOException;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * @author WenXian Bai
 * @Date: 2018/3/13.
 * @Description:
 */

public class NetObserver extends BaseObserver {
    /**
     * 回调接口
     */
    private NetCallback mNetCallback;
    private BaseRepository mBaseRepository;
    public NetObserver( NetCallback netCallback) {
        mNetCallback = netCallback;

    }

    public NetObserver(BaseRepository baseRepository, NetCallback netCallback) {
        mNetCallback = netCallback;
        mBaseRepository =baseRepository;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (mIsNetRequesting) {
            d.dispose();
        } else {
            mNetCallback.onSubscribe(d);
            addNetManage(d);
        }
        if(mBaseRepository!=null){
            mBaseRepository.addDisposable(d);
        }
        mIsNetRequesting = true;
    }

    @Override
    public void onNext(Response<ResponseBody> response) {
        mIsNetRequesting = false;
        boolean successful = response.isSuccessful();
        if (successful) {
            int code = response.code();
            if (code == 200) {
                try {
                    String jsonString = response.body().string();
                    mNetCallback.onResponse(jsonString);
                } catch (IOException e) {
                    mNetCallback.onError(e);
                    e.printStackTrace();
                }
            } else {
                netError(response);
            }
        } else {
            netError(response);
        }
    }

    /**
     * 网络异常
     *
     * @param response
     */
    private void netError(Response<ResponseBody> response) {
        HttpException httpException = new HttpException(response);
        mNetCallback.onError(httpException);
    }

    @Override
    public void onError(Throwable e) {
        mIsNetRequesting = false;

        mNetCallback.onError(e);
    }

    @Override
    public void onComplete() {
        mIsNetRequesting = false;
    }
}
