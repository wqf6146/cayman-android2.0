package com.spark.szhb_master.activity.ads;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.Ads;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface AdsContract {
    interface View extends Contract.BaseView<Presenter> {

        void allAdsSuccess(List<Ads> obj);

        void allAdsFail(Integer code, String toastMessage);

        void doOptionSucess(String obj);

        void doPostFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {
        void allAds(HashMap<String, String> params);

        void release(HashMap<String, String> params);

        void offShelf(HashMap<String, String> params);

        void delete(HashMap<String, String> params);
    }
}
