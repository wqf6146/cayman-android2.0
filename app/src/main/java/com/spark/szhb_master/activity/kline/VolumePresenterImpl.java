package com.spark.szhb_master.activity.kline;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.VolumeInfo;
import com.spark.szhb_master.factory.UrlFactory;

import java.util.ArrayList;
import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class VolumePresenterImpl implements KlineContract.VolumePresenter {
    private final DataSource dataRepository;
    private final KlineContract.VolumeView view;

    public VolumePresenterImpl(DataSource dataRepository, KlineContract.VolumeView view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getVolume(HashMap<String, String> params) {
        dataRepository.doStringPost(UrlFactory.getVolume(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                try {
                    String response = (String) obj;
                    ArrayList<VolumeInfo> volumeInfos = new Gson().fromJson(response, new TypeToken<ArrayList<VolumeInfo>>() {
                    }.getType());
                    view.getVolumeSuccess(volumeInfos);
                } catch (Exception e) {
                    e.printStackTrace();
                    view.dpPostFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.dpPostFail(code, toastMessage);
            }
        });
    }
}
