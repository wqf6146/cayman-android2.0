package com.spark.szhb_master.activity.myinfo;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.entity.User;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface MyInfoContract {

    interface View extends Contract.BaseView<Presenter> {

        void safeSettingSuccess(SafeSetting obj);

        void uploadBase64PicSuccess(User user);

        void getUserInfoSuccess(User user);

        void avatarSuccess(String obj);

        void doPostFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {

        void safeSetting();

        void uploadBase64Pic(HashMap<String, String> params);

        void avatar(HashMap<String, String> params);

        void uploadImageFile(File file);

        void getUserInfo();
    }
}
