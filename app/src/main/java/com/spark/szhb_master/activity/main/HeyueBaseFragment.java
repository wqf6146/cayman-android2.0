package com.spark.szhb_master.activity.main;

import android.content.Context;

import com.spark.szhb_master.base.BaseLazyFragment;
import com.spark.szhb_master.entity.Currency;

/**
 * Created by Administrator on 2018/2/26.
 */

public abstract class HeyueBaseFragment extends BaseLazyFragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(getActivity() instanceof HeyueOperateCallback)) {
            throw new RuntimeException("The Activity which this fragment is located must implement the MarketOperateCallback interface!");
        }
    }


    public interface HeyueOperateCallback {
        int TYPE_SWITCH_SYMBOL = 0;
        int TYPE_TO_KLINE = 1;
        void itemClick(Currency currency, int type);
    }

}
