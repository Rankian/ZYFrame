package com.sanjie.zy.widget.bottom;

import android.support.annotation.NonNull;

/**
 * Created by LangSanJie on 2017/3/20.
 */

public class MenuItem {

    public int iconResId;

    public String iconPath;

    public String name;

    public MenuItem(int iconResId, @NonNull String name) {
        this.iconResId = iconResId;
        this.name = name;
    }

    public MenuItem(@NonNull String iconPath, @NonNull String name) {
        this.iconPath = iconPath;
        this.name = name;
    }
}
