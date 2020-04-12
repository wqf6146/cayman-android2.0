package com.spark.szhb_master.activity.signup;


import com.spark.szhb_master.base.Contract;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface SignUpContract {
    interface View extends Contract.BaseView<Presenter> {

//        void codeSuccess(String obj);
//
//        void codeFail(Integer code, String toastMessage);
//
//        void captchSuccess(JSONObject obj);
//
//        void captchFail(Integer code, String toastMessage);

        void sighUpSuccess(String obj);

        void sighUpFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {

        void getCode(HashMap<String, String> params);

        void sighUp(String url, HashMap<String, String> params);

        void captch();
    }

}
