package com.sl.shenmian.lib.base.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.sl.shenmian.lib.base.repository.BaseRepository;
import com.sl.shenmian.lib.utils.TUtil;


/**
 * @author WenXian Bai
 * @Date: 2018/11/2.
 * @Description:
 */
public class BaseViewModel<T extends BaseRepository> extends AndroidViewModel {
    public String tag = getClaseName();

    public T mRepository;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        mRepository = TUtil.getNewInstance(this, 0);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mRepository != null) {
            mRepository.unDisposable();
        }
    }
    private String getClaseName(){
        return getClass().getSimpleName();
    }
}
