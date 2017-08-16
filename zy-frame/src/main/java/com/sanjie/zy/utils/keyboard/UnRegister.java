package com.sanjie.zy.utils.keyboard;

import android.view.ViewTreeObserver;

/**
 * Created by LangSanJie on 2017/5/2.
 */

public interface UnRegister {
    /**
     * unregisters the {@link ViewTreeObserver.OnGlobalLayoutListener} and there by does provide any more callback to the {@link ZYKeyboardVisibilityEventListener}
     */
    void unRegister();
}
