package com.spark.szhb_master.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.entity.Entrust;
import com.spark.szhb_master.utils.MathUtils;
import com.spark.szhb_master.utils.StringUtils;

import java.math.BigDecimal;

/**
 * 点击委托列表弹出的dialog
 * Created by Administrator on 2018/1/31.
 */

public class EntrustDialog extends Dialog {
    private TextView tvCancle; // 取消
    private TextView tvType; // 类别
    private TextView tvPrice; // 价格
    private TextView tvTotal; // 总额
    private TextView tvAmount; // 数量
    private TextView tvOption; // 撤单
    private TextView tvSecOption; // 撤单后买入
    private Entrust entrust;
    private Context context;
    private boolean isBuyEntrust;

    public EntrustDialog(Context context) {
        super(context, R.style.myDialog);
        this.context = context;
        initView();
        setListener();
    }

    public void setEntrust(Entrust entrust) {
        this.entrust = entrust;
        initData();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) (MyApplication.getApp().getmWidth());
        window.setAttributes(lp);
    }

    protected void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_entrust, null);
        setContentView(view);
        tvCancle = view.findViewById(R.id.tvCancle);
        tvType = view.findViewById(R.id.tvType);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvTotal = view.findViewById(R.id.tvTotal);
        tvAmount = view.findViewById(R.id.tvAmount);
        tvOption = view.findViewById(R.id.tvOption);
        tvSecOption = view.findViewById(R.id.tvSecOption);
    }

    private void initData() {
        if (StringUtils.isNotEmpty(entrust.getDirection())) {
            if (GlobalConstant.BUY.equals(entrust.getDirection())) {
                tvType.setText(context.getString(R.string.text_buy_in));
            } else {
                tvType.setText(context.getString(R.string.text_sale_out));
                tvType.setEnabled(false);
            }
        }

        if (StringUtils.isNotEmpty(entrust.getType())) {
            if (GlobalConstant.LIMIT_PRICE.equals(entrust.getType())) { // 限价
                tvPrice.setText(String.valueOf(entrust.getPrice() + entrust.getBaseSymbol()));
                tvAmount.setText(String.valueOf(BigDecimal.valueOf(entrust.getAmount()) + entrust.getCoinSymbol()));
                tvTotal.setText(String.valueOf(MathUtils.getRundNumber(entrust.getPrice() * entrust.getAmount(), 2, null)
                        + entrust.getBaseSymbol()));
            } else { // 市价
                tvPrice.setText(String.valueOf(context.getString(R.string.text_best_prices)));
                if (StringUtils.isNotEmpty(entrust.getDirection())) {
                    if (GlobalConstant.BUY.equals(entrust.getDirection())) {
                        tvAmount.setText(String.valueOf("--" + entrust.getCoinSymbol()));
                        tvTotal.setText(String.valueOf(entrust.getAmount() + entrust.getCoinSymbol()));
                    } else {
                        tvAmount.setText(String.valueOf(entrust.getAmount() + entrust.getCoinSymbol()));
                        tvTotal.setText(String.valueOf(MathUtils.getRundNumber(entrust.getPrice() * entrust.getAmount(), 2, null)
                                + entrust.getBaseSymbol()));
                    }
                }
            }
        }
    }

    private void setListener() {
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void setOnCancelOrder(View.OnClickListener listener) {
        tvOption.setOnClickListener(listener);
    }

}
