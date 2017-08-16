package com.sanjie.zy.picture.ui.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by LangSanJie on 2017/4/25.
 */

public abstract class BasePresenter<V extends BaseView> {

    public V view;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected void start(){

    }

    void attachModeView(V v){
        this.view = v;
        start();
    }

    void detachView(){
        this.view = null;
        if(compositeDisposable != null && compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
        }
    }

    public Disposable add(Disposable disposable){
        compositeDisposable.add(disposable);
        return disposable;
    }
}
