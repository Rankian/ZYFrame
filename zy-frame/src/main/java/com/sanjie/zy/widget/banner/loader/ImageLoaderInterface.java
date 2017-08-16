package com.sanjie.zy.widget.banner.loader;

import android.content.Context;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by LangSanJie on 2017/3/2.
 */

public interface ImageLoaderInterface extends Serializable {

    void displayImage(Context context, Object o, ImageView imageView);

    ImageView createImageView(Context context);
}
