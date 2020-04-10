package com.spark.szhb_master.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.spark.szhb_master.R;

/**
 * Created by Administrator on 2018/9/20 0020.
 */

public class ShowDialog extends Dialog {

    private Context context;
    private TextView text_one,text_two;

    public ShowDialog(Context context) {
        super(context, R.style.myDialog);
        this.context = context;
        initView();
    }


    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_show, null);
        setContentView(view);

        text_one = view.findViewById(R.id.text_one);
        text_two = view.findViewById(R.id.text_two);



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
    public void setEntrust(int num) {
        if (num == 1){
            text_one.setText("啊哦！该商品打Call失败喽，");
            text_two.setText("赶紧去为你喜欢的商品打Call吧");
        }

    }

}
