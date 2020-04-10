package com.spark.szhb_master.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.Address;
import com.spark.szhb_master.utils.StringUtils;

import java.util.List;

/**
 * 提币地址
 * Created by Administrator on 2018/3/8.
 */

public class AddressAdapter extends BaseQuickAdapter<Address, BaseViewHolder> {
    public AddressAdapter(int layoutResId, @Nullable List<Address> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Address item) {
        helper.setText(R.id.tvRemark, StringUtils.isEmpty(item.getRemark()) ? "无备注" : item.getRemark()).setText(R.id.tvAddress, item.getAddress());
    }
}
