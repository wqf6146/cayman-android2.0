package com.spark.szhb_master.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.entity.NewCurrency;
import com.spark.szhb_master.utils.GlobalConstant;

import java.util.List;

/**
 * Created by Administrator on 2018/1/29.
 */

public class HomeOneAdapter extends BaseQuickAdapter<NewCurrency, BaseViewHolder> {
    public HomeOneAdapter(@LayoutRes int layoutResId, @Nullable List<NewCurrency> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewCurrency item) {
        //helper.setImageResource(R.id.ivCollect, item.isCollect() ? R.mipmap.icon_collect_hover : R.mipmap.icon_collect_normal);
        //helper.addOnClickListener(R.id.ivCollect);
        helper.setText(R.id.tvCurrencyName1, item.getSymbol())
                .setText(R.id.tvClose, item.getClose())
                .setText(R.id.tvAddPercent, (Float.parseFloat(item.getScale()) >= 0 ? "+" : "-") + item.getScale() + "%")
                .setText(R.id.tvVol, "≈" + item.getConvert() + GlobalConstant.USD)
                .setTextColor(R.id.tvAddPercent, Float.parseFloat(item.getScale()) >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_green) : ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red))
                .setTextColor(R.id.tvClose, Float.parseFloat(item.getScale()) >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_green) : ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red));
    }

}
