package com.spark.szhb_master.activity.bind_email;


import com.spark.szhb_master.base.Contract;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface BindEmailContract {
    interface View extends Contract.BaseView<Presenter> {

        void bindEmailSuccess(String obj);


        void sendEmailCodeSuccess(String obj);


        void doPostFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {
        void sendEmailCode(HashMap<String, String> params);

        void bindEmail(HashMap<String, String> params);
    }
}
