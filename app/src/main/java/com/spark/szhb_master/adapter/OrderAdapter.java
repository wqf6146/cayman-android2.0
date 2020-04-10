package com.spark.szhb_master.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.amulyakhare.textdrawable.TextDrawable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.order.OrderFragment;
import com.spark.szhb_master.entity.Order;
import com.spark.szhb_master.utils.MathUtils;
import com.spark.szhb_master.utils.VersionCompareUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2018/2/5.
 */

public class OrderAdapter extends BaseQuickAdapter<Order, BaseViewHolder> {
    private Context context;
    private OrderFragment.Status status;

    public OrderAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<Order> data, OrderFragment.Status status) {
        super(layoutResId, data);
        this.context = context;
        this.status = status;
    }

    @Override
    protected void convert(BaseViewHolder helper, Order item) {
        helper.setText(R.id.tvName, item.getName()).setText(R.id.tvType, (item.getType() == 0 ? context.getString(R.string.text_buy_in) : context.getString(R.string.text_sale_out)) + item.getUnit())
                .setText(R.id.tvCount, "："+MathUtils.getRundNumber(Double.valueOf(BigDecimal.valueOf(item.getAmount()).toString()), 8, null) + " "+item.getUnit()).setText(R.id.tvTotal, "："+item.getMoney() +" "+ "CNY").setText(R.id.tvState, status.getStatusStr());
        ;

        helper.setText(R.id.order_time,item.getCreateTime());
        helper.getView(R.id.tvType).setEnabled(item.getType() == 0);

//        if (StringUtils.isNotEmpty(item.getAvatar()))
//            x.image().bind((ImageView) helper.getView(R.id.ivHeader), item.getAvatar());
        String str = VersionCompareUtil.firstName(item.getName());

        TextDrawable textDrawable = TextDrawable.builder()
                .beginConfig()
                .width(40)  // width in px
                .height(40) // height in px
                .endConfig()
                .buildRect(str, context.getResources().getColor(R.color.textimage));

        helper.setImageDrawable(R.id.ivHeader, textDrawable);
    }
}
