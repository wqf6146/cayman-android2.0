package com.spark.szhb_master.activity.Treasure;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.PowerDetail;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface PowerContract {

    interface View extends Contract.BaseView<Presenter> {

        void  getPowerDetailSuccess(List<PowerDetail> list);

        void doFiled(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {

        void getPowerDetail(HashMap<String, String> params);

    }
}
