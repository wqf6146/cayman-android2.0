package com.spark.szhb_master.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.utils.StringUtils;

/**
 * Created by Administrator on 2018/8/22 0022.
 */

public class ReceiveDialog extends Dialog {

    private Context context;
    private ImageView img_close;
    private TextView tvLogin;
    private TextView text_receive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (MyApplication.getApp().getmWidth() * 0.8);
        dialogWindow.setAttributes(lp);
    }

    public ReceiveDialog(Context context) {
        super(context, R.style.myDialog);
        this.context = context;
        initView();
        setLisener();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_receive, null);
        setContentView(view);
        img_close = view.findViewById(R.id.img_close);
        tvLogin = view.findViewById(R.id.tvLogin);
        text_receive = view.findViewById(R.id.text_receive);
    }

    private void setLisener() {
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public TextView tvbtn() {
        return tvLogin;
    }


    public void setEntrust(String memberName) {
        if (StringUtils.isEmpty(memberName)){
            text_receive.setText(context.getResources().getString(R.string.receive_trea));
            tvLogin.setText(context.getResources().getString(R.string.my_digging));
        }else {
            tvLogin.setText(context.getResources().getString(R.string.fill_form));
            String html = context.getResources().getString(R.string.dug_it)+"<font color=#ff6e00>" +memberName+ "</font>" + context.getResources().getString(R.string.please_fill_out);
//            helper.setText(R.id.tvLimit, Html.fromHtml(html));
            text_receive.setText(Html.fromHtml(html));
        }
    }


}
