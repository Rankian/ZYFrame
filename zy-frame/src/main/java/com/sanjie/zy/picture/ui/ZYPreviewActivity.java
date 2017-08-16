package com.sanjie.zy.picture.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanjie.zy.R;
import com.sanjie.zy.base.ZYActivity;
import com.sanjie.zy.picture.bean.ImageItem;
import com.sanjie.zy.picture.ui.adapter.PreviewAdapter;
import com.sanjie.zy.utils.log.ZYLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LangSanJie on 2017/4/25.
 */

public class ZYPreviewActivity extends ZYActivity {

//    private Toolbar toolbar;
    private ImageView backBtn;
    private TextView titleTv;
    private ViewPager mPreview;
    private PreviewAdapter mAdapter;

    private List<ImageItem> data;

    public static void start(Context context, ArrayList<ImageItem> data) {
        Intent starter = new Intent(context, ZYPreviewActivity.class);
        starter.putExtra("preview_list",data);
        context.startActivity(starter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_preview;
    }

    @Override
    public void initView() {
        setupToolbar();
        mPreview = (ViewPager) findViewById(R.id.vp_preview);
        mAdapter = new PreviewAdapter(data);
        mPreview.setAdapter(mAdapter);
        mPreview.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
//                toolbar.setTitle(position + 1 + "/" + data.size());
                titleTv.setText(position + 1 + "/" + data.size());
            }
        });
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        data = (List<ImageItem>) getIntent().getSerializableExtra("preview_list");
    }

    private void setupToolbar() {
//        toolbar = (Toolbar) findViewById(R.id.nav_top_bar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar.setTitle("1/" + data.size());

        backBtn = (ImageView) findViewById(R.id.preview_back_btn);
        titleTv = (TextView) findViewById(R.id.preview_title_tv);
        titleTv.setText("1/" + data.size());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
