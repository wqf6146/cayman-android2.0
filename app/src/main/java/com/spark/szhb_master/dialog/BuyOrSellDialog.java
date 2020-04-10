package com.spark.szhb_master.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.releaseAd.PubAdsActivity;
import com.spark.szhb_master.entity.User;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.utils.okhttp.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Request;

import static com.spark.szhb_master.utils.okhttp.OkhttpUtils.post;

/**
 * 购买或者买入广告dialog
 * Created by Administrator on 2018/1/31.
 */

public class BuyOrSellDialog extends Dialog {
    private LinearLayout llSell;
    private LinearLayout llBuy;
    private TextView ivClose;
    private Context context;
    private User user;

    public BuyOrSellDialog(Context context) {
        super(context, R.style.myDialog);
        this.context = context;
        initView();
        setLisener();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.width = (int) (MyApplication.getApp().getmWidth() * 0.8);
        lp.width = (int) (MyApplication.getApp().getmWidth());
        dialogWindow.setAttributes(lp);
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_buy_or_sell_ads, null);
        setContentView(view);
        llSell = view.findViewById(R.id.llSell);
        llBuy = view.findViewById(R.id.llBuy);
        ivClose = view.findViewById(R.id.ivClose);

    }

    @Override
    protected void onStart() {
        super.onStart();
        user = MyApplication.getApp().getCurrentUser();
    }


    private void setLisener() {
        llBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBusiness(0);
            }
        });

        llSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBusiness(1);



            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    /**
     * @param intType 0-buy ，1-sell
     */
    private void doPubSellOrrBuy(int intType) {
        Bundle bundle = new Bundle();
        bundle.putString("type", intType == 0 ? GlobalConstant.BUY : GlobalConstant.SELL);
        Intent intent = new Intent(context, PubAdsActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
        dismiss();
    }

    private int memberLevel;
    private int certifiedBusinessStatus;

    private void getBusiness(final int type) {
        User user = MyApplication.getApp().getCurrentUser();

        post().url(UrlFactory.getBusinessUrl()).addHeader("x-auth-token", user.getToken()).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public boolean onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (!response.equals("")) {
                         memberLevel = object.getJSONObject("data").getInt("memberLevel");
                         certifiedBusinessStatus = object.getJSONObject("data").getInt("certifiedBusinessStatus");
                        if (memberLevel == 2 && certifiedBusinessStatus == 2) {
                            doPubSellOrrBuy(type);
                        } else {
//                            doPubSellOrrBuy(type);
                            ToastUtils.show("需要成为认证商家才能发布广告", Toast.LENGTH_SHORT);
                            dismiss();
                        }
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }
}
