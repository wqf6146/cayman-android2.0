package com.spark.szhb_master.activity.Treasure;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.PowerDetailAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.PowerDetail;
import com.spark.szhb_master.utils.MathUtils;
import com.spark.szhb_master.utils.NetCodeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * Created by Administrator on 2018/9/14 0014.
 */
public class PowerActivity extends BaseActivity implements PowerContract.View{

    private int pageNo = 0;
    private int pageSize = 10;
    @BindView(R.id.trea_detail_recy)
    RecyclerView trea_detail_recy;   @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.gcx_num)
    TextView gcx_num;
    @BindView(R.id.anminal_text)
    TextView anminal_text;

    private PowerDetailAdapter adapter;
    private List<PowerDetail> callHistories = new ArrayList<>();
    private PowerContract.Presenter presenter;


    @Override
    protected int getActivityLayoutId() {
        return R.layout.power_layout;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            immersionBar.setTitleBar(PowerActivity.this, llTitle);
            isSetTitle = true;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        tvGoto.setVisibility(View.INVISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setBackground(null);
        tvTitle.setText(getResources().getString(R.string.calculation_of_power));
        llTitle.setBackgroundDrawable(null);
        new PowerPresenter(Injection.provideTasksRepository(getApplicationContext()), this);

        Bundle bundle = getIntent().getExtras();
        int gcx_nu = bundle.getInt("gcx_num");
        double num = bundle.getDouble("num");
        gcx_num.setText("GCX " + gcx_nu);
        anminal_text.setText(String.valueOf(MathUtils.getRundNumber(num, 2, null))+"");


        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        trea_detail_recy.setLayoutManager(manager);
        adapter = new PowerDetailAdapter(this,R.layout.power_detail, callHistories);
        adapter.bindToRecyclerView(trea_detail_recy);
        adapter.isFirstOnly(true);
        adapter.setEnableLoadMore(false);
        trea_detail_recy.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 0;
                getPowerDetail();
            }
        });

        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNo = pageNo + 1;
                getPowerDetail();
            }
        });
    }

    @Override
    protected void loadData() {
        super.loadData();
        getPowerDetail();
    }

    private void getPowerDetail() {
        HashMap<String, String> map = new HashMap<>();
        map.put("pageNo", pageNo + "");
        map.put("pageSize", pageSize + "");
        presenter.getPowerDetail(map);
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
    public void getPowerDetailSuccess(List<PowerDetail> obj) {

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
    public void doFiled(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
        adapter.setEnableLoadMore(true);
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void setPresenter(PowerContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
