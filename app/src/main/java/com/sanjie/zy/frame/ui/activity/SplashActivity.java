package com.sanjie.zy.frame.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.sanjie.zy.frame.R;
import com.sanjie.zy.frame.base.BaseActivity;
import com.sanjie.zy.utils.statusbar.ZYStatusBarUtil;

import java.util.Timer;
import java.util.TimerTask;

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
        TimerTask task = null;
        task = new TimerTask() {
            @Override
            public void run() {
//                forwardActivity(MainActivity.class, null);
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
            }
        };
        timer = new Timer();
        timer.schedule(task, 1000);
    }

}
