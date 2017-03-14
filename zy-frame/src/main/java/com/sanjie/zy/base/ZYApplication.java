package com.sanjie.zy.base;

import android.app.Application;

import com.sanjie.zy.ZYFrame;

/**
 * Created by LangSanJie on 2017/3/7.
 */

public class ZYApplication extends Application {

    private static ZYApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ZYFrame.init(this);
    }

    public static ZYApplication getInstance(){
        return instance;
    }
}
