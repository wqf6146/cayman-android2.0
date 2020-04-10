package com.spark.szhb_master.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.MatchHis;

import java.util.List;

/**
 * Created by Administrator on 2018/7/11 0011.
 */
public class MatchHisAdapter extends BaseQuickAdapter<MatchHis,BaseViewHolder> {
    public MatchHisAdapter(@LayoutRes int layoutResId, @Nullable List<MatchHis> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MatchHis item) {

        int red = ContextCompat.getColor(MyApplication.getApp(), R.color.kred);
        int green = ContextCompat.getColor(MyApplication.getApp(), R.color.kgreen);
        if (item.getAmount() > 0){
            helper.setTextColor(R.id.tv_num,green).setText(R.id.tv_kind,item.getSymbol())
                    .setText(R.id.tv_num,item.getAmount()+"").setText(R.id.tv_time,item.getCreateTime());
        }else{
//            helper.setTextColor(R.id.tvNum, red);
            helper.setTextColor(R.id.tv_num,red).setText(R.id.tv_kind,item.getSymbol())
                    .setText(R.id.tv_num,item.getAmount()+"").setText(R.id.tv_time,item.getCreateTime());
        }

    }
}
