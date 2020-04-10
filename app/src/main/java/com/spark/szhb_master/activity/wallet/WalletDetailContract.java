package com.spark.szhb_master.activity.wallet;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.WalletDetail;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface WalletDetailContract {
    interface View extends Contract.BaseView<Presenter> {

        void allTransactionSuccess(List<WalletDetail> obj);

        void allTransactionFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {

        void allTransaction(HashMap<String, String> params);
    }
}
