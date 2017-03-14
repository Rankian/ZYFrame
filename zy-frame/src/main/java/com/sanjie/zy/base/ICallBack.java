package com.sanjie.zy.base;

import android.os.Bundle;

/**
 * Created by LangSanJie on 2017/3/7.
 */

public interface ICallBack {
    //返回布局文件id
    int getLayoutId();
    //初始化布局文件
    void initView();
    //初始化数据
    void initData(Bundle savedInstanceState);
}
