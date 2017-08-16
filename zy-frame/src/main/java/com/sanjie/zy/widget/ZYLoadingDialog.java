package com.sanjie.zy.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sanjie.zy.R;

/**
 * Created by LangSanJie on 2017/3/8.
 */

public class ZYLoadingDialog extends Dialog {

    private static ZYLoadingDialog dialog;
    private Context context;
    private TextView mTextView;
    private ProgressBar mProgressBar;

    private String message;
    private Drawable indeterminateDrawable;

    public ZYLoadingDialog(@NonNull Context context) {
        super(context, R.style.loading_dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
        mProgressBar = (ProgressBar) findViewById(R.id.loading_progressbar);
        mTextView = (TextView) findViewById(R.id.loading_message);
        setIndeterminateDrawable(indeterminateDrawable);
        setMessage(message);
    }

    public static ZYLoadingDialog with(Context context) {
        if (dialog == null) {
            dialog = new ZYLoadingDialog(context);
        }
        return dialog;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (dialog != null)
            dialog = null;
    }

    public ZYLoadingDialog setIndeterminateDrawable(Drawable drawable){
        indeterminateDrawable = drawable;
        if(mProgressBar != null && indeterminateDrawable != null){
            mProgressBar.setIndeterminateDrawable(indeterminateDrawable);
        }
        return dialog;
    }

    public ZYLoadingDialog setMessage(String msg){
        this.message = msg;
        if(mTextView != null && !TextUtils.isEmpty(message)){
            mTextView.setText(message);
        }
        return this;
    }

    public ZYLoadingDialog setCanceled(boolean canceled){
        setCanceledOnTouchOutside(canceled);
        setCancelable(canceled);
        return dialog;
    }
}
