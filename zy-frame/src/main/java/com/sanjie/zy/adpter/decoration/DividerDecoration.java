package com.sanjie.zy.adpter.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.sanjie.zy.adpter.ZYRecyclerViewAdapter;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Created by LangSanJie on 2017/3/7.
 */

public class DividerDecoration extends RecyclerView.ItemDecoration {

    private ColorDrawable mColorDrawable;
    private int mHeight;
    private int mPaddingLeft;
    private int mPaddingRight;
    private boolean mDrawLastItem = true;
    private boolean mDrawHeaderFooter = false;

    public DividerDecoration(int color, int height) {
        this.mColorDrawable = new ColorDrawable(color);
        this.mHeight = height;
    }

    public DividerDecoration(int color, int height, int paddingLeft, int paddingRight) {
        this.mColorDrawable = new ColorDrawable(color);
        this.mHeight = height;
        this.mPaddingLeft = paddingLeft;
        this.mPaddingRight = paddingRight;
    }

    public void setDrawLastItem(boolean drawLastItem) {
        this.mDrawLastItem = drawLastItem;
    }

    public void setDrawHeaderFooter(boolean drawHeaderFooter) {
        this.mDrawHeaderFooter = drawHeaderFooter;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int orientation = 0;
        int headerCount = 0, footerCount = 0;
        if (parent.getAdapter() instanceof ZYRecyclerViewAdapter) {
            headerCount = ((ZYRecyclerViewAdapter) parent.getAdapter()).getHeaderCount();
            footerCount = ((ZYRecyclerViewAdapter) parent.getAdapter()).getFooterCount();
        }

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof GridLayoutManager) {
            orientation = ((GridLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof LinearLayoutManager) {
            orientation = ((LinearLayoutManager) layoutManager).getOrientation();
        }

        if (position >= headerCount && position < parent.getAdapter().getItemCount() - footerCount || mDrawHeaderFooter) {
            if (orientation == OrientationHelper.VERTICAL) {
                outRect.bottom = mHeight;
            } else {
                outRect.right = mHeight;
            }
        }
    }

    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getAdapter() == null) {
            return;
        }

        int orientation = 0;
        int headerCount = 0, footerCount = 0, dataCount;

        if (parent.getAdapter() instanceof ZYRecyclerViewAdapter) {
            headerCount = ((ZYRecyclerViewAdapter) parent.getAdapter()).getHeaderCount();
            footerCount = ((ZYRecyclerViewAdapter) parent.getAdapter()).getFooterCount();
            dataCount = ((ZYRecyclerViewAdapter) parent.getAdapter()).getDataCount();
        } else {
            dataCount = parent.getAdapter().getItemCount();
        }

        int dataStartPosition = headerCount;
        int dataEndPosition = headerCount = dataCount;

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof GridLayoutManager) {
            orientation = ((GridLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof LinearLayoutManager) {
            orientation = ((LinearLayoutManager) layoutManager).getOrientation();
        }

        int start, end;
        if (orientation == OrientationHelper.VERTICAL) {
            start = parent.getPaddingLeft() + mPaddingLeft;
            end = parent.getWidth() - parent.getPaddingRight() - mPaddingRight;
        } else {
            start = parent.getPaddingTop() + mPaddingLeft;
            end = parent.getHeight() - parent.getPaddingBottom() - mPaddingRight;
        }

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);

            if (position >= dataStartPosition && position < dataEndPosition - 1//数据清楚了最后一项
                    || (position == dataEndPosition - 1 && mDrawLastItem)//数据项最后一项
                    || (!(position >= dataStartPosition && position < dataEndPosition) && mDrawHeaderFooter)) {//header & footer且可绘制
                if (orientation == OrientationHelper.VERTICAL) {
                    RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
                    int top = child.getBottom() + lp.bottomMargin;
                    int bottom = top + mHeight;
                    mColorDrawable.setBounds(start, top, end, bottom);
                    mColorDrawable.draw(c);
                } else {
                    RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
                    int left = child.getRight() + lp.rightMargin;
                    int right = left + mHeight;
                    mColorDrawable.setBounds(start, left, right, end);
                    mColorDrawable.draw(c);
                }
            }
        }
    }
}
