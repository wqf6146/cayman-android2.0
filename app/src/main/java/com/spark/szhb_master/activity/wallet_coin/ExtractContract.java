package com.spark.szhb_master.activity.wallet_coin;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.ExtractInfo;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface ExtractContract {
    interface View extends Contract.BaseView<Presenter> {

        void extractinfoSuccess(List<ExtractInfo> obj);

        void extractSuccess(String obj);

        void doPostFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {
        void extractinfo();

        void extract(HashMap<String, String> params);
    }
}
