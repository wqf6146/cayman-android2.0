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
import com.spark.szhb_master.adapter.TrustHistoryAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.EntrustHistory;
import com.spark.szhb_master.entity.MarketSymbol;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.NetCodeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * 全部委托/我的委托
 */
public class AllTrustActivity extends BaseActivity implements IAllTrustContract.View {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mRefresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.tvTag)
    TextView tvTag;
    @BindView(R.id.tvSymbol)
    TextView tvSymbol; @BindView(R.id.iv_ping)
    ImageView iv_ping;
    private TrustHistoryAdapter mAdapter;
    private List<EntrustHistory> mData = new ArrayList<>();
    private IAllTrustContract.Presenter mPresenter;
    private String symbol;
    private int page = 0;
    private boolean isMy;
    private List<MarketSymbol> symbolList = new ArrayList<>();
    private String[] symbolName;
    private int his = 0;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_all_trust;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isMy = bundle.getBoolean("isMy");
            if (isMy) {
                tvTag.setVisibility(View.GONE);
                tvSymbol.setVisibility(View.VISIBLE);
                tvGoto.setVisibility(View.VISIBLE);
                iv_ping.setVisibility(View.INVISIBLE);
                tvGoto.setText(R.string.now_trust);
                setTitle(getString(R.string.history_trust));
            } else {
                setTitle(getString(R.string.my_entrust));
                symbol = bundle.getString("symbol");
                setTitle(symbol);
                tvGoto.setVisibility(View.INVISIBLE);
            }
        }
        new AllTrustImpl(Injection.provideTasksRepository(getApplicationContext()), this);
        initRv();
    }

    /**
     * 初始化列表
     */
    private void initRv() {
        mAdapter = new TrustHistoryAdapter(mData);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(false);
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                mAdapter.setEnableLoadMore(false);
                getList(false);
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EntrustHistory entrustHistory = (EntrustHistory) adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString("symbol", symbol);
                bundle.putSerializable("order", entrustHistory);
                showActivity(TrustDetailActivity.class, bundle);
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                refreshLayout.setEnabled(false);
                page = page + 1;
                getList(false);
            }
        }, mRecyclerView);

//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                symbol = symbolName.get(position);
//                page = 0;
//                getList(true);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        tvGoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void loadData() {
        if (!isMy)
            getList(true);
        else
            mPresenter.getSymbol();
    }

    /**
     * 获取全部委托数据
     */
    private void getList(boolean isShow) {
        if (isShow)
            displayLoadingPopup();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageNo", page + "");
        map.put("pageSize", GlobalConstant.PageSize + "");
        map.put("symbol", symbol);
        mPresenter.getAllEntrust(map);
    }

    @Override
    public void errorMes(int e, String meg) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
        mAdapter.loadMoreComplete();
    }

    @Override
    public void getAllSuccess(List<EntrustHistory> obj) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
        mAdapter.loadMoreComplete();
        if (obj == null) return;
        if (page == 0) {
            this.mData.clear();
            if (obj.size() == 0) {
                mAdapter.setEmptyView(R.layout.empty_no_message);
                mAdapter.loadMoreEnd();
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
    public void doPostFail(int code, String message) {
        NetCodeUtils.checkedErrorCode(this, code, message);
    }

    @Override
    public void setPresenter(IAllTrustContract.Presenter presenter) {
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
