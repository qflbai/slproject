package com.sl.shenmian.lib.base.activity;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.sl.shenmian.lib.base.viewmodel.BaseViewModel;
import com.sl.shenmian.lib.utils.TUtil;


/**
 * @author WenXian Bai
 * @Date: 2018/11/2.
 * @Description:
 */
public abstract class AbsActivity<T extends BaseViewModel> extends BaseActivity {

    protected T mViewModel;

    public AbsActivity() {}


    public void initViews(Bundle savedInstanceState) {
        mViewModel = VMProviders(this, (Class<T>) TUtil.getInstance(this, 0));
        dataObserver();
    }


    protected <T extends ViewModel> T VMProviders(AppCompatActivity fragment, @NonNull Class modelClass) {
        return (T) ViewModelProviders.of(fragment).get(modelClass);
    }

    protected void dataObserver() {

    }


    protected void onStateRefresh() {
        showLoading();
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
}
