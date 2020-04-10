package com.spark.szhb_master.activity.my_promotion;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.PromotionRecordAdapter;
import com.spark.szhb_master.base.BaseLazyFragment;
import com.spark.szhb_master.entity.Promotion;
import com.spark.szhb_master.entity.PromotionRecord;
import com.spark.szhb_master.utils.NetCodeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import config.Injection;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

/**
 * 推广好友
 */
public class PromotionRecordFragment extends BaseLazyFragment implements PromotionRecordContract.View {
    @BindView(R.id.rvContent)
    RecyclerView rvContent;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private PromotionRecordContract.Presenter presenter;
    private List<PromotionRecord> records = new ArrayList<>();
    private PromotionRecordAdapter adapter;
    private boolean isPrepared;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_c2c_list;
    }

    @Override
    protected void initData() {
        new PromotionRecordPresenter(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
        initRv();
        //已经初始化
        isPrepared = true;
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setEnableLoadMore(false);
                adapter.loadMoreEnd(false);
                presenter.getPromotion();
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                refreshLayout.setEnabled(false);
                presenter.getPromotion();
            }
        }, rvContent);
    }

    /**
     * 初始化列表
     */
    private void initRv() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        adapter = new PromotionRecordAdapter(R.layout.item_promotion_record, records);
        adapter.bindToRecyclerView(rvContent);
        adapter.setEnableLoadMore(false);
    }

    @Override
    protected void loadData() {
        presenter.getPromotion();
        isNeedLoad = false;

        ((OperateCallback) getActivity()).selectReward();
    }

    @Override
    public void setPresenter(PromotionRecordContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getPromotionFail(Integer code, String toastMessage) {
        adapter.setEnableLoadMore(true);
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        NetCodeUtils.checkedErrorCode(getmActivity(), code, toastMessage);
    }

    @Override
    public void getPromotionSuccess(List<PromotionRecord> obj) {
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        if (refreshLayout != null) {
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
        }
        records.clear();
        if (obj == null) return;
        if (obj.size() == 0) {
            adapter.setEmptyView(R.layout.empty_no_message);
        } else {
            records.addAll(obj);
        }
        adapter.notifyDataSetChanged();
        adapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isPrepared && isVisibleToUser) {
            ((OperateCallback) getActivity()).selectReward();
        }

    }
    @Override
    public void getPromotionRewardSuccess(Promotion obj,String wcnm) {

    }

    @Override
    public void getPromotionRewardFail(Integer code, String toastMessage) {

    }


    public interface OperateCallback {
        void selectReward();
    }

}
