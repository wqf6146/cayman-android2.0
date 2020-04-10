package com.spark.szhb_master.activity.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.Treasure.HistoryActivity;
import com.spark.szhb_master.activity.Treasure.TreaDetialActivity;
import com.spark.szhb_master.activity.main.presenter.PreheatingPresenter;
import com.spark.szhb_master.activity.main.presenter.TreasureContract;
import com.spark.szhb_master.adapter.PreheatAdapter;
import com.spark.szhb_master.base.BaseTransFragment;
import com.spark.szhb_master.dialog.CallDialog;
import com.spark.szhb_master.dialog.LoginDialog;
import com.spark.szhb_master.dialog.PreheaDialog;
import com.spark.szhb_master.dialog.ShiMingDialog;
import com.spark.szhb_master.dialog.TreawaDialog;
import com.spark.szhb_master.entity.Preheatinginfo;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.entity.Treasure;
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

public class PreheatingFragment extends BaseTransFragment implements TreasureContract.PreheatingView {

    private PreheatAdapter adapter;
    private ArrayList<Treasure.DataBean.ItemsBean> trea = new ArrayList<>();
    private int pageNo = 1;

    @BindView(R.id.preheatRecycler)
    ExpandableListView preheatRecycler;
    private LinearLayout tv_footer;
    private TextView tv_foot;
    private List<Preheatinginfo> groupList = new ArrayList<>();
    private List<List<Preheatinginfo.ActivityCommodityInfoListBean>> childList = new ArrayList<>();

    private TreasureContract.PreheatingPresenter presenter;
    private boolean isfirst = false;
    private List<Preheatinginfo.ActivityCommodityInfoListBean> datas;
    private TreawaDialog dialog;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.refreshLayout_two)
    SwipeRefreshLayout refreshLayout_two;
    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;
    private TreawaDialog treawaDialog;
    @BindView(R.id.tvLogin_empty)
    TextView tvLogin_empty;
    private User user;
    private LoginDialog loginDialog;

    @Override
    protected String getmTag() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_preheating;
    }

    private void initAdapter() {

        dialog = new TreawaDialog(getContext());
        adapter = new PreheatAdapter(getContext(), groupList, childList);
        preheatRecycler.setAdapter(adapter);
        user = MyApplication.getApp().getCurrentUser();
        loginDialog = new LoginDialog(getContext());

        tv_footer = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.treasure_foot, null);
        preheatRecycler.addFooterView(tv_footer);
        TextView tv = tv_footer.findViewById(R.id.tvLogin);
        tv.setBackground(getResources().getDrawable(R.mipmap.his_back_red));
        tv.setText(getResources().getString(R.string.historical_activity));
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyApplication.getApp().isLogin()) {
                    loginDialog.show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("form", 2);
                    showActivity(HistoryActivity.class, bundle, 0);
                }
            }
        });

        tvLogin_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyApplication.getApp().isLogin()) {
                    loginDialog.show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("form", 2);
                    showActivity(HistoryActivity.class, bundle, 0);
                }
            }
        });

        preheatRecycler.setGroupIndicator(null);

        preheatRecycler.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Bundle bundle = new Bundle();
                bundle.putInt("commodityId", groupList.get(i).getActivityCommodityInfoList().get(i1).getActivityCommodityId());
                bundle.putString("name", groupList.get(i).getActivityCommodityInfoList().get(i1).getCommodityName());
                bundle.putInt("degree", groupList.get(i).getActivityCommodityInfoList().get(i1).getVoteAmount());
                bundle.putInt("joinAmount", groupList.get(i).getActivityCommodityInfoList().get(i1).getJoinAmount());
                bundle.putString("imgs", groupList.get(i).getActivityCommodityInfoList().get(i1).getImgs());
                bundle.putInt("voteId", groupList.get(i).getActivityId());
                bundle.putInt("joinVote", groupList.get(i).getActivityCommodityInfoList().get(i1).getJoinVote());
                bundle.putString("startTime", groupList.get(i).getStartTime());
                bundle.putString("endTime", groupList.get(i).getEndTime());
                showActivity(TreaDetialActivity.class, bundle, 0);
                return true;
            }
        });
        //接口的调用，获取传递的id
        adapter.setOnClickMyTextView(new PreheatAdapter.onClickMyTextView() {
            @Override
            public void myTextViewClick(int postion, int commodI, int activityI) {
                presenter.safeSetting();
                commodId = commodI;
                activityId = activityI;

            }
        });
        treawaDialog = new TreawaDialog(getContext());
        preheatRecycler.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {

            }
        });
    }

    private SafeSetting safeSetting;
    private int commodId;
    private int activityId;

    @Override
    public void safeSettingSuccess(SafeSetting obj) {
        if (obj == null)
            return;
        safeSetting = obj;
        if (safeSetting.getRealVerified() == 0) {
            ShiMingDialog shiMingDialog = new ShiMingDialog(getContext());
            String name = safeSetting.getRealNameRejectReason();
            if (safeSetting.getRealVerified() == 0) {
                if (safeSetting.getRealAuditing() == 1) {
                    shiMingDialog.setEntrust(7, name, 1);
                } else {
                    if (safeSetting.getRealNameRejectReason() != null)
                        shiMingDialog.setEntrust(8, name, 1);
                    else
                        shiMingDialog.setEntrust(9, name, 1);
                }
            } else {
                shiMingDialog.setEntrust(6, name, 1);
            }
            shiMingDialog.show();
        }else {
            if (commodId == 0 && activityId == 0) {
                presenter.getVoteList();
            } else {
                commod_id = commodId;
                activity_id = activityId;
                HashMap<String, String> map = new HashMap<>();
                map.put("commodityId", commodId + "");
                presenter.getCheckVote(map);//检测投票GCX
            }
        }
    }

    private int commod_id;
    private int activity_id;

    //10.19更改
//    @Override
//    protected void initView() {
//        super.initView();
//        isfirst = true;
//    }
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (isfirst) {
//            presenter.getVoteList();
//        }
//    }
    @Override
    protected void initData() {
        super.initData();
        new PreheatingPresenter(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
        initAdapter();
    }


    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
    }
    //10.19更改
//    @Override
//    public void onResume() {
//        super.onResume();
//        presenter.getVoteList();
//    }

    @Override
    protected void loadData() {
        super.loadData();
//        if (MyApplication.getApp().isLogin()) {
//            refreshLayout.setVisibility(View.VISIBLE);
//            refreshLayout_two.setVisibility(View.VISIBLE);
        presenter.getVoteList();
//        } else {
//            refreshLayout.setVisibility(View.GONE);
//            refreshLayout_two.setVisibility(View.GONE);
//        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 0;
                presenter.getVoteList();
            }
        });
        treawaDialog.tvbtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                treawaDialog.tvbtn().setEnabled(false);
                HashMap<String, String> map = new HashMap<>();
                map.put("commodityId", commod_id + "");
                map.put("voteId", activity_id + "");
                voteAmount = treawaDialog.seekBar().getProgress();
                map.put("voteAmount", voteAmount + "");
                presenter.getVote(map);
            }
        });

        refreshLayout_two.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 0;
                presenter.getVoteList();
            }
        });
    }

    @Override
    public void getVoteListSuccess(Object obj) {
        setVoteList((List<Preheatinginfo>) obj);
    }

    private int voteAmount;
    private CallDialog callDialog;

    @Override
    public void getVoteSuccess(int obj) {
        presenter.getVoteList();
        callDialog = new CallDialog(getContext());
        callDialog.setEntrust(voteAmount, obj);
        callDialog.show();
        mCallback.onArticleSelected(2);
        treawaDialog.tvbtn().setEnabled(true);
        treawaDialog.dismiss();
    }


    @Override
    public void getCheckVoteSuccess(int min, int max) {
        if (max == 0) {
            PreheaDialog preheaDialog = new PreheaDialog(getContext());
            preheaDialog.show();
        } else {
            treawaDialog.setEntrust(2, commod_id, activity_id, min, max, 0);
            treawaDialog.show();
        }
    }

    @Override
    public void geCheckVoteFailed(Integer code, String toastMessage) {
        if (code == 500 && toastMessage.equals("打Call额度已用完!")) {
            PreheaDialog preheaDialog = new PreheaDialog(getContext());
            preheaDialog.show();
        }
    }

    @Override
    public void getCheckJoinSuccess(int min, int masx) {

    }

    @Override
    public void getJoinSuccess(Object obj) {

    }

    private void setVoteList(List<Preheatinginfo> obj) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        refreshLayout_two.setEnabled(true);
        refreshLayout_two.setRefreshing(false);
        if (obj == null || obj.size() == 0) {
            rlEmpty.setVisibility(View.VISIBLE);
            refreshLayout_two.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
        } else {
            refreshLayout_two.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
            childList.clear();
            groupList.clear();
            groupList.addAll(obj);
            for (int i = 0; i < obj.size(); i++) {
                datas = obj.get(i).getActivityCommodityInfoList();
                childList.add(datas);
            }
            adapter.notifyDataSetChanged();
            preheatRecycler.setGroupIndicator(null);
            for (int i = 0; i < groupList.size(); i++) {
                preheatRecycler.expandGroup(i);
            }
        }
    }

    @Override
    public void doFiled(Integer code, String toastMessage) {
//        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
        ToastUtils.show(toastMessage, Toast.LENGTH_SHORT);
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        refreshLayout_two.setEnabled(true);
        refreshLayout_two.setRefreshing(false);
        treawaDialog.tvbtn().setEnabled(true);
        treawaDialog.dismiss();
    }

    @Override
    public void setPresenter(TreasureContract.PreheatingPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    OnHeadlineSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(int position);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

}
