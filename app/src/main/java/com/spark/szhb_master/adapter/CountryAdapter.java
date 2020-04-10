package com.spark.szhb_master.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.Country;
import com.spark.szhb_master.utils.SharedPreferenceInstance;

import java.util.List;

/**
 * 国家
 * Created by Administrator on 2018/3/1.
 */

public class CountryAdapter extends BaseQuickAdapter<Country, BaseViewHolder> {

    public CountryAdapter(int layoutResId, @Nullable List<Country> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Country item) {
        switch (SharedPreferenceInstance.getInstance().getLanguageCode()) {
            case 1:
                helper.setText(R.id.tvname, item.getZhName());
                break;
            case 2:
                helper.setText(R.id.tvname, item.getEnName());
                break;
            case 3:
                helper.setText(R.id.tvname, item.getJpLanguage());
                break;
        }

    }
}
