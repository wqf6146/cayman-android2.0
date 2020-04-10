package com.spark.szhb_master.activity.main.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.Trade.TradeActivity;
import com.spark.szhb_master.activity.main.MainActivity;
import com.spark.szhb_master.activity.main.MarketBaseFragment;
import com.spark.szhb_master.adapter.MarketAdapter;
import com.spark.szhb_master.entity.Currency;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 行情--usdt
 * Created by Administrator on 2018/1/29.
 */

public class USDTMarketFragment extends MarketBaseFragment {
    @BindView(R.id.rvContent)
    RecyclerView rvContent;
    private MarketAdapter mAdapter;
    private List<Currency> currencies = new ArrayList<>();
    private int type;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    public static USDTMarketFragment getInstance(int type) {
        USDTMarketFragment usdtMarketFragment = new USDTMarketFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        usdtMarketFragment.setArguments(bundle);
        return usdtMarketFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_coin;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt("type");
        }
        initRvContent();
    }

    private void initRvContent() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        mAdapter = new MarketAdapter(currencies, 1);
        mAdapter.isFirstOnly(true);
        mAdapter.setLoad(true);
        mAdapter.bindToRecyclerView(rvContent);
//        rvContent.setAdapter(mAdapter);
        if (currencies.size() == 0)
            mAdapter.setEmptyView(R.layout.empty_no_message);
    }

    @Override
    protected void setListener() {
        super.setListener();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //10.19更改
//                ((MarketOperateCallback) getActivity()).itemClick(USDTMarketFragment.this.mAdapter.getItem(position), type);
                Currency currency = mAdapter.getItem(position);
                Intent intent = new Intent(activity, TradeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("currency", currency);
                intent.putExtras(bundle);
                activity.startActivityForResult(intent, 1); //  执行父级activity的回调
            }
        });
    }

    @Override
    protected void loadData() {
        notifyData();
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    public void dataLoaded(List<Currency> baseUsdt) {
        this.currencies.clear();
        this.currencies.addAll(baseUsdt);
        if (mAdapter != null) mAdapter.notifyDataSetChanged();
    }

    public void isChange(boolean isLoad) {
        if (mAdapter != null) {
            mAdapter.setLoad(isLoad);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void notifyData() {
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    public void tcpNotify() {
        if (getUserVisibleHint() && mAdapter != null) mAdapter.notifyDataSetChanged();
    }
}
