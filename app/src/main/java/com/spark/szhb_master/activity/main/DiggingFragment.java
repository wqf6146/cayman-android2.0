package com.spark.szhb_master.activity.main;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.Treasure.DiggingDetialActivity;
import com.spark.szhb_master.activity.Treasure.HistoryActivity;
import com.spark.szhb_master.activity.main.presenter.DiggingPresenter;
import com.spark.szhb_master.activity.main.presenter.TreasureContract;
import com.spark.szhb_master.adapter.TreasureAdapter;
import com.spark.szhb_master.base.BaseTransFragment;
import com.spark.szhb_master.dialog.CallDialog;
import com.spark.szhb_master.dialog.DiggingDialog;
import com.spark.szhb_master.dialog.ShiMingDialog;
import com.spark.szhb_master.dialog.TreawaDialog;
import com.spark.szhb_master.entity.Preheatinginfo;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.entity.Treasure;
import com.spark.szhb_master.utils.DateUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import config.Injection;

/**
 * Created by Administrator on 2018/9/12 0012.
 */

public class DiggingFragment extends BaseTransFragment implements TreasureContract.DiggingView {


    private TreasureAdapter adapter;
    private ArrayList<Treasure.DataBean.ItemsBean> trea = new ArrayList<>();
    private int pageNo = 0;
    private int pageSize = 10;

    @BindView(R.id.diggingRecycler)
    ExpandableListView diggingRecycler;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.refreshLayout_two)
    SwipeRefreshLayout refreshLayout_two;
    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;
    @BindView(R.id.tvLogin_empty)
    TextView tvLogin_empty;
    private LinearLayout tv_footer;
    private List<Preheatinginfo> groupList = new ArrayList<>();
    private List<List<Preheatinginfo.ActivityCommodityInfoListBean>> childList = new ArrayList<>();
    private List<Preheatinginfo.ActivityCommodityInfoListBean> datas;
    private TreasureContract.DiggingPresenter presenter;

    @Override
    protected String getmTag() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                long lasttiem = 0;
                for (int i = 0; i < groupList.size(); i++) {
                    if (!StringUtils.isEmpty(groupList.get(i).getEndTime())) {
                        lasttiem = DateUtils.getTimeMillis(null, groupList.get(i).getEndTime());
                    }
                    long time_start = DateUtils.getTimeMillis(null, groupList.get(i).getStartTime());
                    long now_time = DateUtils.getSimpleDateFormat();
                    if (now_time < time_start) {//小于开始时间
                        tiem = lasttiem;
                    }
                    adapter.countDown(lasttiem);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };
    private long tiem;

    private Timer timer = new Timer();
    private TimerTask timerTask;
    private void initAdapter() {
        adapter = new TreasureAdapter(getContext(), groupList, childList);
        diggingRecycler.setAdapter(adapter);

        tv_footer = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.treasure_foot, null);
        diggingRecycler.addFooterView(tv_footer);

        TextView tv = tv_footer.findViewById(R.id.tvLogin);
        tv.setText(getResources().getString(R.string.historical_treasure));

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("form", 1);
                showActivity(HistoryActivity.class, bundle, 0);
            }
        });

        tvLogin_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("form", 1);
                showActivity(HistoryActivity.class, bundle, 0);
            }
        });

        diggingRecycler.setGroupIndicator(null);

        diggingRecycler.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Bundle bundle = new Bundle();
                bundle.putInt("commodityId", groupList.get(i).getActivityCommodityInfoList().get(i1).getActivityCommodityId());
                bundle.putString("name", groupList.get(i).getActivityCommodityInfoList().get(i1).getCommodityName());
                bundle.putInt("degree", groupList.get(i).getActivityCommodityInfoList().get(i1).getDegree());
                bundle.putInt("joinAmount", groupList.get(i).getActivityCommodityInfoList().get(i1).getJoinAmount());
                bundle.putString("imgs", groupList.get(i).getActivityCommodityInfoList().get(i1).getImgs());
                bundle.putInt("voteId", groupList.get(i).getActivityId());
                bundle.putInt("completeAmount", groupList.get(i).getActivityCommodityInfoList().get(i1).getCompleteAmount());
                bundle.putInt("joinVote", groupList.get(i).getActivityCommodityInfoList().get(i1).getActivityStatus());//1.正在挖宝，2.挖宝结束，3等待公布
                bundle.putInt("acitvityStatus", groupList.get(i).getActivityCommodityInfoList().get(i1).getActivityStatus());//1.正在挖宝，2.挖宝结束，3等待公布
                bundle.putString("startTime", groupList.get(i).getStartTime());
                showActivity(DiggingDetialActivity.class, bundle, 0);
                return true;
            }
        });

        adapter.setOnClickMyTextView(new TreasureAdapter.onClickMyTextView() {
            @Override
            public void myTextViewClick(int i, int commodId, int activityId) {

                presenter.safeSetting();


                commod_id = commodId;
                activity_id = activityId;
//                HashMap<String, String> map = new HashMap<>();
//                map.put("commodityId", commodId + "");
//                presenter.getCheckJoin(map);//检测投票GCX
            }
        });
        treawaDialog = new TreawaDialog(getContext());
        presenter.getUserList();
    }

    private int commod_id;
    private int activity_id;
    private SafeSetting safeSetting;

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
            HashMap<String, String> map = new HashMap<>();
            map.put("commodityId", commod_id + "");
            presenter.getCheckJoin(map);//检测投票GCX
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //10.19更改
//        presenter.getUserList();
        if (isfirst) { // 第一次添加该fragment，手动出发请求
            loadData();
            isfirst = false;
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
//        if (MyApplication.getApp().isLogin()) {
        presenter.getUserList();
//        } else {
//            refreshLayout.setVisibility(View.GONE);
//            refreshLayout_two.setVisibility(View.GONE);
//        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_digging;
    }

    @Override
    protected void initData() {
        super.initData();
        new DiggingPresenter(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
        initAdapter();
    }

    private TreawaDialog treawaDialog;
    private int voteAmount;
    private CallDialog callDialog;

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 0;
                presenter.getUserList();
            }
        });
        treawaDialog.tvbtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                treawaDialog.tvbtn().setEnabled(false);
                HashMap<String, String> map = new HashMap<>();
                map.put("commodityId", commod_id + "");
                voteAmount = treawaDialog.seekBar().getProgress();
                map.put("power", voteAmount + "");
                map.put("activityId", activity_id + "");
                presenter.getJoin(map);
            }
        });

        refreshLayout_two.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 0;
                presenter.getUserList();
            }
        });
    }

    @Override
    public void getUserListSuccess(Object obj) {
        refreshLayout.setVisibility(View.VISIBLE);
        refreshLayout_two.setVisibility(View.VISIBLE);
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        setVoteList((List<Preheatinginfo>) obj);

        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = Message.obtain();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        timer.schedule(timerTask, 2000, 1000);
    }

    @Override
    public void getJoinSuccess(Object obj) {
        presenter.getUserList();
        DiggingDialog diggingDialog = new DiggingDialog(getContext());
        diggingDialog.setEntrust(1);
        diggingDialog.show();
        mCallback.onArticleSelected(1);
        treawaDialog.tvbtn().setEnabled(true);
        treawaDialog.dismiss();
    }

    @Override
    public void getCheckJoinSuccess(int min, int masx, int rate) {
        if (masx == 0) {
            DiggingDialog diggingDialog = new DiggingDialog(getContext());
            diggingDialog.setEntrust(1);
            diggingDialog.show();
        } else {
            treawaDialog.setEntrust(1, commod_id, activity_id, min, masx, rate);
            treawaDialog.show();
        }
    }

    private boolean isfirst = false;
    //10.19更改
//    @Override
//    protected void initView() {
//        super.initView();
//        isfirst = true;
//    }
//
//
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isfirst) {
//            presenter.getUserList();
//        }
//    }

    private void setVoteList(List<Preheatinginfo> obj) {
        refreshLayout_two.setEnabled(true);
        refreshLayout_two.setRefreshing(false);
        isFlase = true;
        if (obj == null || obj.size() == 0) {
            rlEmpty.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
            refreshLayout_two.setVisibility(View.VISIBLE);
        } else {
            childList.clear();
            groupList.clear();
            for (int j = 0; j < obj.size(); j++) {
                if (obj.get(j).getActivityCommodityInfoList().size() != 0) {
                    groupList.add(obj.get(j));
                }
            }
            for (int i = 0; i < obj.size(); i++) {
                if (obj.get(i).getActivityCommodityInfoList().size() != 0) {
                    datas = obj.get(i).getActivityCommodityInfoList();
                    childList.add(datas);
                    isFlase = false;
                }
            }
            if (isFlase) {
                rlEmpty.setVisibility(View.VISIBLE);
                refreshLayout.setVisibility(View.GONE);
                refreshLayout_two.setVisibility(View.VISIBLE);
            } else {
                refreshLayout.setVisibility(View.VISIBLE);
                refreshLayout_two.setVisibility(View.GONE);
            }
            adapter.notifyDataSetChanged();
            diggingRecycler.setGroupIndicator(null);
            for (int i = 0; i < groupList.size(); i++) {
                diggingRecycler.expandGroup(i);
            }
        }
    }
    private boolean isFlase = true;

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
    public void setPresenter(TreasureContract.DiggingPresenter presenter) {
        this.presenter = presenter;
    }

    OnHeadlineSelectedListener mCallback;

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

    @Override
    public void onDestroy() {
        if (null != timer) {
            timer.cancel();
        }
        super.onDestroy();
    }
}
