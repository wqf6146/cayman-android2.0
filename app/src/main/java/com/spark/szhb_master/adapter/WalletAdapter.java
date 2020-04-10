package com.spark.szhb_master.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.Coin;
import com.spark.szhb_master.utils.MathUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2018/2/5.
 */

public class WalletAdapter extends BaseQuickAdapter<Coin, BaseViewHolder> {
    public WalletAdapter(@LayoutRes int layoutResId, @Nullable List<Coin> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Coin item) {
        helper.setText(R.id.tvCoinUnit, item.getCoin().getUnit())
                .setText(R.id.tvBuyCanUse, MathUtils.getRundNumber(Double.valueOf(new BigDecimal(item.getBalance()).toString()),8,null))
                .setText(R.id.tvFrozon, MathUtils.getRundNumber(Double.valueOf(new BigDecimal(item.getFrozenBalance()).toString()),8,null))
                .addOnClickListener(R.id.tvRecharge).addOnClickListener(R.id.tvExtract);

    }
}
