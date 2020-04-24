package com.spark.szhb_master.activity.Treasure;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.BannerImageLoader;
import com.spark.szhb_master.adapter.DiggingDetailAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.dialog.DiggingDialog;
import com.spark.szhb_master.dialog.LoginDialog;
import com.spark.szhb_master.dialog.ReceiveDialog;
import com.spark.szhb_master.dialog.ShiMingDialog;
import com.spark.szhb_master.dialog.TimeDialog;
import com.spark.szhb_master.dialog.TreawaDialog;
import com.spark.szhb_master.dialog.UpPowerDialog;
import com.spark.szhb_master.entity.DiggingDetail;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.DateUtils;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.SharedPreferenceInstance;
import com.spark.szhb_master.utils.StringUtils;
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
 * Created by Administrator on 2018/9/19 0019.
 */

public class DiggingDetialActivity extends BaseActivity implements DiggingDetailContract.View {
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
    @BindView(R.id.text_luck)
    TextView text_luck;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.line_peo)
    LinearLayout line_peo;
    private DiggingDetailContract.Presenter presenter;
    private DiggingDetailAdapter adapter;
    private List<DiggingDetail> treaDetail = new ArrayList<>();
    private String imgs;
    private List<String> imageUrls = new ArrayList<>();
    private TreawaDialog treawaDialog;
    private int degree, completeAmount, form;
    private int activityStatus;
    private String luckUser;
    private int joinAmount;
    private int luckUserID;
    private String cover;
    private String startTime;
    private int pageNo = 0;
    private int pageSize = 10;


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            immersionBar.setTitleBar(DiggingDetialActivity.this, llTitle);
            isSetTitle = true;
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.digging_detail_layout;
    }

    private String name;

    @Override
    protected void initData() {
        super.initData();
        tvGoto.setVisibility(View.INVISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setBackground(null);
        tvTitle.setText(getResources().getString(R.string.details));
        llTitle.setBackgroundDrawable(getResources().getDrawable(R.mipmap.trea_back_head));
        new DiggingDetailPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            commodityId = bundle.getInt("commodityId");
            voteId = bundle.getInt("voteId");
//            activityStatus = bundle.getInt("joinVote");
            name = bundle.getString("name");
            degree = bundle.getInt("degree");
            joinAmount = bundle.getInt("joinAmount");
            imgs = bundle.getString("imgs");
            completeAmount = bundle.getInt("completeAmount");
            form = bundle.getInt("form");
            activityStatus = bundle.getInt("acitvityStatus");
            luckUser = bundle.getString("luckUser");
            luckUserID = bundle.getInt("luckUserID");
            cover = bundle.getString("cover");
            startTime = bundle.getString("startTime");
            comm_name.setText(name);

            tvNum.setText(getResources().getString(R.string.number_participants) + "：" + joinAmount);
            if (activityStatus == 2 || activityStatus == 3) {//1.正在挖宝，2.挖宝结束，3等待公布
                text_btn.setEnabled(false);
                text_btn.setBackground(null);
                text_btn.setTextColor(getResources().getColor(R.color.uneable_text));
                text_btn.setBackgroundColor(getResources().getColor(R.color.uneable));
            }
        }

        if (form == 1 || form == 3) {
            degree = completeAmount;//从历史过来
            text_btn.setEnabled(false);
            text_btn.setBackground(null);
            text_btn.setTextColor(getResources().getColor(R.color.uneable_text));
            text_btn.setBackgroundColor(getResources().getColor(R.color.uneable));
            if (luckUserID == MyApplication.getApp().getCurrentUser().getId()) {
                text_btn.setEnabled(true);
                text_btn.setText(getResources().getString(R.string.sun_drying));
            } else {
                if (!StringUtils.isEmpty(luckUser)) {//挖宝结束
                    text_luck.setText(getResources().getString(R.string.congratulations) + luckUser + getResources().getString(R.string.success_digging));
                    line_peo.setVisibility(View.VISIBLE);
                    text_btn.setText(getResources().getString(R.string.over));
                }
            }
        } else {
            if (activityStatus == 4) {//等待领取
                text_btn.setText(getResources().getString(R.string.waiting_collection));
            } else {
                if (degree == completeAmount) {
                    text_btn.setEnabled(false);
                    text_btn.setBackground(null);
                    text_btn.setTextColor(getResources().getColor(R.color.uneable_text));
                    text_btn.setBackgroundColor(getResources().getColor(R.color.uneable));
                    text_btn.setText(getResources().getString(R.string.waiting_results));
                }
            }
        }

        String html = getResources().getString(R.string.completion) + "：" + "<font color=#ff6e00>" + completeAmount + "</font>" + "/" + degree;
        tvLimit.setText(Html.fromHtml(html));
//        tvLimit.setText("完成度：" + completeAmount + "/" + degree);
        getPic();
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        trea_detail_recy.setLayoutManager(manager);
        adapter = new DiggingDetailAdapter(this, R.layout.item_trea_detail, treaDetail);
        adapter.bindToRecyclerView(trea_detail_recy);
        adapter.isFirstOnly(true);
        adapter.setEnableLoadMore(false);
        trea_detail_recy.setAdapter(adapter);

        treawaDialog = new TreawaDialog(this);
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
        getDetialList();
    }

    private void getDetialList() {
        HashMap<String, String> map = new HashMap<>();
        map.put("commodityId", commodityId + "");
        map.put("pageNo", pageNo + "");
        map.put("pageSize", pageSize + "");
        presenter.getJoinDetail(map);
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
                voteAmount = treawaDialog.seekBar().getProgress();
                map.put("power", voteAmount + "");
                map.put("activityId", voteId + "");
                presenter.getJoin(map);
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
                if (!MyApplication.getApp().isLogin()) {
                    LoginDialog loginDialog = new LoginDialog(this);
                    loginDialog.show();
                } else {
                    presenter.safeSetting();
                }
                break;
        }
    }


    @Override
    public void getJoinDetailSuccess(List<DiggingDetail> obj) {

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
    public void getCheckJoinSuccess(int min, int masx, int rate) {
        if (masx == 0) {
            DiggingDialog diggingDialog = new DiggingDialog(this);
            diggingDialog.setEntrust(1);
            diggingDialog.show();
        } else {
            treawaDialog.setEntrust(1, commodityId, voteId, min, masx, rate);
            treawaDialog.show();
        }
    }

    @Override
    public void getJoinSuccess(Object obj) {
        getDetialList();
        DiggingDialog diggingDialog = new DiggingDialog(DiggingDetialActivity.this);
        diggingDialog.setEntrust(1);
        diggingDialog.show();
        treawaDialog.tvbtn().setEnabled(true);
        treawaDialog.dismiss();
        getAdd();
    }

    private int num = 0;
    private int count = 0;

    private void getAdd() {
        num = num + 1;
        count = count + voteAmount;
        int peoplenum = joinAmount + num;
        tvNum.setText(getResources().getString(R.string.number_participants) + "：" + peoplenum);
        String html = getResources().getString(R.string.completion) + "：" + "<font color=#ff6e00>" + (completeAmount + count) + "</font>" + "/" + degree;
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

    private SafeSetting safeSetting;

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
                    shiMingDialog.setEntrust(7, name, 0);
                } else {
                    if (safeSetting.getRealNameRejectReason() != null)
                        shiMingDialog.setEntrust(8, name, 0);
                    else
                        shiMingDialog.setEntrust(9, name, 0);
                }
            } else {
                shiMingDialog.setEntrust(6, name, 0);
            }
            shiMingDialog.show();
        } else {
            if (form == 1 || form == 3) {
                if (luckUserID == MyApplication.getApp().getCurrentUser().getId()) {
                    startActivity(new Intent(this, ShowTreaActivity.class).putExtra("img", cover));
                }
            } else {
                int suan = SharedPreferenceInstance.getInstance().getSUAN();
                int gcx = SharedPreferenceInstance.getInstance().getGCX();
                UpPowerDialog upPowerDialog = new UpPowerDialog(this);
                TimeDialog timeDialog = new TimeDialog(this);

                long time_start = 0;
                if (!StringUtils.isEmpty(startTime)) {
                    time_start = DateUtils.getTimeMillis(null, startTime);
                }
                long now_time = DateUtils.getSimpleDateFormat();
                long typeTime = time_start - now_time;
                int type_two = (now_time > time_start ? 1 : 2);
                if (activityStatus == 4) {
                    final ReceiveDialog receiveDialog = new ReceiveDialog(this);
                    receiveDialog.setEntrust(name);
                    receiveDialog.show();
                    receiveDialog.tvbtn().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String url = "http://galaxychain.mikecrm.com/Uubnadu";
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
//                                presenter.safeSetting();
//                                receiveDialog.dismiss();
                        }
                    });
                } else if (type_two == 2) { //2 未开始
                    String str = DateUtils.getSFM(typeTime);
                    timeDialog.setEntrust(str, 3);
                    timeDialog.show();
                } else if (suan == 0) {
                    upPowerDialog.setEntrust(1);
                    upPowerDialog.show();
                } else if (gcx == 0) {
                    upPowerDialog.setEntrust(2);
                    upPowerDialog.show();
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("commodityId", commodityId + "");
                    presenter.getCheckJoin(map);
                }
            }
        }
    }


    @Override
    public void setPresenter(DiggingDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void http() {
        post().url(UrlFactory.getSafeSettingUrl()).addHeader("x-auth-token", MyApplication.getApp().getCurrentUser().getToken()).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public boolean onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        safeSetting = new Gson().fromJson(object.getJSONObject("data").toString(), SafeSetting.class);
                    } else {
                        ToastUtils.show(object.optString("message"), Toast.LENGTH_SHORT);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

    }
}