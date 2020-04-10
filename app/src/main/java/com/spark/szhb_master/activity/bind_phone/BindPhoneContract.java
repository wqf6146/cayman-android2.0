package com.spark.szhb_master.activity.bind_phone;


import com.spark.szhb_master.base.Contract;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface BindPhoneContract {
    interface View extends Contract.BaseView<Presenter> {
        void bindPhoneSuccess(String obj);

        void sendChangePhoneCodeSuccess(String obj);
        void sendNewChangePhoneCodeSuccess(String obj);

        void changePhoneSuccess(String obj);

        void sendCodeSuccess(String obj);

        void sendNewCodeSuccess(String obj);

        void doPostFail(Integer code, String toastMessage);

    }

    interface Presenter extends Contract.BasePresenter {
        void bindPhone(HashMap<String, String> params);

        void sendCode(HashMap<String, String> params);

        void sendNewCode(HashMap<String, String> params);

        void sendChangePhoneCode();

        void changePhone(HashMap<String, String> params);
        void newchangePhone(HashMap<String, String> params);
    }
}
