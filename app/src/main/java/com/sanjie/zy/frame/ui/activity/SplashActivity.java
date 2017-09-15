package com.sanjie.zy.frame.ui.activity;

import android.os.Bundle;

import com.sanjie.zy.frame.R;
import com.sanjie.zy.frame.base.BaseActivity;
import com.sanjie.zy.http.reactive.MObserver;
import com.sanjie.zy.utils.statusbar.ZYStatusBarUtil;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by LangSanJie on 2017/3/9.
 */

public class SplashActivity extends BaseActivity {
    public static final String TAG = "StartupActivity";

    private Timer timer;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    protected void initAppToolBar() {

    }

    public String getToolBarTitle() {
        return null;
    }

    public boolean hasToolbar() {
        return false;
    }

    @Override
    public void initView() {
        ZYStatusBarUtil.translucentStatusBar(this);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    protected void processLogic() {
        Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MObserver<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        super.onNext(aLong);
                        forwardActivity(MainActivity.class, null);
                        finish();
                    }
                });
    }

}
