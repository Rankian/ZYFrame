package com.sanjie.zy.picture.bean;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by LangSanJie on 2017/4/24.
 */

public class ImageItem implements Serializable, Comparable<ImageItem> {

    private Integer id;

    private String path;

    private String name;

    private long addTime;

    public ImageItem(Integer id, String path, String name, long addTime) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.addTime = addTime;
    }

    public ImageItem() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    @Override
    public int compareTo(@NonNull ImageItem o) {
        long time = o.getAddTime() - getAddTime();
        if(time > Integer.MAX_VALUE){
            return Integer.MAX_VALUE;
        }else if(time < - Integer.MAX_VALUE){
            return - Integer.MAX_VALUE;
        }
        return (int) time;
    }
}
