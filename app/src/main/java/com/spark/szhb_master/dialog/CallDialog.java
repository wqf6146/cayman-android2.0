package com.spark.szhb_master.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;

import java.util.Timer;

/**
 * Created by Administrator on 2018/9/19 0019.
 */

public class CallDialog extends Dialog {

    private Context context;
    private TextView tv_suanli,tv_num;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (MyApplication.getApp().getmWidth() * 0.5);
        dialogWindow.setAttributes(lp);
    }


    public CallDialog(Context context) {
        super(context, R.style.myDialog);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_call, null);
        setContentView(view);
        tv_suanli = view.findViewById(R.id.tv_suanli);
        tv_num = view.findViewById(R.id.tv_num);

    }

    public void setEntrust(int num,int suanli) {
        if (suanli == 0){
            tv_suanli.setVisibility(View.GONE);
        }else {
            tv_suanli.setText(context.getResources().getString(R.string.power)+"+" + suanli);
        }
        tv_num.setText(context.getResources().getString(R.string.heat)+"+"+(num*10));
//        timerStart();
        // 只呈现1s
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
