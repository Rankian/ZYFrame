package com.sanjie.zy.picture.ui.base;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sanjie.zy.utils.ZYClassUtils;
import com.sanjie.zy.widget.ZYLoadingDialog;

/**
 * Created by LangSanJie on 2017/4/25.
 */

public abstract class AbstractFragment<P extends BasePresenter> extends Fragment implements BaseView {

    public P presenter;

    protected ZYLoadingDialog waitDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutId(), container, false);
        presenter = ZYClassUtils.getT(this, 0);
        presenter.attachModeView(this);
        initView(v);
        return v;
    }

    protected abstract int getLayoutId();

    protected abstract void initView(View view);

    @Override
    public void showWaitDialog() {
        if(!Thread.currentThread().getName().equals("main")){
            new Handler(Looper.getMainLooper()).post(new DialogRunnable());
        }else{
            new DialogRunnable().run();
        }
    }

    @Override
    public void hideWaitDialog() {
        if(waitDialog != null){
            waitDialog.dismiss();
        }
    }

    private class DialogRunnable implements Runnable{
        @Override
        public void run() {
            if(waitDialog == null){
                waitDialog = new ZYLoadingDialog(getActivity());
                waitDialog.setMessage("正在加载...");
            }
            waitDialog.show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(presenter != null){
            presenter.detachView();
        }
    }
}
