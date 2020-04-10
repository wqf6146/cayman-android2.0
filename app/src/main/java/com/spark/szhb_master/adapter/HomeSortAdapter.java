package com.spark.szhb_master.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.main.MainActivity;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.entity.Currency;
import com.spark.szhb_master.utils.MathUtils;

import java.util.List;

/**
 * author: wuzongjie
 * time  : 2018/4/16 0016 15:23
 * desc  : 首页涨幅榜适配器
 */

public class HomeSortAdapter extends BaseQuickAdapter<Currency, BaseViewHolder> {

    private boolean isLoad;
    private int lenth = 0;
    private Context context;

    public boolean isLoad() {
        return isLoad;
    }

    public void setLoad(boolean load) {
        isLoad = load;
    }

    public HomeSortAdapter(@Nullable List<Currency> data,Context context) {
        super(R.layout.item_home_sort, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Currency item) {
//        lenth = lenth + 1;
//        Log.e("TAG",lenth + " 11111111111");
//
//        if (lenth % 2 == 1){
//            helper.setBackgroundColor(R.id.lin_gray, context.getResources().getColor(R.color.grgray));
//        }else {
//            helper.setBackgroundColor(R.id.lin_gray, context.getResources().getColor(R.color.white));
//        }



        if (isLoad) {
            helper.setText(R.id.tvConvert, "¥" + MathUtils.getRundNumber(item.getClose() * item.getBaseUsdRate() * MainActivity.rate, 2, null));
        } else {
            helper.setText(R.id.tvConvert, "$" + MathUtils.getRundNumber(item.getUsdRate(), 2, null));
        }
        helper.setText(R.id.tvSort, "" + (helper.getAdapterPosition() + 1));
        helper.setText(R.id.item_home_chg, (item.getChg() >= 0 ? "+" : "") + MathUtils.getRundNumber(item.getChg() * 100, 2, "########0.") + "%");
//        helper.setTextColor(R.id.tvClose, item.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(),
//                R.color.main_font_green) : ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red));
        helper.getView(R.id.item_home_chg).setEnabled(item.getChg() >= 0);
        helper.setText(R.id.tvBuySymbol, item.getSymbol());
        helper.setText(R.id.tv24HCount, MyApplication.getApp().getString(R.string.text_24_change) + MathUtils.getRundNumber(Double.valueOf(item.getVolume().toString()), 2, null));
        helper.setText(R.id.tvClose, MathUtils.getRundNumber(item.getClose(), 2, null));

    }
}
