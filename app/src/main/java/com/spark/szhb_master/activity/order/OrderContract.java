package com.spark.szhb_master.activity.order;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.Order;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface OrderContract {
    interface View extends Contract.BaseView<Presenter> {

        void myOrderFail(Integer code, String toastMessage);

        void myOrderSuccess(List<Order> obj);
    }

    interface Presenter extends Contract.BasePresenter {
        void myOrder(HashMap<String, String> params);
    }
}
