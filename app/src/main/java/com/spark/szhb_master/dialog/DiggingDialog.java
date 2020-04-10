package com.spark.szhb_master.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;

import java.util.Timer;

/**
 * Created by Administrator on 2018/9/19 0019.
 */

public class DiggingDialog extends Dialog {

    private Context context;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (MyApplication.getApp().getmWidth() * 0.5);
        dialogWindow.setAttributes(lp);
    }


    public DiggingDialog(Context context) {
        super(context, R.style.myDialog);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_digging, null);
        setContentView(view);
    }

    public void setEntrust(int num) {
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
