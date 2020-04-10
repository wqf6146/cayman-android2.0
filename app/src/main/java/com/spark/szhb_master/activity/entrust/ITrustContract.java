package com.spark.szhb_master.activity.entrust;

import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.Entrust;
import com.spark.szhb_master.entity.MarketSymbol;

import java.util.HashMap;
import java.util.List;

/**
 * author: wuzongjie
 * time  : 2018/4/18 0018 11:21
 * desc  :
 */

public class ITrustContract {
    interface View extends Contract.BaseView<ITrustContract.Presenter> {

        void doPostFail(int e, String meg);

        void getCurrentOrderSuccess(List<Entrust> objs);
        void getSymbolSucccess(List<MarketSymbol> objs);

        void cancelOrderSuccess(String message);
    }

    interface Presenter extends Contract.BasePresenter {
        void getCurrentOrder(HashMap<String, String> params);

        void cancelOrder(String orderId);
        void getSymbol();

    }
}
