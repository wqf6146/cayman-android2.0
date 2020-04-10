package com.spark.szhb_master.activity.setting;

import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.Vision;

/**
 * Created by Administrator on 2018/4/24 0024.
 */

public class SettingContact {

    interface View extends Contract.BaseView<SettingContact.Presenter> {

        void getNewVisionSuccess(Vision obj);

        void getNewVisionFail(Integer code, String toastMessage);

    }

    interface Presenter extends Contract.BasePresenter {


    }
}
