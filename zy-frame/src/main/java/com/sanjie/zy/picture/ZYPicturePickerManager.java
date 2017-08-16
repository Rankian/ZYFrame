package com.sanjie.zy.picture;

import android.content.Intent;
import android.widget.ImageView;

import com.sanjie.zy.picture.bean.ImageItem;
import com.sanjie.zy.picture.ui.fragment.ZYPickerFragment;
import com.sanjie.zy.utils.ZYPickerImageLoader;

import java.util.List;

/**
 * Created by LangSanJie on 2017/4/25.
 */

public class ZYPicturePickerManager {

    private PickerConfig config;
    private static ZYPicturePickerManager manager;

    private ZYPickerImageLoader imageLoader;

    public static ZYPicturePickerManager getInstance(){
        if(manager == null) {
            synchronized (ZYPicturePickerManager.class) {
                if (manager == null) {
                    manager = new ZYPicturePickerManager();
                }
            }
        }
        return manager;
    }

    public ZYPicturePickerManager() {
    }

    ZYPicturePickerManager setConfig(PickerConfig config){
        this.config = config;
        return this;
    }

    public PickerConfig getConfig(){
        return config;
    }

    void init(ZYPickerImageLoader imageLoader){
        this.imageLoader = imageLoader;
    }

    void setMode(PickerConfig.Mode mode){
        config.setMode(mode);
    }

    void showCamera(boolean showCamera){
        config.setShowCamera(showCamera);
    }

    void limit(int limitValue){
        config.setMaxValue(limitValue);
    }

    public void display(ImageView imageView, String path, int width, int height){
        if(imageLoader == null){
            throw new NullPointerException("You must fist of all call 'ZYPicturePickerManager.getInstance().init()' to initialize");
        }
        imageLoader.display(imageView, path, width, height);
    }

    public List<ImageItem> getResult(Intent intent){
        return (List<ImageItem>) intent.getSerializableExtra(ZYPickerFragment.MEDIA_RESULT);
    }
}
