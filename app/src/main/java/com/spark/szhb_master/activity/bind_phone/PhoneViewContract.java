package com.spark.szhb_master.activity.bind_phone;


import com.spark.szhb_master.base.Contract;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface PhoneViewContract {
    interface View extends Contract.BaseView<Presenter> {


        void changePhoneSuccess(String obj);

        void sendChangePhoneCodeSuccess(String obj);
        void findChangePhoneCodeSuccess(String obj);

        void doPostFail(Integer code, String toastMessage);


    }

    interface Presenter extends Contract.BasePresenter {

        void sendChangePhoneCode();

        void findChangePhoneCode(HashMap<String, String> params);


    }
}
