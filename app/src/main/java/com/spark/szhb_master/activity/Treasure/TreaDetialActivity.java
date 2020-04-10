package com.spark.szhb_master.activity.Treasure;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.BannerImageLoader;
import com.spark.szhb_master.adapter.TreaDetailAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.dialog.CallDialog;
import com.spark.szhb_master.dialog.LoginDialog;
import com.spark.szhb_master.dialog.PreheaDialog;
import com.spark.szhb_master.dialog.ShiMingDialog;
import com.spark.szhb_master.dialog.ShowDialog;
import com.spark.szhb_master.dialog.TimeDialog;
import com.spark.szhb_master.dialog.TreawaDialog;
import com.spark.szhb_master.dialog.UpPowerDialog;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.entity.TreaDetail;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.DateUtils;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.SharedPreferenceInstance;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.utils.okhttp.StringCallback;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;
import okhttp3.Request;

import static com.spark.szhb_master.utils.okhttp.OkhttpUtils.post;

/**
 * Created by Administrator on 2018/9/17 0017.
 */

public class TreaDetialActivity extends BaseActivity implements TreaDetailContract.View {

    private int commodityId, voteId;
    @BindView(R.id.trea_detail_recy)
    RecyclerView trea_detail_recy;
    @BindView(R.id.comm_name)
    TextView comm_name;
    @BindView(R.id.text_btn)
    TextView text_btn;
    @BindView(R.id.tvLimit)
    TextView tvLimit;
    @BindView(R.id.tvNum)
    TextView tvNum;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.line_gong)
    LinearLayout line_gong;@BindView(R.id.line_gone_two)
    LinearLayout line_gone_two;@BindView(R.id.line_vis)
    LinearLayout line_vis;


    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private TreaDetailContract.Presenter presenter;
    private TreaDetailAdapter adapter;
    private List<TreaDetail> treaDetail = new ArrayList<>();
    private String imgs;
    private List<String> imageUrls = new ArrayList<>();
    private TreawaDialog treawaDialog;
    private int degree, form, joinAcitvity;
    private int joinAmount;
    private String startTime, endTime;
    private int joinVote;

    private int pageNo = 0;
    private int pageSize = 10;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            immersionBar.setTitleBar(TreaDetialActivity.this, llTitle);
            isSetTitle = true;
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.trea_detail_layout;
    }

    @Override
    protected void initData() {
        super.initData();
        tvGoto.setVisibility(View.INVISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setBackground(null);
        tvTitle.setText(getResources().getString(R.string.details));
        llTitle.setBackgroundDrawable(getResources().getDrawable(R.mipmap.trea_back_head));
        new TreaDetailPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            commodityId = bundle.getInt("commodityId");
            voteId = bundle.getInt("voteId");
            joinVote = bundle.getInt("joinVote");//状态
            String name = bundle.getString("name");
            degree = bundle.getInt("degree");
            joinAmount = bundle.getInt("joinAmount");
            form = bundle.getInt("form");
            imgs = bundle.getString("imgs");
            joinAcitvity = bundle.getInt("joinAcitvity");
            startTime = bundle.getString("startTime");
            endTime = bundle.getString("endTime");
            Log.e("TAG", commodityId + "");
            comm_name.setText(name);

            String html = getResources().getString(R.string.heat)+"：" + "<font color=#f84f5c>" + (degree * 10) + "";
            tvLimit.setText(Html.fromHtml(html));
//            tvLimit.setText("热度值：" + degree);
            tvNum.setText(getResources().getString(R.string.number_participants)+"：" + joinAmount);
            if (joinVote == 0) {
                text_btn.setBackground(null);
                text_btn.setTextColor(getResources().getColor(R.color.uneable_text));
                text_btn.setBackgroundColor(getResources().getColor(R.color.uneable));
            }
            //|| detial == 9
            if (form == 2) {//来自打call
                text_btn.setBackground(null);
                text_btn.setTextColor(getResources().getColor(R.color.uneable_text));
                text_btn.setBackgroundColor(getResources().getColor(R.color.uneable));
                if (joinAcitvity == 1) {//已入选
                    text_btn.setText(getResources().getString(R.string.selected));
                } else {
                    text_btn.setText(getResources().getString(R.string.over_one));
                }
            }
        }
        getPic();
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        trea_detail_recy.setLayoutManager(manager);
        adapter = new TreaDetailAdapter(this, R.layout.item_trea_detail, treaDetail);
        adapter.bindToRecyclerView(trea_detail_recy);
        adapter.isFirstOnly(true);
        adapter.setEnableLoadMore(false);
        trea_detail_recy.setAdapter(adapter);
        treawaDialog = new TreawaDialog(this);
        getGone();
    }

    private void getGone() {
        int type = (joinVote == 1 ? 1 : 2);
        if (type == 2) {
            line_gong.setVisibility(View.GONE);
            text_btn.setVisibility(View.GONE);
            line_gone_two.setVisibility(View.GONE);
            line_vis.setVisibility(View.VISIBLE);
        }else {
            line_gong.setVisibility(View.VISIBLE);
            text_btn.setVisibility(View.VISIBLE);
            line_gone_two.setVisibility(View.VISIBLE);
            line_vis.setVisibility(View.GONE);
        }
    }

    private void getPic() {
        String[] strarray = imgs.split("[;]");
        for (int i = 0; i < strarray.length; i++) {
            imageUrls.add(strarray[i]);
            System.out.println(strarray[i]);
        }
        if (imageUrls.size() > 0) {
            banner.setImages(imageUrls); // 设置图片集合
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR).setIndicatorGravity(BannerConfig.CENTER)// 设置样式
                    .setImageLoader(new BannerImageLoader());
            banner.setDelayTime(3000); // 设置轮播时间
            banner.start();
        }
    }


    @Override
    protected void loadData() {
        super.loadData();
        int type = (joinVote == 1 ? 1 : 2);
        if (type == 1) {
            getDetialList();
        }
    }

    private void getDetialList() {
        HashMap<String, String> map = new HashMap<>();
        map.put("commodityId", commodityId + "");
        map.put("pageNo", pageNo + "");
        map.put("pageSize", pageSize + "");
        presenter.getVoteDetail(map);
    }


    @Override
    protected void initView() {
        super.initView();
    }

    private int voteAmount;

    @Override
    protected void setListener() {
        super.setListener();
        treawaDialog.tvbtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                treawaDialog.tvbtn().setEnabled(false);
                HashMap<String, String> map = new HashMap<>();
                map.put("commodityId", commodityId + "");
                map.put("voteId", voteId + "");
                voteAmount = treawaDialog.seekBar().getProgress();
                map.put("voteAmount", voteAmount + "");
                presenter.getVote(map);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 0;
                getDetialList();
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNo = pageNo + 1;
                getDetialList();
            }
        });
    }

    @OnClick({R.id.ivBack, R.id.text_btn})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.text_btn:
                if (!MyApplication.getApp().isLogin()){
                    LoginDialog loginDialog = new LoginDialog(this);
                    loginDialog.show();
                    return;
                }else {
                    presenter.safeSetting();
                }
                break;
        }
    }

    @Override
    public void getVoteDetailSuccess(List<TreaDetail> obj) {
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        if (obj != null && obj.size() > 0) {
            if (pageNo == 0) {
                this.treaDetail.clear();
            } else {
                adapter.loadMoreEnd();
            }
            this.treaDetail.addAll(obj);
            adapter.notifyDataSetChanged();
        } else {
            if (pageNo == 0) {
                this.treaDetail.clear();
                adapter.setEmptyView(R.layout.empty_no_treaing);
                adapter.notifyDataSetChanged();
            }
        }
        adapter.disableLoadMoreIfNotFullPage();

    }

    @Override
    public void getCheckVoteSuccess(int min, int masx) {
        if (masx == 0) {
            PreheaDialog preheaDialog = new PreheaDialog(this);
            preheaDialog.show();
        } else {
            treawaDialog.setEntrust(2, commodityId, voteId, min, masx, 0);
            treawaDialog.show();
        }
    }

    private CallDialog callDialog;

    @Override
    public void getVoteSuccess(int obj) {
        getDetialList();
        callDialog = new CallDialog(TreaDetialActivity.this);
        callDialog.setEntrust(voteAmount, obj);
        callDialog.show();
        treawaDialog.tvbtn().setEnabled(true);
        treawaDialog.dismiss();
        getAdd();
    }

    @Override
    public void geCheckVoteFailed(Integer code, String toastMessage) {
        if (code == 500 && toastMessage.equals("打Call额度已用完!")) {
            PreheaDialog preheaDialog = new PreheaDialog(this);
            preheaDialog.show();
        }
    }

    private int num = 0;
    private int count = 0;

    private void getAdd() {
        num = num + 1;
        count = count + voteAmount;
        tvNum.setText(getResources().getString(R.string.number_participants)+"：" + (joinAmount + num));
        String html = getResources().getString(R.string.heat)+"：" + "<font color=#f84f5c>" + ((degree + count) * 10) + "";
        tvLimit.setText(Html.fromHtml(html));
    }

    @Override
    public void doFiled(Integer code, String toastMessage) {
        adapter.setEnableLoadMore(true);
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
        treawaDialog.tvbtn().setEnabled(true);
        treawaDialog.dismiss();
    }

    @Override
    public void safeSettingSuccess(SafeSetting obj) {
        if (obj == null)
            return;
        safeSetting = obj;
        if (safeSetting.getRealVerified() == 0) {
            ShiMingDialog shiMingDialog = new ShiMingDialog(this);
            String name = safeSetting.getRealNameRejectReason();
            if (safeSetting.getRealVerified() == 0) {
                if (safeSetting.getRealAuditing() == 1) {
                    shiMingDialog.setEntrust(7, name,0);
                } else {
                    if (safeSetting.getRealNameRejectReason() != null)
                        shiMingDialog.setEntrust(8, name,0);
                    else
                        shiMingDialog.setEntrust(9, name,0);
                }
            } else {
                shiMingDialog.setEntrust(6, name,0);
            }
            shiMingDialog.show();
        } else {
            if (form == 2) {//来自打call
                if (joinAcitvity == 1) {//已入选
                    ShowDialog showDialog = new ShowDialog(this);
                    showDialog.setEntrust(2);
                    showDialog.show();
                } else {
                    ShowDialog showDialog = new ShowDialog(this);
                    showDialog.setEntrust(1);
                    showDialog.show();
                }
                return;
            }
            long time_start = DateUtils.getTimeMillis(null, startTime);
            long now_time = DateUtils.getSimpleDateFormat();
            long time_end = DateUtils.getTimeMillis(null, endTime);

            if (now_time > time_start) {
            } else if (now_time < time_start) {
                time_end = time_start;
            }
            long typeTime = time_end - now_time;
            int type_two = (now_time > time_start ? 1 : 2);
            int type = (joinVote == 1 ? 1 : 2);
            TimeDialog timeDialog = new TimeDialog(this);
            int gcx = SharedPreferenceInstance.getInstance().getGCX();
            if (type == 2) {   //type  1无需打call
                String str = DateUtils.getSFM(typeTime);
                timeDialog.setEntrust(str, 1);
                timeDialog.show();
            } else if (type_two == 2) { //2 未开始
                String str = DateUtils.getSFM(typeTime);
                timeDialog.setEntrust(str, 2);
                timeDialog.show();
            } else if (gcx == 0) {
                UpPowerDialog upPowerDialog = new UpPowerDialog(this);
                upPowerDialog.setEntrust(2);
                upPowerDialog.show();
            } else {
                HashMap<String, String> map = new HashMap<>();
                map.put("commodityId", commodityId + "");
                presenter.getCheckVote(map);//检测投票GCX
            }
        }
    }

    @Override
    public void setPresenter(TreaDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }


    private SafeSetting safeSetting;

}
