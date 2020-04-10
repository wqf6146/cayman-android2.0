package com.spark.szhb_master.activity.order;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.OrderDetial;

import java.util.HashMap;

import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface OrderDetailContract {
    interface View extends Contract.BaseView<Presenter> {

        void orderDetailSuccess(OrderDetial obj);

        void payDoneSuccess(String obj);

        void cancleSuccess(String obj);

        void releaseSuccess(String obj);

        void downloadSuccess(Response obj);

        void doPostFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {
        void orderDetail(HashMap<String, String> params);

        void payDone(HashMap<String, String> params);

        void cancle(HashMap<String, String> params);

        void release(HashMap<String, String> params);

        void doDownload(String url);
    }
}
