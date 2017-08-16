package com.sanjie.zy.picture.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.sanjie.zy.R;
import com.sanjie.zy.adpter.ZYRecyclerViewAdapter;
import com.sanjie.zy.adpter.ZYViewHolder;
import com.sanjie.zy.picture.ZYPicturePickerManager;
import com.sanjie.zy.picture.bean.FolderClickEvent;
import com.sanjie.zy.picture.bean.ImageFolder;
import com.sanjie.zy.utils.RxBus;

import java.util.List;

/**
 * Created by LangSanJie on 2017/4/25.
 */

public class PickerAlbumAdapter extends ZYRecyclerViewAdapter<ImageFolder> {

    private int imageWidth;
    private List<ImageFolder> datas;
    private int checkPosition = 0;

    private View.OnClickListener dismissListener;

    public PickerAlbumAdapter(@NonNull RecyclerView mRecyclerView, List<ImageFolder> dataLists, int i) {
        super(mRecyclerView, dataLists, R.layout.item_album);
        this.datas = dataLists;
        this.imageWidth = i;
    }

    @Override
    protected void bindData(ZYViewHolder holder, final ImageFolder data, final int position) {
        String path = data.getImages().get(0).getPath();
        holder.setText(R.id.tv_album_name, data.getName());

        ImageView ivPreView = holder.getView(R.id.iv_preview);
        ImageView icCheck = holder.getView(R.id.iv_check);

        ZYPicturePickerManager.getInstance().display(ivPreView, path, imageWidth, imageWidth);
        icCheck.setVisibility(data.isChecked() ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dismissListener != null) {
                    dismissListener.onClick(v);
                }
                if (checkPosition == position) return;

                ImageFolder newFolder = datas.get(position);
                ImageFolder oldFolder = datas.get(checkPosition);

                oldFolder.setChecked(false);
                newFolder.setChecked(true);
                notifyItemChanged(checkPosition);
                notifyItemChanged(position);

                checkPosition = position;

                RxBus.singleton().post(new FolderClickEvent(position, newFolder));
            }
        });
    }

    public void setDismissListener(View.OnClickListener dismissListener) {
        this.dismissListener = dismissListener;
    }
}
