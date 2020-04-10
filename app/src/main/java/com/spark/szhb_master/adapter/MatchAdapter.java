package com.spark.szhb_master.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.MyMacth;

import java.util.List;

/**
 * Created by Administrator on 2018/7/6 0006.
 */
public class MatchAdapter extends BaseQuickAdapter<MyMacth, BaseViewHolder> {

    public MatchAdapter(@LayoutRes int layoutResId, @Nullable List<MyMacth> data) {
        super(layoutResId, data);
    }



    @Override
    protected void convert(BaseViewHolder helper, MyMacth item) {

        helper.setText(R.id.tvKind,item.getCoins().getGCA().getCoin().getName());
    }
}
