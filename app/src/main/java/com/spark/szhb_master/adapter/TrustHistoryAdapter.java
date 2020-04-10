package com.spark.szhb_master.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.entity.EntrustHistory;
import com.spark.szhb_master.utils.DateUtils;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.MathUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * author: wuzongjie
 * time  : 2018/4/17 0017 10:56
 * desc  :历史记录的适配器
 */

public class TrustHistoryAdapter extends BaseQuickAdapter<EntrustHistory, BaseViewHolder> {

    public TrustHistoryAdapter(@Nullable List<EntrustHistory> data) {
        super(R.layout.item_history_trust, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntrustHistory item) {
        if (GlobalConstant.BUY.equals(item.getDirection())) {
            helper.setText(R.id.tvType, MyApplication.getApp().getString(R.string.text_buy_in)).setTextColor(R.id.tvType,
                    ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_green));
        } else {
            helper.setText(R.id.tvType, MyApplication.getApp().getString(R.string.text_sale_out)).setTextColor(R.id.tvType,
                    ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red));
        }
        helper.setText(R.id.tv_symbol,item.getSymbol());
        if ("COMPLETED".equals(item.getStatus())) { // 已成交
            helper.setVisible(R.id.tvCancel, false);
            helper.setVisible(R.id.tvSuccess, true);
        } else {
            helper.setVisible(R.id.tvCancel, true);
            helper.setVisible(R.id.tvSuccess, false);
        }

        helper.setText(R.id.tvVolumeTag, MyApplication.getApp().getString(R.string.text_volume) + "(" + item.getCoinSymbol() + ")");
        String[] times = DateUtils.getFormatTime(null, new Date(item.getTime())).split(" ");
        helper.setText(R.id.tvEntrustTime, times[0].substring(5, times[0].length()) + " " + times[1].substring(0, 5));
        if (GlobalConstant.LIMIT_PRICE.equals(item.getType())) { // 限价
            helper.setText(R.id.tvEntrustCountTag, MyApplication.getApp().getString(R.string.text_entrust_num) + "(" + item.getCoinSymbol() + ")");
            helper.setText(R.id.tvEntrustPrice, String.valueOf(item.getPrice()));
        } else { // 市价
            if (GlobalConstant.BUY.equals(item.getDirection())) {
                helper.setText(R.id.tvEntrustPrice, String.valueOf(MyApplication.getApp().getString(R.string.text_best_prices)));
                helper.setText(R.id.tvEntrustCountTag, MyApplication.getApp().getString(R.string.text_total_entrust) + "(" + item.getBaseSymbol() + ")");
            } else {
                helper.setText(R.id.tvEntrustCountTag, MyApplication.getApp().getString(R.string.text_entrust_num) + "(" + item.getCoinSymbol() + ")");
                helper.setText(R.id.tvEntrustPrice, String.valueOf(MyApplication.getApp().getString(R.string.text_best_prices)));
            }
        }
        helper.setText(R.id.tvEntrustPriceTag, MyApplication.getApp().getString(R.string.text_entrust_price) + "(" + item.getBaseSymbol() + ")");
        // 委托量
        helper.setText(R.id.tvEntrustCount, String.valueOf(item.getAmount()));
        // 成交总额
        helper.setText(R.id.tvTotal, String.valueOf(item.getTurnover()));
        helper.setText(R.id.tvTotalTag, MyApplication.getApp().getString(R.string.text_total_deal) + "(" + item.getBaseSymbol() + ")");
        // 成交均价
        if (item.getTradedAmount() == 0.0 || item.getTurnover() == 0.0) {
            helper.setText(R.id.tvAverage, String.valueOf(0.0));
        } else {
            helper.setText(R.id.tvAverage, String.valueOf(MathUtils.getRundNumber(item.getTurnover() / item.getTradedAmount(),
                    2, null)));
        }
        helper.setText(R.id.tvAverageTag, MyApplication.getApp().getString(R.string.text_average_price) + "(" + item.getBaseSymbol() + ")");
        // 成交量
        //helper.setText(R.id.history_six,String.valueOf(item.getTradedAmount()));
        helper.setText(R.id.tvVolume, String.valueOf(BigDecimal.valueOf(item.getTradedAmount())));
    }
}