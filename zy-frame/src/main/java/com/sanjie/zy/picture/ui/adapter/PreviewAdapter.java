package com.sanjie.zy.picture.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;

import com.sanjie.zy.picture.ZYPicturePickerManager;
import com.sanjie.zy.picture.bean.ImageItem;
import com.sanjie.zy.utils.ZYDisplayUtils;

import java.util.List;

/**
 * Created by LangSanJie on 2017/4/25.
 */

public class PreviewAdapter extends PagerAdapter {

    private List<ImageItem> data;

    public PreviewAdapter(List<ImageItem> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        AppCompatImageView imageView = new AppCompatImageView(container.getContext());
        ViewPager.LayoutParams lp = new ViewPager.LayoutParams();
        imageView.setLayoutParams(lp);
        ImageItem imageItem = data.get(position);
        container.addView(imageView);
        int deviceWidth = ZYDisplayUtils.getScreenWidth();
        ZYPicturePickerManager.getInstance().display(imageView,imageItem.getPath(), deviceWidth, deviceWidth);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
