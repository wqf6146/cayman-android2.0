package com.spark.szhb_master.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.entity.VolumeInfo;
import com.spark.szhb_master.utils.DateUtils;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.MathUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * 成交adapter
 * Created by daiyy on 2018/1/29.
 */

public class VolumeAdapter extends RecyclerView.Adapter<VolumeAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<VolumeInfo> objList;

    public VolumeAdapter(Context context, ArrayList<VolumeInfo> objList) {
        this.context = context;
        this.objList = objList;
    }

    public void setObjList(ArrayList<VolumeInfo> objList) {
        this.objList = objList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_volume, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        VolumeInfo volumeInfo = objList.get(position);
        if (volumeInfo.getTime() == -1) {
            holder.tvTime.setText("-- --");
        } else {
            holder.tvTime.setText(DateUtils.getFormatTime("HH:mm:ss", new Date(volumeInfo.getTime())));
        }
        String direct = volumeInfo.getDirection();
        if (direct == null) {
            holder.tvDirect.setText("-- --");
        } else {
            holder.tvDirect.setText(volumeInfo.getDirection().equals(GlobalConstant.SELL) ? context.getString(R.string.text_sale_out) :context.getString(R.string.text_buy_in));
            holder.tvDirect.setTextColor(volumeInfo.getDirection().equals(GlobalConstant.SELL) ? ContextCompat.getColor(MyApplication.getApp(), R.color.font_kline_depth_sell) : ContextCompat.getColor(MyApplication.getApp(), R.color.font_kline_depth_buy));
        }
        holder.tvPrice.setText(volumeInfo.getPrice() == -1 ? "-- --" : volumeInfo.getPrice() + "");
        holder.tvNumber.setText(volumeInfo.getAmount() == -1 ? "-- --" : MathUtils.getRundNumber(volumeInfo.getAmount(),
                4, null) + "");
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTime;
        private TextView tvDirect;
        private TextView tvPrice;
        private TextView tvNumber;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDirect = itemView.findViewById(R.id.tvDirect);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvNumber = itemView.findViewById(R.id.tvNumber);
        }


    }
}
