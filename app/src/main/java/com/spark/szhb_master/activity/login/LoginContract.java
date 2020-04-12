package com.spark.szhb_master.activity.login;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.User;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface LoginContract {
    interface View extends Contract.BaseView<Presenter> {

        //void captchSuccess(JSONObject obj);

        //void captchFail(Integer code, String toastMessage);

        void loginSuccess(User obj);

        void loginFail(Integer code, String toastMessage);

        //void googleLoginSuccess(User obj);

        //void googleLoginFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {
        void login(HashMap<String, String> params);

        void captch();

        void googleLogin(HashMap<String, String> params);
    }
}
