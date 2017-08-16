package com.sanjie.zy.widget.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by SanJie on 2017/6/12.
 */

public class ZYCompoundIconTextView extends AppCompatTextView {

    public static final int UNDEFINED_RESOURCE = -0x001;

    private static final int INDEX_LEFT = 0;
    private static final int INDEX_TOP = 1;
    private static final int INDEX_RIGHT = 2;
    private static final int INDEX_BOTTOM = 3;

    public ZYCompoundIconTextView(Context context) {
        this(context, null);
    }

    public ZYCompoundIconTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZYCompoundIconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
