package com.spark.szhb_master.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.Treasure.ShowTreaActivity;
import com.spark.szhb_master.entity.History;
import com.spark.szhb_master.entity.User;
import com.spark.szhb_master.utils.WonderfulStringUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/9/18 0018.
 */
public class HistoryAdapter extends BaseQuickAdapter<History, BaseViewHolder> {


    private Context context;
    private int from;

    public HistoryAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<History> data,int from) {
        super(layoutResId, data);
        this.context = context;
        this.from = from;
    }

    @Override
    protected void convert(BaseViewHolder helper, final History item) {
        if (from == 2){//dacall
            String html = context.getResources().getString(R.string.heat)+"："+"<font color=#f84f5c>" + (item.getVoteAmount()*10) + "";
            helper.setText(R.id.tvLimit,(Html.fromHtml(html)));
//            helper.setText(R.id.tvLimit, "热度值："+item.getVoteAmount());
            if (item.getJoinActivity() == 1){
                helper.setText(R.id.trea_btn, context.getResources().getString(R.string.selected));
                helper.setTextColor(R.id.trea_btn, context.getResources().getColor(R.color.blue_trea));
            }else {
                helper.setText(R.id.trea_btn, context.getResources().getString(R.string.over_one));
            }
        }else if(from == 1 || from == 3){
            String html = context.getResources().getString(R.string.completion)+"："+"<font color=#ff6e00>" +item.getCompleteAmount()+ "</font>" + "/" + item.getDegree();
            helper.setText(R.id.tvLimit,Html.fromHtml(html));
//            helper.setText(R.id.tvLimit, "完成度："+item.getCompleteAmount() +"/"+item.getDegree());
            User user = MyApplication.getApp().getCurrentUser();
            if (item.getLuckUserId() == user.getId()){
                helper.setText(R.id.trea_btn, context.getResources().getString(R.string.sun_drying));
                helper.setTextColor(R.id.trea_btn, context.getResources().getColor(R.color.white));
                helper.getView(R.id.trea_btn).setBackground(context.getResources().getDrawable(R.mipmap.zise_btn));
                helper.setOnClickListener(R.id.trea_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context,ShowTreaActivity.class).putExtra("img",item.getCover()));
                    }
                });

            }else {
                if (item.getActivityStatus() == 1) {
                    helper.setText(R.id.trea_btn, context.getResources().getString(R.string.digging_treasure));
                } else if (item.getActivityStatus() == 2) {
                    helper.setText(R.id.trea_btn, context.getResources().getString(R.string.over));
                } else if (item.getActivityStatus() == 3) {
                    helper.setText(R.id.trea_btn, context.getResources().getString(R.string.waiting_announcement));
                }
            }
        }
        if (!WonderfulStringUtils.isEmpty(item.getCover()))
            Glide.with(context).load(item.getCover()).into((ImageView) helper.getView(R.id.image_child));
        helper.setText(R.id.comm_name, item.getCommodityName());
        helper.setText(R.id.tvNum, context.getResources().getString(R.string.number_participants)+"："+item.getJoinAmount());
//        1.正在挖宝，2.挖宝结束，3等待公布
    }
}