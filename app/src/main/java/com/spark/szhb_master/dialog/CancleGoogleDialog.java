package com.spark.szhb_master.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;

/**
 * Created by Administrator on 2018/9/5 0005.
 */

public class CancleGoogleDialog extends Dialog {

    TextView tvCancle;
    TextView tvConfirm;
    private Context context;

    public CancleGoogleDialog(@NonNull Context context) {
        super(context, R.style.myDialog);
        this.context = context;
        initView();
        setListener();
    }

    private void setListener() {
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) (MyApplication.getApp().getmWidth() * 0.8);
//        lp.width = (int) (MyApplication.getApp().getmWidth());
        window.setAttributes(lp);
    }

    public TextView getTvConfirm() {
        return tvConfirm;
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_goole_cancle, null);
        setContentView(view);
        tvCancle = view.findViewById(R.id.tvCancle);
        tvConfirm = view.findViewById(R.id.tvConfirm);
    }



}
