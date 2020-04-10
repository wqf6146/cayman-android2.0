package com.spark.szhb_master.activity.entrust;

import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.EntrustHistory;
import com.spark.szhb_master.entity.MarketSymbol;

import java.util.HashMap;
import java.util.List;

/**
 * author: wuzongjie
 * time  : 2018/4/18 0018 15:24
 * desc  :
 */
interface IAllTrustContract {

    interface View extends Contract.BaseView<IAllTrustContract.Presenter> {
        void errorMes(int e, String meg);

        void getAllSuccess(List<EntrustHistory> success);


        void getSymbolSucccess(List<MarketSymbol> objs);

        void doPostFail(int code, String message);
    }

    interface Presenter extends Contract.BasePresenter {

        void getAllEntrust(HashMap<String, String> params);


        void getSymbol();
    }
}


