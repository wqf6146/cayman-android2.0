package com.spark.szhb_master.base;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface ContractGet {
    interface BasePresenter {
        void doStringGet(String params);
    }

    interface BaseView<T> {
        void setPresenter(T presenter);

        void hideLoadingPopup();

        void displayLoadingPopup();

        void doGetSuccess(String obj);

        void doGetFail(Integer code, String toastMessage);

    }
}
