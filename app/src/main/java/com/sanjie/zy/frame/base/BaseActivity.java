package com.sanjie.zy.frame.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sanjie.zy.base.ZYActivity;
import com.sanjie.zy.frame.R;
import com.sanjie.zy.utils.ZYKeyboardUtils;
import com.sanjie.zy.utils.statusbar.ZYStatusBarUtil;

import butterknife.ButterKnife;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackLayout;

/**
 * Created by LangSanJie on 2017/3/9.
 */

public abstract class BaseActivity extends AppCompatActivity implements BGASwipeBackLayout.PanelSlideListener{

    protected BGASwipeBackLayout swipeBackLayout;

    public Toolbar mToolbar;
    TextView tvTitleTextView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getLayoutId() > 0){
            setContentView(getLayoutId());
        }
        ButterKnife.bind(this);
        if(hasToolbar()){
            initAppToolBar();
        }
        initSwipeBackFinish();
        initView();
        initData(savedInstanceState);
        processLogic();
    }

    public abstract int getLayoutId();

    public abstract void initView();

    public abstract void initData(Bundle savedInstanceState);

    /**
     * 获取标题
     * @return
     */
    public abstract String getToolBarTitle();

    /**
     * 是否含有 Toolbar
     * 默认 返回true， 若不含，重写该方法返回false 即可
     * @return
     */
    protected boolean hasToolbar(){
        return true;
    }
    /**
     * 是否支持返回按钮
     * 默认 返回true， 若不支持，重写该方法返回false 即可
     * @return
     */
    protected boolean isSupportBack(){
        return true;
    }

    /**
     * 处理业务逻辑，状态恢复等操作
     */
    protected abstract void processLogic();


    protected void initAppToolBar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitleTextView = (TextView) findViewById(R.id.tv_titleTextView);
        setSupportActionBar(mToolbar);
        //隐藏返回按钮 NavigationIcon
        getSupportActionBar().setDisplayHomeAsUpEnabled(isSupportBack());
        getSupportActionBar().setTitle("");
        tvTitleTextView.setText(getToolBarTitle());
        if(isSupportBack()){
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    /**
     * 初始化滑动返回
     */
    private void initSwipeBackFinish() {
        if (isSupportSwipeBack()) {
            swipeBackLayout = new BGASwipeBackLayout(this);
            swipeBackLayout.attachToActivity(this);
            swipeBackLayout.setPanelSlideListener(this);

            // 下面四项项可以不配置，这里只是为了讲述接口用法。
            // 如果需要启用微信滑动返回样式，必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getInstance().init(this)

            // 设置滑动返回是否可用。默认值为 true
            swipeBackLayout.setSwipeBackEnable(true);
            // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
            swipeBackLayout.setIsOnlyTrackingLeftEdge(true);
            // 设置是否是微信滑动返回样式。默认值为 true
            swipeBackLayout.setIsWeChatStyle(true);
            // 设置阴影资源 id。默认值为 R.drawable.bga_swipebacklayout_shadow
            swipeBackLayout.setShadowResId(R.drawable.bga_swipebacklayout_shadow);
            // 设置是否显示滑动返回的阴影效果。默认值为 true
            swipeBackLayout.setIsNeedShowShadow(true);
            // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
            swipeBackLayout.setIsShadowAlphaGradient(true);
        }
    }

    /**
     * 是否支持滑动返回。默认支持，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    protected boolean isSupportSwipeBack() {
        return true;
    }

    /**
     * 动态设置滑动返回是否可用。
     *
     * @param swipeBackEnable
     */
    protected void setSwipeBackEnable(boolean swipeBackEnable) {
        if (swipeBackLayout != null) {
            swipeBackLayout.setSwipeBackEnable(swipeBackEnable);
        }
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override
    public void onPanelOpened(View panel) {
        swipeBackward();
    }

    @Override
    public void onPanelClosed(View panel) {

    }

    @Override
    public void onBackPressed() {
        backward();
    }

    /**
     * 跳转到下一个Activity，不销毁当前Activity
     *
     * @param cls 下一个Activity的Class
     */
    public void forwardActivity(Class<?> cls, Bundle bundle) {
        ZYKeyboardUtils.closeKeyboard(this);
        Intent intent = new Intent(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        executeForwardAnim();
    }

    /**
     * 执行跳转到下一个Activity的动画
     */
    public void executeForwardAnim() {
        overridePendingTransition(R.anim.activity_forward_enter, R.anim.activity_forward_exit);
    }

    /**
     * 回到上一个Activity，并销毁当前Activity
     */
    public void backward() {
        ZYKeyboardUtils.closeKeyboard(this);
        finish();
        executeBackwardAnim();
    }

    /**
     * 滑动返回上一个Activity，并销毁当前Activity
     */
    public void swipeBackward() {
        ZYKeyboardUtils.closeKeyboard(this);
        finish();
        executeSwipeBackAnim();
    }

    /**
     * 执行回到到上一个Activity的动画
     */
    public void executeBackwardAnim() {
        overridePendingTransition(R.anim.activity_backward_enter, R.anim.activity_backward_exit);
    }

    /**
     * 执行滑动返回到到上一个Activity的动画
     */
    public void executeSwipeBackAnim() {
        overridePendingTransition(R.anim.activity_swipeback_enter, R.anim.activity_swipeback_exit);
    }
    /**
     * 设置状态栏颜色
     *
     * @param color
     */
    protected void setStatusBarColor(@ColorInt int color) {
        setStatusBarColor(color, com.jaeger.library.StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * 设置状态栏颜色
     *
     * @param color
     * @param statusBarAlpha 透明度
     */
    public void setStatusBarColor(@ColorInt int color, @IntRange(from = 0, to = 255) int statusBarAlpha) {
//        StatusBarUtil.setColorForSwipeBack(this, color, statusBarAlpha);

        // 临时修复 https://github.com/laobie/StatusBarUtil 与 CoordinatorLayout 结合使用时的 bug，等 StatusBarUtil 的作者更新后用他的库
        ZYStatusBarUtil.setColorForSwipeBack(this, color, statusBarAlpha);
    }

}
