package com.sanjie.zy.http.download;

import com.sanjie.zy.http.listener.HttpProgressOnNextListener;
import com.sanjie.zy.http.reactive.MObserver;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by SanJie on 2017/6/8.
 */

public class DownloadProgressObserver<T> implements Observer<T>, DownloadProgressListener {

    //弱引用结果回调
    private WeakReference<HttpProgressOnNextListener> mObserverOnNextListener;
    private DownloadInfo downloadInfo;

    public DownloadProgressObserver(DownloadInfo downloadInfo) {
        this.mObserverOnNextListener = new WeakReference<HttpProgressOnNextListener>(downloadInfo.getListener());
        this.downloadInfo = downloadInfo;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        if (mObserverOnNextListener.get() != null) {
            mObserverOnNextListener.get().onStart();
        }
        downloadInfo.setState(DownloadState.START);
    }

    @Override
    public void onNext(@NonNull T t) {
        if (mObserverOnNextListener.get() != null) {
            mObserverOnNextListener.get().onNext(t);
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        //停止下载
        if (mObserverOnNextListener.get() != null) {
            mObserverOnNextListener.get().onError(e);
        }
        downloadInfo.setState(DownloadState.ERROR);
    }

    @Override
    public void onComplete() {
        if (mObserverOnNextListener.get() != null) {
            mObserverOnNextListener.get().onComplete();
        }
        downloadInfo.setState(DownloadState.FINISH);
    }

    @Override
    public void update(long read, long count, boolean completed) {
        if (downloadInfo.getCountLength() > count) {
            read = downloadInfo.getCountLength() - count + read;
        } else {
            downloadInfo.setCountLength(count);
        }
        downloadInfo.setReadLength(read);
        if (mObserverOnNextListener.get() != null) {
            Observable.just(read).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MObserver<Long>() {
                        @Override
                        public void onNext(Long aLong) {
                            super.onNext(aLong);
                            if (downloadInfo.getState() == DownloadState.PAUSE
                                    || downloadInfo.getState() == DownloadState.STOP) return;
                            downloadInfo.setState(DownloadState.DOWNLOAD);
                            mObserverOnNextListener.get().updateProgress(aLong, downloadInfo.getCountLength());
                        }
                    });
        }
    }
}
