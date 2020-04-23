package com.spark.szhb_master.activity.credit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.kline.DepthFragment;
import com.spark.szhb_master.activity.kline.VolumeFragment;
import com.spark.szhb_master.activity.login.LoginActivity;
import com.spark.szhb_master.adapter.PagerAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.base.BaseFragment;
import com.spark.szhb_master.entity.Credit;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.BitmapUtils;
import com.spark.szhb_master.utils.FileUtils;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.PermissionUtils;
import com.spark.szhb_master.utils.SharedPreferenceInstance;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.utils.UriUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * 身份验证
 */
public class CreditOneActivity extends BaseActivity{

    @BindView(R.id.tablayout)
    TabLayout mTablayout;

    @BindView(R.id.viewpager)
    ViewPager mViewpager;



    private List<BaseFragment> fragmentList = new ArrayList<>();
    private List<String> tabs = new ArrayList<>();
    private int type;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_creditone;
    }


    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
        tvGoto.setVisibility(View.INVISIBLE);
        fragmentList.add(CreditFragment.getInstance(1));
        tabs.add("身份证");

        mViewpager.setAdapter(new PagerAdapter(getSupportFragmentManager(), fragmentList, tabs));
        mTablayout.setTabMode(TabLayout.MODE_FIXED);
        mTablayout.setupWithViewPager(mViewpager);
    }

    @Override
    protected void onStart() {
        isNeedChecke = false;
        super.onStart();
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.my_credit));

    }


}
