package com.spark.szhb_master.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.entity.WalletDetail;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/2/5.
 */

public class WalletDetailAdapter extends BaseQuickAdapter<WalletDetail, BaseViewHolder> {
    private Context context;

    public WalletDetailAdapter(@LayoutRes int layoutResId, @Nullable List<WalletDetail> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletDetail item) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(item.getCreateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String data = String.valueOf(android.text.format.DateFormat.format("HH:mm MM/dd", date));
        helper.setText(R.id.tvTime, data)
                .setText(R.id.tvCount, new BigDecimal(String.valueOf(item.getAmount())).toString()).setTextColor(R.id.tvCount, item.getAmount() > 0 ? context.getResources().getColor(R.color.main_font_green) : context.getResources().getColor(R.color.main_font_red))
                .setText(R.id.tvUnit, item.getSymbol())
                .setText(R.id.tvCharge, new BigDecimal(String.valueOf(item.getFee())).toString());
        TextView tvTypeText = helper.getView(R.id.tvType);
        String text = "";
        switch (item.getType()) {
            case 0:
                text = MyApplication.getApp().getString(R.string.top_up);
                break;
            case 1:
                text = MyApplication.getApp().getString(R.string.withdrawal);
                break;
            case 2:
                text = MyApplication.getApp().getString(R.string.transfer);
                break;
            case 3:
                text = MyApplication.getApp().getString(R.string.coin_currency_trade);
                break;
            case 4:
                text = MyApplication.getApp().getString(R.string.fiat_money_buy);
                break;
            case 5:
                text = MyApplication.getApp().getString(R.string.fiat_money_sell);
                break;
            case 6:
                text = MyApplication.getApp().getString(R.string.activities_reward);
                break;
            case 7:
                text = MyApplication.getApp().getString(R.string.promotion_rewards);
                break;
            case 8:
                text = MyApplication.getApp().getString(R.string.share_out_bonus);
                break;
            case 9:
                text = MyApplication.getApp().getString(R.string.vote);
                break;
            case 10:
                text = MyApplication.getApp().getString(R.string.artificial_top_up);
                break;
            case 11:
                text = MyApplication.getApp().getString(R.string.match_money);
                break;
            case 24:
                text = "挖宝";
                break;
        }
        tvTypeText.setText(text);
    }
}
