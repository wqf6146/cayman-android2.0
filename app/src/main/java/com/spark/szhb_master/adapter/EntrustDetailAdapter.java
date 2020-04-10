package com.spark.szhb_master.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.EntrustHistory;
import com.spark.szhb_master.utils.DateUtils;
import com.spark.szhb_master.utils.MathUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 委托详情
 * author: wuzongjie
 * time  : 2018/4/25 0025 16:47
 * desc  :
 */

public class EntrustDetailAdapter extends BaseQuickAdapter<EntrustHistory.DetailBean, BaseViewHolder> {

    public EntrustDetailAdapter(@Nullable List<EntrustHistory.DetailBean> data) {
        super(R.layout.item_entrust_detail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntrustHistory.DetailBean item) {
        String[] times = DateUtils.getFormatTime(null, new Date(item.getTime())).split(" ");
        helper.setText(R.id.tvTime, times[0].substring(5, times[0].length()) + " " + times[1].substring(0, 5));
        helper.setText(R.id.tvPrice, String.valueOf(item.getPrice()));
        helper.setText(R.id.tvEntrustCount, new BigDecimal(String.valueOf(item.getAmount())).toString());
        helper.setText(R.id.tvFee, MathUtils.getRundNumber(item.getFee(), 4, null));
    }
}
