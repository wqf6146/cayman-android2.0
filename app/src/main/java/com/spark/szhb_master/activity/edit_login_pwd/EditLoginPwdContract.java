package com.spark.szhb_master.activity.edit_login_pwd;


import com.spark.szhb_master.base.Contract;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface EditLoginPwdContract {
    interface View extends Contract.BaseView<Presenter> {

        void sendEditLoginPwdCodeSuccess(String obj);

        void editPwdSuccess(String obj);

        void doPostFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {

        void sendEditLoginPwdCode();

        void editPwd(HashMap<String, String> map);
    }
}
