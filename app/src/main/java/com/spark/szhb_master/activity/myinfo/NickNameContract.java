package com.spark.szhb_master.activity.myinfo;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.User;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface NickNameContract {

    interface View extends Contract.BaseView<Presenter> {

        void getNikeNameSuccess();


        void doPostFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {

        void getNikeName(HashMap<String, String> params);

    }
}
