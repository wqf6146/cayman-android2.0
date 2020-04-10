package com.spark.szhb_master.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;

/**
 * 确认是否下单
 * Created by Administrator on 2018/3/7.
 */

public class OrderCancleDialog extends Dialog {
    TextView tvCancle;
    TextView tvConfirm,cancle_rule;
    LinearLayout llContent;
    private Context context;
    private ImageView img_switch;
    private int i = 0;

    public TextView getTvConfirm() {
        return tvConfirm;
    }

    public TextView getTvCancel() {
        return tvCancle;
    }

    public OrderCancleDialog(@NonNull Context context) {
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
        initData();
    }


    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_order_cancle, null);
        setContentView(view);
        tvCancle = view.findViewById(R.id.tvCancle);
        cancle_rule = view.findViewById(R.id.cancle_rule);
        tvConfirm = view.findViewById(R.id.tvConfirm);
        llContent = view.findViewById(R.id.llContent);
        img_switch = view.findViewById(R.id.img_switch);
        SpannableStringBuilder builder = new SpannableStringBuilder(cancle_rule.getText().toString()+" ");
//        ForegroundColorSpan span = new ForegroundColorSpan(context.getResources().getColor(R.color.main_back_sel));
        ForegroundColorSpan span = new ForegroundColorSpan(Color.BLACK);
        builder.setSpan(span,0,5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        cancle_rule.setText(builder);
    }

    private void initData() {
    }

    private void setListener() {
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        img_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i == 0) {
                    i = 1;
                    img_switch.setImageResource(R.drawable.shape_sxy);
                    tvConfirm.setBackgroundResource(R.drawable.update_shape);
                    tvConfirm.setTextColor(context.getResources().getColor(R.color.white));
                    tvConfirm.setEnabled(true);
                } else if (i == 1) {
                    i = 0;
                    img_switch.setImageResource(R.drawable.shape_kxy);
                    tvConfirm.setBackgroundResource(R.drawable.ffive_shape);
                    tvConfirm.setTextColor(context.getResources().getColor(R.color.uneable_text));
                    tvConfirm.setEnabled(false);
                }
            }
        });
    }



}
