package com.spark.szhb_master.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.NewCurrency;

import java.util.List;

/**
 * 行情adapter
 * author: wuzongjie
 * time  : 2018/4/16 0016 18:18
 */

public class HeyueAdapter extends BaseQuickAdapter<NewCurrency, BaseViewHolder> {
    private int type;
    private boolean isLoad;

    public boolean isLoad() {
        return isLoad;
    }

    public void setLoad(boolean load) {
        isLoad = load;
    }

    public HeyueAdapter(@Nullable List<NewCurrency> data, int type) {
        super(R.layout.item_market, data);
        this.type = type;
    }


    @Override
    protected void convert(BaseViewHolder helper, NewCurrency item) {
//        if (isLoad) {
//            helper.setText(R.id.tvConvert, "¥" + String.valueOf(MathUtils.getRundNumber(
//                    item.getBaseUsdRate() * item.getClose() * MainActivity.rate, 2, null)));
//        } else {
//            helper.setText(R.id.tvConvert, "$" + String.valueOf(MathUtils.getRundNumber(
//                    item.getBaseUsdRate() * item.getClose(), 2, null)));
//        }
//        helper.setText(R.id.item_home_chg, (item.getChg() >= 0 ? "+" : "") + MathUtils.getRundNumber(item.getChg() * 100, 2, "########0.") + "%");
//        helper.getView(R.id.item_home_chg).setEnabled(item.getChg() >= 0);
//        if (type == 2) {
//            helper.setText(R.id.tvBuySymbol, item.getSymbol().split("/")[0]);
//            helper.setText(R.id.tvSecSymbol, "/" + item.getSymbol().split("/")[1]);
//        } else {
//            helper.setText(R.id.tvBuySymbol, item.getOtherCoin());
//            helper.setText(R.id.tvSecSymbol, item.getSymbol().substring(item.getSymbol().indexOf("/"), item.getSymbol().length()));
//        }
//        helper.setText(R.id.tvClose, String.valueOf(MathUtils.getRundNumber(item.getClose(),2,null)));
////        helper.setTextColor(R.id.tvClose, item.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(),
////                R.color.main_font_green) : ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red));
//        helper.setText(R.id.tv24HCount, MyApplication.getApp().getString(R.string.text_24_change) + item.getVolume());


        boolean tol = Float.parseFloat(item.getScale()) > 0 ? true : false;

        helper.setText(R.id.tvBuySymbol, item.getSymbol());
        helper.setText(R.id.tvSecSymbol, "/" + item.getType());
        helper.setText(R.id.tvPrice, item.getClose());
        if (tol){
            helper.setText(R.id.item_home_chg, "+"+ item.getScale() + "%");
            helper.getView(R.id.item_home_chg).setEnabled(true);
        }else{
            helper.setText(R.id.item_home_chg, item.getScale() + "%");
            helper.getView(R.id.item_home_chg).setEnabled(false);
        }
    }
}