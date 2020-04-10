package com.spark.szhb_master.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.OrderAdapter;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.base.BaseLazyFragment;
import com.spark.szhb_master.entity.Order;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.NetCodeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import config.Injection;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2018/2/5.
 */

public class OrderFragment extends BaseLazyFragment implements OrderContract.View {
    @BindView(R.id.rvContent)
    RecyclerView rvContent;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private List<Order> orders = new ArrayList<>();
    private OrderAdapter adapter;
    private Status status;
    private OrderContract.Presenter presenter;
    private int pageNo = 0;
    private boolean isLoad = false;

    public static OrderFragment getInstance(Status status) {
        OrderFragment fragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("status", status);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            getList();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_c2c_list;
    }

    @Override
    protected void initData() {
        new OrderPresenter(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            status = (Status) bundle.getSerializable("status");
        }
        initRv();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getList();
            }
        }, 500);
    }

    /**
     * 获取订单列表
     */
    private void getList() {
        if (!isLoad) {
            displayLoadingPopup();
            isLoad = true;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("pageNo", pageNo + "");
        map.put("pageSize", GlobalConstant.PageSize + "");
        map.put("status", status.getStatus() + "");
        presenter.myOrder(map);
    }

    @Override
    protected void loadData() {
        getList();
    }


    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 0;
                getList();
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("orderSn", orders.get(position).getOrderSn());
//                Intent intent = new Intent(activity, OrderDetailActivity.class);
//                intent.putExtras(bundle);
//                startActivityForResult(intent, 0);
                showActivity(OrderDetailActivity.class, bundle);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNo = pageNo + 1;
                getList();
            }
        }, rvContent);
    }

    private void initRv() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        adapter = new OrderAdapter(getActivity(), R.layout.item_order, orders, status);
        adapter.bindToRecyclerView(rvContent);
        adapter.setEnableLoadMore(false);
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    @Override
    public void setPresenter(OrderContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void myOrderFail(Integer code, String toastMessage) {
        adapter.setEnableLoadMore(true);
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        NetCodeUtils.checkedErrorCode(getmActivity(), code, toastMessage);
    }

    @Override
    public void myOrderSuccess(List<Order> obj) {
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        if (refreshLayout != null) {
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
        }
        if (obj == null) return;
        if (pageNo == 0) {
            orders.clear();
            if (obj.size() == 0) {
                adapter.setEmptyView(R.layout.empty_no_message);
            }
        } else if (obj.size() == 0) adapter.loadMoreEnd();
        orders.addAll(obj);
        adapter.notifyDataSetChanged();
        adapter.disableLoadMoreIfNotFullPage();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public enum Status {
        CANC(0, MyApplication.getApp().getResources().getStringArray(R.array.order_status)[3]),
        UNPAID(1, MyApplication.getApp().getResources().getStringArray(R.array.order_status)[0]),
        PAID(2, MyApplication.getApp().getResources().getStringArray(R.array.order_status)[1]),
        DONE(3, MyApplication.getApp().getResources().getStringArray(R.array.order_status)[2]),
        COMPLAINING(4, MyApplication.getApp().getResources().getStringArray(R.array.order_status)[4]);

        private int status;
        private String statusStr;

        Status(int status, String statusStr) {
            this.status = status;
            this.statusStr = statusStr;
        }

        public int getStatus() {
            return status;
        }

        public String getStatusStr() {
            return statusStr;
        }
    }
}
