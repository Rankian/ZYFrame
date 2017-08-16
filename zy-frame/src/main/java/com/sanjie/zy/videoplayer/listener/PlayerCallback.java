package com.sanjie.zy.videoplayer.listener;

import android.media.MediaPlayer;

/**
 * 视频操作回调，是将系统MediaPlayer的常见回调封装了起来
 * Created by SanJie on 2017/6/22.
 */

public interface PlayerCallback {
    /**
     * 准备完成
     *
     * @param mp
     */
    void onPrepared(MediaPlayer mp);

    /**
     * 视频size发生变化
     *
     * @param mp
     * @param width
     * @param height
     */
    void onVideoSizeChanged(MediaPlayer mp, int width, int height);

    /**
     * 缓存更新变化
     *
     * @param mp
     * @param percent 缓冲百分比
     */
    void onBufferingUpdate(MediaPlayer mp, int percent);

    /**
     * 播放完成
     *
     * @param mp
     */
    void onCompleted(MediaPlayer mp);

    /**
     * 视频错误
     *
     * @param mp
     * @param what  错误类型
     *              <ul>
     *              <li>{@link MediaPlayer#MEDIA_ERROR_UNKNOWN}</li>
     *              <li>{@link MediaPlayer#MEDIA_ERROR_SERVER_DIED}</li>
     *              </ul>
     * @param extra 特殊错误码
     *              <ul>
     *              <li>{@link MediaPlayer#MEDIA_ERROR_IO}</li>
     *              <li>{@link MediaPlayer#MEDIA_ERROR_MALFORMED}</li>
     *              <li>{@link MediaPlayer#MEDIA_ERROR_UNSUPPORTED}</li>
     *              <li>{@link MediaPlayer#MEDIA_ERROR_TIMED_OUT}</li>
     *              </ul>
     */
    void onError(MediaPlayer mp, int what, int extra);

    /**
     * 视频加载状态变化
     * @param isShow 是否显示loading
     */
    void onLoadingChanged(boolean isShow);

    /**
     * 视频状态变化
     * @param currentState 当前视频状态
     */
    void onStateChanged(int currentState);
}
