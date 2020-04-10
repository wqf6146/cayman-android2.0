package com.spark.szhb_master.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.entity.PromotionRecord;

import java.util.List;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class PromotionRecordAdapter extends BaseQuickAdapter<PromotionRecord, BaseViewHolder> {

    public PromotionRecordAdapter(int layoutResId, @Nullable List<PromotionRecord> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PromotionRecord item) {
        helper.setText(R.id.tvRegistrationTime, item.getCreateTime())
                .setText(R.id.tvUserName, item.getUsername());
        switch (item.getLevel()) {
            case 0:
                helper.setText(R.id.RecommendationLevel, MyApplication.getApp().getString(R.string.level_one));
                break;
            case 1:
                helper.setText(R.id.RecommendationLevel, MyApplication.getApp().getString(R.string.level_two));
                break;
            case 2:
                helper.setText(R.id.RecommendationLevel, MyApplication.getApp().getString(R.string.level_three));
                break;
            case 3:
                helper.setText(R.id.RecommendationLevel, MyApplication.getApp().getString(R.string.level_four));
                break;
        }
    }

}
