package com.spark.szhb_master.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.entity.Exchange;
import com.spark.szhb_master.utils.MathUtils;

import java.util.List;

/**
 * author: wuzongjie
 * time  : 2018/4/17 0017 17:42
 * desc  : 这个是交易界面盘口的适配器
 */

public class SellAdapter extends BaseQuickAdapter<Exchange, BaseViewHolder> {
    private int type = 0;
    private int price = 1;
    private int amount = 2;
    private myText text;

    public myText getText() {
        return text;
    }

    public void setText(myText text) {
        this.text = text;
    }

    public SellAdapter(@Nullable List<Exchange> data, int type) {
        super(R.layout.item_lv_market_sell_and_buy, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, Exchange item) {
        // 对不同的币种做不同的限制
        if (text != null) {
            price = text.one();
            amount = text.two();
        }
        if ("--".equals(item.getPrice()) || "--".equals(item.getAmount())) {
            helper.setText(R.id.tvPrice, String.valueOf(item.getPrice()));
            helper.setText(R.id.tvCount, String.valueOf(item.getAmount()));
        } else {
            helper.setText(R.id.tvPrice, "" + MathUtils.getRundNumber(Double.valueOf(item.getPrice()), 2, null));
//            helper.setText(R.id.tvPrice, "" + MathUtils.getRundNumber(Double.valueOf(item.getPrice()), price, null));
            helper.setText(R.id.tvCount, "" + MathUtils.getRundNumber(Double.valueOf(item.getAmount()), amount, null));
        }
        if (type == 1) {
            helper.setText(R.id.tvTag, MyApplication.getApp().getString(R.string.item_buy) + (item.getPosition() + 1));
            helper.setTextColor(R.id.tvPrice, ContextCompat.getColor(MyApplication.getApp(),
                    R.color.main_font_green));
            helper.setTextColor(R.id.tvCount, ContextCompat.getColor(MyApplication.getApp(),
                    R.color.main_font_green));
        } else {
            helper.setText(R.id.tvTag, MyApplication.getApp().getString(R.string.item_sell) + (item.getPosition()));
            helper.setTextColor(R.id.tvPrice, ContextCompat.getColor(MyApplication.getApp(),
                    R.color.main_font_red));
            helper.setTextColor(R.id.tvCount, ContextCompat.getColor(MyApplication.getApp(),
                    R.color.main_font_red));
        }
    }

    public interface myText {
        int one();

        int two();
    }
}
