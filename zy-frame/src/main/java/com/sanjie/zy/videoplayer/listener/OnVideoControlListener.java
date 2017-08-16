package com.sanjie.zy.videoplayer.listener;

/**
 * Created by SanJie on 2017/6/22.
 */

public interface OnVideoControlListener {
    /**
     * 返回
     */
    void onBack();

    /**
     * 全屏
     */
    void onFullScreen();

    /**
     * 错误后重试
     * @param errorStatus
     *      <ul>
     *          <li>{@link }</li>
     *      </ul>
     */
    void onRetry(int errorStatus);
}
