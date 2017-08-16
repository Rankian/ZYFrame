package com.sanjie.zy.videoplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.sanjie.zy.R;
import com.sanjie.zy.utils.ZYNetworkUtils;
import com.sanjie.zy.videoplayer.bean.ZYVideoInfo;
import com.sanjie.zy.videoplayer.listener.OnVideoControlListener;
import com.sanjie.zy.videoplayer.listener.ZYSimplePlayerCallback;
import com.sanjie.zy.videoplayer.view.ZYVideoBehaviorView;
import com.sanjie.zy.videoplayer.view.ZYVideoControllerView;
import com.sanjie.zy.videoplayer.view.ZYVideoProgressOverlay;
import com.sanjie.zy.videoplayer.view.ZYVideoSystemOverlay;

/**
 * Created by SanJie on 2017/6/22.
 */

public class ZYVideoView extends ZYVideoBehaviorView {

    private SurfaceView mSurfaceView;
    private View mLoading;
    private ZYVideoControllerView mediaController;
    private ZYVideoSystemOverlay videoSystemOverlay;
    private ZYVideoProgressOverlay videoProgressOverlay;
    private ZYVideoPlayer mMediaPlayer;

    private int initWidth;
    private int initHeight;

    public boolean isLock() {
        return mediaController.isScreenLock();
    }

    public ZYVideoView(@NonNull Context context) {
        super(context);
        init();
    }

    public ZYVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZYVideoView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.video_view, this);

        mSurfaceView = (SurfaceView) findViewById(R.id.video_surface);
        mLoading = findViewById(R.id.video_loading);
        mediaController = (ZYVideoControllerView) findViewById(R.id.video_controller);
        videoSystemOverlay = (ZYVideoSystemOverlay) findViewById(R.id.video_system_overlay);
        videoProgressOverlay = (ZYVideoProgressOverlay) findViewById(R.id.video_progress_overlay);

        initPlayer();

        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initWidth = getWidth();
                initHeight = getHeight();

                if(mMediaPlayer != null){
                    mMediaPlayer.setDisplay(holder);
                    mMediaPlayer.openVideo();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        registerNetChangedReceiver();
    }

    private void initPlayer() {
        mMediaPlayer = new ZYVideoPlayer();
        mMediaPlayer.setCallback(new ZYSimplePlayerCallback() {
            @Override
            public void onStateChanged(int currentState) {
                switch (currentState) {
                    case ZYVideoPlayer.STATE_IDLE:
                        am.abandonAudioFocus(null);
                        break;
                    case ZYVideoPlayer.STATE_PREPARING:
                        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                        break;
                }
            }

            @Override
            public void onCompleted(MediaPlayer mp) {
                mediaController.updatePausePlay();
            }

            @Override
            public void onError(MediaPlayer mp, int what, int extra) {
                mediaController.checkShowError(false);
            }

            @Override
            public void onLoadingChanged(boolean isShow) {
                if (isShow) showLoading();
                else hideLoading();
            }

            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();
                mediaController.show();
                mediaController.hideErrorView();
            }
        });
        mediaController.setMediaPlayer(mMediaPlayer);
    }

    private void showLoading() {
        mLoading.setVisibility(VISIBLE);
    }

    private void hideLoading() {
        mLoading.setVisibility(GONE);
    }

    private boolean isBackgroundPause;

    public void stop() {
        if (mMediaPlayer.isPlaying()) {
            //如果已经开始播放，则暂停记录状态
            isBackgroundPause = true;
            mMediaPlayer.pause();
        }
    }

    public void start() {
        if (isBackgroundPause) {
            //如果切换到后台播放，后又切回来，则继续播放
            isBackgroundPause = false;
            mMediaPlayer.start();
        }
    }

    public void onDestroy() {
        mMediaPlayer.stop();
        mediaController.release();
        unRegisterNetChangedReceiver();
    }

    public void startPlayVideo(ZYVideoInfo video) {
        if (video == null) {
            return;
        }
        mMediaPlayer.reset();

        mediaController.setVideoInfo(video);
        mMediaPlayer.setVideoPath(video.getVideoPath());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        mediaController.toggleDisplay();
        return super.onSingleTapUp(e);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        if (isLock()) {
            return false;
        }
        return super.onDown(event);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (isLock()) {
            return false;
        }
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    protected void endGesture(int behaviorType) {
        switch (behaviorType) {
            case ZYVideoBehaviorView.FINGER_BEHAVIOR_BRIGHTNESS:
            case ZYVideoBehaviorView.FINGER_BEHAVIOR_VOLUME:
                videoSystemOverlay.hide();
                break;
            case ZYVideoBehaviorView.FINGER_BEHAVIOR_PROGRESS:
                mMediaPlayer.seekTo(videoProgressOverlay.getTargetProgress());
                videoProgressOverlay.hide();
        }
    }

    @Override
    protected void updateSeekUI(int delProgress) {
        videoProgressOverlay.show(delProgress, mMediaPlayer.getCurrentPosition(), mMediaPlayer.getDuration());
    }

    @Override
    protected void updateVolumeUI(int max, int progress) {
        videoSystemOverlay.show(ZYVideoSystemOverlay.SystemType.VOLUME, max, progress);
    }

    @Override
    protected void updateLightUI(int max, int progress) {
        videoSystemOverlay.show(ZYVideoSystemOverlay.SystemType.BRIGHTNESS, max, progress);
    }

    public void setOnVideoControlListener(OnVideoControlListener onVideoControlListener) {
        mediaController.setOnVideoControlListener(onVideoControlListener);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getLayoutParams().width = initWidth;
            getLayoutParams().height = initHeight;
        } else {
            getLayoutParams().width = LayoutParams.MATCH_PARENT;
            getLayoutParams().height = LayoutParams.MATCH_PARENT;
        }
    }

    private NetChangedReceiver netChangedReceiver;

    public void registerNetChangedReceiver() {
        if (netChangedReceiver == null) {
            netChangedReceiver = new NetChangedReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            activity.registerReceiver(netChangedReceiver, filter);
        }
    }

    public void unRegisterNetChangedReceiver() {
        if (netChangedReceiver != null) {
            activity.unregisterReceiver(netChangedReceiver);
        }
    }

    private class NetChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Parcelable extra = intent.getParcelableExtra(ConnectivityManager.EXTRA_EXTRA_INFO);
            if (extra != null && extra instanceof NetworkInfo) {
                NetworkInfo networkInfo = (NetworkInfo) extra;
                if (ZYNetworkUtils.isConnected() && networkInfo.getState() != NetworkInfo.State.CONNECTED) {
                    return;
                }
                mediaController.checkShowError(true);
            }
        }
    }

}
