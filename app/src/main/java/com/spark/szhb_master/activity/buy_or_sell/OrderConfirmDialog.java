package com.spark.szhb_master.activity.buy_or_sell;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;

/**
 * 确认是否下单
 * Created by Administrator on 2018/3/7.
 */

public class OrderConfirmDialog extends Dialog {
    TextView tvCancle;
    TextView tvPriceText;
    TextView tvPrice;
    TextView tvCountText;
    TextView tvCount;
    TextView tvTotalText;
    TextView tvTotal;
    TextView tvConfirm;
    LinearLayout llContent;
    private String type;
    private String price;
    private String count;
    private String total;
    private Context context;

    public TextView getTvConfirm() {
        return tvConfirm;
    }

    public OrderConfirmDialog(@NonNull Context context) {
        super(context, R.style.myDialog);
        this.context = context;
        initView();
        setListener();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
//        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) (MyApplication.getApp().getmWidth() * 0.8);
//        lp.width = (int) (MyApplication.getApp().getmWidth());
        window.setAttributes(lp);
    }

    public void setData(String type, String price, String count, String total) {
        this.type = type;
        this.price = price;
        this.count = count;
        this.total = total;
        initData();
    }


    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_order_confirm, null);
        setContentView(view);
        tvCancle = view.findViewById(R.id.tvCancle);
        tvPriceText = view.findViewById(R.id.tvPriceText);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvCountText = view.findViewById(R.id.tvCountText);
        tvCount = view.findViewById(R.id.tvCount);
        tvTotalText = view.findViewById(R.id.tvTotalText);
        tvTotal = view.findViewById(R.id.tvTotal);
        tvConfirm = view.findViewById(R.id.tvConfirm);
        llContent = view.findViewById(R.id.llContent);
    }

    private void initData() {
        if ("0".equals(type)) {
            tvPriceText.setText(context.getString(R.string.dialog_three_ones));
            tvCountText.setText(context.getString(R.string.dialog_three_twos));
            tvTotalText.setText(context.getString(R.string.dialog_three_threes));
        } else {
            tvPriceText.setText(context.getString(R.string.dialog_three_one));
            tvCountText.setText(context.getString(R.string.dialog_three_two));
            tvTotalText.setText(context.getString(R.string.dialog_three_three));
        }
        tvPrice.setText(price + "");
        tvCount.setText(count + "");
        tvTotal.setText(total + "");
    }

    private void setListener() {
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
