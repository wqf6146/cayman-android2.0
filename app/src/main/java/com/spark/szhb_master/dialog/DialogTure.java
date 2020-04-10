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
 * Created by Administrator on 2018/9/25 0025.
 */

public class DialogTure extends Dialog {

    private ImageView img_close;
    private TextView cancle;
    private TextView go_up;
    private TextView cometwo;
    private TextView name_title;

    private Context context;
    private int count ;


    public DialogTure(Context context) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_cay, null);
        setContentView(view);

        go_up = view.findViewById(R.id.go_up);
        cancle = view.findViewById(R.id.cancle);
        img_close = view.findViewById(R.id.img_close);
        cometwo = view.findViewById(R.id.cometwo);
        name_title = view.findViewById(R.id.name_title);
    }

    public TextView go_up() {
        return go_up;
    }

    public TextView cancle() {
        return cancle;
    }

    public ImageView img_close() {
        return img_close;
    }

    public TextView cometwo() {
        return cometwo;
    }

    public TextView name_title() {
        return name_title;
    }




}
