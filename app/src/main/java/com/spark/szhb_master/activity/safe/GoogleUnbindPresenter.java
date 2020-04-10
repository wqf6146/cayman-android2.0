package com.spark.szhb_master.activity.safe;


import com.google.gson.Gson;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class GoogleUnbindPresenter implements GoogleContract.UnBindPresenter {
    private final DataSource dataRepository;
    private final GoogleContract.UnBindView view;

    public GoogleUnbindPresenter(DataSource dataRepository, GoogleContract.UnBindView view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }


    @Override
    public void doUnbind(HashMap<String, String> params) {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getUnBindGoogleCode(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    int code = object.optInt("code");
                    if (code == 0) {
                        view.doUnbindSuccess(object.optString("message"));
                    } else {
                        view.doUnbindFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.doUnbindFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.doUnbindFail(code, toastMessage);
            }
        });
    }

    @Override
    public void unBindCode() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.unbindCode(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    int code = object.optInt("code");
                    if (code == 0) {
                        view.unBindCodeSuccess(object.optString("message"));
                    } else {
                        view.unBindCodeFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.unBindCodeFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.unBindCodeFail(code, toastMessage);
            }
        });
    }

    @Override
    public void safeSetting() {
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
                        view.safeSettingSuccess(safeSetting);
                    } else {
                        view.safeSettingFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.safeSettingFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.safeSettingFail(JSON_ERROR, null);
            }
        });
    }
}
