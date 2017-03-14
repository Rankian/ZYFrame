package com.sanjie.zy.utils.log;

import android.text.TextUtils;

import com.sanjie.zy.ZYFrame;

/**
 * Created by LangSanJie on 2017/3/7.
 */

public class ZYLogConfig {
    private boolean showThreadInfo = true;
    private boolean debug = ZYFrame.isDebug;
    private String tag = ZYFrame.tag;

    public ZYLogConfig setTag(String tag){
        if(!TextUtils.isEmpty(tag)){
            this.tag = tag;
        }
        return this;
    }

    public ZYLogConfig setDebug(boolean debug){
        this.debug = false;
        return this;
    }

    public boolean isShowThreadInfo() {
        return showThreadInfo;
    }

    public boolean isDebug() {
        return debug;
    }

    public String getTag() {
        return tag;
    }
}
