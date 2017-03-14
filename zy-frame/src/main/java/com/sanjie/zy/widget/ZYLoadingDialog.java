package com.sanjie.zy.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.TextView;

import com.sanjie.zy.R;

/**
 * Created by LangSanJie on 2017/3/8.
 */

public class ZYLoadingDialog extends Dialog {

    private static ZYLoadingDialog dialog;
    private Context context;
    private TextView mTextView;

    public ZYLoadingDialog(@NonNull Context context) {
        super(context, R.style.loading_dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
        mTextView = (TextView) findViewById(R.id.loading_message);
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

    public ZYLoadingDialog setMessage(String message){
        if(mTextView != null && !TextUtils.isEmpty(message)){
            mTextView.setText(message);
        }
        return this;
    }

    public ZYLoadingDialog setCanceled(boolean canceled){
        setCanceledOnTouchOutside(canceled);
        setCanceled(canceled);
        return dialog;
    }
}
