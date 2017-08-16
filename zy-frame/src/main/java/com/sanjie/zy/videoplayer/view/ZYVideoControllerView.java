package com.sanjie.zy.videoplayer.view;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sanjie.zy.R;
import com.sanjie.zy.utils.ZYDisplayUtils;
import com.sanjie.zy.utils.ZYFormatTimeUtils;
import com.sanjie.zy.utils.ZYNetworkUtils;
import com.sanjie.zy.videoplayer.ZYVideoPlayer;
import com.sanjie.zy.videoplayer.bean.ZYVideoInfo;
import com.sanjie.zy.videoplayer.listener.OnVideoControlListener;
import com.sanjie.zy.videoplayer.listener.ZYSimpleOnVideoControlListener;
import com.sanjie.zy.widget.ZYToast;

/**
 * Created by SanJie on 2017/6/22.
 */

public class ZYVideoControllerView extends FrameLayout {

    public static final int DEFAULT_SHOW_TIME = 3000;

    private View mControlerBack;
    private View mControlerTitle;
    private TextView mVideoTitle;
    private View mControlerBottom;
    private SeekBar mPlayerSeekBar;
    private ImageView mVideoPlayState;
    private TextView mVideoProgress;
    private TextView mVideoDuration;
    private ImageView mVideoFullScreen;
    private ImageView mScreenLock;
    private ZYVideoErrorView mErrorView;

    private boolean isScreenLock;
    private boolean mShowing;
    private boolean mAllowUnWifiPlay;
    private ZYVideoPlayer mPlayer;
    private ZYVideoInfo videoInfo;
    private OnVideoControlListener onVideoControlListener;

    public void setOnVideoControlListener(OnVideoControlListener onVideoControlListener) {
        this.onVideoControlListener = onVideoControlListener;
    }

    public ZYVideoControllerView(@NonNull Context context) {
        super(context);
        init();
    }

    public ZYVideoControllerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZYVideoControllerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.video_media_controller, this);
        initControllerPanel();
    }

    private void initControllerPanel() {
        //back
        mControlerBack = findViewById(R.id.video_back);
        mControlerBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onVideoControlListener != null) {
                    onVideoControlListener.onBack();
                }
            }
        });
        //top
        mControlerTitle = findViewById(R.id.video_controller_title);
        mVideoTitle = (TextView) mControlerTitle.findViewById(R.id.video_title);
        //bottom
        mControlerBottom = findViewById(R.id.video_controller_bottom);
        mPlayerSeekBar = (SeekBar) mControlerBottom.findViewById(R.id.player_seek_bar);
        mVideoPlayState = (ImageView) mControlerBottom.findViewById(R.id.player_pause);
        mVideoProgress = (TextView) mControlerBottom.findViewById(R.id.player_progress);
        mVideoDuration = (TextView) mControlerBottom.findViewById(R.id.player_duration);
        mVideoFullScreen = (ImageView) mControlerBottom.findViewById(R.id.video_full_screen);
        mVideoPlayState.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doPauseResume();
            }
        });
        mVideoPlayState.setImageResource(R.drawable.ic_video_pause);
        mPlayerSeekBar.setOnSeekBarChangeListener(mSeekListener);
        mVideoFullScreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onVideoControlListener != null) {
                    onVideoControlListener.onFullScreen();
                }
            }
        });

        //lock
        mScreenLock = (ImageView) findViewById(R.id.player_lock_screen);
        mScreenLock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isScreenLock()) unlock();
                else lock();
                show();
            }
        });

        //error
        mErrorView = (ZYVideoErrorView) findViewById(R.id.video_controller_error);
        mErrorView.setOnVideoControlListener(new ZYSimpleOnVideoControlListener() {
            @Override
            public void onRetry(int errorStatus) {
                retry(errorStatus);
            }
        });

        mPlayerSeekBar.setMax(1000);
    }

    public void setMediaPlayer(ZYVideoPlayer player) {
        this.mPlayer = player;
        updatePausePlay();
    }

    public void setVideoInfo(ZYVideoInfo videoInfo) {
        this.videoInfo = videoInfo;
        mVideoTitle.setText(videoInfo.getVideoTitle());
    }

    public void toggleDisplay() {
        if (mShowing) {
            hide();
        } else {
            show();
        }
    }

    public void show() {
        show(DEFAULT_SHOW_TIME);
    }

    public void show(int timeout) {
        setProgress();

        if (!isScreenLock) {
            mControlerBack.setVisibility(VISIBLE);
            mControlerTitle.setVisibility(VISIBLE);
            mControlerBottom.setVisibility(VISIBLE);
        } else {
            if (!ZYDisplayUtils.isPortrait(getContext())) {
                mControlerBack.setVisibility(GONE);
            }
            mControlerTitle.setVisibility(GONE);
            mControlerBottom.setVisibility(GONE);
        }

        if (!ZYDisplayUtils.isPortrait(getContext())) {
            mScreenLock.setVisibility(VISIBLE);
        }

        mShowing = true;

        updatePausePlay();

        post(mShowProgress);

        if (timeout > 0) {
            removeCallbacks(mFadeOut);
            postDelayed(mFadeOut, timeout);
        }
    }

    private void hide() {
        if (!mShowing) {
            return;
        }

        if (!ZYDisplayUtils.isPortrait(getContext())) {
            // 横屏才消失
            mControlerBack.setVisibility(GONE);
        }
        mControlerTitle.setVisibility(GONE);
        mControlerBottom.setVisibility(GONE);
        mScreenLock.setVisibility(GONE);

        removeCallbacks(mShowProgress);

        mShowing = false;
    }

    private final Runnable mFadeOut = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private boolean mDragging;
    private long mDraggingProgress;
    private final Runnable mShowProgress = new Runnable() {
        @Override
        public void run() {
            int pos = setProgress();
            if (!mDragging && mShowing && mPlayer.isPlaying()) {
                postDelayed(mShowProgress, 1000 - (pos % 1000));
            }
        }
    };

    private int setProgress() {
        if (mPlayer == null || mDragging) {
            return 0;
        }
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (mPlayerSeekBar != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                mPlayerSeekBar.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mPlayerSeekBar.setSecondaryProgress(percent * 10);
        }

        mVideoProgress.setText(ZYFormatTimeUtils.stringForTime(position));
        mVideoDuration.setText(ZYFormatTimeUtils.stringForTime(duration));

        return position;
    }

    /**
     * 判断显示错误类型
     */
    public void checkShowError(boolean isNetChanged) {
        boolean isConnect = ZYNetworkUtils.isConnected();
        boolean isMobileNet = ZYNetworkUtils.getDataEnabled();
        boolean isWifiNet = ZYNetworkUtils.isWifiConnected();

        if (isConnect) {
            // 如果已经联网
            if (mErrorView.getCurStatus() == ZYVideoErrorView.STATUS_NO_NETWORK_ERROR && !(isMobileNet && !isWifiNet)) {
                // 如果之前是无网络 TODO 应该提示“网络已经重新连上，请重试”，这里暂不处理
            } else if (videoInfo == null) {
                // 优先判断是否有video数据
                showError(ZYVideoErrorView.STATUS_VIDEO_DETAIL_ERROR);
            } else if (isMobileNet && !isWifiNet && !mAllowUnWifiPlay) {
                // 如果是手机流量，且未同意过播放，且非本地视频，则提示错误
                mErrorView.showError(ZYVideoErrorView.STATUS_UN_WIFI_ERROR);
                mPlayer.pause();
            } else if (isWifiNet && isNetChanged && mErrorView.getCurStatus() == ZYVideoErrorView.STATUS_UN_WIFI_ERROR) {
                // 如果是wifi流量，且之前是非wifi错误，则恢复播放
                playFromUnWifiError();
            } else if (!isNetChanged) {
                showError(ZYVideoErrorView.STATUS_VIDEO_SRC_ERROR);
            }
        } else {
            mPlayer.pause();
            showError(ZYVideoErrorView.STATUS_NO_NETWORK_ERROR);
        }
    }

    public void hideErrorView() {
        mErrorView.hideError();
    }

    private void reload() {
        mPlayer.restart();
    }

    public void release() {
        removeCallbacks(mShowProgress);
        removeCallbacks(mFadeOut);
    }

    private void retry(int status) {

        switch (status) {
            case ZYVideoErrorView.STATUS_VIDEO_DETAIL_ERROR:
                // 传递给activity
                if (onVideoControlListener != null) {
                    onVideoControlListener.onRetry(status);
                }
                break;
            case ZYVideoErrorView.STATUS_VIDEO_SRC_ERROR:
                reload();
                break;
            case ZYVideoErrorView.STATUS_UN_WIFI_ERROR:
                allowUnWifiPlay();
                break;
            case ZYVideoErrorView.STATUS_NO_NETWORK_ERROR:
                // 无网络时
                if (ZYNetworkUtils.isConnected()) {
                    if (videoInfo == null) {
                        // 如果video为空，重新请求详情
                        retry(ZYVideoErrorView.STATUS_VIDEO_DETAIL_ERROR);
                    } else if (mPlayer.isInPlaybackState()) {
                        // 如果有video，可以直接播放的直接恢复
                        mPlayer.start();
                    } else {
                        // 视频未准备好，重新加载
                        reload();
                    }
                } else {
                    ZYToast.error("网络未连接");
                }
                break;
        }
    }

    private final SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onStartTrackingTouch(SeekBar bar) {
            show(3600000);

            mDragging = true;

            removeCallbacks(mShowProgress);
        }

        @Override
        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (!fromuser) {
                return;
            }

            long duration = mPlayer.getDuration();
            mDraggingProgress = (duration * progress) / 1000L;

            if (mVideoProgress != null) {
                mVideoProgress.setText(ZYFormatTimeUtils.stringForTime((int) mDraggingProgress));
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar bar) {
            mPlayer.seekTo((int) mDraggingProgress);
            play();
            mDragging = false;
            mDraggingProgress = 0;

            post(mShowProgress);
        }
    };

    private void showError(int status) {
        mErrorView.showError(status);
        hide();

        // 如果提示了错误，则看需要解锁
        if (isScreenLock) {
            unlock();
        }
    }

    public boolean isScreenLock() {
        return isScreenLock;
    }

    private void lock() {
        isScreenLock = true;
        mScreenLock.setImageResource(R.drawable.video_locked);
    }

    private void unlock() {
        isScreenLock = false;
        mScreenLock.setImageResource(R.drawable.video_unlock);
    }

    private void allowUnWifiPlay() {
        mAllowUnWifiPlay = true;
        playFromUnWifiError();
    }

    private void playFromUnWifiError() {
        if (mPlayer.isInPlaybackState()) {
            mPlayer.start();
        } else {
            mPlayer.restart();
        }
    }

    public void updatePausePlay() {
        if (mPlayer.isPlaying()) {
            mVideoPlayState.setImageResource(R.drawable.ic_video_pause);
        } else {
            mVideoPlayState.setImageResource(R.drawable.ic_video_play);
        }
    }

    private void doPauseResume() {
        if (mPlayer.isPlaying()) {
            pause();
        } else {
            play();
        }
    }

    private void pause() {
        mPlayer.pause();

    }

    private void play() {
        mPlayer.start();
        show();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggleVideoLayoutParams();
    }

    void toggleVideoLayoutParams() {
        final boolean isPorrait = ZYDisplayUtils.isPortrait(getContext());
        if (isPorrait) {
            mControlerBack.setVisibility(View.VISIBLE);
            mVideoFullScreen.setVisibility(VISIBLE);
            mScreenLock.setVisibility(GONE);
        } else {
            mVideoFullScreen.setVisibility(GONE);
            if (mShowing) {
                mScreenLock.setVisibility(VISIBLE);
            }
        }
    }
}
