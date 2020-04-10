package com.spark.szhb_master.activity.kline;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.DepthResult;
import com.spark.szhb_master.factory.UrlFactory;

import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class DepthPresenterImpl implements KlineContract.DepthPresenter {
    private final DataSource dataRepository;
    private final KlineContract.DepthView view;

    public DepthPresenterImpl(DataSource dataRepository, KlineContract.DepthView view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getDepth(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getDepth(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                try {
                    String response = (String) obj;
                    DepthResult result = new Gson().fromJson(response, new TypeToken<DepthResult>() {
                    }.getType());
                    view.getDepthtSuccess(result);
                } catch (Exception e) {
                    e.printStackTrace();
                    view.dpPostFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.dpPostFail(code, toastMessage);
            }
        });
    }
}
