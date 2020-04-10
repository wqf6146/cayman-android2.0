package com.spark.szhb_master.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.amulyakhare.textdrawable.TextDrawable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.entity.Ads;
import com.spark.szhb_master.utils.MathUtils;
import com.spark.szhb_master.utils.VersionCompareUtil;

import java.util.List;

/**
 * 广告
 * Created by Administrator on 2018/2/5.
 */

public class AdsAdapter extends BaseQuickAdapter<Ads, BaseViewHolder> {

    private String username;
    private String avatar;
    private Context context;

    public AdsAdapter(@LayoutRes int layoutResId, @Nullable List<Ads> data, String username, String avatar, Context context) {
        super(layoutResId, data);
        this.username = username;
        this.avatar = avatar;
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, Ads item) {
        helper.setText(R.id.tvName, username)
                .setText(R.id.tvStatus, item.getStatus() == 0 ? MyApplication.getApp().getString(R.string.grounding) : MyApplication.getApp().getString(R.string.shelved))
                .setText(R.id.tvType, item.getAdvertiseType() == 0 ? MyApplication.getApp().getString(R.string.text_buy) : MyApplication.getApp().getString(R.string.text_sell))
                .setText(R.id.tvPrice, "："+MathUtils.getRundNumber(item.getRemainAmount(), 1, null) + item.getCoin().getUnit())
                .setText(R.id.tvLimit, "："+ item.getMinLimit() + "~" + item.getMaxLimit() + item.getCountry().getLocalCurrency());

        helper.setText(R.id.order_time,item.getCreateTime());

        helper.getView(R.id.tvStatus).setEnabled(item.getStatus() == 0);

        String str = VersionCompareUtil.firstName(username);
        TextDrawable textDrawable = TextDrawable.builder()
                .beginConfig()
                .width(40)  // width in px
                .height(40) // height in px
                .endConfig()
                .buildRect(str,  context.getResources().getColor(R.color.textimage));

        helper.setImageDrawable(R.id.ivHeader, textDrawable);

//        Glide.with(context).load(avatar).asBitmap().placeholder(R.mipmap.icon_default_header_grey).centerCrop().into
//                (new BitmapImageViewTarget((ImageView) helper.getView(R.id.ivHeader)) {
//                    @Override
//                    protected void setResource(Bitmap resource) {
//                        RoundedBitmapDrawable circularBitmapDrawable =
//                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
//                        circularBitmapDrawable.setCircular(true);
//                        ((ImageView) helper.getView(R.id.ivHeader)).setImageDrawable(circularBitmapDrawable);
//                    }
//                });
    }
}
