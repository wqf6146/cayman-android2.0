package com.spark.szhb_master.activity.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.login.LoginActivity;
import com.spark.szhb_master.activity.main.presenter.C2CPresenterImpl;
import com.spark.szhb_master.activity.order.MyOrderActivity;
import com.spark.szhb_master.adapter.PagerAdapter;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.base.BaseFragment;
import com.spark.szhb_master.base.BaseNestingTransFragment;
import com.spark.szhb_master.dialog.BuyOrSellDialog;
import com.spark.szhb_master.dialog.ShiMingDialog;
import com.spark.szhb_master.entity.CoinInfo;
import com.spark.szhb_master.entity.Country;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * Created by Administrator on 2018/1/29.
 */

public class C2CFragment extends BaseNestingTransFragment implements MainContract.C2CView {
    public static final String TAG = C2CFragment.class.getSimpleName();
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewPager)
    ViewPager vp;
    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvSell)
    TextView tvSell;
    @BindView(R.id.ivShow)
    ImageButton ivShow;
    @BindView(R.id.ivReleseAd)
    ImageView ivReleseAd;
    @BindView(R.id.ivGoOrder)
    ImageView ivGoOrder;
    @BindView(R.id.llBuyAndSell)
    LinearLayout llBuyAndSell;
    private MainContract.C2CPresenter presenter;
    private List<CoinInfo> coinInfos = new ArrayList<>();
    private PagerAdapter adapter;
    private ArrayList<String> tabs = new ArrayList<>();
    private List<BaseFragment> fragments = new ArrayList<>();
    private List<CoinInfo> saveCoinInfos;
    private Country country;
    private BuyOrSellDialog dialog;
    private SafeSetting safeSetting;
    private final String mPageName = "C2CFragment";
    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            immersionBar.setTitleBar(getActivity(), llTitle);
            isSetTitle = true;
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
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_c2c;
    }

    @Override
    protected void initView() {
        ivGoOrder.setVisibility(View.VISIBLE);
        ivReleseAd.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.GONE);
        tvGoto.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
        llBuyAndSell.setVisibility(View.VISIBLE);
        tvBuy.setSelected(true);
        tvSell.setSelected(false);
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new C2CPresenterImpl(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
        dialog = new BuyOrSellDialog(activity);


        Bundle bundle = getArguments();
        if (bundle != null) {
            saveCoinInfos = (List<CoinInfo>) bundle.getSerializable("coinInfos");
            recoveryFragments();
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
        if (getArguments() == null) {
            isNeedLoad = false;
            presenter.all();
        }

    }

    @OnClick({R.id.tvBuy, R.id.tvSell, R.id.ivShow, R.id.ivReleseAd, R.id.ivGoOrder})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        if (v.getId() == R.id.ivReleseAd || v.getId() == R.id.ivGoOrder) {
            if (!MyApplication.getApp().isLogin()) {
                showActivity(LoginActivity.class, null, LoginActivity.RETURN_LOGIN);
                return;
            }
        }
        switch (v.getId()) {
            case R.id.tvBuy:
                clickTabBuy();
                break;
            case R.id.tvSell:
                clickTabSell();
                break;
            case R.id.ivShow:
                if (dialog != null) {
                    dialog.show();
                }
                break;
            case R.id.ivReleseAd:
                presenter.safeSetting();
                break;
            case R.id.ivGoOrder:
                showActivity(MyOrderActivity.class, null);
                break;
        }

    }


    /**
     * 卖出
     */
    private void clickTabSell() {
        tvBuy.setSelected(false);
        tvSell.setSelected(true);
        for (BaseFragment fragment : fragments)
            ((C2CListFragment) fragment).setAdvertiseType(GlobalConstant.BUY);

    }

    /**
     * 买入
     */
    private void clickTabBuy() {
        tvBuy.setSelected(true);
        tvSell.setSelected(false);
        for (BaseFragment fragment : fragments)
            ((C2CListFragment) fragment).setAdvertiseType(GlobalConstant.SELL);
    }


    @Override
    public void setPresenter(MainContract.C2CPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void allSuccess(List<CoinInfo> obj) {
        if (obj == null || obj.size() == 0) return;
        coinInfos.clear();
        coinInfos.addAll(obj);
        tabs.clear();
        fragments.clear();
        country = new Country();
        country.setLocalCurrency(GlobalConstant.CNY);
        country.setZhName("中国");
        for (CoinInfo coinInfo : coinInfos) {
            tabs.add(coinInfo.getUnit());
            fragments.add(C2CListFragment.getInstance(coinInfo));
        }
        if (fragments.size() > 4) tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        else tab.setTabMode(TabLayout.MODE_FIXED);
        if (adapter == null) {
            vp.setAdapter(adapter = new PagerAdapter(getChildFragmentManager(), fragments, tabs));
            tab.setupWithViewPager(vp);
            vp.setOffscreenPageLimit(fragments.size() - 1);
        } else adapter.notifyDataSetChanged();
        isNeedLoad = false;
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(getmActivity(), code, toastMessage);
    }

    @Override
    public void safeSettingSuccess(SafeSetting obj) {
        if (obj == null)
            return;
        safeSetting = obj;
        if (safeSetting.getRealVerified() == 0) {
            ShiMingDialog shiMingDialog = new ShiMingDialog(getContext());
            String name = safeSetting.getRealNameRejectReason();
            if (safeSetting.getRealVerified() == 0) {
                if (safeSetting.getRealAuditing() == 1) {
                    shiMingDialog.setEntrust(7, name,1);
                } else {
                    if (safeSetting.getRealNameRejectReason() != null)
                        shiMingDialog.setEntrust(8, name,1);
                    else
                        shiMingDialog.setEntrust(9, name,1);
                }
            } else {
                shiMingDialog.setEntrust(6, name,1);
            }
            shiMingDialog.show();
        } else {
            dialog.show();
        }
    }

    public static String getTAG() {
        return TAG;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("coinInfos", (Serializable) coinInfos);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void addFragments() {

    }

    @Override
    protected void recoveryFragments() {
        if (saveCoinInfos == null || saveCoinInfos.size() == 0) return;
        coinInfos.clear();
        coinInfos.addAll(saveCoinInfos);
        tabs.clear();
        fragments.clear();
        for (int i = 0; i < coinInfos.size(); i++) {
            tabs.add(coinInfos.get(i).getUnit());
            fragments.add((C2CListFragment) getChildFragmentManager().findFragmentByTag(makeFragmentName(vp.getId(), i)));
        }
        if (fragments.size() > 4) tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        else tab.setTabMode(TabLayout.MODE_FIXED);
        if (adapter == null) {
            vp.setAdapter(adapter = new PagerAdapter(getChildFragmentManager(), fragments, tabs));
            tab.setupWithViewPager(vp);
            vp.setOffscreenPageLimit(fragments.size() - 1);
        } else adapter.notifyDataSetChanged();
        isNeedLoad = false;
    }

    @Override
    protected String getmTag() {
        return TAG;
    }
}
