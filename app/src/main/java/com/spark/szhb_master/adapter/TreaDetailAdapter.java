package com.spark.szhb_master.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.amulyakhare.textdrawable.TextDrawable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.TreaDetail;
import com.spark.szhb_master.utils.VersionCompareUtil;

import java.util.List;

/**
 * Created by Administrator on 2018/9/17 0017.
 */
public class TreaDetailAdapter extends BaseQuickAdapter<TreaDetail, BaseViewHolder> {


    private Context context;

    public TreaDetailAdapter(Context context,@LayoutRes int layoutResId, @Nullable List<TreaDetail> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, TreaDetail item) {
        String str =  VersionCompareUtil.firstName(item.getUsername());
        TextDrawable textDrawable = TextDrawable.builder()
                .beginConfig()
                .width(40)  // width in px
                .height(40) // height in px
                .endConfig()
//                .buildRect(str,context.getResources().getColor(R.color.main_font_red_hover));
                .buildRect(str,context.getResources().getColor(R.color.textimage));
        helper.setImageDrawable(R.id.ivHeader, textDrawable);
        helper.setText(R.id.tv_num,"+"+(item.getAmount()*10)+" "+context.getResources().getString(R.string.heat));
        helper.setText(R.id.tv_name,item.getUsername());
        helper.setTextColor(R.id.tv_num,context.getResources().getColor(R.color.main_font_red_hover));
    }
}
