package com.spark.szhb_master.activity.country;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.CountryAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.Country;
import com.spark.szhb_master.utils.NetCodeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import config.Injection;

/**
 * 国家选择
 */
public class CountryActivity extends BaseActivity implements CountryContract.View {

    public static final int RETURN_COUNTRY = 0;

    @BindView(R.id.ivBack)
    ImageButton ibBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivHelp)
    ImageButton ibHelp;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.rvCountry)
    RecyclerView rvCountry;
    private List<Country> countries = new ArrayList<>();
    private CountryAdapter adapter;

    private CountryContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_country;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
        new CountryPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        initRvCountry();
    }

    private void initRvCountry() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCountry.setLayoutManager(manager);
        adapter = new CountryAdapter(R.layout.item_country, countries);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("country", countries.get(position));
                CountryActivity.this.setResult(RESULT_OK, intent);
                finish();
            }
        });
        rvCountry.setAdapter(adapter);
    }

    @Override
    protected void loadData() {
        presenter.country();
    }

    @Override
    public void setPresenter(CountryContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void countrySuccess(List<Country> obj) {
        if (obj == null) return;
        this.countries.clear();
        this.countries.addAll(obj);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void countryFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }
}
