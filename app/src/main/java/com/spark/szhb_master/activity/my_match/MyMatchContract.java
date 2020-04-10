package com.spark.szhb_master.activity.my_match;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.GccMatch;
import com.spark.szhb_master.entity.SafeSetting;

/**
 * Created by Administrator on 2018/4/18 0018.
 */

public interface MyMatchContract {

    interface View extends Contract.BaseView<Present>{
        void myMatchFail(Integer code, String toastMessage);

        void myMatchSuccess(String obj);
        void getCheckMatchSuccess(GccMatch obj);

        void getCheckMatchFail(Integer code, String toastMessage);

        void getStartMatchSuccess(String obj);

        void getStartMatchFail(Integer code, String toastMessage);

        void doPostFail(Integer code, String toastMessage);

        void safeSettingSuccess(SafeSetting obj);
    }

    interface Present extends Contract.BasePresenter{
        void myMatch(String token);

        void getCheckMatch(String token,String symbol);

        void getStartMatch(String token, String amount,String symbol);

        void safeSetting();
    }



}
