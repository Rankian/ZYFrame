package com.sanjie.zy.picture.ui.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sanjie.zy.R;
import com.sanjie.zy.picture.bean.ImageFolder;
import com.sanjie.zy.picture.ui.adapter.PickerAlbumAdapter;
import com.sanjie.zy.utils.ZYDisplayUtils;

import java.util.List;

/**
 * Created by LangSanJie on 2017/4/25.
 */

public class PopWindowManager {
    private PopupWindow mAlbumPopWindow;

    private List<ImageFolder> datas;

    public void init(final TextView title, final List<ImageFolder> data) {
        this.datas = data;
        title.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                showPopWindow(v);
            }
        });
    }

    private void showPopWindow(View v) {
        if (mAlbumPopWindow == null) {
            int height = ZYDisplayUtils.dp2px(300);
            View windowView = createWindowView(v);
            mAlbumPopWindow =
                    new PopupWindow(windowView, ViewGroup.LayoutParams.MATCH_PARENT, height, true);
            mAlbumPopWindow.setAnimationStyle(R.style.RxPicker_PopupAnimation);
            mAlbumPopWindow.setContentView(windowView);
            mAlbumPopWindow.setOutsideTouchable(true);
        }
        mAlbumPopWindow.showAsDropDown(v, 0, 0);
    }

    @NonNull
    private View createWindowView(View clickView) {
        View view =
                LayoutInflater.from(clickView.getContext()).inflate(R.layout.item_popwindow_album, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.album_recycleview);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        View albumShadowLayout = view.findViewById(R.id.album_shadow);
        albumShadowLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismissAlbumWindow();
            }
        });

        PickerAlbumAdapter albumAdapter = new PickerAlbumAdapter(recyclerView, datas, ZYDisplayUtils.dp2px(80));
        albumAdapter.setDismissListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismissAlbumWindow();
            }
        });

        recyclerView.setAdapter(albumAdapter);
        return view;
    }

    private void dismissAlbumWindow() {
        if (mAlbumPopWindow != null && mAlbumPopWindow.isShowing()) {
            mAlbumPopWindow.dismiss();
        }
    }
}

