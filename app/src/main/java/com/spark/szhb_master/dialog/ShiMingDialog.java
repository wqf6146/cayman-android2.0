package com.spark.szhb_master.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.credit.CreditSfzActivity;

/**
 * Created by Administrator on 2018/10/24 0024.
 */

public class ShiMingDialog extends Dialog {

    private ImageView img_close;
    private TextView cancle;
    private TextView go_up;
    private TextView cometwo;

    private Context context;
    private int count;


    public ShiMingDialog(Context context) {
        super(context, R.style.myDialog);
        this.context = context;
        initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (MyApplication.getApp().getmWidth() * 0.8);
        dialogWindow.setAttributes(lp);
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_shiming, null);
        setContentView(view);

        go_up = view.findViewById(R.id.go_up);
        cancle = view.findViewById(R.id.cancle);
        img_close = view.findViewById(R.id.img_close);
        cometwo = view.findViewById(R.id.cometwo);


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        go_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, CreditSfzActivity.class).putExtra("NoticeType",type).putExtra("Notice",name_type));
                dismiss();
            }
        });
    }

    private  int type ;
    private String name_type;

    public void setEntrust(int come, String name,int from) {
        type = come;
        name_type = name;
        if (from == 0){

        }else if (from == 1){
            cometwo.setText(context.getResources().getString(R.string.before_real));
        }
    }

}
