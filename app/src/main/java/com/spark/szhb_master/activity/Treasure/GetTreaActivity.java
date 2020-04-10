package com.spark.szhb_master.activity.Treasure;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.GetTreaAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.Treaget;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.okhttp.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;

import static com.spark.szhb_master.utils.okhttp.OkhttpUtils.post;

/**
 * Created by Administrator on 2018/9/19 0019.
 */
public class GetTreaActivity extends BaseActivity{

    @BindView(R.id.hisRecycler)
    RecyclerView hisRecycler; @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private int pageNo = 0;
    private int pageSize = 10;

    private GetTreaAdapter getTreaAdapter;
    private List<Treaget> treagets = new ArrayList<>();

    @Override
    protected int getActivityLayoutId() {
        return R.layout.get_trea_layout;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            immersionBar.setTitleBar(GetTreaActivity.this, llTitle);
            isSetTitle = true;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        tvGoto.setVisibility(View.INVISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setBackground(null);
        tvTitle.setText(getResources().getString(R.string.all_announcements));
        llTitle.setBackgroundDrawable(null);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        hisRecycler.setLayoutManager(manager);
        getTreaAdapter = new GetTreaAdapter(this,R.layout.get_trea_detail, treagets);
        getTreaAdapter.bindToRecyclerView(hisRecycler);
        getTreaAdapter.isFirstOnly(true);
        getTreaAdapter.setEnableLoadMore(false);
        hisRecycler.setAdapter(getTreaAdapter);


    }

    @OnClick({R.id.ivBack})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
        }
    }


    @Override
    protected void loadData() {
        super.loadData();
        getTrea();
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 0;
                getTrea();
            }
        });

        getTreaAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNo = pageNo + 1;
                getTrea();
            }
        });
    }

    private void getTrea() {
//        if (!StringUtils.isEmpty(getToken())) {
            post().url(UrlFactory.getRewardPage()).addParams("pageNo", pageNo + "").addParams("pageSize", pageSize + "")
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Request request, Exception e) {
                    getTreaAdapter.setEnableLoadMore(true);
                    refreshLayout.setEnabled(true);
                    refreshLayout.setRefreshing(false);
                }

                @Override
                public boolean onResponse(String response) {
                    JSONObject object = null;
                    try {

                        getTreaAdapter.setEnableLoadMore(true);
                        getTreaAdapter.loadMoreComplete();
                        refreshLayout.setEnabled(true);
                        refreshLayout.setRefreshing(false);

                        object = new JSONObject(response);
                        List<Treaget> treage = new Gson().fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<Treaget>>() {
                        }.getType());

                        if (treage != null && treage.size() > 0) {
                            if (pageNo == 0) {
                                treagets.clear();
                            } else {
                                getTreaAdapter.loadMoreEnd();
                            }
                            treagets.addAll(treage);
                            getTreaAdapter.notifyDataSetChanged();
                        } else {
                            if (pageNo == 0) {
                                treagets.clear();
                                getTreaAdapter.setEmptyView(R.layout.empty_no_treaing);
                                getTreaAdapter.notifyDataSetChanged();
                            }
                        }
                        getTreaAdapter.disableLoadMoreIfNotFullPage();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return false;
                }
            });
//        }
    }


}
