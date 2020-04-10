package com.spark.szhb_master.activity.main.presenter;

import com.google.gson.Gson;
import com.spark.szhb_master.activity.main.MainContract;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.C2C;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2018/2/28.
 */

public class C2CListPresenterImpl implements MainContract.C2CListPresenter {
    private MainContract.C2CListView view;
    private DataSource dataRepository;

    public C2CListPresenterImpl(DataSource dataRepository, MainContract.C2CListView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void advertise(HashMap<String, String> params) {
        dataRepository.doStringPost(UrlFactory.getAdvertiseUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        C2C c2C = new Gson().fromJson(object.getJSONObject("data").toString(), C2C.class);
                        view.advertiseSuccess(c2C);
                    } else {
                        view.advertiseFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.advertiseFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.advertiseFail(JSON_ERROR, null);
            }
        });
    }

    @Override
    public void safeSetting(final int position) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getSafeSettingUrl(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        SafeSetting safeSetting = new Gson().fromJson(object.getJSONObject("data").toString(), SafeSetting.class);
                        view.safeSettingSuccess(safeSetting,position);
                    } else {
                        view.doPostFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doPostFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doPostFail(JSON_ERROR, null);
            }
        });
    }

}
