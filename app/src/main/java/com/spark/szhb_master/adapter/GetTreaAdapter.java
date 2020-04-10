package com.spark.szhb_master.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.Treaget;

import java.util.List;

/**
 * Created by Administrator on 2018/9/21 0021.
 */
public class GetTreaAdapter extends BaseQuickAdapter<Treaget, BaseViewHolder> {


    private Context context;

    public GetTreaAdapter(Context context,@LayoutRes int layoutResId, @Nullable List<Treaget> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Treaget item) {

        helper.setText(R.id.title_name,context.getResources().getString(R.string.date)+"："+item.getDate());
        helper.setText(R.id.title_tv,"恭喜"+item.getLuckUsername() + "在"+item.getActivityTitle()+"获得"+item.getCommodityName());



    }
}
