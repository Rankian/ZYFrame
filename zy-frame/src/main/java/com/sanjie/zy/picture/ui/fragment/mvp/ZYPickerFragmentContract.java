package com.sanjie.zy.picture.ui.fragment.mvp;

import android.content.Context;

import com.sanjie.zy.picture.bean.ImageFolder;
import com.sanjie.zy.picture.ui.base.BasePresenter;
import com.sanjie.zy.picture.ui.base.BaseView;

import java.util.List;

/**
 * Created by LangSanJie on 2017/4/25.
 */

public interface ZYPickerFragmentContract {
    interface View extends BaseView{
        void showAllImage(List<ImageFolder> datas);
    }

    abstract class Presenter extends BasePresenter<View>{
        public abstract void loadAllImage(Context context);
    }
}
