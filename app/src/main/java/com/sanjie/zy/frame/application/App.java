package com.sanjie.zy.frame.application;

import com.sanjie.zy.ZYFrame;
import com.sanjie.zy.base.ZYApplication;

/**
 * Created by LangSanJie on 2017/3/9.
 */

public class App extends ZYApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        ZYFrame.init(getApplicationContext());
        ZYFrame.initXLog();
        ZYFrame.initXLoadingView();
    }
}
