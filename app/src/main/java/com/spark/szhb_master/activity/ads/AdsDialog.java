package com.spark.szhb_master.activity.ads;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.Ads;
import com.spark.szhb_master.utils.GlobalConstant;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/6.
 */

public class AdsDialog extends Dialog {
    @BindView(R.id.tvLimit)
    TextView tvLimit;
    @BindView(R.id.tvRemainAmount)
    TextView tvRemainAmount;
    @BindView(R.id.tvEdit)
    TextView tvEdit;
    @BindView(R.id.tvReleaseAgain)
    TextView tvReleaseAgain;
    @BindView(R.id.tvDelete)
    TextView tvDelete;  @BindView(R.id.tvDismmis)
    TextView tvDismmis;
    @BindView(R.id.llContent)
    LinearLayout llContent;
    @BindView(R.id.ivClose)
    ImageButton ivClose;
    private Ads ads;
    private Context context;

    public AdsDialog(@NonNull Context context) {
        super(context, R.style.myDialog);
        this.context = context;
        initView();
    }


    public void setAds(Ads ads) {
        this.ads = ads;
        initData();
    }

    public TextView getTvEdit() {
        return tvEdit;
    }

    public TextView getTvReleaseAgain() {
        return tvReleaseAgain;
    }

    public TextView getTvDelete() {
        return tvDelete;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) (MyApplication.getApp().getmWidth());
        window.setAttributes(lp);
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_ads, null);
        setContentView(view);
        tvLimit = view.findViewById(R.id.tvLimit);
        tvRemainAmount = view.findViewById(R.id.tvRemainAmount);
        tvEdit = view.findViewById(R.id.tvEdit);
        tvReleaseAgain = view.findViewById(R.id.tvReleaseAgain);
        tvDelete = view.findViewById(R.id.tvDelete);
        llContent = view.findViewById(R.id.llContent);
        tvDismmis = view.findViewById(R.id.tvDismmis);
        ivClose = view.findViewById(R.id.ivClose);
        setLisenter();
    }

    private void initData() {
        tvLimit.setText(ads.getMinLimit() + "~" + ads.getMaxLimit() + " " + GlobalConstant.CNY);
        tvRemainAmount.setText(ads.getRemainAmount() + " " + ads.getCoin().getUnit());
        tvReleaseAgain.setText(ads.getStatus() == 0 ? context.getString(R.string.shelves) : context.getString(R.string.up_shelves));
        tvEdit.setVisibility(ads.getStatus() == 0 ? View.GONE : View.VISIBLE);
        tvDelete.setVisibility(ads.getStatus() == 0 ? View.GONE : View.VISIBLE);
        tvDismmis.setVisibility(ads.getStatus() == 0 ? View.VISIBLE : View.GONE);

    }

    private void setLisenter() {
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tvDismmis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

}
