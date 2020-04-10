package com.spark.szhb_master.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.entity.Entrust;
import com.spark.szhb_master.utils.DateUtils;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.MathUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 委托
 * author: wuzongjie
 * time  : 2018/4/17 0017 10:18
 * desc  : 当前委托的适配器
 */

public class TrustAdapter extends BaseQuickAdapter<Entrust, BaseViewHolder> {

    public TrustAdapter(@Nullable List<Entrust> data) {
        super(R.layout.item_trust, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Entrust item) {
        if (GlobalConstant.BUY.equals(item.getDirection())) {
            helper.setText(R.id.trust_type, MyApplication.getApp().getString(R.string.text_buy_in)).setTextColor(R.id.trust_type,
                    ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_green));
        } else {
            helper.setText(R.id.trust_type, MyApplication.getApp().getString(R.string.text_sale_out)).setTextColor(R.id.trust_type,
                    ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red));
        }
        helper.setText(R.id.tv_symbol, item.getSymbol());
        String[] times = DateUtils.getFormatTime(null, new Date(item.getTime())).split(" ");
        helper.setText(R.id.trust_time, times[0].substring(5, times[0].length()) + " " + times[1].substring(0, 5));
        if (GlobalConstant.LIMIT_PRICE.equals(item.getType())) { // 限价
            helper.setText(R.id.trust_price, MathUtils.getRundNumber(item.getPrice(), 2, null));
            // 数量
            helper.setText(R.id.trust_num, String.valueOf(BigDecimal.valueOf(item.getAmount())));
        } else { // 市价
            helper.setText(R.id.trust_price, String.valueOf(MyApplication.getApp().getString(R.string.text_best_prices)));
            // 数量 如果是市价并买入情况就是--
            if (GlobalConstant.BUY.equals(item.getDirection())) {
                helper.setText(R.id.trust_num, String.valueOf("--"));
            } else {
                helper.setText(R.id.trust_num, MathUtils.getRundNumber(Double.valueOf(String.valueOf(BigDecimal.valueOf(item.getAmount()))), 2, null));
            }
        }
        String symbol = item.getSymbol();
        int i = symbol.indexOf("/");
        helper.setText(R.id.trust_one, MyApplication.getApp().getString(R.string.text_number) + "(" + symbol.substring(0, i) + ")");
        helper.setText(R.id.trust_two, MyApplication.getApp().getString(R.string.text_actual_deal) + "(" + symbol.substring(0, i) + ")");
        helper.setText(R.id.trust_ones, MyApplication.getApp().getString(R.string.text_price) + "(" + symbol.substring(i + 1, symbol.length()) + ")");

        helper.setText(R.id.trust_done, MathUtils.getRundNumber(item.getTradedAmount(), 2, null)); // 已成交

        helper.addOnClickListener(R.id.trust_back);
    }
}
