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

/**
 * Created by Administrator on 2018/7/16 0016.
 */

public class VersionDialogFragment extends Dialog {

    TextView tvConfirm_version;
    TextView tvCancle_version;
    TextView version_remark;
    TextView version_time;
    TextView version_tx;
    TextView version_tital;
    private Context context;

    public VersionDialogFragment(Context context) {
        super(context, R.style.myDialog);
        this.context = context;
        initView();
        setLisener();
    }

    private void setLisener() {

        tvCancle_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public TextView tvConfirm_version(){
        return  tvConfirm_version;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (MyApplication.getApp().getmWidth() * 0.8);
        dialogWindow.setAttributes(lp);


    }

    public void setEntrust(String version, String remark, String time) {
//        Bundle bundle = new Bundle();
//        bundle.putString("version", version);
//        bundle.putString("remark", remark);
//        bundle.putString("time", time);

        version_tx.setText("V"+ version);
//        version_tital.setText("Cayman"+ getArguments().getString("version"));

        version_time.setText("更新时间："+time.substring(0,10));
        version_remark.setText(remark.replace("\\n","\n"));
    }


//    public static VersionDialogFragment getInstance(String version, String remark, String time) {


    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_fragment_version, null);
        setContentView(view);

        tvConfirm_version = view.findViewById(R.id.tvConfirm_version);
        tvCancle_version = view.findViewById(R.id.tvCancle_version);
        version_remark = view.findViewById(R.id.version_remark);
        version_time = view.findViewById(R.id.version_time);
        version_tx = view.findViewById(R.id.version_tx);
        version_tital = view.findViewById(R.id.version_tital);
    }

}
