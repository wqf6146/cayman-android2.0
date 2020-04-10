package com.spark.szhb_master.widget;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.MyApplication;


public class TimeCount extends CountDownTimer {
    private TextView codeView;

    public TimeCount(long millisInFuture, long countDownInterval, TextView codeView) {
        super(millisInFuture, countDownInterval);
        this.codeView = codeView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        codeView.setBackgroundResource(R.drawable.shape_bg_send_code_pressed);
        codeView.setClickable(false);
//        codeView.setText("(" + millisUntilFinished / 1000 + ") 秒后可重新发送");
        codeView.setText("(" + millisUntilFinished / 1000 + ") 秒");
        codeView.setTextColor(MyApplication.getApp().getResources().getColor(R.color.grey_a5a5a5));
    }

    @Override
    public void onFinish() {
        codeView.setText("发送验证码");
        codeView.setClickable(true);
        codeView.setBackgroundResource(R.drawable.shape_bg_send_code_normal);
        codeView.setTextColor(MyApplication.getApp().getResources().getColor(R.color.btn_normal));

    }
}