package com.spark.szhb_master.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.C2C;
import com.spark.szhb_master.entity.C2CExchangeInfo;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.ui.AvatarImageView;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.okhttp.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Request;

import static com.spark.szhb_master.utils.okhttp.OkhttpUtils.post;

/**
 * Created by Administrator on 2018/8/22 0022.
 */

public class ImgDialog extends Dialog {

    private Context context;
    private ImageView img_close;
    private C2C.C2CBean c2CBean;
    private TextView tvName,tvExchangeCount;
    private AvatarImageView ivHeader;
    private ImageView name_dialog_img,email_dialog_img,phone_dialog_img;
    private TextView name_dialog,email_dialog,phone_dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (MyApplication.getApp().getmWidth() * 0.8);
        dialogWindow.setAttributes(lp);
    }

    public ImgDialog(Context context) {
        super(context, R.style.myDialog);
        this.context = context;
        initView();
        setLisener();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_img, null);
        setContentView(view);
        img_close = view.findViewById(R.id.img_close);
        tvName = view.findViewById(R.id.tvName);
        tvExchangeCount = view.findViewById(R.id.tvExchangeCount);
        ivHeader = view.findViewById(R.id.ivHeader);
        name_dialog_img = view.findViewById(R.id.name_dialog_img);
        email_dialog_img = view.findViewById(R.id.email_dialog_img);
        phone_dialog_img = view.findViewById(R.id.phone_dialog_img);
        name_dialog = view.findViewById(R.id.name_dialog);
        email_dialog = view.findViewById(R.id.email_dialog);
        phone_dialog = view.findViewById(R.id.phone_dialog);
    }

    private void setLisener() {
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void setEntrust(String memberName,String getTransactions,int id,int phone,int email) {
        tvName.setText(memberName);
        tvExchangeCount.setText( context.getString(R.string.text_deal_num_two)+"："+getTransactions);

        String str = memberName.substring(0,1);

        ivHeader.setTextColor(context.getResources().getColor(R.color.white));
        ivHeader.setTextAndColor(str, context.getResources().getColor(R.color.textimage));
//            if (!StringUtils.isEmpty(user.getAvatar()))
        Glide.with(context.getApplicationContext()).load("").into(ivHeader);

//        initInfo(id);

        if (email==0){
            email_dialog.setText(context.getString(R.string.email_over_no));
            email_dialog_img.setImageResource(R.mipmap.email_verification_no);
//                            email_dialog_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.email_verification_no));
        }
        if (phone==0){
            phone_dialog.setText(context.getString(R.string.phone_over_no));
//                            email_dialog_img.setBackground();
            phone_dialog_img.setImageResource(R.mipmap.phone_verification_no);
        }
        if (id==0){
            name_dialog.setText(context.getString(R.string.realname_no));
            name_dialog_img.setImageResource(R.mipmap.name_verification_no);
        }
    }

    private void initInfo(int id) {
        String token = MyApplication.getApp().getCurrentUser().getToken();
        if (StringUtils.isEmpty(token))return;
        post().url(UrlFactory.getC2CInfoUrl()).addHeader("x-auth-token",token).addParams("id", id+"").build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public boolean onResponse(String response) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    if (!response.equals("")) {
                        C2CExchangeInfo c2CExchangeInfo = new Gson().fromJson(object.getJSONObject("data").toString(), C2CExchangeInfo.class);
                        if (c2CExchangeInfo.getEmailVerified()==0){
                            email_dialog.setText(context.getString(R.string.email_over_no));
                            email_dialog_img.setImageResource(R.mipmap.email_verification_no);
//                            email_dialog_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.email_verification_no));
                        }
                        if (c2CExchangeInfo.getPhoneVerified()==0){
                            phone_dialog.setText(context.getString(R.string.phone_over_no));
//                            email_dialog_img.setBackground();
                            phone_dialog_img.setImageResource(R.mipmap.phone_verification_no);
                        }
                        if (c2CExchangeInfo.getIdCardVerified()==0){
                            name_dialog.setText(context.getString(R.string.realname_no));
                            name_dialog_img.setImageResource(R.mipmap.name_verification_no);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

    }
}
