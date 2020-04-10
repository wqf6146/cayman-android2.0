package com.spark.szhb_master.activity.Treasure;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.DiggingDetail;
import com.spark.szhb_master.entity.SafeSetting;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface DiggingDetailContract {

    interface View extends Contract.BaseView<Presenter> {

        void  getJoinDetailSuccess(List<DiggingDetail> list);

        void getCheckJoinSuccess(int min,int masx,int rate);

        void getJoinSuccess(Object obj);

        void doFiled(Integer code, String toastMessage);

        void safeSettingSuccess(SafeSetting obj);
    }

    interface Presenter extends Contract.BasePresenter {

        void getJoinDetail(HashMap<String, String> params);
        void getCheckJoin(HashMap<String, String> map);
        void getJoin(HashMap<String, String> map);

        void safeSetting();
    }
}
