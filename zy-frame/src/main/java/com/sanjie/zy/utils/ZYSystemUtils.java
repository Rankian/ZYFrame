package com.sanjie.zy.utils;

import android.os.Build;

import java.util.Locale;

/**
 * Created by SanJie on 2017/5/23.
 */

public class ZYSystemUtils {
    /**
     * 获取手机系统语言
     * @return
     */
    public static String getSystemLanguage(){
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取猴急系统版本号
     * @return
     */
    public static String getSystemVersion(){
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     * @return
     */
    public static String getDeviceModel(){
        return Build.MODEL;
    }

    /**
     * 获取手机厂商名称
     * @return
     */
    public static String getDeviceBrand(){
        return Build.BRAND;
    }
}
