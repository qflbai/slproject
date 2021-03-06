package com.sl.shenmian.lib.net.rxjava;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.sl.shenmian.lib.net.body.ServerResponseResult;
import com.sl.shenmian.lib.net.callback.DataNetCallback;
import com.sl.shenmian.lib.net.state.ServerResponseState;
import com.sl.shenmian.lib.utils.toast.ToastUtil;

import java.io.IOException;
import java.net.SocketTimeoutException;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * @author WenXian Bai
 * @Date: 2018/3/13.
 * @Description:
 */

public class DataNetObserver extends BaseObserver {
    /**
     * 回调接口
     */
    private DataNetCallback mNetCallback;
    /**
     * 上下文
     */
    private Context mContext;

    public DataNetObserver(Context context, DataNetCallback netCallback) {
        mNetCallback = netCallback;
        mContext = context;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (mIsNetRequesting) {
            d.dispose();
        } else {
            mNetCallback.onSubscribe(d);
            addNetManage(d);
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
                    try {
                        ServerResponseResult serverResponseResult = JSONObject.parseObject(jsonString, ServerResponseResult.class);
                        Object data = serverResponseResult.getData();
                        String json = JSONObject.toJSONString(data);
                        String resultCode = serverResponseResult.getResultCode();
                        if ("0".equals(resultCode)) {
                            mNetCallback.onOkResponse(json);
                        } else {

                            String stateMessage = ServerResponseState.getStateMessage(resultCode);
                            if (!stateMessage.isEmpty()) {
                                ToastUtil.show(mContext, stateMessage);
                                mNetCallback.onFailResponse(json, serverResponseResult);
                            } else {
                                mNetCallback.onFailResponse(json, serverResponseResult);
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    ToastUtil.show(mContext, "IO异常");
                    mNetCallback.onError(e);
                    e.printStackTrace();
                } catch (Exception e) {
                    ToastUtil.show(mContext, "请求异常");
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
        int code = httpException.code();
        if (code == 401) {
            ToastUtil.show(mContext, code + "登陆超时...");
            Intent intent = new Intent();
            intent.setAction("com.suntech.app.xiuzheng.launch.ui.LoginActivity");
            intent.addCategory("android.intent.category.DEFAULT");
            mContext.startActivity(intent);
        } else {
            ToastUtil.show(mContext, code + "错误");
            mNetCallback.onError(httpException);
        }

    }

    @Override
    public void onError(Throwable e) {
        mIsNetRequesting = false;
        if (e instanceof SocketTimeoutException) {
            ToastUtil.show(mContext, "网络连接超时");
        }
        mNetCallback.onError(e);
    }

    @Override
    public void onComplete() {

    }
}
