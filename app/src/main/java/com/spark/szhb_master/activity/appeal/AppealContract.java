package com.spark.szhb_master.activity.appeal;


import com.spark.szhb_master.base.Contract;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface AppealContract {
    interface View extends Contract.BaseView<Presenter> {

        void uploadBase64PicSuccess(String obj, int type);
        void appealSuccess(String obj);

        void appealFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {

        void appeal(HashMap<String, String> params);

        void uploadBase64Pic(HashMap<String, String> params, int type);

        void uploadImageFile(File file, int type);
    }
}
