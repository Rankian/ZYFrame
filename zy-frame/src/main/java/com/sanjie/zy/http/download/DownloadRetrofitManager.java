package com.sanjie.zy.http.download;

import com.sanjie.zy.http.converter.JsonConverterFactory;
import com.sanjie.zy.http.exception.HttpTimeException;
import com.sanjie.zy.http.exception.RetryWhenNetworkException;
import com.sanjie.zy.http.service.DownloadService;
import com.sanjie.zy.rx.android.schedulers.AndroidSchedulers;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by SanJie on 2017/6/8.
 */

public class DownloadRetrofitManager {

    /*
    记录下载数据
     */
    private Set<DownloadInfo> downloadInfos;
    /*
    回调队列
     */
    private HashMap<String, DownloadProgressObserver> subMap;

    private volatile static DownloadRetrofitManager INSTANCE;

    public DownloadRetrofitManager() {
        downloadInfos = new HashSet<>();
        subMap = new HashMap<>();
    }

    public static DownloadRetrofitManager getInstance() {
        if (INSTANCE == null) {
            synchronized (DownloadRetrofitManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DownloadRetrofitManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 开始下载
     *
     * @param info
     */
    public void startDownload(final DownloadInfo info) {
        /*
        正在下载不处理
         */
        if (info == null || subMap.get(info.getUrl()) != null) {
            return;
        }
        //添加回调处理
        DownloadProgressObserver observer = new DownloadProgressObserver(info);
        //记录回调sub
        subMap.put(info.getUrl(), observer);
        //获取service,措辞请求共拥有一个service
        DownloadService service;
        if (downloadInfos.contains(info)) {
            service = info.getService();
        } else {
            DownloadInterceptor interceptor = new DownloadInterceptor(observer);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(info.getConnectionTime(), TimeUnit.SECONDS);
            builder.addInterceptor(interceptor);

            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .addConverterFactory(JsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(info.getBaseUrl())
                    .build();
            service = retrofit.create(DownloadService.class);
            info.setService(service);
        }
        //从上次下载位置开始下载
        service.download("bytes=" + info.getReadLength() + " -", info.getUrl())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .retryWhen(new RetryWhenNetworkException())
                .map(new Function<ResponseBody, DownloadInfo>() {
                    @Override
                    public DownloadInfo apply(@NonNull ResponseBody responseBody) throws Exception {
                        try {
                            writeFile(responseBody, new File(info.getSavePath()), info);
                        } catch (IOException e) {
                            throw new HttpTimeException(e.getMessage());
                        }
                        return info;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 停止下载
     * @param info
     */
    public void stopDownload(DownloadInfo info){
        if(info == null) return;
        info.setState(DownloadState.STOP);
        info.getListener().onStop();
        if(subMap.containsKey(info.getUrl())){
            DownloadProgressObserver observer = subMap.get(info.getUrl());
            subMap.remove(info.getUrl());
        }
    }
    public void deleteDownload(DownloadInfo info){
        stopDownload(info);
        //删除文件
    }

    /**
     * 暂定下载
     * @param info
     */
    public void pause(DownloadInfo info){
        if (info == null) return;
        info.setState(DownloadState.PAUSE);
        info.getListener().onPuase();
        if(subMap.containsKey(info.getUrl())){
            DownloadProgressObserver observer = subMap.get(info.getUrl());
            subMap.remove(info.getUrl());
        }
    }

    /**
     * 停止全部下载
     */
    public void stopAllDownload(){
        for (DownloadInfo info : downloadInfos){
            stopDownload(info);
        }
        subMap.clear();
        downloadInfos.clear();
    }

    public void pauseAllDownload(){
        for (DownloadInfo info : downloadInfos){
            pause(info);
        }
        subMap.clear();
        downloadInfos.clear();
    }

    public Set<DownloadInfo> getDownloadInfos() {
        return downloadInfos;
    }

    /**
     * 保存文件
     *
     * @param responseBody
     * @param file
     * @param info
     * @throws IOException
     */
    public void writeFile(ResponseBody responseBody, File file, DownloadInfo info) throws IOException {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        long allLength;
        if (info.getCountLength() == 0) {
            allLength = responseBody.contentLength();
        } else {
            allLength = info.getCountLength();
        }
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
        FileChannel channel = randomAccessFile.getChannel();

        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE,
                info.getReadLength(), allLength - info.getReadLength());
        byte[] buffer = new byte[1024 * 8];
        int len;
        int record = 0;
        while ((len = responseBody.byteStream().read(buffer)) != -1) {
            mappedByteBuffer.put(buffer, 0, len);
            record += len;
        }
        responseBody.byteStream().close();
        channel.close();
        randomAccessFile.close();

    }

}
