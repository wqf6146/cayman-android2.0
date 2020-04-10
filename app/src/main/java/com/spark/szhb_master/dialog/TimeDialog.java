package com.spark.szhb_master.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;

/**
 * Created by Administrator on 2018/9/21 0021.
 */

public class TimeDialog extends Dialog {

    private Context context;
    private TextView tv_time, tv_isee;
    private ImageView image_close;

    public TimeDialog(Context context) {
        super(context);
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
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_time, null);
        setContentView(view);
        tv_time = view.findViewById(R.id.tv_time);
        tv_isee = view.findViewById(R.id.tv_isee);
        image_close = view.findViewById(R.id.img_close);
        image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tv_isee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void setEntrust(String time, int type) {
        if (type == 1) {//无需dacall
            tv_time.setText(context.getResources().getString(R.string.not_need)+ "\n" + time +context.getResources().getString(R.string.directly_to));
        } else if (type == 2){
            tv_time.setText(context.getResources().getString(R.string.event_noyet) + "\n" +
                    context.getResources().getString(R.string.please) + time + context.getResources().getString(R.string.after_call));
        }else if (type == 3){
            tv_time.setText(context.getResources().getString(R.string.event_noyet) + "\n" +
                    context.getResources().getString(R.string.please) + time +context.getResources().getString(R.string.start_trea));
        }

    }
}
