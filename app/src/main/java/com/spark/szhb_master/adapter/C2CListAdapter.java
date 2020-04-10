package com.spark.szhb_master.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.amulyakhare.textdrawable.TextDrawable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.dialog.ImgDialog;
import com.spark.szhb_master.entity.C2C;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.MathUtils;
import com.spark.szhb_master.utils.VersionCompareUtil;

import java.util.Arrays;
import java.util.List;

/**
 * c2c列表
 * Created by Administrator on 2018/2/28.
 */

public class C2CListAdapter extends BaseQuickAdapter<C2C.C2CBean, BaseViewHolder> {
    private Context context;
    private ImgDialog imgDialog;

    public C2CListAdapter(Context context, int layoutResId, @Nullable List<C2C.C2CBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final C2C.C2CBean item) {
        helper.setText(R.id.tvName, item.getMemberName()).setText(R.id.tvLimit, context.getString(R.string.text_quota) + ": " +MathUtils.getRundNumber(item.getMinLimit(), 2, null) + "~" + MathUtils.getRundNumber(item.getMaxLimit(), 2, null) +" "+ GlobalConstant.CNY)
                .setText(R.id.tvPrice, MathUtils.getRundNumber(item.getPrice(), 2, null) +" "+ GlobalConstant.CNY)
                .setText(R.id.tvNum, ": " + item.getRemainAmount() + " " + item.getUnit())
                .setText(R.id.tvBuy, ("0".equals(item.getAdvertiseType()) ?
                        context.getString(R.string.text_sale_out) : context.getString(R.string.text_buy_in)))
                .setText(R.id.tvTradeAmount, ": " + item.getTransactions() + "  |  " + item.getCompleteRate() + "%");
//               .setText(R.id.tvTradeAmount, context.getString(R.string.text_deal) + ": " + item.getTransactions()).setText(R.id.tvCount, context.getString(R.string.amount) + ":" + item.getRemainAmount());

//        Glide.with(getActivity().getApplicationContext()).load("").into(ivHeader);

        String str = VersionCompareUtil.firstName(item.getMemberName());
        TextDrawable textDrawable = TextDrawable.builder()
                .beginConfig()
                .width(40)  // width in px
                .height(40) // height in px
                .endConfig()
                .buildRect(str,context.getResources().getColor(R.color.textimage));
        helper.setImageDrawable(R.id.ivHeader, textDrawable);

//        if (StringUtils.isNotEmpty(item.getAvatar()))
//            Glide.with(context).load(item.getAvatar()).into((ImageView) helper.getView(R.id.ivHeader));
//        else
//            Glide.with(context).load(R.mipmap.icon_default_header_grey).into((ImageView) helper.getView(R.id.ivHeader));
        List<String> pays = Arrays.asList(item.getPayMode().split(","));
        if (pays.contains("支付宝")) helper.setVisible(R.id.ivZhifubao, true);
        else helper.setGone(R.id.ivZhifubao, false);
        if (pays.contains("微信")) helper.setVisible(R.id.ivWeChat, true);
        else helper.setGone(R.id.ivWeChat, false);
        if (pays.contains("银联") || pays.contains("银行卡")) helper.setVisible(R.id.ivUnionPay, true);
        else helper.setGone(R.id.ivUnionPay, false);

        imgDialog = new ImgDialog(context);
        helper.setOnClickListener(R.id.ivHeader, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgDialog != null) {
//                    imgDialog.setEntrust(item.getMemberName(),item.getTransactions()+"",item.getAdvertiseId());
                    imgDialog.setEntrust(item.getMemberName(), item.getTransactions() + "", item.getAdvertiseId(), item.getPhoneVerified(), item.getEmailVerified());
                    imgDialog.show();
                }
            }
        });

        helper.setOnClickListener(R.id.tvName, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgDialog != null) {
//                    imgDialog.setEntrust(item.getMemberName(),item.getTransactions()+"",item.getAdvertiseId());
                    imgDialog.setEntrust(item.getMemberName(), item.getTransactions() + "", item.getAdvertiseId(), item.getPhoneVerified(), item.getEmailVerified());
                    imgDialog.show();
                }
            }
        });
    }


}
