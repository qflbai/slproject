package com.sl.shenmian.lib.base.fragment;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;


import com.sl.shenmian.lib.base.viewmodel.BaseViewModel;
import com.sl.shenmian.lib.event.LiveBus;
import com.sl.shenmian.lib.utils.TUtil;

import java.util.ArrayList;
import java.util.List;



/**
 * @author WenXian Bai
 * @Date: 2018/11/2.
 * @Description:
 */
public abstract class AbsFragment<T extends BaseViewModel> extends BaseFragment {

    protected T mViewModel;

    protected Object mStateEventKey;

    protected String mStateEventTag;

    private List<Object> events = new ArrayList<>();

    @Override
    public void initView(Bundle state) {
        mViewModel = VMProviders(this, (Class<T>) TUtil.getInstance(this, 0));
        if (null != mViewModel) {
            dataObserver();
            mStateEventKey = getStateEventKey();
            mStateEventTag = getStateEventTag();
            events.add(new StringBuilder((String) mStateEventKey).append(mStateEventTag).toString());

        }
    }

    /**
     * ViewPager +fragment tag
     *
     * @return
     */
    protected String getStateEventTag() {
        return "";
    }

    /**
     * get state page event key
     *
     * @return
     */
    protected abstract Object getStateEventKey();

    /**
     * create ViewModelProviders
     *
     * @return ViewModel
     */
    protected <T extends ViewModel> T VMProviders(BaseFragment fragment, @NonNull Class<T> modelClass) {
        return ViewModelProviders.of(fragment).get(modelClass);

    }

    protected void dataObserver() {

    }

    protected <T> MutableLiveData<T> registerObserver(Object eventKey, Class<T> tClass) {

        return registerObserver(eventKey, null, tClass);
    }

    protected <T> MutableLiveData<T> registerObserver(Object eventKey, String tag, Class<T> tClass) {
        String event;
        if (TextUtils.isEmpty(tag)) {
            event = (String) eventKey;
        } else {
            event = eventKey + tag;
        }
        events.add(event);
        return LiveBus.getDefault().subscribe(eventKey, tag, tClass);
    }


    @Override
    protected void onStateRefresh() {
        showLoading();
    }


    /**
     * 获取网络数据
     */
    protected void getRemoteData() {

    }

    protected void showError() {

    }


    protected void showSuccess() {

    }

    protected void showLoading() {

    }


    protected Observer observer = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String state) {
            if (!TextUtils.isEmpty(state)) {

            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (events != null && events.size() > 0) {
            for (int i = 0; i < events.size(); i++) {
                LiveBus.getDefault().clear(events.get(i));
            }
        }
    }
}
