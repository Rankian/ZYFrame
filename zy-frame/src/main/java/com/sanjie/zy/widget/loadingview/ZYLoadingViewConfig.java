package com.sanjie.zy.widget.loadingview;

import android.support.annotation.LayoutRes;

import com.sanjie.zy.R;

/**
 * Created by LangSanJie on 2017/3/8.
 */

public class ZYLoadingViewConfig {

    private int emptyViewResId = R.layout.loading_empty_view;
    private int errorViewResId = R.layout.loading_error_view;
    private int loadingViewResId = R.layout.loading_loading_view;
    private int noNetWorkViewResId = R.layout.loading_no_network_view;

    public int getEmptyViewResId(){
        return emptyViewResId;
    }
    public ZYLoadingViewConfig setEmptyViewResId(@LayoutRes int resId){
        this.emptyViewResId = resId;
        return this;
    }

    public int getErrorViewResId(){
        return errorViewResId;
    }

    public ZYLoadingViewConfig setErrorViewResId(@LayoutRes int resId){
        this.errorViewResId = resId;
        return this;
    }

    public int getLoadingViewResId(){
        return loadingViewResId;
    }

    public ZYLoadingViewConfig setLoadingViewResId(@LayoutRes int resId){
        this.loadingViewResId = resId;
        return this;
    }

    public int getNoNetWorkViewResId(){
        return noNetWorkViewResId;
    }

    public ZYLoadingViewConfig setNoNetWorkViewResId(@LayoutRes int resId){
        this.noNetWorkViewResId = resId;
        return this;
    }

}
