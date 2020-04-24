package com.spark.szhb_master.activity.credit;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.PagerAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_creditone;
    }


    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
        tvGoto.setVisibility(View.INVISIBLE);
        fragmentList.add(CreditSfzFragment.getInstance(0));
        fragmentList.add(CreditHzFragment.getInstance(1));
        tabs.add("身份证");
        tabs.add("护照");

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
