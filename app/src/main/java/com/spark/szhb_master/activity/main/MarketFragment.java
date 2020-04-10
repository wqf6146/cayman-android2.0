package com.spark.szhb_master.activity.main;

import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.main.presenter.USDTMarketFragment;
import com.spark.szhb_master.adapter.PagerAdapter;
import com.spark.szhb_master.base.BaseNestingTransFragment;
import com.spark.szhb_master.entity.Currency;
import com.spark.szhb_master.ui.intercept.MyInterceptViewPager;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;

/**
 * 行情
 * Created by Administrator on 2018/1/29.
 */

public class MarketFragment extends BaseNestingTransFragment implements MainContract.MarketView {
    public static final String TAG = MarketFragment.class.getSimpleName();
    @BindView(R.id.ivSwitch)
    ImageButton ivSwitch;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.vpPager)
    MyInterceptViewPager vpPager;
    @BindArray(R.array.home_two_top_tab)
    String[] tabs;
    private List<Currency> baseUsdt = new ArrayList<>();
    private List<Currency> baseBtc = new ArrayList<>();
    private List<Currency> baseEth = new ArrayList<>();
    private List<Currency> favoriteCoin = new ArrayList<>();
    private USDTMarketFragment usdtMarketFragment;
    private BTCMarketFragment btcMarketFragment;
    private ETHMarketFragment ethMarketFragment;
    private SelfSelectionragment favoriteFragment;
    private MainContract.MarketPresenter presenter;
    private boolean isLoad = false;
    private final String mPageName = "MarketFragment";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_market;
    }

    @Override
    protected void initView() {
        super.initView();
        ivSwitch.setVisibility(View.VISIBLE);
        tvGoto.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        super.initData();
        if (((MainActivity) getActivity()).getSavedInstanceState() != null) recoveryFragments();
        vpPager.setOffscreenPageLimit(3);
        final List<String> tabs = Arrays.asList(this.tabs);
        vpPager.setAdapter(new PagerAdapter(getChildFragmentManager(), fragments, tabs));
        tab.setupWithViewPager(vpPager, true);
//        tab.post(new Runnable() {
//            @Override
//            public void run() {
//                setIndicator(tab, 40, 40);
//            }
//        });

    }

    @Override
    protected void setListener() {
        super.setListener();
        ivSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLoad(isLoad);
            }
        });
    }

    private void changeLoad(boolean isLoads) {
        isLoad = !isLoads;
        if (usdtMarketFragment != null) usdtMarketFragment.isChange(isLoad);
        if (btcMarketFragment != null) btcMarketFragment.isChange(isLoad);
        if (favoriteFragment != null) favoriteFragment.isChange(isLoad);
    }

    @Override
    protected void addFragments() {
        int type = MarketBaseFragment.MarketOperateCallback.TYPE_TO_KLINE;
        if (usdtMarketFragment == null)
            fragments.add(usdtMarketFragment = USDTMarketFragment.getInstance(type));
        if (btcMarketFragment == null)
            fragments.add(btcMarketFragment = BTCMarketFragment.getInstance(type));
        if (ethMarketFragment == null)
            fragments.add(ethMarketFragment = ETHMarketFragment.getInstance(type));
        if (favoriteFragment == null)
            fragments.add(favoriteFragment = SelfSelectionragment.getInstance(type));
    }

    @Override
    protected void recoveryFragments() {
        usdtMarketFragment = (USDTMarketFragment) getChildFragmentManager().findFragmentByTag(makeFragmentName(vpPager.getId(), 0));
        btcMarketFragment = (BTCMarketFragment) getChildFragmentManager().findFragmentByTag(makeFragmentName(vpPager.getId(), 1));
        ethMarketFragment = (ETHMarketFragment) getChildFragmentManager().findFragmentByTag(makeFragmentName(vpPager.getId(), 2));
        favoriteFragment = (SelfSelectionragment) getChildFragmentManager().findFragmentByTag(makeFragmentName(vpPager.getId(), 3));
        fragments.add(usdtMarketFragment);
        fragments.add(btcMarketFragment);
        fragments.add(ethMarketFragment);
        fragments.add(favoriteFragment);
    }

    @Override
    protected void loadData() {
        if (usdtMarketFragment != null)
            usdtMarketFragment.dataLoaded(baseUsdt);
        if (btcMarketFragment != null)
            btcMarketFragment.dataLoaded(baseBtc);
        if (ethMarketFragment != null)
            ethMarketFragment.dataLoaded(baseEth);
        if (favoriteFragment != null)
            favoriteFragment.dataLoaded(favoriteCoin);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        immersionBar.flymeOSStatusBarFontColor("#FFFFFF").init();
        if (!isSetTitle) {
            immersionBar.setTitleBar(getActivity(), llTitle);
            isSetTitle = true;
            tvTitle.setText(getResources().getString(R.string.market));
        }
    }

    @Override
    public void setPresenter(MainContract.MarketPresenter presenter) {
        this.presenter = presenter;
    }

    public void dataLoaded(final List<Currency> baseUsdt, final List<Currency> baseBtc, final List<Currency> baseEth, final List<Currency> favoriteCoin) {
        this.baseUsdt.clear();
        this.baseUsdt.addAll(baseUsdt);

        this.baseBtc.clear();
        this.baseBtc.addAll(baseBtc);

        this.baseEth.clear();
        this.baseEth.addAll(baseEth);

        this.favoriteCoin.clear();
        this.favoriteCoin.addAll(favoriteCoin);

        try {
            favoriteFragment.dataLoaded(this.favoriteCoin);
        } catch (Exception e) {
        }
        try {
            usdtMarketFragment.dataLoaded(baseUsdt);
            btcMarketFragment.dataLoaded(baseBtc);
            ethMarketFragment.dataLoaded(baseEth);

        } catch (NullPointerException ex) {
        }
    }

    public static String getTAG() {
        return TAG;
    }

    public void tcpNotify() {
        if (usdtMarketFragment == null) return;
        usdtMarketFragment.tcpNotify();
        btcMarketFragment.tcpNotify();
        ethMarketFragment.tcpNotify();
        favoriteFragment.tcpNotify();
    }

    @Override
    protected String getmTag() {
        return TAG;
    }


    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
    }

}
