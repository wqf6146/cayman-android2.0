package com.spark.szhb_master.activity.my_match;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.MatchHisAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.MatchHis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import config.Injection;

/**
 * Created by Administrator on 2018/7/11 0011.
 */

public class MatchHistory extends BaseActivity implements MyMatchHisContract.View {
    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.match_his)
    RecyclerView match_his;
    @BindView(R.id.his_refreshLayout)
    SwipeRefreshLayout his_refreshLayout;

    private MyMatchHisContract.Present presenter;
    private MatchHisAdapter adapter;
    private String dateString;
    private int pageNo = 0;
    private int pageSize = 15;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MatchHistory.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Date date=new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
        date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        dateString = formatter.format(date);

        System.out.println(dateString);
    }


    @Override
    protected int getActivityLayoutId() {
        return R.layout.match_histoty;
    }

    @Override
    protected void initView() {
        new MatchHisPresent(Injection.provideTasksRepository(getApplicationContext()),this);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        match_his.setLayoutManager(manager);

        adapter = new MatchHisAdapter(R.layout.adapter_match_his, matchHis);

        match_his.setAdapter(adapter);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        his_refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        initrel();
    }
    private void refresh() {
        presenter.myMatchHis(getToken(),"2018-04-01～"+ dateString,pageNo,pageSize,11);
        adapter.setEnableLoadMore(false);
        adapter.loadMoreEnd(false);
    }


    private void initrel() {
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, match_his);
    }

    private void loadMore() {
        his_refreshLayout.setEnabled(false);
        presenter.myMatchHis(getToken(),"2018-04-01～"+ dateString,++pageNo,pageSize,11);
    }

    @Override
    protected void loadData() {
        presenter.myMatchHis(getToken(),"2018-04-01～"+ dateString,pageNo,pageSize,11);
    }


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            immersionBar.setTitleBar(MatchHistory.this, llTitle);
            isSetTitle = true;
        }
    }

    @Override
    public void setPresenter(MyMatchHisContract.Present presenter) {
        this.presenter = presenter;
    }

    @Override
    public void myMatchHisFail(Integer code, String toastMessage) {
        adapter.setEnableLoadMore(true);
        his_refreshLayout.setEnabled(true);
        his_refreshLayout.setRefreshing(false);
    }
    private List<MatchHis> matchHis = new ArrayList<>();

    @Override
    public void myMatchHisSuccess(List<MatchHis> obj) {

        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        his_refreshLayout.setEnabled(true);
        his_refreshLayout.setRefreshing(false);

        if (obj == null) return;
        if (pageNo == 0) this.matchHis.clear();
        else if (obj.size() == 0) adapter.loadMoreEnd();
        matchHis.addAll(obj);
        adapter.notifyDataSetChanged();
        adapter.disableLoadMoreIfNotFullPage();

//        if (obj != null && obj.size() > 0) {
//            if (pageNo ==0){
//                this.matchHis.clear();
//            } else {
//                adapter.loadMoreEnd();
//            }
//            this.matchHis.addAll(obj);
//            adapter.notifyDataSetChanged();
//        }else {
//            if (pageNo == 0){
//                this.matchHis.clear();
//                adapter.setEmptyView(R.layout.empty_no_treaing);
//                adapter.notifyDataSetChanged();
//            }
//        }
//        adapter.disableLoadMoreIfNotFullPage();
    }
}
