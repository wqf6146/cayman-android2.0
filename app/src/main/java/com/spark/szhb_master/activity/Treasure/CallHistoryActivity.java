package com.spark.szhb_master.activity.Treasure;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.CallHistoryAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.CallHistory;
import com.spark.szhb_master.entity.TreasureInfo;
import com.spark.szhb_master.utils.NetCodeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * Created by Administrator on 2018/9/18 0018.
 */

public class CallHistoryActivity extends BaseActivity implements CallHistoryContract.View{


    private CallHistoryContract.Presenter presenter;
    @BindView(R.id.trea_detail_recy)
    RecyclerView trea_detail_recy;   @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.gcx_num)
    TextView gcx_num;
    @BindView(R.id.anminal_text)
    TextView anminal_text;

    private CallHistoryAdapter adapter;

    private int pageNo = 0;
    private int pageSize = 10;
    private List<CallHistory> callHistories = new ArrayList<>();

    @Override
    protected int getActivityLayoutId() {
        return R.layout.call_layout;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            immersionBar.setTitleBar(CallHistoryActivity.this, llTitle);
            isSetTitle = true;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        tvGoto.setVisibility(View.INVISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setBackground(null);
        tvTitle.setText(getResources().getString(R.string.call_historu));
        tvGoto.setVisibility(View.INVISIBLE);
        llTitle.setBackgroundDrawable(null);

        new CallHistoryPresenter(Injection.provideTasksRepository(getApplicationContext()), this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
        }

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        trea_detail_recy.setLayoutManager(manager);
        adapter = new CallHistoryAdapter(this,R.layout.call_histry_detail, callHistories);
        adapter.bindToRecyclerView(trea_detail_recy);
        adapter.isFirstOnly(true);
        adapter.setEnableLoadMore(false);
        trea_detail_recy.setAdapter(adapter);

        presenter.getTreaInfo();
    }


    @OnClick({R.id.ivBack})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 0;
                getHistory();
            }
        });

        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNo = pageNo + 1;
                getHistory();
            }
        });
    }

    @Override
    protected void loadData() {
        super.loadData();
        getHistory();
    }

    private void getHistory() {
        HashMap<String, String> map = new HashMap<>();
        map.put("pageNo", pageNo + "");
        map.put("pageSize", pageSize + "");
        presenter.getVoteHistory(map);
    }


    @Override
    public void getVoteHistorySuccess(List<CallHistory> obj) {
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        if (obj != null && obj.size() > 0) {
            if (pageNo ==0){
                this.callHistories.clear();
            } else {
                adapter.loadMoreEnd();
            }
            this.callHistories.addAll(obj);
            adapter.notifyDataSetChanged();
        }else {
            if (pageNo == 0){
                this.callHistories.clear();
                adapter.setEmptyView(R.layout.empty_no_treaing);
                adapter.notifyDataSetChanged();
            }
        }
        adapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    public void getTreaInfoSuccess(TreasureInfo treasureInfo) {
        double num = treasureInfo.getAvailablePower();
//        anminal_text.showNumberWithAnimation(num, CountNumberView.INTREGEX);
        anminal_text.setText(num+"");
        Math.floor(treasureInfo.getGcxAmount());
        gcx_num.setText("GCX "+(int)Math.floor(treasureInfo.getGcxAmount()));
    }

    @Override
    public void getSigninSuccess() {

    }

    @Override
    public void getPowerRecordSuccess(List<WaterBallBean> list) {

    }

    @Override
    public void powerCollectSuccess() {

    }

    @Override
    public void getSigninFiled(Integer code, String toastMessage) {

    }

    @Override
    public void doFiled(Integer code, String toastMessage) {
        adapter.setEnableLoadMore(true);
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void setPresenter(CallHistoryContract.Presenter presenter) {
        this.presenter = presenter;
    }


}
