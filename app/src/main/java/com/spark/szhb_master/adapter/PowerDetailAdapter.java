package com.spark.szhb_master.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.PowerDetail;

import java.util.List;

/**
 * Created by Administrator on 2018/9/20 0020.
 */
public class PowerDetailAdapter extends  BaseQuickAdapter<PowerDetail, BaseViewHolder> {


    private Context context;

    public PowerDetailAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<PowerDetail> data) {
        super(layoutResId, data);
        this.context = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, PowerDetail item) {
        if (item.getType() == 0){
            helper.setText(R.id.tvCommainname, context.getResources().getString(R.string.coin_currency_trade));
        }else if (item.getType() == 1){
            helper.setText(R.id.tvCommainname, context.getResources().getString(R.string.recommend));
        }else if (item.getType() == 2){
            helper.setText(R.id.tvCommainname, context.getResources().getString(R.string.call));
        }else if (item.getType() == 4){
            helper.setText(R.id.tvCommainname, context.getResources().getString(R.string.treasure));
        }else if (item.getType() == 5){
            helper.setText(R.id.tvCommainname, context.getResources().getString(R.string.reward));
        }else if (item.getType() == 3){
            helper.setText(R.id.tvCommainname, context.getResources().getString(R.string.collect));
        }

        if (item.getAmount() > 0){
            helper.setTextColor(R.id.tv_num, context.getResources().getColor(R.color.blue_trea));
            helper.setText(R.id.tv_num, "+" + item.getAmount() + context.getResources().getString(R.string.power));
        }else {
            helper.setTextColor(R.id.tv_num, context.getResources().getColor(R.color.chart_red));
            helper.setText(R.id.tv_num, item.getAmount() + context.getResources().getString(R.string.power));
        }

        helper.setText(R.id.tv_name, item.getCreateTime());
    }
}




