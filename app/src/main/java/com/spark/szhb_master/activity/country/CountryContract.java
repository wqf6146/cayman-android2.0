package com.spark.szhb_master.activity.country;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.Country;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface CountryContract {
    interface View extends Contract.BaseView<Presenter> {

        void countrySuccess(List<Country> obj);

        void countryFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {

        void country();
    }
}
