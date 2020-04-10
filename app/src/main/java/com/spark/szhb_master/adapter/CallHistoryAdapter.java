package com.spark.szhb_master.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spark.szhb_master.R;
import com.spark.szhb_master.entity.CallHistory;

import java.util.List;

/**
 * Created by Administrator on 2018/9/18 0018.
 */
public class CallHistoryAdapter extends BaseQuickAdapter<CallHistory, BaseViewHolder> {


        private Context context;

        public CallHistoryAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<CallHistory> data) {
            super(layoutResId, data);
            this.context = context;
        }


        @Override
        protected void convert(BaseViewHolder helper, CallHistory item) {
            helper.setText(R.id.tvCommainname,item.getActivityTitle() + "("+item.getCommodityName()+")");
            helper.setText(R.id.tv_num,"+"+(item.getAmount()*10)+context.getResources().getString(R.string.heat));
            helper.setText(R.id.tv_name,item.getCreateTime());
        }
}
