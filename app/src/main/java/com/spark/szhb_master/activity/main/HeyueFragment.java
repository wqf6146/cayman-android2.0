package com.spark.szhb_master.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.Trade.TradeActivity;
import com.spark.szhb_master.adapter.HeyueAdapter;
import com.spark.szhb_master.base.BaseTransFragment;
import com.spark.szhb_master.entity.Currency;
import com.spark.szhb_master.entity.NewCurrency;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2018/1/29.
 */

public class HeyueFragment extends BaseTransFragment implements MainContract.HeyueView {

    public static final String TAG = HeyueFragment.class.getSimpleName();

    @BindView(R.id.llTitle)
    LinearLayout llTitle;

    @BindView(R.id.rvContent)
    RecyclerView rvContent;
    private HeyueAdapter adapter;
    private List<NewCurrency> currencies = new ArrayList<>();

    private int type;

    private MainContract.HeyuePresenter presenter;

    private final String mPageName = "HeyueFragment";

    public static HeyueFragment getInstance(int type) {
        HeyueFragment heyueFragment = new HeyueFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        heyueFragment.setArguments(bundle);
        return heyueFragment;
    }

    @Override
    public void setPresenter(MainContract.HeyuePresenter presenter) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_heyue;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        immersionBar.flymeOSStatusBarFontColor("#FFFFFF").init();
        if (!isSetTitle) {
            immersionBar.setTitleBar(getActivity(), llTitle);
            isSetTitle = true;
        }
    }

    @Override
    protected void initData() {
        setTitle(getString(R.string.heyue));
        ivBack.setVisibility(GONE);
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = getArguments().getInt("type");
        }
        initRvContent();
    }

    @Override
    protected String getmTag() {
        return TAG;
    }

    private void initRvContent() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        adapter = new HeyueAdapter(currencies, 1);
        adapter.isFirstOnly(true);
        adapter.bindToRecyclerView(rvContent);
//        rvContent.setAdapter(adapter);
        if (currencies.size() == 0)
            adapter.setEmptyView(R.layout.empty_no_message);
    }


    @Override
    protected void setListener() {
        super.setListener();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                ((MarketOperateCallback) getActivity()).itemClick(BTCMarketFragment.this.adapter.getItem(position), type);
                NewCurrency currency = (NewCurrency) adapter.getItem(position);
                Intent intent = new Intent(activity, TradeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("currency", currency);
                intent.putExtras(bundle);
                activity.startActivityForResult(intent, 1); //  执行父级activity的回调
            }
        });
    }

    public void isChange(boolean isLoad) {
        if (adapter != null) {
            adapter.setLoad(isLoad);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void loadData() {
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    public void dataLoaded(List<NewCurrency> datas) {
        this.currencies.clear();
        this.currencies.addAll(datas);
        if (adapter != null) {
            if (currencies.size() == 0)
                adapter.setEmptyView(R.layout.empty_no_message);
            adapter.notifyDataSetChanged();
        }
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        getActivity().getSupportFragmentManager().putFragment(outState, "USDT", this);
//    }

    public void tcpNotify() {
        if (getUserVisibleHint() && adapter != null) adapter.notifyDataSetChanged();
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
