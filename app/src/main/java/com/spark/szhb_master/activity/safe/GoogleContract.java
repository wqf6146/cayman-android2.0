package com.spark.szhb_master.activity.safe;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.GoogleCode;
import com.spark.szhb_master.entity.SafeSetting;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/07/21
 */

public interface GoogleContract {
    interface View extends Contract.BaseView<Presenter> {

        void getInfoSuccess(GoogleCode obj);

        void getInfoFail(Integer code, String toastMessage);




    }

    interface Presenter extends Contract.BasePresenter {

        void getInfo();



    }

    interface UnBindView extends Contract.BaseView<UnBindPresenter> {

        void doUnbindSuccess(String obj);

        void doUnbindFail(Integer code, String toastMessage);

        void unBindCodeSuccess(String obj);

        void unBindCodeFail(Integer code, String toastMessage);

        void safeSettingSuccess(SafeSetting obj);

        void safeSettingFail(Integer code, String toastMessage);


    }

    interface UnBindPresenter extends Contract.BasePresenter {


        void doUnbind(HashMap<String, String> params);

        void unBindCode();

        void safeSetting();

    }

    interface BindView extends Contract.BaseView<BindPresenter> {

        void doBindSuccess(String obj);

        void doBindFail(Integer code, String toastMessage);

        void modifyvAlidateSuccess(String obj);

        void modifyvAlidateFail(Integer code, String toastMessage);

        void getbindcodeSuccess(String obj);
        void getbindcodeFail(Integer code, String toastMessage);

        void modifyvcodeSuccess(String obj);
        void modifyvcodeFail(Integer code, String toastMessage);

        void modifyvBindSuccess(String obj);
        void modifyvBindFail(Integer code, String toastMessage);

    }

    interface BindPresenter extends Contract.BasePresenter {

        void doBind(HashMap<String, String> params);

        void bindcode();

        void modifyvcode();

        void modifyvAlidate(HashMap<String, String> params);

        void modifyvBind(HashMap<String, String> params);



    }
}
