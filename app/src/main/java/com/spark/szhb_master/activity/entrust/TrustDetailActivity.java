package com.spark.szhb_master.activity.entrust;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.EntrustDetailAdapter;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.EntrustHistory;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.MathUtils;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;

/**
 * 交易明细
 */
public class TrustDetailActivity extends BaseActivity {
    @BindView(R.id.mDetailType)
    TextView mDetailType; // 买入还是卖出
    @BindView(R.id.mDetailName)
    TextView mDetailName; // BTC/USDT
    @BindView(R.id.mDetailOne)
    TextView mDetailOne; // 成交总额
    @BindView(R.id.mDetailTwo)
    TextView mDetailTwo; // 成交均价
    @BindView(R.id.mDetailThree)
    TextView mDetailThree; // 成交量
    @BindView(R.id.detailLayout)
    LinearLayout mDetailLayout;
    @BindView(R.id.mDetailFour)
    TextView mDetailFour;
    @BindView(R.id.mDetailFive)
    TextView mDetailFive;
    @BindView(R.id.mDetailTotal)
    TextView mDetailTotal;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private EntrustHistory entrustHistory;
    private String symbol;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_trust_detail;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.trust_detail));
        tvGoto.setVisibility(View.INVISIBLE);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            symbol = bundle.getString("symbol");
//            setTitle(symbol);
            mDetailName.setText(symbol);
            entrustHistory = (EntrustHistory) bundle.getSerializable("order");
            if (entrustHistory != null) {
                if (GlobalConstant.BUY.equals(entrustHistory.getDirection())) {
                    mDetailType.setText(MyApplication.getApp().getString(R.string.text_buy_in));
                    mDetailType.setTextColor(ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_green));
                } else {
                    mDetailType.setText(MyApplication.getApp().getString(R.string.text_sale_out));
                    mDetailType.setTextColor(ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red));
                }
                mDetailTotal.setText(MyApplication.getApp().getString(R.string.text_total_deal) + "(" + entrustHistory.getBaseSymbol() + ")");
                mDetailFour.setText(MyApplication.getApp().getString(R.string.text_average_price) + "(" + entrustHistory.getBaseSymbol() + ")");
                mDetailFive.setText(MyApplication.getApp().getString(R.string.text_volume) + "(" + entrustHistory.getCoinSymbol() + ")");
                mDetailOne.setText(String.valueOf(entrustHistory.getTurnover()));
                if (entrustHistory.getTradedAmount() == 0.0 || entrustHistory.getTurnover() == 0.0) {
                    mDetailTwo.setText(String.valueOf(0.0));
                } else {
                    mDetailTwo.setText(MathUtils.getRundNumber(Double.parseDouble(String.valueOf(entrustHistory.getTurnover() / entrustHistory.getTradedAmount())), 2, null));
                }
                mDetailThree.setText(new BigDecimal(String.valueOf(entrustHistory.getTradedAmount())).toString());
                List<EntrustHistory.DetailBean> detail = entrustHistory.getDetail();
                if (detail != null && detail.size() > 0) {
                    initRv();
                } else {
                    mDetailLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    private void initRv() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mRecyclerView.setAdapter(new EntrustDetailAdapter(entrustHistory.getDetail()));
    }
}
