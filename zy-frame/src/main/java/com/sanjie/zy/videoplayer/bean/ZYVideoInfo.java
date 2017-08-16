package com.sanjie.zy.videoplayer.bean;

import java.io.Serializable;

/**
 * 视频数据类请实现本接口
 * Created by SanJie on 2017/6/22.
 */

public interface ZYVideoInfo extends Serializable {
    /**
     * 视频标题
     * @return
     */
    String getVideoTitle();

    /**
     * 视频播放路径
     * @return
     */
    String getVideoPath();
}
