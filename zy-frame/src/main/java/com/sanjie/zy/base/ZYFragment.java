package com.sanjie.zy.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by LangSanJie on 2017/3/7.
 */

public abstract class ZYFragment extends Fragment implements ICallBack{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayoutId()>0){
            return inflater.inflate(getLayoutId(),container,false);
        }else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
        initView();
    }
}
