package com.sanjie.zy.http.reactive;

import com.sanjie.zy.utils.ZYNetworkUtils;
import com.sanjie.zy.utils.log.ZYLog;
import com.sanjie.zy.widget.ZYToast;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by LangSanJie on 2017/3/28.
 */

public class MObserver<T> implements Observer<T> {

    private static final String TAG = "MObserver";

    @Override
    public void onSubscribe(Disposable d) {
        if(!ZYNetworkUtils.isConnected()){
            //无网络
            ZYToast.error("请连接网络或稍后重试");
        }
    }

    @Override
    public void onNext(T t) {
    }

    @Override
    public void onError(Throwable e) {
        ZYLog.d("Error:" + e.getMessage());
    }

    @Override
    public void onComplete() {
    }
}
