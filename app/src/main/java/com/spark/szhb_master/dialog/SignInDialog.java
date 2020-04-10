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
 * Created by Administrator on 2018/10/9 0009.
 */

public class SignInDialog extends Dialog {

    private Context context;
    private TextView tv_suanli,name_title;
    private ImageView image_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (MyApplication.getApp().getmWidth() * 0.5);
        dialogWindow.setAttributes(lp);
    }

    public SignInDialog(Context context) {
        super(context, R.style.myDialog);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_sign_in, null);
        setContentView(view);
        tv_suanli = view.findViewById(R.id.tv_suanli);
        name_title = view.findViewById(R.id.name_title);
        image_title = view.findViewById(R.id.image_title);
    }

    public void setEntrust(int num) {
        if (num == 1){
        }else {
            tv_suanli.setVisibility(View.GONE);
            name_title.setText("已签到");
            image_title.setBackground(context.getResources().getDrawable(R.mipmap.empty_trea));
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                int progress = 0;
                while (System.currentTimeMillis() - startTime < 2000) {
                    try {
                        progress += 10;
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        dismiss();
                    }
                }
                dismiss();
            }
        }).start();

    }
}
