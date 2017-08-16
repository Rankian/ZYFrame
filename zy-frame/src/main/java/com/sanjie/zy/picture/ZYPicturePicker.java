package com.sanjie.zy.picture;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;

import com.sanjie.zy.picture.bean.ImageItem;
import com.sanjie.zy.picture.ui.ZYPickerActivity;
import com.sanjie.zy.picture.ui.fragment.ZYResultHandlerFragment;
import com.sanjie.zy.utils.ZYPickerImageLoader;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.internal.util.AppendOnlyLinkedArrayList;

/**
 * Created by LangSanJie on 2017/4/25.
 */

public class ZYPicturePicker {

    private ZYPicturePicker(PickerConfig config){
        ZYPicturePickerManager.getInstance().setConfig(config);
    }

    static ZYPicturePicker of(PickerConfig config){
        return new ZYPicturePicker(config);
    }

    public static ZYPicturePicker of(){
        return new ZYPicturePicker(new PickerConfig());
    }

    public ZYPicturePicker single(boolean isSingle){
        ZYPicturePickerManager.getInstance().setMode(isSingle ? PickerConfig.Mode.SINGLE_IMG : PickerConfig.Mode.MULTIPLE_IMG);
        return this;
    }

    public ZYPicturePicker camera(boolean showCamera){
        ZYPicturePickerManager.getInstance().showCamera(showCamera);
        return this;
    }

    public ZYPicturePicker limit(int limit){
        ZYPicturePickerManager.getInstance().limit(limit);
        return this;
    }

    public Observable<List<ImageItem>> start(Activity activity){
        return start(activity.getFragmentManager());
    }

    public Observable<List<ImageItem>> start(Fragment fragment) {
        return start(fragment.getFragmentManager());
    }

    private Observable<List<ImageItem>> start(FragmentManager fragmentManager){
        ZYResultHandlerFragment fragment = (ZYResultHandlerFragment) fragmentManager.findFragmentByTag(ZYResultHandlerFragment.class.getSimpleName());
        if(fragment == null){
            fragment = ZYResultHandlerFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(fragment, fragment.getClass().getSimpleName())
                    .commit();
        }else if(fragment.isDetached()){
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.attach(fragment);
            transaction.commit();
        }
        final ZYResultHandlerFragment finalFragment = fragment;
        return finalFragment.getAttachSubject().filter(new AppendOnlyLinkedArrayList.NonThrowingPredicate<Boolean>() {
            @Override
            public boolean test(Boolean aBoolean) {
                return aBoolean;
            }
        }).flatMap(new Function<Boolean, ObservableSource<List<ImageItem>>>() {
            @Override
            public ObservableSource<List<ImageItem>> apply(@NonNull Boolean aBoolean) throws Exception {
                Intent intent = new Intent(finalFragment.getActivity(), ZYPickerActivity.class);
                finalFragment.startActivityForResult(intent,ZYResultHandlerFragment.REQUEST_CODE);
                return finalFragment.getResultSubject();
            }
        }).take(1);
    }

    public static void init(ZYPickerImageLoader loader){
        ZYPicturePickerManager.getInstance().init(loader);
    }
}
