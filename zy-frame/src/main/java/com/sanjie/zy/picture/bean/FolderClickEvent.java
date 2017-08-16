package com.sanjie.zy.picture.bean;

/**
 * Created by LangSanJie on 2017/4/24.
 */

public class FolderClickEvent {

    private int position;

    private ImageFolder folder;

    public FolderClickEvent(int position, ImageFolder folder) {
        this.position = position;
        this.folder = folder;
    }

    public int getPosition() {
        return position;
    }

    public ImageFolder getFolder() {
        return folder;
    }
}
