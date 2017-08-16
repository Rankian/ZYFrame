package com.sanjie.zy.widget.banner.loader;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by LangSanJie on 2017/3/2.
 */

public abstract class ImageLoader implements ImageLoaderInterface {
    @Override
    public ImageView createImageView(Context context) {
        return new ImageView(context);
    }
}
