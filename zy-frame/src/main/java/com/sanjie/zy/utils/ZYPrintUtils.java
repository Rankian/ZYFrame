package com.sanjie.zy.utils;


import com.sanjie.zy.ZYFrame;

/**
 * 此内用于框架系统打印输出控制，使用者用XLog格式化体验更好。
 *
 */
public class ZYPrintUtils {
    private static String tag = ZYFrame.tag;
    private static boolean log = ZYFrame.isDebug;

    public static void setLog(boolean log) {
        ZYPrintUtils.log = log;
    }

    public static void i(String msg) {
        if (log)
            android.util.Log.i(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (log)
            android.util.Log.i(tag, msg);
    }

    public static void d(String msg) {
        if (log)
            android.util.Log.d(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (log)
            android.util.Log.d(tag, msg);
    }

    public static void w(String msg) {
        if (log)
            android.util.Log.w(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (log)
            android.util.Log.w(tag, msg);
    }

    public static void v(String msg) {
        if (log)
            android.util.Log.v(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (log)
            android.util.Log.v(tag, msg);
    }

    public static void e(String msg) {
        android.util.Log.e(tag, msg);
    }

    public static void e(String tag, String msg) {
        android.util.Log.e(tag, msg);
    }
}
