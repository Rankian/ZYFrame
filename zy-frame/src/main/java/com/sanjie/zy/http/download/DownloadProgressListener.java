package com.sanjie.zy.http.download;

/**
 * 下载成功进度回调
 * Created by SanJie on 2017/6/8.
 */

public interface DownloadProgressListener {

    /**
     * 下载进度
     * @param progress
     * @param count
     * @param completed
     */
    void update(long progress, long count, boolean completed);
}
