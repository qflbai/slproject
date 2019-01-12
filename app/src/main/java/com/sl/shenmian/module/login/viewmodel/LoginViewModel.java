package com.sl.shenmian.module.login.viewmodel;

import android.app.Application;
import android.support.annotation.NonNull;

import com.sl.shenmian.lib.base.viewmodel.BaseViewModel;
import com.sl.shenmian.module.login.source.LoginRepository;

public class LoginViewModel extends BaseViewModel<LoginRepository> {

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }
}
