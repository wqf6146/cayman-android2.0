package com.spark.szhb_master.activity.Treasure;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.PowerDetail;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class PowerPresenter implements PowerContract.Presenter {


    private final DataSource dataRepository;
    private final PowerContract.View view;

    public PowerPresenter(DataSource dataRepository, PowerContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getPowerDetail(HashMap<String, String> params) {
        dataRepository.doStringPost(UrlFactory.getPowerDetail(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<PowerDetail>list = new Gson().fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<PowerDetail>>() {
                        }.getType());
                        view.getPowerDetailSuccess(list);
                    } else {
                        view.doFiled(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doFiled(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doFiled(code, toastMessage);
            }
        });
    }
}
