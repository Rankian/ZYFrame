package com.sanjie.zy.picture.ui.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.sanjie.zy.picture.ZYPicturePickerManager;
import com.sanjie.zy.picture.bean.ImageItem;

import java.util.List;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by LangSanJie on 2017/4/25.
 */

public class ZYResultHandlerFragment extends Fragment {

    PublishSubject<List<ImageItem>> resultSubject = PublishSubject.create();
    BehaviorSubject<Boolean> attachSubject = BehaviorSubject.create();

    public static final int REQUEST_CODE = 0x00100;

    public static ZYResultHandlerFragment newInstance() {
        return new ZYResultHandlerFragment();
    }

    public PublishSubject<List<ImageItem>> getResultSubject() {
        return resultSubject;
    }

    public BehaviorSubject<Boolean> getAttachSubject() {
        return attachSubject;
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            resultSubject.onNext(ZYPicturePickerManager.getInstance().getResult(data));
        }
    }

    @TargetApi(23) @Override public void onAttach(Context context) {
        super.onAttach(context);
        attachSubject.onNext(true);
    }

    @SuppressWarnings("deprecation") @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < 23) {
            attachSubject.onNext(true);
        }
    }
}
