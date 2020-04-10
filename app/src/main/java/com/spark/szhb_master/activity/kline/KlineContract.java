package com.spark.szhb_master.activity.kline;


import com.spark.szhb_master.base.Contract;
import com.spark.szhb_master.entity.Currency;
import com.spark.szhb_master.entity.DepthResult;
import com.spark.szhb_master.entity.VolumeInfo;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/1/17.
 */

public interface KlineContract {

    interface View extends Contract.BaseView<Presenter> {

        void KDataSuccess(JSONArray obj);

        void allCurrencySuccess(List<Currency> obj);

        void doDeleteOrCollectSuccess(String msg);

        void dpPostFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {

        void KData(HashMap<String, String> map);

        void allCurrency();

        void doCollect(HashMap<String, String> map);

        void doDelete(HashMap<String, String> map);

    }

    interface DepthView extends Contract.BaseView<DepthPresenter> {

        void getDepthtSuccess(DepthResult depthResult);

        void dpPostFail(Integer code, String toastMessage);
    }

    interface DepthPresenter extends Contract.BasePresenter {

        void getDepth(HashMap<String, String> map);

    }

    interface VolumeView extends Contract.BaseView<VolumePresenter> {

        void getVolumeSuccess(ArrayList<VolumeInfo> list);

        void dpPostFail(Integer code, String toastMessage);
    }

    interface VolumePresenter extends Contract.BasePresenter {

        void getVolume(HashMap<String, String> map);

    }
}
