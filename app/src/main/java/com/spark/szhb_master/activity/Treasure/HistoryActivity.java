package com.spark.szhb_master.activity.Treasure;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.HistoryAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.History;
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

public class HistoryActivity extends BaseActivity implements HistoryContract.View{


    @BindView(R.id.hisRecycler)
    RecyclerView hisRecycler;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;   @BindView(R.id.iv_ping)
    ImageView iv_ping;

    private int pageNo = 0;
    private int pageSize = 10;
    private HistoryContract.Presenter presenter;
    private List<History> histories = new ArrayList<>();

    private HistoryAdapter adapter;
    private int form;


    @Override
    protected int getActivityLayoutId() {
        return R.layout.histoty_layout;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            immersionBar.setTitleBar(HistoryActivity.this, llTitle);
            isSetTitle = true;
        }
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
    protected void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            form = bundle.getInt("form");
        }
        if (form == 2){
            tvGoto.setVisibility(View.VISIBLE);
            tvGoto.setText(getResources().getString(R.string.call_historu));
            iv_ping.setVisibility(View.INVISIBLE);
            tvGoto.setBackground(null);
        }else if (form == 1 || form == 3){
            tvGoto.setVisibility(View.INVISIBLE);
            tvGoto.setBackground(null);
        }
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setBackground(null);
        tvTitle.setText(getResources().getString(R.string.historical_activity));
        llTitle.setBackgroundDrawable(getResources().getDrawable(R.mipmap.trea_back_head));

        new HistoryPresenter(Injection.provideTasksRepository(getApplicationContext()), this);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        hisRecycler.setLayoutManager(manager);
        adapter = new HistoryAdapter(this,R.layout.histry_detail, histories,form);
        adapter.bindToRecyclerView(hisRecycler);
        adapter.isFirstOnly(true);
        adapter.setEnableLoadMore(false);
        hisRecycler.setAdapter(adapter);
    }

    @Override
    public void getVoteHistorySuccess(List<History> obj) {
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        if (obj != null && obj.size() > 0) {
            if (pageNo ==0){
                this.histories.clear();
            } else {
                adapter.loadMoreEnd();
            }
            this.histories.addAll(obj);
            adapter.notifyDataSetChanged();
        }else {
            if (pageNo == 0){
                this.histories.clear();
                adapter.setEmptyView(R.layout.empty_no_treaing);
                adapter.notifyDataSetChanged();
            }
        }
        adapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    public void getJoinHistorySuccess(List<History> obj) {
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        if (obj != null && obj.size() > 0) {
            if (pageNo ==0){
                this.histories.clear();
            } else {
                adapter.loadMoreEnd();
            }
            this.histories.addAll(obj);
            adapter.notifyDataSetChanged();
        }else {
            if (pageNo == 0){
                this.histories.clear();
                adapter.setEmptyView(R.layout.empty_no_message);
                adapter.notifyDataSetChanged();
            }
        }
        adapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    public void getUserHistorySuccess(List<History> obj) {
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        if (obj != null && obj.size() > 0) {
            if (pageNo ==0){
                this.histories.clear();
            } else {
                adapter.loadMoreEnd();
            }
            this.histories.addAll(obj);
            adapter.notifyDataSetChanged();
        }else {
            if (pageNo == 0){
                this.histories.clear();
                adapter.setEmptyView(R.layout.empty_no_treaing);
                adapter.notifyDataSetChanged();
            }
        }
        adapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 0;
                getHistoryList();
            }
        });
        tvGoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActivity(CallHistoryActivity.class,null,0);
            }
        });

        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNo = pageNo + 1;
                getHistoryList();
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Bundle bundle = new Bundle();
                bundle.putInt("commodityId",histories.get(position).getActivityCommodityId());
                bundle.putString("name",histories.get(position).getCommodityName());
                bundle.putInt("degree",histories.get(position).getVoteAmount());
                bundle.putInt("completeAmount",histories.get(position).getCompleteAmount());
                bundle.putInt("joinAmount",histories.get(position).getJoinAmount());
                bundle.putString("imgs",histories.get(position).getImgs());
                bundle.putInt("voteId",0);
                bundle.putInt("joinVote",histories.get(position).getJoinVote());
                bundle.putInt("form",form);//form
                bundle.putInt("joinAcitvity",histories.get(position).getJoinActivity());//form
                if (form == 2) {
                    showActivity(TreaDetialActivity.class, bundle, 0);
                }else {
                    bundle.putInt("acitvityStatus",histories.get(position).getActivityStatus());//form
                    bundle.putString("luckUser",histories.get(position).getLuckUser());//form
                    bundle.putInt("luckUserID",histories.get(position).getLuckUserId());//form
                    bundle.putString("cover",histories.get(position).getCover());//form
                    showActivity(DiggingDetialActivity.class, bundle, 0);
                }

            }
        });
    }

    private void getHistoryList() {
        HashMap<String, String> map = new HashMap<>();
        map.put("pageNo", pageNo + "");
        map.put("pageSize", pageSize + "");
        if (form == 1){
            presenter.getJoinHistory(map);
        }else if (form == 2) {
            presenter.getVoteHistory(map);
        }else if (form == 3){
            presenter.getUserHistory(map);
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
        getHistoryList();
    }




    @Override
    public void doFiled(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
        adapter.setEnableLoadMore(true);
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void setPresenter(HistoryContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
