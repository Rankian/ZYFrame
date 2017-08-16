package com.sanjie.zy.videoplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;

import com.sanjie.zy.utils.ZYEmptyUtils;
import com.sanjie.zy.videoplayer.listener.PlayerCallback;

import java.io.IOException;

/**
 * 只包含最基础的播放器功能，MediaPlayer可以替换成其他框架的播放器
 * Created by SanJie on 2017/6/22.
 */

public class ZYVideoPlayer {

    private static final String TAG = "ZYVideoPlayer";

    public static final int STATE_ERROR = -1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_PREPARING = 1;
    public static final int STATE_PREPARED = 2;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_PAUSED = 4;
    public static final int STATE_PLAY_BACK_COMPLEDTED = 5;

    private MediaPlayer player;
    private int currentState = STATE_IDLE;

    private PlayerCallback callback;
    private int currentBufferPercentage;
    private String path;
    private SurfaceHolder surfaceHolder;

    public void setCallback(PlayerCallback callback) {
        this.callback = callback;
    }

    private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            setCurrentState(STATE_ERROR);
            if (callback != null) {
                callback.onError(player, what, extra);
            }
            return true;
        }
    };

    public ZYVideoPlayer() {
        setCurrentState(STATE_IDLE);
    }

    public void setDisplay(SurfaceHolder display) {
        this.surfaceHolder = display;
    }

    public void setVideoPath(String path) {
        this.path = path;
        openVideo();
    }

    public String getPath() {
        return path;
    }

    public void openVideo() {
        if (ZYEmptyUtils.isEmpty(path) || surfaceHolder == null) {
            return;
        }
        reset();

        try {
            player = new MediaPlayer();
            player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    currentBufferPercentage = percent;
                    if(callback != null) callback.onBufferingUpdate(mp, percent);
                }
            });
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    setCurrentState(STATE_PLAY_BACK_COMPLEDTED);
                    if(callback != null) callback.onCompleted(mp);
                }
            });
            player.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    if(callback != null){
                        if(what == MediaPlayer.MEDIA_INFO_BUFFERING_START){
                            callback.onLoadingChanged(true);
                        }else if(what == MediaPlayer.MEDIA_INFO_BUFFERING_END){
                            callback.onLoadingChanged(false);
                        }
                    }
                    return false;
                }
            });
            player.setOnErrorListener(onErrorListener);
            player.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    if(callback != null) callback.onVideoSizeChanged(mp, width, height);
                }
            });
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    setCurrentState(STATE_PREPARED);
                    if(callback != null){
                        callback.onPrepared(mp);
                    }
                }
            });
            currentBufferPercentage = 0;
            player.setDataSource(path);
            player.setDisplay(surfaceHolder);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setScreenOnWhilePlaying(true);
            player.prepareAsync();
        }catch (IOException | IllegalArgumentException ex){
            setCurrentState(STATE_ERROR);
            onErrorListener.onError(player,MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
        }
    }

    public void start() {
        if (isInPlaybackState()){
            player.start();
            setCurrentState(STATE_PLAYING);
        }
    }

    public void restart(){
        openVideo();
    }

    public void reset(){
        if (player != null) {
            player.reset();
            player.release();
            setCurrentState(STATE_IDLE);
        }
    }

    public void pause(){
        if (isInPlaybackState()){
            if(player.isPlaying()){
                player.pause();
                setCurrentState(STATE_PAUSED);
            }
        }
    }

    private void setCurrentState(int state) {
        currentState = state;
        if (callback != null) {
            callback.onStateChanged(currentState);
            switch (state) {
                case STATE_IDLE:
                case STATE_ERROR:
                case STATE_PREPARED:
                    callback.onLoadingChanged(false);
                    break;
                case STATE_PREPARING:
                    callback.onLoadingChanged(true);
                    break;
            }
        }
    }

    public void stop(){
        if(player != null){
            player.stop();
            player.release();
            player = null;
            surfaceHolder = null;
            setCurrentState(STATE_IDLE);
        }
    }

    public int getDuration(){
        return isInPlaybackState() ? player.getDuration() : 0;
    }

    public int getCurrentPosition(){
        return isInPlaybackState() ? player.getCurrentPosition() : 0;
    }

    public void seekTo(int progress){
        if(isInPlaybackState()){
            player.seekTo(progress);
        }
    }

    public boolean isPlaying(){
        return isInPlaybackState() && player.isPlaying();
    }

    public int getBufferPercentage() {
        return player != null ? currentBufferPercentage : 0;
    }

    public boolean isInPlaybackState() {
        return (player != null &&
                currentState != STATE_ERROR &&
                currentState != STATE_IDLE &&
                currentState != STATE_PREPARING);
    }
}
