package com.sanjie.zy.picture.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.sanjie.zy.R;
import com.sanjie.zy.base.ZYActivity;
import com.sanjie.zy.picture.ui.fragment.ZYPickerFragment;
import com.sanjie.zy.widget.ZYToast;

/**
 * Created by LangSanJie on 2017/4/25.
 */

public class ZYPickerActivity extends ZYActivity {

    private static final int READ_STORAGE_PERMISSION = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_picker;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData(Bundle savedInstanceState) {
        requestPermission();
    }

    private void requestPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ZYPickerActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},READ_STORAGE_PERMISSION);
        }else{
            setupFragment();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == READ_STORAGE_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                setupFragment();
            }else{
                ZYToast.warning("获取读取相册权限失败");
                finish();
            }
        }
    }

    private void setupFragment() {
        ZYPickerFragment fragment = (ZYPickerFragment) getFragmentManager().findFragmentByTag(ZYPickerFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = ZYPickerFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment, ZYPickerFragment.class.getSimpleName())
                    .commitAllowingStateLoss();
        }
    }
}
