package com.spark.szhb_master.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.Treasure.PreheatFrdActivity;

/**
 * Created by Administrator on 2018/9/20 0020.
 */

public class PreheaDialog extends Dialog {

    private Context context;
    private ImageView img_close;
    private TextView tv_frd;

    public PreheaDialog(Context context) {
        super(context);
        this.context = context;
        initView();
        setLisener();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_prehea, null);
        setContentView(view);
        tv_frd = view.findViewById(R.id.tv_frd);
        img_close = view.findViewById(R.id.img_close);
    }

    private void setLisener() {

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        //邀请好友
        tv_frd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, PreheatFrdActivity.class));
                dismiss();
            }
        });

    }


}
