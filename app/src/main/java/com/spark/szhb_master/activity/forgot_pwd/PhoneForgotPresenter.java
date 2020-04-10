package com.spark.szhb_master.activity.forgot_pwd;


import com.spark.szhb_master.data.DataSource;
import com.spark.szhb_master.factory.UrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class PhoneForgotPresenter implements ForgotPwdContract.Presenter {
    private final DataSource dataRepository;
    private final ForgotPwdContract.View view;

    public PhoneForgotPresenter(DataSource dataRepository, ForgotPwdContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void forgotCode(String url, HashMap<String, String> params) {
        dataRepository.doStringPost(url, params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.forgotCodeSuccess(object.optString("message"));
                    } else {
                        view.forgotCodeFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.forgotCodeFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.forgotCodeFail(code, toastMessage);
            }
        });
    }

    @Override
    public void captch() {
        view.displayLoadingPopup();
        dataRepository.doStringPost(UrlFactory.getCaptchaUrl(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    view.captchSuccess((JSONObject) object);
                } catch (Exception e) {
                    view.captchFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.captchFail(code, toastMessage);
            }
        });
    }

    @Override
    public void doForget(HashMap<String, String> params) {
        dataRepository.doStringPost(UrlFactory.getForgotPwdUrl(), params, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                String response = (String) obj;
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.doForgetSuccess(object.optString("message"));
                    } else {
                        view.doForgetFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.doForgetFail(JSON_ERROR, null);
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.doForgetFail(code, toastMessage);
            }
        });
    }
}
