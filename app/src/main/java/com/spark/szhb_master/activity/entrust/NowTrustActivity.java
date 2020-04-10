package com.spark.szhb_master.activity.entrust;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.TrustAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.dialog.EntrustDialog;
import com.spark.szhb_master.entity.Entrust;
import com.spark.szhb_master.entity.MarketSymbol;
import com.spark.szhb_master.utils.GlobalConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * Created by Administrator on 2018/9/3 0003.
 */

public class NowTrustActivity extends BaseActivity implements ITrustContract.View {

    @BindView(R.id.mRefresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView; // 列表控价
    private List<Entrust> mData = new ArrayList<>();
    private TrustAdapter mAdapter;
    private int page = 0; // 页码
    private ITrustContract.Presenter mPresenter;
    private String symbol;
    private EntrustDialog dialog;
    private String orderId;

    private List<MarketSymbol> symbolList = new ArrayList<>();
    private String[] symbolName;
    @BindView(R.id.tvSymbol)
    TextView tvSymbol;
    @BindView(R.id.tvTag)
    TextView tvTag;@BindView(R.id.iv_ping)
    ImageView iv_ping;

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
        tvGoto.setVisibility(View.VISIBLE);
        iv_ping.setVisibility(View.INVISIBLE);
        tvGoto.setText(getString(R.string.history_trust));

        setTitle(getString(R.string.current_trust));
        tvSymbol.setVisibility(View.VISIBLE);
        tvTag.setVisibility(View.GONE);
        initRv();
    }

    @Override
    protected void initData() {
        super.initData();
        dialog = new EntrustDialog(activity);
        new TrustPresentImpl(Injection.provideTasksRepository(getApplicationContext()), this);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_all_trust;
    }

    /**
     * 初始化列表
     */
    private void initRv() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TrustAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(false);
    }
    @Override
    protected void setListener() {
        super.setListener();
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMoreText();
            }
        }, mRecyclerView);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Entrust entrust = (Entrust) adapter.getItem(position);
                orderId = entrust.getOrderId();
                if (entrust != null) {
                    dialog.setEntrust(entrust);
                    dialog.show();
                }
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshText();
            }
        });

        dialog.setOnCancelOrder(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.cancelOrder(orderId);
                dialog.dismiss();
            }
        });

        tvGoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isMy", true);
                showActivity(AllTrustActivity.class,bundle);
            }
        });

    }

    @Override
    protected void loadData() {
//        getList(true);
        mPresenter.getSymbol();
    }

    /**
     * 刷新
     */
    private void refreshText() {
        page = 0;
        mAdapter.setEnableLoadMore(false);
        getList(false);
    }

    /**
     * 下拉
     */
    private void loadMoreText() {
        refreshLayout.setEnabled(false);
        page++;
        getList(false);
    }

    private void getList(boolean isShow) {
        if (isShow)
            displayLoadingPopup();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageNo", page + "");
        map.put("pageSize", GlobalConstant.PageSize + "");
        map.put("symbol", symbol);
        mPresenter.getCurrentOrder(map);
    }


    @Override
    public void doPostFail(int e, String meg) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
        mAdapter.loadMoreComplete();
    }

    @Override
    public void getCurrentOrderSuccess(List<Entrust> obj) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
        mAdapter.loadMoreComplete();
        if (obj == null) return;
        if (page == 0) {
            this.mData.clear();
            if (obj.size() == 0) {
                mAdapter.loadMoreEnd();
                mAdapter.setEmptyView(R.layout.empty_no_message);
            } else {
                this.mData.addAll(obj);
            }
        } else {
            if (obj.size() != 0) this.mData.addAll(obj);
            else mAdapter.loadMoreEnd();
        }
        mAdapter.disableLoadMoreIfNotFullPage();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getSymbolSucccess(List<MarketSymbol> objs) {
        if (objs != null && objs.size() != 0) {
            symbolList.clear();
            symbolList.addAll(objs);
            symbolName = new String[symbolList.size()];
            for (int i = 0; i < symbolList.size(); i++) {
                symbolName[i] = symbolList.get(i).getSymbol();
            }
            symbol = symbolName[0];
            tvSymbol.setText(symbol);
            getList(true);
        }
    }

    @Override
    public void cancelOrderSuccess(String message) {
        getList(true);
    }

    @Override
    public void setPresenter(ITrustContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private NormalListDialog normalDialog;
    /**
     * 列表类选择框
     */
    @OnClick(R.id.tvSymbol)
    void showListDialog() {
        if (normalDialog == null) {
            normalDialog = new NormalListDialog(activity, symbolName);
            normalDialog.title(getString(R.string.text_coin_type));
            normalDialog.titleBgColor(getResources().getColor(R.color.main_head_bg));
        }
        normalDialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                symbol = symbolName[position];
                tvSymbol.setText(symbol);
                page = 0;
                getList(true);
                normalDialog.dismiss();
            }
        });
        normalDialog.show();
    }

}
