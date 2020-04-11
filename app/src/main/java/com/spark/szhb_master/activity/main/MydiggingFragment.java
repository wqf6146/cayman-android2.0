package com.spark.szhb_master.activity.main;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.Treasure.DiggingDetialActivity;
import com.spark.szhb_master.activity.Treasure.HistoryActivity;
import com.spark.szhb_master.activity.main.presenter.MyDiggingPresenter;
import com.spark.szhb_master.activity.main.presenter.TreasureContract;
import com.spark.szhb_master.adapter.MyDiggingAdapter;
import com.spark.szhb_master.base.BaseTransFragment;
import com.spark.szhb_master.entity.MyDigging;
import com.spark.szhb_master.entity.User;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import config.Injection;

/**
 * Created by Administrator on 2018/9/12 0012.
 */

public class MydiggingFragment extends BaseTransFragment implements TreasureContract.MyDiggingView{

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private int pageNo = 0;
    private int pageSize = 10;
    private TreasureContract.MyDiggingPresenter presenter;
    private List<MyDigging> groupList = new ArrayList<>();
    private MyDiggingAdapter adapter;

    @Override
    protected String getmTag() {
        return null;
    }

    @Override
    protected void initData() {
        super.initData();
        new MyDiggingPresenter(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(manager);
        adapter = new MyDiggingAdapter(getContext(),R.layout.my_digging_detail, groupList);
        adapter.bindToRecyclerView(recycler);
        adapter.isFirstOnly(true);
        adapter.setEnableLoadMore(false);
        recycler.setAdapter(adapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_mydigging;
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 0;
                getUserList();
            }
        });
        
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("commodityId",groupList.get(position).getActivityCommodityId());
                bundle.putString("name",groupList.get(position).getCommodityName());
                bundle.putInt("degree",groupList.get(position).getDegree());
                bundle.putInt("joinAmount",groupList.get(position).getJoinAmount());
                bundle.putString("imgs",groupList.get(position).getImgs());
                bundle.putInt("voteId",groupList.get(position).getActivityId());
                bundle.putInt("completeAmount",groupList.get(position).getCompleteAmount());
                bundle.putInt("joinVote",groupList.get(position).getActivityStatus());//1.正在挖宝，2.挖宝结束，3等待公布，4,等待领取
                bundle.putInt("acitvityStatus",groupList.get(position).getActivityStatus());//1.正在挖宝，2.挖宝结束，3等待公布，4,等待领取
                showActivity(DiggingDetialActivity.class,bundle,0);
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("form",3);
                showActivity(HistoryActivity.class,bundle,0);
            }
        });
    }

    private void getUserList() {
        HashMap<String, String> map = new HashMap<>();
        map.put("pageNo", pageNo + "");
        map.put("pageSize", pageSize + "");
        presenter.getUsermyList();
    }

    //10.18更改
//    @Override
//    public void onResume() {
//        super.onResume();
//        getUserList();
//    }
//    @Override
//    protected void initView() {
//        super.initView();
//        isfirst = true;
//    }
    //10.18更改
    @Override
    protected void loadData() {
        super.loadData();
        User user = MyApplication.getApp().getCurrentUser();
        if (!MyApplication.getApp().isLogin() || StringUtils.isEmpty(user.getNick_name())){
            refreshLayout.setVisibility(View.GONE);
            tvLogin.setVisibility(View.GONE);
        }else {
            refreshLayout.setVisibility(View.VISIBLE);
            tvLogin.setVisibility(View.VISIBLE);
            getUserList();
        }
    }

    //10.18更改
//    private boolean isfirst = false;
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isfirst) {
//            getUserList();
//        }
//    }

    @Override
    public void getUserListmySuccess(Object obj) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        setVoteList((List< MyDigging>)obj);
    }

    private void setVoteList(List<MyDigging> obj) {

        if (obj != null && obj.size() > 0) {
            if (pageNo ==0){
                this.groupList.clear();
            } else {
                adapter.loadMoreEnd();
            }
            this.groupList.addAll(obj);
            adapter.notifyDataSetChanged();
        }else {
            if (pageNo == 0){
                this.groupList.clear();
                adapter.setEmptyView(R.layout.empty_no_trea);
                adapter.notifyDataSetChanged();
            }
        }
        adapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    public void doFiled(Integer code, String toastMessage) {
//        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
        ToastUtils.show(toastMessage, Toast.LENGTH_SHORT);
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
    }

    @Override
    public void setPresenter(TreasureContract.MyDiggingPresenter presenter) {
        this.presenter = presenter;
    }


}
