package com.spark.szhb_master.activity.wallet;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.Coin;
import com.spark.szhb_master.entity.GccMatch;
import com.spark.szhb_master.entity.SafeSetting;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface WalletContract {
    interface View extends Contract.BaseView<Presenter> {

        void myWalletSuccess(List<Coin> obj);

        void getCheckMatchSuccess(GccMatch obj);

        void getStartMatchSuccess(String obj);

        void doPostFail(Integer code, String toastMessage);

        void safeSettingSuccess(SafeSetting obj);
    }

    interface Presenter extends Contract.BasePresenter {

        void myWallet();

        void getCheckMatch();

        void getStartMatch(HashMap<String, String> params);

        void safeSetting();
    }
}
