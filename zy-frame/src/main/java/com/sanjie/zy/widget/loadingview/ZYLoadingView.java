package com.sanjie.zy.widget.loadingview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sanjie.zy.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 简单实用的页面状态统一管理 ，加载中、无网络、无数据、出错等状态的随意切换
 * Created by LangSanJie on 2017/3/8.
 */

public class ZYLoadingView extends FrameLayout {

    private int mEmptyViewResId;
    private int mErrorViewResId;
    private int mLoadingViewResId;
    private int mNoNetWorkViewResId;
    private int mContentViewResId;
    private LayoutInflater mInflater;
    private OnClickListener mOnRetryClickListener;
    private Map<Integer, View> mResId = new HashMap<>();

    public static ZYLoadingViewConfig config = new ZYLoadingViewConfig();

    public static ZYLoadingViewConfig init() {
        return config;
    }

    public static ZYLoadingView wrap(Activity activity) {
        return warp(((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0));
    }

    public static ZYLoadingView warp(Fragment fragment) {
        return warp(fragment.getView());
    }

    public static ZYLoadingView warp(View view) {
        if (view == null) {
            throw new RuntimeException("content view can not be null");
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent == null) {
            throw new RuntimeException("parent view can not be null");
        }
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        int index = parent.indexOfChild(view);
        parent.removeView(view);

        ZYLoadingView zyLoadingView = new ZYLoadingView(view.getContext());
        parent.addView(zyLoadingView, index, lp);
        zyLoadingView.addView(view);
        zyLoadingView.setContentView(view);
        return zyLoadingView;

    }

    public ZYLoadingView(@NonNull Context context) {
        this(context, null);
    }

    public ZYLoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZYLoadingView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.ZYLoadingView,defStyleAttr,0);
        mEmptyViewResId = a.getResourceId(R.styleable.ZYLoadingView_emptyView, config.getEmptyViewResId());
        mErrorViewResId = a.getResourceId(R.styleable.ZYLoadingView_errorView, config.getErrorViewResId());
        mLoadingViewResId = a.getResourceId(R.styleable.ZYLoadingView_loadingView, config.getLoadingViewResId());
        mNoNetWorkViewResId = a.getResourceId(R.styleable.ZYLoadingView_noNetworkView, config.getNoNetWorkViewResId());
        a.recycle();
    }

    private void setContentView(View view) {
        mContentViewResId = view.getId();
        mResId.put(mContentViewResId, view);
    }

    private void show(int resId){
        for (View view : mResId.values()){
            view.setVisibility(GONE);
        }
        layout(resId).setVisibility(VISIBLE);
    }

    private View layout(int resId){
        if(mResId.containsKey(resId)){
            return mResId.get(resId);
        }
        View view = mInflater.inflate(resId, this, false);
        view.setVisibility(GONE);
        addView(view);
        mResId.put(resId, view);
        if(resId == mErrorViewResId || resId == mNoNetWorkViewResId){
            View v = view.findViewById(R.id.loading_retry);
            if(mOnRetryClickListener != null && v != null){
                v.setOnClickListener(mOnRetryClickListener);
            }
        }
        return view;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(getChildCount() == 0){
            return;
        }
        if(getChildCount() > 1){
            removeViews(1, getChildCount() -1);
        }
        View view =getChildAt(0);
        setContentView(view);
        addView(view);
    }

    /**
     * 设置重试点击事件
     * @param mOnRetryClickListener
     */
    public void setmOnRetryClickListener(OnClickListener mOnRetryClickListener) {
        this.mOnRetryClickListener = mOnRetryClickListener;
    }
}
