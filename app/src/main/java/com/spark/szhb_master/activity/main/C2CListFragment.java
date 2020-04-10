package com.spark.szhb_master.activity.main;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.buy_or_sell.C2CBuyOrSellActivity;
import com.spark.szhb_master.activity.login.LoginActivity;
import com.spark.szhb_master.activity.main.presenter.C2CListPresenterImpl;
import com.spark.szhb_master.adapter.C2CListAdapter;
import com.spark.szhb_master.base.BaseLazyFragment;
import com.spark.szhb_master.dialog.ShiMingDialog;
import com.spark.szhb_master.entity.C2C;
import com.spark.szhb_master.entity.CoinInfo;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.ui.AvatarImageView;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.ToastUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import config.Injection;

/**
 * Created by Administrator on 2018/2/28.
 */

public class C2CListFragment extends BaseLazyFragment implements MainContract.C2CListView {
    @BindView(R.id.rvContent)
    RecyclerView rvContent;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private ArrayList<C2C.C2CBean> c2cs = new ArrayList<>();
    private C2CListAdapter adapter;
    private CoinInfo coinInfo;
    private MainContract.C2CListPresenter presenter;
    private int pageNo = 1;
    private String advertiseType = GlobalConstant.SELL;
    private boolean isInit;

    public static C2CListFragment getInstance(CoinInfo coinInfo) {
        C2CListFragment c2CFragment = new C2CListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("coinInfo", coinInfo);
        c2CFragment.setArguments(bundle);
        return c2CFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_c2c_list;
    }

    @Override
    protected void initView() {
        super.initView();
        setShowBackBtn(true);
    }

    @Override
    protected void initData() {
        presenter = new C2CListPresenterImpl(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
        initRvContent();
        Bundle bundle = getArguments();
        if (bundle != null) {
            coinInfo = (CoinInfo) bundle.getSerializable("coinInfo");
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
        if (coinInfo != null && !isInit) {
            isInit = true;
            isNeedLoad = false;
            getC2cList(true);
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                getC2cList(false);
            }
        });

        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNo = pageNo + 1;
                getC2cList(false);
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if ( !MyApplication.getApp().isLogin()) {
                    showActivity(LoginActivity.class, null, LoginActivity.RETURN_LOGIN);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("c2cBean", c2cs.get(position));
                showActivity(C2CBuyOrSellActivity.class, bundle);
//                presenter.safeSetting(position);
            }
        });

    }


    private void initRvContent() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        adapter = new C2CListAdapter(getActivity(), R.layout.item_c2c_list, c2cs);
        AvatarImageView ivHeader ;
        adapter.bindToRecyclerView(rvContent);
        adapter.isFirstOnly(true);
        adapter.setEnableLoadMore(false);
    }

    /**
     * 获取列表数据
     */
    public void getC2cList(boolean isShow) {
        if (isShow)
            displayLoadingPopup();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageNo", pageNo + "");
        map.put("pageSize", GlobalConstant.PageSize + "");
        map.put("advertiseType", advertiseType);
        map.put("id", coinInfo.getId());
        map.put("unit", coinInfo.getUnit());
        presenter.advertise(map);
    }


    public void setAdvertiseType(String advertiseType) {
        this.advertiseType = advertiseType;
        getC2cList(true);
    }

    @Override
    public void advertiseSuccess(C2C obj) {
        hideLoadingPopup();
        try {
            adapter.setEnableLoadMore(true);
            adapter.loadMoreComplete();
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
            List<C2C.C2CBean> c2cs = obj.getContext();
            if (c2cs != null && c2cs.size() > 0) {
                if (pageNo == 1) {
                    this.c2cs.clear();
                } else {
                    adapter.loadMoreEnd();
                }
                this.c2cs.addAll(c2cs);
                adapter.notifyDataSetChanged();
            } else {
                if (pageNo == 1) {
//                if (pageNo == 1 && obj.getTotalElement() == 0 ) {
                    this.c2cs.clear();
                    adapter.setEmptyView(R.layout.empty_no_message);
                    adapter.notifyDataSetChanged();
                }
            }
            adapter.disableLoadMoreIfNotFullPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void advertiseFail(Integer code, String toastMessage) {
        hideLoadingPopup();
        adapter.setEnableLoadMore(true);
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        NetCodeUtils.checkedErrorCode(getmActivity(), code, toastMessage);
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        ToastUtils.showToast(toastMessage);
    }
    private SafeSetting safeSetting;
    @Override
    public void safeSettingSuccess(SafeSetting obj,int position) {
        if (obj == null)
            return;
        safeSetting = obj;
        if (safeSetting.getRealVerified() == 0) {
            ShiMingDialog shiMingDialog = new ShiMingDialog(getContext());
            String name = safeSetting.getRealNameRejectReason();
            if (safeSetting.getRealVerified() == 0) {
                if (safeSetting.getRealAuditing() == 1) {
                    shiMingDialog.setEntrust(7, name,1);
                } else {
                    if (safeSetting.getRealNameRejectReason() != null)
                        shiMingDialog.setEntrust(8, name,1);
                    else
                        shiMingDialog.setEntrust(9, name,1);
                }
            } else {
                shiMingDialog.setEntrust(6, name,1);
            }
            shiMingDialog.show();
        } else {

        }
    }

    @Override
    public void setPresenter(MainContract.C2CListPresenter presenter) {
        this.presenter = presenter;
    }
}
