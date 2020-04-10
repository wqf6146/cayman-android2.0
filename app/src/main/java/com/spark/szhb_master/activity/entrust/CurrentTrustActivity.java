package com.spark.szhb_master.activity.entrust;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
 * 当前委托的界面
 */
public class CurrentTrustActivity extends BaseActivity implements ITrustContract.View {
    @BindView(R.id.mRefresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView; // 列表控价
    @BindView(R.id.tvTag)
    TextView tvTag; @BindView(R.id.iv_ping)
    ImageView iv_ping;
    private List<Entrust> mData = new ArrayList<>();
    private TrustAdapter mAdapter;
    private int page = 0; // 页码
    private ITrustContract.Presenter mPresenter;
    private String symbol;
    private EntrustDialog dialog;
    private String orderId;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_all_trust;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
        tvGoto.setVisibility(View.VISIBLE);
        tvGoto.setText(getString(R.string.history_record));
        iv_ping.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initData() {
        super.initData();
        dialog = new EntrustDialog(activity);
        new TrustPresentImpl(Injection.provideTasksRepository(getApplicationContext()), this);
        tvTag.setText(getString(R.string.current_trust));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            symbol = bundle.getString("symbol");
            setTitle(symbol);
        }
        initRv();
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


    @OnClick(R.id.tvGoto)
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        Bundle bundle = new Bundle();
        bundle.putString("symbol", symbol);
        showActivity(AllTrustActivity.class, bundle);
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

    }

    @Override
    protected void loadData() {
        getList(true);
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

    }

    @Override
    public void cancelOrderSuccess(String message) {
        getList(true);
    }

    @Override
    public void setPresenter(ITrustContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
