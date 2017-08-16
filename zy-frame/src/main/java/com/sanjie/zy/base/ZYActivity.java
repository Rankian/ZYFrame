package com.sanjie.zy.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sanjie.zy.common.ZYActivityStack;

/**
 * Created by LangSanJie on 2017/3/7.
 */

public abstract class ZYActivity extends AppCompatActivity implements ICallBack {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ZYActivityStack.getInstance().addActivity(this);
        if(getLayoutId() > 0){
            setContentView(getLayoutId());
        }
        initData(savedInstanceState);
        initView();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZYActivityStack.getInstance().finishActivity();
    }
}
