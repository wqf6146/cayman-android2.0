package com.spark.szhb_master.activity.account_pwd;


import com.spark.szhb_master.base.Contract;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface AccountPwdContract {
    interface View extends Contract.BaseView<Presenter> {

        void accountPwdSuccess(String obj);

        void editAccountPwdSuccess(String obj);

        void doPostFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {
        void accountPwd(HashMap<String, String> params);

        void editAccountPwd(HashMap<String, String> params);
    }

    interface ResetView extends Contract.BaseView<ResetPresenter> {

        void resetAccountPwdSuccess(String obj);

        void doPostFail(Integer code, String toastMessage);

        void resetAccountPwdCodeSuccess(String obj);

    }

    interface ResetPresenter extends Contract.BasePresenter {

        void resetAccountPwd(HashMap<String, String> params);

        void resetAccountPwdCode();
    }

}
