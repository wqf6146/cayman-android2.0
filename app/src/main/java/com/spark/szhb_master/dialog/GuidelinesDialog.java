package com.spark.szhb_master.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;

/**
 * Created by Administrator on 2018/10/16 0016.
 */

public class GuidelinesDialog extends Dialog {

    private Context context;
    private View view_title;
    private GuidelinesDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 130;
        lp.width = (int) (MyApplication.getApp().getmWidth() * 0.8);
        dialogWindow.setGravity(Gravity.TOP);
        dialogWindow.setAttributes(lp);
    }

    public GuidelinesDialog(Context context) {
        super(context, R.style.myDialog);
        this.context = context;
        dialog = new GuidelinesDialog(context);
        dialog.setCancelable(false);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_guidelines, null);
        setContentView(view);
        view_title = view.findViewById(R.id.view_title);

    }


}
