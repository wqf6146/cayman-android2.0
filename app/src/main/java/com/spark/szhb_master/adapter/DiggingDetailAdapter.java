package com.spark.szhb_master.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.amulyakhare.textdrawable.TextDrawable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.DiggingDetail;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.VersionCompareUtil;

import java.util.List;

/**
 * Created by Administrator on 2018/9/17 0017.
 */
public class DiggingDetailAdapter extends BaseQuickAdapter<DiggingDetail, BaseViewHolder> {


    private Context context;

    public DiggingDetailAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<DiggingDetail> data) {
        super(layoutResId, data);
        this.context = context;
    }
    @Override
    protected void convert(BaseViewHolder helper, DiggingDetail item) {

//        String str = item.getUsername().substring(0, 1);
        String str = VersionCompareUtil.firstName(item.getUsername());
        if (StringUtils.isEmpty(str)){
            item.getUsername().substring(0, 2);
        }

        TextDrawable textDrawable = TextDrawable.builder()
                .beginConfig()
                .width(40)  // width in px
                .height(40) // height in px
                .endConfig()
                .buildRect(str,context.getResources().getColor(R.color.textimage));

        helper.setImageDrawable(R.id.ivHeader, textDrawable);
        helper.setText(R.id.tv_num,"+"+item.getPower()+" "+context.getResources().getString(R.string.completion_one));
        helper.setText(R.id.tv_name,item.getUsername());
    }
}
