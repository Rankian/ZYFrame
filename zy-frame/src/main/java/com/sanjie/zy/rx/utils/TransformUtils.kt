package com.sanjie.zy.rx.utils

import com.sanjie.zy.rx.android.schedulers.AndroidSchedulers
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers

/**
 * </p>
 * 创建者:SanJie
 * 时间:2019/8/6
 */
object TransformUtils {

    fun <T> defaultSchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> defaultNewThreadSchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }
}