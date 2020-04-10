package com.spark.szhb_master.activity.order;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.PagerAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.base.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;

/**
 * 我的订单界面
 */
public class MyOrderActivity extends BaseActivity {

    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.vpPager)
    ViewPager vpPager;
    @BindView(R.id.llContainer)
    LinearLayout llContainer;
    @BindArray(R.array.order_status)
    String[] status;
    private List<BaseFragment> fragments = new ArrayList<>();
    private int type;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_my_order;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
//        setTitle(getString(R.string.my_order));
        tvGoto.setVisibility(View.INVISIBLE);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getInt("type", 0);
        }
        setFragments();
        vpPager.setCurrentItem(type);
    }

    private void setFragments() {
        addFragments();
        vpPager.setOffscreenPageLimit(fragments.size() - 1);
        vpPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), fragments, Arrays.asList(status)));
        tab.setupWithViewPager(vpPager);
    }

    @Override
    protected ViewGroup getEmptyView() {
        return llContainer;
    }

    private void addFragments() {
        fragments.add(OrderFragment.getInstance(OrderFragment.Status.UNPAID));
        fragments.add(OrderFragment.getInstance(OrderFragment.Status.PAID));
        fragments.add(OrderFragment.getInstance(OrderFragment.Status.DONE));
        fragments.add(OrderFragment.getInstance(OrderFragment.Status.CANC));
        fragments.add(OrderFragment.getInstance(OrderFragment.Status.COMPLAINING));
    }

}
