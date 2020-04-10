package com.spark.szhb_master.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.dialog.ReceiveDialog;
import com.spark.szhb_master.dialog.ShiMingDialog;
import com.spark.szhb_master.entity.MyDigging;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.WonderfulStringUtils;
import com.spark.szhb_master.utils.okhttp.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Request;

import static com.spark.szhb_master.utils.okhttp.OkhttpUtils.post;

/**
 * Created by Administrator on 2018/9/18 0018.
 */
public class MyDiggingAdapter extends BaseQuickAdapter<MyDigging, BaseViewHolder> {


    private Context context;
    private ReceiveDialog receiveDialog;

    public MyDiggingAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<MyDigging> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MyDigging item) {
        receiveDialog = new ReceiveDialog(context);
        String html = context.getResources().getString(R.string.completion) + "：" + "<font color=#ff6e00>" + item.getCompleteAmount() + "</font>" + "/" + item.getDegree();
        helper.setText(R.id.tvLimit, Html.fromHtml(html));
//        helper.setText(R.id.tvLimit, "完成度：" + item.getCompleteAmount() + "/" + item.getDegree());
        if (!WonderfulStringUtils.isEmpty(item.getCover()))
            Glide.with(context).load(item.getCover())
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into((ImageView) helper.getView(R.id.image_child));
        helper.setText(R.id.comm_name, item.getCommodityName());
        helper.setText(R.id.tvNum, context.getResources().getString(R.string.number_participants) + "：" + item.getJoinAmount());

        if (item.getActivityStatus() == 1) {
            helper.setText(R.id.trea_btn, context.getResources().getString(R.string.digging_treasure));//正在挖宝
            helper.getView(R.id.trea_btn).setEnabled(false);
            helper.getView(R.id.trea_btn).setBackground(context.getResources().getDrawable(R.mipmap.zise_btn));
            helper.setTextColor(R.id.trea_btn, context.getResources().getColor(R.color.white));
        } else if (item.getActivityStatus() == 2) {
            helper.getView(R.id.trea_btn).setEnabled(false);
            helper.setText(R.id.trea_btn, context.getResources().getString(R.string.treasure_end));
        } else if (item.getActivityStatus() == 3) {
            helper.getView(R.id.trea_btn).setEnabled(false);
            helper.setText(R.id.trea_btn, context.getResources().getString(R.string.waiting_announcement));
        } else if (item.getActivityStatus() == 4) {
            helper.getView(R.id.trea_btn).setEnabled(true);
            helper.setTextColor(R.id.trea_btn, context.getResources().getColor(R.color.white));
            helper.setText(R.id.trea_btn, context.getResources().getString(R.string.waiting_collection));
            helper.getView(R.id.trea_btn).setBackground(context.getResources().getDrawable(R.mipmap.zise_btn));
            helper.setOnClickListener(R.id.trea_btn, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    receiveDialog.setEntrust(item.getCommodityName());
                    receiveDialog.show();
                }
            });
            receiveDialog.tvbtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    http();
                    receiveDialog.dismiss();
                }
            });

        }
//        1.正在挖宝，2.挖宝结束，3等待公布

    }

    private void http() {
        String token = MyApplication.getApp().getCurrentUser().getToken();
        post().url(UrlFactory.getSafeSettingUrl()).addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public boolean onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        int safeSetting = Integer.parseInt(object.getJSONObject("data").getString("realVerified").toString());
                        String name = object.getJSONObject("data").getString("realNameRejectReason").toString();
                        int  realAuditing = object.getJSONObject("data").getInt("realAuditing");
                        if (safeSetting == 0) {

                            ShiMingDialog shiMingDialog = new ShiMingDialog(context);
                            if (safeSetting == 0) {
                                if (realAuditing == 1) {
                                    shiMingDialog.setEntrust(7, name,0);
                                } else {
                                    if (name!= null)
                                        shiMingDialog.setEntrust(8, name,0);
                                    else
                                        shiMingDialog.setEntrust(9, name,0);
                                }
                            } else {
                                shiMingDialog.setEntrust(6, name,0);
                            }
                            shiMingDialog.show();
                        } else {
                            String url = "http://galaxychain.mikecrm.com/Uubnadu";
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            context.startActivity(intent);
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