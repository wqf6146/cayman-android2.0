package com.spark.szhb_master.activity.credit;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.Credit;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface CreditContract {
    interface View extends Contract.BaseView<Presenter> {

        void uploadBase64PicSuccess(String obj, int type);

        void doCreditSuccess(String obj);

        void getCreditInfoSuccess(Credit.DataBean obj);

        void doPostFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {

        void uploadBase64Pic(HashMap<String, String> params, int type);

        void credit(HashMap<String, String> params);

        void getCreditInfo();

        void uploadImageFile(File file, int type);
    }
}
