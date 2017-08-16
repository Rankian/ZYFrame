package com.sanjie.zy.widget.marquee;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.sanjie.zy.R;
import com.sanjie.zy.utils.ZYDisplayUtils;
import com.sanjie.zy.utils.ZYEmptyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 跑马灯
 * Created by LangSanJie on 2017/5/4.
 */

public class ZYMarqueeView extends ViewFlipper {

    private Context mContext;
    private List<String> notices;
    private boolean isSetAnimDuration = false;

    private OnItemClickListener onItemClickListener;

    private int interval = 2000;
    private int animDuration = 500;
    private int textSize = 14;
    private int textColor = 0xffffffff;

    private boolean singleLine = false;
    private int gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
    private static final int TEXT_GRAVITY_LEFT = 0, TEXT_GRAVITY_CENTER = 1, TEXT_GRAVITY_RIGHT = 2;

    public ZYMarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        if (notices == null) {
            notices = new ArrayList<>();
        }

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ZYMarqueeViewStyle, defStyleAttr, 0);
        interval = a.getInteger(R.styleable.ZYMarqueeViewStyle_mvInterval, interval);
        isSetAnimDuration = a.hasValue(R.styleable.ZYMarqueeViewStyle_mvAnimDuration);
        animDuration = a.getInteger(R.styleable.ZYMarqueeViewStyle_mvAnimDuration, animDuration);
        singleLine = a.getBoolean(R.styleable.ZYMarqueeViewStyle_mvSingleLine, singleLine);

        if (a.hasValue(R.styleable.ZYMarqueeViewStyle_mvTextSize)) {
            textSize = (int) a.getDimension(R.styleable.ZYMarqueeViewStyle_mvTextSize, textSize);
            textSize = ZYDisplayUtils.px2sp(textSize);
        }
        textColor = a.getColor(R.styleable.ZYMarqueeViewStyle_mvTextColor, textColor);
        int gravityType = a.getInt(R.styleable.ZYMarqueeViewStyle_mvGravity, TEXT_GRAVITY_LEFT);
        switch (gravityType) {
            case TEXT_GRAVITY_CENTER:
                gravity = Gravity.CENTER;
                break;
            case TEXT_GRAVITY_RIGHT:
                gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                break;
        }
        a.recycle();
        setFlipInterval(interval);
    }

    public void startWithText(final String notice) {
        if (ZYEmptyUtils.isEmpty(notice)) return;
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                startWithFixedWidth(notice, getWidth());
            }
        });
    }

    public void startWithList(List<String> notices) {
        setNotices(notices);
        start();
    }

    /**
     * 根据宽度和公告字符串启动轮播
     *
     * @param notice
     * @param width
     */
    private void startWithFixedWidth(String notice, int width) {
        int noticeLength = notice.length();
        int dpw = ZYDisplayUtils.px2dp(width);
        int limit = dpw / textSize;
        if (dpw == 0) {
            throw new RuntimeException("Please set MarqueeView's width !");
        }
        List<String> list = new ArrayList<>();
        if (noticeLength <= limit) {
            list.add(notice);
        } else {
            int size = noticeLength / limit + (noticeLength % limit != 0 ? 1 : 0);
            for (int i = 0; i < size; i++) {
                int startIndex = i * limit;
                int endIndex = ((i + 1) * limit <= noticeLength ? noticeLength : (i + 1) * limit);
                list.add(notice.substring(startIndex, endIndex));
            }
        }

        notices.addAll(list);
        start();
    }

    /**
     * 开始轮播notice
     *
     * @return
     */
    public boolean start() {

        if (notices == null || notices.size() == 0) return false;

        removeAllViews();
        resetAnimation();

        for (int i = 0; i < notices.size(); i++) {
            final TextView textView = createTextView(notices.get(i), i);
            final int position = i;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position, textView);
                    }
                }
            });
            addView(textView);
        }

        if (notices.size() > 1) {
            startFlipping();
        } else {
            stopFlipping();
        }

        return true;
    }

    /**
     * 重置动画
     */
    private void resetAnimation() {
        clearAnimation();

        Animation animIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_in);
        if (isSetAnimDuration) animIn.setDuration(animDuration);
        setInAnimation(animIn);

        Animation animOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_out);
        if (isSetAnimDuration) animOut.setDuration(animDuration);
        setOutAnimation(animOut);
    }

    /**
     * 为ViewFlipper 创建TextView
     *
     * @param text
     * @param position
     * @return
     */
    private TextView createTextView(CharSequence text, int position) {
        TextView tv = new TextView(mContext);
        tv.setGravity(gravity);
        tv.setText(text);
        tv.setTextColor(textColor);
        tv.setTextSize(textSize);
        tv.setSingleLine(singleLine);
        tv.setTag(position);
        return tv;
    }

    public int getPosition() {
        return (int) getCurrentView().getTag();
    }

    public List<String> getNotices() {
        return notices;
    }

    public void setNotices(List<String> notices) {
        this.notices = notices;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, TextView textView);
    }
}
