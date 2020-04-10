package com.spark.szhb_master.activity.aboutus;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.AppInfo;
import com.spark.szhb_master.entity.Vision;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface AboutUsContract {

    interface View extends Contract.BaseView<Presenter> {

        void appInfoFail(Integer code, String toastMessage);

        void appInfoSuccess(AppInfo obj);
        void getNewVersionSuccess(Vision obj);
        void getNewVersionFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {
        void appInfo();
        void getNewVision();
    }
}
