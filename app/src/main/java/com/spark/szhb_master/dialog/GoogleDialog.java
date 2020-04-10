package com.spark.szhb_master.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.okhttp.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Request;

import static com.spark.szhb_master.utils.okhttp.OkhttpUtils.post;

/**
 * Created by Administrator on 2018/9/4 0004.
 */

public class GoogleDialog extends Dialog {

    private Context context;
    private TextView google_phone;
    private EditText google_yzm;
    private TextView google_send,tvGccConfirm;
    private EditText google_code;
    private ImageView img_close;
    private String mobile;
    private LinearLayout line_two;

    public GoogleDialog(Context context) {
        super(context, R.style.myDialog);
        this.context = context;
        initView();
        setListener();
        getPhone();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (MyApplication.getApp().getmWidth());
        dialogWindow.setAttributes(lp);
    }

    private void getPhone() {

        post().url(UrlFactory.getSafeSettingUrl()).addHeader("x-auth-token","").build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public boolean onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (!response.equals("")) {
                        mobile = object.getJSONObject("data").getString("mobilePhone");
                        String maskNumber = mobile.substring(0,3)+"****"+mobile.substring(7,mobile.length());
                        google_phone.setText(maskNumber);
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    private void setListener() {
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


    }
    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.google_dialog, null);
        setContentView(view);
        google_phone = view.findViewById(R.id.google_phone);
        google_send = view.findViewById(R.id.google_send);
        img_close = view.findViewById(R.id.img_close);
        google_code = view.findViewById(R.id.google_code);
        tvGccConfirm = view.findViewById(R.id.tvGccConfirm);


        line_two = view.findViewById(R.id.line_two);
        google_yzm = view.findViewById(R.id.google_yzm);
        line_two.setVisibility(View.GONE);
    }


    public TextView sendCode() {
        return google_send;
    }

    public TextView tvConfigm() {
        return tvGccConfirm;
    }

    public TextView google_phone() {

        return google_phone;
    }

    public EditText google_yzm() {

        return google_yzm;
    }
    public LinearLayout line_two() {
        return line_two;
    }

    public EditText getCode(){
        return google_code;
    }







}
