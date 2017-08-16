package com.sanjie.zy;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.util.DisplayMetrics;

import com.sanjie.zy.utils.ZYDisplayUtils;
import com.sanjie.zy.utils.ZYOutdatedUtils;
import com.sanjie.zy.utils.log.ZYLog;
import com.sanjie.zy.utils.log.ZYLogConfig;
import com.sanjie.zy.widget.loadingview.ZYLoadingView;
import com.sanjie.zy.widget.loadingview.ZYLoadingViewConfig;

/**
 * Created by LangSanJie on 2017/3/7.
 */

public class ZYFrame {

    private static Context context;
    public static int screenHeight;
    public static int screenWidth;

    public static String tag = "ZYFrame";
    public static boolean isDebug = true;

    public ZYFrame() {
    }

    public static void init(Context context) {
        ZYFrame.context = context;
        screenHeight = ZYDisplayUtils.getScreenHeight();
        screenWidth = ZYDisplayUtils.getScreenWidth();
    }


    public static ZYLogConfig initXLog() {
        return ZYLog.init();
    }

    public static ZYLoadingViewConfig initXLoadingView() {
        return ZYLoadingView.init();
    }

    public static Context getContext() {
        synchronized (ZYFrame.class) {
            if (ZYFrame.context == null)
                throw new NullPointerException("Call XFrame.init(context) within your Application onCreate() method." +
                        "Or extends XApplication");
            return ZYFrame.context.getApplicationContext();
        }
    }

    public static Resources getResources() {
        return ZYFrame.getContext().getResources();
    }

    public static Resources.Theme getTheme() {
        return ZYFrame.getContext().getTheme();
    }

    public static AssetManager getAssets() {
        return ZYFrame.getContext().getAssets();
    }

    public static Drawable getDrawable(@DrawableRes int id) {
        return ZYOutdatedUtils.getDrawable(id);
    }

    public static int getColor( @ColorRes int id) {
        return ZYOutdatedUtils.getColor(id);
    }

    public static Object getSystemService(String name){
        return ZYFrame.getContext().getSystemService(name);
    }

    public static Configuration getConfiguration() {
        return ZYFrame.getResources().getConfiguration();
    }

    public static DisplayMetrics getDisplayMetrics() {
        return ZYFrame.getResources().getDisplayMetrics();
    }
}
