package com.sanjie.zy.widget.bottom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sanjie.zy.R;
import com.sanjie.zy.utils.ZYDisplayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LangSanJie on 2017/3/20.
 */

public class ZYBottomDialog extends Dialog {

    private Context mContext;

    private View contentView;
    private LayoutInflater mInflater;

    private LinearLayout mItemContainer;

    private boolean isCircle;

    private MenuItem menuItem;

    private List<MenuItem> items;

    private OnBottomDialogItemClickListener bottomDialogItemClickListener;

    public void setBottomDialogItemClickListener(OnBottomDialogItemClickListener bottomDialogItemClickListener) {
        this.bottomDialogItemClickListener = bottomDialogItemClickListener;
    }

    public interface OnBottomDialogItemClickListener {
        void onBottomDialogItemClick(MenuItem menu, int position);
    }

    public ZYBottomDialog(@NonNull Context context) {
        super(context, R.style.BottomDialog);
        this.mContext = context;
        contentView = getWindow().getDecorView().findViewById(android.R.id.content);
        mInflater = LayoutInflater.from(mContext);

        items = new ArrayList<>();

        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_dialog_content_normal);

        mItemContainer = (LinearLayout) findViewById(R.id.container);
        addItem(menuItem);
        setMenuItems(items);
        setBackgroundWithCircle(isCircle);
        layout();
    }

    public ZYBottomDialog addItem(final MenuItem menu) {
        menuItem = menu;
        if (menuItem != null && mItemContainer != null) {
            View itemView = null;
            LinearLayout menuItemBtn = null;
            TextView menuNameTV = null;
            ImageView menuIconIv = null;

            itemView = mInflater.inflate(R.layout.ic_bottom_dialog_item_layout, null, false);
            menuItemBtn = (LinearLayout) itemView.findViewById(R.id.menu_item_btn);
            menuNameTV = (TextView) itemView.findViewById(R.id.menu_name_tv);
            menuIconIv = (ImageView) itemView.findViewById(R.id.menu_icon_iv);
            menuNameTV.setText(menu.name);
            //本地资源文件
            if (menuItem.iconResId > 0) {
                menuIconIv.setImageResource(menuItem.iconResId);
            }
            //采用图片地址
            if (!TextUtils.isEmpty(menuItem.iconPath)) {
                Glide.with(mContext).load(menuItem.iconPath).into(menuIconIv).onLoadStarted(mContext.getResources().getDrawable(R.drawable.ic_android));
            }
            itemView.setTag(mItemContainer.getChildCount());

            final View finalItemView = itemView;
            menuItemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bottomDialogItemClickListener != null) {
                        bottomDialogItemClickListener.onBottomDialogItemClick(menu, (Integer) finalItemView.getTag());
                    }
                }
            });
            mItemContainer.addView(itemView);
        }
        return this;
    }

    public ZYBottomDialog setMenuItems(@NonNull List<MenuItem> menuItems) {
        if (items.size() == 0) {
            items.addAll(menuItems);
        }
        if (mItemContainer != null) {
            mItemContainer.removeAllViews();
            for (int i = 0; i < items.size(); i++) {
                MenuItem menu = items.get(i);
                addItem(menu);
            }
        }
        return this;
    }

    public ZYBottomDialog setBackgroundWithCircle(boolean backgroundWithCircle) {
        if (mItemContainer != null) {
            isCircle = backgroundWithCircle;
            mItemContainer.setBackgroundResource(backgroundWithCircle ? R.drawable.shape_bottom_dialog_circle : R.color.white);
        }
        return this;
    }

    public void layout() {
        if (isCircle) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
            lp.width = mContext.getResources().getDisplayMetrics().widthPixels - ZYDisplayUtils.dp2px(16f);
            lp.bottomMargin = ZYDisplayUtils.dp2px(8f);
            contentView.setLayoutParams(lp);
        } else {
            ViewGroup.LayoutParams lp = contentView.getLayoutParams();
            lp.width = mContext.getResources().getDisplayMetrics().widthPixels;
            contentView.setLayoutParams(lp);
        }
    }
}
