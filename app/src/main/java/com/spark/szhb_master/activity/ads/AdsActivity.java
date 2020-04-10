package com.spark.szhb_master.activity.ads;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.releaseAd.PubAdsActivity;
import com.spark.szhb_master.adapter.AdsAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.Ads;
import com.spark.szhb_master.utils.CopyToast;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import config.Injection;

/**
 * 我的广告列表
 */
public class AdsActivity extends BaseActivity implements AdsContract.View {
    public static final int RETURN_ADS = 0;
    @BindView(R.id.rvContent)
    RecyclerView rvAds;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.llHead)
    LinearLayout llHead;
    private AdsContract.Presenter presenter;
    private List<Ads> objList = new ArrayList<>();
    private AdsAdapter adapter;
    private String username;
    private String avatar;
    private AdsDialog adsDialog;
    private Ads ads;
    private int pageNo = 1;
    private int comefrom;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == RETURN_ADS) {
            pageNo = 1;
            getList(true);
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.fragment_c2c_list;
    }

    @Override
    protected void initView() {
        super.initView();
        llHead.setVisibility(View.VISIBLE);
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.my_ads));
        tvGoto.setVisibility(View.INVISIBLE);
        adsDialog = new AdsDialog(activity);
        new AdsPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            username = bundle.getString("username");
            avatar = bundle.getString("avatar");
            comefrom = bundle.getInt("comefrom",0);
        }
        initRvAds();
    }

    @Override
    protected void setListener() {
        super.setListener();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ads = (Ads) adapter.getItem(position);
                if (ads != null) {
                    adsDialog.setAds(ads);
                    adsDialog.show();
                }
            }
        });

        adsDialog.getTvDelete().setOnClickListener(new View.OnClickListener() { // 删除
            @Override
            public void onClick(View view) {
                doOption(2);
                adsDialog.dismiss();
            }
        });

        adsDialog.getTvEdit().setOnClickListener(new View.OnClickListener() { //修改
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("type", ads.getAdvertiseType() == 0 ? GlobalConstant.BUY : GlobalConstant.SELL);
                bundle.putSerializable("ads", ads);
                showActivity(PubAdsActivity.class, bundle, RETURN_ADS);
                adsDialog.dismiss();
            }
        });

        adsDialog.getTvReleaseAgain().setOnClickListener(new View.OnClickListener() { // 下架
            @Override
            public void onClick(View view) {
                if (ads.getStatus() == 0) {
                    doOption(1);
                } else {
                    doOption(0);
                }
                adsDialog.dismiss();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                getList(false);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNo = pageNo + 1;
                getList(false);
            }
        }, rvAds);
    }

    /**
     * 获取列表
     */
    private void getList(boolean isShow) {
        if (isShow)
            displayLoadingPopup();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageSize", GlobalConstant.PageSize + "");
        map.put("pageNo", pageNo + "");
        presenter.allAds(map);
    }

    @Override
    protected void loadData() {
        getList(true);
    }


    private void initRvAds() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvAds.setLayoutManager(manager);
        adapter = new AdsAdapter(R.layout.item_ads, objList, username, avatar, activity);
        adapter.bindToRecyclerView(rvAds);
        adapter.setEnableLoadMore(false);
        if (comefrom == 1) {
            CopyToast.showText(this, getString(R.string.pub_ads_up));
        }
    }


    @Override
    public void setPresenter(AdsContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void allAdsSuccess(List<Ads> obj) {
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        if (refreshLayout != null) {
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
        }
        if (obj == null) return;
        if (pageNo == 1) {
            objList.clear();
            if (obj.size() == 0) {
                adapter.setEmptyView(R.layout.empty_no_message);
                adapter.notifyDataSetChanged();
            }
        } else if (obj.size() == 0) {
            adapter.loadMoreEnd();
        }
        objList.addAll(obj);
        adapter.notifyDataSetChanged();
        adapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    public void allAdsFail(Integer code, String toastMessage) {
        adapter.setEnableLoadMore(true);
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }

    @Override
    public void doOptionSucess(String obj) {
        ToastUtils.showToast(obj);
        pageNo = 1;
        getList(true);
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    /**
     * 操作
     *
     * @param type 0-上架，1-下架，2-删除
     */
    public void doOption(int type) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", ads.getId() + "");
        if (type == 0)
            presenter.release(map);
        else if (type == 1)
            presenter.offShelf(map);
        else
            presenter.delete(map);
    }
}
