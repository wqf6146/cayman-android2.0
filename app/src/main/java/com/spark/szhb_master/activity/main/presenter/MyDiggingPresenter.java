package com.spark.szhb_master.activity.main.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.MyDigging;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2018/9/17 0017.
 */

public class MyDiggingPresenter implements TreasureContract.MyDiggingPresenter {

    private final DataSource dataRepository;
    private final TreasureContract.MyDiggingView view;

    public MyDiggingPresenter(DataSource dataRepository, TreasureContract.MyDiggingView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        view.setPresenter(this);
    }

    @Override
    public void getUsermyList() {
        dataRepository.doStringPost(UrlFactory.getUserList(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<MyDigging> preheatinginfos = new Gson().fromJson(object.getJSONArray("data").toString(), new TypeToken<List<MyDigging>>() {
                        }.getType());
                        view.getUserListmySuccess(preheatinginfos);
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
