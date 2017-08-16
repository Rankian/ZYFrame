package com.sanjie.zy.picture.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by LangSanJie on 2017/4/24.
 */

public class ImageFolder implements Serializable {

    private Integer id;

    private String name;

    private ArrayList<ImageItem> images = new ArrayList<>();

    private boolean isChecked;

    public ImageFolder() {
        super();
    }

    public ImageFolder(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ImageItem> getImages() {
        return images;
    }

    public void addPhoto(ImageItem photo){
        this.images.add(photo);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
