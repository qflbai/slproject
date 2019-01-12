package com.sl.shenmian.lib.base.repository;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author WenXian Bai
 * @Date: 2018/11/2.
 * @Description:
 */
public  class BaseRepository {

    private CompositeDisposable mCompositeDisposable;


    public BaseRepository() {

    }

    public void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    public void unDisposable() {
        if (mCompositeDisposable != null && mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }
}
