package com.sanjie.zy.frame.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.sanjie.zy.common.ZYActivityStack;
import com.sanjie.zy.frame.R;
import com.sanjie.zy.frame.application.App;
import com.sanjie.zy.frame.base.BaseActivity;
import com.sanjie.zy.utils.statusbar.ZYStatusBarUtil;
import com.sanjie.zy.widget.ZYToast;

import butterknife.BindView;

/**
 * Created by LangSanJie on 2017/3/9.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.right_Btn)
    ImageView rightBtn;
    @BindView(R.id.contentFrame)
    FrameLayout contentFrame;
    @BindView(R.id.radioTab)
    RadioGroup radioTab;
    @BindView(R.id.translucentContent)
    LinearLayout translucentContent;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
/*
        初始化侧滑菜单
         */
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        ZYStatusBarUtil.setColorForDrawerLayout(MainActivity.this, drawerLayout, getResources().getColor(R.color.colorPrimary), 0);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    protected boolean isSupportBack() {
        return false;
    }

    @Override
    public String getToolBarTitle() {
        return getString(R.string.app_name);
    }

    @Override
    protected void processLogic() {
 /*
         头部文件
         */
        View headerView = navigationView.getHeaderView(0);


        /*
         navigationView Item Click
         */
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.scanner_bluetooth:
                        break;
                    case R.id.add_btn:
                        break;
                }
                return true;
            }
        });
    }

    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            ZYToast.info("再按一次退出程序");
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            ZYActivityStack.getInstance().appExit();
        }
    }
}
