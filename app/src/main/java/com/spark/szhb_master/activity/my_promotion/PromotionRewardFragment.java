package com.spark.szhb_master.activity.my_promotion;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.PromotionRewardAdapter;
import com.spark.szhb_master.base.BaseLazyFragment;
import com.spark.szhb_master.entity.Promotion;
import com.spark.szhb_master.entity.PromotionRecord;
import com.spark.szhb_master.entity.PromotionReward;
import com.spark.szhb_master.utils.NetCodeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import config.Injection;

/**
 * 我的好友
 * Created by Administrator on 2018/5/8 0008.
 */

public class PromotionRewardFragment extends BaseLazyFragment implements PromotionRewardContract.View {
    @BindView(R.id.rvContent)
    RecyclerView rvContent;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private PromotionRewardContract.Presenter presenter;
    private List<PromotionReward> rewards = new ArrayList<>();
    private PromotionRewardAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_c2c_list;
    }


    private void refresh() {
        presenter.getPromotionReward();
        adapter.setEnableLoadMore(false);
        adapter.loadMoreEnd(false);
    }

    @Override
    protected void initData() {
        new PromotionRewardPresenter(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
        initRv();
        isPrepared = true;
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void initRv() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        adapter = new PromotionRewardAdapter(R.layout.item_promotion_reward, rewards);
        adapter.bindToRecyclerView(rvContent);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, rvContent);
        adapter.setEnableLoadMore(false);
    }

    private void loadMore() {
        refreshLayout.setEnabled(false);
        presenter.getPromotionReward();
    }

    private boolean isPrepared;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isPrepared && isVisibleToUser) {
            ((OperateCallback) getActivity()).select();
        }
    }

    @Override
    protected void loadData() {
        presenter.getPromotionReward();
        isNeedLoad = false;
        ((OperateCallback) getActivity()).select();
    }

    @Override
    public void setPresenter(PromotionRewardContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getPromotionFail(Integer code, String toastMessage) {

    }

    @Override
    public void getPromotionSuccess(List<PromotionRecord> obj) {

    }

    @Override
    public void getPromotionRewardFail(Integer code, String toastMessage) {
        adapter.setEnableLoadMore(true);
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        NetCodeUtils.checkedErrorCode(getmActivity(), code, toastMessage);
    }

    @Override
    public void getPromotionRewardSuccess(List<PromotionReward> obj, Promotion objs,String wcnm ) {
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        rewards.clear();
        if (obj == null) return;
        if (obj.size() == 0) {
            adapter.setEmptyView(R.layout.empty_no_message);
        } else {
            rewards.addAll(obj);
        }
        adapter.notifyDataSetChanged();
        adapter.disableLoadMoreIfNotFullPage();
    }

    public interface OperateCallback {
        void select();
    }
}
