package com.spark.szhb_master.activity.releaseAd;

import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.AccountSetting;
import com.spark.szhb_master.entity.Ads;
import com.spark.szhb_master.entity.CoinInfo;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface ReleaseAdContract {
    interface View extends Contract.BaseView<Presenter> {

        void allSuccess(List<CoinInfo> obj);

        void createSuccess(String obj);

        void adDetailSuccess(Ads obj);

        void updateSuccess(String obj);

        void getAccountSettingSuccess(AccountSetting obj);

        void doPostFail(Integer code, String toastMessage);

    }

    interface Presenter extends Contract.BasePresenter {

        void all();

        void create(HashMap<String, String> params);

        void adDetail(HashMap<String, String> params);

        void updateAd(HashMap<String, String> params);

        void getAccountSetting();
    }
}
