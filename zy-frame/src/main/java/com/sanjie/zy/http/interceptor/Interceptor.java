package com.sanjie.zy.http.interceptor;

import com.sanjie.zy.utils.ZYNetworkUtils;
import com.sanjie.zy.utils.log.ZYLog;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Request;
import okhttp3.Response;

/**
 * OkHttp 拦截器
 * 1、有网络从网络获取 无网络获取缓存
 * 2、无论有无网 都获取缓存
 * Created by LangSanJie on 2017/3/28.
 */

public class Interceptor {

    private static final String TAG = "Interceptor";

    private static okhttp3.Interceptor interceptor = null;

    public static okhttp3.Interceptor cacheInterceptor(){
        interceptor = new okhttp3.Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();//获取请求
                //没有网络的时候强制使用缓存数据
                if(!ZYNetworkUtils.isConnected()){
                    request = request.newBuilder()
                            //强制使用缓存数据
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                    ZYLog.d("no network, is from cache");
                }
                Response originalResponse = chain.proceed(request);
                if(ZYNetworkUtils.isConnected()){
                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + 0)
                            .build();
                }else{
                    //设置无网络的缓存时间
                    int maxTime = 4 * 24 * 60 *60;
                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxTime)
                            .build();
                }
            }
        };
        return interceptor;
    }

    public static okhttp3.Interceptor onlyCacheInterceptor(){
        //有无网络都获取缓存数据
        interceptor = new okhttp3.Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                int maxAge = 60;
                return response
                        .newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            }
        };
        return interceptor;
    }
}
