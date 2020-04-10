package com.spark.szhb_master.activity.buy_or_sell;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.AccountSetting;
import com.spark.szhb_master.entity.C2CExchangeInfo;
import com.spark.szhb_master.entity.SafeSetting;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface C2CBuyOrSellContract {
    interface View extends Contract.BaseView<Presenter> {

        void c2cInfoSuccess(C2CExchangeInfo obj);

        void c2cBuyOrSellSuccess(String obj);

        void doPostFail(Integer code, String toastMessage);

        void safeSettingSuccess(SafeSetting obj);

        void getAccountSettingSuccess(AccountSetting obj);
    }

    interface Presenter extends Contract.BasePresenter {
        void c2cInfo(HashMap<String, String> params);

        void c2cBuy(HashMap<String, String> params);

        void c2cSell(HashMap<String, String> params);

        void safeSetting();

        void getAccountSetting();
    }
}
