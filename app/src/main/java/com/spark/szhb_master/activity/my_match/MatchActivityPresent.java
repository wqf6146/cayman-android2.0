package com.spark.szhb_master.activity.my_match;


import com.google.gson.Gson;
import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.entity.GccMatch;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2018/7/6 0006.
 */

public class MatchActivityPresent implements MyMatchContract.Present {

    private final DataSource dataRepository;
    private final MyMatchContract.View view;
    String str;

    public MatchActivityPresent(DataSource dataRepository, MyMatchContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void myMatch(String token) {
        view.displayLoadingPopup();
        dataRepository.myMatch(token, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.myMatchSuccess(str);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.myMatchFail(code, toastMessage);

            }
        });
    }

    @Override
    public void getCheckMatch(String token, String symbol) {
        view.displayLoadingPopup();
        dataRepository.getCheckMatch(token, symbol,new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getCheckMatchSuccess((GccMatch) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getCheckMatchFail(code, toastMessage);
            }
        });
    }

    @Override
    public void getStartMatch(String token, String amount, String symbol) {
        view.displayLoadingPopup();
        dataRepository.getStartMatch(token, amount,symbol,new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getStartMatchSuccess((String) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getStartMatchFail(code, toastMessage);
            }
        });
    }

    @Override
    public void safeSetting() {
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
